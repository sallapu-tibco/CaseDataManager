package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
import com.tibco.bpm.cdm.rest.functions.CustomerCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class GetCasesTest extends Utils
{

	AccountCreatorFunction		accountCreator			= new AccountCreatorFunction();

	CustomerCreatorFunction		customerCreatorFunction	= new CustomerCreatorFunction();

	CaseTypesFunctions			caseTypes				= new CaseTypesFunctions();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	int							validStatusCode			= 200;

	int							invalidStatusCode		= 400;

	int							notFoundStatusCode		= 404;

	private JsonPath			jsonPath				= null;

	private List<String>		caseRefs				= null;

	private List<String>		caseRefsCustomer		= null;

	private static final String	caseType				= "com.example.bankdatamodel.Account";

	private static final String	applicationMajorVersion	= "1";

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger					deploymentIdApp1		= BigInteger.valueOf(5);

	BigInteger					deploymentIdApp2		= BigInteger.valueOf(6);

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

	@BeforeClass
	private void deployApplication() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException, ArgumentException,
			DataModelSerializationException, InterruptedException
	{

		forceUndeploy(deploymentIdApp1);
		forceUndeploy(deploymentIdApp2);

		deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

		//create cases for account class
		caseRefs = accountCreator.createAccounts();
		caseRefsCustomer = customerCreatorFunction.createCustomerSpecificStateOffsetDefaultCases(1, "");

	}

	@AfterClass
	private void undeployApplication() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException
	{
		forceUndeploy(deploymentIdApp1);
		forceUndeploy(deploymentIdApp2);
	}

	private String alterCaseReference(String caseReference)
	{
		String tempString = "";
		tempString = caseReference.concat("2");
		return tempString;
	}

	//Test to select cases in an application with different select filters for EQ filter with CID
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases in an application with different select filters for EQ filter with CID")
	public void getCasesForFilterWithCID() throws IOException, InterruptedException
	{

		System.out.println(caseRefs.get(0));
		String top = "10";
		//String casedataToMatch = "{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"SAVING\", \"institutionDetails\": {\"institutionCode\": \"débâcle!123\", \"nameoftheInstitution\": \"ORACLE\", \"institutionBranchAddress\": {\"city\": \"SWINDON\", \"county\": \"WILTSHIRE\", \"country\": \"ENGLAND\", \"postCode\": \"SN1 5JU\", \"firstLine\": \"ORACLE funds\", \"secondLine\": \"9, CURTIS STREET\"}}, \"dateofAccountClosing\": \"2001-12-24\", \"dateofAccountOpening\": \"1990-04-26\", \"timeofAccountClosing\": \"22:22:00\", \"timeofAccountOpening\": \"10:10:00\", \"accountLiabilityHolding\": 2}";
		String casedataToMatch = "{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"SAVING\", \"institutionDetails\": {\"institutionCode\": \"dÃ©bÃ¢cle!123\", \"nameoftheInstitution\": \"ORACLE\", \"institutionBranchAddress\": {\"city\": \"SWINDON\", \"county\": \"WILTSHIRE\", \"country\": \"ENGLAND\", \"postCode\": \"SN1 5JU\", \"firstLine\": \"ORACLE funds\", \"secondLine\": \"9, CURTIS STREET\"}}, \"dateofAccountClosing\": \"2001-12-24\", \"dateofAccountOpening\": \"1990-04-26\", \"timeofAccountClosing\": \"22:22:00\", \"timeofAccountOpening\": \"10:10:00\", \"accountLiabilityHolding\": 2}";
		String summaryToMatch = "{\"accountID\":1,\"accountType\":\"SAVING\",\"dateofAccountOpening\":\"1990-04-26\",\"timeofAccountOpening\":\"10:10:00\",\"state\":\"@ACTIVE&\"}";
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("cid", "1");

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("EQ", filterMap, selectArray[selectIterator], "", top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
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
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(0).toString(),
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

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
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
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(0).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
						"summary does not match");
			}
		}
	}

	//Test to select cases in an application with different select filters for EQ filter with State
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases in an application with different select filters for EQ filter with State")
	public void getCasesForFilterWithState() throws IOException, InterruptedException
	{

		System.out.println(caseRefs.get(0));
		String top = "10";
		String skip = "";
		String casedataToMatch = "{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 5, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"B#^^#!21\", \"nameoftheInstitution\": \"BURBERY\", \"institutionBranchAddress\": {\"city\": \"READING\", \"county\": \"KENT\", \"country\": \"GREAT BRITAIN\", \"postCode\": \"AA65 7AJ\", \"firstLine\": \"BURBERY HQ\", \"secondLine\": \"20, LONDON STREET\"}}, \"dateofAccountClosing\": \"1992-01-07\", \"dateofAccountOpening\": \"1999-10-22\", \"timeofAccountClosing\": \"14:58:00\", \"timeofAccountOpening\": \"02:58:00\", \"accountLiabilityHolding\": 6}";
		String summaryToMatch = "{\"accountID\":5,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"1999-10-22\",\"timeofAccountOpening\":\"02:58:00\",\"state\":\"@WAITING FOR APPROVAL&\"}";
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("caseState", "@WAITING FOR APPROVAL&");

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("EQ", filterMap, selectArray[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
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
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
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

			response = cases.getCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
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
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
						"summary does not match");
			}
		}
	}

	//Test to select cases in an application with different select filters for IN filter for one case
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases in an application with different select filters for IN filter for one case")
	public void getCasesForFilterINSingleCase() throws IOException, InterruptedException
	{

		//skip and top filters are not used in the in request

		//passing 1 case references
		String caseRefIn = "('" + caseRefs.get(3) + "')";

		String casedataToMatchFirst = "{\"state\": \"@WAITING FOR CANCELLATION&\", \"accountID\": 7, \"accountType\": \"FIXEDDEPOSIT\", \"institutionDetails\": {\"institutionCode\": \"Panos+123\", \"nameoftheInstitution\": \"PANOSANIC\", \"institutionBranchAddress\": {\"city\": \"BRISTOL\", \"county\": \"GLOUCESTER\", \"country\": \"UK\", \"postCode\": \"CA10 4DD\", \"firstLine\": \"The PANOSANIC\", \"secondLine\": \"12, BRISTOL AVANUE\"}}, \"dateofAccountClosing\": \"1993-05-26\", \"dateofAccountOpening\": \"1998-09-21\", \"timeofAccountClosing\": \"09:21:00\", \"timeofAccountOpening\": \"21:21:00\", \"accountLiabilityHolding\": 8}";
		String summaryToMatchFirst = "{\"accountID\":7,\"accountType\":\"FIXEDDEPOSIT\",\"dateofAccountOpening\":\"1998-09-21\",\"timeofAccountOpening\":\"21:21:00\",\"state\":\"@WAITING FOR CANCELLATION&\"}";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("IN", filterMap, selectArray[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("casedata") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchFirst),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchFirst),
						"summary does not match");
			}

		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("c") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchFirst),
						"casedata does not match");
			}
			if (aspectNames.contains("m") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchFirst),
						"summary does not match");
			}
		}
	}

	//Test to select cases of different types in an application with different select filters for IN filter for one case
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases of different types in an application with different select filters for IN filter for one case")
	public void getCasesOfDifferentTypesForFilterIN() throws IOException, InterruptedException
	{

		//skip and top filters are not used in the in request

		//passing 1 case references
		String caseRefIn = "('" + caseRefs.get(3) + "','" + caseRefsCustomer.get(0) + "')";

		String casedataToMatchFirst = "{\"state\": \"@WAITING FOR CANCELLATION&\", \"accountID\": 7, \"accountType\": \"FIXEDDEPOSIT\", \"institutionDetails\": {\"institutionCode\": \"Panos+123\", \"nameoftheInstitution\": \"PANOSANIC\", \"institutionBranchAddress\": {\"city\": \"BRISTOL\", \"county\": \"GLOUCESTER\", \"country\": \"UK\", \"postCode\": \"CA10 4DD\", \"firstLine\": \"The PANOSANIC\", \"secondLine\": \"12, BRISTOL AVANUE\"}}, \"dateofAccountClosing\": \"1993-05-26\", \"dateofAccountOpening\": \"1998-09-21\", \"timeofAccountClosing\": \"09:21:00\", \"timeofAccountOpening\": \"21:21:00\", \"accountLiabilityHolding\": 8}";
		String summaryToMatchFirst = "{\"accountID\":7,\"accountType\":\"FIXEDDEPOSIT\",\"dateofAccountOpening\":\"1998-09-21\",\"timeofAccountOpening\":\"21:21:00\",\"state\":\"@WAITING FOR CANCELLATION&\"}";
		String casedataToMatchSecond = "{\"state\": \"TRIAL\", \"customerCID\": \"WINTERFELL-00001\", \"personalDetails\": {\"age\": 25, \"gender\": \"FEMALE\", \"lastName\": \"Stark\", \"firstName\": \"Sansa\", \"salutation\": \"LADY\", \"dateofBirth\": \"1992-11-01\", \"homeAddress\": {\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", \"secondLine\": \"Sadar-Bazar\"}, \"numberofYearsStayingattheAddress\": 1.0}, \"professionalDetails\": [{\"dateofJoining\": \"2017-12-31\", \"typeofEmployement\": \"Self-Employed\", \"annualIncomeSalaryBeforeTaxes\": 1234.56}]}";
		String summaryToMatchSecond = "{\"customerCID\":\"WINTERFELL-00001\",\"state\":\"TRIAL\"}";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("IN", filterMap, selectArray[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("casedata") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchFirst),
						"casedata does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchSecond),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefsCustomer.get(0).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchFirst),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchSecond),
						"summary does not match");
			}

		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("c") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchFirst),
						"casedata does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchSecond),
						"casedata does not match");

			}
			if (aspectNames.contains("m") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefsCustomer.get(0).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchFirst),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchSecond),
						"summary does not match");
			}
		}
	}

	//Test to select cases in an application with different select filters for IN filter for two distinct cases
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases in an application with different select filters for IN filter for two cases")
	public void getCasesForFilterINTwoDistintCases() throws IOException, InterruptedException
	{

		//skip and top filters are not used in the in request

		String casedataToMatchFirst = "{\"state\": \"@WAITING FOR CANCELLATION&\", \"accountID\": 7, \"accountType\": \"FIXEDDEPOSIT\", \"institutionDetails\": {\"institutionCode\": \"Panos+123\", \"nameoftheInstitution\": \"PANOSANIC\", \"institutionBranchAddress\": {\"city\": \"BRISTOL\", \"county\": \"GLOUCESTER\", \"country\": \"UK\", \"postCode\": \"CA10 4DD\", \"firstLine\": \"The PANOSANIC\", \"secondLine\": \"12, BRISTOL AVANUE\"}}, \"dateofAccountClosing\": \"1993-05-26\", \"dateofAccountOpening\": \"1998-09-21\", \"timeofAccountClosing\": \"09:21:00\", \"timeofAccountOpening\": \"21:21:00\", \"accountLiabilityHolding\": 8}";
		String summaryToMatchFirst = "{\"accountID\":7,\"accountType\":\"FIXEDDEPOSIT\",\"dateofAccountOpening\":\"1998-09-21\",\"timeofAccountOpening\":\"21:21:00\",\"state\":\"@WAITING FOR CANCELLATION&\"}";
		String casedataToMatchSecond = "{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 5, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"B#^^#!21\", \"nameoftheInstitution\": \"BURBERY\", \"institutionBranchAddress\": {\"city\": \"READING\", \"county\": \"KENT\", \"country\": \"GREAT BRITAIN\", \"postCode\": \"AA65 7AJ\", \"firstLine\": \"BURBERY HQ\", \"secondLine\": \"20, LONDON STREET\"}}, \"dateofAccountClosing\": \"1992-01-07\", \"dateofAccountOpening\": \"1999-10-22\", \"timeofAccountClosing\": \"14:58:00\", \"timeofAccountOpening\": \"02:58:00\", \"accountLiabilityHolding\": 6}";
		String summaryToMatchSecond = "{\"accountID\":5,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"1999-10-22\",\"timeofAccountOpening\":\"02:58:00\",\"state\":\"@WAITING FOR APPROVAL&\"}";

		//skip and top filters are not used in the in request
		//passing 2 case references
		String caseRefIn = "('" + caseRefs.get(2) + "','" + caseRefs.get(3) + "')";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("IN", filterMap, selectArray[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("casedata") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchFirst),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchSecond),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchFirst),
						"summary does not match");
			}

		}

		for (

				int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("c") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchFirst),
						"casedata does not match");

			}
			if (aspectNames.contains("m") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchSecond),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchFirst),
						"summary does not match");
			}
		}
	}

	//Test to select cases in an application after update and for an unreasonably modified case reference
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select cases in an application after update and for an unreasonably modified case reference")
	public void getCasesForFilterINAfterCaseUpdate() throws IOException, InterruptedException
	{

		String tempCasedata = "";
		String tempCaseSummary = "";

		String casedataToMatchSecond = "{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 5, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"B#^^#!21\", \"nameoftheInstitution\": \"BURBERY\", \"institutionBranchAddress\": {\"city\": \"READING\", \"county\": \"KENT\", \"country\": \"GREAT BRITAIN\", \"postCode\": \"AA65 7AJ\", \"firstLine\": \"BURBERY HQ\", \"secondLine\": \"20, LONDON STREET\"}}, \"dateofAccountClosing\": \"1992-01-07\", \"dateofAccountOpening\": \"1999-10-22\", \"timeofAccountClosing\": \"14:58:00\", \"timeofAccountOpening\": \"02:58:00\", \"accountLiabilityHolding\": 6}";
		String summaryToMatchSecond = "{\"accountID\":5,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"1999-10-22\",\"timeofAccountOpening\":\"02:58:00\",\"state\":\"@WAITING FOR APPROVAL&\"}";

		//passing skip and top filters but they are discarded
		String skip = "";
		String top = "";

		//get a case
		response = cases.getSingleCase(caseRefs.get(4), SELECT_CASES_C_S);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		tempCasedata = jsonPath.getString("casedata");
		tempCaseSummary = jsonPath.getString("summary");

		//update case - change the state
		tempCasedata = tempCasedata.replace("@REINSTATED&", "@TREMPORARILY DEACTIVATED&");
		tempCaseSummary = tempCaseSummary.replace("@REINSTATED&", "@TREMPORARILY DEACTIVATED&");
		response = cases.updateSingleCase(caseRefs.get(4), tempCasedata);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		//get a case to verify that the case reference modification
		Map<String, String> filterMapFetchCase = new HashMap<String, String>();
		filterMapFetchCase.put("caseType", caseType);
		filterMapFetchCase.put("applicationMajorVersion", applicationMajorVersion);
		filterMapFetchCase.put("caseState", "@TREMPORARILY DEACTIVATED&");
		String topToUpdate = "1";
		response = cases.getCases("EQ", filterMapFetchCase, SELECT_CASES_CASEREFERENCE, "", topToUpdate, "");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 1);
		String caseRefAfterUpdate = obj.get(0).getCaseReference();

		//alter case reference for the test case
		String caseRefeUnreasonablyModified = alterCaseReference(caseRefAfterUpdate);

		//passing 2 duplicate case references 1 unique case reference 
		String caseRefIn = "('" + caseRefs.get(4) + "','" + caseRefAfterUpdate + "','" + caseRefs.get(2) + "','"
				+ caseRefeUnreasonablyModified + "')";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArray[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 4);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("casedata") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), tempCasedata),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), tempCasedata),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[3].casedata"), tempCasedata),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[3].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[3].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), tempCaseSummary),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), tempCaseSummary),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryToMatchSecond),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[3].summary"), tempCaseSummary),
						"summary does not match");
			}
		}

		for (

				int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 4);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("c") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), tempCasedata),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), tempCasedata),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[3].casedata"), tempCasedata),
						"casedata does not match");
			}
			if (aspectNames.contains("m") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[3].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[3].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[3].caseReference"), caseRefAfterUpdate,
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), tempCaseSummary),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), tempCaseSummary),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryToMatchSecond),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[3].summary"), tempCaseSummary),
						"summary does not match");
			}
		}
	}

	//Test to select case in an application with different select filters for IN filter for two duplicate one unique cases
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to select types in an application with different select filters for IN filter for two duplicate one unique cases")
	public void getCasesForFilterINTwoDuplicateOneUniqueCases() throws IOException, InterruptedException
	{

		String casedataToMatchFirst = "{\"state\": \"@WAITING FOR CANCELLATION&\", \"accountID\": 7, \"accountType\": \"FIXEDDEPOSIT\", \"institutionDetails\": {\"institutionCode\": \"Panos+123\", \"nameoftheInstitution\": \"PANOSANIC\", \"institutionBranchAddress\": {\"city\": \"BRISTOL\", \"county\": \"GLOUCESTER\", \"country\": \"UK\", \"postCode\": \"CA10 4DD\", \"firstLine\": \"The PANOSANIC\", \"secondLine\": \"12, BRISTOL AVANUE\"}}, \"dateofAccountClosing\": \"1993-05-26\", \"dateofAccountOpening\": \"1998-09-21\", \"timeofAccountClosing\": \"09:21:00\", \"timeofAccountOpening\": \"21:21:00\", \"accountLiabilityHolding\": 8}";
		String summaryToMatchFirst = "{\"accountID\":7,\"accountType\":\"FIXEDDEPOSIT\",\"dateofAccountOpening\":\"1998-09-21\",\"timeofAccountOpening\":\"21:21:00\",\"state\":\"@WAITING FOR CANCELLATION&\"}";
		String casedataToMatchSecond = "{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 5, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"B#^^#!21\", \"nameoftheInstitution\": \"BURBERY\", \"institutionBranchAddress\": {\"city\": \"READING\", \"county\": \"KENT\", \"country\": \"GREAT BRITAIN\", \"postCode\": \"AA65 7AJ\", \"firstLine\": \"BURBERY HQ\", \"secondLine\": \"20, LONDON STREET\"}}, \"dateofAccountClosing\": \"1992-01-07\", \"dateofAccountOpening\": \"1999-10-22\", \"timeofAccountClosing\": \"14:58:00\", \"timeofAccountOpening\": \"02:58:00\", \"accountLiabilityHolding\": 6}";
		String summaryToMatchSecond = "{\"accountID\":5,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"1999-10-22\",\"timeofAccountOpening\":\"02:58:00\",\"state\":\"@WAITING FOR APPROVAL&\"}";

		//passing skip and top filters but they are discarded
		String skip = "";
		String top = "";

		//passing 2 duplicate case references 1 unique case reference 
		String caseRefIn = "('" + caseRefs.get(2) + "','" + caseRefs.get(3) + "','" + caseRefs.get(2) + "')";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (

				int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("IN", filterMap, selectArray[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 3);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("casedata") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchFirst),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), casedataToMatchSecond),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchSecond),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchFirst),
						"summary does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryToMatchSecond),
						"summary does not match");
			}
		}

		for (

				int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], skip, top, "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 3);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			//first select is null and does not contain any explicit select filter 
			if (aspectNames.contains("c") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchSecond),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatchFirst),
						"casedata does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), casedataToMatchSecond),
						"casedata does not match");
			}
			if (aspectNames.contains("m") || selectIterator == 0)
			{
				Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
						"metadata should be tagged");

				Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
						"metadata should be tagged");
			}
			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefs.get(3).toString(),
						"caseRef should be tagged");

				Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseRefs.get(2).toString(),
						"caseRef should be tagged");
			}
			if (aspectNames.contains("s") || selectIterator == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchSecond),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatchFirst),
						"summary does not match");

				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryToMatchSecond),
						"summary does not match");
			}
		}
	}

	//Test to verify error response on passing a non-existing case reference
	@Test(groups = {
			"SmallNumberOfCases"}, description = "Test to verify error response on passing a non-existing case reference")
	public void getCasesForFilterINErrorNonExistingReference() throws IOException, InterruptedException
	{
		String errorMessage = "";
		String nonExistingCaseReference = "1234456-com.example.bankdatamodel.Account-2-1";

		//passing 3 unique, 1 duplicate and 1 non-existing case references
		String caseRefIn = "('" + caseRefs.get(0) + "','" + caseRefs.get(1) + "','" + caseRefs.get(2) + "','"
				+ caseRefs.get(3) + "','" + caseRefs.get(3) + "','" + nonExistingCaseReference + "','" + caseRefs.get(4)
				+ "')";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArray[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), properties.getReferenceDoesNotExist().errorResponse,
					"incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			errorMessage = properties.getReferenceDoesNotExist().errorMsg;
			errorMessage = errorMessage.replaceAll("\\{.*?\\}", nonExistingCaseReference);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getReferenceDoesNotExist().errorCode);
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
			Assert.assertEquals(response.getStatusCode(), properties.getReferenceDoesNotExist().errorResponse);

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(jsonPath.getString("stackTrace")
					.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Case does not exist: "
							+ nonExistingCaseReference),
					true, "incorrect stacktrace");

			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), nonExistingCaseReference);
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), properties.getReferenceDoesNotExist().errorResponse,
					"incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			errorMessage = properties.getReferenceDoesNotExist().errorMsg;
			errorMessage = errorMessage.replaceAll("\\{.*?\\}", nonExistingCaseReference);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getReferenceDoesNotExist().errorCode);
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
			Assert.assertEquals(response.getStatusCode(), properties.getReferenceDoesNotExist().errorResponse);

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(jsonPath.getString("stackTrace")
					.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Case does not exist: "
							+ nonExistingCaseReference),
					true, "incorrect stacktrace");

			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), nonExistingCaseReference);
		}
	}

	//Test to select case in an application with different select filters for IN filter for more than 200 cases
	@Test(dependsOnGroups = {"SmallNumberOfCases"})
	public void getCasesForFilterINLargeNumberOfCases() throws IOException, InterruptedException, DeploymentException,
			PersistenceException, InternalException, URISyntaxException, RuntimeApplicationException, CasedataException,
			ReferenceException, ArgumentException, DataModelSerializationException
	{

		//undeploy the application
		forceUndeploy(deploymentIdApp1);

		//deploy v2 as a new application
		deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

		//create cases for account class
		caseRefs.clear();
		List<String> tempRefs = null;

		tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(1000, 50, "@UNFINISHED CASES PURGE&");
		caseRefs.addAll(tempRefs);
		tempRefs.clear();
		tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(2000, 100, "dräçÅrûß");
		caseRefs.addAll(tempRefs);
		tempRefs.clear();

		//Thread.sleep(CASES_TO_UPDATE);

		String caseRefIn = "(";
		for (int casesIterator = 0; casesIterator < caseRefs.size(); casesIterator++)
		{
			if (casesIterator < (caseRefs.size() - 1))
			{
				caseRefIn = caseRefIn + "'" + caseRefs.get(casesIterator) + "',";
			}
			else if (casesIterator == (caseRefs.size() - 1))
			{
				caseRefIn = caseRefIn + "'" + caseRefs.get(casesIterator) + "'";
			}
		}
		caseRefIn = caseRefIn + ")";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		for (

				int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{

			response = cases.getCases("IN", filterMap, selectArray[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 150);

			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

			if (aspectNames.contains("caseReference") || selectIterator == 0)
			{
				for (int caseIterator = 0; caseIterator < caseRefs.size(); caseIterator++)
				{
					Assert.assertEquals(jsonPath.getString("[" + caseIterator + "].caseReference"),
							caseRefs.get(caseIterator).toString(), "caseRef should be tagged");
				}
			}

		}

		for (

				int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], "", "", "");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator]); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 150);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

			if (aspectNames.contains("cr") || selectIterator == 0)
			{
				for (int caseIterator = 0; caseIterator < caseRefs.size(); caseIterator++)
				{
					Assert.assertEquals(jsonPath.getString("[" + caseIterator + "].caseReference"),
							caseRefs.get(caseIterator).toString(), "caseRef should be tagged");
				}
			}
		}

		forceUndeploy(deploymentIdApp1);
		forceUndeploy(deploymentIdApp2);

	}

}
