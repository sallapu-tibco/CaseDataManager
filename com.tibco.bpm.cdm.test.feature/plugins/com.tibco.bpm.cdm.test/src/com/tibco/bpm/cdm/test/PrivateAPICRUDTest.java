package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tibco.bpm.cdm.api.CaseDataManager;
import com.tibco.bpm.cdm.api.dto.CaseInfo;
import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class PrivateAPICRUDTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testCreateAndReadCase() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);
			CaseInfo info = privateAPI.readCase(ref);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CREATED\", \"number\": 123, \"premium\": 2000, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateAndReadManyCases() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = privateAPI.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Arrays.asList("{\"number\":123}", "{\"number\":456}"));
			Assert.assertNotNull(refs);
			Assert.assertEquals(refs.size(), 2);
			List<CaseInfo> infos = privateAPI.readCases(refs);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 2);
			Assert.assertEquals(infos.get(0).getReference(), refs.get(0));
			Assert.assertEquals(infos.get(0).getCasedata(),
					"{\"state\": \"CREATED\", \"number\": 123, \"premium\": 2000, "
							+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
							+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");
			Assert.assertEquals(infos.get(1).getReference(), refs.get(1));
			Assert.assertEquals(infos.get(1).getCasedata(),
					"{\"state\": \"CREATED\", \"number\": 456, \"premium\": 2000, "
							+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
							+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateCaseNullTypeName()
			throws com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.createCase(null, 1, "");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: null");
		}
	}

	@Test
	public void testCreateManyCasesNullTypeName()
			throws com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.createCases(null, 1, Arrays.asList("", ""));
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: null");
		}
	}

	@Test
	public void testCreateCaseInvalidCasedata() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":\"Not a number = bad!\"}");
			Assert.fail("Expected failure");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getMessage(), "Data is not a valid instance of the specified type: "
					+ "[number -> Expected NumericNode but found TextNode: \"Not a number = bad!\"]");
			Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateCaseCasedataNotObject() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "[]");
			Assert.fail("Expected failure");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_DATA_NOT_JSON_OBJECT");
			Assert.assertEquals(e.getMessage(), "Not JSON object");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateManyCasesCasedataNotObject() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, Arrays.asList("[]", "[]"));
			Assert.fail("Expected failure");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_DATA_NOT_JSON_OBJECT");
			Assert.assertEquals(e.getMessage(), "Not JSON object");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCreateCaseZeroLengthTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.createCase(new QualifiedTypeName(""), 1, "");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: ");
		}
	}

	@Test
	public void testCreateManyCasesZeroLengthTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.createCases(new QualifiedTypeName(""), 1, Arrays.asList("", ""));
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: ");
		}
	}

	@Test
	public void testCreateCaseUnqualifiedTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.createCase(new QualifiedTypeName("Brian"), 1, "");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: Brian");
		}
	}

	@Test
	public void testCreateCaseWrongMajorVersion() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 987654321, "{\"number\":456}");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_NAMESPACE");
			Assert.assertEquals(e.getMessage(), "Unknown namespace: org.policycorporation (major version 987654321)");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadByRefReturnsTerminalStateCase() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create a case in a terminal state.
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123, \"state\":\"CANCELLED\"}");
			Assert.assertNotNull(ref);

			// Some APIs hide terminal state cases; Fetching by ref should not
			CaseInfo info = privateAPI.readCase(ref);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CANCELLED\", \"number\": 123, \"premium\": 2000, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadByCIDExcludesTerminalStateCase() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create a case in a terminal state.
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123, \"state\":\"CANCELLED\"}");
			Assert.assertNotNull(ref);

			// Some APIs hide terminal state cases, including fetching by CID
			try
			{
				privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "123");
				Assert.fail("Expected failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_CID_NOT_EXIST");
				Assert.assertEquals(e.getMessage(), "Case with case identifier does not exist: 123");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadCaseNullRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.readCase(null);
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
		}
	}

	@Test
	public void testReadCaseZeroLengthRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.readCase(new CaseReference(""));
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: ");
		}
	}

	@Test
	public void testReadCaseRefVersionIgnored()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);
			// Change the version in the ref (shouldn't matter in read)
			CaseReference cRef = ref.duplicate();
			cRef.setVersion(12345);
			CaseInfo info = privateAPI.readCase(cRef);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CREATED\", \"number\": 123, \"premium\": 2000, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadCaseThatDoesNotExist()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = new CaseReference("123456789-org.policycorporation.Policy-1-0");
			try
			{
				privateAPI.readCase(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_NOT_EXIST");
				Assert.assertEquals(e.getMessage(), "Case does not exist: 123456789-org.policycorporation.Policy-1-0");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteCase() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);
			CaseInfo info = privateAPI.readCase(ref);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CREATED\", \"number\": 123, \"premium\": 2000, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");

			// Delete the case
			privateAPI.deleteCase(ref);

			// Check it has gone
			try
			{
				privateAPI.readCase(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_NOT_EXIST");
				Assert.assertEquals(e.getMessage(), "Case does not exist: " + ref);
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteCaseNullRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.deleteCase(null);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
		}
	}

	@Test
	public void testDeleteCaseZeroLengthRef()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.deleteCase(new CaseReference(""));
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: ");
		}
	}

	@Test
	public void testDeleteCaseWrongVersion()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ReferenceException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);

			ref.setVersion(654321);

			// Delete wrong version
			try
			{
				privateAPI.deleteCase(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_VERSION_MISMATCH");
				Assert.assertEquals(e.getMessage(),
						"Version in case reference (654321) mismatches actual version (0): " + ref);
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteCaseWrongMajorVersion()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ReferenceException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":999}");
			Assert.assertNotNull(ref);

			ref.setApplicationMajorVersion(66);

			// Read with wrong model major version
			try
			{
				privateAPI.deleteCase(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_NOT_EXIST");
				Assert.assertEquals(e.getMessage(), "Case does not exist: " + ref);
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateCase() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);
			CaseInfo info = privateAPI.readCase(ref);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CREATED\", \"number\": 123, \"premium\": 2000, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");

			// Modify the casedata, changing the 'premium' attribute
			JsonNode root = om.readTree(info.getCasedata());
			JsonNodeFactory fac = JsonNodeFactory.instance;
			((ObjectNode) root).set("premium", fac.numberNode(50));
			String newCasedata = om.writeValueAsString(root);
			CaseReference newRef = privateAPI.updateCase(ref, newCasedata);
			Assert.assertEquals(newRef.getVersion(), 1, "Version number didn't bump");

			// Read back and make sure the changes stuck
			info = privateAPI.readCase(newRef);
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), newRef);
			Assert.assertEquals(info.getCasedata(), "{\"state\": \"CREATED\", \"number\": 123, \"premium\": 50, "
					+ "\"expiresAt\": \"2099-12-31T23:59:59.999Z\", \"policyStartDate\": \"2016-12-25\", "
					+ "\"policyStartTime\": \"00:01:02\", \"termsAndConditions\": \"http://terms.example.com/terms.html\"}");

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	public void updateCaseWrongVersion()
	{
		//TODO
	}

	public void updateCaseInvalidCasedata()
	{
		//TODO
	}

	@Test
	public void testGetByCID() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 3 cases
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456}");
			Assert.assertNotNull(ref2);
			CaseReference ref3 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":789}");
			Assert.assertNotNull(ref3);

			// Find each in turn
			CaseInfo info = privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "123");
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref1, "Wrong ref");

			info = privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "456");
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref2, "Wrong ref");

			info = privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "789");
			Assert.assertNotNull(info);
			Assert.assertEquals(info.getReference(), ref3, "Wrong ref");

			// Attempt to get a case that doesn't exist and check it fails
			info = privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "347536478564563784");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_CID_NOT_EXIST");
			Assert.assertEquals(e.getMessage(), "Case with case identifier does not exist: 347536478564563784");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testGetByCIDZeroLengthTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.readCase(new QualifiedTypeName(""), 1, "123");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: ");
		}
	}

	@Test
	public void testGetByCIDUnqualifiedTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.readCase(new QualifiedTypeName("Brian"), 1, "123");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: Brian");
		}
	}

	@Test
	public void testGetByCIDWrongMajorVersion() throws ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "{\"number\":456}");
			privateAPI.readCase(new QualifiedTypeName("org.policycorporation.Policy"), 999999999, "456");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
			Assert.assertEquals(e.getMessage(), "Unknown type: org.policycorporation.Policy (major version 999999999)");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testGetByCIDNullTypeName() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1, "{\"number\":456}");
			privateAPI.readCase(null, 1, "456");
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_TYPE_INVALID");
			Assert.assertEquals(e.getMessage(), "Type invalid: null");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSearchSanity() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, ArgumentException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 3 cases
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello World\"}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456,\"noClaimsYears\":10}");
			Assert.assertNotNull(ref2);
			CaseReference ref3 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":789,\"noClaimsYears\":10}");
			Assert.assertNotNull(ref3);

			// Simple search: "world" - 1 match
			List<CaseInfo> infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "world", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref1);

			// Simple search: "sausage" - zero matches
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "sausage", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 0);

			// Simple search: "10" - 2 match
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "10", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 2);
			Assert.assertEquals(infos.get(0).getReference(), ref3);
			Assert.assertEquals(infos.get(1).getReference(), ref2);

			// Simple search: "10" - missing top
			try
			{
				infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null, null, "10",
						null);
				Assert.fail("Expected failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_API_TOP_MANDATORY");
				Assert.assertEquals(e.getMessage(), "Top is mandatory");
			}

			// Simple search: "1" - zero matches
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "1", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 0);

			// DQL search: noClaimsYears = 10
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, null, "noClaimsYears = 10");
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 2);
			Assert.assertEquals(infos.get(0).getReference(), ref3);
			Assert.assertEquals(infos.get(1).getReference(), ref2);

			// DQL search: noClaimsYears = 10 (top 1)
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null, 1, null,
					"noClaimsYears = 10");
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref3);

			// DQL search: noClaimsYears = 10 (skip1, top 1)
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, 1, 1, null,
					"noClaimsYears = 10");
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref2);

			// DQL search: noClaimsYears = 10 and number = 789
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, null, "noClaimsYears = 10 and number = 789");
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref3);

			// DQL search - missing top
			try
			{
				infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null, null, null,
						"noClaimsYears = 10");
				Assert.fail("Expected failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_API_TOP_MANDATORY");
				Assert.assertEquals(e.getMessage(), "Top is mandatory");
			}

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadByTypeExcludesTerminalState() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, ArgumentException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 2 cases, one active state and one terminal state
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello World\",\"state\":\"CANCELLED\"}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456,\"noClaimsYears\":10,\"state\":\"CREATED\"}");
			Assert.assertNotNull(ref2);

			// Get all Policies. Should not return the terminal state case
			List<CaseInfo> infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testReadByTypeExcludesTerminalStateAfterCaseUpdated() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, ArgumentException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 2 cases, both active state
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello\",\"state\":\"CREATED\"}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456,\"comments\":\"Hello\",\"state\":\"CREATED\"}");
			Assert.assertNotNull(ref2);

			// Get all Policies. Should return both
			List<CaseInfo> infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 2);
			Assert.assertEquals(infos.get(0).getReference(), ref2);
			Assert.assertEquals(infos.get(1).getReference(), ref1);

			// Update one of the cases, making it terminal state
			privateAPI.updateCase(ref1, "{\"number\":123,\"comments\":\"Hello World\",\"state\":\"CANCELLED\"}");

			// Fetch all Policies again. Only the single active case should be returned
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSimpleSearchExcludesTerminalStateAfterCaseUpdated() throws DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException, ArgumentException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 2 cases, both active state
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello\",\"state\":\"CREATED\"}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456,\"comments\":\"Hello\",\"state\":\"CREATED\"}");
			Assert.assertNotNull(ref2);

			// Get all Policies matching simple search 'Hello'. Should return both
			List<CaseInfo> infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "Hello", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 2);
			Assert.assertEquals(infos.get(0).getReference(), ref2);
			Assert.assertEquals(infos.get(1).getReference(), ref1);

			// Update one of the cases, making it terminal state
			privateAPI.updateCase(ref1, "{\"number\":123,\"comments\":\"Hello\",\"state\":\"CANCELLED\"}");

			// Fetch all Policies again. Only the single active case should be returned
			infos = privateAPI.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
					Integer.MAX_VALUE, "Hello", null);
			Assert.assertNotNull(infos);
			Assert.assertEquals(infos.size(), 1);
			Assert.assertEquals(infos.get(0).getReference(), ref2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateMulti() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, ArgumentException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 2 cases
			CaseReference ref1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello World\"}");
			Assert.assertNotNull(ref1);
			CaseReference ref2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":456}");
			Assert.assertNotNull(ref2);

			List<CaseReference> newRefs = privateAPI.updateCases(Arrays.asList(ref1, ref2),
					Arrays.asList("{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":1}",
							"{\"number\":456,\"noClaimsYears\":2}"));

			// Read each case and check the changes stuck
			CaseInfo info1 = privateAPI.readCase(ref1);
			CaseInfo info2 = privateAPI.readCase(ref2);

			// Bump version in original refs and check the update caused the same.
			CaseReference cr1 = ref1.duplicate();
			cr1.setVersion(cr1.getVersion() + 1);
			CaseReference cr2 = ref2.duplicate();
			cr2.setVersion(cr2.getVersion() + 1);

			// Check refs from reading back the cases
			Assert.assertEquals(info1.getReference(), cr1);
			Assert.assertEquals(info2.getReference(), cr2);

			// Also check refs returned from the update call
			Assert.assertEquals(newRefs.get(0), cr1);
			Assert.assertEquals(newRefs.get(1), cr2);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateMultiNullRefList()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.updateCases(null, null);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_REF_LIST_INVALID");
			Assert.assertEquals(e.getMessage(), "Case reference list must be non-null and contain at least one value");
		}
	}

	@Test
	public void testUpdateMultiEmptyRefList()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			privateAPI.updateCases(Collections.emptyList(), null);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_REF_LIST_INVALID");
			Assert.assertEquals(e.getMessage(), "Case reference list must be non-null and contain at least one value");
		}
	}

	@Test
	public void testUpdateMultiNullCasedataList()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			// Don't care about first arg, as long as it's non-empty
			privateAPI.updateCases(Collections.singletonList(new CaseReference("1-com.example.sausage.Roll-0-0")),
					null);
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_CASEDATA_LIST_INVALID");
			Assert.assertEquals(e.getMessage(), "Casedata list must be non-null and contain at least one value");
		}
	}

	@Test
	public void testUpdateMultiEmptyCasedataList()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			// Don't care about first arg, as long as it's non-empty
			privateAPI.updateCases(Collections.singletonList(new CaseReference("1-com.example.sausage.Roll-0-0")),
					Collections.emptyList());
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_CASEDATA_LIST_INVALID");
			Assert.assertEquals(e.getMessage(), "Casedata list must be non-null and contain at least one value");
		}
	}

	@Test
	public void testUpdateMultiListSizeMismatch()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		try
		{
			// Don't care about first arg, as long as it's non-empty
			privateAPI.updateCases(Collections.singletonList(new CaseReference("1-com.example.sausage.Roll-0-0")),
					Arrays.asList("1", "2"));
			Assert.fail("Expected failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_API_REF_AND_CASEDATA_LISTS_SIZE_MISMATCH");
			Assert.assertEquals(e.getMessage(), "Case reference and casedata lists must be the same size");
		}
	}

	@Test
	public void testUpdateWrongVersion()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ArgumentException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello World\"}");
			Assert.assertNotNull(ref);

			// Update (version 0 to 1)
			privateAPI.updateCase(ref, "{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":1}");
			try
			{

				// Attempt an update using the out-of-date ref
				privateAPI.updateCase(ref, "{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":2}");
				Assert.fail("Expected failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_VERSION_MISMATCH");
				Assert.assertEquals(e.getMessage(),
						"Version in case reference (0) mismatches actual version (1): " + ref);
			}

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateInvalidCasedata()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ArgumentException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"comments\":\"Hello World\"}");
			Assert.assertNotNull(ref);

			try
			{

				// Attempt an update using invalid data (noClaimsYears should be a number)
				privateAPI.updateCase(ref,
						"{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":\"Loads!\"}");
				Assert.fail("Expected failure");
			}
			catch (ValidationException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
				Assert.assertEquals(e.getMessage(), "Data is not a valid instance of the specified type: "
						+ "[noClaimsYears -> Expected NumericNode but found TextNode: \"Loads!\"]");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUpdateTerminalStateCase() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123, \"state\":\"CANCELLED\"}");
			Assert.assertNotNull(ref);

			try
			{

				// Attempt an update. Not allowed in CANCELLED state.
				privateAPI.updateCase(ref, "{\"number\":123,\"comments\":\"Hello World\",\"noClaimsYears\":10}");
				Assert.fail("Expected failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_TERMINAL_STATE_PREVENTS_UPDATE");
				Assert.assertEquals(e.getMessage(), "Case is in a terminal state so cannot be updated: " + ref);
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
