package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CDMErrorData;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.NotAuthorisedException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.aspect.CaseAspectSelection;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.core.dto.CaseInfoDTO;
import com.tibco.bpm.cdm.core.dto.CaseUpdateDTO;
import com.tibco.bpm.cdm.test.util.PolicyCreator;
import com.tibco.bpm.cdm.util.DateTimeParser;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.n2.common.security.RequestContext;

public class CaseCRUDTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	@Test
	public void testCreateAndRead() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			ReferenceException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<String> cases = new ArrayList<>();
			cases.add(
					"{\"state\":\"CREATED\",\"number\":999,\"dirtbox\":{\"a\":9},\"trumpet\":\"trouser\",\"claims\":[{\"c1\":123}]}");
			cases.add("{\"state\":\"CANCELLED\",\"number\":888}");
			cases.add("{\"state\":\"CREATED\",\"number\":777}");
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					cases);
			System.out.println("Cases created: " + refs);

			List<CaseInfoDTO> caseInfos = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, null, 999999, null, "CREATED", null,null, null, null,
					CaseAspectSelection.fromSelectExpression("cr,c,s,m"), false);
			Assert.assertEquals(caseInfos.size(), 2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateAndReadByRefs() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			ReferenceException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<String> cases = new ArrayList<>();
			cases.add("{\"state\":\"CREATED\",\"number\":999}");
			cases.add("{\"state\":\"CANCELLED\",\"number\":888}");
			cases.add("{\"state\":\"CREATED\",\"number\":777}");
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					cases);
			System.out.println("Cases created: " + refs);

			List<CaseInfoDTO> caseInfos = caseManager.readCases(refs,
					CaseAspectSelection.fromSelectExpression("cr,c,s,m"));
			Assert.assertEquals(caseInfos.size(), 3);

			// Try single case API
			for (CaseReference ref : refs)
			{
				CaseInfoDTO dto = caseManager.readCase(ref, CaseAspectSelection.fromSelectExpression("cr,c,s,m"),
						false);
				Assert.assertEquals(dto.getId(), ref.getId());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testPolicyCreate() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			ReferenceException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		int numPolicies = 10;

		try
		{
			List<String> cases = new ArrayList<>();
			for (int i = 0; i < numPolicies; i++)
			{

				cases.add(PolicyCreator.generatePolicyJSON());
			}
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					cases);
			System.out.println("Cases created: " + refs);

			List<CaseInfoDTO> caseInfos = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, 0, 999999, null, null, null,null, "created", null,
					CaseAspectSelection.fromSelectExpression("cr,c,s,m"), false);
			System.out.println(caseInfos);
			Assert.assertTrue(caseInfos.size() > 0);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}

	}

	@Test
	public void testPolicyUpdate() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, ReferenceException, DataModelSerializationException, DeploymentException,
			InternalException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);
			Assert.assertEquals(refs.get(0).getVersion(), 0);

			CaseUpdateDTO dto = new CaseUpdateDTO(refs.get(0),
					"{\"state\":\"CANCELLED\",\"number\":999,\"lecturn\":9}");
			caseManager.updateCases(Collections.singletonList(dto));

			// Check version was bumped
			Assert.assertEquals(dto.getNewCaseReference().getVersion(), 1);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testPolicyUpdateWrongVersion() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, ReferenceException, DataModelSerializationException, DeploymentException,
			InternalException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);
			Assert.assertEquals(refs.get(0).getVersion(), 0);

			// Mess with the version number
			refs.get(0).setVersion(123);

			try
			{
				caseManager.updateCases(Collections
						.singletonList(new CaseUpdateDTO(refs.get(0), "{\"state\":\"CANCELLED\",\"number\":999}")));
				Assert.fail("Expected failure");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData(), CDMErrorData.CDM_REFERENCE_VERSION_MISMATCH);
				Assert.assertEquals(e.getAttributes().get("version"), "123");
				Assert.assertEquals(e.getAttributes().get("actualVersion"), "0");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateMixedTypes()
			throws PersistenceException, InternalException, ValidationException, ArgumentException
	{
		List<CaseUpdateDTO> dtos = new ArrayList<>();
		dtos.add(new CaseUpdateDTO(new CaseReference("1-com.example.model1-0-0"), ""));
		dtos.add(new CaseUpdateDTO(new CaseReference("1-com.example.model2-0-0"), ""));
		try
		{
			caseManager.updateCases(dtos);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_API_CASES_SAME_TYPE.getCode());
		}
	}

	@Test
	public void testUpdateMixedMajorVersions()
			throws PersistenceException, InternalException, ValidationException, ArgumentException
	{
		List<CaseUpdateDTO> dtos = new ArrayList<>();
		dtos.add(new CaseUpdateDTO(new CaseReference("1-com.example.model1-0-0"), ""));
		dtos.add(new CaseUpdateDTO(new CaseReference("1-com.example.model1-99-0"), ""));
		try
		{
			caseManager.updateCases(dtos);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_API_CASES_SAME_MAJOR_VERSION.getCode());
		}
	}

	@Test
	public void testUpdateTerminalStateCase()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, ReferenceException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":123, \"state\":\"CANCELLED\"}"));
			Assert.assertNotNull(refs);
			Assert.assertEquals(refs.size(), 1);

			try
			{
				caseManager.updateCases(Collections.singletonList(new CaseUpdateDTO(refs.get(0),
						"{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":10}")));
				Assert.fail("Expected failure");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_TERMINAL_STATE_PREVENTS_UPDATE");
				Assert.assertEquals(e.getMessage(), "Case is in a terminal state so cannot be updated: " + refs.get(0));
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteByRefWrongVersion() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);
			Assert.assertEquals(refs.get(0).getVersion(), 0);

			// Read case back to check its there
			CaseInfoDTO dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("cr"), false);
			Assert.assertNotNull(dto);
			Assert.assertEquals(dto.getId(), refs.get(0).getId());

			// Change the version in the ref and attempt delete. It should fail as the version number mismatches.
			CaseReference ref = refs.get(0);
			ref.setVersion(ref.getVersion() + 1);
			try
			{
				caseManager.deleteCase(ref);
				Assert.fail("Expected failure due to version mismatch");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_VERSION_MISMATCH.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteByRef() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);
			Assert.assertEquals(refs.get(0).getVersion(), 0);

			// Read case back to check its there
			CaseInfoDTO dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("cr"), false);
			Assert.assertNotNull(dto);
			Assert.assertEquals(dto.getId(), refs.get(0).getId());

			// Delete single case by ref
			caseManager.deleteCase(refs.get(0));

			// Read again to check it has gone
			try
			{
				dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("cr"), false);
				Assert.fail("Expected failure as case should not exist");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_NOT_EXIST.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteByConstraints() throws PersistenceException, ReferenceException, DeploymentException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Waaaaay in the future
			Calendar maxModificationTimestamp = DateTimeParser.parseString("2999TZ", true);

			// There is nothing to delete
			int deletionCount = caseManager.deleteCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"CREATED", maxModificationTimestamp, null);
			Assert.assertEquals(deletionCount, 0);

			// Create a case
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);

			// Wrong state value - won't delete anything
			deletionCount = caseManager.deleteCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"CANCELLED", maxModificationTimestamp, null);
			Assert.assertEquals(deletionCount, 0);

			// The original delete request again; This time it deletes 1 case.
			deletionCount = caseManager.deleteCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, "CREATED",
					maxModificationTimestamp, null);
			Assert.assertEquals(deletionCount, 1);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testNoodleSoupAutoCID() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException,
			DataModelSerializationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		// NoodleSoup is configured with a Text CID, padded to 3 digits with no prefix or suffix.
		BigInteger deploymentId = deploy("/apps/app2");

		try
		{
			// Create 1,001 cases
			List<String> casedataList = new ArrayList<>();
			for (int i = 0; i < 1001; i++)
			{
				casedataList.add("{\"state\":\"CREATED\"}");
			}

			List<CaseReference> refs = caseManager
					.createCases(new QualifiedTypeName("com.mybigcompany.otherstuff.NoodleSoup"), 1, casedataList);
			Assert.assertEquals(refs.size(), 1001);

			// Read cases back and check the correct CIDs have been assigned
			List<CaseInfoDTO> dtos = caseManager.readCases(refs, CaseAspectSelection.fromSelectExpression("c"));
			Assert.assertEquals(dtos.size(), 1001);

			String casedata1 = dtos.get(0).getCasedata();
			String casedata10 = dtos.get(9).getCasedata();
			String casedata100 = dtos.get(99).getCasedata();
			String casedata1000 = dtos.get(999).getCasedata();

			// Check padded to 3 digits, but able to grow longer
			Assert.assertEquals(om.readTree(casedata1).at("/potCode").asText(), "001");
			Assert.assertEquals(om.readTree(casedata10).at("/potCode").asText(), "010");
			Assert.assertEquals(om.readTree(casedata100).at("/potCode").asText(), "100");
			Assert.assertEquals(om.readTree(casedata1000).at("/potCode").asText(), "1000");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testBananaAutoCID() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		// Banana is configured with a Text CID, padded to 15 digits with a 'BAN-' prefix and '-ANA' suffix
		BigInteger deploymentId = deploy("/apps/app2");

		try
		{
			// Create 11 cases
			List<String> casedataList = new ArrayList<>();
			for (int i = 0; i < 11; i++)
			{
				casedataList.add("{\"state\":\"CREATED\"}");
			}

			List<CaseReference> refs = caseManager
					.createCases(new QualifiedTypeName("com.mybigcompany.commonassets.Banana"), 1, casedataList);
			Assert.assertEquals(refs.size(), 11);

			// Read cases back and check the correct CIDs have been assigned
			List<CaseInfoDTO> dtos = caseManager.readCases(refs, CaseAspectSelection.fromSelectExpression("c"));
			Assert.assertEquals(dtos.size(), 11);

			String casedata1 = dtos.get(0).getCasedata();
			String casedata2 = dtos.get(1).getCasedata();
			String casedata3 = dtos.get(2).getCasedata();
			String casedata4 = dtos.get(3).getCasedata();
			String casedata5 = dtos.get(4).getCasedata();
			String casedata6 = dtos.get(5).getCasedata();
			String casedata7 = dtos.get(6).getCasedata();
			String casedata8 = dtos.get(7).getCasedata();
			String casedata9 = dtos.get(8).getCasedata();
			String casedata10 = dtos.get(9).getCasedata();
			String casedata11 = dtos.get(10).getCasedata();

			// Check CIDs
			Assert.assertEquals(om.readTree(casedata1).at("/fruitCode").asText(), "BAN-000000000000001-ANA");
			Assert.assertEquals(om.readTree(casedata2).at("/fruitCode").asText(), "BAN-000000000000002-ANA");
			Assert.assertEquals(om.readTree(casedata3).at("/fruitCode").asText(), "BAN-000000000000003-ANA");
			Assert.assertEquals(om.readTree(casedata4).at("/fruitCode").asText(), "BAN-000000000000004-ANA");
			Assert.assertEquals(om.readTree(casedata5).at("/fruitCode").asText(), "BAN-000000000000005-ANA");
			Assert.assertEquals(om.readTree(casedata6).at("/fruitCode").asText(), "BAN-000000000000006-ANA");
			Assert.assertEquals(om.readTree(casedata7).at("/fruitCode").asText(), "BAN-000000000000007-ANA");
			Assert.assertEquals(om.readTree(casedata8).at("/fruitCode").asText(), "BAN-000000000000008-ANA");
			Assert.assertEquals(om.readTree(casedata9).at("/fruitCode").asText(), "BAN-000000000000009-ANA");
			Assert.assertEquals(om.readTree(casedata10).at("/fruitCode").asText(), "BAN-000000000000010-ANA");
			Assert.assertEquals(om.readTree(casedata11).at("/fruitCode").asText(), "BAN-000000000000011-ANA");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCIDChangePrevented() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException,
			DataModelSerializationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":123}"));
			Assert.assertEquals(refs.size(), 1);
			try
			{
				caseManager.updateCases(Collections.singletonList(new CaseUpdateDTO(refs.get(0), "{\"number\":456}")));
				Assert.fail("Expected CID change to prompt failure");
			}
			catch (CasedataException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_CASEDATA_CID_CHANGED.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCIDClashPrevented() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException,
			DataModelSerializationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":123}"));
			Assert.assertEquals(refs.size(), 1);

			try
			{
				caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
						Collections.singletonList("{\"number\":123}"));
				Assert.fail("Expected duplicate CID to prompt failure");
			}
			catch (CasedataException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_CASEDATA_NON_UNIQUE_CID.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateWithNotExistentNamespace() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		try
		{
			caseManager.createCases(new QualifiedTypeName("org.crazy.unthinkable.FantasyType"), 1,
					Collections.singletonList("{\"number\":999}"));
			Assert.fail("Expected non-existent namespace to cause failure");
		}
		catch (ReferenceException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_UNKNOWN_NAMESPACE.getCode());
		}
	}

	@Test
	public void testCreateWithNotExistentTypeInRealNamespace() throws PersistenceException, ReferenceException,
			InternalException, DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			caseManager.createCases(new QualifiedTypeName("org.policycorporation.ThereIsNoClassCalledThis"), 1,
					Collections.singletonList("{\"nickname\":\"Banana\"}"));
			Assert.fail("Expected non-existent namespace to cause failure");
		}
		catch (ReferenceException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_UNKNOWN_TYPE.getCode());
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateWithWrongMajorVersion() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Quick check it works with the _correct_ major version
			caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":3453345}"));

			// But this should fail
			try
			{
				caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 12345678,
						Collections.singletonList("{\"number\":555444}"));
				Assert.fail("Expected non-existent namespace to cause failure");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_UNKNOWN_NAMESPACE.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteSingleRef() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":555}"));
			Assert.assertEquals(refs.size(), 1);
			caseManager.deleteCase(refs.get(0));
			// Check it has gone
			try
			{
				caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("cr"), false);
				Assert.fail("Expected fetch of deleted case to fail");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_NOT_EXIST.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteSingleRefWrongVersion() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"number\":555}"));
			Assert.assertEquals(refs.size(), 1);
			CaseReference ref = refs.get(0);
			// Mess with the version
			ref.setVersion(9999999);
			try
			{
				caseManager.deleteCase(ref);
				Assert.fail("Expected delete to fail");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_VERSION_MISMATCH.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testMultipleSearchParameters() throws PersistenceException, CasedataException, ReferenceException,
			InternalException, DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			try
			{
				caseManager.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, 0, Integer.MAX_VALUE,
						null, null, null,null, "search string", "dql string", CaseAspectSelection.fromSelectExpression("cr"),
						false);
				Assert.fail("Expected fail due to search and dql used together");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_API_DQL_WITH_OTHER_SEARCH_PARAMETERS.getCode());
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateCaseWithSpecificUser() throws PersistenceException, DeploymentException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException, NotAuthorisedException,
			ValidationException, ArgumentException
	{
		// Set a user other than tibco-admin (which has already been set in BaseTest)
		String userGUID = "DEADBEEF-0123-4567-89AB-DEADBEEF0000";
		RequestContext.getCurrent().getCurrentUser().setGuid(userGUID);

		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999,\"premium\":100}"));
			Assert.assertNotNull(refs);
			Assert.assertEquals(refs.size(), 1);
			CaseInfoDTO dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("m"), false);
			String createdBy = dto.getCreatedBy();
			Assert.assertEquals(createdBy, userGUID);
			// Creating user is by definition the modifier too
			String modifiedBy = dto.getCreatedBy();
			Assert.assertEquals(modifiedBy, userGUID);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateCaseWithDifferentUser() throws PersistenceException, DeploymentException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException, NotAuthorisedException,
			ValidationException, ArgumentException
	{
		// Set a user other than tibco-admin (which has already been set in BaseTest)
		String userGUID = "DEADBEEF-0123-4567-89AB-DEADBEEF0000";
		RequestContext.getCurrent().getCurrentUser().setGuid(userGUID);

		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999,\"premium\":100}"));
			Assert.assertNotNull(refs);
			Assert.assertEquals(refs.size(), 1);
			CaseInfoDTO dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("m"), false);
			Assert.assertEquals(dto.getCreatedBy(), userGUID);
			// Creating user is by definition the modifier too
			Assert.assertEquals(dto.getModifiedBy(), userGUID);

			// Now, update the case using a different user and check that createdBy remains the same, but
			// modified by changes.
			String userGUID2 = "ABBABABE-0000-1111-2222-333333333333";
			RequestContext.getCurrent().getCurrentUser().setGuid(userGUID2);
			caseManager.updateCases(Collections.singletonList(
					new CaseUpdateDTO(refs.get(0), "{\"state\":\"CREATED\",\"number\":999,\"premium\":200}")));
			dto = caseManager.readCase(refs.get(0), CaseAspectSelection.fromSelectExpression("m"), false);
			// Creating user remains the same
			Assert.assertEquals(dto.getCreatedBy(), userGUID);
			// Modifier is the second user
			Assert.assertEquals(dto.getModifiedBy(), userGUID2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUserGUIDTooLong() throws ReferenceException, NotAuthorisedException, PersistenceException,
			ValidationException, ArgumentException, InternalException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Set a dodgy GUID and attempt to create a case
			String dodgyGUID = "This is a dodgy GUID to say the least. It's waaaaay too long for a start!";
			RequestContext.getCurrent().getCurrentUser().setGuid(dodgyGUID);
			caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999,\"premium\":100}"));
			Assert.fail("Expected failure due to dodgy GUID in RequestContext");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_API_BAD_CURRENT_USER_GUID.getCode());
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
