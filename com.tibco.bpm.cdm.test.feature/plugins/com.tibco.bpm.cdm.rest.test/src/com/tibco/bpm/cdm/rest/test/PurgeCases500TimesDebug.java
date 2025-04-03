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

public class PurgeCases500TimesDebug extends Utils
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
		String newTimestamp = "";
		//		String tempString = "";
		//		int milliScendods = 0;
		//
		//		tempString = timestamp.substring(20, 23);
		//		milliScendods = (Integer.parseInt(tempString)) - 1;
		//		if (milliScendods < 0)
		//		{
		//			newTimestamp = timestamp.substring(0, 20) + "000" + "Z";
		//		}
		//
		//		else if (milliScendods < 10 && milliScendods >= 0)
		//		{
		//			newTimestamp = timestamp.substring(0, 20) + "00" + Integer.toString(milliScendods) + "Z";
		//		}
		//
		//		else if (milliScendods < 100 && milliScendods >= 10)
		//		{
		//			newTimestamp = timestamp.substring(0, 20) + "0" + Integer.toString(milliScendods) + "Z";
		//		}
		//
		//		else if (milliScendods >= 100)
		//		{
		//			newTimestamp = timestamp.substring(0, 20) + Integer.toString(milliScendods) + "Z";
		//		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Calendar cal = Calendar.getInstance();
		Date parse = dateFormat.parse(timestamp);
		cal.setTimeInMillis(parse.getTime() - 1);
		newTimestamp = dateFormat.format(cal.getTime());

		return newTimestamp;
	}

	//Test to purge cases after a particular modification timestamp
	@Test(description = "Test to purge cases after a particular modification timestamp")
	public void purgeParticularModificationTS()
			throws IOException, InterruptedException, PersistenceException, DeploymentException, InternalException,
			ArgumentException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ClassNotFoundException, SQLException, ParseException, CasedataException, ReferenceException
	{
		int j = 0;
		String errorMessage = "Has not failed";
		for (int i = 0; i < 500; i++)
		{

			//undeploy application
			forceUndeploy(deploymentIdApp1);

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create 50 cases in "@ACTIVE&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(3000, 50, "@ACTIVE&");
			//create 50 cases in "@CANCELLED&"
			accountCreator.createAccountSpecificStateOffsetDefaultCases(4000, 50, "@CANCELLED&");

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

			//get new modification timestamp of 50th case - ACTIVE
			response = cases.getCases("EQ", filterMap, "", "", toptoUpdate, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);
			modificationTimestampLastCase = obj.get(0).getMetadata().getModificationTimestamp();
			modificationTimestampIntermediate = reduceTimestampBy1ms(modificationTimestampLastCase);
			System.out.println("modification timestamp of intermediate = " + modificationTimestampIntermediate);

			//add modification timestamp to filter map - 1 ms reduced
			filterMap.put("modificationTimestamp", modificationTimestampIntermediate);

			//purge cases in @ACTIVE& state
			response = cases.purgeCases(filterMap);
			//Thread.sleep(CASES_TO_PURGE);
			if (response.getStatusCode() == 400)
			{
				System.out.println(
						"============================ The response for failing output is ===========================================");
				System.out.println(response.asString()); // useful for debugging
				System.out.println("modification timestamp of last case = " + modificationTimestampLastCase);
				System.out.println("modification timestamp of intermediate = " + modificationTimestampIntermediate);
				errorMessage = response.asString();
				j++;
			}
			else if (response.getStatusCode() == 200)
			{
				jsonPath = response.jsonPath();
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
			}
			//undeploy application
			forceUndeploy(deploymentIdApp1);
		}

		System.out.println("\nThe test has failed " + j + " times");
		System.out.println("\nThe response of the last failure was :" + errorMessage);
		System.out.println("\nThe test has passed " + (500 - j) + " times");
	}

}