package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

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
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

/**
 * Tests private API methods relating to case links
 * @author smorgan
 * @since 2019
 */
public class PrivateAPILinkTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("cdmPrivateAPI")
	private CaseDataManager		privateAPI;

	@Test
	public void testLinkSameTargetTwice() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference per1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Bob Lamambe\", \"age\":49}");
			Assert.assertNotNull(per1);
			CaseReference pol1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":111}");
			Assert.assertNotNull(pol1);

			privateAPI.linkCase(per1, "policies", pol1);
			try
			{
				privateAPI.linkCase(per1, "policies", pol1);
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_LINK_ALREADY_LINKED");
				Assert.assertEquals(e.getMessage(),
						"Case " + per1 + " is already linked to " + pol1 + " via link 'policies'");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkSameTargetTwiceInOneCall() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference per1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Bob Lamambe\", \"age\":49}");
			Assert.assertNotNull(per1);
			CaseReference pol1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":111}");
			Assert.assertNotNull(pol1);

			privateAPI.linkCase(per1, "policies", pol1);
			try
			{
				privateAPI.linkCases(per1, "policies", Arrays.asList(pol1, pol1));
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_DUPLICATE_LINK_TARGET");
				Assert.assertEquals(e.getMessage(),
						"Case '" + per1 + "' cannot be linked twice to '" + pol1 + "' via link 'policies'");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkNullRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException
	{
		try
		{
			privateAPI.linkCase(null, null, null);
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
			Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
		}
	}

	@Test
	public void testLinkNullTargetRef() throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException,
			DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			try
			{
				privateAPI.linkCase(new CaseReference("1-org.policycorporation.Person-1-0"), null, null);
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_INVALID_FORMAT");
				Assert.assertEquals(e.getMessage(), "Invalid case reference format: null");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkNullName()
			throws ArgumentException, com.tibco.bpm.cdm.api.exception.InternalException, DeploymentException,
			PersistenceException, InternalException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			try
			{
				privateAPI.linkCase(new CaseReference("1-org.policycorporation.Person-1-0"), null,
						new CaseReference("1-org.policycorporation.Person-1-0"));
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_API_LINK_NAME_MANDATORY");
				Assert.assertEquals(e.getMessage(), "Link name must be specified");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testUnknownLinkName() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			privateAPI.linkCase(new CaseReference("1-org.policycorporation.Person-1-0"), "shirtbox",
					new CaseReference("1-org.policycorporation.Person-1-0"));
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getCode(), "CDM_REFERENCE_LINK_NAME_NOT_EXIST");
			Assert.assertEquals(e.getMessage(),
					"Type 'org.policycorporation.Person' (major version 1) does not have a link called 'shirtbox'");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkTargetWrongType() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			CaseReference per1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Bob Lamambe\", \"age\":49}");
			try
			{
				privateAPI.linkCase(per1, "policies", per1);
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getCode(), "CDM_REFERENCE_LINK_WRONG_TYPE");
				Assert.assertEquals(e.getMessage(), "Case " + per1 + " cannot be linked to " + per1
						+ " as target case is the wrong type for 'policies'");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkSanity() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ArgumentException,
			com.tibco.bpm.cdm.api.exception.InternalException, ValidationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 3 Person
			CaseReference per1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Bob Lamambe\", \"age\":49}");
			Assert.assertNotNull(per1);
			CaseReference per2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Henry Lestick\", \"age\":48}");
			Assert.assertNotNull(per2);
			CaseReference per3 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Person"), 1,
					"{\"personState\":\"ALIVE\", \"name\":\"Buzz\", \"age\":47}");
			Assert.assertNotNull(per3);

			// Navigate (no links)
			List<CaseReference> targetRefs = privateAPI.navigateLinks(per1, "isParentOf", null, Integer.MAX_VALUE, null,
					null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 0);

			// Link 1 to 2
			privateAPI.linkCase(per1, "isParentOf", per2);

			// Navigate
			targetRefs = privateAPI.navigateLinks(per1, "isParentOf", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), per2);

			// Navigate from the other end
			targetRefs = privateAPI.navigateLinks(per2, "isChildOf", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), per1);

			// Link 1 to 3
			privateAPI.linkCase(per1, "isParentOf", per3);

			// Navigate - check it has 2 children
			targetRefs = privateAPI.navigateLinks(per1, "isParentOf", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 2);
			Assert.assertEquals(targetRefs.get(0), per2);
			Assert.assertEquals(targetRefs.get(1), per3);

			// Create 3 Policy
			CaseReference pol1 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":111}");
			Assert.assertNotNull(pol1);
			CaseReference pol2 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":222}");
			Assert.assertNotNull(pol2);
			CaseReference pol3 = privateAPI.createCase(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					"{\"number\":333}");
			Assert.assertNotNull(pol3);

			// Link per1 to all three policies
			privateAPI.linkCases(per1, "policies", Arrays.asList(pol1, pol2, pol3));

			// and navigate it
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 3);
			Assert.assertEquals(targetRefs.get(0), pol1);
			Assert.assertEquals(targetRefs.get(1), pol2);
			Assert.assertEquals(targetRefs.get(2), pol3);

			// Navigate with pagination
			targetRefs = privateAPI.navigateLinks(per1, "policies", 0, 1, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol1);
			targetRefs = privateAPI.navigateLinks(per1, "policies", 1, 1, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol2);
			targetRefs = privateAPI.navigateLinks(per1, "policies", 2, 1, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol3);

			// Navigate from each of the 3 policies back to the policyholder
			targetRefs = privateAPI.navigateLinks(pol1, "holder", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), per1);
			targetRefs = privateAPI.navigateLinks(pol2, "holder", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), per1);
			targetRefs = privateAPI.navigateLinks(pol1, "holder", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), per1);

			// Navigate with simple search for '111'
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, "111", null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol1);

			// Navigate with simple search for '222'
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, "222", null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol2);

			// Navigate with simple search for '333'
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, "333", null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol3);

			// Navigate with simple search expecting no matches
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE,
					"No matches for this, at least not as far as I recall from when I was writing "
							+ "the lines of code above this! \"Hello!\", not 'goodbye'",
					null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 0);

			// Unlink the second Policy
			privateAPI.unlinkCase(per1, "policies", pol2);

			// Check we're left with the 1st and 3rd
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 2);
			Assert.assertEquals(targetRefs.get(0), pol1);
			Assert.assertEquals(targetRefs.get(1), pol3);

			// Unlink all the remainders
			privateAPI.unlinkCases(per1, "policies");

			// Check all links have gone
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 0);

			// Link all 3 again and check
			privateAPI.linkCases(per1, "policies", Arrays.asList(pol1, pol2, pol3));
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 3);

			// Unlink the 1st and 3rd via a list of refs
			privateAPI.unlinkCases(per1, "policies", Arrays.asList(pol1, pol3));

			// and check we're left with just the 2nd
			targetRefs = privateAPI.navigateLinks(per1, "policies", null, Integer.MAX_VALUE, null, null);
			Assert.assertNotNull(targetRefs);
			Assert.assertEquals(targetRefs.size(), 1);
			Assert.assertEquals(targetRefs.get(0), pol2);

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

}
