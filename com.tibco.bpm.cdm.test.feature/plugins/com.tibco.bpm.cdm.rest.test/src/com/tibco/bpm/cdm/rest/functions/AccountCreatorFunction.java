package com.tibco.bpm.cdm.rest.functions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.Assert;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBodyItem;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class AccountCreatorFunction extends Utils
{
	private CasesFunctions			caseFunctions			= new CasesFunctions();

	public static Random			randomGenerator			= new Random();

	private Response				response				= null;

	private JsonPath				jsonPath				= null;

	public static ArrayList<String>	accountID				= new ArrayList<String>();

	public static ArrayList<String>	states					= new ArrayList<String>();

	public static ArrayList<String>	accountType				= new ArrayList<String>();

	public static ArrayList<String>	dateofAccountOpening	= new ArrayList<String>();

	public static ArrayList<String>	dateofAccountClosing	= new ArrayList<String>();

	public static ArrayList<String>	timeofAccountOpening	= new ArrayList<String>();

	public static ArrayList<String>	timeofAccountClosing	= new ArrayList<String>();

	public static ArrayList<String>	accountLiabilityHolding	= new ArrayList<String>();

	public static ArrayList<String>	institutionCode			= new ArrayList<String>();

	public static ArrayList<String>	nameoftheInstitution	= new ArrayList<String>();

	public static ArrayList<String>	firstLine				= new ArrayList<String>();

	public static ArrayList<String>	secondLine				= new ArrayList<String>();

	public static ArrayList<String>	city					= new ArrayList<String>();

	public static ArrayList<String>	county					= new ArrayList<String>();

	public static ArrayList<String>	country					= new ArrayList<String>();

	public static ArrayList<String>	postCode				= new ArrayList<String>();

	static
	{
		//populate status
		states.add("@ACTIVE&");
		states.add("@FROZEN&");
		states.add("@WAITING FOR APPROVAL&");
		states.add("@WAITING FOR CANCELLATION&");
		//states.add("@TERMINATED&");
		//states.add("@CANCELLED&");
		states.add("@TREMPORARILY DEACTIVATED&");
		states.add("@REINSTATED&");

		accountType.add("SAVING");
		accountType.add("MORTGAGE");
		accountType.add("CURRENT");
		accountType.add("FIXEDDEPOSIT");
		accountType.add("CASHISA");
		accountType.add("INSURANCE");

		dateofAccountOpening.add("1990-04-26");
		dateofAccountOpening.add("2000-11-23");
		dateofAccountOpening.add("1999-10-22");
		dateofAccountOpening.add("1998-09-21");
		dateofAccountOpening.add("2019-01-01");
		dateofAccountOpening.add("2001-03-03");
		dateofAccountOpening.add("2019-02-14");
		dateofAccountOpening.add("2008-10-10");
		dateofAccountOpening.add("2000-02-29");
		dateofAccountOpening.add("2005-03-08");

		dateofAccountClosing.add("2001-12-24");
		dateofAccountClosing.add("1992-06-01");
		dateofAccountClosing.add("1992-01-07");
		dateofAccountClosing.add("1993-05-26");
		dateofAccountClosing.add("2019-01-05");
		dateofAccountClosing.add("2001-02-04");
		dateofAccountClosing.add("2019-01-05");
		dateofAccountClosing.add("2008-11-12");
		dateofAccountClosing.add("1991-11-21");
		dateofAccountClosing.add("2005-09-10");

		timeofAccountOpening.add("10:10:00");
		timeofAccountOpening.add("01:59:00");
		timeofAccountOpening.add("02:58:00");
		timeofAccountOpening.add("21:21:00");
		timeofAccountOpening.add("23:57:00");
		timeofAccountOpening.add("19:03:00");
		timeofAccountOpening.add("11:11:00");
		timeofAccountOpening.add("03:15:00");
		timeofAccountOpening.add("04:56:00");
		timeofAccountOpening.add("05:28:00");

		timeofAccountClosing.add("22:22:00");
		timeofAccountClosing.add("13:59:00");
		timeofAccountClosing.add("14:58:00");
		timeofAccountClosing.add("09:21:00");
		timeofAccountClosing.add("11:57:00");
		timeofAccountClosing.add("07:03:00");
		timeofAccountClosing.add("23:11:00");
		timeofAccountClosing.add("15:15:00");
		timeofAccountClosing.add("16:56:00");
		timeofAccountClosing.add("17:28:00");

		//institutionCode.add("débâcle!123");
		institutionCode.add("dÃ©bÃ¢cle!123");
		institutionCode.add("Bloggs_345");
		institutionCode.add("B#^^#!21");
		institutionCode.add("Panos+123");
		institutionCode.add("Cains");
		institutionCode.add("Lawrence%57798");
		institutionCode.add("Mathis2565");
		institutionCode.add("Davidson3632");
		institutionCode.add("Silvijo352345");
		institutionCode.add("Pearce657456");

		nameoftheInstitution.add("ORACLE");
		nameoftheInstitution.add("BLOOMBERG");
		nameoftheInstitution.add("BURBERY");
		nameoftheInstitution.add("PANOSANIC");
		nameoftheInstitution.add("CAINES");
		nameoftheInstitution.add("F-LEX LEGAL");
		nameoftheInstitution.add("MARKS & SPENCERS");
		nameoftheInstitution.add("DAWOOD");
		nameoftheInstitution.add("SANTADER");
		nameoftheInstitution.add("PUMA");

		firstLine.add("ORACLE funds");
		firstLine.add("BLOOMBERG PLC");
		firstLine.add("BURBERY HQ");
		firstLine.add("The PANOSANIC");
		firstLine.add("CAINES Funds");
		firstLine.add("F-LEX LEGAL System");
		firstLine.add("MARKS & SPENCERS HQ");
		firstLine.add("DAWOOD Shop");
		firstLine.add("SANTADER Bank");
		firstLine.add("PUMA Store");

		secondLine.add("9, CURTIS STREET");
		secondLine.add("1, FARSNBY STREET");
		secondLine.add("20, LONDON STREET");
		secondLine.add("12, BRISTOL AVANUE");
		secondLine.add("21, BAKER STREET");
		secondLine.add("10, DOWNING STREET");
		secondLine.add("3, BATH ROAD");
		secondLine.add("26, MANCHESTER STREET");
		secondLine.add("5, UNITED NATIONS STREET");
		secondLine.add("19, BABAR LANE");

		city.add("SWINDON");
		city.add("THE BATH SPA");
		city.add("READING");
		city.add("BRISTOL");
		city.add("CARDIFF");
		city.add("LONDON");
		city.add("NEW LONDON");
		city.add("BIRMINGHAM");
		city.add("MUNICH");
		city.add("PALO ALTO");

		county.add("WILTSHIRE");
		county.add("SOMERSET");
		county.add("KENT");
		county.add("GLOUCESTER");
		county.add("DOVER");
		county.add("CORNWALL");
		county.add("YORK");
		county.add("BIRMINGHAM");
		county.add("DISTRICT 29");
		county.add("CALIFORNIA");

		country.add("ENGLAND");
		country.add("UNITED KINGDOM");
		country.add("GREAT BRITAIN");
		country.add("UK");
		country.add("WALES");
		country.add("GLOBAL");
		country.add("ENGLAND");
		country.add("UK");
		country.add("GENRMAY");
		country.add("UNITED STATES");

		postCode.add("SN1 5JU");
		postCode.add("BJ1 2AD");
		postCode.add("AA65 7AJ");
		postCode.add("CA10 4DD");
		postCode.add("DQ16 7HQ");
		postCode.add("SW11 5AN");
		postCode.add("EK1 1EK");
		postCode.add("JR5 6KI");
		postCode.add("10011");
		postCode.add("10001");

	}

	static AtomicInteger number = new AtomicInteger();

	private static String generateAccountJSON(Integer index) throws IOException
	{
		String account = readFileContents("/Casedata/accounts.json");

		String accountStates = states.get(index);
		String accountTypes = accountType.get(index);
		String dateofAccountOpenings = dateofAccountOpening.get(index);
		String dateofAccountClosings = dateofAccountClosing.get(index);
		String timeofAccountOpenings = timeofAccountOpening.get(index);
		String timeofAccountClosings = timeofAccountClosing.get(index);
		String institutionCodes = institutionCode.get(index);
		String nameoftheInstitutions = nameoftheInstitution.get(index);
		String firstLines = firstLine.get(index);
		String secondLines = secondLine.get(index);
		String cities = city.get(index);
		String counties = county.get(index);
		String countries = country.get(index);
		String postCodes = postCode.get(index);

		String content1 = account.replaceAll("ACCOUNTIDTOBEREPLACED", Integer.toString((number.incrementAndGet())));
		String content2 = content1.replaceFirst("STATETOBEREPLACED", accountStates);
		String content3 = content2.replaceFirst("ACCOUNTTYPETOBEREPLACED", accountTypes);
		String content4 = content3.replaceFirst("DATEOFACCOUNTOPENINGTOBEREPLACED", dateofAccountOpenings);
		String content5 = content4.replaceFirst("DATEOFACCOUNTCLOSINGTOBEREPLACED", dateofAccountClosings);
		String content6 = content5.replaceFirst("TIMEOFACCOUNTOPENINGTOBEREPLACED", timeofAccountOpenings);
		String content7 = content6.replaceFirst("TIMEOFACCOUNTCLOSINGTOBEREPLACED", timeofAccountClosings);
		String content8 = content7.replaceFirst("TIMEOFACCOUNTCLOSINGTOBEREPLACED", timeofAccountClosings);
		String content9 = content8.replaceFirst("ACCOUNTLIABILITYHOLDINGTOBEREPLACED",
				Integer.toString((number.incrementAndGet())));
		String content10 = content9.replaceFirst("INSTITUTIONCODETOBEREPLACED", institutionCodes);
		String content11 = content10.replaceFirst("NAMEOFTHEINSTITUTIONTOBEREPLACED", nameoftheInstitutions);
		String content12 = content11.replaceFirst("FIRSTLINETOBEREPLACED", firstLines);
		String content13 = content12.replaceFirst("SECONDLINETOBEREPLACED", secondLines);
		String content14 = content13.replaceFirst("CITYTOBEREPLACED", cities);
		String content15 = content14.replaceFirst("COUNTYTOBEREPLACED", counties);
		String content16 = content15.replaceFirst("COUNTRYTOBEREPLACED", countries);
		String content17 = content16.replaceFirst("POSTCODETOBEREPLACED", postCodes);

		String finalString = Utils.sanitize(content17);

		return finalString;
	}

	private static List<String> generateSequentialAccountJSON(Integer numberOfAccounts, String jsonFileName)
			throws IOException
	{

		List<String> cases = new ArrayList<>();
		cases.clear();

		String account = readFileContents("/Casedata/" + jsonFileName + ".json");

		for (int accountIterrator = 0; accountIterrator < numberOfAccounts; accountIterrator++)
		{
			String content1 = account.replaceFirst("ACCOUNTIDTOBEREPLACED", Integer.toString(accountIterrator + 1));
			String content2 = content1.replaceFirst("STATETOBEREPLACED", states.get(accountIterrator % 6));
			String content3 = content2.replaceFirst("ACCOUNTTYPETOBEREPLACED", accountType.get(accountIterrator % 6));
			String content4 = content3.replaceFirst("DATEOFACCOUNTOPENINGTOBEREPLACED",
					"2011-01-1" + ((accountIterrator % 9) + 1));
			String content5 = content4.replaceFirst("DATEOFACCOUNTCLOSINGTOBEREPLACED",
					"2019-12-2" + ((accountIterrator % 9) + 1));
			String content6 = content5.replaceFirst("TIMEOFACCOUNTOPENINGTOBEREPLACED",
					"11:" + (accountIterrator % 5) + "" + (accountIterrator % 5) + ":20");
			String content7 = content6.replaceFirst("TIMEOFACCOUNTCLOSINGTOBEREPLACED",
					"23:" + (accountIterrator % 6) + "" + (accountIterrator % 6) + ":30");
			String content8 = content7.replaceFirst("ACCOUNTLIABILITYHOLDINGTOBEREPLACED",
					Integer.toString(accountIterrator + 1));
			String content9 = content8.replaceFirst("INSTITUTIONCODETOBEREPLACED",
					"institution code " + accountIterrator);
			String content10 = content9.replaceFirst("NAMEOFTHEINSTITUTIONTOBEREPLACED",
					"instituion name " + accountIterrator);
			String content11 = content10.replaceFirst("FIRSTLINETOBEREPLACED", "first line " + accountIterrator);
			String content12 = content11.replaceFirst("SECONDLINETOBEREPLACED", "second line" + accountIterrator);
			String content13 = content12.replaceFirst("CITYTOBEREPLACED", "city" + accountIterrator);
			String content14 = content13.replaceFirst("COUNTYTOBEREPLACED", "county" + accountIterrator);
			String content15 = content14.replaceFirst("COUNTRYTOBEREPLACED", "country" + accountIterrator);
			String content16 = content15.replaceFirst("POSTCODETOBEREPLACED", "postcode " + accountIterrator);

			if (jsonFileName != "accounts")
			{
				//add the data for the fields for the second version of the application
			}

			String finalString = Utils.sanitize(content16);
			cases.add(finalString);
		}
		return cases;
	}

	private static List<String> updateAccountJSON(Integer numberOfAccounts, String jsonFileName, Integer updateNumber)
			throws IOException
	{
		String account = readFileContents("/Casedata/" + jsonFileName + ".json");
		List<String> cases = new ArrayList<>();
		cases.clear();

		for (int accountIterrator = 0; accountIterrator < numberOfAccounts; accountIterrator++)
		{
			String content1 = account.replaceFirst("ACCOUNTIDTOBEREPLACED", Integer.toString(accountIterrator + 1));
			String content2 = content1.replaceFirst("STATETOBEREPLACED", states.get(5 - (accountIterrator % 6)));
			String content3 = content2.replaceFirst("ACCOUNTTYPETOBEREPLACED",
					accountType.get(5 - (accountIterrator % 6)));
			String content4 = content3.replaceFirst("DATEOFACCOUNTOPENINGTOBEREPLACED",
					"2011-01-2" + ((accountIterrator % 9) + 1));
			String content5 = content4.replaceFirst("DATEOFACCOUNTCLOSINGTOBEREPLACED",
					"2019-12-1" + ((accountIterrator % 9) + 1));
			String content6 = content5.replaceFirst("TIMEOFACCOUNTOPENINGTOBEREPLACED",
					"10:" + (accountIterrator % 5) + "" + (accountIterrator % 5) + ":0" + (accountIterrator % 7));
			String content7 = content6.replaceFirst("TIMEOFACCOUNTCLOSINGTOBEREPLACED",
					"22:" + (accountIterrator % 6) + "" + (accountIterrator % 6) + ":0" + (accountIterrator % 8));
			String content8 = content7.replaceFirst("ACCOUNTLIABILITYHOLDINGTOBEREPLACED",
					Integer.toString(accountIterrator + 1));
			String content9 = content8.replaceFirst("INSTITUTIONCODETOBEREPLACED",
					"institution update " + updateNumber + " code " + accountIterrator);
			String content10 = content9.replaceFirst("NAMEOFTHEINSTITUTIONTOBEREPLACED",
					"instituion update " + updateNumber + " code " + accountIterrator);
			String content11 = content10.replaceFirst("FIRSTLINETOBEREPLACED",
					"update " + updateNumber + " first line " + accountIterrator);
			String content12 = content11.replaceFirst("SECONDLINETOBEREPLACED",
					"second line " + updateNumber + " second line " + accountIterrator);
			String content13 = content12.replaceFirst("CITYTOBEREPLACED",
					"update " + updateNumber + " city " + accountIterrator);
			String content14 = content13.replaceFirst("COUNTYTOBEREPLACED",
					"update " + updateNumber + " county " + accountIterrator);
			String content15 = content14.replaceFirst("COUNTRYTOBEREPLACED",
					"update " + updateNumber + " country " + accountIterrator);
			String content16 = content15.replaceFirst("POSTCODETOBEREPLACED",
					"update " + updateNumber + " postcode " + accountIterrator);

			if (jsonFileName != "accounts")
			{
				//add the data for the fields for the second version of the application
			}
			String finalString = Utils.sanitize(content16);
			cases.add(finalString);
		}

		return cases;
	}

	public List<String> createAccounts() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, InterruptedException
	{
		int numAccounts = 6;

		List<String> cases = new ArrayList<>();
		List<String> refs = new ArrayList<>();
		refs.clear();
		for (int i = 0; i < numAccounts; i++)
		{
			cases.clear();
			cases.add(AccountCreatorFunction.generateAccountJSON(i));
			response = caseFunctions.createCases("com.example.bankdatamodel.Account", 1, cases);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			refs.add(jsonPath.getString("[0]"));
		}
		return refs;
	}

	public List<String> createAccountsArrayData(int numberOfAccounts, String jsonFile)
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			DataModelSerializationException, DeploymentException, InternalException, CasedataException,
			ReferenceException, ArgumentException, InterruptedException
	{

		List<String> refs = new ArrayList<>();
		List<String> casedata = new ArrayList<>();
		casedata.clear();
		refs.clear();
		casedata = generateSequentialAccountJSON(numberOfAccounts, jsonFile);

		response = caseFunctions.createCases("com.example.bankdatamodel.Account", 1, casedata);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		for (int i = 0; i < numberOfAccounts; i++)
		{
			refs.add(jsonPath.getString("[" + i + "]"));
		}
		return refs;
	}

	public List<String> updateAccountsArrayData(List<String> caseRefs, int numberOfUpdates, String jsonFile)
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			DataModelSerializationException, DeploymentException, InternalException, CasedataException,
			ReferenceException, ArgumentException, InterruptedException
	{

		List<String> casedata = new ArrayList<>();
		casedata.clear();

		List<String> refs = new ArrayList<>();
		List<String> intermediateRefs = new ArrayList<>();
		//refs.clear();
		//intermediateRefs.clear();
		refs.addAll(caseRefs);
		intermediateRefs.addAll(caseRefs);

		List<String> updatedRefs = new ArrayList<>();
		updatedRefs.clear();

		for (int i = 0; i < numberOfUpdates; i++)
		{
			casedata = AccountCreatorFunction.updateAccountJSON(refs.size(), jsonFile, (i + 1));
			CasesPutRequestBody body = new CasesPutRequestBody();
			for (int casesIterator = 0; casesIterator < refs.size(); casesIterator++)
			{
				//body.clear();
				CasesPutRequestBodyItem payload = new CasesPutRequestBodyItem();
				payload.setCasedata(casedata.get(casesIterator));
				payload.setCaseReference(intermediateRefs.get(casesIterator));
				body.add(payload);
			}
			response = caseFunctions.arrayUpdateCases(body);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			intermediateRefs.clear();
			for (int updatedCasesIterator = 0; updatedCasesIterator < refs.size(); updatedCasesIterator++)
			{
				intermediateRefs.add(jsonPath.getString("[" + updatedCasesIterator + "]"));
			}
		}

		for (int i = 0; i < caseRefs.size(); i++)
		{
			updatedRefs.add(jsonPath.getString("[" + i + "]"));
		}

		return updatedRefs;
	}

	public List<String> updateAccountsArrayData(List<String> caseRefs, int numberOfUpdates, String jsonFile,
			Boolean returnIntermediate) throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			CasedataException, ReferenceException, ArgumentException, InterruptedException
	{

		List<String> casedata = new ArrayList<>();
		casedata.clear();

		List<String> refs = new ArrayList<>();
		List<String> intermediateRefs = new ArrayList<>();

		refs.addAll(caseRefs);
		intermediateRefs.addAll(caseRefs);

		List<String> updatedRefs = new ArrayList<>();
		updatedRefs.clear();

		for (int i = 0; i < numberOfUpdates; i++)
		{
			casedata = AccountCreatorFunction.updateAccountJSON(refs.size(), jsonFile, (i + 1));
			CasesPutRequestBody body = new CasesPutRequestBody();
			for (int casesIterator = 0; casesIterator < refs.size(); casesIterator++)
			{
				//body.clear();
				CasesPutRequestBodyItem payload = new CasesPutRequestBodyItem();
				payload.setCasedata(casedata.get(casesIterator));
				payload.setCaseReference(intermediateRefs.get(casesIterator));
				body.add(payload);
			}
			response = caseFunctions.arrayUpdateCases(body);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			intermediateRefs.clear();
			for (int updatedCasesIterator = 0; updatedCasesIterator < refs.size(); updatedCasesIterator++)
			{
				intermediateRefs.add(jsonPath.getString("[" + updatedCasesIterator + "]"));
				if (returnIntermediate == true)
				{
					updatedRefs.add(jsonPath.getString("[" + updatedCasesIterator + "]"));
				}
			}
		}

		//retun intermediate references
		if (returnIntermediate == true)
		{
			return updatedRefs;
		}

		//return the end result references
		else
		{
			for (int i = 0; i < caseRefs.size(); i++)
			{
				updatedRefs.add(jsonPath.getString("[" + i + "]"));
			}
			return updatedRefs;
		}
	}

	public List<String> createAccountWithDefaultValues(String accountID) throws IOException, URISyntaxException,
			RuntimeApplicationException, PersistenceException, DataModelSerializationException, DeploymentException,
			InternalException, CasedataException, ReferenceException, ArgumentException, InterruptedException
	{

		String pre_casedata = "{\"accountID\":" + accountID
				+ ",\"institutionDetails\":{\"institutionBranchAddress\":{}}}";

		//String pre_casedata = account.replaceAll("ACCOUNTIDTOBEREPLACED", Integer.toString(999999));
		String casedata = Utils.sanitize(pre_casedata);

		List<String> cases = new ArrayList<>();
		List<String> refs = new ArrayList<>();
		cases.clear();
		refs.clear();
		cases.add(casedata);
		response = caseFunctions.createCases("com.example.bankdatamodel.Account", 1, cases);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(response.asString()); // useful for debugging
		System.out.println(jsonPath.getString("")); // useful for debugging
		refs.add(jsonPath.getString("[0]"));
		return refs;
	}

	public List<String> createAccountSpecificStateOffsetDefaultCases(Integer offset, Integer numberOfAccounts,
			String state) throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			DataModelSerializationException, DeploymentException, InternalException, CasedataException,
			ReferenceException, ArgumentException, InterruptedException
	{

		List<String> cases = new ArrayList<>();
		List<String> refs = new ArrayList<>();
		cases.clear();
		refs.clear();

		if (state != "" && !(state.equals("")))
		{
			for (int accountIterator = 0; accountIterator < numberOfAccounts; accountIterator++)
			{

				String pre_casedata = "{\"accountID\":" + Integer.toString(accountIterator + offset) + ",\"state\":\""
						+ state + "\"" + ",\"institutionDetails\":{\"institutionBranchAddress\":{}}}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		else if (state.length() == 0 || state.equals(""))
		{
			for (int accountIterator = 0; accountIterator < numberOfAccounts; accountIterator++)
			{

				String pre_casedata = "{\"accountID\":" + Integer.toString(accountIterator + offset)
						+ ",\"institutionDetails\":{\"institutionBranchAddress\":{}}}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		response = caseFunctions.createCases("com.example.bankdatamodel.Account", 1, cases);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(response.asString()); // useful for debugging
		System.out.println(jsonPath.getString("")); // useful for debugging
		for (int caseIterator = 0; caseIterator < numberOfAccounts; caseIterator++)
		{
			refs.add(jsonPath.getString("[" + caseIterator + "]"));
		}
		return refs;
	}

}
