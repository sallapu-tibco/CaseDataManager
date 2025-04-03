package com.tibco.bpm.cdm.rest.test;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.AccountCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.CaseLinksFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.CustomerCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.test.util.FileUtils.DataModelModifier;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.da.dm.api.IdentifierInitialisationInfo;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class DQLTest extends Utils
{
	private static ObjectMapper	om						= new ObjectMapper();

	AccountCreatorFunction		accountCreator			= new AccountCreatorFunction();

	CustomerCreatorFunction		customerCreator			= new CustomerCreatorFunction();

	CaseLinksFunction			linksFunction			= new CaseLinksFunction();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	CaseTypesFunctions			caseTypes				= new CaseTypesFunctions();

	private Response			response				= null;

	private JsonPath			jsonPath				= null;

	private static final String	caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String	caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String	caseTypeNewCaseType		= "com.example.bankdatamodel.NewCaseType";

	private static final String	applicationMajorVersion	= "1";

	private BigInteger			deploymentIdApp1		= BigInteger.valueOf(7);

	private BigInteger			deploymentIdApp2		= BigInteger.valueOf(8);

	private void upgrade() throws IOException, URISyntaxException, DataModelSerializationException,
			RuntimeApplicationException, InterruptedException
	{

		DataModelModifier modifierVx = null;

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("isCase", "TRUE");

		//get the zip file
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.1.0");

		modifierVx = (dm) -> {
			//new case type
			//Boolean, URI & Performer are not included in the new case type since those are neither searchable nor summary
			//https://confluence.tibco.com/display/BPM/AMX-BPM%3A+Container+Edition+FP+-+Supported+data+types

			StructuredType stCaseType = dm.newStructuredType();
			stCaseType.setName("NewCaseType");
			stCaseType.setLabel("New Case Type");
			stCaseType.setDescription("A new case type");
			stCaseType.setIsCase(true);

			IdentifierInitialisationInfo stCaseIdentifier = stCaseType.newIdentifierInitialisationInfo();
			stCaseIdentifier.setMinNumLength(4);
			stCaseIdentifier.setPrefix("New Case - ");
			stCaseIdentifier.setSuffix(" - ID");

			StateModel stCaseModel = stCaseType.newStateModel();
			stCaseModel.newState("DEPLOYED", "deployed", false);
			stCaseModel.newState("UNDEPLOYED", "undeployed", true);

			//identifier - text
			Attribute aCaseId = stCaseType.newAttribute();
			aCaseId.setName("id");
			aCaseId.setLabel("ID");
			aCaseId.setType("base:Text");
			aCaseId.setIsSummary(true);
			aCaseId.setIsSearchable(true);
			aCaseId.setDescription("This is an id");
			aCaseId.setIsMandatory(true);
			aCaseId.setIsIdentifier(true);

			//state - default "undeployed" - non-terminal
			Attribute aCaseState = stCaseType.newAttribute();
			aCaseState.setName("state");
			aCaseState.setLabel("State");
			aCaseState.setType("base:Text");
			aCaseState.setIsSummary(true);
			aCaseState.setIsSearchable(true);
			aCaseState.setDescription("This is a state");
			aCaseState.setIsMandatory(true);
			aCaseState.setIsState(true);
			aCaseState.setDefaultValue("deployed");

			//text attribute - searchable
			Attribute aCaseText = stCaseType.newAttribute();
			aCaseText.setName("text");
			aCaseText.setLabel("Text");
			aCaseText.setType("base:Text");
			aCaseText.setIsSummary(true);
			aCaseText.setIsSearchable(true);
			aCaseText.setDescription("This is a text");
			aCaseText.newConstraint("length", "10");

			//number attribute - searchable
			Attribute aCaseNumber = stCaseType.newAttribute();
			aCaseNumber.setName("number");
			aCaseNumber.setLabel("Number");
			aCaseNumber.setType("base:Number");
			aCaseNumber.setIsSummary(true);
			aCaseNumber.setIsSearchable(true);
			aCaseNumber.setDescription("This is a number");
			aCaseNumber.newConstraint("minValue", "-999999999999998");
			aCaseNumber.newConstraint("maxValue", "999999999999998");

			//date attribute - searchable
			Attribute aCaseDate = stCaseType.newAttribute();
			aCaseDate.setName("date");
			aCaseDate.setLabel("Date");
			aCaseDate.setType("base:Date");
			aCaseDate.setIsSearchable(true);
			aCaseDate.setIsSummary(true);
			aCaseDate.setDescription("This is a date");

			//time attribute - searchable
			Attribute aCaseTime = stCaseType.newAttribute();
			aCaseTime.setName("time");
			aCaseTime.setLabel("Time");
			aCaseTime.setType("base:Time");
			aCaseTime.setIsSearchable(true);
			aCaseTime.setIsSummary(true);
			aCaseTime.setDescription("This is a Time");

			//dateTimeZone attribute - searchable
			Attribute aCaseDateTimeTZ = stCaseType.newAttribute();
			aCaseDateTimeTZ.setName("dateTimeTZ");
			aCaseDateTimeTZ.setLabel("DateTimeTZ");
			aCaseDateTimeTZ.setType("base:DateTimeTZ");
			aCaseDateTimeTZ.setIsSearchable(true);
			aCaseDateTimeTZ.setIsSummary(true);
			aCaseDateTimeTZ.setDescription("This is a DateTimeTZ");

			//link new case to customers - many to many 			
			com.tibco.bpm.da.dm.api.Link aNewCaseTypesToCustomer = dm.newLink();
			aNewCaseTypesToCustomer.getEnd1().setOwner("NewCaseType");
			aNewCaseTypesToCustomer.getEnd1().setLabel("customers to new case types");
			aNewCaseTypesToCustomer.getEnd1().setName("customers_to_new_case_types");
			aNewCaseTypesToCustomer.getEnd1().setIsArray(true);
			aNewCaseTypesToCustomer.getEnd2().setOwner("Customer");
			aNewCaseTypesToCustomer.getEnd2().setLabel("new case types to customers");
			aNewCaseTypesToCustomer.getEnd2().setName("new_case_types_to_customers");
			aNewCaseTypesToCustomer.getEnd2().setIsArray(true);

		};

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierVx);

		//deploy Vx
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//the test should never reach here
			response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", "10");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//assert that there are 3 structured types returned
			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 3, "incorrect number of types");

		}
		catch (DeploymentException |

				InternalException e)
		{
			System.out.println("upgrade should be successful");
			Assert.fail();
		}

	}

	private LinksPostRequestBody formulateBodyForLinkCases(int beginIndex, int endIndex, List<String> caseRefs,
			String linkName)
	{
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		for (int caseRefIterator = Integer.valueOf(beginIndex); caseRefIterator < Integer
				.valueOf(endIndex); caseRefIterator++)
		{
			Link link = new Link();
			link.setCaseReference(caseRefs.get(caseRefIterator));
			link.setName(linkName);
			linksPostRequestBody.add(link);
		}

		return linksPostRequestBody;
	}

	private void assertLinkedCases(int beginIndex, int endIndex, int totalLinks, List<String> caseRefs, int offset,
			String linkName, Response response) throws JsonParseException, JsonMappingException, IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		if (beginIndex < endIndex)
		{
			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), totalLinks, String.valueOf(totalLinks) + " cases are returned");

			for (int caseRefIterator = Integer.valueOf(beginIndex); caseRefIterator < Integer

					.valueOf(endIndex); caseRefIterator++)
			{
				if (offset == 0)
				{
					Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(), caseRefs.get(caseRefIterator),
							"linked case reference is incorrect");
				}
				else
				{
					Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
							caseRefs.get(caseRefIterator + offset), "linked case reference is incorrect");
				}
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}
		}

		else if (beginIndex == endIndex)
		{
			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), totalLinks, String.valueOf(totalLinks) + " cases are returned");
			Assert.assertEquals(response.asString(), "[]", "response should be empty");
			Assert.assertEquals(obj.isEmpty(), true, "object should be empty");
		}
	}

	private void assertCasedataSummaryReference(String casedata, String caseSummary, String caseReference,
			Response response) throws JsonParseException, JsonMappingException, IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 1);
		//assert casedata
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedata), "casedata does not match");
		//assert metadata
		Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"), "metadata should be tagged");
		//assert case reference
		Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReference, "caseRef should be tagged");
		//assert summary
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), caseSummary), "summary does not match");

	}

	@Test
	public void testDQL() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			CasesPostRequestBody createBody = new CasesPostRequestBody();
			createBody.setCaseType("org.policycorporation.Policy");
			createBody.setApplicationMajorVersion(1);
			createBody.setCasedata(Collections.singletonList("{\"state\":\"CREATED\",\"number\":777}"));
			String payload = om.writeValueAsString(createBody);
			Response response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(URL_CASES);

			CasesPostResponseBody responseBody = response.as(CasesPostResponseBody.class);
			Assert.assertNotNull(responseBody);
			Assert.assertEquals(responseBody.size(), 1);

			// Search for Policy (not expecting match)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParam("$top", Integer.MAX_VALUE)
					.queryParam("$filter",
							"caseType eq 'org.policycorporation.Policy' and applicationMajorVersion eq 1")
					.queryParam("$dql", "number = 123").get(URL_CASES);
			CasesGetResponseBody getResponseBody = response.as(CasesGetResponseBody.class);
			Assert.assertNotNull(getResponseBody);
			Assert.assertEquals(getResponseBody.size(), 0);

			// Search for Policy (expecting match)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParam("$top", Integer.MAX_VALUE)
					.queryParam("$filter",
							"caseType eq 'org.policycorporation.Policy' and applicationMajorVersion eq 1")
					.queryParam("$dql", "number = 777").get(URL_CASES);
			getResponseBody = response.as(CasesGetResponseBody.class);
			Assert.assertNotNull(getResponseBody);
			Assert.assertEquals(getResponseBody.size(), 1);
			Assert.assertEquals(getResponseBody.get(0).getCaseReference(), responseBody.get(0), "wrong ref returned");
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	//Test navigateBy(criteria) on links and after purge
	@Test(description = "Test navigateBy(criteria) on links and after purge")
	public void testNavigateByOnMultipleCases() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException,
			ArgumentException, DataModelSerializationException, InterruptedException

	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempRefs;
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 100 accounts of each type
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(1000, 100, "@ACTIVE&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(2000, 100, "@FROZEN&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(3000, 100,
					"@TREMPORARILY DEACTIVATED&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(4000, 100, "@WAITING FOR APPROVAL&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(5000, 100, "@REINSTATED&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(6000, 100,
					"@WAITING FOR CANCELLATION&");
			caseRefsAccounts.addAll(tempRefs);

			//Thread.sleep(CASES_TO_UPDATE);

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//link first 100 accounts to the customer - ACTIVE state
			linksPostRequestBody = formulateBodyForLinkCases(0, 100, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate by state name - @ACTIVE&
			//returns first 100 cases
			filterMap.put("name", "holdstheaccounts");
			filterDql.put("state", "'@ACTIVE&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//navigate by state name - @FROZEN&
			//no links re returned
			filterDql.put("state", "'@FROZEN&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//link next 100 accounts to the customer - FROZEN state
			linksPostRequestBody = formulateBodyForLinkCases(100, 200, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate by state name - @FROZEN&
			//next 100 items are retunred links re returned
			filterDql.put("state", "'@FROZEN&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 100, "holdstheaccounts", response);
			filterDql.clear();

			//link next 100 accounts to the customer - TERMINATED state
			linksPostRequestBody = formulateBodyForLinkCases(200, 300, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate by state name - @TREMPORARILY DEACTIVATED&
			//next 100 items are retunred links re returned
			filterDql.put("state", "'@TREMPORARILY DEACTIVATED&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 200, "holdstheaccounts", response);
			filterDql.clear();

			//link next 100 accounts to the customer - WAITING FOR APPROVAL state
			linksPostRequestBody = formulateBodyForLinkCases(300, 400, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate by state name - @WAITING FOR APPROVAL&
			//next 100 items are retunred links re returned
			filterDql.put("state", "'@WAITING FOR APPROVAL&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 300, "holdstheaccounts", response);
			filterDql.clear();

			//navigate by state name - @FROZEN&
			//next 100 items are retunred links re returned
			filterDql.put("state", "'@FROZEN&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "50", "200");
			assertLinkedCases(0, 50, 50, caseRefsAccounts, 150, "holdstheaccounts", response);
			filterDql.clear();

			//get the linked cases from other side (accounts)
			//dql navigate by state
			filterMap.clear();
			filterMap.put("name", "heldby");
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			for (int accountIterator = 0; accountIterator < 100; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//dql navigate by CID
			filterDql.put("customerCID", "'WINTERFELL-00001'");
			for (int accountIterator = 100; accountIterator < 200; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//dql navigate by state and CID
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			filterDql.put("customerCID", "'WINTERFELL-00001'");
			for (int accountIterator = 200; accountIterator < 300; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//get the linked cases from other side (accounts)
			//dql navigate by state
			filterMap.clear();
			filterMap.put("name", "heldby");
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(400), "0", "10");
			assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "heldby", response);
			filterDql.clear();

			//get the linked cases from other side (accounts)
			//dql navigate by state
			filterMap.clear();
			filterMap.put("name", "heldby");
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(599), "0", "10");
			assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "heldby", response);
			filterDql.clear();

			//purge accounts in @WAITING FOR APPROVAL& state
			filterMap.clear();
			filterMap.put("caseType", caseTypeAccount);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@WAITING FOR APPROVAL&");
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "100", "incorrect number of cases are purged");

			//use dql no result should be returned
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			filterDql.put("state", "'@WAITING FOR APPROVAL&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//purge customer in VERYACTIVE50KABOVE state
			filterMap.clear();
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "VERYACTIVE50KABOVE");
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//get the linked cases from other side (accounts)
			//dql navigate by state
			filterMap.clear();
			filterMap.put("name", "heldby");
			filterDql.put("state", "'VERYACTIVE50KABOVE'");

			//dql navigate by CID
			//check for random cases
			filterDql.put("customerCID", "'WINTERFELL-00001'");
			for (int accountIterator = 99; accountIterator < 300; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "heldby", response);
			}

			//change in the response after ACE-1518
			//cases 300 to 399 - @WAITING FOR APPROVAL& are purged
			for (int accountIterator = 300; accountIterator < 399; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				System.out.println("case reference is :" + caseRefsAccounts.get(accountIterator));
				properties.assertErrorResponseRefDoesNotExist(caseRefsAccounts.get(accountIterator), response);
			}

			for (int accountIterator = 400; accountIterator < 599; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "heldby", response);
			}
			filterDql.clear();

		}
		finally

		{
			forceUndeploy(deploymentId);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
	// The test needs to be updated to cope with this.
	// --------------------------------------------------------------------------------------------------
	//Test navigateBy(criteria) on self links, upgrade and after removal of links
	//@Test(description = "Test navigateBy(criteria) on self links, upgrade and after removal of links")
	public void testNavigateByAfterUpgradeSelfLinksAndLinksRemoval() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException, CasedataException,
			ReferenceException, ArgumentException, DataModelSerializationException, InterruptedException
	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempCaseRefHolder = new ArrayList<>();
		List<String> tempRefs = new ArrayList<>();
		List<String> updatedCaseRefAccounts = new ArrayList<>();
		List<String> updatedCaseRefCustomers = new ArrayList<>();
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 12 accounts of each type
			caseRefsAccounts = accountCreator.createAccountsArrayData(12, "accounts");

			//create 9 customers
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "REGULAR1KTO10K"));
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//link first 12 accounts to 1st customer
			linksPostRequestBody = formulateBodyForLinkCases(0, 12, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate using all types of data fields
			//navigate using time
			filterMap.put("name", "holdstheaccounts");
			filterDql.put("timeofAccountOpening", "11:11:20");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 3, caseRefsAccounts, 1, "holdstheaccounts", response);
			assertLinkedCases(1, 2, 3, caseRefsAccounts, 5, "holdstheaccounts", response);
			assertLinkedCases(2, 3, 3, caseRefsAccounts, 9, "holdstheaccounts", response);
			//test skip
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "1", "1");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 6, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using date
			filterDql.put("dateofAccountOpening", "2011-01-12");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 2, caseRefsAccounts, 1, "holdstheaccounts", response);
			assertLinkedCases(1, 2, 2, caseRefsAccounts, 9, "holdstheaccounts", response);
			//test top
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 1, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using both date and time, there is only one unique case
			filterDql.put("timeofAccountOpening", "11:11:20");
			filterDql.put("dateofAccountOpening", "2011-01-12");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "5");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 1, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using accountID - number
			filterDql.put("accountID", "5");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "5");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 4, "holdstheaccounts", response);
			filterDql.clear();

			//duplicate account id does not work
			filterDql.put("accountID", "5");
			filterDql.put("duplicate-accountID", "6");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "5");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using accountType - text  
			filterDql.put("accountType", "'CASHISA'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "5");
			assertLinkedCases(0, 1, 2, caseRefsAccounts, 4, "holdstheaccounts", response);
			assertLinkedCases(1, 2, 2, caseRefsAccounts, 9, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using state - text
			filterDql.put("state", "'@REINSTATED&'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "5");
			assertLinkedCases(0, 1, 2, caseRefsAccounts, 5, "holdstheaccounts", response);
			assertLinkedCases(1, 2, 2, caseRefsAccounts, 10, "holdstheaccounts", response);
			filterDql.clear();

			//navigate using a big enough criteria
			filterDql.put("accountID", "6");
			filterDql.put("state", "'@REINSTATED&'");
			filterDql.put("accountType", "'INSURANCE'");
			filterDql.put("timeofAccountOpening", "11:00:20");
			filterDql.put("dateofAccountOpening", "2011-01-16");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "1");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 5, "holdstheaccounts", response);

			//however important you are if top=0, you are skipped
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "", "0");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 4, "holdstheaccounts", response);
			filterDql.clear();

			//update account
			//updating the accounts 11 times
			tempCaseRefHolder.clear();
			tempCaseRefHolder.addAll(caseRefsAccounts);
			for (int i = 11; i >= 0; i--)
			{
				tempCaseRefHolder = accountCreator.updateAccountsArrayData(tempCaseRefHolder.subList(0, i), i,
						"accounts");
				if (i > 0)
				{
					updatedCaseRefAccounts.add(0, tempCaseRefHolder.get(i - 1));
				}
			}
			updatedCaseRefAccounts.add(caseRefsAccounts.get(11));

			//upgrade to v2
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//update customer change state
			updatedCaseRefCustomers = customerCreator.customerChangState(caseRefsCustomers, "REGULAR1KTO10K",
					"VERYACTIVE50KABOVE");

			//create 2 cases with upgrade account v2 - 13th and 14th
			updatedCaseRefAccounts.addAll(accountCreator.createAccountWithDefaultValues("12345"));
			updatedCaseRefAccounts.addAll(accountCreator.createAccountWithDefaultValues("123456"));

			//create a case for the v2 accounts - 15th
			//as ACE-1834 is resolved, value is changed to -999999999999.998
			String casedataAccountV2 = "{\"state\": \"@UNFINISHED CASES PURGE&\", \"accountID\": 100000, \"accountType\": \"ThisLongSentence'Is400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREIsPunctuationsInThis'PassageONLYSpacesAreKeptANDACombinations'OfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC\","
					+ " \"institutionDetails\": {\"institutionCode\": \"HDFC101\", \"nameoftheInstitution\": \"HDFC Bank Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Noida\", \"county\": \"Uttar Pradesh\", \"country\": \"India\", \"postCode\": \"120001\", \"firstLine\": \"B-75\", "
					+ "\"secondLine\": \"Noida\"}}, \"portfolioWebsite\":\"https://www.hdfc-bank-plc.com\",\"activePortfolio\":true,\"dateofAccountClosing\": \"2089-12-18\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"11:22:01\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"activeationTimeStamp\":\"2129-12-09T23:11:39.891Z\",\"accountLiabilityHolding\": -999999999999.998}";

			List<String> casedata = new ArrayList<>();
			casedata.add(casedataAccountV2);
			response = cases.createCases("com.example.bankdatamodel.Account", 1, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			//adding the new case reference to updated case ref accounts
			updatedCaseRefAccounts.add(jsonPath.getString("[0]"));

			// link account 13th to 15th account to updated customer 
			linksPostRequestBody = formulateBodyForLinkCases(12, 15, updatedCaseRefAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(updatedCaseRefCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigate using the remaining data types 
			//activeationTimeStamp - dateTimeTZ - version independence
			filterDql.put("activeationTimeStamp", "2129-12-09T23:11:39.891Z");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 14, "holdstheaccounts", response);
			filterDql.clear();

			//activeationTimeStamp - dateTimeTZ - case reference customer exact version
			filterDql.put("activeationTimeStamp", "2129-12-09T23:11:39.891Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 14, "holdstheaccounts", response);
			filterDql.clear();

			//activeationTimeStamp - dateTimeTZ - partially providing datetimeTZ does not work
			filterDql.put("activeationTimeStamp", "2129");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "10");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse, "incorrect response");
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"),
					"Bad DQL: [DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for DateTimeTZ type: activeationTimeStamp = 2129]",
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertTrue(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for DateTimeTZ type: activeationTimeStamp = 2129]"),
					"incorrect stacktrace content");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
					"[DQL_VALUE_NOT_VALID_FOR_TYPE: Value is not appropriate for DateTimeTZ type: activeationTimeStamp = 2129]");
			filterDql.clear();

			//state - dräçÅrûß
			//accented character - version independence
			filterDql.put("state", "'dräçÅrûß'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 2, 2, updatedCaseRefAccounts, 12, "holdstheaccounts", response);
			filterDql.clear();

			//text - with a single quote '
			//accented character - exact version 
			filterDql.put("accountType",
					"'ThisLongSentence\\'Is400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREIsPunctuationsInThis\\'PassageONLYSpacesAreKeptANDACombinations\\'OfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 14, "holdstheaccounts", response);
			filterDql.clear();

			//accountLiabilityHolding for the 15th case
			//negative fixed point number - exact version
			//as ACE-1834 is resolved, value is changed to -999999999999.998			
			filterDql.put("accountLiabilityHolding", "-999999999999.998");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 14, "holdstheaccounts", response);
			filterDql.clear();

			//accountLiabilityHolding for the 15th case
			//positive fixed point number - version independence
			filterDql.put("accountLiabilityHolding", "1.234");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 2, 2, updatedCaseRefAccounts, 12, "holdstheaccounts", response);
			filterDql.clear();

			//test for null activeationTimeStamp
			filterDql.put("activeationTimeStamp", "null");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "15");
			assertLinkedCases(0, 12, 12, updatedCaseRefAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//test for skip and top
			//test for null activeationTimeStamp
			filterDql.put("activeationTimeStamp", "null");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "5", "10");
			assertLinkedCases(0, 7, 7, updatedCaseRefAccounts, 5, "holdstheaccounts", response);
			filterDql.clear();

			//test for skip and top
			//test for null activeationTimeStamp
			filterDql.put("activeationTimeStamp", "null");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "10", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 10, "holdstheaccounts", response);
			filterDql.clear();

			//test for skip and top
			//test for null activeationTimeStamp
			filterDql.put("activeationTimeStamp", "null");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "", "0");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 0, "holdstheaccounts", response);
			filterDql.clear();

			//verify that older attributes searchable attributes which are not searchable now don't work - exact version
			filterDql.put("dateofAccountOpening", "2011-01-15");
			filterDql.put("timeofAccountOpening", "11:44:20");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "10");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse, "incorrect response");
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"),
					"Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]",
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertTrue(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]"),
					"incorrect stacktrace content");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
					"[DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]");
			filterDql.clear();

			//verify that older attributes searchable attributes which are not searchable now don't work - version independence
			filterDql.put("dateofAccountOpening", "2011-01-15");
			filterDql.put("timeofAccountOpening", "11:44:20");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse, "incorrect response");
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"),
					"Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]",
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertTrue(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]"),
					"incorrect stacktrace content");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
					"[DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 11:44:20, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2011-01-15]");
			//clear filterMap since it will be used
			filterDql.clear();
			filterMap.clear();

			//navigate the other way round on all the account - state VERYACTIVE50KABOVE
			filterMap.put("name", "heldby");
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			for (int accountIterator = 0; accountIterator < 15; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(accountIterator),
						"0", "10");
				assertLinkedCases(0, 1, 1, updatedCaseRefCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//add the updated caseRefs to the caseRefAccounts
			caseRefsAccounts.add(updatedCaseRefAccounts.get(12));
			caseRefsAccounts.add(updatedCaseRefAccounts.get(13));
			caseRefsAccounts.add(updatedCaseRefAccounts.get(14));
			filterDql.put("state", "'VERYACTIVE50KABOVE'");
			for (int accountIterator = 0; accountIterator < 15; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 1, 1, updatedCaseRefCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//check with cid
			filterDql.put("customerCID", "'WINTERFELL-00001'");
			for (int accountIterator = 0; accountIterator < 15; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(accountIterator),
						"0", "10");
				assertLinkedCases(0, 1, 1, updatedCaseRefCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//navigate the other way round on all the account - state REGULAR1KTO10K - as the case is updated
			filterDql.put("state", "'REGULAR1KTO10K'");
			for (int accountIterator = 0; accountIterator < 15; accountIterator++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(accountIterator), "0",
						"10");
				assertLinkedCases(0, 0, 0, updatedCaseRefCustomers, 0, "heldby", response);
			}
			filterDql.clear();

			//neither with any other state - false search
			filterDql.put("state", "'BARREDORTERMINATED'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(0), "0", "10");
			assertLinkedCases(0, 0, 0, updatedCaseRefCustomers, 0, "heldby", response);
			filterDql.clear();
			filterMap.clear();

			//link accounts to itself and navigate 
			filterMap.put("name", "children_of_the_accounts");

			//link account 0 to 12 to account 13
			linksPostRequestBody = formulateBodyForLinkCases(0, 13, updatedCaseRefAccounts, "children_of_the_accounts");
			response = linksFunction.createLinks(caseRefsAccounts.get(13), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//account 14 is not linked to any search on that would not return any result
			//search both ways
			filterDql.put("accountLiabilityHolding", "1");
			filterDql.put("activeationTimeStamp", null);
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "3");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "3");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 12, "children_of_the_accounts", response);
			filterDql.clear();

			//navigate using accountID - number
			filterDql.put("accountID", "1");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "5");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//navigate using accountType - text  
			filterDql.put("accountType", "'INSURANCE'");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "5");
			assertLinkedCases(0, 1, 3, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			assertLinkedCases(1, 2, 3, updatedCaseRefAccounts, 5, "children_of_the_accounts", response);
			assertLinkedCases(2, 3, 3, updatedCaseRefAccounts, 9, "children_of_the_accounts", response);
			filterDql.clear();

			//navigate using state - text
			filterDql.put("state", "'@WAITING FOR CANCELLATION&'");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "5");
			assertLinkedCases(0, 1, 2, updatedCaseRefAccounts, 2, "children_of_the_accounts", response);
			assertLinkedCases(1, 2, 2, updatedCaseRefAccounts, 7, "children_of_the_accounts", response);
			filterDql.clear();

			//navigate using a big enough criteria
			filterDql.put("accountID", "1");
			filterDql.put("state", "'@REINSTATED&'");
			filterDql.put("accountType", "'INSURANCE'");
			filterDql.put("accountLiabilityHolding", "1");
			filterDql.put("activeationTimeStamp", null);
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//version independant
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(13), "", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//with the search parameters of account 14 no results are returned since it is not linked
			filterDql.put("accountID", "100000");
			filterDql.put("state", "'@UNFINISHED CASES PURGE&'");
			filterDql.put("accountType",
					"'ThisLongSentence\\'Is400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREIsPunctuationsInThis\\'PassageONLYSpacesAreKeptANDACombinations\\'OfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			//as ACE-1834 is resolved, value is changed to -999999999999.998
			filterDql.put("accountLiabilityHolding", "-999999999999.998");
			filterDql.put("activeationTimeStamp", "2129-12-09T23:11:39.891Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "1");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//clear the filter map and add a new value
			filterMap.clear();
			filterMap.put("name", "parent_account");

			//navigate using a big enough criteria
			//search the other way
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(12), "", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 13, "parent_account", response);
			filterDql.clear();

			//clear the filter map and add a new value
			filterMap.clear();
			filterMap.put("name", "children_of_the_accounts");

			//specify a different filter and dql attribute of a different class - does that work?
			//filterMap - conatains - "children_of" and dql is for an attribute of "Customer"
			filterDql.put("customerCID", "'WINTERFELL-00001'");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "10");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse, "incorrect response");
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"),
					"Bad DQL: [DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: customerCID]",
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertTrue(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: customerCID]"),
					"incorrect stacktrace content");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
					"[DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: customerCID]");
			//clear filterMap since it will be used
			filterDql.clear();

			//delete links and make sure navigate by does not work
			//delete account links 0 to 10 from 13 and verify that search returns 11 for activation timestamp
			response = linksFunction.deleteLinks(updatedCaseRefAccounts.get(13), "children_of_the_accounts", "IN",
					caseRefsAccounts.subList(0, 11));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "11", "checking for empty response");

			//search on activation timestamp 
			filterDql.put("activeationTimeStamp", null);
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "0", "20");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 11, "children_of_the_accounts", response);
			filterDql.clear();

			//link 14 to 13 and new search works fine
			linksPostRequestBody = formulateBodyForLinkCases(14, 15, updatedCaseRefAccounts,
					"children_of_the_accounts");
			response = linksFunction.createLinks(updatedCaseRefAccounts.get(13), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//clear the filter map and add a new value
			filterMap.clear();
			filterMap.put("name", "parent_account");

			//search from the 14th account
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(14), "", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 13, "parent_account", response);
			filterDql.clear();

			//clear the filter map and add a new value
			filterMap.clear();
			filterMap.put("name", "children_of_the_accounts");

			//search from the 14th account won't work since the filterMap is wrong
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(14), "", "1");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 0, "", response);
			filterDql.clear();

			//search for the 14th account
			filterDql.put("accountID", "100000");
			filterDql.put("state", "'@UNFINISHED CASES PURGE&'");
			filterDql.put("accountType",
					"'ThisLongSentence\\'Is400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREIsPunctuationsInThis\\'PassageONLYSpacesAreKeptANDACombinations\\'OfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			//as ACE-1834 is resolved, value is changed to -999999999999.998
			filterDql.put("accountLiabilityHolding", "-999999999999.998");
			filterDql.put("activeationTimeStamp", "2129-12-09T23:11:39.891Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "1");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 14, "children_of_the_accounts", response);
			filterDql.clear();

			//delete all links and see navigate by returns 0 results
			response = linksFunction.deleteLinks(updatedCaseRefAccounts.get(13), "children_of_the_accounts", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "3", "checking response for number of links deleted");

			//search from the 14th account
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(14), "", "1");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//search for the 14th account
			filterDql.put("accountID", "100000");
			filterDql.put("state", "'@UNFINISHED CASES PURGE&'");
			filterDql.put("accountType",
					"'ThisLongSentence\\'Is400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREIsPunctuationsInThis\\'PassageONLYSpacesAreKeptANDACombinations\\'OfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			//as ACE-1834 is resolved, value is changed to -999999999999.998
			filterDql.put("accountLiabilityHolding", "-999999999999.998");
			filterDql.put("activeationTimeStamp", "2129-12-09T23:11:39.891Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "1");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 0, "children_of_the_accounts", response);
			filterDql.clear();

			//link all cases including itself
			linksPostRequestBody = formulateBodyForLinkCases(0, 15, updatedCaseRefAccounts, "children_of_the_accounts");
			response = linksFunction.createLinks(updatedCaseRefAccounts.get(13), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//this navigateBy is almost like a findBy
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 13, "children_of_the_accounts", response);
			filterDql.clear();

			//clear the filter map and add a new value
			filterMap.clear();
			filterMap.put("name", "parent_account");

			//this navigateBy is almost like a findBy
			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "10");
			assertLinkedCases(0, 1, 1, updatedCaseRefAccounts, 13, "parent_account", response);
			filterDql.clear();

			//delete all links and see navigate by returns 0 results
			response = linksFunction.deleteLinks(updatedCaseRefAccounts.get(13), "children_of_the_accounts", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "15", "checking response for number of links deleted");

			filterDql.put("accountID", "123456");
			filterDql.put("state", "'dräçÅrûß'");
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");
			filterDql.put("accountLiabilityHolding", "1.234");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(13), "", "10");
			assertLinkedCases(0, 0, 0, updatedCaseRefAccounts, 13, "", response);
			filterDql.clear();

		}
		finally
		{
			forceUndeploy(deploymentId);
			forceUndeploy(deploymentIdApp2);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
	// The test needs to be updated to cope with this.
	// --------------------------------------------------------------------------------------------------
	//Test findBy(criteria) with isInTerminalState and navigateBy(criteria) on null data
	//@Test(description = "Test findBy(criteria) with isInTerminalState and navigateBy(criteria) on null data")
	public void testFindAndNavigateByOnNullData() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException,
			ArgumentException, DataModelSerializationException, InterruptedException
	{
		List<String> caseRefsNewCaseType = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsNewCaseType.clear();
		caseRefsCustomers.clear();
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		String skip = "0";
		String top = "5";

		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		try
		{
			//new case type data
			String casedataNewCaseTypeEmpty = "{}";
			String casedataNewCaseTypeWithValue = "{\"state\": \"undeployed\", \"text\": \"text value\", \"number\": -123456.789, \"date\": \"2001-01-01\", \"time\": \"02:25:31\", \"dateTimeTZ\": \"2001-02-02T16:06:30.304Z\"}";
			String summaryNewCaseTypeEmpty = "{\"state\": \"deployed\", \"id\": \"New Case - 0001 - ID\"}";
			String summaryNewCaseTypeWithValue = "{\"id\": \"New Case - 0002 - ID\", \"state\": \"undeployed\", \"text\": \"text value\", \"number\": -123456.789, \"date\": \"2001-01-01\", \"time\": \"02:25:31\", \"dateTimeTZ\": \"2001-02-02T16:06:30.304Z\"}";

			//searchable attributes having null value
			String[] nullSearchableAttributeTypes = {"text", "number", "date", "time", "dateTimeTZ"};
			String[] searchableAttributeWithValue = {"'text value'", "-123456.789", "2001-01-01", "02:25:31",
					"2001-02-02T16:06:30.304Z"};

			//deploy v1
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 1 customer
			caseRefsCustomers
					.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "REGULAR1KTO10K"));

			//upgrade - new case type is deployed
			upgrade();

			//create new case type cases and store the case references			
			List<String> casedataNewCaseType = new ArrayList<String>();
			casedataNewCaseType.add(casedataNewCaseTypeEmpty);
			casedataNewCaseType.add(casedataNewCaseTypeWithValue);
			response = cases.createCases(caseTypeNewCaseType, casedataNewCaseType);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int i = 0; i < 2; i++)
			{
				caseRefsNewCaseType.add(jsonPath.getString("[" + i + "]"));
			}

			//findBy(criteria) using null value for individual searchable attributes
			filterMap.put("caseType", caseTypeNewCaseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");

				response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
				System.out.println("values in the loop " + " search attribute value = "
						+ nullSearchableAttributeTypes[searchAttributeIterator]); // useful for debugging
				assertCasedataSummaryReference(summaryNewCaseTypeEmpty, summaryNewCaseTypeEmpty,
						caseRefsNewCaseType.get(0), response);
			}
			filterDql.clear();
			filterMap.clear();

			//findBy(criteria) with big criteria having all the searchable attributes except id and state as null
			filterMap.put("caseType", caseTypeNewCaseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");
			}
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(summaryNewCaseTypeEmpty, summaryNewCaseTypeEmpty, caseRefsNewCaseType.get(0),
					response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//findBy(criteria) with big criteria having all the searchable attributes as null and id & state set to correct value
			filterMap.put("caseType", caseTypeNewCaseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");
			}
			filterDql.put("id", "'New Case - 0001 - ID'");
			filterDql.put("state", "'deployed'");
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(summaryNewCaseTypeEmpty, summaryNewCaseTypeEmpty, caseRefsNewCaseType.get(0),
					response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//dql with isInTerminalState flag set to FALSE
			filterMap.put("caseType", caseTypeNewCaseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("isInTerminalState", "FALSE");
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");
			}
			filterDql.put("id", "'New Case - 0001 - ID'");
			filterDql.put("state", "'deployed'");
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(summaryNewCaseTypeEmpty, summaryNewCaseTypeEmpty, caseRefsNewCaseType.get(0),
					response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//findBy(criteria) with big criteria having all the searchable attributes NOT null and id & state set to correct value
			filterMap.put("caseType", caseTypeNewCaseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator],
						searchableAttributeWithValue[searchAttributeIterator]);
			}
			filterDql.put("id", "'New Case - 0002 - ID'");
			filterDql.put("state", "'undeployed'");
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(summaryNewCaseTypeWithValue, summaryNewCaseTypeWithValue,
					caseRefsNewCaseType.get(1), response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//link both cases of new case type to customer
			linksPostRequestBody = formulateBodyForLinkCases(0, 2, caseRefsNewCaseType, "new_case_types_to_customers");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//navigateBy(criteria) using null value for individual searchable attributes 
			filterMap.put("name", "new_case_types_to_customers");

			//navigateBy(criteria) using null value for individual searchable attributes 
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");

				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), skip, top);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " search attribute value = "
						+ nullSearchableAttributeTypes[searchAttributeIterator]); // useful for debugging

				assertLinkedCases(0, 1, 1, caseRefsNewCaseType, 0, "new_case_types_to_customers", response);
			}
			filterDql.clear();
			filterMap.clear();

			//navigateBy(criteria) with big criteria having all the searchable attributes except id and state as null
			filterMap.put("name", "new_case_types_to_customers");
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");
			}
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), skip, top);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			assertLinkedCases(0, 1, 1, caseRefsNewCaseType, 0, "new_case_types_to_customers", response);
			filterDql.clear();
			filterMap.clear();

			//navigateBy(criteria) with big criteria having all the searchable attributes except id and state as null
			filterMap.put("name", "new_case_types_to_customers");
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator], "null");
			}
			filterDql.put("id", "'New Case - 0001 - ID'");
			filterDql.put("state", "'deployed'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), skip, top);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			assertLinkedCases(0, 1, 1, caseRefsNewCaseType, 0, "new_case_types_to_customers", response);
			filterDql.clear();
			filterMap.clear();

			//navigateBy(criteria) with big criteria having all the searchable attributes with value and id & state set to correct values
			filterMap.put("name", "new_case_types_to_customers");
			for (int searchAttributeIterator = 0; searchAttributeIterator < nullSearchableAttributeTypes.length; searchAttributeIterator++)
			{
				filterDql.put(nullSearchableAttributeTypes[searchAttributeIterator],
						searchableAttributeWithValue[searchAttributeIterator]);
			}
			filterDql.put("id", "'New Case - 0002 - ID'");
			filterDql.put("state", "'undeployed'");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), skip, top);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			assertLinkedCases(0, 1, 1, caseRefsNewCaseType, 1, "new_case_types_to_customers", response);
			filterDql.clear();
			filterMap.clear();

			//delete all links
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "new_case_types_to_customers", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking response for number of links deleted");
		}
		finally
		{
			forceUndeploy(deploymentIdApp2);
			forceUndeploy(deploymentIdApp1);
		}

	}

}
