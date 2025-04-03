package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DatabaseQueriesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CasesViewTest extends Utils
{

	private CasesFunctions			cases						= new CasesFunctions();

	static DatabaseQueriesFunction	executeStmt					= new DatabaseQueriesFunction();

	int								validStatusCode				= 200;

	private Response				response					= null;

	private JsonPath				jsonPath					= null;

	private static final String		caseTypeAccount				= "com.example.bankdatamodel.Account";

	private static final String		applicationNameAccount		= "AccountsDataModel";

	private static final String		applicationIdAccount		= "com.example.bankdatamodel";

	private static final String		applicationVersionAccount	= "1.0.0.20190417163923357";

	private String					caseRefAccount				= "";

	private String					caseRefAccountUpdated		= "";

	private String					createAccount				= "{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
			+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
			+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
			+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
			+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}";

	private String					updateAccountTerminal		= "{\"state\": \"@CANCELLED&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
			+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
			+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
			+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
			+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}";

	//create an account
	private void createAccount() throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{

		try
		{
			List<String> casedata = new ArrayList<>();
			casedata.add(createAccount);
			response = cases.createCases(caseTypeAccount, 1, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefAccount = jsonPath.getString("[0]");
		}
		catch (Exception e)
		{
			System.out.println("exception in creating account cases");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
	}

	//update an account
	private void updateAccount() throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		try
		{
			response = cases.updateSingleCase(caseRefAccount, updateAccountTerminal);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefAccountUpdated = jsonPath.getString("[0]");
		}
		catch (Exception e)
		{
			System.out.println("problem while updating cases");
			System.out.println(e.getMessage());
		}
	}

	private String timestampDifference(String timestamp1, String timestamp2) throws ParseException
	{
		String difference = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Date parse1 = dateFormat.parse(timestamp1);
		Date parse2 = dateFormat.parse(timestamp2);

		long milliseconds = parse1.getTime() - parse2.getTime();

		if (milliseconds == 0)
		{
			difference = "00:00:00";
		}
		else
		{

			difference = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(milliseconds),
					TimeUnit.MILLISECONDS.toMinutes(milliseconds)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)), // The change is in this line
					TimeUnit.MILLISECONDS.toSeconds(milliseconds)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)),
					TimeUnit.MILLISECONDS.toMillis(milliseconds)
							- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(milliseconds)));
		}

		//		int ms = (int) (milliseconds % 1000);
		//		int seconds = (int) milliseconds / 1000;
		//
		//		int hours = seconds / 3600;
		//		int minutes = (seconds % 3600) / 60;
		//		seconds = (seconds % 3600) % 60;
		//
		//		if (seconds > 9)
		//		{
		//			difference = "00:" + "00:" + seconds + "." + ms;
		//		}
		//		else if (seconds == 0)
		//		{
		//			if (ms == 0)
		//			{
		//				difference = "00:" + "00:" + "0" + seconds;
		//			}
		//			else
		//			{
		//				difference = "00:" + "00:" + "0" + seconds + "." + ms;
		//			}
		//		}
		//		if (seconds < 9 && seconds > 0)
		//		{
		//			difference = "00:" + "00:" + "0" + seconds + "." + ms;
		//		}

		return difference;
	}

	//assert view
	private void assertView(String caseReference, Boolean updated)
			throws ClassNotFoundException, SQLException, IOException, InterruptedException, ParseException
	{
		Map<String, String> resultData = executeStmt.getDataFromView(host);

		String unversionedCaseRef = caseReference.substring(0, caseReference.length() - 2);

		//get the creation timestamp and modification timestamp
		response = cases.getSingleCase(caseReference, SELECT_CASES_METADATA);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		String creationTimestamp = executeStmt.getCreationTimestampForCID(host, "1");
		String creationTimestampFormatted = creationTimestamp;
		if (creationTimestampFormatted.length() == 19)
		{
			creationTimestampFormatted = creationTimestampFormatted + ".";
		}

		while (creationTimestampFormatted.length() < 23)
		{
			creationTimestampFormatted = creationTimestampFormatted + "0";
		}

		String modificationTimestamp = executeStmt.getModificationTimestampForCID(host, "1");

		String modificationTimestampFormatted = modificationTimestamp;
		if (modificationTimestampFormatted.length() == 19)
		{
			modificationTimestampFormatted = modificationTimestampFormatted + ".";
		}

		while (modificationTimestampFormatted.length() < 23)
		{
			modificationTimestampFormatted = modificationTimestampFormatted + "0";
		}

		//		//make timestamps in the same format as the values in the database
		//		creationTimestamp = creationTimestamp.substring(0, (creationTimestamp.length() - 1));
		//		creationTimestamp = creationTimestamp.replace("T", " ");
		//		modificationTimestamp = modificationTimestamp.substring(0, (modificationTimestamp.length() - 1));
		//		modificationTimestamp = modificationTimestamp.replace("T", " ");

		String difference = timestampDifference(modificationTimestampFormatted, creationTimestampFormatted);

		//assert the data with the expected values
		Assert.assertEquals(resultData.size(), 14, "number of columns should be 15");

		Assert.assertTrue(resultData.containsKey("case_identifier"), "case_identifier is not part of the view");
		Assert.assertEquals(resultData.get("case_identifier"), "1",
				"incorrect case_identifier is not part of the view");

		Assert.assertTrue(resultData.containsKey("unversioned_casereference"),
				"unversioned_casereference is not part of the view");
		Assert.assertEquals(resultData.get("unversioned_casereference"), unversionedCaseRef,
				"incorrect unversioned_casereference is not part of the view");

		Assert.assertTrue(resultData.containsKey("type"), "type is not part of the view");
		Assert.assertEquals(resultData.get("type"), caseTypeAccount, "incorrect type is not part of the view");

		Assert.assertTrue(resultData.containsKey("application_name"), "application_name is not part of the view");
		Assert.assertEquals(resultData.get("application_name"), applicationNameAccount,
				"incorrect application_name is not part of the view");

		Assert.assertTrue(resultData.containsKey("application_id"), "application_id is not part of the view");
		Assert.assertEquals(resultData.get("application_id"), applicationIdAccount,
				"incorrect application_id is not part of the view");

		Assert.assertTrue(resultData.containsKey("application_version"), "application_version is not part of the view");
		Assert.assertEquals(resultData.get("application_version"), applicationVersionAccount,
				"incorrect application_version is not part of the view");

		if (updated)
		{
			Assert.assertTrue(resultData.containsKey("casereference"), "casereference is not part of the view");
			Assert.assertEquals(resultData.get("casereference"), caseRefAccountUpdated,
					"incorrect casereference is not part of the view");

			Assert.assertTrue(resultData.containsKey("version"), "version is not part of the view");
			Assert.assertEquals(resultData.get("version"), "1", "incorrect version is not part of the view");

			Assert.assertTrue(resultData.containsKey("state"), "state is not part of the view");
			Assert.assertEquals(resultData.get("state"), "@CANCELLED&", "incorrect state is not part of the view");

			Assert.assertTrue(resultData.containsKey("casedata"), "casedata is not part of the view");
			Assert.assertEquals(resultData.get("casedata"), updateAccountTerminal,
					"incorrect casedata is not part of the view");

			Assert.assertTrue(resultData.containsKey("is_active"), "is_active is not part of the view");
			Assert.assertEquals(resultData.get("is_active"), "false", "incorrect is_active is not part of the view");

			Assert.assertNotEquals(resultData.get("creation_timestamp"),
					resultData.containsKey("modification_timestamp"),
					"after update creation and modification timestamp should not be equal");

			Assert.assertTrue(resultData.containsKey("creation_timestamp"),
					"creation_timestamp is not part of the view");
			Assert.assertEquals(resultData.get("creation_timestamp"), creationTimestamp,
					"incorrect creation_timestamp is not part of the view");

			Assert.assertTrue(resultData.containsKey("modification_timestamp"),
					"modification_timestamp is not part of the view");
			Assert.assertEquals(resultData.get("modification_timestamp"), modificationTimestamp,
					"incorrect modification_timestamp is not part of the view");

			Assert.assertTrue(resultData.containsKey("completed_case_duration"),
					"completed_case_duration is not part of the view");
			Assert.assertEquals(resultData.get("completed_case_duration"), difference,
					"incorrect completed_case_duration is not part of the view");
		}

		else
		{
			Assert.assertTrue(resultData.containsKey("casereference"), "casereference is not part of the view");
			Assert.assertEquals(resultData.get("casereference"), caseRefAccount,
					"incorrect casereference is not part of the view");

			Assert.assertTrue(resultData.containsKey("version"), "version is not part of the view");
			Assert.assertEquals(resultData.get("version"), "0", "incorrect version is not part of the view");

			Assert.assertTrue(resultData.containsKey("state"), "state is not part of the view");
			Assert.assertEquals(resultData.get("state"), "@ACTIVE&", "incorrect state is not part of the view");

			Assert.assertTrue(resultData.containsKey("casedata"), "casedata is not part of the view");
			Assert.assertEquals(resultData.get("casedata"), createAccount,
					"incorrect casedata is not part of the view");

			Assert.assertTrue(resultData.containsKey("is_active"), "is_active is not part of the view");
			Assert.assertEquals(resultData.get("is_active"), "true", "incorrect is_active is not part of the view");

			Assert.assertEquals(resultData.get("creation_timestamp"), resultData.get("modification_timestamp"),
					"before update creation and modification timestamp should be equal");

			Assert.assertTrue(resultData.containsKey("creation_timestamp"),
					"creation_timestamp is not part of the view");
			Assert.assertEquals(resultData.get("creation_timestamp"), creationTimestamp,
					"incorrect creation_timestamp is not part of the view");

			Assert.assertTrue(resultData.containsKey("modification_timestamp"),
					"modification_timestamp is not part of the view");
			Assert.assertEquals(resultData.get("modification_timestamp"), modificationTimestamp,
					"incorrect modification_timestamp is not part of the view");

			Assert.assertTrue(resultData.containsKey("completed_case_duration"),
					"completed_case_duration is not part of the view");
			Assert.assertEquals(resultData.get("completed_case_duration"), difference,
					"incorrect completed_case_duration is not part of the view");
		}
	}

	//Test to create and update a case to check the data populated in the view
	@Test(description = "Test to create and update a case to check the data populated in the view")
	public void createAndUpdateAccount() throws IOException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, PersistenceException, InternalException, InterruptedException,
			DeploymentException, ClassNotFoundException, SQLException, ParseException
	{

		BigInteger deploymentId = BigInteger.valueOf(7);

		try
		{
			deploymentId = deploy("/apps/Accounts/AccountsDataModel");

			createAccount();

			assertView(caseRefAccount, false);

			updateAccount();

			assertView(caseRefAccountUpdated, true);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}
}