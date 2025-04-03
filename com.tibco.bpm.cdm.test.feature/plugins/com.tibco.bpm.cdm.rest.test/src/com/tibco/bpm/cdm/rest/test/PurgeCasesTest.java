package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.AccountCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DatabaseQueriesFunction;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class PurgeCasesTest extends Utils
{

	AccountCreatorFunction			accountCreator			= new AccountCreatorFunction();

	CaseTypesFunctions				caseTypes				= new CaseTypesFunctions();

	CasesFunctions					cases					= new CasesFunctions();

	GetPropertiesFunction			properties				= new GetPropertiesFunction();

	int								validStatusCode			= 200;

	int								invalidStatusCode		= 400;

	int								notFoundStatusCode		= 404;

	private JsonPath				jsonPath				= null;

	private static final String		caseType				= "com.example.bankdatamodel.Account";

	private static final String		applicationMajorVersion	= "1";

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger						deploymentIdApp1		= BigInteger.valueOf(9);

	BigInteger						deploymentIdApp2		= BigInteger.valueOf(10);

	private Response				response				= null;

	static DatabaseQueriesFunction	executeStmt				= new DatabaseQueriesFunction();

	private String reduceTimestampBy1ms(String timestamp) throws ParseException
	{
		//new reilable method to reduce time by 1 milli-second
		String newTimestamp = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Calendar cal = Calendar.getInstance();
		Date parse = dateFormat.parse(timestamp);
		cal.setTimeInMillis(parse.getTime() - 1);
		newTimestamp = dateFormat.format(cal.getTime());

		return newTimestamp;
	}

	//Test to purge cases in a particular state
	@Test(description = "Test to purge cases in a particular terminal state")
	public void purgeParticularTerminalStates()
			throws IOException, InterruptedException, PersistenceException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, ClassNotFoundException, SQLException
	{
		//undeploy application
		forceUndeploy(deploymentIdApp1);

		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 10 cases in "@TERMINATED&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(1000, 10, "@TERMINATED&");
			//create 10 cases in "@CANCELLED&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(2000, 10, "@CANCELLED&");

			//create filter
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@TERMINATED&");
			String top = "1000";

			//count cases
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 10);

			//verify entries in the cdm_cases_int table
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "20", "total 20 cases are not created");

			//purge cases in @TERMINATED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "10", "incorrect number of cases are purged");

			//count cases
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//verify entries in the cdm_cases_int table
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "10", "10 cases are not present");

			//change the filter map
			filterMap.remove("caseState");
			filterMap.put("caseState", "@CANCELLED&");

			//purge cases in @CANCELLED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "10", "incorrect number of cases are purged");

			//count cases
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//verify entries in the cdm_cases_int table
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "0", "cases are present");

			//ineffective purge - since there is no data in the tables

			//purge cases in @CANCELLED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "0", "incorrect number of cases are purged");

			//count cases
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//verify entries in the cdm_cases_int table
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "0", "cases are present");
		}
		finally
		{
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to purge cases after a particular modification timestamp
	@Test(description = "Test to purge cases after a particular modification timestamp")
	public void purgeParticularModificationTS()
			throws IOException, InterruptedException, PersistenceException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, ClassNotFoundException, SQLException, ParseException
	{
		//undeploy application
		forceUndeploy(deploymentIdApp1);

		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 50 cases in "@ACTIVE&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(3000, 50, "@ACTIVE&");
			//create 50 cases in "@REINSTATED&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(4000, 50, "@REINSTATED&");

			//Thread.sleep(CASES_TO_UPDATE);

			//create filter
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@ACTIVE&");
			String top = "1000";
			String toptoUpdate = "1";
			String modificationTimestampLastCase = "";
			String modificationTimestampIntermediate = "";
			String casedataLastCase = "";
			String caseReferenceLastCase = "";

			//count cases - ACTIVE
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 50);

			//verify entries in the cdm_cases_int table - ACTIVE AND CANCELLED
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "100", "total 100 cases are not created");

			//get casedata and modification timestamp of 50th case - ACTIVE
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			casedataLastCase = obj.get(0).getCasedata();
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			caseReferenceLastCase = obj.get(0).getCaseReference();

			//update 50th case - the update does not alter data; it is only for changing the modification timestamp - ACTIVE
			response = cases.updateCases(caseReferenceLastCase, casedataLastCase);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Thread.sleep(CASES_TO_UPDATE);

			//get new modification timestamp of 50th case - ACTIVE
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			modificationTimestampIntermediate = reduceTimestampBy1ms(modificationTimestampLastCase);

			//add modification timestamp to filter map - 1 ms reduced
			filterMap.put("modificationTimestamp", modificationTimestampIntermediate);

			//purge cases in @ACTIVE& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "49", "incorrect number of cases are purged");

			//remove the modification timestamp from the filter map
			filterMap.remove("modificationTimestamp");

			//get the count - it should be 1
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			//get the count from cdm_case_int - it should be 51
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "51", "cases are present");

			//remove the modification timestamp from the filter map
			filterMap.remove("caseState");
			filterMap.put("caseState", "@REINSTATED&");

			//get casedata and modification timestamp of 50th case - CANCELLED
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			casedataLastCase = obj.get(0).getCasedata();
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			caseReferenceLastCase = obj.get(0).getCaseReference();

			//update 50th case - the update does not alter data; it is only for changing the modification timestamp - CANCELLED
			response = cases.updateCases(caseReferenceLastCase, casedataLastCase);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//get new modification timestamp of 50th case
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			modificationTimestampIntermediate = reduceTimestampBy1ms(modificationTimestampLastCase);

			//add modification timestamp to filter map
			filterMap.put("modificationTimestamp", modificationTimestampIntermediate);

			//purge cases in @REINSTATED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "49", "incorrect number of cases are purged");

			//remove the modification timetsmp from the filter map
			filterMap.remove("modificationTimestamp");

			//get the count - it should be 1
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			//get the count from cdm_case_int - it should be 51
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "2", "cases are present");

			//add modification timestamp to filter map
			filterMap.put("modificationTimestamp", modificationTimestampLastCase);

			//purge cases in @REINSTATED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//remove the modification timetsmp from the filter map
			filterMap.remove("modificationTimestamp");

			//get the count - it should be 0
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//get the count from cdm_case_int - it should be 1
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "1", "cases are present");

			//remove the modification timestamp from the filter map
			filterMap.remove("caseState");
			filterMap.put("caseState", "@ACTIVE&");

			//purge cases in @ACTIVE& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//remove the modification timestamp from the filter map - ACTIVE
			filterMap.remove("modificationTimestamp");

			//get the count - it should be 0 - ACTIVE
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//get the count from cdm_case_int - it should be 0
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "0", "cases are present");

		}
		finally
		{
			//undeploy application
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to purge cases after cases created in v1 and v2
	@Test(description = "Test to purge cases after cases created in v1 and v2")
	public void purgeCasesAfterUpgrade()
			throws IOException, InterruptedException, PersistenceException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, ClassNotFoundException, SQLException, ParseException
	{
		//undeploy v1 and v2
		forceUndeploy(deploymentIdApp1);
		forceUndeploy(deploymentIdApp2);

		try
		{
			//create filter
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@FROZEN&");
			String top = "1000";
			String toptoUpdate = "1";
			String modificationTimestampLastCase = "";
			String modificationTimestampIntermediate = "";
			String casedataLastCase = "";
			String caseReferenceLastCase = "";

			//deploy v1 of the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 100 cases in @FROZEN& state
			accountCreator.createAccountSpecificStateOffsetDefaultCases(5000, 100, "@FROZEN&");
			//create 100 cases in @TREMPORARILY DEACTIVATED& state
			accountCreator.createAccountSpecificStateOffsetDefaultCases(6000, 100, "@TREMPORARILY DEACTIVATED&");

			//Thread.sleep(CASES_TO_UPDATE);

			//deploy v2 of the application
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//create 100 case of v2
			accountCreator.createAccountSpecificStateOffsetDefaultCases(7000, 100, "@UNFINISHED CASES PURGE&");

			//Thread.sleep(CASES_TO_UPDATE);

			//get the last case for v1 - FROZEN
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			casedataLastCase = obj.get(0).getCasedata();
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			caseReferenceLastCase = obj.get(0).getCaseReference();

			//update 100th case from v1 - change state only - it is only for changing the modification timestamp - FROZEN
			String newCasedataLastCase = casedataLastCase.replace("@FROZEN&", "@UNFINISHED CASES PURGE&");
			response = cases.updateCases(caseReferenceLastCase, newCasedataLastCase);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//get modification timestamp of this newly updated case
			//change filter - remove the state from the filter map
			filterMap.remove("caseState");
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			modificationTimestampIntermediate = reduceTimestampBy1ms(modificationTimestampLastCase);

			//create select filters for modification timestamp
			filterMap.put("modificationTimestamp", modificationTimestampIntermediate);
			filterMap.put("caseState", "@FROZEN&");

			//purge 99 cases of v1 - cases in @FROZEN& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "99", "incorrect number of cases are purged");

			//remove the modification timestamp from the filter map
			filterMap.remove("modificationTimestamp");
			filterMap.remove("caseState");

			//get the count - it should be 1
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 201);

			//get the count from cdm_case_int - it should be 201
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "201", "cases are present");

			//add modification timestamp and state for v1 in the filter
			filterMap.put("caseState", "@TREMPORARILY DEACTIVATED&");
			filterMap.put("modificationTimestamp", modificationTimestampLastCase);

			//purge 100 cases of v1 - cases in @TREMPORARILY DEACTIVATED& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "100", "incorrect number of cases are purged");

			//remove the modification timestamp from the filter map
			filterMap.remove("modificationTimestamp");
			filterMap.remove("caseState");

			//get the count - it should be 101
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 101);

			//get the count from cdm_case_int - it should be 201
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "101", "cases are present");

			//add modification timestamp and state for v2 in the filter
			filterMap.put("caseState", "@UNFINISHED CASES PURGE&");
			filterMap.put("modificationTimestamp", modificationTimestampLastCase);

			//purge 101 cases of v1 - cases in @UNFINISHED CASES PURGE& state
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.asString(), "101", "incorrect number of cases are purged");

			//remove the modification timestamp from the filter map
			filterMap.remove("modificationTimestamp");
			filterMap.remove("caseState");

			//get the count - it should be 0
			response = cases.getCases("EQ", filterMap, "", "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0);

			//get the count from cdm_case_int - it should be 201
			Assert.assertEquals(executeStmt.getCasesCountForType(host), "0", "cases are present");
		}
		finally
		{
			//undeploy v1 and v2
			forceUndeploy(deploymentIdApp1);
			forceUndeploy(deploymentIdApp2);
		}
	}

	//Test to verify the error messages for case state and/or modification timestamp
	@Test(description = "Test to verify the error messages for case state and/or modification timestamp")
	public void purgeErrorCodeTest()
			throws IOException, InterruptedException, PersistenceException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, ClassNotFoundException, SQLException
	{

		Map<String, String> filterMap = new HashMap<String, String>();

		//undeploy application
		forceUndeploy(deploymentIdApp1);

		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 1@TERMINATED& APPROVAL&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(1000, 1, "@WAITING FOR APPROVAL&");

			//create filter - not passing both case state and modification timestamp
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", EMPTY_VALUE);
			filterMap.put("modificationTimestamp", EMPTY_VALUE);

			//purge cases in @WAITING FOR APPROVAL& state
			response = cases.purgeCases(filterMap);

			//verify error - it appears only for case state, if both are not passed
			Assert.assertEquals(response.getStatusCode(), properties.getCaseStateMandatory().errorResponse,
					"incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getCaseStateMandatory().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), properties.getCaseStateMandatory().errorMsg,
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: A case state (state value) must be specified"),
					true, "incorrect stacktrace");

			//change the filter map - default modification timestamp will be used but state is still not passed 
			filterMap.remove("modificationTimestamp");
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);

			//@CANCELLED&R CANCELLATION& state
			response = cases.purgeCases(filterMap);

			//verify error - it appears only for case state, as default modification timestamp is passed
			Assert.assertEquals(response.getStatusCode(), properties.getCaseStateMandatory().errorResponse,
					"incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getCaseStateMandatory().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), properties.getCaseStateMandatory().errorMsg,
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: A case state (state value) must be specified"),
					true, "incorrect stacktrace");

			//change the filter map
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.remove("caseState");
			filterMap.put("caseState", "@WAITING FOR APPROVAL&");
			filterMap.put("modificationTimestamp", EMPTY_VALUE);

			//purge cases in @WAITING FOR CANCELLATION& state
			response = cases.purgeCases(filterMap);

			//verify error - it appears only for modification timestamp, as default case state is passed
			Assert.assertEquals(response.getStatusCode(), properties.getModificationTimestampMandatory().errorResponse,
					"incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(jsonPath.getString("errorCode"),
					properties.getModificationTimestampMandatory().errorCode, "incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), properties.getModificationTimestampMandatory().errorMsg,
					"incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace").contains(
							"com.tibco.bpm.cdm.api.exception.ArgumentException: Modification timestamp must be specified"),
					true, "incorrect stacktrace");
		}
		finally
		{
			//undeploy application
			forceUndeploy(deploymentIdApp1);
		}
	}

}