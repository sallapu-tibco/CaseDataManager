package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CDMErrorData;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.NotAuthorisedException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.core.dto.CaseLinkDTO;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CaseLinkTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	@Test
	public void testLinkSanity() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 2x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1,
					Arrays.asList("{\"state\":\"CREATED\",\"number\":777}", "{\"state\":\"CREATED\",\"number\":888}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 2);

			// Fetch links (none exist)
			List<CaseLinkDTO> links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertTrue(links == null || links.isEmpty(), "Not expecting links");

			// Create 2 Persons
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 2);

			// Attempt to link one Policy to the other via 'holder'
			// Should fail, as the type needs to be Person.
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("holder", polRefs.get(1))));
				Assert.fail("Expected failure as type wrong");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_LINK_WRONG_TYPE.getCode(),
						"Wrong error code");
			}

			// Attempt to link via a non-existent link name
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("noLinkCalledThis!", perRefs.get(0))));
				Assert.fail("Expected failure as link name wrong");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_NAME_NOT_EXIST.getCode(), "Wrong error code");
			}

			// Attempt to link the same Person twice (in a single call) as a namedDriver
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Arrays.asList(new CaseLinkDTO("namedDrivers", perRefs.get(0)),
								new CaseLinkDTO("namedDrivers", perRefs.get(0))));
				Assert.fail("Expected failure as can't link to duplicate refs");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_DUPLICATE_LINK_TARGET.getCode(), "Wrong error code");
			}

			// Link Policy to Person via 'holder' link
			caseManager.createCaseLinks(polRefs.get(0),
					Collections.singletonList(new CaseLinkDTO("holder", perRefs.get(0))));

			// ...and again, which should fail (can't link same thing twice).
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("holder", perRefs.get(0))));
				Assert.fail("Expected failure as already linked");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_ALREADY_LINKED.getCode(), "Wrong error code");
			}

			// Attempt to link a second Person via 'holder' (not allowed - it's not an array)
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("holder", perRefs.get(1))));
				Assert.fail("Expected failure as not an array and is already linked");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_LINK_NOT_ARRAY.getCode(),
						"Wrong error code");
			}

			// Fetch links for Policy (should see the Person via 'holder')
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 1, "Expecting a link");
			Assert.assertEquals(links.get(0).getCaseReference(), perRefs.get(0));
			Assert.assertEquals(links.get(0).getName(), "holder");

			// Fetch from the other end, the Person (should see the Policy via 'policies')
			links = caseManager.getCaseLinks(perRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 1, "Expecting a link");
			Assert.assertEquals(links.get(0).getName(), "policies");

			// Link both Persons via the Policy's 'namedDrivers' link (allowed, as it's an array)
			caseManager.createCaseLinks(polRefs.get(0), Arrays.asList(new CaseLinkDTO("namedDrivers", perRefs.get(0)),
					new CaseLinkDTO("namedDrivers", perRefs.get(1))));

			// Fetch links for the Policy. Now there should be 'holder' and 2 x 'namedDrivers'
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 3, "Expecting 3 links");

			// Delete the Policy, which should implicitly remove all links to/from it
			caseManager.deleteCase(polRefs.get(0));
			links = caseManager.getCaseLinks(perRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 0, "Not expecting links after deleting Policy");
			links = caseManager.getCaseLinks(perRefs.get(1), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 0, "Not expecting links after deleting Policy");
			links = caseManager.getCaseLinks(polRefs.get(1), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 0, "Not expecting links after deleting Policy");

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkForCaseThatDoesNotExist()
			throws PersistenceException, InternalException, ReferenceException, ArgumentException,
			NotAuthorisedException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			caseManager.getCaseLinks(new CaseReference("123456789-org.policycorporation.Policy-1-0"), null, null,
					Integer.MAX_VALUE, null, null);
			Assert.fail("Expected failure");
		}
		catch (ReferenceException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_NOT_EXIST.getCode(),
					"Wrong error code");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkForTypeThatDoesNotExist()
			throws PersistenceException, InternalException, ReferenceException, ArgumentException,
			NotAuthorisedException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			caseManager.getCaseLinks(new CaseReference("123456789-org.policycorporation.DonkeyKong-1-0"), null, null,
					Integer.MAX_VALUE, null, null);
			Assert.fail("Expected failure");
		}
		catch (ReferenceException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_UNKNOWN_TYPE.getCode(),
					"Wrong error code");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkTwoNamesInOneCall() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 2x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 2);

			// Link the first Person to the Policy (via 'policies') and the other Person (via 'isParentOf') in a single
			// call to prove that this mixture works.
			caseManager.createCaseLinks(perRefs.get(0), Arrays.asList(new CaseLinkDTO("policies", polRefs.get(0)),
					new CaseLinkDTO("isParentOf", perRefs.get(1))));

			// Three case are now linked like this:
			// Policy0 <--> Person0 <--> Person 1

			// Read back
			List<CaseLinkDTO> links = caseManager.getCaseLinks(perRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertEquals(links.size(), 2, "Wrong link count");
			Assert.assertTrue(links.stream()
					.anyMatch(l -> l.getCaseReference().equals(polRefs.get(0)) && l.getName().equals("policies")));
			Assert.assertTrue(links.stream()
					.anyMatch(l -> l.getCaseReference().equals(perRefs.get(1)) && l.getName().equals("isParentOf")));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteAll() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, ArgumentException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 2x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 2);

			// Link the Policy to the first Person via the namedDriver/allowedToDrive link
			caseManager.createCaseLinks(polRefs.get(0), Arrays.asList(new CaseLinkDTO("namedDrivers", perRefs.get(0))));

			// Link that same Person to the other Person via isChildOf
			caseManager.createCaseLinks(perRefs.get(0), Arrays.asList(new CaseLinkDTO("isChildOf", perRefs.get(1))));

			// Three case are now linked like this:
			// Policy0 <--> Person0 <--> Person 1

			// Delete all links from Person0. This exercises deletion of links where the origin case is either
			// 'end 1' or 'end 2' of the link.
			caseManager.deleteCaseLinks(perRefs.get(0), null, null);

			// Check links have gone.
			List<CaseLinkDTO> links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertEquals(links.size(), 0, "Not expecting links");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testManyToMany() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// 10 people, each a named driver on all 10 policies (100 case links total)

			// Create 10x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1,
					Arrays.asList("{\"state\":\"CREATED\",\"number\":1}", "{\"state\":\"CREATED\",\"number\":2}",
							"{\"state\":\"CREATED\",\"number\":3}", "{\"state\":\"CREATED\",\"number\":4}",
							"{\"state\":\"CREATED\",\"number\":5}", "{\"state\":\"CREATED\",\"number\":6}",
							"{\"state\":\"CREATED\",\"number\":7}", "{\"state\":\"CREATED\",\"number\":8}",
							"{\"state\":\"CREATED\",\"number\":9}", "{\"state\":\"CREATED\",\"number\":10}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 10);

			// Create 10x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1,
					Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 10);

			// We'll exercise this from both ends, so first:
			// Link the first 5 policies to all 10 persons
			for (int i = 0; i < 5; i++)
			{
				caseManager.createCaseLinks(polRefs.get(i),
						perRefs.stream().map(r -> new CaseLinkDTO("namedDrivers", r)).collect(Collectors.toList()));
			}

			// Now, from the person end, link all 10 persons to the 2nd five policies
			List<CaseReference> secondFivePolicies = polRefs.subList(5, 10);
			for (CaseReference perRef : perRefs)
			{
				caseManager.createCaseLinks(perRef, secondFivePolicies.stream()
						.map(r -> new CaseLinkDTO("allowedToDrive", r)).collect(Collectors.toList()));
			}

			// Fetch links for each Policy - All ten should be linked to 10 persons.
			for (CaseReference polRef : polRefs)
			{
				List<CaseLinkDTO> links = caseManager.getCaseLinks(polRef, null, null, Integer.MAX_VALUE, null, null);
				Assert.assertEquals(links.size(), 10);
			}

			// Fetch links for each Person - All ten should be linked to 10 policies.
			for (CaseReference perRef : perRefs)
			{
				List<CaseLinkDTO> links = caseManager.getCaseLinks(perRef, null, null, Integer.MAX_VALUE, null, null);
				Assert.assertEquals(links.size(), 10);
			}

		}
		finally
		{
			forceUndeploy(deploymentId);
		}

	}

	@Test
	public void testNoTop() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			try
			{
				caseManager.getCaseLinks(polRefs.get(0), null, null, null, null, null);
				Assert.fail("Expected failure due to lack of top");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_API_TOP_MANDATORY.getCode(),
						"Wrong error code");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testWrongTypeForTargetCaseId() throws PersistenceException, InternalException, ArgumentException,
			ReferenceException, DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 2x Persons
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 2);

			// Link the first Person to the Policy, but change the id part of the Policy's ref to be the id
			// of the 2nd person
			CaseReference dodgyRef = new CaseReference(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					perRefs.get(1).getId(), 0);

			try
			{
				caseManager.createCaseLinks(perRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("policies", dodgyRef)));
				Assert.fail("Expected failure as target ref has id of a case of a different type");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_REFERENCE_NOT_EXIST.getCode(),
						"Wrong error code");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testOppositeMultiplicityCheck() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ReferenceException,
			DataModelSerializationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 2x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 2);

			// A Policy can only have one 'holder', so attempt to violate this
			// by linking from the other end twice (Person's 'policies' link)
			caseManager.createCaseLinks(perRefs.get(0),
					Collections.singletonList(new CaseLinkDTO("policies", polRefs.get(0))));
			try
			{
				caseManager.createCaseLinks(perRefs.get(1),
						Collections.singletonList(new CaseLinkDTO("policies", polRefs.get(0))));
				Assert.fail("Expected failure as Policy already has a holder");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_OPPOSITE_NOT_ARRAY.getCode(), "Wrong error code");
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteByRefWithoutName() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 1x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 1);

			caseManager.createCaseLinks(polRefs.get(0),
					Collections.singletonList(new CaseLinkDTO("namedDrivers", perRefs.get(0))));

			caseManager.deleteCaseLinks(polRefs.get(0), Collections.singletonList(perRefs.get(0)), null);
			Assert.fail("Expected failure due to lack of name");
		}
		catch (ArgumentException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), CDMErrorData.CDM_API_LINK_REFS_WITHOUT_NAME.getCode(),
					"Wrong error code");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}

	}

	@Test
	public void testDeleteIndividual() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Fetch links (none exist)
			List<CaseLinkDTO> links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertTrue(links == null || links.isEmpty(), "Not expecting links");

			// Create 10x Person
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1,
					Arrays.asList("{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 10);

			// Link Policy to all 10 Person
			caseManager.createCaseLinks(polRefs.get(0),
					perRefs.stream().map(r -> new CaseLinkDTO("namedDrivers", r)).collect(Collectors.toList()));

			// Remove each link, one at a time, verifying that the link count reduces accordingly
			while (!perRefs.isEmpty())
			{
				links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
				Assert.assertEquals(links.size(), perRefs.size(), "Wrong link count");

				caseManager.deleteCaseLinks(polRefs.get(0), Collections.singletonList(perRefs.remove(0)),
						"namedDrivers");
			}

			// Fetch Policy's links (0 exist)
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 0, "Wrong link count");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}

	}

	@Test
	public void testBulk() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Fetch links (none exist)
			List<CaseLinkDTO> links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertTrue(links == null || links.isEmpty(), "Not expecting links");

			// To perform adhoc testing of even larger volumes, just make a local edit to this:
			int batchCount = 100;

			// Create many Person
			List<String> personCasedatas = new ArrayList<>();
			for (int i = 0; i < batchCount; i++)
			{
				personCasedatas.addAll(Arrays.asList("{\"personState\":\"ALIVE\", \"age\":25}",
						"{\"personState\":\"ALIVE\", \"age\":null}", "{\"personState\":\"ALIVE\"}",
						"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
						"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
						"{\"personState\":\"ALIVE\"}"));
			}
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, personCasedatas);
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), batchCount * 10);

			// Link Policy to all Persons
			caseManager.createCaseLinks(polRefs.get(0),
					perRefs.stream().map(r -> new CaseLinkDTO("namedDrivers", r)).collect(Collectors.toList()));

			// Navigate to all Persons
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), batchCount * 10, "Wrong number of links returned");

			try
			{
				// Navigate to a non-existent link name (without DQL)
				links = caseManager.getCaseLinks(polRefs.get(0), "noLinkCalledThis", null, Integer.MAX_VALUE, null,
						null);
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_NAME_NOT_EXIST.getCode(), "Wrong error code");
			}

			try
			{
				// Navigate to a non-existent link name (with DQL)
				links = caseManager.getCaseLinks(polRefs.get(0), "noLinkCalledThis", null, Integer.MAX_VALUE, null,
						"personState = 'ALIVE'");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_NAME_NOT_EXIST.getCode(), "Wrong error code");
			}

			// Navigate to all alive Persons (10 per batch)
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"personState = 'ALIVE'");
			Assert.assertEquals(links.size(), batchCount * 10, "Wrong number of links returned");

			// Navigate to all dead Persons (0 per batch)
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"personState = 'DEAD'");
			Assert.assertEquals(links.size(), batchCount * 0, "Wrong number of links returned");

			// Navigate to all 25 year-old Persons (1 per batch)
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null, "age = 25");
			Assert.assertEquals(links.size(), batchCount * 1, "Wrong number of links returned");

			// Navigate to all Persons with no age set (9 per batch)
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"age = null");
			Assert.assertEquals(links.size(), batchCount * 9, "Wrong number of links returned");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}

	}

	@Test
	public void testVarious() throws PersistenceException, InternalException, ReferenceException, DeploymentException,
			IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CREATED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Fetch links (none exist)
			List<CaseLinkDTO> links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null,
					null);
			Assert.assertTrue(links == null || links.isEmpty(), "Not expecting links");

			// Create 10x Person
			List<CaseReference> perRefs = new ArrayList<>();
			perRefs.addAll(caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"), 1,
					Arrays.asList("{\"personState\":\"ALIVE\", \"age\":25}",
							"{\"personState\":\"ALIVE\", \"age\":null}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}", "{\"personState\":\"ALIVE\"}",
							"{\"personState\":\"ALIVE\"}")));

			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 10);

			// Link Policy to first Person via 'holder'
			caseManager.createCaseLinks(polRefs.get(0),
					Collections.singletonList(new CaseLinkDTO("holder", perRefs.get(0))));

			// Link each Person (including the first one) to the Policy via 'allowedToDrive' 
			// (the opposite of Policy's 'namedDrivers');
			for (CaseReference perRef : perRefs)
			{
				caseManager.createCaseLinks(perRef,
						Collections.singletonList(new CaseLinkDTO("allowedToDrive", polRefs.get(0))));
			}

			// Fetch Policy's links (11 exist)
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 11, "Wrong link count");

			// Fetch by name (holder)
			links = caseManager.getCaseLinks(polRefs.get(0), "holder", null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 1, "Wrong link count");

			// Fetch by name (holder) - with a condition that matches
			links = caseManager.getCaseLinks(polRefs.get(0), "holder", null, Integer.MAX_VALUE, null,
					"personState = 'ALIVE'");
			Assert.assertEquals(links.size(), 1, "Wrong link count");

			// Fetch by name (holder) - with a condition that doesn't match
			links = caseManager.getCaseLinks(polRefs.get(0), "holder", null, Integer.MAX_VALUE, null,
					"personState = 'DEAD'");
			Assert.assertEquals(links.size(), 0, "Wrong link count");

			// Fetch by name (namedDrivers)
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 10, "Wrong link count");

			// Fetch by name (namedDrivers) - with DQL match
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"personState = 'ALIVE'");
			Assert.assertEquals(links.size(), 10, "Wrong link count");

			// Fetch by name (namedDrivers) - with DQL no match
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"personState = 'DEAD'");
			Assert.assertEquals(links.size(), 0, "Wrong link count");

			// Fetch by null age - note that this should return the 8 Persons that have no age, as well as
			// the one that has the age explicitly set to 'null' (which will have been cleansed during casedata
			// validation, having that property removed).
			links = caseManager.getCaseLinks(polRefs.get(0), "namedDrivers", null, Integer.MAX_VALUE, null,
					"age = null");
			Assert.assertEquals(links.size(), 9, "Wrong link count");

			// Fetch from the Person end
			links = caseManager.getCaseLinks(perRefs.get(0), "allowedToDrive", null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 1, "Wrong link count");

			// Fetch from the Person end - with DQL match
			links = caseManager.getCaseLinks(perRefs.get(0), "allowedToDrive", null, Integer.MAX_VALUE, null,
					"number = 777");
			Assert.assertEquals(links.size(), 1, "Wrong link count");

			// Fetch from the Person end - with DQL no match
			links = caseManager.getCaseLinks(perRefs.get(0), "allowedToDrive", null, Integer.MAX_VALUE, null,
					"number = 345345343");
			Assert.assertEquals(links.size(), 0, "Wrong link count");

			// Delete 'satisfactorySoundInsulation'
			try
			{
				caseManager.deleteCaseLinks(polRefs.get(0), null, "satisfactorySoundInsulation");
				Assert.fail("Expected failure due to lack of satisfactorySoundInsulation");
			}
			catch (ReferenceException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_LINK_NAME_NOT_EXIST.getCode(),
						"There is no satisfactorySoundInsulation");
			}

			// Delete 'holder'
			caseManager.deleteCaseLinks(polRefs.get(0), null, "holder");

			// Fetch Policy's links (10 exist)
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 10, "Wrong link count");

			// Delete 'holder' again (to check it silently does nothing)
			caseManager.deleteCaseLinks(polRefs.get(0), null, "holder");

			// Fetch Policy's links (10 exist)
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 10, "Wrong link count");

			// Delete 'namedDrivers'
			caseManager.deleteCaseLinks(polRefs.get(0), null, "namedDrivers");

			// Fetch Policy's links (0 exist)
			links = caseManager.getCaseLinks(polRefs.get(0), null, null, Integer.MAX_VALUE, null, null);
			Assert.assertEquals(links.size(), 0, "Wrong link count");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testLinkToTerminalProhibited() throws ReferenceException, NotAuthorisedException, PersistenceException,
			ValidationException, ArgumentException, InternalException, DeploymentException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create an Policy in a terminal state and a Person in an active state
			// It should not be possible to link these cases to each other, given
			// that the Person is in a terminal state.

			// Create 1x Policy (terminal state)
			List<CaseReference> polRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"),
					1, Arrays.asList("{\"state\":\"CANCELLED\",\"number\":777}"));
			Assert.assertNotNull(polRefs);
			Assert.assertEquals(polRefs.size(), 1);

			// Create 1x Person (action state)
			List<CaseReference> perRefs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"),
					1, Arrays.asList("{\"personState\":\"ALIVE\"}"));
			Assert.assertNotNull(perRefs);
			Assert.assertEquals(perRefs.size(), 1);

			// Attempt to link (disallowed)
			// Link Policy to first Person via 'holder'
			try
			{
				caseManager.createCaseLinks(polRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("holder", perRefs.get(0))));
				Assert.fail("Expected failure when linking a terminal-state case");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_TERMINAL_STATE_PREVENTS_LINKING.getCode(), "Wrong error code");
				Assert.assertEquals(e.getMessage(),
						"Links cannot be created for a case in a terminal state: " + polRefs.get(0));
			}

			// Attempt to link in the opposite direction; Should fail in exactly the same way
			// Link Person to first Policy via 'policies'
			try
			{
				caseManager.createCaseLinks(perRefs.get(0),
						Collections.singletonList(new CaseLinkDTO("policies", polRefs.get(0))));
				Assert.fail("Expected failure when linking a terminal-state case");
			}
			catch (ArgumentException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_REFERENCE_TERMINAL_STATE_PREVENTS_LINKING.getCode(), "Wrong error code");
				Assert.assertEquals(e.getMessage(),
						"Links cannot be created for a case in a terminal state: " + polRefs.get(0));
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
