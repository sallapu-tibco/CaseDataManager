package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBodyItem;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.rest.functions.CaseLinksFunction;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DirectoryEngineFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.RestDeploymentFunctions;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class TerminalStateUpdatesTest extends Utils
{

	private Response					response										= null;

	private static final String			caseTypeAccount									= "com.example.bankdatamodel.Account";

	private static final String			applicationMajorVersion							= "1";

	private RestDeploymentFunctions		rest											= new RestDeploymentFunctions();

	private DirectoryEngineFunctions	de												= new DirectoryEngineFunctions();

	private CasesFunctions				cases											= new CasesFunctions();

	private CaseLinksFunction			links											= new CaseLinksFunction();

	private GetPropertiesFunction		properties										= new GetPropertiesFunction();

	private BigInteger					deploymentIdApp1								= BigInteger.valueOf(8);

	private BigInteger					deploymentIdApp2								= BigInteger.valueOf(9);

	private List<String>				caseReferencesAccounts							= new ArrayList<>();

	private List<String>				caseReferencesAccountsUpdated					= new ArrayList<>();

	//create account cases in non-terminal states
	private List<String>				createCasedataAccountNonTerminal				= Arrays.asList(
			"{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}",
			"{\"state\": \"@FROZEN&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN2 2BC\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-05-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 2345.67}");

	//summary of account cases in non-terminal states
	private List<String>				summaryAccountNonTerminal						= Arrays.asList(
			"{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-04-26\"," + "\"timeofAccountOpening\": \"17:24:45\"}",
			"{\"state\": \"@FROZEN&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-05-27\"," + "\"timeofAccountOpening\": \"18:24:45\"}");

	//update account cases non-terminal to terminal states - STATE & VALUE CHANGE
	private List<String>				updateCasedataAccountNonTerminalToTerminal		= Arrays.asList(
			"{\"state\": \"@CANCELLED&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}",
			"{\"state\": \"@TERMINATED&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN2 2BC\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-05-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 2345.67}");

	//summary of account cases updated from non-terminal state to terminal states
	private List<String>				summaryAccountNonTerminalToTerminal				= Arrays.asList(
			"{\"state\": \"@CANCELLED&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-04-26\"," + "\"timeofAccountOpening\": \"17:24:45\"}",
			"{\"state\": \"@TERMINATED&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-05-27\"," + "\"timeofAccountOpening\": \"18:24:45\"}");

	private String						summaryAccountNonTerminalV2						= "{\"state\": \"@CANCELLED&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
			+ "\"accountLiabilityHolding\": 1234.56}";

	//update account cases terminal(after v2, it is not terminal) to non-terminal state
	private String						updateCasedataAccountNonTerminalToNonTerminalV2	= "{\"state\": \"dräçÅrûß\", \"accountID\": 1, \"accountType\": \"CURRENT\","
			+ "\"activePortfolio\": true, \"portfolioWebsite\": \"https://www.myportfoliomanager.com\", \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
			+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
			+ "\"secondLine\": \"Address Line 2\"}}, \"activeationTimeStamp\": \"2019-03-21T12:34:56.789Z\", \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
			+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}";

	//summary of account cases updated from non-terminal state to terminal states
	private String						summaryAccountNonTerminalToNonTerminalV2		= "{\"state\": \"dräçÅrûß\", \"accountID\": 1, \"accountType\": \"CURRENT\","
			+ "\"accountLiabilityHolding\": 1234.56, \"activeationTimeStamp\": \"2019-03-21T12:34:56.789Z\"}";

	//update account cases non-terminal(new state in v2) to a new terminal in v2
	private String						updateCasedataAccountNonTerminalToTerminalV2	= "{\"state\": \"@UNFINISHED CASES PURGE&\", \"accountID\": 1, \"accountType\": \"MORTGAGE\","
			+ "\"activePortfolio\": false, \"portfolioWebsite\": \"https://www.myportfoliomanager.com\", \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"Bhudargarh Building Society\", \"institutionBranchAddress\": "
			+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
			+ "\"secondLine\": \"Address Line 2\"}}, \"activeationTimeStamp\": \"2019-03-21T12:34:56.789Z\", \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
			+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 0.01}";

	//summary of account cases non-terminal(new state in v2) to a new terminal in v2
	private String						summaryAccountNonTerminalToTerminalV2			= "{\"state\": \"@UNFINISHED CASES PURGE&\", \"accountID\": 1, \"accountType\": \"MORTGAGE\","
			+ "\"accountLiabilityHolding\": 0.01, \"activeationTimeStamp\": \"2019-03-21T12:34:56.789Z\"}";

	//create account cases in terminal state
	private List<String>				createCasedataAccountTerminal					= Arrays.asList(
			"{\"state\": \"@TERMINATED&\", \"accountID\": 3, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}",
			"{\"state\": \"@CANCELLED&\", \"accountID\": 4, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN2 2BC\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-05-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 2345.67}");

	//summary of account cases in terminal states
	private List<String>				summaryAccountTerminal							= Arrays.asList(
			"{\"state\": \"@TERMINATED&\", \"accountID\": 3, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-04-26\"," + "\"timeofAccountOpening\": \"17:24:45\"}",
			"{\"state\": \"@CANCELLED&\", \"accountID\": 4, \"accountType\": \"CURRENT\","
					+ "\"dateofAccountOpening\": \"2019-05-27\"," + "\"timeofAccountOpening\": \"18:24:45\"}");

	//update account cases in terminal state - VALUE CHANGE (random)
	private List<String>				updateCasedataAccountTerminalValueChange		= Arrays.asList(
			"{\"state\": \"@TERMINATED&\", \"accountID\": 3, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Athens\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"AT1 001\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-12-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 0.56}",
			"{\"state\": \"@CANCELLED&\", \"accountID\": 4, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Athens\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"AT2 002\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-06-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 0.67}");

	//update account cases in terminal state - STATE CHANGE to Other Terminal
	private List<String>				updateCasedataAccountTerminalToTerminal			= Arrays.asList(
			"{\"state\": \"@CANCELLED&\", \"accountID\": 3, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}",
			"{\"state\": \"@TERMINATED&\", \"accountID\": 4, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN2 2BC\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-05-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 2345.67}");

	//update account cases in terminal state - STATE CHANGE to non-terminal
	private String						updateCasedataAccountTerminalToNonTerminal		= "{\"state\": \"@REINSTATED&\", \"accountID\": 3, \"accountType\": \"CURRENT\","
			+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
			+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
			+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
			+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}";

	//deploy org model and data project
	private void prerequisites() throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		try
		{
			response = rest.deployApplication("", "/apps/OrgModel/AceProject3-Organisational.rasc");
		}
		catch (Exception e)
		{
			System.out.println("exception in deployment");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
	}

	//create single case
	private List<String> createSingleCase(String caseType, String casedata, String resourceName)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException, InternalException,
			RuntimeApplicationException
	{
		List<String> caseRefsOutput = new ArrayList<>();
		List<String> casedataInput = new ArrayList<>();

		try
		{
			casedataInput.add(casedata);
			response = cases.createCases(caseType, 1, casedataInput, resourceName);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 1; caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating case");
			System.out.println(e.getMessage());
		}
		return caseRefsOutput;
	}

	//create single case
	private List<String> createMultipleCase(String caseType, List<String> casedata, String resourceName)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException, InternalException,
			RuntimeApplicationException
	{
		List<String> caseRefsOutput = new ArrayList<>();
		List<String> casedataInput = new ArrayList<>();

		try
		{
			for (int i = 0; i < casedata.size(); i++)
			{
				casedataInput.add(casedata.get(i));
			}
			response = cases.createCases(caseType, 1, casedataInput, resourceName);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < casedata.size(); caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating case");
			System.out.println(e.getMessage());
		}
		return caseRefsOutput;
	}

	//update single case
	private List<String> updateForSuccess(String caseReference, String casedata, String resourceName)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException, InternalException,
			RuntimeApplicationException
	{
		List<String> caseRefsInput = new ArrayList<>();
		List<String> caseRefsOutput = new ArrayList<>();

		caseRefsInput.add(caseReference);

		try
		{
			response = cases.updateSingleCase(caseRefsInput.get(0), casedata, resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsOutput.add(jsonPath.getString("[" + 0 + "]"));
		}
		catch (Exception e)
		{
			System.out.println("problem while creating cases");
			System.out.println(e.getMessage());
		}

		return caseRefsOutput;
	}

	//update single case to result in failure
	private Response updateCheck(String caseReference, String casedata, String resourceName)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException, InternalException,
			RuntimeApplicationException
	{
		List<String> caseRefsInput = new ArrayList<>();

		caseRefsInput.add(caseReference);

		try
		{
			response = cases.updateSingleCase(caseRefsInput.get(0), casedata, resourceName);
			System.out.println(response.asString());
		}
		catch (Exception e)
		{
			System.out.println("problem while creating cases");
			System.out.println(e.getMessage());
		}

		return response;
	}

	private Response arrayUpdateMultiple(List<String> caseReferences, List<String> casedata, String resourceName)
			throws IOException, InterruptedException
	{
		List<String> caseRefsInput = new ArrayList<>();

		for (int i = 0; i < caseReferences.size(); i++)
		{
			caseRefsInput.add(caseReferences.get(i));
		}

		CasesPutRequestBody body = new CasesPutRequestBody();
		for (int i = 0; i < caseReferences.size(); i++)
		{
			CasesPutRequestBodyItem item = new CasesPutRequestBodyItem();
			item.setCasedata(casedata.get(i));
			item.setCaseReference(caseRefsInput.get(i));
			body.add(item);
		}

		response = cases.arrayUpdateCases(body, resourceName);
		System.out.println(response.asString());

		return response;
	}

	private void assertUpdateFailure(String caseReference, Response response) throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), properties.getPreventUpdateTerminalState().errorResponse,
				"incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = properties.getPreventUpdateTerminalState().errorMsg;
		errorMessage = errorMessage.replaceAll("\\{.*?\\}", caseReference);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getPreventUpdateTerminalState().errorCode);
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
		Assert.assertEquals(response.getStatusCode(), properties.getPreventUpdateTerminalState().errorResponse);

		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace").contains(
				"com.tibco.bpm.cdm.api.exception.ReferenceException: Case is in a terminal state so cannot be updated: "
						+ caseReference),
				true, "incorrect stacktrace");

		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1, "incorrect number of context attributes");

		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReference);
	}

	private void assertBadIsInTerminalStateValue(String isInTerminalStateValue, Response response) throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), properties.getBadIsInTerminalStateValue().errorResponse,
				"incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = properties.getBadIsInTerminalStateValue().errorMsg;
		errorMessage = errorMessage.replaceAll("\\{.*?\\}", isInTerminalStateValue);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadIsInTerminalStateValue().errorCode);
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
		Assert.assertEquals(response.getStatusCode(), properties.getBadIsInTerminalStateValue().errorResponse);

		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace").contains(
				"com.tibco.bpm.cdm.core.rest.RESTRequestException: The only value allowed for isInTerminalState is FALSE, not: "
						+ isInTerminalStateValue),
				true, "incorrect stacktrace");

		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1, "incorrect number of context attributes");

		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "isInTerminalState");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), isInTerminalStateValue);
	}

	private String getCasesReturnLastModificationTimestamp(String caseType, String isInTerminalState,
			String casedataToMatch, String SummaryToMatch, String caseReference, int positionToAssert, int count,
			String resourceName) throws IOException, InterruptedException
	{

		String skip = "0";
		String top = "10";

		String modificationTSOut = "";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		if (!(isInTerminalState.equals("")))
		{
			filterMap.put("isInTerminalState", isInTerminalState);
		}

		//get cases
		response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", resourceName);
		System.out.println(response.asString());

		if (isInTerminalState.equals("FALSE") || isInTerminalState.equals(""))
		{
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), count, "incorrect number of objects returned");

			if (count > 0)
			{
				//assert casedata
				Assert.assertTrue(
						assertUnsortedData(jsonPath.getString("[" + positionToAssert + "].casedata"), casedataToMatch),
						"casedata does not match");

				//assert metadata
				Assert.assertNotNull(jsonPath.getString("[" + positionToAssert + "].metadata"),
						"metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[" + positionToAssert + "].metadata.creationTimestamp"),
						"metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[" + positionToAssert + "].metadata.modifiedBy"),
						"metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[" + positionToAssert + "].metadata.modificationTimestamp"),
						"metadata should be tagged");
				//assign modificationTimestamp
				modificationTSOut = jsonPath.getString("[" + positionToAssert + "].metadata.modificationTimestamp");

				//assert case reference
				Assert.assertEquals(jsonPath.getString("[" + positionToAssert + "].caseReference"), caseReference,
						"caseRef should be tagged");

				//assert summary
				Assert.assertTrue(
						assertUnsortedData(jsonPath.getString("[" + positionToAssert + "].summary"), SummaryToMatch),
						"summary does not match");
			}

			return modificationTSOut;
		}

		else
		{
			assertBadIsInTerminalStateValue(isInTerminalState, response);
			return "error";
		}
	}

	private void getCases(String caseType, String isInTerminalState, List<String> casedataToMatch,
			List<String> SummaryToMatch, List<String> caseReference, int count, String resourceName)
			throws IOException, InterruptedException
	{

		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		if (!(isInTerminalState.equals("")))
		{
			filterMap.put("isInTerminalState", isInTerminalState);
		}

		//get cases
		response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", resourceName);
		System.out.println(response.asString());

		if (isInTerminalState.equals("FALSE") || isInTerminalState.equals(""))
		{
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), count, "incorrect number of objects returned");

			if (count > 0)
			{
				for (int i = 0; i < count; i++)
				{
					//assert casedata
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[" + i + "].casedata"),
							casedataToMatch.get(((count - 1) - i))), "casedata does not match");

					//assert metadata
					Assert.assertNotNull(jsonPath.getString("[" + i + "].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[" + i + "].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[" + i + "].metadata.modifiedBy"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[" + i + "].metadata.modificationTimestamp"),
							"metadata should be tagged");

					//assert case reference
					Assert.assertEquals(jsonPath.getString("[" + i + "].caseReference"),
							caseReference.get(((count - 1) - i)), "caseRef should be tagged");

					//assert summary
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[" + i + "].summary"),
							SummaryToMatch.get(((count - 1) - i))), "summary does not match");
				}
			}
		}

	}

	//create links, get links and verify
	private void linkUnlink(String sourceCaseReference, String targetCaseReference, String linkName,
			String resourceName) throws IOException, InterruptedException
	{
		//create link
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		Link link = new Link();
		link.setCaseReference(targetCaseReference);
		link.setName(linkName);
		linksPostRequestBody.add(link);

		response = links.createLinks(sourceCaseReference, linksPostRequestBody, resourceName);
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		Assert.assertEquals(response.asString(), "", "checking for empty response");

		//get link
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDQL = new HashMap<String, String>();
		filterMap.put("name", linkName);

		String skip = "0";
		String top = "10";

		response = links.getLinks(filterMap, filterDQL, sourceCaseReference, skip, top, resourceName);
		System.out.println(response.asString());

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
		Assert.assertEquals(obj.size(), 1, "multiple or no cases are returned");

		Assert.assertEquals(obj.get(0).getCaseReference(), targetCaseReference, "linked case reference is incorrect");
		Assert.assertEquals(obj.get(0).getName(), linkName, "name is incorrect");

		//remove link
		response = links.deleteLinks(sourceCaseReference, "", "NONE", new ArrayList<>(), resourceName);
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

		//verify link removed
		response = links.getLinks(filterMap, filterDQL, sourceCaseReference, skip, top, resourceName);
		System.out.println(response.asString());

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
		Assert.assertEquals(obj.size(), 0, "cases are returned");

	}

	//Test to perform single case update to verify a case in terminal state cannot be updated - value change and/or state change
	@Test(description = "Test to perform single case update to verify a case in terminal state cannot be updated - value change and/or state change")
	private void testTerminalStateUpdate() throws PersistenceException, DeploymentException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, InterruptedException
	{
		caseReferencesAccounts.clear();
		caseReferencesAccountsUpdated.clear();

		String modificationTimestamp = "";
		String resourceName = RESOURCE_TONY;

		try
		{
			//deploy org model
			prerequisites();

			//Tony would be acting as a user to create, read and update
			de.mapResources(CONTAINER_NAME, resourceName, POSITION_CREATE_READ_UPDATE_DELETE);

			//deploy
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create a case in non-terminal state
			caseReferencesAccounts
					.addAll(createSingleCase(caseTypeAccount, createCasedataAccountNonTerminal.get(0), resourceName));
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE", createCasedataAccountNonTerminal.get(0),
					summaryAccountNonTerminal.get(0), caseReferencesAccounts.get(0), 0, 1, resourceName);

			//create a cases in terminal state - read modificationTimestamp
			caseReferencesAccounts
					.addAll(createSingleCase(caseTypeAccount, createCasedataAccountTerminal.get(0), resourceName));

			//isInTerminalState can only have value FALSE - anything elese should result in an error
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "TRUE", createCasedataAccountTerminal.get(0),
					summaryAccountTerminal.get(0), caseReferencesAccounts.get(1), 0, 2, resourceName);

			//isInTerminalState can only have value FALSE - anything elese should result in an error - passing no value
			modificationTimestamp = getCasesReturnLastModificationTimestamp(caseTypeAccount, "",
					createCasedataAccountTerminal.get(0), summaryAccountTerminal.get(0), caseReferencesAccounts.get(1),
					0, 2, resourceName);

			//update the case in non-terminal state to terminal state - allowed
			caseReferencesAccountsUpdated.addAll(updateForSuccess(caseReferencesAccounts.get(0),
					updateCasedataAccountNonTerminalToTerminal.get(0), resourceName));

			//read the case with flag isTerminal FALSE - no cases will be /get
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE", "", "", "", 0, 0, resourceName);

			//get cases
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "",
					updateCasedataAccountNonTerminalToTerminal.get(0), summaryAccountNonTerminalToTerminal.get(0),
					caseReferencesAccountsUpdated.get(0), 0, 2, resourceName);

			//update the case in terminal state to non-terminal state - not allowed
			response = updateCheck(caseReferencesAccounts.get(1), updateCasedataAccountTerminalToNonTerminal,
					resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(1), response);

			//check the modificationTimestamp has remained same
			String tempModoficationTimestamp = getCasesReturnLastModificationTimestamp(caseTypeAccount, "",
					createCasedataAccountTerminal.get(0), summaryAccountTerminal.get(0), caseReferencesAccounts.get(1),
					1, 2, resourceName);
			Assert.assertEquals(modificationTimestamp, tempModoficationTimestamp,
					"modification timestamp is not updated");

			//update the case in terminal state to other state - not allowed
			response = updateCheck(caseReferencesAccounts.get(1), updateCasedataAccountTerminalToTerminal.get(0),
					resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(1), response);

			//check the modificationTimestamp has remained same
			tempModoficationTimestamp = getCasesReturnLastModificationTimestamp(caseTypeAccount, "",
					createCasedataAccountTerminal.get(0), summaryAccountTerminal.get(0), caseReferencesAccounts.get(1),
					1, 2, resourceName);
			Assert.assertEquals(modificationTimestamp, tempModoficationTimestamp,
					"modification timestamp is not updated");

			//update the value not allowed
			response = updateCheck(caseReferencesAccounts.get(1), updateCasedataAccountTerminalValueChange.get(0),
					resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(1), response);

			//check the modificationTimestamp has remained same
			tempModoficationTimestamp = getCasesReturnLastModificationTimestamp(caseTypeAccount, "",
					createCasedataAccountTerminal.get(0), summaryAccountTerminal.get(0), caseReferencesAccounts.get(1),
					1, 2, resourceName);
			Assert.assertEquals(modificationTimestamp, tempModoficationTimestamp,
					"modification timestamp is not updated");

			// --------------------------------------------------------------------------------------------------
			// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
			// The test needs to be updated to cope with this.
			// --------------------------------------------------------------------------------------------------

			//			//check that a case can be self-linked to a case in terminal state since the link is not considered an update it should be possible
			//			linkUnlink(caseReferencesAccounts.get(0), caseReferencesAccountsUpdated.get(0), "parent_account",
			//					resourceName);
		}
		finally
		{
			caseReferencesAccounts.clear();
			caseReferencesAccountsUpdated.clear();

			//undeploy application
			forceUndeploy(deploymentIdApp1);

			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}

	//Test to perform single case update to verify a case in terminal state, if marked in v2 upgrade, can be updated - value change and state change
	@Test(description = "Test to perform single case update to verify a case in terminal state, if marked in v2 upgrade, can be updated - value change and state change")
	private void testTerminalStateUpdateWithUpgrade() throws PersistenceException, DeploymentException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException, InterruptedException
	{
		caseReferencesAccounts.clear();
		caseReferencesAccountsUpdated.clear();

		String resourceName = RESOURCE_TONY;

		try
		{
			//deploy
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create a case is non-terminal state - @ACTIVE&
			caseReferencesAccounts
					.addAll(createSingleCase(caseTypeAccount, createCasedataAccountNonTerminal.get(0), resourceName));

			//get cases with flag isTerminal FALSE
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE", createCasedataAccountNonTerminal.get(0),
					summaryAccountNonTerminal.get(0), caseReferencesAccounts.get(0), 0, 1, resourceName);

			//update the case in non-terminal state to terminal state - allowed - @CANCELLED&
			caseReferencesAccountsUpdated.addAll(updateForSuccess(caseReferencesAccounts.get(0),
					updateCasedataAccountNonTerminalToTerminal.get(0), resourceName));

			//get cases with flag isTerminal FALSE - 0 cases
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE", "", "", "", 0, 0, resourceName);

			//upgrade the application so that the case in terminal state is not terminal any more - @CANCELLED& is not terminal any more
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//read the case with flag isTerminal FALSE - 1 case
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE",
					updateCasedataAccountNonTerminalToTerminal.get(0), summaryAccountNonTerminalV2,
					caseReferencesAccountsUpdated.get(0), 0, 1, resourceName);

			//update the case to other non-terminal state - allowed - dräçÅrûß
			caseReferencesAccountsUpdated.addAll(updateForSuccess(caseReferencesAccountsUpdated.get(0),
					updateCasedataAccountNonTerminalToNonTerminalV2, resourceName));

			//read the case with flag isTerminal FALSE - 1 case
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE",
					updateCasedataAccountNonTerminalToNonTerminalV2, summaryAccountNonTerminalToNonTerminalV2,
					caseReferencesAccountsUpdated.get(1), 0, 1, resourceName);

			//update the case to other terminal state - allowed - @UNFINISHED CASES PURGE&
			caseReferencesAccountsUpdated.addAll(updateForSuccess(caseReferencesAccountsUpdated.get(1),
					updateCasedataAccountNonTerminalToTerminalV2, resourceName));

			//read the cases
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "", updateCasedataAccountNonTerminalToTerminalV2,
					summaryAccountNonTerminalToTerminalV2, caseReferencesAccountsUpdated.get(2), 0, 1, resourceName);

			//get cases with flag isTerminal FALSE - 0 cases
			getCasesReturnLastModificationTimestamp(caseTypeAccount, "FALSE", "", "", "", 0, 0, resourceName);
		}
		finally
		{
			caseReferencesAccounts.clear();
			caseReferencesAccountsUpdated.clear();

			//undeploy application
			forceUndeploy(deploymentIdApp2);
			forceUndeploy(deploymentIdApp1);

			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}

	//Test to perform updates on multiple cases (mix of terminal and non-terminal) to verify that updates cannot be perfomred
	@Test(description = "Test to perform updates on multiple cases (mix of terminal and non-terminal) to verify that updates cannot be perfomred")
	private void testTerminalStateArrayUpdate() throws PersistenceException, DeploymentException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, InterruptedException
	{
		caseReferencesAccounts.clear();
		caseReferencesAccountsUpdated.clear();

		String resourceName = RESOURCE_TONY;

		try
		{
			//deploy org model
			prerequisites();

			//Tony would be acting as a user to create, read and update
			de.mapResources(CONTAINER_NAME, resourceName, POSITION_CREATE_READ_UPDATE_DELETE);

			//deploy
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create a case in non-terminal state
			caseReferencesAccounts
					.addAll(createMultipleCase(caseTypeAccount, createCasedataAccountNonTerminal, resourceName));
			caseReferencesAccounts
					.addAll(createMultipleCase(caseTypeAccount, createCasedataAccountTerminal, resourceName));

			getCases(caseTypeAccount, "FALSE", createCasedataAccountNonTerminal, summaryAccountNonTerminal,
					caseReferencesAccounts.subList(0, 2), 2, resourceName);

			List<String> casedataArrays = new ArrayList<>();
			casedataArrays.addAll(createCasedataAccountNonTerminal);
			casedataArrays.addAll(createCasedataAccountTerminal);

			List<String> summaryArrays = new ArrayList<>();
			summaryArrays.addAll(summaryAccountNonTerminal);
			summaryArrays.addAll(summaryAccountTerminal);

			//get all cases
			getCases(caseTypeAccount, "", casedataArrays, summaryArrays, caseReferencesAccounts, 4, resourceName);

			//update multiple cases
			//case 1 and case 2 are in non-terminal states
			//however case 3 and case 4 in terminal states
			//so ideally the update should not be allowed for any cases

			//update for the value change for terminal states
			List<String> casedataUpdateArrays = new ArrayList<>();
			casedataUpdateArrays.addAll(updateCasedataAccountNonTerminalToTerminal);
			casedataUpdateArrays.addAll(updateCasedataAccountTerminalValueChange);

			response = arrayUpdateMultiple(caseReferencesAccounts, casedataUpdateArrays, resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(2), response);

			//get all cases
			getCases(caseTypeAccount, "", casedataArrays, summaryArrays, caseReferencesAccounts, 4, resourceName);

			//update from terminal states to other terminal state
			casedataUpdateArrays.clear();
			casedataUpdateArrays.addAll(updateCasedataAccountNonTerminalToTerminal);
			casedataUpdateArrays.addAll(updateCasedataAccountTerminalToTerminal);

			response = arrayUpdateMultiple(caseReferencesAccounts, casedataUpdateArrays, resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(2), response);

			//get all cases
			getCases(caseTypeAccount, "", casedataArrays, summaryArrays, caseReferencesAccounts, 4, resourceName);

			//update from terminal states to other non-terminal state
			casedataUpdateArrays.clear();
			casedataUpdateArrays.addAll(updateCasedataAccountNonTerminalToTerminal);
			casedataUpdateArrays.add(updateCasedataAccountTerminalToNonTerminal);

			response = arrayUpdateMultiple(caseReferencesAccounts.subList(0, 3), casedataUpdateArrays, resourceName);
			assertUpdateFailure(caseReferencesAccounts.get(2), response);

			//get all cases
			getCases(caseTypeAccount, "", casedataArrays, summaryArrays, caseReferencesAccounts, 4, resourceName);
		}
		finally
		{
			caseReferencesAccounts.clear();
			caseReferencesAccountsUpdated.clear();

			//undeploy application
			forceUndeploy(deploymentIdApp1);

			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}

}