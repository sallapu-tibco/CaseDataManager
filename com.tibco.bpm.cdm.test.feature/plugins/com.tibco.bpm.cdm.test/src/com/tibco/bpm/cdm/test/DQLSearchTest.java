package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CDMException;
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
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

/**
 * Tests for DQL search.  Note that DQL in the context of navigating links is tested in CaseLinkTest.
 * @author smorgan
 * @since 2019
 */
public class DQLSearchTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	private void searchPolicies(String dql, List<CaseReference> expectedRefs)
			throws PersistenceException, ReferenceException, InternalException, ArgumentException,
			DataModelSerializationException, NotAuthorisedException
	{
		List<CaseInfoDTO> list = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, null,
				Integer.MAX_VALUE, null, null, null,null, null, dql, CaseAspectSelection.fromSelectExpression("cr,c"),
				false);
		Assert.assertEquals(list.size(), expectedRefs.size(), "Wrong number of matches");
		List<CaseReference> actualRefs = new ArrayList<>();
		for (CaseInfoDTO dto : list)
		{
			actualRefs.add(new CaseReference(dto.getTypeName(), dto.getMajorVersion(), dto.getId(), dto.getVersion()));
		}

		Assert.assertTrue(actualRefs.containsAll(expectedRefs),
				"Wrong cases returned. Expected " + expectedRefs + " but got " + actualRefs);
	}

	private void searchPoliciesWithFailure(String dql, String expectedErrorCode, String expectedMessage)
			throws PersistenceException, ReferenceException, InternalException, ArgumentException,
			DataModelSerializationException
	{
		try
		{
			@SuppressWarnings("unused")
			List<CaseInfoDTO> list = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					null, Integer.MAX_VALUE, null, null, null, null,null, dql,
					CaseAspectSelection.fromSelectExpression("cr,c"), false);
			Assert.fail("Expected failure");
		}
		catch (CDMException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), expectedErrorCode, "Failed with wrong code");
			Assert.assertTrue(e.getMessage().contains(expectedMessage),
					"Expected message to contain: " + expectedMessage + ", but was " + e.getMessage());
		}
	}

	private void searchPersonsWithFailure(String dql, String expectedErrorCode, String expectedMessage)
			throws PersistenceException, ReferenceException, InternalException, ArgumentException,
			DataModelSerializationException
	{
		try
		{
			@SuppressWarnings("unused")
			List<CaseInfoDTO> list = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Person"), 1,
					null, Integer.MAX_VALUE, null, null, null, null,null, dql,
					CaseAspectSelection.fromSelectExpression("cr,c"), false);
			Assert.fail("Expected failure");
		}
		catch (CDMException e)
		{
			Assert.assertEquals(e.getErrorData().getCode(), expectedErrorCode, "Failed with wrong code");
			Assert.assertTrue(e.getMessage().contains(expectedMessage),
					"Expected message to contain: " + expectedMessage + ", but was " + e.getMessage());
		}
	}

	private void searchPersons(String dql, List<CaseReference> expectedRefs)
			throws PersistenceException, ReferenceException, InternalException, ArgumentException,
			DataModelSerializationException, NotAuthorisedException
	{
		List<CaseInfoDTO> list = caseManager.readCases(new QualifiedTypeName("org.policycorporation.Person"), 1, null,
				Integer.MAX_VALUE, null, null, null, null,null, dql, CaseAspectSelection.fromSelectExpression("cr,c"),
				false);
		Assert.assertEquals(list.size(), expectedRefs.size(), "Wrong number of matches");
		List<CaseReference> actualRefs = new ArrayList<>();
		for (CaseInfoDTO dto : list)
		{
			actualRefs.add(new CaseReference(dto.getTypeName(), dto.getMajorVersion(), dto.getId(), dto.getVersion()));
		}
		Assert.assertTrue(actualRefs.containsAll(expectedRefs),
				"Wrong cases returned. Expected " + expectedRefs + " but got " + actualRefs);
	}

	private List<CaseReference> createPolicyTrio() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		List<String> cases = new ArrayList<>();
		cases.add(
				"{\"state\":\"CREATED\",\"number\":999,\"shirtbox\":{\"a\":9},\"trumpet\":\"trouser\",\"claims\":[{\"c1\":123}]}");
		cases.add("{\"state\":\"CANCELLED\",\"number\":888,\"comments\":\"Monkey's Uncle\", \"noClaimsYears\":10}");
		cases.add("{\"state\":\"CREATED\",\"number\":777, \"policyStartDate\":\"2019-06-03\","
				+ "\"policyStartTime\":\"12:34:56\",\"noClaimsYears\":null,\"premium\":199, \"expiresAt\":\"2001-02-03T04:05:06.007Z\"}");
		List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
				cases);
		return refs;
	}

	private List<CaseReference> createPersonTrio() throws PersistenceException, ReferenceException, InternalException,
			DataModelSerializationException, ValidationException, ArgumentException, NotAuthorisedException
	{
		List<String> cases = new ArrayList<>();
		cases.add("{\"personState\":\"ALIVE\",\"name\":\"Joshy\"}");
		cases.add("{\"personState\":\"ALIVE\",\"name\":\"Simon\"}");
		cases.add("{\"personState\":\"ALIVE\",\"name\":\"Howard\", \"timeOfBirth\":\"12:34:56\"}");
		List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Person"), 1,
				cases);
		return refs;
	}

	@Test
	public void testDateTimeTZ() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPolicyTrio();
			CaseReference ref999 = refs.get(0);
			CaseReference ref888 = refs.get(1);
			CaseReference ref777 = refs.get(2);

			// Expect cases 999 and 888 (expiresAt has its default value 2099-12-31T23:59:59.999Z)
			searchPolicies("expiresAt = 2099-12-31T23:59:59.999Z", Arrays.asList(ref999, ref888));

			// Expect case 777
			searchPolicies("expiresAt = 2001-02-03T04:05:06.007Z", Collections.singletonList(ref777));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDate() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPolicyTrio();
			CaseReference ref999 = refs.get(0);
			CaseReference ref888 = refs.get(1);
			CaseReference ref777 = refs.get(2);

			// Expect cases 999 and 888 (has its default value: 2016-12-25)
			searchPolicies("policyStartDate = 2016-12-25", Arrays.asList(ref999, ref888));

			// Expect case 777
			searchPolicies("policyStartDate = 2019-06-03", Collections.singletonList(ref777));

			// Expect none
			searchPolicies("policyStartDate = 2001-02-03", Collections.emptyList());
			searchPolicies("policyStartDate = null", Collections.emptyList());
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testTime() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPersonTrio();
			CaseReference refJoshy = refs.get(0);
			CaseReference refSimon = refs.get(1);
			CaseReference refHoward = refs.get(2);

			// Expect cases 999 and 888 (has its default value: 00:01:02)
			searchPersons("timeOfBirth = 00:01:02", Arrays.asList(refJoshy, refSimon));

			// Expect case 777
			searchPersons("timeOfBirth = 12:34:56", Collections.singletonList(refHoward));

			// Expect none
			searchPersons("timeOfBirth = 11:11:11", Collections.emptyList());
			searchPersons("timeOfBirth = null", Collections.emptyList());
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testText() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPolicyTrio();
			CaseReference ref999 = refs.get(0);
			CaseReference ref888 = refs.get(1);
			CaseReference ref777 = refs.get(2);

			searchPolicies("comments = 'Monkey\\'s Uncle'", Collections.singletonList(ref888));
			searchPolicies("comments = 'Monkey\\'s Uncle' and number = 888", Collections.singletonList(ref888));
			searchPolicies("comments = 'Monkey\\'s Uncle' and number = 777", Collections.emptyList());
			searchPolicies("comments = 'Sunmagic outage'", Collections.emptyList());
			searchPolicies("comments = null", Arrays.asList(ref999, ref777));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testFixedPointNumber() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPolicyTrio();
			CaseReference ref777 = refs.get(2);

			searchPolicies("premium = 199", Collections.singletonList(ref777));
			searchPolicies("premium = 0199", Collections.singletonList(ref777));
			searchPolicies("premium = 000000000000199", Collections.singletonList(ref777));
			searchPolicies("premium = 199.0", Collections.singletonList(ref777));
			searchPolicies("premium = 199.000000000000", Collections.singletonList(ref777));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testNumber() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, ReferenceException, DataModelSerializationException,
			ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			List<CaseReference> refs = createPolicyTrio();
			CaseReference ref999 = refs.get(0);
			CaseReference ref888 = refs.get(1);
			CaseReference ref777 = refs.get(2);

			searchPolicies("number = null", Collections.emptyList());
			searchPolicies("number = 666", Collections.emptyList());
			searchPolicies("number = 777", Collections.singletonList(ref777));
			searchPolicies("number = 888", Collections.singletonList(ref888));
			searchPolicies("number = 999", Collections.singletonList(ref999));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testErrors() throws PersistenceException, ReferenceException, InternalException, ArgumentException,
			DataModelSerializationException, DeploymentException, IOException, URISyntaxException,
			RuntimeApplicationException, CasedataException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			searchPoliciesWithFailure("", "CDM_API_BAD_DQL", "DQL_UNPARSABLE: Unparseable DQL string");

			searchPoliciesWithFailure("notAnAttribute = 321", "CDM_API_BAD_DQL",
					"DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: notAnAttribute");

			searchPoliciesWithFailure("NUMber = 321", "CDM_API_BAD_DQL",
					"DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: NUMber");

			searchPoliciesWithFailure("termsAndConditions = 'http://rtfm.org'", "CDM_API_BAD_DQL",
					"DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'termsAndConditions' is not searchable: "
							+ "termsAndConditions = 'http://rtfm.org'");

			searchPoliciesWithFailure("legalCover = true", "CDM_API_BAD_DQL",
					"DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'legalCover' is not searchable: legalCover = true");

			searchPoliciesWithFailure("number = '999'", "CDM_API_BAD_DQL",
					"DQL_VALUE_SHOULD_NOT_BE_QUOTED: Value for Number attribute should not be quoted: number = '999'");

			searchPoliciesWithFailure("state = CANCELLED", "CDM_API_BAD_DQL",
					"DQL_VALUE_SHOULD_BE_QUOTED: Value for Text attribute should be single-quoted: state = CANCELLED");

			searchPersonsWithFailure("timeOfBirth = 123", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for Time type: timeOfBirth = 123");

			searchPersonsWithFailure("timeOfBirth = 12:34", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for Time type: timeOfBirth = 12:34");

			searchPoliciesWithFailure("policyStartDate = 123", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for Date type: policyStartDate = 123");

			searchPoliciesWithFailure("policyStartDate = 2019", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for Date type: policyStartDate = 2019");

			searchPoliciesWithFailure("policyStartDate = 2019", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for Date type: policyStartDate = 2019");

			searchPoliciesWithFailure("expiresAt = 2019", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for DateTimeTZ type: expiresAt = 2019");

			searchPoliciesWithFailure("premium=hello", "CDM_API_BAD_DQL",
					"DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for FixedPointNumber type: premium=hello");

			searchPoliciesWithFailure("gibberish!", "CDM_API_BAD_DQL",
					"DQL_BAD_EXPRESSION: Bad expression: gibberish!");

			// Leading ' and ', so there is effectively a first expression which is an empty string
			searchPoliciesWithFailure(" and premium = 123", "CDM_API_BAD_DQL", "DQL_BAD_EXPRESSION: Bad expression: ");

			// Trailing ' and ', so there is effectively a second expression which is an empty string
			searchPoliciesWithFailure("premium = 123 and ", "CDM_API_BAD_DQL", "DQL_BAD_EXPRESSION: Bad expression: ");

			// Trailing ' and' with no whitespace, so will not be recognised. The 'and' will be considered
			// part of the first expression.
			searchPoliciesWithFailure("premium = 123 and", "CDM_API_BAD_DQL",
					"DQL_BAD_EXPRESSION: Bad expression: premium = 123 and");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSearchSanity() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			ReferenceException, ArgumentException, ValidationException, NotAuthorisedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			List<CaseReference> refs = createPolicyTrio();

			CaseReference ref999 = refs.get(0);
			CaseReference ref888 = refs.get(1);
			CaseReference ref777 = refs.get(2);

			// Expect case 999
			searchPolicies("state = 'CREATED' and number = 999", Collections.singletonList(ref999));
			searchPolicies(" number = 999     AND state='CREATED' ", Collections.singletonList(ref999));

			// Expect case 888
			searchPolicies("state = 'CANCELLED'", Collections.singletonList(ref888));
			searchPolicies("number = 888", Collections.singletonList(ref888));

			// Expect case 777
			searchPolicies("premium = 199", Collections.singletonList(ref777));
			searchPolicies("policyStartDate = 2019-06-03", Collections.singletonList(ref777));

			// Expect cases 999 and 777
			searchPolicies("state = 'CREATED'", Arrays.asList(ref999, ref777));

			// Expect no matches
			searchPolicies("number = 123", Collections.emptyList());
			searchPolicies("number = 999 and state='CANCELLED'", Collections.emptyList());

			// 777 contains a property with value null, but 999 doesn't have the property at all. Both should match.
			searchPolicies("noClaimsYears = null", Arrays.asList(ref999, ref777));
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}
