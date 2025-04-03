package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.CaseDataManager;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

/**
 * Tests that were historically testing the validate(...) API, which has been removed.
 * They now call processData instead, with all options disabled, 
 * such that it behaves like the old validate(...) API.
 * @since 2019
 * @author smorgan
 */
public class PrivateAPIValidationTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testSuccess() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid
			privateAPI.validate(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"premium\":1000}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUnexpectedProperty() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid
			privateAPI.validate(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123,\"noSuchAttribute\":123}");
			Assert.fail("Expected validation to fail");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
			Assert.assertEquals(e.getMessage(), "Data is not a valid instance of the specified type: "
					+ "[noSuchAttribute -> Type has no attribute called 'noSuchAttribute']", "Wrong message");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSuccessNonCaseType() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid
			privateAPI.validate(new QualifiedTypeName("org.policycorporation.Address"), 1,
					"{\"firstLine\":\"1 High Street\",\"secondLine\":\"Casetown\"}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testNonCaseTypeBadText() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid
			privateAPI.validate(new QualifiedTypeName("org.policycorporation.Address"), 1,
					"{\"firstLine\":\"1 High Street\",\"secondLine\":123}");
			Assert.fail("Expected validation to fail");
		}
		catch (ValidationException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
			Assert.assertEquals(e.getMessage(), "Data is not a valid instance of the specified type: "
					+ "[secondLine -> Expected TextNode but found IntNode: 123]", "Wrong message");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testFailureBadNumber() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Invalid
			try
			{
				privateAPI.validate(new QualifiedTypeName("org.policycorporation.Policy"), 1,
						"{\"number\":456,\"premium\":\"Not A Number\"}");
				Assert.fail("Expected validation to fail");
			}
			catch (ValidationException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
				Assert.assertEquals(e.getMessage(),
						"Data is not a valid instance of the specified type: "
								+ "[premium -> Expected NumericNode but found TextNode: \"Not A Number\"]",
						"Wrong message");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testFailureBadText() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Invalid
			try
			{
				privateAPI.validate(new QualifiedTypeName("org.policycorporation.Policy"), 1,
						"{\"number\":888,\"comments\":123}");
				Assert.fail("Expected validation to fail");
			}
			catch (ValidationException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_DATA_INVALID");
				Assert.assertEquals(e.getMessage(), "Data is not a valid instance of the specified type: "
						+ "[comments -> Expected TextNode but found IntNode: 123]", "Wrong message");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testInvalidNamespace() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Invalid
			try
			{
				privateAPI.validate(new QualifiedTypeName("this.is.not.a.valid.type"), 1, "{}");
				Assert.fail("Expected validation to fail");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_NAMESPACE");
				Assert.assertEquals(e.getMessage(), "Unknown namespace: this.is.not.a.valid (major version 1)",
						"Wrong message");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testInvalidName() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Invalid
			try
			{
				privateAPI.validate(new QualifiedTypeName("org.policycorporation.NamespaceFineButClassNameNot!"), 1,
						"{}");
				Assert.fail("Expected validation to fail");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
				Assert.assertEquals(e.getMessage(),
						"Unknown type: org.policycorporation.NamespaceFineButClassNameNot! (major version 1)",
						"Wrong message");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testWrongMajorVersion() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Invalid
			try
			{
				privateAPI.validate(new QualifiedTypeName("org.policycorporation.Policy"), 12345678, "{}");
				Assert.fail("Expected validation to fail");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_NAMESPACE");
				Assert.assertEquals(e.getMessage(), "Unknown namespace: org.policycorporation (major version 12345678)",
						"Wrong message");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
