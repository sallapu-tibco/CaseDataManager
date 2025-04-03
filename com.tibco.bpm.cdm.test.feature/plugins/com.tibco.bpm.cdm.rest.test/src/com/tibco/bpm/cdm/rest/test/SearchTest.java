package com.tibco.bpm.cdm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CaseInfo;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.AccountCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.test.util.FileUtils.DataModelModifier;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class SearchTest extends Utils
{

	AccountCreatorFunction		accountCreator						= new AccountCreatorFunction();

	CaseTypesFunctions			caseTypes							= new CaseTypesFunctions();

	CasesFunctions				cases								= new CasesFunctions();

	GetPropertiesFunction		properties							= new GetPropertiesFunction();

	int							validStatusCode						= 200;

	int							invalidStatusCode					= 400;

	int							notFoundStatusCode					= 404;

	private JsonPath			jsonPath							= null;

	private List<String>		caseRefsV1							= new ArrayList<>();

	private List<String>		caseRefsV2							= new ArrayList<>();

	private List<String>		updatedCaseRefsV1;

	private static final String	caseType							= "com.example.bankdatamodel.Account";

	private static final String	applicationMajorVersion				= "1";

	private static final String	applicationMajorVersionLargeText	= "2";

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger					deploymentIdApp1					= BigInteger.valueOf(7);

	BigInteger					deploymentIdApp2					= BigInteger.valueOf(8);

	BigInteger					deploymentIdApp3					= BigInteger.valueOf(9);

	BigInteger					deploymentIdApp4					= BigInteger.valueOf(10);

	private Response			response							= null;

	private String[]			selectArrayAbbreviated				= {SELECT_CASES, SELECT_CASES_C, SELECT_CASES_S,
			SELECT_CASES_CR, SELECT_CASES_M, SELECT_CASES_C_S, SELECT_CASES_C_CR, SELECT_CASES_C_M, SELECT_CASES_S_CR,
			SELECT_CASES_S_M, SELECT_CASES_CR_M, SELECT_CASES_C_S_CR, SELECT_CASES_C_S_M, SELECT_CASES_C_CR_M,
			SELECT_CASES_S_CR_M, SELECT_CASES_C_CR_M_S};

	private String[]			selectArray							= {SELECT_CASES, SELECT_CASES_CASEDATA,
			SELECT_CASES_SUMMARY, SELECT_CASES_CASEREFERENCE, SELECT_CASES_METADATA, SELECT_CASES_CASEDATA_SUMMARY,
			SELECT_CASES_CASEDATA_CASEREFERENCE, SELECT_CASES_CASEDATA_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE,
			SELECT_CASES_SUMMARY_METADATA, SELECT_CASES_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, SELECT_CASES_CASEDATA_SUMMARY_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY};

	private void assertCaseInfo(CaseInfo info, List<String> aspectNames, String casedataToMatch, String summaryToMatch,
			Integer caseRefNumber) throws IOException
	{
		if (aspectNames.contains("casedata") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(assertUnsortedData(info.getCasedata(), casedataToMatch), "casedata does not match");
		}
		if (aspectNames.contains("metadata") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertNotNull(info.getMetadata(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getCreationTimestamp(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getModificationTimestamp(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getCreatedBy(), "metadata should be tagged");
		}
		if (aspectNames.contains("caseReference") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getCaseReference(), caseRefsV1.get(caseRefNumber).toString(),
					"caseRef should be tagged");
		}
		if (aspectNames.contains("summary") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(assertUnsortedData(info.getSummary(), summaryToMatch), "summary does not match");
		}
	}

	private void assertCaseInfo(CaseInfo info, List<String> aspectNames, String casedataToMatch, String summaryToMatch,
			List<String> caseReference, Integer caseRefNumber) throws IOException
	{
		if (aspectNames.contains("casedata") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(assertUnsortedData(info.getCasedata(), casedataToMatch), "casedata does not match");
		}
		if (aspectNames.contains("metadata") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertNotNull(info.getMetadata(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getCreationTimestamp(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getModificationTimestamp(), "metadata should be tagged");
			Assert.assertNotNull(info.getMetadata().getCreatedBy(), "metadata should be tagged");
		}
		if (aspectNames.contains("caseReference") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getCaseReference(), caseReference.get(caseRefNumber).toString(),
					"caseRef should be tagged");
		}
		if (aspectNames.contains("summary") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(assertUnsortedData(info.getSummary(), summaryToMatch), "summary does not match");
		}
	}

	private void assertEmptyResponse() throws IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 0);
		Assert.assertEquals(response.asString(), "[]", "response should be empty");
	}

	private void deployWithModification(DataModelModifier modifier, String rascVersion) throws IOException,
			URISyntaxException, DataModelSerializationException, RuntimeApplicationException, InterruptedException
	{

		BigInteger deploymentIdApp = null;
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);
		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), rascVersion);

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifier);

		if (rascVersion.equals("2.0.0.1"))
		{
			deploymentIdApp = BigInteger.valueOf(9);
		}
		else
		{
			deploymentIdApp = BigInteger.valueOf(10);
		}
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));

		runtimeApplication.setID(deploymentIdApp);

		try
		{
			deploymentManager.deploy(runtimeApplication);
		}
		catch (DeploymentException | InternalException e)
		{
			System.out.println("deployment should have been successful");
			Assert.fail();
		}
	}

	//Test to deploy v1 of the application and upgrade to v2 with change in searchable attributes
	@Test(description = "Test to deploy v1 of the application and upgrade to v2 with change in searchable attributes")
	public void upgradeSearch() throws IOException, InterruptedException, PersistenceException, DeploymentException,
			InternalException, CasedataException, ReferenceException, ArgumentException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException
	{
		try
		{
			//create select filters
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			String top = "1";
			String skip = "0";
			String[] searchStringBeforeUpgrade = {"999999", "CURRENT", "2019-04-26", "17:24:45", "@ACTIVE&"};

			String casedataToMatch = "{\"state\": \"@ACTIVE&\", \"accountID\": 999999, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11}";

			String summaryToMatch = "{\"accountID\":999999,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"2019-04-26\",\"timeofAccountOpening\":\"17:24:45\",\"state\":\"@ACTIVE&\"}";

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			caseRefsV1 = accountCreator.createAccountWithDefaultValues("999999");
			//Thread.sleep(CASES_TO_UPDATE);

			for (int searchStringIterator = 0; searchStringIterator < searchStringBeforeUpgrade.length; searchStringIterator++)
			{
				for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
							searchStringBeforeUpgrade[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println(
							"values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

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
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("caseReference") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
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
					response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
							searchStringBeforeUpgrade[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + " select value = "
							+ selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 1);

					List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

					//first select is null and does not contain any explicit select filter 
					if (aspectNames.contains("c") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatch),
								"casedata does not match");
					}
					if (aspectNames.contains("m") || selectIterator == 0)
					{
						Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("cr") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
								"caseRef should be tagged");
					}
					if (aspectNames.contains("s") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
								"summary does not match");
					}
				}
			}

			String[] searchStringV1Success = {"999999", "CURRENT", "@ACTIVE&"}; //account id, type and state are still searchable
			String[] searchStringV1Failure = {"2019-04-26", "17:24:45"}; //date and time are not searchable anymore

			//dräçÅrûß
			String[] searchStringV2 = {"100000",
					"ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC",
					"dräçÅrûß", "2019-03-21T12:34:56.789Z", "1.234"}; //new attribute values for v2 that are searchable

			//dräçÅrûß
			String casedataToMatchV2 = "{\"state\": \"dräçÅrûß\", \"accountID\": 100000, \"accountType\": \"ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"portfolioWebsite\":\"https://www.myportfoliomanager.com\",\"activePortfolio\":true,\"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"activeationTimeStamp\":\"2019-03-21T12:34:56.789Z\",\"accountLiabilityHolding\": 1.234}";

			//dräçÅrûß
			String summaryToMatchV2 = "{\"accountID\":100000,\"accountType\":\"ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC\","
					+ "\"activeationTimeStamp\":\"2019-03-21T12:34:56.789Z\",\"accountLiabilityHolding\":1.234,\"state\":\"dräçÅrûß\"}";

			String casedataToMatchV1 = "{\"state\": \"@ACTIVE&\", \"accountID\": 999999, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11}";

			String summaryToMatchV1 = "{\"accountID\":999999,\"accountType\":\"CURRENT\","
					+ "\"accountLiabilityHolding\":991212.11,\"state\":\"@ACTIVE&\"}";

			//deploy the application
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//create cases for account v2
			caseRefsV2 = accountCreator.createAccountWithDefaultValues("100000");
			//Thread.sleep(CASES_TO_UPDATE);

			//Test to deploy v2 of the application, create cases and perform operations for search on new attributes and old attributes 

			for (int searchStringIterator = 0; searchStringIterator < searchStringV2.length; searchStringIterator++)
			{
				for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
							searchStringV2[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println(
							"values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 1);

					List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

					//first select is null and does not contain any explicit select filter 
					if (aspectNames.contains("casedata") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV2),
								"casedata does not match");
					}
					if (aspectNames.contains("metadata") || selectIterator == 0)
					{
						Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("caseReference") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV2.get(0).toString(),
								"caseRef should be tagged");
					}
					if (aspectNames.contains("summary") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV2),
								"summary does not match");
					}
				}

				for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
							searchStringV2[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + " select value = "
							+ selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 1);

					List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

					//first select is null and does not contain any explicit select filter 
					if (aspectNames.contains("c") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV2),
								"casedata does not match");
					}
					if (aspectNames.contains("m") || selectIterator == 0)
					{
						Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("cr") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV2.get(0).toString(),
								"caseRef should be tagged");
					}
					if (aspectNames.contains("s") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV2),
								"summary does not match");
					}
				}
			}

			for (int searchStringIterator = 0; searchStringIterator < searchStringV1Success.length; searchStringIterator++)
			{
				for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
							searchStringV1Success[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println(
							"values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 1);

					List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

					//first select is null and does not contain any explicit select filter 
					if (aspectNames.contains("casedata") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
								"casedata does not match");
					}
					if (aspectNames.contains("metadata") || selectIterator == 0)
					{
						Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("caseReference") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
								"caseRef should be tagged");
					}
					if (aspectNames.contains("summary") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
								"summary does not match");
					}
				}

				for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
							searchStringV1Success[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + " select value = "
							+ selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 1);

					List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

					//first select is null and does not contain any explicit select filter 
					if (aspectNames.contains("c") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
								"casedata does not match");
					}
					if (aspectNames.contains("m") || selectIterator == 0)
					{
						Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"),
								"metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
						Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
								"metadata should be tagged");
					}
					if (aspectNames.contains("cr") || selectIterator == 0)
					{
						Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
								"caseRef should be tagged");
					}
					if (aspectNames.contains("s") || selectIterator == 0)
					{
						Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
								"summary does not match");
					}
				}

			}

			for (int searchStringIterator = 0; searchStringIterator < searchStringV1Failure.length; searchStringIterator++)
			{
				for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
							searchStringV1Failure[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println(
							"values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 0);
				}

				for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
				{
					response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
							searchStringV1Failure[searchStringIterator], "");
					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + " select value = "
							+ selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

					CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
							CasesGetResponseBody.class);
					Assert.assertEquals(obj.size(), 0);
				}
			}
		}
		finally
		{
			caseRefsV1.clear();
			caseRefsV2.clear();
			forceUndeploy(deploymentIdApp1);
			forceUndeploy(deploymentIdApp2);
		}
	}

	//Test to findBy(criteria)
	@Test(description = "Test to findBy(criteria)")
	public void findBy() throws IOException, InterruptedException, PersistenceException, InternalException,
			CasedataException, ReferenceException, ArgumentException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, DeploymentException
	{
		try
		{
			//create select filters
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", caseType);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			String top = "5";
			String skip = "0";
			String[] searchStringBeforeUpgrade = {"@ACTIVE&", "CURRENT", "2019-04-26", "17:24:45", "999999"};

			String casedataToMatch0 = "{\"state\": \"@ACTIVE&\", \"accountID\": 999999, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": {\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", \"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", \"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11}";
			String summaryToMatch0V1 = "{\"accountID\":999999,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"2019-04-26\",\"timeofAccountOpening\":\"17:24:45\",\"state\":\"@ACTIVE&\"}";
			String summaryToMatch0V2 = "{\"accountID\":999999,\"accountType\":\"CURRENT\",\"accountLiabilityHolding\":991212.11,\"state\":\"@ACTIVE&\"}";

			String casedataToMatch6 = "{\"state\": \"@REINSTATED&\", \"accountID\": 6, \"accountType\": \"INSURANCE\", \"institutionDetails\": {\"institutionCode\": \"institution code 5\", \"nameoftheInstitution\": \"instituion name 5\", \"institutionBranchAddress\": {\"city\": \"city5\", \"county\": \"county5\", \"country\": \"country5\", \"postCode\": \"postcode 5\", \"firstLine\": \"first line 5\", \"secondLine\": \"second line5\"}}, \"dateofAccountClosing\": \"2019-12-26\", \"dateofAccountOpening\": \"2011-01-16\", \"timeofAccountClosing\": \"23:55:30\", \"timeofAccountOpening\": \"11:00:20\", \"accountLiabilityHolding\": 6}";
			String summaryToMatch6V2 = "{\"accountID\":6,\"accountType\":\"INSURANCE\",\"state\":\"@REINSTATED&\",\"accountLiabilityHolding\":6}";

			String casedataToMatch5 = "{\"state\": \"@TREMPORARILY DEACTIVATED&\", \"accountID\": 5, \"accountType\": \"CASHISA\", \"institutionDetails\": {\"institutionCode\": \"institution code 4\", \"nameoftheInstitution\": \"instituion name 4\", \"institutionBranchAddress\": {\"city\": \"city4\", \"county\": \"county4\", \"country\": \"country4\", \"postCode\": \"postcode 4\", \"firstLine\": \"first line 4\", \"secondLine\": \"second line4\"}}, \"dateofAccountClosing\": \"2019-12-25\", \"dateofAccountOpening\": \"2011-01-15\", \"timeofAccountClosing\": \"23:44:30\", \"timeofAccountOpening\": \"11:44:20\", \"accountLiabilityHolding\": 5}";
			String summaryToMatch5V2 = "{\"accountID\":5,\"accountType\":\"CASHISA\",\"state\":\"@TREMPORARILY DEACTIVATED&\",\"accountLiabilityHolding\":5}";

			String casedataToMatch4 = "{\"state\": \"@WAITING FOR CANCELLATION&\", \"accountID\": 4, \"accountType\": \"FIXEDDEPOSIT\", \"institutionDetails\": {\"institutionCode\": \"institution code 3\", \"nameoftheInstitution\": \"instituion name 3\", \"institutionBranchAddress\": {\"city\": \"city3\", \"county\": \"county3\", \"country\": \"country3\", \"postCode\": \"postcode 3\", \"firstLine\": \"first line 3\", \"secondLine\": \"second line3\"}}, \"dateofAccountClosing\": \"2019-12-24\", \"dateofAccountOpening\": \"2011-01-14\", \"timeofAccountClosing\": \"23:33:30\", \"timeofAccountOpening\": \"11:33:20\", \"accountLiabilityHolding\": 4}";
			String summaryToMatch4V2 = "{\"accountID\":4,\"accountType\":\"FIXEDDEPOSIT\",\"state\":\"@WAITING FOR CANCELLATION&\",\"accountLiabilityHolding\":4}";

			String casedataToMatch3 = "{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 3, \"accountType\": \"CURRENT\", \"institutionDetails\": {\"institutionCode\": \"institution code 2\", \"nameoftheInstitution\": \"instituion name 2\", \"institutionBranchAddress\": {\"city\": \"city2\", \"county\": \"county2\", \"country\": \"country2\", \"postCode\": \"postcode 2\", \"firstLine\": \"first line 2\", \"secondLine\": \"second line2\"}}, \"dateofAccountClosing\": \"2019-12-23\", \"dateofAccountOpening\": \"2011-01-13\", \"timeofAccountClosing\": \"23:22:30\", \"timeofAccountOpening\": \"11:22:20\", \"accountLiabilityHolding\": 3}";
			String summaryToMatch3V2 = "{\"accountID\":3,\"accountType\":\"CURRENT\",\"state\":\"@WAITING FOR APPROVAL&\",\"accountLiabilityHolding\":3}";

			String casedataToMatch2 = "{\"state\": \"@FROZEN&\", \"accountID\": 2, \"accountType\": \"MORTGAGE\", \"institutionDetails\": {\"institutionCode\": \"institution code 1\", \"nameoftheInstitution\": \"instituion name 1\", \"institutionBranchAddress\": {\"city\": \"city1\", \"county\": \"county1\", \"country\": \"country1\", \"postCode\": \"postcode 1\", \"firstLine\": \"first line 1\", \"secondLine\": \"second line1\"}}, \"dateofAccountClosing\": \"2019-12-22\", \"dateofAccountOpening\": \"2011-01-12\", \"timeofAccountClosing\": \"23:11:30\", \"timeofAccountOpening\": \"11:11:20\", \"accountLiabilityHolding\": 2}";
			String summaryToMatch2V2 = "{\"accountID\":2,\"accountType\":\"MORTGAGE\",\"state\":\"@FROZEN&\",\"accountLiabilityHolding\":2}";

			String casedataToMatch1 = "{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"SAVING\", \"institutionDetails\": {\"institutionCode\": \"institution code 0\", \"nameoftheInstitution\": \"instituion name 0\", \"institutionBranchAddress\": {\"city\": \"city0\", \"county\": \"county0\", \"country\": \"country0\", \"postCode\": \"postcode 0\", \"firstLine\": \"first line 0\", \"secondLine\": \"second line0\"}}, \"dateofAccountClosing\": \"2019-12-21\", \"dateofAccountOpening\": \"2011-01-11\", \"timeofAccountClosing\": \"23:00:30\", \"timeofAccountOpening\": \"11:00:20\", \"accountLiabilityHolding\": 1}";
			String summaryToMatch1V2 = "{\"accountID\":1,\"accountType\":\"SAVING\",\"accountLiabilityHolding\":1,\"state\":\"@ACTIVE&\"}";
			String summaryToMatch1V1 = "{\"accountID\":1,\"accountType\":\"SAVING\",\"dateofAccountOpening\":\"2011-01-11\",\"timeofAccountOpening\":\"11:00:20\",\"state\":\"@ACTIVE&\"}";
			String updatedCasedataToMatch1V2 = "{\"state\": \"@REINSTATED&\", \"accountID\": 1, \"accountType\": \"INSURANCE\", \"institutionDetails\": {\"institutionCode\": \"institution update 4 code 0\", \"nameoftheInstitution\": \"instituion update 4 code 0\", \"institutionBranchAddress\": {\"city\": \"update 4 city 0\", \"county\": \"update 4 county 0\", \"country\": \"update 4 country 0\", \"postCode\": \"update 4 postcode 0\", \"firstLine\": \"update 4 first line 0\", \"secondLine\": \"second line 4 second line 0\"}}, \"dateofAccountClosing\": \"2019-12-11\", \"dateofAccountOpening\": \"2011-01-21\", \"timeofAccountClosing\": \"22:00:00\", \"timeofAccountOpening\": \"10:00:00\", \"accountLiabilityHolding\": 1, \"activeationTimeStamp\": \"2019-03-21T12:34:56.789Z\", \"portfolioWebsite\": \"https://www.myportfoliomanager.com\", \"activePortfolio\": true}";
			String updatedSummaryToMatch1V2 = "{\"accountID\":1,\"accountType\":\"INSURANCE\",\"accountLiabilityHolding\":1,\"state\":\"@REINSTATED&\", \"activeationTimeStamp\":\"2019-03-21T12:34:56.789Z\"}";

			//dräçÅrûß
			String casedataToMatch7V2 = "{\"state\": \"dräçÅrûß\", \"accountID\": 100000, \"accountType\": \"ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"portfolioWebsite\":\"https://www.myportfoliomanager.com\",\"activePortfolio\":true,\"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"activeationTimeStamp\":\"2019-03-21T12:34:56.789Z\",\"accountLiabilityHolding\": 1.234}";
			String summaryToMatch7V2 = "{\"accountID\":100000,\"accountType\":\"ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC\","
					+ "\"activeationTimeStamp\":\"2019-03-21T12:34:56.789Z\",\"accountLiabilityHolding\":1.234,\"state\":\"dräçÅrûß\"}";

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			caseRefsV1 = accountCreator.createAccountsArrayData(6, "accounts");
			caseRefsV1.addAll(accountCreator.createAccountWithDefaultValues("999999"));

			//dql filter for case state
			Map<String, String> filterDql = new HashMap<String, String>();
			filterDql.put("state", "'" + searchStringBeforeUpgrade[0] + "'");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 2);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatch0),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), casedataToMatch1),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(6).toString(),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch0V1),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryToMatch1V1),
							"summary does not match");
				}
			}

			//findby criteria with 2 values of states
			filterDql.put("duplicate-state", "'@WAITING FOR APPROVAL&'");
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}
			filterDql.remove("duplicate-state");

			//dql filter for account type - text 
			filterDql.put("accountType", "'" + searchStringBeforeUpgrade[1] + "'");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V1, 6);
			}

			//findby criteria with 2 values of account types
			filterDql.put("duplicate-accountType", "'MORTGAGE'");
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}
			filterDql.remove("duplicate-accountType");

			//dql filter for date of Account Opening - date 
			filterDql.put("dateofAccountOpening", searchStringBeforeUpgrade[2]);

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V1, 6);
			}

			//findby criteria with 2 values of dateofAccountOpening
			filterDql.put("duplicate-dateofAccountOpening", "2011-01-16");
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}
			filterDql.remove("duplicate-dateofAccountOpening");

			//dql filter for time of Account Opening - time 
			filterDql.put("timeofAccountOpening", searchStringBeforeUpgrade[3]);

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V1, 6);
			}

			//findby criteria with 2 values of timeofAccountOpening
			filterDql.put("duplicate-timeofAccountOpening", "11:22:20");
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}
			filterDql.remove("duplicate-timeofAccountOpening");

			//dql filter for account ID - number 
			filterDql.put("accountID", searchStringBeforeUpgrade[4]);

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V1, 6);
			}

			//findby criteria with 2 values of timeofAccountOpening
			filterDql.put("duplicate-accountID", "2");
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}
			filterDql.remove("duplicate-accountID");

			//upgrade the application
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//create case for account v2
			caseRefsV1.addAll(accountCreator.createAccountWithDefaultValues("100000"));

			//existing dql filter should not work  
			//$dql=accountID = 999999 and timeofAccountOpening = 17:24:45 and accountType = 'CURRENT' and dateofAccountOpening = 2019-04-26 and state = '@ACTIVE&'
			//timeofAccountOpening, dateofAccountOpening are not searchable anymore
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				//				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse,
						"incorrect response");
				Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
						"incorrect error code");
				Assert.assertEquals(jsonPath.getString("errorMsg"),
						"Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 17:24:45, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2019-04-26]",
						"incorrect error message");

				Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
				Assert.assertTrue(
						jsonPath.getString("stackTrace").contains(
								"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 17:24:45, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2019-04-26]"),
						"incorrect stacktrace content");

				Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
						"incorrect number of context attributes");

				Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
				Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
						"[DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'timeofAccountOpening' is not searchable: timeofAccountOpening = 17:24:45, DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'dateofAccountOpening' is not searchable: dateofAccountOpening = 2019-04-26]");

			}

			//dql modification
			//$dql=accountID = 999999 and accountType = 'CURRENT' and state = '@ACTIVE&'
			filterDql.remove("timeofAccountOpening");
			filterDql.remove("dateofAccountOpening");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V2, 6);
			}

			//dql modification
			//activation timestamp set to null
			//$dql=accountID = 999999 and accountType = 'CURRENT' and state = '@ACTIVE&' and activeationTimeStamp = null
			filterDql.put("activeationTimeStamp", "null");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch0, summaryToMatch0V2, 6);
			}

			//dql modification
			//keep activeationTimestamp = null and all old v1 cases should be returned
			//$dql=activeationTimeStamp = null
			//manipulation on skip and top 
			filterDql.remove("accountID");
			filterDql.remove("state");
			filterDql.remove("accountType");

			for (int skipParam = 0; skipParam <= 7; skipParam++)
			{
				for (int topParam = 0; topParam <= 7; topParam++)
				{
					for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
					{
						response = cases.getCasesDQL("DQL", filterMap, filterDql,
								selectArrayAbbreviated[stringArrayIterator], String.valueOf(skipParam),
								String.valueOf(topParam), "");

						Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
						jsonPath = response.jsonPath();
						System.out.println(jsonPath.getString("")); // useful for debugging
						System.out.println("values in the loop " + "skip = " + skipParam + " top = " + topParam
								+ " select value = " + selectArray[stringArrayIterator]); // useful for debugging

						String repsonseJson = response.getBody().asString();
						CasesGetResponseBody body = om.readValue(repsonseJson, CasesGetResponseBody.class);

						// Expect quantity to be equal to 5 minus the skipped types.
						Assert.assertEquals(body.size(), Math.min(topParam, Math.max(0, 7 - skipParam)));

						List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

						int idx = 0;
						if ((topParam) == 0)
						{
							Assert.assertEquals(response.asString(), "[]", "empty response");
						}
						if (skipParam < 1 && topParam >= 1)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch0, summaryToMatch0V2, 6);
						}
						if (skipParam < 2 && skipParam + topParam >= 2)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch6, summaryToMatch6V2, 5);
						}
						if (skipParam < 3 && skipParam + topParam >= 3)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch5, summaryToMatch5V2, 4);
						}
						if (skipParam < 4 && skipParam + topParam >= 4)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch4, summaryToMatch4V2, 3);
						}
						if (skipParam < 5 && skipParam + topParam >= 5)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch3, summaryToMatch3V2, 2);
						}
						if (skipParam < 6 && skipParam + topParam >= 6)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch2, summaryToMatch2V2, 1);
						}
						if (skipParam < 7 && skipParam + topParam >= 7)
						{
							CaseInfo caseInfo = body.get(idx++);
							assertCaseInfo(caseInfo, aspectNames, casedataToMatch1, summaryToMatch1V2, 0);
						}
					}
				}
			}

			//dql modification
			//long text value in accountType
			filterDql.clear();
			filterDql.put("accountType",
					"'ThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXEDCASESIfTHISSEARCHFAILSItClearlyMeansThatTheSearchFor400CharactersIsNotWorkingThisLongSentenceIs400CharactersLongSPECFICALLYCreatedToTestTheTextSearchOf400CharactersTHEREAreNoPunctuationsInThisPassageONLYSpacesAreKeptANDACombinationsOfMIXC'");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "");
				//				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				//				jsonPath = response.jsonPath();
				//				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch7V2, summaryToMatch7V2, 7);
			}

			//dql modification
			//dql with dateTimeTZ
			//$dql=activeationTimeStamp = null
			filterDql.clear();
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch7V2, summaryToMatch7V2, 7);
			}

			//dql modification
			//dql with fixed point numbers
			//$dql=accountLiabilityHolding = 1.234
			filterDql.clear();
			filterDql.put("accountLiabilityHolding", "1.234");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch7V2, summaryToMatch7V2, 7);
			}

			//dql modification
			//dql with text accented character
			//$dql=state = 'dräçÅrûß'
			filterDql.clear();
			filterDql.put("state", "'dräçÅrûß'");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, casedataToMatch7V2, summaryToMatch7V2, 7);
			}

			//update the tests 1 to 6 and check case reference and data areturned always latest in find by 
			updatedCaseRefsV1 = accountCreator.updateAccountsArrayData(caseRefsV1.subList(0, 1), 4, "accounts");

			//dql to check searchable account fields
			filterDql.clear();
			filterDql.put("accountID", "1");
			filterDql.put("accountType", "'INSURANCE'");
			filterDql.put("accountLiabilityHolding", "1");
			filterDql.put("state", "'@REINSTATED&'");
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				assertCaseInfo(obj.get(0), aspectNames, updatedCasedataToMatch1V2, updatedSummaryToMatch1V2,
						updatedCaseRefsV1, 0);
			}

			//dql to modification - check if different values for the same key do not return any results
			//since criteria uses the conjunction "and" and NOT "or" 
			filterDql.clear();
			filterDql.put("accountID", "1");
			filterDql.put("duplicate-accountID", "2");
			filterDql.put("state", "'@REINSTATED&'");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}

			//dql to modification - check if different values for the same key do not return any results
			//since criteria uses the conjunction "and" and NOT "or" 
			filterDql.clear();
			filterDql.put("state", "'@WAITING FOR CANCELLATION&'");
			filterDql.put("duplicate-state", "'@FROZEN&'");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}

			//dql to modification - check if different values for the same key do not return any results
			//since criteria uses the conjunction "and" and NOT "or" 
			filterDql.clear();
			filterDql.put("activeationTimeStamp", "2019-03-21T12:34:56.789Z");
			filterDql.put("duplicate-activeationTimeStamp", "2019-03-21T12:34:57Z");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}

			//dql to modification - check if different values for the same key do not return any results
			//since criteria uses the conjunction "and" and NOT "or" 
			filterDql.clear();
			filterDql.put("accountLiabilityHolding", "1");
			filterDql.put("duplicate-accountLiabilityHolding", "991212.11");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
				assertEmptyResponse();
			}

			//specifiying structured attributes returns an error
			filterDql.clear();
			filterDql.put("institutionDetails", "null");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse,
						"incorrect response");
				Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
						"incorrect error code");
				Assert.assertEquals(jsonPath.getString("errorMsg"),
						"Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'institutionDetails' is not searchable: institutionDetails = null]",
						"incorrect error message");

				Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
				Assert.assertTrue(
						jsonPath.getString("stackTrace").contains(
								"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'institutionDetails' is not searchable: institutionDetails = null]"),
						"incorrect stacktrace content");

				Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
						"incorrect number of context attributes");

				Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
				Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
						"[DQL_ATTRIBUTE_NOT_SEARCHABLE: Attribute 'institutionDetails' is not searchable: institutionDetails = null]");

			}

			filterDql.clear();
			filterDql.put("institutionBranchAddress", "null");

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("DQL", filterMap, filterDql, selectArray[selectIterator], skip, top, "");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				Assert.assertEquals(response.getStatusCode(), properties.getBadDQL().errorResponse,
						"incorrect response");
				Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadDQL().errorCode,
						"incorrect error code");
				Assert.assertEquals(jsonPath.getString("errorMsg"),
						"Bad DQL: [DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: institutionBranchAddress]",
						"incorrect error message");

				Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
				Assert.assertTrue(
						jsonPath.getString("stackTrace").contains(
								"com.tibco.bpm.cdm.api.exception.ArgumentException: Bad DQL: [DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: institutionBranchAddress]"),
						"incorrect stacktrace content");

				Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1,
						"incorrect number of context attributes");

				Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "issues");
				Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"),
						"[DQL_UNKNOWN_ATTRIBUTE_NAME: Unknown attribute name: institutionBranchAddress]");

			}
			filterDql.clear();
		}
		finally
		{
			caseRefsV1.clear();
			caseRefsV2.clear();
			forceUndeploy(deploymentIdApp1);
			forceUndeploy(deploymentIdApp2);
		}
	}

	//Test to include the length of text to be changed to more than 400 when its marked not searchable
	@Test(description = "Test to include the length of text to be changed to more than 400 when its marked not searchable")
	public void taskACE1829() throws IOException, InterruptedException, PersistenceException, DeploymentException,
			InternalException, CasedataException, ReferenceException, ArgumentException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException
	{

		//create select filters
		Map<String, String> filterMap = new HashMap<String, String>();
		List<String> casedata = new ArrayList<>();
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("caseState ", "'@ACTIVE&'");
		filterMap.put("modificationTimestamp", cases.DEFAULT_MODIFICATION_TIME);
		String top = "1";
		String skip = "0";

		try
		{
			//400 characters long
			String searchString = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij";

			String casedataToMatchV1 = "{\"state\": \"@ACTIVE&\", \"accountID\": 12345678, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11,"
					+ "\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij\"}";

			String summaryToMatchV1 = "{\"accountID\":12345678,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"2019-04-26\",\"timeofAccountOpening\":\"17:24:45\",\"state\":\"@ACTIVE&\",\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij\"}";

			//deploy the application
			deployWithModification((dm) -> {
				Attribute aLargeText = dm.getStructuredTypeByName("Account").newAttribute();
				aLargeText.setName("aLargeText");
				aLargeText.setLabel("aLargeText");
				aLargeText.setIsSearchable(true);
				aLargeText.setIsSummary(true);
				aLargeText.setType("base:Text");
				aLargeText.newConstraint("length", "400");
			}, "2.0.0.1");

			//create cases for account
			casedata.add(casedataToMatchV1);
			response = cases.createCases(caseType, 2, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsV1.add(jsonPath.getString("[" + 0 + "]"));
			casedata.clear();

			//application major version is 2 and not 1
			response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top, searchString, "");
			properties.assertErrorResponseUnkownType(caseType, applicationMajorVersion, response);

			//changing the application major version
			filterMap.remove("applicationMajorVersion");
			filterMap.put("applicationMajorVersion", applicationMajorVersionLargeText);

			//iterate based on a search string
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top, searchString, "");
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
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchString, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
							"summary does not match");
				}
			}

			//deploy the application
			deployWithModification((dm) -> {
				Attribute aLargeText = dm.getStructuredTypeByName("Account").newAttribute();
				aLargeText.setName("aLargeText");
				aLargeText.setLabel("aLargeText");
				aLargeText.setIsSearchable(false);
				aLargeText.setIsSummary(true);
				aLargeText.setType("base:Text");
				aLargeText.newConstraint("length", "4000");
			}, "2.0.1.2");

			String casedataToMatchV2 = "{\"state\": \"@ACTIVE&\", \"accountID\": 12345679, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11,"
					+ "\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij\"}";

			String summaryToMatchV2 = "{\"accountID\":12345679,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"2019-04-26\",\"timeofAccountOpening\":\"17:24:45\",\"state\":\"@ACTIVE&\",\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij\"}";

			String searchStringBeforeUpgrade = "12345678";
			String searchStringAfterUpgrade = "12345679";

			//create cases for account
			casedata.add(casedataToMatchV2);
			response = cases.createCases(caseType, 2, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsV2.add(jsonPath.getString("[" + 0 + "]"));
			casedata.clear();

			//iterate based on a search string before upgrade 
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
						searchStringBeforeUpgrade, "");
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
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchStringBeforeUpgrade, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1),
							"summary does not match");
				}
			}

			//iterate based on a search string before upgrade 
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
						searchStringAfterUpgrade, "");
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
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV2),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV2.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV2),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchStringAfterUpgrade, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV2),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV2.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV2),
							"summary does not match");
				}
			}

			//400 characters long
			String searchStringUpdate = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789áéíóúÁÉÍÓÚäöÜÖëàèÙÒãñÑÕâôÊÎ";

			String casedataToMatchV1Update = "{\"state\": \"@ACTIVE&\", \"accountID\": 12345678, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11,"
					+ "\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789áéíóúÁÉÍÓÚäöÜÖëàèÙÒãñÑÕâôÊÎ\"}";

			String summaryToMatchV1Update = "{\"accountID\":12345678,\"accountType\":\"CURRENT\",\"dateofAccountOpening\":\"2019-04-26\",\"timeofAccountOpening\":\"17:24:45\",\"state\":\"@ACTIVE&\",\"aLargeText\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789áéíóúÁÉÍÓÚäöÜÖëàèÙÒãñÑÕâôÊÎ\"}";

			//update cases for account
			casedata.add(casedataToMatchV2);
			response = cases.updateSingleCase(caseRefsV1.get(0), casedataToMatchV1Update);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsV1.clear();
			caseRefsV1.add(jsonPath.getString("[" + 0 + "]"));
			casedata.clear();

			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
						searchStringUpdate, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 0);
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchStringUpdate, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 0);
			}

			//iterate based on a search string before upgrade 
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top,
						searchStringBeforeUpgrade, "");
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
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1Update),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1Update),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchStringBeforeUpgrade, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedataToMatchV1Update),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseRefsV1.get(0).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatchV1Update),
							"summary does not match");
				}
			}
		}
		finally
		{
			caseRefsV1.clear();
			caseRefsV2.clear();
			forceUndeploy(deploymentIdApp4);
			forceUndeploy(deploymentIdApp3);
		}
	}
}