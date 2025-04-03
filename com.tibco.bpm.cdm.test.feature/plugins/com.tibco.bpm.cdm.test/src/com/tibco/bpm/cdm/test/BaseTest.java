package com.tibco.bpm.cdm.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.TransientPersistenceException;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.test.BaseTest.CLFContextBeginningDependencyInjectionTestExecutionListener;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.logging.cloud.context.CLFThreadContext;
import com.tibco.n2.common.security.CurrentUser;
import com.tibco.n2.common.security.RequestContext;

@TestExecutionListeners(inheritListeners = false, listeners = {
		CLFContextBeginningDependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/com/tibco/bpm/cdm/test/config/cdmBeans-test.xml"})
public abstract class BaseTest extends AbstractTestNGSpringContextTests
{
	// Exclusive lock wait time (seconds)
	protected static final int			WAIT									= 300000;										// mS for 5 minutes

	protected static final ObjectMapper	om										= new ObjectMapper();

	private static final String			SQL_DELETE_ALL_CASES_BY_DEPLOYMENT_ID	= "DELETE FROM cdm_cases_int WHERE type_id IN "
			+ "(SELECT id FROM cdm_types WHERE datamodel_id IN "
			+ "(SELECT id FROM cdm_datamodels WHERE application_id IN"
			+ "(SELECT id FROM cdm_applications WHERE deployment_id=?)))";

	private static final String			SQL_DELETE_LINKS_BY_DEPLOYMENT_ID		= "DELETE FROM cdm_case_links WHERE link_id IN"
			+ "(SELECT id FROM cdm_links WHERE end1_owner_id IN " + "(SELECT id FROM cdm_types WHERE datamodel_id IN "
			+ "(SELECT id FROM cdm_datamodels WHERE application_id IN"
			+ "(SELECT id FROM cdm_applications WHERE deployment_id=?))) " + "OR end2_owner_id IN "
			+ "(SELECT id FROM cdm_types WHERE datamodel_id IN "
			+ "(SELECT id FROM cdm_datamodels WHERE application_id IN"
			+ "(SELECT id FROM cdm_applications WHERE deployment_id=?))))";

	@Autowired
	@Qualifier("dataSource")
	private DataSource					dataSource;

	@Autowired
	@Qualifier("deploymentManager")
	protected DeploymentManager			deploymentManager;

	private static int					nextDeploymentId						= 1;

	@BeforeMethod
	public void beforeMethod()
	{
		RequestContext.setCurrent(new RequestContext(null, null, new CurrentUser("tibco-admin", null, null)));
	}

	// If we do this in a beforeTest, it runs _before_ Spring initialisation (so fails as CLF isn't bootstrapped).
	// We could do it in beforeMethod, but that has the side-effect of it running before _every_ method, which is a waste of time.
	// This is a way of getting the CLF beginContext done at the appropriate time (and only once).
	public static class CLFContextBeginningDependencyInjectionTestExecutionListener
			extends DependencyInjectionTestExecutionListener
	{
		@Override
		protected void injectDependencies(TestContext testContext) throws Exception
		{
			super.injectDependencies(testContext);
			CLFThreadContext.beginContext(CLFThreadContext.RECORD_LOG_MESSAGES.OFF);
		}
	}

	protected void releaseConnection(Connection conn)
	{
		DataSourceUtils.releaseConnection(conn, dataSource);
	}

	/**
	 * Performs generic clean-up of the given resources. Called following a database interaction.
	 * @param ts
	 * @param ps
	 * @param conn
	 */
	protected void cleanUp(Statement ts, PreparedStatement ps, Connection conn)
	{
		if (ts != null)
		{
			try
			{
				ts.execute("RESET statement_timeout");
				ts.close();
			}
			catch (SQLException e)
			{
				// TODO log
				System.out.println(e);
			}
		}
		if (ps != null)
		{
			try
			{
				ps.close();
			}
			catch (SQLException e)
			{
				// TODO log
				System.out.println(e);
			}
		}

		if (conn != null)
		{
			releaseConnection(conn);
		}
	}

	protected Connection getConnection() throws PersistenceException
	{
		try
		{
			Connection conn = DataSourceUtils.getConnection(dataSource);
			if (conn == null)
			{
				throw TransientPersistenceException.newNotConnected();
			}
			return conn;
		}
		catch (CannotGetJdbcConnectionException e)
		{
			throw TransientPersistenceException.newNotConnected(e);
		}
	}

	protected BigInteger deploy(String appResource) throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException
	{
		// Deploy application
		File file = FileUtils.buildZipFromFolderURL(BaseTest.class.getResource(appResource), true);
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId = BigInteger.valueOf(nextDeploymentId++);
		runtimeApplication.setID(deploymentId);

		deploymentManager.deploy(runtimeApplication);
		return deploymentId;
	}

	//deployrasc

	protected BigInteger deployRASC(String appResource) throws IOException, URISyntaxException,
			RuntimeApplicationException, DeploymentException, PersistenceException, InternalException
	{
		// Deploy application

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZipResource(appResource);

		BigInteger deploymentId = BigInteger.valueOf(nextDeploymentId++);
		runtimeApplication.setID(deploymentId);

		deploymentManager.deploy(runtimeApplication);
		return deploymentId;

	}

	protected void undeploy(BigInteger deploymentId) throws IOException, URISyntaxException,
			RuntimeApplicationException, DeploymentException, PersistenceException, InternalException
	{
		deploymentManager.undeploy(deploymentId);
	}

	protected void deleteAllCases(BigInteger deploymentId) throws PersistenceException
	{
		PreparedStatement ps = null;
		Statement ts = null;
		Connection conn = null;
		try
		{
			conn = getConnection();
			ts = conn.createStatement();
			ts.execute("SET statement_timeout TO " + String.valueOf(WAIT));

			// Delete rows from cdm_case_links where either end refers to this deployment
			ps = conn.prepareStatement(SQL_DELETE_LINKS_BY_DEPLOYMENT_ID);
			ps.setBigDecimal(1, new BigDecimal(deploymentId));
			ps.setBigDecimal(2, new BigDecimal(deploymentId));
			int count = ps.executeUpdate();
			ps.close();
			System.out.println(String.format("%s links deleted for deployment id %s", count, deploymentId));
			ps = conn.prepareStatement(SQL_DELETE_ALL_CASES_BY_DEPLOYMENT_ID);
			ps.setBigDecimal(1, new BigDecimal(deploymentId));
			count = ps.executeUpdate();
			System.out.println(String.format("%s cases deleted for deployment id %s", count, deploymentId));
		}
		catch (SQLException e)
		{
			throw PersistenceException.newRepositoryProblem(e);
		}
		finally
		{
			cleanUp(ts, ps, conn);
		}

	}

	protected void forceUndeploy(BigInteger deploymentId) throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		deleteAllCases(deploymentId);
		undeploy(deploymentId);
	}
}
