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

public class PrivateAPIProcessDataTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testSuccessRemoveNulls() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid, nulls to remove
			String data = privateAPI.processData(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":75,\"premium\":null,\"policyStartTime\":null,\"policyStartDate\":null,"
							+ "\"expiresAt\":null,\"noClaimsYears\":null,\"termsAndConditions\":null"
							+ ",\"comments\":null,\"claims\":null,\"legalCover\":null}",
					true);
			Assert.assertEquals(data, "{\"number\":75}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSuccessRemoveNullsFromArrays() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid, nulls to remove
			String data = privateAPI
					.processData(new QualifiedTypeName("org.policycorporation.Person"), 1,
							"{\"pcode\":\"The Mighty Rob\", \"personState\": \"ALIVE\", "
									+ "\"aliases\":[\"Bob\", null, \"Robert\"], \"otherAddresses\":[{},null,{}]}",
							true);
			Assert.assertEquals(data,
					"{\"pcode\":\"The Mighty Rob\",\"personState\":\"ALIVE\",\"aliases\":[\"Bob\",\"Robert\"],"
							+ "\"otherAddresses\":[{},{}]}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSuccessDoNotRemoveNulls() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException, ValidationException,
			com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid, nulls to remove, but not asking to remove it
			String data = privateAPI.processData(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":75,\"premium\":null,\"policyStartTime\":null,\"policyStartDate\":null,"
							+ "\"expiresAt\":null,\"noClaimsYears\":null,\"termsAndConditions\":null"
							+ ",\"comments\":null,\"claims\":null,\"legalCover\":null}",
					false);
			Assert.assertEquals(data,
					"{\"number\":75,\"premium\":null,\"policyStartTime\":null,\"policyStartDate\":null"
							+ ",\"expiresAt\":null,\"noClaimsYears\":null,\"termsAndConditions\":null"
							+ ",\"comments\":null,\"claims\":null,\"legalCover\":null}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSuccessRemoveNullsInNestedObjects() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			ValidationException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid, nulls to remove
			String data = privateAPI.processData(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":95,\"claims\":[{\"date\":null,\"blame\":null,\"description\":null}]}", true);
			Assert.assertEquals(data, "{\"number\":95,\"claims\":[{}]}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSuccessDoNotRemoveNullsInNestedObjects() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			ValidationException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Valid, nulls to remove
			String data = privateAPI.processData(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":95,\"claims\":[{\"date\":null,\"blame\":null,\"description\":null}]}", false);
			Assert.assertEquals(data,
					"{\"number\":95,\"claims\":[{\"date\":null,\"blame\":null,\"description\":null}]}");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
