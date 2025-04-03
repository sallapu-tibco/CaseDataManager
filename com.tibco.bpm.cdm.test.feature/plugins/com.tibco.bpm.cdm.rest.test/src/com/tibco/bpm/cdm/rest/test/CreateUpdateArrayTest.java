package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CreateUpdateArrayTest extends Utils
{

	AccountCreatorFunction		accountCreator			= new AccountCreatorFunction();

	CaseTypesFunctions			caseTypes				= new CaseTypesFunctions();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	int							validStatusCode			= 200;

	int							invalidStatusCode		= 400;

	int							notFoundStatusCode		= 404;

	private JsonPath			jsonPath				= null;

	private List<String>		caseRefs				= null;

	private List<String>		caseRefsUpdated			= null;

	private static final String	caseType				= "com.example.bankdatamodel.Account";

	private static final String	applicationMajorVersion	= "1";

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger					deploymentIdApp1		= BigInteger.valueOf(1);

	BigInteger					deploymentIdSBOM		= BigInteger.valueOf(2);

	private Response			response				= null;

	private String[]			selectArrayAbbreviated	= {SELECT_CASES, SELECT_CASES_C, SELECT_CASES_S,
			SELECT_CASES_CR, SELECT_CASES_M, SELECT_CASES_C_S, SELECT_CASES_C_CR, SELECT_CASES_C_M, SELECT_CASES_S_CR,
			SELECT_CASES_S_M, SELECT_CASES_CR_M, SELECT_CASES_C_S_CR, SELECT_CASES_C_S_M, SELECT_CASES_C_CR_M,
			SELECT_CASES_S_CR_M, SELECT_CASES_C_CR_M_S};

	private String[]			selectArray				= {SELECT_CASES, SELECT_CASES_CASEDATA, SELECT_CASES_SUMMARY,
			SELECT_CASES_CASEREFERENCE, SELECT_CASES_METADATA, SELECT_CASES_CASEDATA_SUMMARY,
			SELECT_CASES_CASEDATA_CASEREFERENCE, SELECT_CASES_CASEDATA_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE,
			SELECT_CASES_SUMMARY_METADATA, SELECT_CASES_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, SELECT_CASES_CASEDATA_SUMMARY_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY};

	private void assertCaseReferenceLists(List<String> caseRefList1, List<String> caseRefList2, Integer numberOfUpdates)
	{

		Assert.assertEquals(caseRefList1.size(), caseRefList2.size(), "different size of the case references list");

		//compare that the elements of the list are not exactly the same
		Assert.assertFalse(caseRefList1.equals(caseRefList2), "case references are not same");

		//compare that every case reference is just an update of the original case reference
		for (int i = 0; i < caseRefList1.size(); i++)
		{
			String caseRef1 = caseRefList1.get(i);
			String caseRef2 = caseRefList2.get(i);

			System.out.println("case references for assertion " + caseRef1 + " & " + caseRef2);
			System.out.println("values in the loop " + i); // useful for debugging

			//the case references are not exact match
			Assert.assertNotEquals(caseRef1, caseRef2, "case references are same");

			if (numberOfUpdates < 10)
			{
				//will they of equal length always? nope- for the 1000 updates case :-)
				Assert.assertEquals(caseRef1.length(), caseRef2.length(), "case references are not of equal lengths");

				//assert substrings
				Assert.assertEquals(caseRef1.substring(0, (caseRef1.length() - 1)),
						caseRef2.substring(0, (caseRef2.length() - 1)),
						"case references matching except for the last digit");
				System.out.println(
						"case reference " + i + " substring " + caseRef1.substring(0, (caseRef1.length() - 1)));
				System.out.println(
						"case reference " + i + " substring " + caseRef2.substring(0, (caseRef2.length() - 1)));
			}

			//direct correlation with the number of updates
			String intermediateString = "";
			intermediateString = caseRef1.substring(0, caseRef1.length() - 1) + numberOfUpdates.toString();
			Assert.assertEquals(caseRef2, intermediateString,
					"updated case reference does not follow proper convention");
		}
	}

	//Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter 
	@Test(description = "Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter")
	public void singleCaseUpdate1000TimesGetWithModificationTS() throws IOException, InterruptedException,
			PersistenceException, DeploymentException, InternalException, CasedataException, ReferenceException,
			ArgumentException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			caseRefs = accountCreator.createAccountsArrayData(1, "accounts");
			//Thread.sleep(CASES_TO_UPDATE);

			//create select filters
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("modificationTimestamp", "2100-01-01T23:58:59.008Z");
			String top = "1";
			String modiTS = "";

			String casedataToMatch = "{\"state\": \"@REINSTATED&\", \"accountID\": 1, \"accountType\": \"INSURANCE\", "
					+ "\"institutionDetails\": {\"institutionCode\": \"institution update 1000 code 0\", \"nameoftheInstitution\": \"instituion update 1000 code 0\", "
					+ "\"institutionBranchAddress\": {\"city\": \"update 1000 city 0\", \"county\": \"update 1000 county 0\", \"country\": \"update 1000 country 0\", "
					+ "\"postCode\": \"update 1000 postcode 0\", \"firstLine\": \"update 1000 first line 0\", "
					+ "\"secondLine\": \"second line 1000 second line 0\"}}, \"dateofAccountClosing\": \"2019-12-11\", \"dateofAccountOpening\": \"2011-01-21\", "
					+ "\"timeofAccountClosing\": \"22:00:00\", \"timeofAccountOpening\": \"10:00:00\", "
					+ "\"accountLiabilityHolding\": 1}";
			String summaryToMatch = "{\"accountID\":1,\"accountType\":\"INSURANCE\",\"dateofAccountOpening\":\"2011-01-21\",\"timeofAccountOpening\":\"10:00:00\",\"state\":\"@REINSTATED&\"}";

			//get the modification timestamp of the case
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_M, "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			modiTS = jsonPath.getString("metadata.modificationTimestamp");

			caseRefsUpdated = accountCreator.updateAccountsArrayData(caseRefs, 1000, "accounts");

			assertCaseReferenceLists(caseRefs, caseRefsUpdated, 1000);

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{

				response = cases.getCases("EQ", filterMap, selectArray[selectIterator], "", top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatch),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotEquals(jsonPath.getString("metadata.modifiedBy"), modiTS,
							"modification timestamp should not be equal to that of the original case");

				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsUpdated.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{

				response = cases.getCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], "", top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatch),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotEquals(jsonPath.getString("metadata.modifiedBy"), modiTS,
							"modification timestamp should not be equal to that of the original case");

				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsUpdated.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
							"summary does not match");
				}
			}
		}
		finally
		{
			//undeploy the application
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter 
	@Test(description = "Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter")
	public void bulkCaseUpdate100TimesGetWithModificationTS() throws IOException, InterruptedException,
			PersistenceException, DeploymentException, InternalException, CasedataException, ReferenceException,
			ArgumentException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			caseRefs = accountCreator.createAccountsArrayData(100, "accounts");
			//Thread.sleep(CASES_TO_UPDATE);

			//create select filters
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("modificationTimestamp", "2100-01-01T23:58:59.008Z");
			String top = "101";

			//get the modification timestamp of the case
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_M, "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			caseRefsUpdated = accountCreator.updateAccountsArrayData(caseRefs, 1, "accounts");
			//Thread.sleep(CASES_TO_UPDATE);

			assertCaseReferenceLists(caseRefs, caseRefsUpdated, 1);

		}
		finally
		{
			//undeploy the application
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter 
	@Test(description = "Test to update multiple times in bulk using array of case data and get case request with modification timestamp filter")
	public void bulkCaseUpdate10TimesGetWithModificationTS() throws IOException, InterruptedException,
			PersistenceException, DeploymentException, InternalException, CasedataException, ReferenceException,
			ArgumentException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		try
		{
			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			caseRefs = accountCreator.createAccountsArrayData(10, "accounts");
			//Thread.sleep(CASES_TO_UPDATE);

			//create select filters
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("modificationTimestamp", "2100-01-01T23:58:59.008Z");
			String top = "11";

			//get the modification timestamp of the case
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_M, "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			caseRefsUpdated = accountCreator.updateAccountsArrayData(caseRefs, 10, "accounts");
			//Thread.sleep(CASES_TO_UPDATE);

			assertCaseReferenceLists(caseRefs, caseRefsUpdated, 10);

			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_CR_M_S, "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 10);
		}
		finally
		{
			//undeploy the application
			forceUndeploy(deploymentIdApp1);
		}
	}

}
