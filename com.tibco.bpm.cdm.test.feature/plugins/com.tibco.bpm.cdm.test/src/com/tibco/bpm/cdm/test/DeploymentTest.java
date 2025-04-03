package com.tibco.bpm.cdm.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.exception.CDMErrorData;
import com.tibco.bpm.cdm.api.exception.CDMException.MetadataEntry;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.dao.DataModelDAO;
import com.tibco.bpm.cdm.core.dao.DataModelDAO.ApplicationIdAndMajorVersion;
import com.tibco.bpm.cdm.core.deployment.DataModelManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.test.util.FileUtils.DataModelModifier;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.BaseType;
import com.tibco.bpm.da.dm.api.Constraint;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.da.dm.api.Link;
import com.tibco.bpm.da.dm.api.State;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class DeploymentTest extends BaseTest
{
	private static final String	SQL_COUNT_TYPES	= "SELECT count(*) FROM cdm_types WHERE datamodel_id IN "
			+ "(select id FROM cdm_datamodels WHERE application_id IN (select id from cdm_applications "
			+ "WHERE deployment_id = ?))";

	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("dataModelManager")
	private DataModelManager	dataModelManager;

	@Autowired
	@Qualifier("dataModelDAO")
	private DataModelDAO		dataModelDAO;

	public void testDeployment() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, DataModelSerializationException, Exception
	{
		File file = FileUtils.buildZipFromFolderURL(DeploymentTest.class.getResource("/apps/app1"), true);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId = BigInteger.valueOf(12345);
		runtimeApplication.setID(deploymentId);

		deploymentManager.deploy(runtimeApplication);

		// Assert that four types exist
		assertTypeCountViaDatabase(deploymentId, 4);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 4, null);
		// ...of which 2 are case and 2 non-case
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 2, true);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 2, false);
		assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);

		deploymentManager.undeploy(deploymentId);

		// Assert that no types exist (app is undeployed)
		assertTypeCountViaDatabase(deploymentId, 0);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 0, null);
		assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);
	}

	@Test
	public void testCrossReference() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException
	{
		File file = FileUtils.buildZipFromFolderResource("/apps/app2", true);
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId = BigInteger.valueOf(201001);
		runtimeApplication.setID(deploymentId);
		deploymentManager.deploy(runtimeApplication);
		deploymentManager.undeploy(deploymentId);
	}

	@Test
	public void testMajorVersionsCanCoExist() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, DataModelSerializationException, Exception
	{
		// Deploy many major versions of the same app and ensure they don't interfere with each other.
		File file = FileUtils.buildZipFromFolderResource("/apps/app2", true);
		RuntimeApplication runtimeApplication = null;

		// No types expected to start with
		assertTypeCountViaDataModelManager(null, null, 0, null);

		for (int majorVersion = 1; majorVersion <= 100; majorVersion++)
		{
			FileUtils.setVersionInRASC(file, majorVersion + ".0.0.1");
			runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);
			// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
			BigInteger deploymentId = BigInteger.valueOf(5000 + majorVersion);
			runtimeApplication.setID(deploymentId);
			deploymentManager.deploy(runtimeApplication);
		}

		// 100 x 3 = 300
		assertTypeCountViaDataModelManager(null, null, 300, null);

		// Undeploy all
		for (int majorVersion = 1; majorVersion <= 100; majorVersion++)
		{
			BigInteger deploymentId = BigInteger.valueOf(5000 + majorVersion);
			deploymentManager.undeploy(deploymentId);
		}

		// All types have gone
		assertTypeCountViaDataModelManager(null, null, 0, null);
	}

	@Test
	public void testTypesPagination() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Deploy app1 (4 types)
		File file = FileUtils.buildZipFromFolderResource("/apps/app1", true);
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);
		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId = BigInteger.valueOf(111001);
		runtimeApplication.setID(deploymentId);
		deploymentManager.deploy(runtimeApplication);
		assertTypeCountViaDatabase(deploymentId, 4);

		// Deploy app2 (3 types)
		File file2 = FileUtils.buildZipFromFolderResource("/apps/app2", true);
		RuntimeApplication runtimeApplication2 = FileUtils.loadRuntimeApplicationFromZip(file2);
		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId2 = BigInteger.valueOf(111002);
		runtimeApplication2.setID(deploymentId2);
		deploymentManager.deploy(runtimeApplication2);
		assertTypeCountViaDatabase(deploymentId2, 3);

		// Test $skip
		for (int i = 0; i <= 7; i++)
		{
			// Skip 'i', expecting 7-i results.
			assertTypeCountViaDataModelManager(null, null, 7 - i, null, i, 999);

			// Test $top
			for (int top = 0; top < 10; top++)
			{
				int expected = Math.min(top, 7 - i);
				assertTypeCountViaDataModelManager(null, null, expected, null, i, top);
			}
		}

		deploymentManager.undeploy(deploymentId);
		deploymentManager.undeploy(deploymentId2);
	}

	@Test
	public void testCrossProjectDependency() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger policyDeploymentId = deploy("/apps/app1");
		BigInteger housingDeploymentId = null;
		try
		{
			housingDeploymentId = deploy("/apps/com.example.housing");
		}
		finally
		{
			if (housingDeploymentId != null)
			{
				undeploy(housingDeploymentId);
			}
			undeploy(policyDeploymentId);
		}
	}

	@Test
	public void testCrossProjectDependencyCannotBreak() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger policyDeploymentId = deploy("/apps/app1");
		BigInteger housingDeploymentId = null;
		try
		{
			housingDeploymentId = deploy("/apps/com.example.housing");
			// Attempt to undeploy Policy. Should fail as Housing needs it.
			try
			{
				undeploy(policyDeploymentId);
				Assert.fail("Expecting failure as this would break dependency");
			}
			catch (DeploymentException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_DEPLOYMENT_DEPENDENCIES_PREVENT_UNDEPLOYMENT.getCode());
			}
		}
		finally
		{
			if (housingDeploymentId != null)
			{
				undeploy(housingDeploymentId);
			}
			undeploy(policyDeploymentId);
		}
	}

	private void doSuccessfulPolicyAppUpgrade(DataModelModifier modifier, int expectedCaseTypeCount,
			int expectedNonCaseTypeCount) throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, DataModelSerializationException, Exception
	{
		boolean deployed1 = false;
		boolean deployed2 = false;

		try
		{
			File file = FileUtils.buildZipFromFolderResource("/apps/app1", true);

			RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

			// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
			BigInteger deploymentId = BigInteger.valueOf(111001);
			runtimeApplication.setID(deploymentId);
			deploymentManager.deploy(runtimeApplication);
			deployed1 = true;

			assertTypeCountViaDatabase(deploymentId, 4);

			// Bump the version number and deploy again
			FileUtils.setVersionInRASC(file.toPath(), "1.0.1.0");

			// Change the model content
			FileUtils.modifyDataModelInZip(file.toPath(), "model.dm", modifier);

			runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(file));
			BigInteger deploymentId2 = BigInteger.valueOf(111002);
			runtimeApplication.setID(deploymentId2);
			deploymentManager.deploy(runtimeApplication);
			deployed2 = true;

			// Check that old version has implicitly gone
			assertTypeCountViaDatabase(deploymentId, 0);

			// Assert that four types exist
			assertTypeCountViaDatabase(deploymentId2, expectedCaseTypeCount + expectedNonCaseTypeCount);
			assertTypeCountViaDataModelManager("com.example.ProjectX", 1,
					expectedCaseTypeCount + expectedNonCaseTypeCount, null);
			// ...of which 2 are case and 2 non-case
			assertTypeCountViaDataModelManager("com.example.ProjectX", 1, expectedCaseTypeCount, true);
			assertTypeCountViaDataModelManager("com.example.ProjectX", 1, expectedNonCaseTypeCount, false);
			assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);

			deploymentManager.undeploy(deploymentId2);
			deployed1 = true;

			// Assert that no types exist (app is undeployed)
			assertTypeCountViaDatabase(deploymentId2, 0);
			assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 0, null);
			assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);
		}
		finally
		{
			if (deployed1)
			{
				forceUndeploy(BigInteger.valueOf(111001));
			}
			if (deployed2)
			{
				forceUndeploy(BigInteger.valueOf(111002));
			}
		}
	}

	private void doUnsuccessfulPolicyAppUpgrade(DataModelModifier modifier, String expectedReportMessage)
			throws IOException, URISyntaxException, RuntimeApplicationException, DeploymentException,
			PersistenceException, InternalException, DataModelSerializationException
	{
		boolean deployed1 = false;
		boolean deployed2 = false;

		try
		{
			File file = FileUtils.buildZipFromFolderResource("/apps/app1", true);

			RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

			// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
			BigInteger deploymentId = BigInteger.valueOf(111001);
			runtimeApplication.setID(deploymentId);
			deploymentManager.deploy(runtimeApplication);
			deployed1 = true;

			assertTypeCountViaDatabase(deploymentId, 4);

			// Bump the version number and deploy again
			FileUtils.setVersionInRASC(file.toPath(), "1.0.1.0");

			// Change the model content
			FileUtils.modifyDataModelInZip(file.toPath(), "model.dm", modifier);

			runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(file));
			BigInteger deploymentId2 = BigInteger.valueOf(111002);
			runtimeApplication.setID(deploymentId2);
			deploymentManager.deploy(runtimeApplication);
			deployed2 = true;
			Assert.fail("Expected upgrade to fail");
		}
		catch (DeploymentException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(),
					CDMErrorData.CDM_DEPLOYMENT_INVALID_DATAMODEL_UPGRADE.getCode(), "wrong error code");
			MetadataEntry metadataEntry = e.getMetadataEntries().stream()
					.filter(me -> me.getName().equals("reportMessage")).findFirst().orElse(null);
			Assert.assertNotNull(metadataEntry, "No reportMessage metadata in exception");
			Assert.assertEquals(metadataEntry.getValue(), expectedReportMessage);
		}
		finally
		{
			if (deployed1)
			{
				forceUndeploy(BigInteger.valueOf(111001));
			}
			if (deployed2)
			{
				forceUndeploy(BigInteger.valueOf(111002));
			}
		}
	}

	@Test
	public void testUpgradeIncreaseLinkEnd1Multiplicity()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Allow a Person to have many favourite policies
		doSuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().get(3).getEnd1().setIsArray(true);
		}, 2, 2);
	}

	@Test
	public void testUpgradeIncreaseLinkEnd2Multiplicity()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Allow a given Policy to have many holders
		doSuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().get(0).getEnd2().setIsArray(true);
		}, 2, 2);
	}

	@Test
	public void testUpgradeReduceLinkEnd1Multiplicity()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException
	{
		doUnsuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().get(0).getEnd1().setIsArray(false);
		}, "DataModel (formatVersion: 2):\n"
				+ "\tLink (Person.policies <-> Policy.holder) end Person.policies cannot be changed from array to non-array\n");
	}

	@Test
	public void testUpgradeReduceLinkEnd2Multiplicity()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException
	{
		doUnsuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().get(2).getEnd2().setIsArray(false);
		}, "DataModel (formatVersion: 2):\n"
				+ "\tLink (Person.isParentOf <-> Person.isChildOf) end Person.isChildOf cannot be changed from array to non-array\n");
	}

	@Test
	public void testUpgradeAddOptionalAttribute() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Should be valid to add an optional attribute
		doSuccessfulPolicyAppUpgrade((dm) -> {
			Attribute attr = dm.getStructuredTypeByName("Policy").newAttribute();
			attr.setName("optionalText");
			attr.setLabel("Optional Text");
			attr.setTypeObject(BaseType.TEXT);
		}, 2, 2);
	}

	@Test
	public void testUpgradeChangeAttributeLabel() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Should be valid to change the label for an existing attribute
		doSuccessfulPolicyAppUpgrade((dm) -> {
			Attribute attr = dm.getStructuredTypeByName("Policy").getAttributeByName("number");
			attr.setLabel("New label for the number attribute");
		}, 2, 2);
	}

	@Test
	public void testUpgradeAddLink() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Upgrade will create one entry in cdm_links and detect that the others are pre-existing
		doSuccessfulPolicyAppUpgrade((dm) -> {
			Link link = dm.newLink();
			link.getEnd1().setLabel("Link End 1");
			link.getEnd1().setName("linkEnd1");
			link.getEnd1().setOwner("Policy");
			link.getEnd1().setIsArray(true);
			link.getEnd2().setLabel("Link End 1");
			link.getEnd2().setName("linkEnd2");
			link.getEnd2().setOwner("Person");
		}, 2, 2);
	}

	@Test
	public void testUpgradeRemoveLink() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		doUnsuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().remove(0);
		}, "DataModel (formatVersion: 2):\n" + "\tLink was removed: Link (Person.policies <-> Policy.holder)\n");
	}

	@Test
	public void testUpgradeChangeLink() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		// Changing the name is effectively removing the link definition and creating a different one
		doUnsuccessfulPolicyAppUpgrade((dm) -> {
			dm.getLinks().get(0).getEnd1().setName("ChangedNameForEnd1");
		}, "DataModel (formatVersion: 2):\n" + "\tLink was removed: Link (Person.policies <-> Policy.holder)\n");
	}

	@Test
	public void testUpgradeAddMandatoryAttribute() throws PersistenceException, DeploymentException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		doUnsuccessfulPolicyAppUpgrade((dm) -> {
			Attribute attr = dm.getStructuredTypeByName("Policy").newAttribute();
			attr.setName("illegalNewMandatory");
			attr.setLabel(
					"You must not add a mandatory attribute during an upgrade as it will render all existing cases invalid!");
			attr.setIsMandatory(true);
			attr.setTypeObject(BaseType.DATE);
		}, "DataModel (formatVersion: 2):\n" + "	StructuredType (name: Policy; label: Policy):\n"
				+ "		New attribute 'illegalNewMandatory' must not be mandatory\n");
	}

	@Test
	public void testUpgradeAddNonCaseType() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		doSuccessfulPolicyAppUpgrade((dm) -> {
			StructuredType st = dm.newStructuredType();
			st.setName("NewNonCaseType");
			st.setLabel("New non-case type");
			Attribute attr = st.newAttribute();
			attr.setName("attr1");
			attr.setLabel("Attr One");
			attr.setTypeObject(BaseType.TEXT);
		}, 2, 3);
	}

	@Test
	public void testUpdateAddCaseTypeWithoutIII() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		doSuccessfulPolicyAppUpgrade((dm) -> {
			StructuredType st = dm.newStructuredType();
			st.setName("NewCaseType");
			st.setLabel("New case type");
			st.setIsCase(true);
			StateModel stateModel = st.newStateModel();
			stateModel.newState("State1", "State1", false);

			Attribute attr = st.newAttribute();
			attr.setName("cid");
			attr.setLabel("CID");
			attr.setTypeObject(BaseType.TEXT);
			attr.newConstraint(Constraint.NAME_LENGTH, "400");
			attr.setIsIdentifier(true);
			attr.setIsSearchable(true);
			attr.setIsSummary(true);
			attr.setIsMandatory(true);

			Attribute sa = st.newAttribute();
			sa.setName("state");
			sa.setLabel("State");
			sa.setTypeObject(BaseType.TEXT);
			sa.setIsState(true);
			sa.setIsSearchable(true);
			sa.setIsSummary(true);
			sa.setIsMandatory(true);
		}, 3, 2);
	}

	@Test
	public void testUpgrade() throws IOException, URISyntaxException, RuntimeApplicationException, DeploymentException,
			PersistenceException, InternalException, DataModelSerializationException, Exception
	{
		File file = FileUtils.buildZipFromFolderResource("/apps/app1", true);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(file);

		// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
		BigInteger deploymentId = BigInteger.valueOf(111001);
		runtimeApplication.setID(deploymentId);
		deploymentManager.deploy(runtimeApplication);

		assertTypeCountViaDatabase(deploymentId, 4);

		// Bump the version number and deploy again
		FileUtils.setVersionInRASC(file.toPath(), "1.0.1.0");

		// Change the model content
		FileUtils.modifyDataModelInZip(file.toPath(), "model.dm", (dm) -> {
			State s = dm.getStructuredTypeByName("Policy").getStateModel().getStateByValue("CANCELLED");
			s.setLabel("Trashed");
			s.setIsTerminal(false);
			dm.getStructuredTypeByName("Policy").getStateModel().newState("Exploded", "EXPLODED", false);
			//			// Add a new searchable attribute
			//			Attribute attr = dm.getStructuredTypeByName("Policy").newAttribute();
			//			attr.setName("newAttribute");
			//			attr.setLabel("New Attribute");
			//			attr.setTypeObject(BaseType.TEXT);
			//			attr.setIsSearchable(true);

			// Remove searchability of policyStartTime
			//			dm.getStructuredTypeByName("Policy").getAttributeByName("policyStartTime").setIsSearchable(false);

			//			// Remove III
			//			dm.getStructuredTypeByName("Policy").removeIdentifierInitialisationInfo();

			//			dm.getStructuredTypeByName("Policy").newIdentifierInitialisationInfo();
			//			dm.getStructuredTypeByName("Person").getidentifierInitialisationInfo().setSuffix("XXX");
		});
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(file));
		BigInteger deploymentId2 = BigInteger.valueOf(111002);
		runtimeApplication.setID(deploymentId2);
		deploymentManager.deploy(runtimeApplication);

		// Check that old version has implicitly gone
		assertTypeCountViaDatabase(deploymentId, 0);

		// Assert that four types exist
		assertTypeCountViaDatabase(deploymentId2, 4);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 4, null);
		// ...of which 2 are case and 2 non-case
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 2, true);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 2, false);
		assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);

		deploymentManager.undeploy(deploymentId2);

		// Assert that no types exist (app is undeployed)
		assertTypeCountViaDatabase(deploymentId2, 0);
		assertTypeCountViaDataModelManager("com.example.ProjectX", 1, 0, null);
		assertTypeCountViaDataModelManager("com.example.ProjectY", 1, 0, null);
	}

	private void assertTypeCountViaDataModelManager(String applicationId, Integer majorVersion, int expected,
			Boolean isCase) throws PersistenceException, DataModelSerializationException, InternalException, Exception
	{
		assertTypeCountViaDataModelManager(applicationId, majorVersion, expected, isCase, null, Integer.MAX_VALUE);
	}

	private void assertTypeCountViaDataModelManager(String applicationId, Integer majorVersion, int expected,
			Boolean isCase, Integer skip, Integer top)
			throws PersistenceException, DataModelSerializationException, InternalException, Exception
	{
		List<TypeInfoDTO> types = dataModelManager.getTypes(applicationId, null, majorVersion, isCase, skip, top, false,
				false, false, false, false);
		Assert.assertEquals(types.size(), expected,
				"Wrong number of types for application id " + applicationId + ", major version " + majorVersion);
	}

	private void assertTypeCountViaDatabase(BigInteger deploymentId, long expected) throws PersistenceException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		Statement ts = null;
		long count = 0;
		try
		{
			conn = getConnection();
			ts = conn.createStatement();
			ts.execute("SET statement_timeout TO " + String.valueOf(WAIT));

			ps = conn.prepareStatement(SQL_COUNT_TYPES);
			ps.setBigDecimal(1, new BigDecimal(deploymentId));

			boolean success = ps.execute();

			if (success)
			{
				ResultSet rset = ps.getResultSet();
				if (rset.next())
				{
					count = rset.getLong(1);
				}
			}
		}
		catch (SQLException e)
		{
			throw PersistenceException.newRepositoryProblem(e);
		}
		finally
		{
			cleanUp(ts, ps, conn);
		}
		Assert.assertEquals(count, expected, "Wrong number of types for deployment id " + deploymentId);
	}

	// Asserts that the exception states that dependencies prevented undeployment
	// and that the correct depending project(s) are mentioned.
	private void assertDependenciesPreventedUndeployment(DeploymentException e, List<String> expectedProjects)
	{
		Assert.assertEquals(e.getErrorData().getCode(),
				CDMErrorData.CDM_DEPLOYMENT_DEPENDENCIES_PREVENT_UNDEPLOYMENT.getCode());
		String dependencies = e.getAttributes().get("dependencies");
		// String is in this format:
		// e.g. [com.example.projb (version 1.0.0.20190502104215794), com.crossproj.proja (version 1.0.0.20190502104215775)]
		List<String> actualNames = new ArrayList<>();
		if (dependencies.startsWith("[") && dependencies.endsWith("]"))
		{
			dependencies = dependencies.substring(1, dependencies.length());
			String[] split = dependencies.split(",");
			for (int i = 0; i < split.length; i++)
			{
				String item = split[i].trim();
				int idx = item.indexOf(' ');
				if (idx != -1)
				{
					actualNames.add(item.substring(0, idx));
				}
			}
		}
		if (!actualNames.containsAll(expectedProjects) || !expectedProjects.containsAll(actualNames))
		{
			Assert.fail("List of projects preventing undeployment is wrong. Expected: " + expectedProjects
					+ ", but got: " + actualNames);
		}
	}

	// Asserts that the list contains the given strings (in the given order)
	private void assertListMatches(List<String> list, String... strings)
	{
		Assert.assertEquals(strings.length, list.size(), "Wrong number of strings in list");
		for (int i = 0; i < strings.length; i++)
		{
			Assert.assertEquals(list.get(i), strings[i], "Wrong string in position " + i);
		}
	}

	@Test
	public void testFourProjectDependencyTest() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ScriptException
	{
		BigInteger deployIdA = null;
		BigInteger deployIdB = null;
		BigInteger deployIdC = null;
		BigInteger deployIdD = null;

		// Deploy in the correct order to satisfy dependencies
		try
		{
			deployIdD = deployRASC("/apps/cross-project-test/ProjD/Exports/Deployment Artifacts/ProjD.rasc");
			deployIdC = deployRASC("/apps/cross-project-test/ProjC/Exports/Deployment Artifacts/ProjC.rasc");
			deployIdB = deployRASC("/apps/cross-project-test/ProjB/Exports/Deployment Artifacts/ProjB.rasc");
			deployIdA = deployRASC("/apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc");

			List<String> scripts = null;

			// Load scripts from RASCs for comparison purposes
			String scriptA1 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc",
					"bom-js/com.crossproj.proja.modela1.js");
			String scriptA2 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc",
					"bom-js/com.crossproj.proja.modela2.js");
			String scriptA3 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc",
					"bom-js/com.crossproj.proja.modela3.js");

			String scriptB1 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjB/Exports/Deployment Artifacts/ProjB.rasc",
					"bom-js/com.crossproj.projb.modelb1.js");
			String scriptB2 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjB/Exports/Deployment Artifacts/ProjB.rasc",
					"bom-js/com.crossproj.projb.modelb2.js");

			String scriptC1 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjC/Exports/Deployment Artifacts/ProjC.rasc",
					"bom-js/com.crossproj.projc.modelc1.js");

			String scriptD1 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjD/Exports/Deployment Artifacts/ProjD.rasc",
					"bom-js/com.crossproj.projd.modeld1.js");
			String scriptD2 = FileUtils.readFileFromRASC(
					"apps/cross-project-test/ProjD/Exports/Deployment Artifacts/ProjD.rasc",
					"bom-js/com.crossproj.projd.modeld2.js");

			// Check that the correct set of scripts (in the correct order!) are returned for each app.

			// Expecting all 8 scripts for ProjA
			scripts = dataModelDAO
					.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.proja", 1)));
			assertListMatches(scripts, scriptD2, scriptD1, scriptC1, scriptB2, scriptB1, scriptA3, scriptA2, scriptA1);

			// Expecting D2, C1, B2, B1 (not D1, as it's only referenced from ProjA)
			scripts = dataModelDAO
					.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.projb", 1)));
			assertListMatches(scripts, scriptD2, scriptC1, scriptB2, scriptB1);

			// Expecting just ProjC's own script
			scripts = dataModelDAO
					.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.projc", 1)));
			assertListMatches(scripts, scriptC1);

			// Expecting just ProjD's own scripts
			scripts = dataModelDAO
					.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.projd", 1)));
			assertListMatches(scripts, scriptD2, scriptD1);

			// Test retrieval of multiple applications at the same time

			// C & D
			scripts = dataModelDAO.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.projc", 1),
					new ApplicationIdAndMajorVersion("com.crossproj.projd", 1)));
			assertListMatches(scripts, scriptD2, scriptD1, scriptC1);

			// All four projects
			scripts = dataModelDAO.readScripts(Arrays.asList(new ApplicationIdAndMajorVersion("com.crossproj.projc", 1),
					new ApplicationIdAndMajorVersion("com.crossproj.projd", 1),
					new ApplicationIdAndMajorVersion("com.crossproj.proja", 1),
					new ApplicationIdAndMajorVersion("com.crossproj.projb", 1)));
			assertListMatches(scripts, scriptD2, scriptD1, scriptC1, scriptB2, scriptB1, scriptA3, scriptA2, scriptA1);

		}
		finally
		{
			if (deployIdA != null)
			{
				undeploy(deployIdA);
			}
			if (deployIdB != null)
			{
				undeploy(deployIdB);
			}
			if (deployIdC != null)
			{
				undeploy(deployIdC);
			}
			if (deployIdD != null)
			{
				undeploy(deployIdD);
			}
		}
	}

	@Test
	public void testFourProjectDependencyRugPulling() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deployIdA = null;
		BigInteger deployIdB = null;
		BigInteger deployIdC = null;
		BigInteger deployIdD = null;

		// Deploy in the correct order to satisfy dependencies
		try
		{
			deployIdD = deployRASC("/apps/cross-project-test/ProjD/Exports/Deployment Artifacts/ProjD.rasc");
			deployIdC = deployRASC("/apps/cross-project-test/ProjC/Exports/Deployment Artifacts/ProjC.rasc");
			deployIdB = deployRASC("/apps/cross-project-test/ProjB/Exports/Deployment Artifacts/ProjB.rasc");
			deployIdA = deployRASC("/apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc");

			// Attempt to undeploy B (i.e. 'pull the rug out from beneath' A)
			try
			{
				undeploy(deployIdB);
				Assert.fail("Expected undeploy of B to fail, as A depends on it");
			}
			catch (DeploymentException e)
			{
				assertDependenciesPreventedUndeployment(e, Collections.singletonList("com.crossproj.proja"));
			}

			// Attempt to undeploy C (i.e. 'pull the rug out from beneath' B)
			try
			{
				undeploy(deployIdC);
				Assert.fail("Expected undeploy of C to fail, as B depends on it");
			}
			catch (DeploymentException e)
			{
				assertDependenciesPreventedUndeployment(e, Collections.singletonList("com.crossproj.projb"));
			}

			// Attempt to undeploy D (i.e. 'pull the rug out from beneath' A and B)
			try
			{
				undeploy(deployIdD);
				Assert.fail("Expected undeploy of D to fail, as A and B depend on it");
			}
			catch (DeploymentException e)
			{
				assertDependenciesPreventedUndeployment(e, Arrays.asList("com.crossproj.proja", "com.crossproj.projb"));
			}

		}
		finally
		{
			if (deployIdA != null)
			{
				undeploy(deployIdA);
			}
			if (deployIdB != null)
			{
				undeploy(deployIdB);
			}
			if (deployIdC != null)
			{
				undeploy(deployIdC);
			}
			if (deployIdD != null)
			{
				undeploy(deployIdD);
			}
		}
	}
}
