package com.tibco.bpm.cdm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
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
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class ACE_1834_MinMaxValueRange extends Utils
{
	AccountCreatorFunction	accountCreator					= new AccountCreatorFunction();

	CustomerCreatorFunction	customerCreator					= new CustomerCreatorFunction();

	CaseLinksFunction		linksFunction					= new CaseLinksFunction();

	CasesFunctions			cases							= new CasesFunctions();

	GetPropertiesFunction	properties						= new GetPropertiesFunction();

	CaseTypesFunctions		caseTypes						= new CaseTypesFunctions();

	private Response		response						= null;

	private JsonPath		jsonPath						= null;

	List<String>			caseRefs						= new ArrayList<>();

	List<String>			updatedCaseRefs					= new ArrayList<>();

	private BigInteger		deploymentIdApp					= BigInteger.valueOf(8);

	private String			createCustomerCaseSkeleton		= "{\"aFixedPointNumber\": " + "VALUE_TO_REPLACE"
			+ ", \"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}";

	private String			customerCasedataSkeletonWithId	= "{\"customerCID\":\"WINTERFELL-00001\", \"aFixedPointNumber\": "
			+ "VALUE_TO_REPLACE"
			+ ", \"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}";

	private String			customerSummarySkeletonWithId	= "{\"customerCID\":\"WINTERFELL-00001\", \"aFixedPointNumber\": "
			+ "VALUE_TO_REPLACE" + ", \"state\": \"ACTIVE10KTO50K\"}";

	private void deployWithModification(DataModelModifier modifier) throws IOException, URISyntaxException,
			DataModelSerializationException, RuntimeApplicationException, InterruptedException
	{

		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifier);

		//deploy Vx
		BigInteger deploymentIdApp2 = BigInteger.valueOf(8);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

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

	private void createCase(String aFixedPointNumberValue) throws IOException, InterruptedException
	{

		List<String> casedata = new ArrayList<>();

		String tempString = createCustomerCaseSkeleton.toString();

		tempString = tempString.replace("VALUE_TO_REPLACE", aFixedPointNumberValue.toString());
		casedata.add(tempString);
		response = cases.createCases("com.example.bankdatamodel.Customer", 1, casedata);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		caseRefs.add(jsonPath.getString("[0]"));
	}

	private String updateCase(String caseReference, String replaceFrom, String replaceTo, boolean success)
			throws IOException, InterruptedException
	{

		List<String> casedata = new ArrayList<>();

		String tempString = customerCasedataSkeletonWithId.toString();
		tempString = tempString.replace("VALUE_TO_REPLACE", replaceFrom.toString());
		tempString = tempString.replace(replaceFrom.toString(), replaceTo.toString());
		casedata.add(tempString);
		response = cases.updateSingleCase(caseReference, tempString);
		if (success == true)
		{
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			updatedCaseRefs.add(jsonPath.getString("[" + 0 + "]"));
		}

		else if (success == false)
		{
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), invalidStatusCode, "incorrect response");
		}

		return tempString;
	}

	private void assertRead(String caseReference, String aFixedPointNumberActualValue)
			throws IOException, InterruptedException
	{

		String tempCasedata = customerCasedataSkeletonWithId.toString();
		tempCasedata = tempCasedata.replace("VALUE_TO_REPLACE", aFixedPointNumberActualValue.toString());

		String tempSummary = customerSummarySkeletonWithId.toString();
		tempSummary = tempSummary.replace("VALUE_TO_REPLACE", aFixedPointNumberActualValue.toString());

		response = cases.getSingleCase(caseReference, SELECT_CASES_CASEDATA_SUMMARY);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), tempCasedata), "casedata does not match");

		Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), tempSummary), "summary does not match");
	}

	//Test to create and update cases when the number has negative min value and positive max value & |minValue| > |maxValue|
	@Test(description = "Test to create and update cases when the number has negative min value and positive max value & |minValue| > |maxValue|")
	public void rangeNegativeToPositive()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{
		caseRefs.clear();
		updatedCaseRefs.clear();

		try
		{
			deployWithModification((dm) -> {
				Attribute aFixedPointNumber = dm.getStructuredTypeByName("Customer").newAttribute();
				aFixedPointNumber.setName("aFixedPointNumber");
				aFixedPointNumber.setLabel("aFixedPointNumber");
				aFixedPointNumber.setIsSearchable(true);
				aFixedPointNumber.setIsSummary(true);
				//decimal places - 5; length - 11
				//min value : negative (-123456.78) and max value : positive (123.45678)
				aFixedPointNumber.setType("base:FixedPointNumber");
				aFixedPointNumber.newConstraint("length", "11");
				aFixedPointNumber.newConstraint("minValue", "-123456.78");
				aFixedPointNumber.newConstraint("minValueInclusive", "true");
				aFixedPointNumber.newConstraint("maxValue", "123.45678");
				aFixedPointNumber.newConstraint("maxValueInclusive", "true");
				aFixedPointNumber.newConstraint("decimalPlaces", "5");
			});

			//create a case with negative -123456.78 update it to 123.45678
			createCase("-123456.78");
			updateCase(caseRefs.get(0), "-123456.78", "123.45678", true);
			assertRead(updatedCaseRefs.get(0), "123.45678");

			//update to -123456.78
			updateCase(updatedCaseRefs.get(0), "123.45678", "-123456.78", true);
			assertRead(updatedCaseRefs.get(1), "-123456.78");

			//update to -98765.345 
			updateCase(updatedCaseRefs.get(1), "-123456.78", "-98765.345", true);
			assertRead(updatedCaseRefs.get(2), "-98765.345");

			//update to 71.34553
			updateCase(updatedCaseRefs.get(2), "-98765.345", "71.34553", true);
			assertRead(updatedCaseRefs.get(3), "71.34553");

			//update to -12234.121
			updateCase(updatedCaseRefs.get(3), "71.34553", "-12234.121", true);
			assertRead(updatedCaseRefs.get(4), "-12234.121");

			//update to -123456.78
			updateCase(updatedCaseRefs.get(4), "-12234.121", "-123456.78", true);
			assertRead(updatedCaseRefs.get(5), "-123456.78");

			//update to 11.2343 
			updateCase(updatedCaseRefs.get(5), "-123456.78", "11.2343", true);
			assertRead(updatedCaseRefs.get(6), "11.2343");

			//update to 123.45678
			updateCase(updatedCaseRefs.get(6), "11.2343", "123.45678", true);
			assertRead(updatedCaseRefs.get(7), "123.45678");

			//update to 123.45679 (update unsuccessful)
			updateCase(updatedCaseRefs.get(7), "123.45678", "123.45679", false);
			assertRead(updatedCaseRefs.get(7), "123.45678");

			//update to -123456.79 (update unsuccessful)
			updateCase(updatedCaseRefs.get(7), "123.45678", "-123456.79", false);
			assertRead(updatedCaseRefs.get(7), "123.45678");

			caseRefs.clear();
			updatedCaseRefs.clear();
		}
		finally
		{
			forceUndeploy(deploymentIdApp);
		}
	}

	//Test to create and update cases when the number has negative min value and max value i.e. minvalue < maxValue < 0
	@Test(description = "Test to create and update cases when the number has negative min value and max value i.e. minvalue < maxValue < 0")
	public void rangeNegativeToNegative()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{
		caseRefs.clear();
		updatedCaseRefs.clear();

		try
		{
			deployWithModification((dm) -> {
				Attribute aFixedPointNumber = dm.getStructuredTypeByName("Customer").newAttribute();
				aFixedPointNumber.setName("aFixedPointNumber");
				aFixedPointNumber.setLabel("aFixedPointNumber");
				aFixedPointNumber.setIsSearchable(true);
				aFixedPointNumber.setIsSummary(true);
				//decimal places - 5; length - 11
				aFixedPointNumber.setType("base:FixedPointNumber");
				aFixedPointNumber.newConstraint("length", "11");
				//min value : negative (-123456.78) and max value : negative (-123.45678)
				aFixedPointNumber.newConstraint("minValue", "-123456.78");
				aFixedPointNumber.newConstraint("minValueInclusive", "true");
				aFixedPointNumber.newConstraint("maxValue", "-123.45678");
				aFixedPointNumber.newConstraint("maxValueInclusive", "true");
				aFixedPointNumber.newConstraint("decimalPlaces", "5");
			});

			//create a case with negative -123456.78 update it to -123.45678
			createCase("-123456.78");
			updateCase(caseRefs.get(0), "-123456.78", "-123.45678", true);
			assertRead(updatedCaseRefs.get(0), "-123.45678");

			//update to negative -123456.78 
			updateCase(updatedCaseRefs.get(0), "-123.45678", "-123456.78", true);
			assertRead(updatedCaseRefs.get(1), "-123456.78");

			//update to negative -12234.121 
			updateCase(updatedCaseRefs.get(1), "-123456.78", "-12234.121", true);
			assertRead(updatedCaseRefs.get(2), "-12234.121");

			//update it to -123455.66
			updateCase(updatedCaseRefs.get(2), "-12234.121", "-123455.66", true);
			assertRead(updatedCaseRefs.get(3), "-123455.66");

			//update to -124.2343 
			updateCase(updatedCaseRefs.get(3), "-123455.66", "-124.2343", true);
			assertRead(updatedCaseRefs.get(4), "-124.2343");

			//update it to 123.45677 (update not possible)
			updateCase(updatedCaseRefs.get(4), "-124.2343", "123.45677", false);
			assertRead(updatedCaseRefs.get(4), "-124.2343");

			//update to -123.45679
			updateCase(updatedCaseRefs.get(4), "-124.2343", "-123.45679", true);
			assertRead(updatedCaseRefs.get(5), "-123.45679");

			//update to -123456.79 (update not possible)
			updateCase(updatedCaseRefs.get(5), "-123.45679", "-123456.79", false);
			assertRead(updatedCaseRefs.get(5), "-123.45679");

			//update to positive 123456.77 (update not possible)
			updateCase(updatedCaseRefs.get(5), "-123.45679", "123.45679", false);
			assertRead(updatedCaseRefs.get(5), "-123.45679");

			//update to positive 123.45677 (update not possible)
			updateCase(updatedCaseRefs.get(5), "-123.45679", "-123456.79", false);
			assertRead(updatedCaseRefs.get(5), "-123.45679");

			caseRefs.clear();
			updatedCaseRefs.clear();
		}
		finally
		{
			forceUndeploy(deploymentIdApp);
		}
	}

	//Test to create and update cases when the number has positive min value and max value i.e. maxValue > minValue > 0
	@Test(description = "Test to create and update cases when the number has positive min value and max value i.e. maxValue > minValue > 0")
	public void rangePositiveToPositive()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{
		caseRefs.clear();
		updatedCaseRefs.clear();

		try
		{
			deployWithModification((dm) -> {
				Attribute aFixedPointNumber = dm.getStructuredTypeByName("Customer").newAttribute();
				aFixedPointNumber.setName("aFixedPointNumber");
				aFixedPointNumber.setLabel("aFixedPointNumber");
				aFixedPointNumber.setIsSearchable(true);
				aFixedPointNumber.setIsSummary(true);
				//decimal places - 5; length - 11
				aFixedPointNumber.setType("base:FixedPointNumber");
				aFixedPointNumber.newConstraint("length", "11");
				//max value : positive (123456.78) and min value : 0
				aFixedPointNumber.newConstraint("minValue", "0");
				aFixedPointNumber.newConstraint("minValueInclusive", "true");
				aFixedPointNumber.newConstraint("maxValue", "123456.78");
				aFixedPointNumber.newConstraint("maxValueInclusive", "true");
				aFixedPointNumber.newConstraint("decimalPlaces", "5");
			});

			//create a case with positive 123456.78 update it to 123.45678
			createCase("123456.78");
			updateCase(caseRefs.get(0), "123456.78", "123.45678", true);
			assertRead(updatedCaseRefs.get(0), "123.45678");

			//update it to 123456.78
			updateCase(updatedCaseRefs.get(0), "123.45678", "123456.78", true);
			assertRead(updatedCaseRefs.get(1), "123456.78");

			//update to positive 12234.121 
			updateCase(updatedCaseRefs.get(1), "123456.78", "12234.121", true);
			assertRead(updatedCaseRefs.get(2), "12234.121");

			//update it to 123455.66
			updateCase(updatedCaseRefs.get(2), "12234.121", "123455.66", true);
			assertRead(updatedCaseRefs.get(3), "123455.66");

			//update to positive 11.2343
			updateCase(updatedCaseRefs.get(3), "123455.66", "11.2343", true);
			assertRead(updatedCaseRefs.get(4), "11.2343");

			//update it to 121.3453
			updateCase(updatedCaseRefs.get(4), "11.2343", "121.3453", true);
			assertRead(updatedCaseRefs.get(5), "121.3453");

			//update to positive 98765.345 
			updateCase(updatedCaseRefs.get(5), "121.3453", "98765.345", true);
			assertRead(updatedCaseRefs.get(6), "98765.345");

			//update to 0.1
			updateCase(updatedCaseRefs.get(6), "98765.345", "0.1", true);
			assertRead(updatedCaseRefs.get(7), "0.1");

			//update to 0
			updateCase(updatedCaseRefs.get(7), "0.1", "0", true);
			assertRead(updatedCaseRefs.get(8), "0");

			//update it to -71.34553 (update not possible)
			updateCase(updatedCaseRefs.get(8), "0", "-71.34553", false);
			assertRead(updatedCaseRefs.get(8), "0");

			//update to positive -0.00001 (update not possible)
			updateCase(updatedCaseRefs.get(8), "0", "-0.00001", false);
			assertRead(updatedCaseRefs.get(8), "0");

			//update to positive -123456.78 (update not possible)
			updateCase(updatedCaseRefs.get(8), "0", "-123456.78", false);
			assertRead(updatedCaseRefs.get(8), "0");

			caseRefs.clear();
			updatedCaseRefs.clear();
		}
		finally
		{
			forceUndeploy(deploymentIdApp);
		}
	}

}
