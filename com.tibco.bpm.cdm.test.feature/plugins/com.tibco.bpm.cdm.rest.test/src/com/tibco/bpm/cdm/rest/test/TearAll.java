package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;

import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.rest.functions.DirectoryEngineFunctions;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class TearAll extends Utils
{
	private DirectoryEngineFunctions de = new DirectoryEngineFunctions();

	//Test to force undploy all the applications deployed by the tests
	@Test(description = "Test to force undploy all the applications deployed by the tests")
	public void tearAll() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		forceUndeploy(BigInteger.valueOf(1));
		forceUndeploy(BigInteger.valueOf(2));
		forceUndeploy(BigInteger.valueOf(3));
		forceUndeploy(BigInteger.valueOf(4));
		forceUndeploy(BigInteger.valueOf(5));
		forceUndeploy(BigInteger.valueOf(6));
		forceUndeploy(BigInteger.valueOf(7));
		forceUndeploy(BigInteger.valueOf(8));
		forceUndeploy(BigInteger.valueOf(9));
		forceUndeploy(BigInteger.valueOf(10));
		forceUndeploy(BigInteger.valueOf(11));
		forceUndeploy(BigInteger.valueOf(12));
		forceUndeploy(BigInteger.valueOf(13));
		forceUndeploy(BigInteger.valueOf(14));
		forceUndeploy(BigInteger.valueOf(15));
		forceUndeploy(BigInteger.valueOf(16));
		forceUndeploy(BigInteger.valueOf(17));
		forceUndeploy(BigInteger.valueOf(18));
	}

	//Test to delete the container created
	@Test(description = "Test to delete the container created")
	public void tearAllContainer() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, InterruptedException
	{
		de.deleteContainer(CONTAINER_NAME);
	}
}
