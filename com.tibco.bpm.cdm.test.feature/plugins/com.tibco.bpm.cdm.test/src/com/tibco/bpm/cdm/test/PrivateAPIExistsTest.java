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

public class PrivateAPIExistsTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testExistsWhenExists() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":999}");
			Assert.assertNotNull(ref);
			boolean exists = privateAPI.exists(ref);
			Assert.assertTrue(exists, "Expected case to exist");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testExistsWhenNotExists() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Type exists, so request is valid, but this id doesn't match a case.
			CaseReference ref = new CaseReference("12345678901234567890-org.policycorporation.Policy-1-0");
			boolean exists = privateAPI.exists(ref);
			Assert.assertFalse(exists, "Expected case to not exist");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testExistsNullRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.exists(new CaseReference(null));
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
		}
	}

	@Test
	public void testExistsZeroLengthRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.exists(new CaseReference(""));
			Assert.fail("Expected case reference failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: ");
		}
	}

	@Test
	public void testExistsVersionIgnored() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference ref = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":123}");
			Assert.assertNotNull(ref);
			// Change the version in the ref (shouldn't matter in read)
			ref.setVersion(12345);
			boolean exists = privateAPI.exists(ref);
			Assert.assertTrue(exists, "Didn't expect mismatching version number to prevent existence check");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testExistsWrongMajorVersion()
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

			// Type exists, but not at version 66.
			try
			{
				privateAPI.exists(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
				Assert.assertEquals(e.getMessage(), "Unknown type: org.policycorporation.Policy (major version 66)");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testExistsButTypoInTypeName()
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

			// Mess up the type name and ensure an appropriate exception occurs. Test case in
			// memory of Mr Michael Atkins.
			CaseReference tweakedRef = new CaseReference(new QualifiedTypeName("org.policycorporation.PollySee"),
					ref.getApplicationMajorVersion(), ref.getId(), ref.getVersion());
			ref = tweakedRef;

			// Case with this id exists, but the type name doesn't match
			try
			{
				privateAPI.exists(ref);
				Assert.fail("Expecting failure");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
				Assert.assertEquals(e.getMessage(), "Unknown type: org.policycorporation.PollySee (major version 1)");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testExistsUnknownTypeNamespace()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, ValidationException,
			ReferenceException, DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		try
		{
			privateAPI.exists(new CaseReference("999999999-org.mythicalcorporation.Unicorn-1-0"));
			Assert.fail("Expecting failure");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_UNKNOWN_TYPE");
			Assert.assertEquals(e.getMessage(), "Unknown type: org.mythicalcorporation.Unicorn (major version 1)");
		}
	}

	@Test
	public void testExistsUnknownTypeName() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			ValidationException, ReferenceException, DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			privateAPI.exists(
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

}
