package com.tibco.bpm.cdm.yy.rest.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.TransientPersistenceException;
import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.cdm.util.DateTimeParser;
import com.tibco.bpm.cdm.yy.rest.api.ToolsService;
import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;

public class ToolsServiceImpl implements ToolsService
{
	static CLFClassContext		logCtx							= CloudLoggingFramework.init(ToolsServiceImpl.class,
			CDMLoggingInfo.instance);																							// Params: new timestamp, id, type name, namespace, major_version
	// Exclusive lock wait time (seconds)

	protected static final int	WAIT							= 300000;														// mS for 5 minutes

	private static final String	SQL_SET_MODIFICATION_TIMESTAMP	= "UPDATE cdm_cases_int SET modification_timestamp = ? WHERE "
			+ "id = ? AND type_id IN (SELECT id FROM cdm_types WHERE name = ? AND datamodel_id IN ("
			+ "SELECT id FROM cdm_datamodels WHERE namespace = ? and major_version = ?))";

	protected DataSource		dataSource;

	// Called by Spring
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Response toolsSetModificationTimestampGet(String caseReference, String modificationTimestamp)
			throws Exception
	{
		Response response = null;
		Calendar cal = parseModificationTimestamp(modificationTimestamp, false);
		if (cal == null)
		{
			response = Response.status(Status.BAD_REQUEST)
					.entity("Not an ISO-8601 datetime (e.g. 2001-02-03T04:05:06.789Z): " + modificationTimestamp)
					.build();
		}
		else
		{
			CaseReference ref = new CaseReference(caseReference);
			Connection conn = getConnection();

			Statement ts = null;
			PreparedStatement ps = null;
			try
			{
				ts = conn.createStatement();
				ts.execute("SET statement_timeout TO " + String.valueOf(WAIT));

				// Verify existence of cases, check version numbers and lock
				ps = conn.prepareStatement(SQL_SET_MODIFICATION_TIMESTAMP);

				Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
				ps.setTimestamp(1, timestamp);
				ps.setBigDecimal(2, new BigDecimal(ref.getId()));
				ps.setString(3, ref.getQualifiedTypeName().getName());
				ps.setString(4, ref.getQualifiedTypeName().getNamespace());
				ps.setInt(5, ref.getApplicationMajorVersion());
				int rowCount = ps.executeUpdate();
				if (rowCount == 0)
				{
					response = Response.status(Status.BAD_REQUEST).entity("No case matching " + caseReference).build();
				}
				else
				{
					response = Response.ok().build();
				}
			}
			catch (SQLException e)
			{
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
				throw PersistenceException.newRepositoryProblem(e);
			}
			finally
			{
				cleanUp(ts, ps, conn);
			}
		}
		return response;
	}

	protected void cleanUp(Statement ts, PreparedStatement ps, Connection conn)
	{
		CLFMethodContext clf = logCtx.getMethodContext("cleanUp");
		if (ts != null)
		{
			try
			{
				ts.execute("RESET statement_timeout");
				ts.close();
			}
			catch (SQLException e)
			{
				clf.local.debug(e, "Failed to close statement");
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
				clf.local.debug(e, "Failed to close prepared statement");
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

	protected void releaseConnection(Connection conn)
	{
		DataSourceUtils.releaseConnection(conn, dataSource);
	}

	private Calendar parseModificationTimestamp(String timestamp, boolean maximise) throws IllegalArgumentException
	{
		return DateTimeParser.parseString(timestamp, maximise);
	}
}
