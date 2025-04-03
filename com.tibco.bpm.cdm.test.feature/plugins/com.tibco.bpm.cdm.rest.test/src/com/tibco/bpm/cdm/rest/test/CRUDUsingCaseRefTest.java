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
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CRUDUsingCaseRefTest extends Utils
{

	CaseTypesFunctions			caseTypes				= new CaseTypesFunctions();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	int							validStatusCode			= 200;

	int							invalidStatusCode		= 400;

	int							notFoundStatusCode		= 404;

	private JsonPath			jsonPath				= null;

	List<String>				caseRefs				= new ArrayList<>();

	private static final String	caseType				= "com.example.bankdatamodel.Customer";

	private static final String	applicationMajorVersion	= "1";

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger					deploymentIdApp1		= BigInteger.valueOf(5);

	BigInteger					deploymentIdApp2		= BigInteger.valueOf(6);

	private String				createCustomer			= "{\"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}";

	private String				createCustomerV1		= "{\"customerCID\":\"WINTERFELL-00001\", \"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}";

	private String				createCustomerReadV1	= "";

	private String				createCustomerSummaryV1	= "{\"customerCID\":\"WINTERFELL-00001\",\"state\":\"ACTIVE10KTO50K\"}";

	private String				updateCustomerV2		= "{\"customerCID\":\"WINTERFELL-00001\", \"state\": \"REGULAR1KTO10K\", \"personalDetails\": {\"age\": 42, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"GREY\", \"firstName\": \"UNPOPULAR\", \"salutation\": \"MR\", \"dateofBirth\": \"1977-11-22\", \"homeAddress\": {\"city\": \"BRUSSELS\", \"county\": \"STATE-16\", \"country\": \"BELGIUM\", \"postCode\": \"BL23 AA1\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 14.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"TORONTO\", \"county\": \"QUEBEC\", \"country\": \"CANADA\", \"postCode\": \"CA 111061\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-03-23\", \"typeofEmployement\": \"SELF-EPMPLOYMENT\", \"annualIncomeSalaryBeforeTaxes\": 81171}, {\"placeofWork\": {\"city\": \"Amsterdam\", \"county\": \"CENTRAL\", \"country\": \"NETHERLANDS\", \"postCode\": \"AMST11\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2000-02-29\", \"annualIncomeSalaryBeforeTaxes\": 25670}, {\"placeofWork\": {\"city\": \"BOMBAY\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"400001\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"IMPHAL\", \"county\": \"MANIPUR\", \"country\": \"INDIA\", \"postCode\": \"913 010\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1986-02-17\", \"typeofEmployement\": \"VOLUNTEERING\", \"annualIncomeSalaryBeforeTaxes\": 10}]}";

	private String				updateCustomerReadV2	= "{\"customerCID\":\"WINTERFELL-00001\", \"state\": \"REGULAR1KTO10K\", \"personalDetails\": {\"age\": 42, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"GREY\", \"firstName\": \"UNPOPULAR\", \"salutation\": \"MR\", \"dateofBirth\": \"1977-11-22\", \"homeAddress\": {\"city\": \"BRUSSELS\", \"county\": \"STATE-16\", \"country\": \"BELGIUM\", \"postCode\": \"BL23 AA1\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 14.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"TORONTO\", \"county\": \"QUEBEC\", \"country\": \"CANADA\", \"postCode\": \"CA 111061\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-03-23\", \"typeofEmployement\": \"SELF-EPMPLOYMENT\", \"annualIncomeSalaryBeforeTaxes\": 81171}, {\"placeofWork\": {\"city\": \"Amsterdam\", \"county\": \"CENTRAL\", \"country\": \"NETHERLANDS\", \"postCode\": \"AMST11\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2000-02-29\", \"typeofEmployement\": \"Self-Employed\", \"annualIncomeSalaryBeforeTaxes\": 25670}, {\"placeofWork\": {\"city\": \"BOMBAY\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"400001\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"IMPHAL\", \"county\": \"MANIPUR\", \"country\": \"INDIA\", \"postCode\": \"913 010\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1986-02-17\", \"typeofEmployement\": \"VOLUNTEERING\", \"annualIncomeSalaryBeforeTaxes\": 10}]}";

	private String				updateCustomerSummaryV2	= "{\"customerCID\":\"WINTERFELL-00001\",\"state\":\"REGULAR1KTO10K\"}";

	private String				updateCustomerV3		= "{\"customerCID\":\"WINTERFELL-00001\", \"state\": \"BARREDORTERMINATED\", \"personalDetails\": {\"lastName\": \"Stark - QUEEN OF THE NORTH\", \"homeAddress\": {\"country\": \"WESTEROS\"}}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"TORONTO\", \"county\": \"QUEBEC\", \"country\": \"CANADA\", \"postCode\": \"CA 111061\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-03-23\", \"typeofEmployement\": \"SELF-EPMPLOYMENT\", \"annualIncomeSalaryBeforeTaxes\": 81171}, {\"placeofWork\": {\"city\": \"Amsterdam\", \"county\": \"CENTRAL\", \"country\": \"NETHERLANDS\", \"postCode\": \"AMST11\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2000-02-29\", \"typeofEmployement\": \"Self-Employed\", \"annualIncomeSalaryBeforeTaxes\": 25670}, {\"placeofWork\": {\"city\": \"BOMBAY\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"400001\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"IMPHAL\", \"county\": \"MANIPUR\", \"country\": \"INDIA\", \"postCode\": \"913 010\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1986-02-17\", \"typeofEmployement\": \"VOLUNTEERING\", \"annualIncomeSalaryBeforeTaxes\": 10}, {\"placeofWork\": {}, \"dateofJoining\": \"2019-06-03\", \"typeofEmployement\": \"FULL TIME EMPLOYEE\", \"annualIncomeSalaryBeforeTaxes\": 40000.12}]}";

	private String				updateCustomerReadV3	= "{\"customerCID\":\"WINTERFELL-00001\", \"state\": \"BARREDORTERMINATED\", \"personalDetails\": {\"age\": 25, \"gender\": \"FEMALE\", \"lastName\": \"Stark - QUEEN OF THE NORTH\", \"firstName\": \"Sansa\", \"salutation\": \"LADY\", \"dateofBirth\": \"1992-11-01\", \"homeAddress\": {\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"WESTEROS\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", \"secondLine\": \"Sadar-Bazar\"}, \"numberofYearsStayingattheAddress\": 1.0}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"TORONTO\", \"county\": \"QUEBEC\", \"country\": \"CANADA\", \"postCode\": \"CA 111061\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-03-23\", \"typeofEmployement\": \"SELF-EPMPLOYMENT\", \"annualIncomeSalaryBeforeTaxes\": 81171}, {\"placeofWork\": {\"city\": \"Amsterdam\", \"county\": \"CENTRAL\", \"country\": \"NETHERLANDS\", \"postCode\": \"AMST11\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2000-02-29\", \"typeofEmployement\": \"Self-Employed\", \"annualIncomeSalaryBeforeTaxes\": 25670}, {\"placeofWork\": {\"city\": \"BOMBAY\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"400001\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"IMPHAL\", \"county\": \"MANIPUR\", \"country\": \"INDIA\", \"postCode\": \"913 010\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1986-02-17\", \"typeofEmployement\": \"VOLUNTEERING\", \"annualIncomeSalaryBeforeTaxes\": 10}, {\"placeofWork\": {\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", \"secondLine\": \"Sadar-Bazar\"}, \"dateofJoining\": \"2019-06-03\", \"typeofEmployement\": \"FULL TIME EMPLOYEE\", \"annualIncomeSalaryBeforeTaxes\": 40000.12}]}";

	private String				updateCustomerSummaryV3	= "{\"customerCID\":\"WINTERFELL-00001\",\"state\":\"BARREDORTERMINATED\"}";

	private Response			response				= null;

	private String				initialModificationTimestamp, initialCreationTimestamp, initialCaseRef, initialSummary,
			initialCasedata, initialUser, modificationUser = "";

	private Map<String, String>	filterMap				= new HashMap<String, String>();

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

	private String alterCaseReference(String caseReference, String validity)
	{
		String tempString, tempCaseRef = "";
		if (validity.equalsIgnoreCase("valid"))
		{
			tempCaseRef = caseReference.substring(0, (caseReference.length() - 2));
		}
		else
		{
			tempCaseRef = caseReference.substring(0, (caseReference.length() - 3));
		}
		tempString = tempCaseRef.concat("-119");
		return tempString;
	}

	private void assertErrorResponseVersionMismatch(String caseReference, String versionProvided, String actualVersion,
			Response response) throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), properties.getCaseReferenceVersionMismatch().errorResponse,
				"incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = properties.getCaseReferenceVersionMismatch().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", versionProvided);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", actualVersion);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReference);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getCaseReferenceVersionMismatch().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReference,
				"incorrect name of context attribute");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace")
				.contains("com.tibco.bpm.cdm.api.exception.CaseOutOfSyncException: Version in case reference ("
						+ versionProvided + ") mismatches actual version (" + actualVersion + "): " + caseReference),
				true, "incorrect stacktrace");

	}

	private void assertReadCase(String expectedCaseReferece, String expectedCasedata, String expectedCaseSummary,
			JsonPath jsonPath, List<String> aspectNames, String version) throws IOException
	{
		if (version.equalsIgnoreCase("v1"))
		{
			if (aspectNames.contains("casedata") || aspectNames.contains("c")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), expectedCasedata),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || aspectNames.contains("m")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
			}
			if (aspectNames.contains("caseReference") || aspectNames.contains("cr")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertEquals(jsonPath.getString("caseReference"), expectedCaseReferece,
						"caseRef should be tagged");
			}
			if (aspectNames.contains("summary") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), expectedCaseSummary),
						"summary does not match");
			}
		}

		else
		{
			if (aspectNames.contains("casedata") || aspectNames.contains("c")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), expectedCasedata),
						"casedata does not match");
				Assert.assertFalse(assertUnsortedData(jsonPath.getString("casedata"), initialCasedata),
						"casedata does not match");
			}
			if (aspectNames.contains("metadata") || aspectNames.contains("m")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
				Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");

				Assert.assertEquals(jsonPath.getString("metadata.creationTimestamp"), initialCreationTimestamp,
						"creation timestamp mismatch");
				Assert.assertEquals(jsonPath.getString("metadata.createdBy"), initialUser, "created by mismatch");
				Assert.assertEquals(jsonPath.getString("metadata.modifiedBy"), modificationUser, "modified by mismatch");

				Assert.assertNotEquals(jsonPath.getString("metadata.modificationTimestamp"),
						initialModificationTimestamp, "modification timestamp should not be same");
			}
			if (aspectNames.contains("caseReference") || aspectNames.contains("cr")
					|| aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertEquals(jsonPath.getString("caseReference"), expectedCaseReferece,
						"caseRef should be tagged");

				Assert.assertNotEquals(jsonPath.getString("caseReference"), initialCaseRef, "created by mismatch");
			}
			if (aspectNames.contains("summary") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), expectedCaseSummary),
						"summary does not match");

				Assert.assertFalse(assertUnsortedData(jsonPath.getString("summary"), initialSummary),
						"summary does not match");
			}
		}
	}

	//Test to deploy a model and create a single case
	//Test to deploy a model and create a single case and use get cases to store all initial values
	@Test(description = "Test to deploy a model and create a single case and use get cases to store all initial values")
	public void createSingleCustomer() throws IOException, InterruptedException, DeploymentException,
			PersistenceException, InternalException, URISyntaxException, RuntimeApplicationException
	{
		forceUndeploy(deploymentIdApp1);

		deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

		List<String> casedata = new ArrayList<>();
		casedata.add(createCustomer);
		filterMap.put("caseType", caseType);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("cid", "WINTERFELL-00001");

		response = cases.createCases("com.example.bankdatamodel.Customer", 1, casedata);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		caseRefs.add(jsonPath.getString("[0]"));

		//store all necessary information from get cases call
		response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while reading single case");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 1);

		createCustomerReadV1 = obj.get(0).getCasedata();
		initialCasedata = obj.get(0).getCasedata();
		initialCreationTimestamp = obj.get(0).getMetadata().getCreationTimestamp();
		initialModificationTimestamp = obj.get(0).getMetadata().getModificationTimestamp();
		initialSummary = obj.get(0).getSummary();
		initialCaseRef = obj.get(0).getCaseReference();
		initialUser = obj.get(0).getMetadata().getCreatedBy();
		modificationUser = "tibco-admin";

		Assert.assertTrue(assertUnsortedData(createCustomerReadV1, createCustomerV1), "casedata does not match");
	}

	//	//Test to deploy a model and create a single case
	//Test to update and read incorrect versions of case reference and non-existing values of case reference 
	@Test(dependsOnMethods = {
			"createSingleCustomer"}, description = "Test to update and read incorrect versions of case reference and non-existing values of case reference")
	public void updateAndReadSingleCustomerIncorrectRef() throws IOException, InterruptedException, DeploymentException,
			PersistenceException, InternalException, URISyntaxException, RuntimeApplicationException
	{

		//read single case with all select filters - incorrect reference
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase("Non_Existing_Case_Ref", selectArray[selectIterator]);
			properties.assertErrorResponseInvalidFormat("Non_Existing_Case_Ref", response);
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase("Non_Existing_Case_Ref", selectArrayAbbreviated[selectIterator]);
			properties.assertErrorResponseInvalidFormat("Non_Existing_Case_Ref", response);
		}

		//read single case with all select filters - incorrect version
		String tempString = alterCaseReference(caseRefs.get(0), "invalid");

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(tempString, selectArray[selectIterator]);
			properties.assertErrorResponseInvalidFormat(tempString, response);
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(tempString, selectArrayAbbreviated[selectIterator]);
			properties.assertErrorResponseInvalidFormat(tempString, response);
		}

		//update - non-existing reference
		response = cases.updateSingleCase(tempString, createCustomerReadV1);
		properties.assertErrorResponseInvalidFormat(tempString, response);

		//update - incorrect reference
		response = cases.updateSingleCase("Non_Existing_Case_Ref", createCustomerReadV1);
		properties.assertErrorResponseInvalidFormat("Non_Existing_Case_Ref", response);

		//update - version mismatch
		tempString = alterCaseReference(caseRefs.get(0), "valid");

		response = cases.updateSingleCase(tempString, createCustomerReadV1);
		assertErrorResponseVersionMismatch(tempString, "119", "0", response);
	}

	//Test to update a single case multiple times; read different versions of case and read the versions of past
	@Test(dependsOnMethods = {
			"updateAndReadSingleCustomerIncorrectRef"}, description = "Test to update a single case multiple times; read different versions of case and read the versions of past")

	public void updateAndReadSingleCustomer() throws IOException, InterruptedException, DeploymentException,
			PersistenceException, InternalException, URISyntaxException, RuntimeApplicationException
	{

		//read single case with all select filters
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(0), selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(0), createCustomerV1, createCustomerSummaryV1, jsonPath, aspectNames, "v1");
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(0), selectArrayAbbreviated[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(0), createCustomerV1, createCustomerSummaryV1, jsonPath, aspectNames, "v1");
		}

		//update
		response = cases.updateSingleCase(caseRefs.get(0), updateCustomerV2);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		caseRefs.add(jsonPath.getString("[" + 0 + "]"));

		response = cases.updateSingleCase(caseRefs.get(0), createCustomerReadV1);
		assertErrorResponseVersionMismatch(caseRefs.get(0), "0", "1", response);

		//read single case with all select filters
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(1), selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(1), updateCustomerReadV2, updateCustomerSummaryV2, jsonPath, aspectNames, "v2");
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(1), selectArrayAbbreviated[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(1), updateCustomerReadV2, updateCustomerSummaryV2, jsonPath, aspectNames, "v2");
		}

		//update
		response = cases.updateSingleCase(caseRefs.get(1), updateCustomerV3);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		caseRefs.add(jsonPath.getString("[" + 0 + "]"));

		response = cases.updateSingleCase(caseRefs.get(1), createCustomerReadV1);
		assertErrorResponseVersionMismatch(caseRefs.get(1), "1", "2", response);

		//read single case with all select selects
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(2), selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(2), selectArrayAbbreviated[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		//read single case with all select filters - case references of past
		//send the case reference that is on v2 but assert that all latest details are returned
		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(1), selectArrayAbbreviated[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		//send the case reference that is on v1 but assert that all latest details are returned
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(0), selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		String tempString = alterCaseReference(caseRefs.get(2), "valid");

		//send the case reference version that is not reached yet but assert that all latest details are returned
		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(tempString, selectArrayAbbreviated[selectIterator]);
			//Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(tempString, selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}
	}

	//Test to delete incorrect versions, non-existing values and correct version of case reference 
	@Test(dependsOnMethods = {
			"updateAndReadSingleCustomer"}, description = "Test to delete incorrect versions, non-existing values and correct version of case reference")
	public void deleteSingleCaseReferece() throws IOException, InterruptedException, DeploymentException,
			PersistenceException, InternalException, URISyntaxException, RuntimeApplicationException
	{

		//purge single case with all select filters - incorrect reference
		response = cases.purgeSingleCase("Non_Existing_Case_Ref");
		properties.assertErrorResponseInvalidFormat("Non_Existing_Case_Ref", response);

		//read single case with all select filters - invalid string
		String tempString = alterCaseReference(caseRefs.get(2), "invalid");

		response = cases.purgeSingleCase(tempString);
		properties.assertErrorResponseInvalidFormat(tempString, response);

		//read single case with all select filters - valid string non-existing version
		tempString = alterCaseReference(caseRefs.get(2), "valid");

		response = cases.purgeSingleCase(tempString);
		assertErrorResponseVersionMismatch(tempString, "119", "2", response);

		//past versions
		response = cases.purgeSingleCase(caseRefs.get(0));
		assertErrorResponseVersionMismatch(caseRefs.get(0), "0", "2", response);

		//past versions
		response = cases.purgeSingleCase(caseRefs.get(1));
		assertErrorResponseVersionMismatch(caseRefs.get(1), "1", "2", response);

		//read single case with all select selects  - just to make sure the case is not deleted 
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(2), selectArray[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		//read single case with all select selects  - just to make sure the case is not deleted - with a past version
		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(1), selectArrayAbbreviated[selectIterator]);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println(
					"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging
			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
			assertReadCase(caseRefs.get(2), updateCustomerReadV3, updateCustomerSummaryV3, jsonPath, aspectNames, "v2");
		}

		//get the count
		response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEREFERENCE, "", "10", "");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 1);

		//purge the right case 
		response = cases.purgeSingleCase(caseRefs.get(2));
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		//get the count - it should be 0
		response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEREFERENCE, "", "10", "");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 0);

		//read single case with all select selects  - just to make sure the case is not deleted 
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(2), selectArray[selectIterator]);
			properties.assertErrorResponseRefDoesNotExist(caseRefs.get(2), response);
		}

		//read single case with all select selects  - just to make sure the case is deleted
		for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(1), selectArrayAbbreviated[selectIterator]);
			properties.assertErrorResponseRefDoesNotExist(caseRefs.get(1), response);
		}

		//read single case with all select selects  - just to make sure the case is deleted
		for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
		{
			response = cases.getSingleCase(caseRefs.get(0), selectArray[selectIterator]);
			properties.assertErrorResponseRefDoesNotExist(caseRefs.get(0), response);
		}

		//purge the right case - again for error case reference does not exist
		response = cases.purgeSingleCase(caseRefs.get(2));
		properties.assertErrorResponseRefDoesNotExist(caseRefs.get(2), response);

		//purge the right case - again for error case reference does not exist
		response = cases.purgeSingleCase(caseRefs.get(0));
		properties.assertErrorResponseRefDoesNotExist(caseRefs.get(0), response);

		forceUndeploy(deploymentIdApp1);
	}
}
