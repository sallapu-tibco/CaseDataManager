package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.CaseDataManager;
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

public class PrivateAPIIsActiveTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testIsActiveWhenInActiveState()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":999}, \"state\":\"CREATED\"}");
			Assert.assertNotNull(ref);
			boolean active = privateAPI.isActive(ref);
			Assert.assertTrue(active, "Expected case to be active, given it is CREATED state");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testIsActiveWhenInTerminalState()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":999, \"state\":\"CANCELLED\"}");
			Assert.assertNotNull(ref);
			boolean active = privateAPI.isActive(ref);
			Assert.assertFalse(active, "Expected case to be inactive, given it is CANCELLED state");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testIsActiveUnknownTypeNamespace() throws com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.isActive(new CaseReference("999999999-org.mythicalcorporation.Unicorn-1-0"));
			Assert.fail("Expecting failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
			Assert.assertEquals(e.getMessage(), "Unknown type: org.mythicalcorporation.Unicorn (major version 1)");
		}
	}

	@Test
	public void testIsActiveUnknownTypeName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ReferenceException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			privateAPI.isActive(
					new CaseReference("8888-org.policycorporation.NoTypeCalledThisInOrgPolicyCorporation-1-0"));
			Assert.fail("Expecting failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
			Assert.assertEquals(e.getMessage(),
					"Unknown type: org.policycorporation.NoTypeCalledThisInOrgPolicyCorporation (major version 1)");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testIsActiveWrongMajorVersion()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ReferenceException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":441362}");
			Assert.assertNotNull(ref);

			ref.setApplicationMajorVersion(454545);

			// Type exists, but not at version 454545.
			try
			{
				privateAPI.isActive(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
				Assert.assertEquals(e.getMessage(),
						"Unknown type: org.policycorporation.Policy (major version 454545)");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testIsActiveVersionIgnored()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Defaults to CREATED state
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":777777}");
			Assert.assertNotNull(ref);
			// Change the version in the ref (shouldn't matter in read)
			ref.setVersion(55555);
			boolean isActive = privateAPI.isActive(ref);
			Assert.assertTrue(isActive, "Didn't expect mismatching version number to prevent isActive check");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testIsActiveNullRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.isActive(null);
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
		}
	}

	@Test
	public void testIsActiveZeroLengthRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.isActive(new CaseReference(""));
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: ");
		}
	}
}
