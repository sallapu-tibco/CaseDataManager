package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.se.api.Expression;
import com.tibco.bpm.se.api.Scope;
import com.tibco.bpm.se.api.ScriptEngineService;
import com.tibco.bpm.se.api.ScriptManager;

public class CaseActivityTest extends BaseTest
{

	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	@Autowired
	@Qualifier("scriptEngineService")
	private ScriptEngineService	scriptEngine;

	public static String readFileContents(String fileLocation)
	{

		InputStream inputStream = CaseActivityTest.class.getClassLoader().getResourceAsStream(fileLocation);
		Scanner scanner = new Scanner(inputStream);
		String lineSeparator = System.getProperty("line.separator");

		StringBuilder s = new StringBuilder();

		while (scanner.hasNextLine())
		{

			s.append(scanner.nextLine());
			s.append(lineSeparator);

		}

		scanner.close();

		return s.toString();

	}

	@Test
	public void testCRUDAccountCases()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testscriptcasedata_account/testscriptcasedata_account.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testcasescript.testcasescript.testscriptcasedata_account");
			//fragment 1 to create cases and read cases
			// @formatter:off
			Expression expression = ScriptManager.createExpression("" 
					+""//setting data for first account case
					+"data.accountBOM = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountBOM.accountBalance = 100.12;"
					+"data.accountBOM.accountInstitution = \"HSBC Bank Plc\";"
					+"data.accountBOM.accountOpened = new Date();"
					+"data.accountBOM.accountOpened.setFullYear(1992);"
					+"data.accountBOM.accountOpened.setMonth(0);"
					+"data.accountBOM.accountOpened.setDate(1);"
					+"data.accountBOM.accountOpened.setHours(1);"
					+"data.accountBOM.accountOpened.setMinutes(1);"
					+"data.accountBOM.accountOpened.setSeconds(1);"
					+"data.accountBOM.accountOpened.setMilliseconds(100);"
					+"data.accountBOM.accountLastAccessed = new Date();"
					+"data.accountBOM.accountLastAccessed.setFullYear(2018);"
					+"data.accountBOM.accountLastAccessed.setMonth(0);"
					+"data.accountBOM.accountLastAccessed.setDate(1);"
					+"data.accountBOM.accountLastAccessed.setHours(1);"
					+"data.accountBOM.accountLastAccessed.setMinutes(1);"
					+"data.accountBOM.accountLastAccessed.setSeconds(1);"
					+"data.accountBOM.accountLastAccessed.setMilliseconds(100);"
					+"data.accountBOM.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.ACTIVE;"
					+"data.accountBOM.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+"data.accountBOM.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOM.institutionAddress.firstLine = \"case 1 line 1\";"
					+"data.accountBOM.institutionAddress.secondLine = \"case 1 line 2\";"
					+"data.accountBOM.institutionAddress.city = \"case 1 city 1\";"
					+"data.accountBOM.institutionAddress.county = \"case 1 county 1\";"
					+"data.accountBOM.institutionAddress.country = \"case 1 country 1\";"
					+"data.accountBOM.institutionAddress.postcode = \"SN1 5AP\";"
					+""//create first account case
					+"data.accountCase = bpm.caseData.create(data.accountBOM,'com.example.testcasedatascripting.Account');"
					+""//second case
					+"var account1 = factory.com_example_testcasedatascripting.createAccount();"
					+"account1.accountBalance = 0.12;"
					+"account1.accountInstitution = \"Nutmeg\";"
					+"account1.accountOpened = new Date();"
					+"account1.accountOpened.setFullYear(1993);"
					+"account1.accountOpened.setMonth(0);"
					+"account1.accountOpened.setDate(1);"
					+"account1.accountOpened.setHours(1);"
					+"account1.accountOpened.setMinutes(1);"
					+"account1.accountOpened.setSeconds(1);"
					+"account1.accountOpened.setMilliseconds(100);"
					+"account1.accountLastAccessed = new Date();"
					+"account1.accountLastAccessed.setFullYear(2019);"
					+"account1.accountLastAccessed.setMonth(0);"
					+"account1.accountLastAccessed.setDate(1);"
					+"account1.accountLastAccessed.setHours(1);"
					+"account1.accountLastAccessed.setMinutes(1);"
					+"account1.accountLastAccessed.setSeconds(1);"
					+"account1.accountLastAccessed.setMilliseconds(100);"
					+"account1.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"account1.accountType = pkg.com_example_testdata_outside_global.AccountType.INVESTMENT;"
					+"account1.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account1.institutionAddress.firstLine = \"case 2 line 1\";"
					+"account1.institutionAddress.secondLine = \"case 2 line 2\";"
					+"account1.institutionAddress.city = \"case 2 city 1\";"
					+"account1.institutionAddress.county = \"case 2 county 1\";"
					+"account1.institutionAddress.country = \"case 2 country 1\";"
					+"account1.institutionAddress.postcode = \"SN2 6AP\";"
					+""//third case
					+"var account2 = factory.com_example_testcasedatascripting.createAccount();"
					+"account2.accountBalance = 2000.56;"
					+"account2.accountInstitution = \"Nationwide Building Society\";"
					+"account2.accountOpened = new Date();"
					+"account2.accountOpened.setFullYear(1994);"
					+"account2.accountOpened.setMonth(0);"
					+"account2.accountOpened.setDate(1);"
					+"account2.accountOpened.setHours(1);"
					+"account2.accountOpened.setMinutes(1);"
					+"account2.accountOpened.setSeconds(1);"
					+"account2.accountOpened.setMilliseconds(100);"
					+"account2.accountLastAccessed = new Date();"
					+"account2.accountLastAccessed.setFullYear(2019);"
					+"account2.accountLastAccessed.setMonth(0);"
					+"account2.accountLastAccessed.setDate(1);"
					+"account2.accountLastAccessed.setHours(1);"
					+"account2.accountLastAccessed.setMinutes(1);"
					+"account2.accountLastAccessed.setSeconds(1);"
					+"account2.accountLastAccessed.setMilliseconds(100);"
					+"account2.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"account2.accountType = pkg.com_example_testdata_outside_global.AccountType.FIXEDDEPOSIT;"
					+"account2.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account2.institutionAddress.firstLine = \"case 3 line 1\";"
					+"account2.institutionAddress.secondLine = \"case 3 line 2\";"
					+"account2.institutionAddress.city = \"case 3 city 1\";"
					+"account2.institutionAddress.county = \"case 3 county 1\";"
					+"account2.institutionAddress.country = \"case 3 country 1\";"
					+"account2.institutionAddress.postcode = \"SN3 7AP\";"
					+"data.accountBOMs.push(account1);"
					+"data.accountBOMs.push(account2);"
					+""//create account cases
					//Sicne createAll was not working, this was earlier commented
					//createAll started working after java update to 1.8.0_221
					+"data.accountCases.pushAll(bpm.caseData.createAll(data.accountBOMs,'com.example.testcasedatascripting.Account'));"
					+"bpm.caseData.read(data.accountCase);"
					+""//read single case
					+"data.accountBOMRead = bpm.caseData.read(data.accountCase);"
					+""//read multiple cases
					+"var lengthAll;"
					+"data.accountBOMsRead.pushAll(bpm.caseData.readAll(data.accountCases));"
					+"lengthAll = data.accountBOMsRead.length;" //2
					+""//find by CID
					+""//0th case reference
					+"var accountBOMFindByCID = factory.com_example_testcasedatascripting.createAccount();"
					+"var findByCID = bpm.caseData.findByCaseIdentifier(\"Account00001\",'com.example.testcasedatascripting.Account');"
					+"accountBOMFindByCID = bpm.caseData.read(findByCID);"
					+""//find all
					+"var accountBOMFindAll0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAll1 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAll2 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindAll.length=0;"
					+"data.accountCasesFindAll.pushAll(bpm.caseData.findAll('com.example.testcasedatascripting.Account', 0, 3));"
					+"var lengthFindAll = data.accountCasesFindAll.length;" //3
					+"accountBOMFindAll0 = bpm.caseData.read(data.accountCasesFindAll[0]);"
					+"accountBOMFindAll1 = bpm.caseData.read(data.accountCasesFindAll[1]);"
					+"accountBOMFindAll2 = bpm.caseData.read(data.accountCasesFindAll[2]);"
					+""//find by criteria
					+"var accountBOMFindByCriteria0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindByCriteria1 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindByCriteria.length=0;"
					+"data.accountCasesFindByCriteria.pushAll(bpm.caseData.findByCriteria(\"accountLastAccessed = 2019-01-01T00:01:01.100Z\",'com.example.testcasedatascripting.Account',0,5));"
					+"var lengthFindByCriteria = data.accountCasesFindByCriteria.length;" //2
					+"accountBOMFindByCriteria0 = bpm.caseData.read(data.accountCasesFindByCriteria[0]);"
					+"accountBOMFindByCriteria1 = bpm.caseData.read(data.accountCasesFindByCriteria[1]);"
					+""//find by criteria - paginated
					+"var accountBOMFindByCriteriaPaginated0 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindByCriteriaPaginated.length=0;"
					+"data.accountCasesFindByCriteriaPaginated.pushAll(bpm.caseData.findByCriteria(\"accountLastAccessed = 2019-01-01T00:01:01.100Z\",'com.example.testcasedatascripting.Account',1,1));"
					+"var lengthFindByCriteriaPaginated = data.accountCasesFindByCriteriaPaginated.length;" //1
					+"accountBOMFindByCriteriaPaginated0 = bpm.caseData.read(data.accountCasesFindByCriteriaPaginated[0]);"
					+""//find by criteria - multiple criteria
					+"var accountBOMFindByMultiCriteria = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindByMultiCriteria.length=0;"
					+"data.accountCasesFindByMultiCriteria.pushAll(bpm.caseData.findByCriteria(\"accountLastAccessed = 2019-01-01T00:01:01.100Z and accountInstitution = 'Nutmeg'\",'com.example.testcasedatascripting.Account',0,1));"
					+"var lengthFindByMultiCriteria = data.accountCasesFindByMultiCriteria.length;" //1
					+"accountBOMFindByMultiCriteria = bpm.caseData.read(data.accountCasesFindByMultiCriteria[0]);"
					+""//search
					+"var accountBOMSearch = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesSearch.length=0;"
					+"data.accountCasesSearch.pushAll(bpm.caseData.findBySimpleSearch(\"100.12\",'com.example.testcasedatascripting.Account',0,2));"
					+"var lengthSearch = data.accountCasesSearch.length;" //1
					+"accountBOMSearch = bpm.caseData.read(data.accountCasesSearch[0]);"
					+""//search - paginated
					+"var accountBOMSearchPaginated = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesSearchPaginated.length=0;"
					+"data.accountCasesSearchPaginated.pushAll(bpm.caseData.findBySimpleSearch(\"'DORMANT'\",'com.example.testcasedatascripting.Account',1,1));"
					+"var lengthSearchPaginated = data.accountCasesSearchPaginated.length;" //1
					+"accountBOMSearchPaginated = bpm.caseData.read(data.accountCasesSearchPaginated[0]);"
					+""//create account case with default data					
					+"data.accountBOMDefault = factory.com_example_testcasedatascripting.createAccount();"
					+"var institutionAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOMDefault.institutionAddress = institutionAddressAccount;"
					+"data.accountCaseDefault = bpm.caseData.create(data.accountBOMDefault,'com.example.testcasedatascripting.Account');"
					+"var accountReadDefault = factory.com_example_testcasedatascripting.createAccount();"
					+"accountReadDefault = bpm.caseData.read(data.accountCaseDefault);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			//assert the length of cases found / read
			Object lengthAll = scope.getValue("lengthAll");
			Assert.assertEquals(lengthAll.toString(), "2", "incorrect number of elements");

			Object lengthFindAll = scope.getValue("lengthFindAll");
			Assert.assertEquals(lengthFindAll.toString(), "3", "incorrect number of elements");

			Object lengthFindByCriteria = scope.getValue("lengthFindByCriteria");
			Assert.assertEquals(lengthFindByCriteria.toString(), "2", "incorrect number of elements");

			Object lengthFindByCriteriaPaginated = scope.getValue("lengthFindByCriteriaPaginated");
			Assert.assertEquals(lengthFindByCriteriaPaginated.toString(), "1", "incorrect number of elements");

			Object lengthFindByMultiCriteria = scope.getValue("lengthFindByMultiCriteria");
			Assert.assertEquals(lengthFindByMultiCriteria.toString(), "1", "incorrect number of elements");

			Object lengthSearch = scope.getValue("lengthSearch");
			Assert.assertEquals(lengthSearch.toString(), "1", "incorrect number of elements");

			Object lengthSearchPaginated = scope.getValue("lengthSearchPaginated");
			Assert.assertEquals(lengthSearchPaginated.toString(), "1", "incorrect number of elements");

			//assert casedata of the cases created
			Object accountBOMFindByCID = scope.getValue("accountBOMFindByCID");
			System.out.println("accountBOMFindByCID: " + accountBOMFindByCID.toString());
			Assert.assertEquals(accountBOMFindByCID,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1992-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2018-01-01T00:01:01.100Z\", \"accountBalance\": 100.12, \"accountInstitution\": \"HSBC Bank Plc\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 5AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAll0 = scope.getValue("accountBOMFindAll0");
			System.out.println("accountBOMFindAll0: " + accountBOMFindAll0.toString());
			Assert.assertEquals(accountBOMFindAll0,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1994-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 2000.56, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"FIXEDDEPOSIT\", \"institutionAddress\": {\"firstLine\": \"case 3 line 1\", \"secondLine\": \"case 3 line 2\", \"city\": \"case 3 city 1\", \"county\": \"case 3 county 1\", \"country\": \"case 3 country 1\", \"postcode\": \"SN3 7AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAll1 = scope.getValue("accountBOMFindAll1");
			System.out.println("accountBOMFindAll1: " + accountBOMFindAll1.toString());
			Assert.assertEquals(accountBOMFindAll1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAll2 = scope.getValue("accountBOMFindAll2");
			System.out.println("accountBOMFindAll2: " + accountBOMFindAll2.toString());
			Assert.assertEquals(accountBOMFindAll2,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1992-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2018-01-01T00:01:01.100Z\", \"accountBalance\": 100.12, \"accountInstitution\": \"HSBC Bank Plc\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 5AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByCriteria0 = scope.getValue("accountBOMFindByCriteria0");
			System.out.println("accountBOMFindByCriteria0: " + accountBOMFindByCriteria0.toString());
			Assert.assertEquals(accountBOMFindByCriteria0,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1994-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 2000.56, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"FIXEDDEPOSIT\", \"institutionAddress\": {\"firstLine\": \"case 3 line 1\", \"secondLine\": \"case 3 line 2\", \"city\": \"case 3 city 1\", \"county\": \"case 3 county 1\", \"country\": \"case 3 country 1\", \"postcode\": \"SN3 7AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByCriteria1 = scope.getValue("accountBOMFindByCriteria1");
			System.out.println("accountBOMFindByCriteria1: " + accountBOMFindByCriteria1.toString());
			Assert.assertEquals(accountBOMFindByCriteria1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByCriteriaPaginated0 = scope.getValue("accountBOMFindByCriteriaPaginated0");
			System.out.println("accountBOMFindByCriteriaPaginated0: " + accountBOMFindByCriteriaPaginated0.toString());
			Assert.assertEquals(accountBOMFindByCriteriaPaginated0,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByMultiCriteria = scope.getValue("accountBOMFindByMultiCriteria");
			System.out.println("accountBOMFindByMultiCriteria: " + accountBOMFindByMultiCriteria.toString());
			Assert.assertEquals(accountBOMFindByMultiCriteria,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountBOMSearch = scope.getValue("accountBOMSearch");
			System.out.println("accountBOMSearch: " + accountBOMSearch.toString());
			Assert.assertEquals(accountBOMSearch,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1992-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2018-01-01T00:01:01.100Z\", \"accountBalance\": 100.12, \"accountInstitution\": \"HSBC Bank Plc\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 5AP\"}}",
					"incorrect payload for the object");

			Object accountBOMSearchPaginated = scope.getValue("accountBOMSearchPaginated");
			System.out.println("accountBOMSearchPaginated: " + accountBOMSearchPaginated.toString());
			Assert.assertEquals(accountBOMSearchPaginated,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountReadDefault = scope.getValue("accountReadDefault");
			System.out.println("accountReadDefault: " + accountReadDefault.toString());
			Assert.assertEquals(accountReadDefault,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1999-12-31T23:59:59.999Z\", \"accountLastAccessed\": \"2019-01-01T01:02:03.004Z\", \"accountBalance\": 3000, \"accountInstitution\": \"Default Institution\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"Default First Line\", \"secondLine\": \"Default Second Line\", \"city\": \"Default City\", \"county\": \"Default County\", \"country\": \"Default Country\", \"postcode\": \"DEFAULT\"}}",
					"incorrect payload for the object");

			//print all the case references
			String accountBOMs = (String) result.get("accountBOMs");
			System.out.println("accountBOMs:" + accountBOMs);
			String accountCase = (String) result.get("accountCase");
			System.out.println("accountCase: " + accountCase);
			String accountCases = (String) result.get("accountCases");
			System.out.println("accountCases: " + accountCases);
			String accountBOMRead = (String) result.get("accountBOMRead");
			System.out.println("accountBOMRead: " + accountBOMRead);
			String accountBOMsRead = (String) result.get("accountBOMsRead");
			System.out.println("accountBOMsRead: " + accountBOMsRead);
			String accountCasesFindAll = (String) result.get("accountCasesFindAll");
			System.out.println("accountCasesFindAll:" + accountCasesFindAll);
			String accountCasesFindByCriteria = (String) result.get("accountCasesFindByCriteria");
			System.out.println("accountCasesFindByCriteria:" + accountCasesFindByCriteria);
			String accountCasesFindByCriteriaPaginated = (String) result.get("accountCasesFindByCriteriaPaginated");
			System.out.println("accountCasesFindByCriteriaPaginated: " + accountCasesFindByCriteriaPaginated);
			String accountCasesFindByMultiCriteria = (String) result.get("accountCasesFindByMultiCriteria");
			System.out.println("accountCasesFindByMultiCriteria:" + accountCasesFindByMultiCriteria);
			String accountCasesSearch = (String) result.get("accountCasesSearch");
			System.out.println("accountCasesSearch:" + accountCasesSearch);
			String accountCasesSearchPaginated = (String) result.get("accountCasesSearchPaginated");
			System.out.println("accountCasesSearchPaginated:" + accountCasesSearchPaginated);

			System.out.println("================= Test for update and delete cases ====================");

			//fragment 1 to update and delete cases
			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+""//setting data for first account case to update
					+"data.accountBOMUpdate = bpm.caseData.read(data.accountCaseDefault);"
					+"data.accountBOMUpdate.accountBalance = 3456.67;"
					+"data.accountBOMUpdate.accountInstitution = 'Metro Bank';"
					+""//Temporarily commented - ACE-2330
					+"data.accountBOM.accountOpened = new Date();"
					+"data.accountBOMUpdate.accountOpened.setFullYear(2001);"
					+"data.accountBOMUpdate.accountOpened.setMonth(2);"
					+"data.accountBOMUpdate.accountOpened.setDate(2);"
					+"data.accountBOMUpdate.accountOpened.setHours(2);"
					+"data.accountBOMUpdate.accountOpened.setMinutes(2);"
					+"data.accountBOMUpdate.accountOpened.setSeconds(2);"
					+"data.accountBOMUpdate.accountOpened.setMilliseconds(200);"
					+""//Temporarily commented - ACE-2330
					+"data.accountBOM.accountLastAccessed = new Date();"
					+"data.accountBOMUpdate.accountLastAccessed.setFullYear(2019);"
					+"data.accountBOMUpdate.accountLastAccessed.setMonth(3);"
					+"data.accountBOMUpdate.accountLastAccessed.setDate(3);"
					+"data.accountBOMUpdate.accountLastAccessed.setHours(3);"
					+"data.accountBOMUpdate.accountLastAccessed.setMinutes(3);"
					+"data.accountBOMUpdate.accountLastAccessed.setSeconds(3);"
					+"data.accountBOMUpdate.accountLastAccessed.setMilliseconds(300);"
					+"data.accountBOMUpdate.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"data.accountBOMUpdate.accountType = pkg.com_example_testdata_outside_global.AccountType.INVESTMENT;"
					+"data.accountBOMUpdate.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOMUpdate.institutionAddress.firstLine = \"case 4 update 1 line 1\";"
					+"data.accountBOMUpdate.institutionAddress.secondLine = \"case 4 update 1 line 2\";"
					+"data.accountBOMUpdate.institutionAddress.city = \"case 4 update 1 city 1\";"
					+"data.accountBOMUpdate.institutionAddress.county = \"case 4 update 1 county 1\";"
					+"data.accountBOMUpdate.institutionAddress.country = \"case 4 update 1 country 1\";"
					+"data.accountBOMUpdate.institutionAddress.postcode = \"SN10 11UP\";"
					+""//update account case
					+"data.accountCaseUpdate = bpm.caseData.updateByRef(data.accountCaseDefault, data.accountBOMUpdate);"
					+"var accountReadDefaultUpdated = factory.com_example_testcasedatascripting.createAccount();"
					+"accountReadDefaultUpdated = bpm.caseData.read(data.accountCaseUpdate);"
					+"var accountReadDefaultOlder = factory.com_example_testcasedatascripting.createAccount();"
					+"accountReadDefaultOlder = bpm.caseData.read(data.accountCaseDefault);"
					+""//using copyAll to copy the entire array of account cases
					+"data.accountCasesToUpdate.pushAll(data.accountCases);"
					+"var accountBOMToUpdateArray = new Array();"
					+"accountBOMToUpdateArray = bpm.scriptUtil.copyAll(bpm.caseData.readAll(data.accountCases));"
					+""//read casedata of 1st
					+"var accountBOMToUpdate1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMToUpdate1 = accountBOMToUpdateArray[0];"
					+""//read casedata of 2nd
					+"var accountBOMToUpdate2 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMToUpdate2 = accountBOMToUpdateArray[1];"
					+""//manipulate the bom of 1st
					+"accountBOMToUpdate1.accountBalance = 1234.56;"
					+"accountBOMToUpdate1.accountInstitution = 'Nutmeg Inc.';"
					+"accountBOMToUpdate1.accountOpened = new Date();"
					+"accountBOMToUpdate1.accountOpened.setFullYear(1992);"
					+"accountBOMToUpdate1.accountOpened.setMonth(0);"
					+"accountBOMToUpdate1.accountOpened.setDate(1);"
					+"accountBOMToUpdate1.accountOpened.setHours(1);"
					+"accountBOMToUpdate1.accountOpened.setMinutes(1);"
					+"accountBOMToUpdate1.accountOpened.setSeconds(1);"
					+"accountBOMToUpdate1.accountOpened.setMilliseconds(300);"
					+"accountBOMToUpdate1.accountLastAccessed = new Date();"
					+"accountBOMToUpdate1.accountLastAccessed = new Date();"
					+"accountBOMToUpdate1.accountLastAccessed.setFullYear(2018);"
					+"accountBOMToUpdate1.accountLastAccessed.setMonth(2);"
					+"accountBOMToUpdate1.accountLastAccessed.setDate(2);"
					+"accountBOMToUpdate1.accountLastAccessed.setHours(2);"
					+"accountBOMToUpdate1.accountLastAccessed.setMinutes(2);"
					+"accountBOMToUpdate1.accountLastAccessed.setSeconds(2);"
					+"accountBOMToUpdate1.accountLastAccessed.setMilliseconds(200);"
					+"accountBOMToUpdate1.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"accountBOMToUpdate1.accountType = pkg.com_example_testdata_outside_global.AccountType.INVESTMENT;"
					+"accountBOMToUpdate1.institutionAddress.firstLine = \"case 2 update 1 line 1\";"
					+"accountBOMToUpdate1.institutionAddress.secondLine = \"case 2 update 1 line 2\";"
					+"accountBOMToUpdate1.institutionAddress.city = \"case 2 update 1 city 1\";"
					+"accountBOMToUpdate1.institutionAddress.county = \"case 2 update 1 county 1\";"
					+"accountBOMToUpdate1.institutionAddress.country = \"case 2 update 1 country 1\";"
					+"accountBOMToUpdate1.institutionAddress.postcode = \"SN2 6UP\";"
					+""//manipulate the bom of 2nd
					+"accountBOMToUpdate2.accountBalance = 2000.66;"
					+"accountBOMToUpdate2.accountInstitution = \"Nationwide Building Society (NBS)\";"
					+"accountBOMToUpdate2.accountOpened = new Date();"
					+"accountBOMToUpdate2.accountOpened.setFullYear(1993);"
					+"accountBOMToUpdate2.accountOpened.setMonth(0);"
					+"accountBOMToUpdate2.accountOpened.setDate(1);"
					+"accountBOMToUpdate2.accountOpened.setHours(1);"
					+"accountBOMToUpdate2.accountOpened.setMinutes(1);"
					+"accountBOMToUpdate2.accountOpened.setSeconds(1);"
					+"accountBOMToUpdate2.accountOpened.setMilliseconds(100);"
					+"accountBOMToUpdate2.accountLastAccessed = new Date();"
					+"accountBOMToUpdate2.accountLastAccessed.setFullYear(2020);"
					+"accountBOMToUpdate2.accountLastAccessed.setMonth(10);"
					+"accountBOMToUpdate2.accountLastAccessed.setDate(1);"
					+"accountBOMToUpdate2.accountLastAccessed.setHours(1);"
					+"accountBOMToUpdate2.accountLastAccessed.setMinutes(1);"
					+"accountBOMToUpdate2.accountLastAccessed.setSeconds(1);"
					+"accountBOMToUpdate2.accountLastAccessed.setMilliseconds(100);"
					+"accountBOMToUpdate2.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"accountBOMToUpdate2.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+"accountBOMToUpdate2.institutionAddress.firstLine = \"case 3 update 1 line 1\";"
					+"accountBOMToUpdate2.institutionAddress.secondLine = \"case 3 update 1 line 2\";"
					+"accountBOMToUpdate2.institutionAddress.city = \"case 3 update 1 city 1\";"
					+"accountBOMToUpdate2.institutionAddress.county = \"case 3 update 1 county 1\";"
					+"accountBOMToUpdate2.institutionAddress.country = \"case 3 update 1 country 1\";"
					+"accountBOMToUpdate2.institutionAddress.postcode = \"SN3 7UP\";"
					+""//put the boms into an array of boms
					+"data.accountBOMsToUpdate.length = 0;"
					+"data.accountBOMsToUpdate.push(accountBOMToUpdate1);"
					+"data.accountBOMsToUpdate.push(accountBOMToUpdate2);"
					+""//update account cases
					//Sicne updateAll was not working, this was earlier commented
					//updateAll started working after java update to 1.8.0_221
					+"data.accountCasesAfterUpdate.pushAll(bpm.caseData.updateAllByRef(data.accountCasesToUpdate, data.accountBOMsToUpdate));"
					+""//read case data after update 1
					+"var accountBOMAfterUpdate1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMAfterUpdate1 = bpm.caseData.read(data.accountCasesAfterUpdate[0]);"
					+""//read case data after update 2
					+"var accountBOMAfterUpdate2 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMAfterUpdate2 = bpm.caseData.read(data.accountCasesAfterUpdate[1]);"
					+""//delete non-updated account
					+"bpm.caseData.deleteByRef(data.accountCase);"
					+""//find all
					+""//cases are updated so the new sequence expected is Account00003, Account00002 & Account00004
					+"var accountBOMFindAllAfterUpdateNDelete0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAllAfterUpdateNDelete1 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAllAfterUpdateNDelete2 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindAllAfterUpdateNDelete.length=0;"
					+"data.accountCasesFindAllAfterUpdateNDelete.pushAll(bpm.caseData.findAll('com.example.testcasedatascripting.Account', 0, 4));"
					+"var lengthFindAllUpdateNDelete = data.accountCasesFindAllAfterUpdateNDelete.length;" //3
					+"accountBOMFindAllAfterUpdateNDelete0 = bpm.caseData.read(data.accountCasesFindAllAfterUpdateNDelete[0]);"
					+"accountBOMFindAllAfterUpdateNDelete1 = bpm.caseData.read(data.accountCasesFindAllAfterUpdateNDelete[1]);"
					+"accountBOMFindAllAfterUpdateNDelete2 = bpm.caseData.read(data.accountCasesFindAllAfterUpdateNDelete[2]);"
					+""//delete updated account
					+"bpm.caseData.deleteByRef(data.accountCaseUpdate);"
					+""//find all
					+"var accountBOMFindAllAfterUpdatedDelete0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAllAfterUpdatedDelete1 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountCasesFindAllAfterUpdatedDelete.length=0;"
					+"data.accountCasesFindAllAfterUpdatedDelete.pushAll(bpm.caseData.findAll('com.example.testcasedatascripting.Account', 0, 10));"
					+"var lengthFindAllUpdatedDelete = data.accountCasesFindAllAfterUpdatedDelete.length;" //2
					+"accountBOMFindAllAfterUpdatedDelete0 = bpm.caseData.read(data.accountCasesFindAllAfterUpdatedDelete[0]);"
					+"accountBOMFindAllAfterUpdatedDelete1 = bpm.caseData.read(data.accountCasesFindAllAfterUpdatedDelete[1]);"	
					+ "");
			// @formatter:on

			expression.eval(scope);
			result = scope.getData();

			//assert the length of cases found / read
			Object lengthFindAllUpdateNDelete = scope.getValue("lengthFindAllUpdateNDelete");
			Assert.assertEquals(lengthFindAllUpdateNDelete.toString(), "3", "incorrect number of elements");

			//			Object lengthFindAllUpdatedDelete = scope.getValue("lengthFindAllUpdatedDelete");
			//			Assert.assertEquals(lengthFindAllUpdatedDelete.toString(), "2", "incorrect number of elements");
			//

			//assert casedata of the cases created
			//even if it is older case referenece - it would still give out the latest data since read returns latest casedata
			Object accountReadDefaultOlder = scope.getValue("accountReadDefaultOlder");
			System.out.println("accountReadDefaultOlder: " + accountReadDefaultOlder.toString());
			Assert.assertNotEquals(accountReadDefaultOlder,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1999-12-31T23:59:59.999Z\", \"accountLastAccessed\": \"2019-01-01T01:02:03.004Z\", \"accountBalance\": 3000, \"accountInstitution\": \"Default Institution\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"Default First Line\", \"secondLine\": \"Default Second Line\", \"city\": \"Default City\", \"county\": \"Default County\", \"country\": \"Default Country\", \"postcode\": \"DEFAULT\"}}",
					"incorrect payload for the object");
			Assert.assertEquals(accountReadDefaultOlder,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"2001-03-02T01:02:02.200Z\", \"accountLastAccessed\": \"2019-04-03T02:03:03.300Z\", \"accountBalance\": 3456.67, \"accountInstitution\": \"Metro Bank\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 4 update 1 line 1\", \"secondLine\": \"case 4 update 1 line 2\", \"city\": \"case 4 update 1 city 1\", \"county\": \"case 4 update 1 county 1\", \"country\": \"case 4 update 1 country 1\", \"postcode\": \"SN10 11UP\"}}",
					"incorrect payload for the object");

			Object accountReadDefaultUpdated = scope.getValue("accountReadDefaultUpdated");
			System.out.println("accountReadDefaultUpdated: " + accountReadDefaultUpdated.toString());
			Assert.assertEquals(accountReadDefaultUpdated,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"2001-03-02T01:02:02.200Z\", \"accountLastAccessed\": \"2019-04-03T02:03:03.300Z\", \"accountBalance\": 3456.67, \"accountInstitution\": \"Metro Bank\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 4 update 1 line 1\", \"secondLine\": \"case 4 update 1 line 2\", \"city\": \"case 4 update 1 city 1\", \"county\": \"case 4 update 1 county 1\", \"country\": \"case 4 update 1 country 1\", \"postcode\": \"SN10 11UP\"}}",
					"incorrect payload for the object");

			Object accountBOMToUpdate1 = scope.getValue("accountBOMToUpdate1");
			System.out.println("accountBOMToUpdate1: " + accountBOMFindAll1.toString());
			Assert.assertEquals(accountBOMToUpdate1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-01-01T00:01:01.300Z\", \"accountLastAccessed\": \"2018-03-02T01:02:02.200Z\", \"accountBalance\": 1234.56, \"accountInstitution\": \"Nutmeg Inc.\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 update 1 line 1\", \"secondLine\": \"case 2 update 1 line 2\", \"city\": \"case 2 update 1 city 1\", \"county\": \"case 2 update 1 county 1\", \"country\": \"case 2 update 1 country 1\", \"postcode\": \"SN2 6UP\"}}",
					"incorrect payload for the object");

			Object accountBOMToUpdate2 = scope.getValue("accountBOMToUpdate2");
			System.out.println("accountBOMToUpdate2: " + accountBOMToUpdate2.toString());
			Assert.assertEquals(accountBOMToUpdate2,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2020-11-01T00:01:01.100Z\", \"accountBalance\": 2000.66, \"accountInstitution\": \"Nationwide Building Society (NBS)\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 3 update 1 line 1\", \"secondLine\": \"case 3 update 1 line 2\", \"city\": \"case 3 update 1 city 1\", \"county\": \"case 3 update 1 county 1\", \"country\": \"case 3 update 1 country 1\", \"postcode\": \"SN3 7UP\"}}",
					"incorrect payload for the object");

			Object accountBOMAfterUpdate1 = scope.getValue("accountBOMAfterUpdate1");
			System.out.println("accountBOMAfterUpdate1: " + accountBOMAfterUpdate1.toString());
			Assert.assertEquals(accountBOMAfterUpdate1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-01-01T00:01:01.300Z\", \"accountLastAccessed\": \"2018-03-02T01:02:02.200Z\", \"accountBalance\": 1234.56, \"accountInstitution\": \"Nutmeg Inc.\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 update 1 line 1\", \"secondLine\": \"case 2 update 1 line 2\", \"city\": \"case 2 update 1 city 1\", \"county\": \"case 2 update 1 county 1\", \"country\": \"case 2 update 1 country 1\", \"postcode\": \"SN2 6UP\"}}",
					"incorrect payload for the object");

			Object accountBOMAfterUpdate2 = scope.getValue("accountBOMAfterUpdate2");
			System.out.println("accountBOMAfterUpdate2: " + accountBOMAfterUpdate2.toString());
			Assert.assertEquals(accountBOMAfterUpdate2,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2020-11-01T00:01:01.100Z\", \"accountBalance\": 2000.66, \"accountInstitution\": \"Nationwide Building Society (NBS)\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 3 update 1 line 1\", \"secondLine\": \"case 3 update 1 line 2\", \"city\": \"case 3 update 1 city 1\", \"county\": \"case 3 update 1 county 1\", \"country\": \"case 3 update 1 country 1\", \"postcode\": \"SN3 7UP\"}}",
					"incorrect payload for the object");

			//cases are updated so the new sequence expected is Account00003, Account00002 & Account00004
			Object accountBOMFindAllAfterUpdateNDelete0 = scope.getValue("accountBOMFindAllAfterUpdateNDelete0");
			System.out.println(
					"accountBOMFindAllAfterUpdateNDelete0: " + accountBOMFindAllAfterUpdateNDelete0.toString());
			Assert.assertEquals(accountBOMFindAllAfterUpdateNDelete0,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2020-11-01T00:01:01.100Z\", \"accountBalance\": 2000.66, \"accountInstitution\": \"Nationwide Building Society (NBS)\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 3 update 1 line 1\", \"secondLine\": \"case 3 update 1 line 2\", \"city\": \"case 3 update 1 city 1\", \"county\": \"case 3 update 1 county 1\", \"country\": \"case 3 update 1 country 1\", \"postcode\": \"SN3 7UP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAllAfterUpdateNDelete1 = scope.getValue("accountBOMFindAllAfterUpdateNDelete1");
			System.out.println(
					"accountBOMFindAllAfterUpdateNDelete1: " + accountBOMFindAllAfterUpdateNDelete1.toString());
			Assert.assertEquals(accountBOMFindAllAfterUpdateNDelete1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-01-01T00:01:01.300Z\", \"accountLastAccessed\": \"2018-03-02T01:02:02.200Z\", \"accountBalance\": 1234.56, \"accountInstitution\": \"Nutmeg Inc.\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 update 1 line 1\", \"secondLine\": \"case 2 update 1 line 2\", \"city\": \"case 2 update 1 city 1\", \"county\": \"case 2 update 1 county 1\", \"country\": \"case 2 update 1 country 1\", \"postcode\": \"SN2 6UP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAllAfterUpdateNDelete2 = scope.getValue("accountBOMFindAllAfterUpdateNDelete2");
			System.out.println(
					"accountBOMFindAllAfterUpdateNDelete2: " + accountBOMFindAllAfterUpdateNDelete2.toString());
			Assert.assertEquals(accountBOMFindAllAfterUpdateNDelete2,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"2001-03-02T01:02:02.200Z\", \"accountLastAccessed\": \"2019-04-03T02:03:03.300Z\", \"accountBalance\": 3456.67, \"accountInstitution\": \"Metro Bank\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 4 update 1 line 1\", \"secondLine\": \"case 4 update 1 line 2\", \"city\": \"case 4 update 1 city 1\", \"county\": \"case 4 update 1 county 1\", \"country\": \"case 4 update 1 country 1\", \"postcode\": \"SN10 11UP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAllAfterUpdatedDelete0 = scope.getValue("accountBOMFindAllAfterUpdatedDelete0");
			System.out.println("accountBOMFindAllAfterUpdatedDelete0: " + accountBOMSearchPaginated.toString());
			Assert.assertEquals(accountBOMFindAllAfterUpdatedDelete0,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2020-11-01T00:01:01.100Z\", \"accountBalance\": 2000.66, \"accountInstitution\": \"Nationwide Building Society (NBS)\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 3 update 1 line 1\", \"secondLine\": \"case 3 update 1 line 2\", \"city\": \"case 3 update 1 city 1\", \"county\": \"case 3 update 1 county 1\", \"country\": \"case 3 update 1 country 1\", \"postcode\": \"SN3 7UP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAllAfterUpdatedDelete1 = scope.getValue("accountBOMFindAllAfterUpdatedDelete1");
			System.out.println(
					"accountBOMFindAllAfterUpdatedDelete1: " + accountBOMFindAllAfterUpdatedDelete1.toString());
			Assert.assertEquals(accountBOMFindAllAfterUpdatedDelete1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-01-01T00:01:01.300Z\", \"accountLastAccessed\": \"2018-03-02T01:02:02.200Z\", \"accountBalance\": 1234.56, \"accountInstitution\": \"Nutmeg Inc.\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 update 1 line 1\", \"secondLine\": \"case 2 update 1 line 2\", \"city\": \"case 2 update 1 city 1\", \"county\": \"case 2 update 1 county 1\", \"country\": \"case 2 update 1 country 1\", \"postcode\": \"SN2 6UP\"}}",
					"incorrect payload for the object");

			//print all the case references
			String accountCaseUpdate = (String) result.get("accountCaseUpdate");
			System.out.println("accountCaseUpdate:" + accountCaseUpdate);
			String accountCasesToUpdate = (String) result.get("accountCasesToUpdate");
			System.out.println("accountCasesToUpdate: " + accountCasesToUpdate);
			String accountCasesAfterUpdate = (String) result.get("accountCasesAfterUpdate");
			System.out.println("accountCasesAfterUpdate: " + accountCasesAfterUpdate);
			String accountBOMUpdate = (String) result.get("accountBOMUpdate");
			System.out.println("accountBOMUpdate: " + accountBOMUpdate);
			accountCases = (String) result.get("accountCases");
			System.out.println("accountCases: " + accountCases);
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
	// The test needs to be updated to cope with this.
	// --------------------------------------------------------------------------------------------------
	//@Test
	public void tesLinkUnlinkNavigate()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testscriptcasedata_link_unlink/testscriptcasedata_link_unlink.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink");
			// @formatter:off
			Expression expression = ScriptManager.createExpression("" 
					+""//0th account
					+"data.accountBOM = factory.com_example_testcasedatascripting.createAccount();"
					+"data.accountBOM.accountBalance = 100.12;"
					+"data.accountBOM.accountInstitution = \"HSBC Bank Plc\";"
					+""//data.accountBOM.accountOpened = new Date().now();"
					+""//data.accountBOM.accountOpened = new Date('1992-01-01');"
					+""//Temporarily commented - ACE-2330
					+"data.accountBOM.accountOpened = new Date();"
					+"data.accountBOM.accountOpened.setFullYear(1992);"
					+"data.accountBOM.accountOpened.setMonth(0);"
					+"data.accountBOM.accountOpened.setDate(1);"
					+"data.accountBOM.accountOpened.setHours(1);"
					+"data.accountBOM.accountOpened.setMinutes(1);"
					+"data.accountBOM.accountOpened.setSeconds(1);"
					+"data.accountBOM.accountOpened.setMilliseconds(100);"
					+""//Temporarily commented - ACE-2330
					+"data.accountBOM.accountLastAccessed = new Date();"
					+"data.accountBOM.accountLastAccessed.setFullYear(2018);"
					+"data.accountBOM.accountLastAccessed.setMonth(0);"
					+"data.accountBOM.accountLastAccessed.setDate(1);"
					+"data.accountBOM.accountLastAccessed.setHours(1);"
					+"data.accountBOM.accountLastAccessed.setMinutes(1);"
					+"data.accountBOM.accountLastAccessed.setSeconds(1);"
					+"data.accountBOM.accountLastAccessed.setMilliseconds(100);"
					+"data.accountBOM.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.ACTIVE;"
					+"data.accountBOM.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+"data.accountBOM.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOM.institutionAddress.firstLine = \"case 1 line 1\";"
					+"data.accountBOM.institutionAddress.secondLine = \"case 1 line 2\";"
					+"data.accountBOM.institutionAddress.city = \"case 1 city 1\";"
					+"data.accountBOM.institutionAddress.county = \"case 1 county 1\";"
					+"data.accountBOM.institutionAddress.country = \"case 1 country 1\";"
					+"data.accountBOM.institutionAddress.postcode = \"SN1 5AP\";"
					+"data.accountCase = bpm.caseData.create(data.accountBOM,'com.example.testcasedatascripting.Account');"
					+""//1st case
					+"var account1 = factory.com_example_testcasedatascripting.createAccount();"
					+"account1.accountBalance = 0.12;"
					+"account1.accountInstitution = \"Nutmeg\";"
					+"account1.accountOpened = new Date();"
					+"account1.accountOpened.setFullYear(1993);"
					+"account1.accountOpened.setMonth(0);"
					+"account1.accountOpened.setDate(1);"
					+"account1.accountOpened.setHours(1);"
					+"account1.accountOpened.setMinutes(1);"
					+"account1.accountOpened.setSeconds(1);"
					+"account1.accountOpened.setMilliseconds(100);"
					+"account1.accountLastAccessed = new Date();"
					+"account1.accountLastAccessed.setFullYear(2019);"
					+"account1.accountLastAccessed.setMonth(0);"
					+"account1.accountLastAccessed.setDate(1);"
					+"account1.accountLastAccessed.setHours(1);"
					+"account1.accountLastAccessed.setMinutes(1);"
					+"account1.accountLastAccessed.setSeconds(1);"
					+"account1.accountLastAccessed.setMilliseconds(100);"
					+"account1.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"account1.accountType = pkg.com_example_testdata_outside_global.AccountType.INVESTMENT;"
					+"account1.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account1.institutionAddress.firstLine = \"case 2 line 1\";"
					+"account1.institutionAddress.secondLine = \"case 2 line 2\";"
					+"account1.institutionAddress.city = \"case 2 city 1\";"
					+"account1.institutionAddress.county = \"case 2 county 1\";"
					+"account1.institutionAddress.country = \"case 2 country 1\";"
					+"account1.institutionAddress.postcode = \"SN2 6AP\";"
					+""//2nd case
					+"var account2 = factory.com_example_testcasedatascripting.createAccount();"
					+"account2.accountBalance = 2000.56;"
					+"account2.accountInstitution = \"Nationwide Building Society\";"
					+"account2.accountOpened = new Date();"
					+"account2.accountOpened.setFullYear(1994);"
					+"account2.accountOpened.setMonth(0);"
					+"account2.accountOpened.setDate(1);"
					+"account2.accountOpened.setHours(1);"
					+"account2.accountOpened.setMinutes(1);"
					+"account2.accountOpened.setSeconds(1);"
					+"account2.accountOpened.setMilliseconds(100);"
					+"account2.accountLastAccessed = new Date();"
					+"account2.accountLastAccessed.setFullYear(2019);"
					+"account2.accountLastAccessed.setMonth(0);"
					+"account2.accountLastAccessed.setDate(1);"
					+"account2.accountLastAccessed.setHours(1);"
					+"account2.accountLastAccessed.setMinutes(1);"
					+"account2.accountLastAccessed.setSeconds(1);"
					+"account2.accountLastAccessed.setMilliseconds(100);"
					+"account2.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"account2.accountType = pkg.com_example_testdata_outside_global.AccountType.FIXEDDEPOSIT;"
					+"account2.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account2.institutionAddress.firstLine = \"case 3 line 1\";"
					+"account2.institutionAddress.secondLine = \"case 3 line 2\";"
					+"account2.institutionAddress.city = \"case 3 city 1\";"
					+"account2.institutionAddress.county = \"case 3 county 1\";"
					+"account2.institutionAddress.country = \"case 3 country 1\";"
					+"account2.institutionAddress.postcode = \"SN3 7AP\";"
					+""//3rd case
					+"var account3 = factory.com_example_testcasedatascripting.createAccount();"
					+"account3.accountBalance = 546.56;"
					+"account3.accountInstitution = \"Nutmeg\";"
					+"account3.accountOpened = new Date();"
					+"account3.accountOpened.setFullYear(1995);"
					+"account3.accountOpened.setMonth(0);"
					+"account3.accountOpened.setDate(1);"
					+"account3.accountOpened.setHours(1);"
					+"account3.accountOpened.setMinutes(1);"
					+"account3.accountOpened.setSeconds(1);"
					+"account3.accountOpened.setMilliseconds(100);"
					+"account3.accountLastAccessed = new Date();"
					+"account3.accountLastAccessed.setFullYear(2019);"
					+"account3.accountLastAccessed.setMonth(0);"
					+"account3.accountLastAccessed.setDate(1);"
					+"account3.accountLastAccessed.setHours(1);"
					+"account3.accountLastAccessed.setMinutes(1);"
					+"account3.accountLastAccessed.setSeconds(1);"
					+"account3.accountLastAccessed.setMilliseconds(100);"
					+"account3.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.TOBETERMINATED;"
					+"account3.accountType = pkg.com_example_testdata_outside_global.AccountType.CURRENT;"
					+"account3.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account3.institutionAddress.firstLine = \"case 4 line 1\";"
					+"account3.institutionAddress.secondLine = \"case 4 line 2\";"
					+"account3.institutionAddress.city = \"case 4 city 1\";"
					+"account3.institutionAddress.county = \"case 4 county 1\";"
					+"account3.institutionAddress.country = \"case 4 country 1\";"
					+"account3.institutionAddress.postcode = \"SN5 8AP\";"
					+"data.accountBOMs.push(account1, account2, account3);"
					//Sicne createAll was not working, this was earlier commented
					//createAll started working after java update to 1.8.0_221
					+"data.accountCases.pushAll(bpm.caseData.createAll(data.accountBOMs,'com.example.testcasedatascripting.Account'));"
					+""//0th customer
					+"data.customerBOM = factory.com_example_testcasedatascripting.createCustomer();"
					+"data.customerBOM.customerID = 'ID0001';"
					+"data.customerBOM.age = 3.25;"
					+"data.customerBOM.dateofBirth = new Date('1989-01-01');"
					+"data.customerBOM.customerCategory = pkg.com_example_testcasedatascripting.CustomerCategory.MEDIUMACTIVITY;"
					+"data.customerBOM.residentialAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"data.customerBOM.residentialAddress.firstLine = 'customer 1 line 1';"
					+"data.customerBOM.residentialAddress.secondLine = 'customer 1 line 2';"
					+"data.customerBOM.residentialAddress.city = 'customer 1 city 1';"
					+"data.customerBOM.residentialAddress.county = 'customer 1 county 1';"
					+"data.customerBOM.residentialAddress.country = 'customer 1 country 1';"
					+"data.customerBOM.residentialAddress.postcode = 'SN1 5AP';"
					+"var employmentDetails1 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"var employmentDetails2 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"employmentDetails1.employeeID = '1001';"
					+"employmentDetails1.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.FULLTIMESALARIED;"
					+"employmentDetails1.numberofyears = 5;"
					+"employmentDetails1.yearlyRenumeration = 12345.12;"
					+"employmentDetails2.employeeID = '1002';"
					+"employmentDetails2.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.PARTTIMESALARIED;"
					+"employmentDetails2.numberofyears = 1;"
					+"employmentDetails2.yearlyRenumeration = 45.12;"
					+"data.customerBOM.employmentDetails.push(employmentDetails1, employmentDetails2);"
					+"data.customerCase = bpm.caseData.create(data.customerBOM,'com.example.testcasedatascripting.Customer');"
					+""//link cases
					+"bpm.caseData.link(data.customerCase, data.accountCase,'accounts');"
					+"bpm.caseData.linkAll(data.customerCase, data.accountCases,'accounts');"
					+""//navigate cases - all
					+"var accountRead0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountRead1 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.navigatedAllAccounts.length=0;"
					+"data.navigatedAllAccounts.pushAll(bpm.caseData.navigateAll(data.customerCase,'accounts',0,2));"
					+"var lengthNavigatedAllPaginated = data.navigatedAllAccounts.length;" //2
					+"accountRead0 = bpm.caseData.read(data.navigatedAllAccounts[0]);"
					+"accountRead1 = bpm.caseData.read(data.navigatedAllAccounts[1]);"
					+""//navigate cases - by criteria
					+"var accountRead2 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountRead3 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.navigatedAccountsByCriteria.length=0;"
					+"data.navigatedAccountsByCriteria.pushAll(bpm.caseData.navigateByCriteria(data.customerCase,'accounts',\"accountLastAccessed = 2019-01-01T00:01:01.100Z and accountInstitution = 'Nutmeg'\", 0, 3));"
					+"var lengthNavigatedAccountsByCriteria = data.navigatedAccountsByCriteria.length;" //2
					+"accountRead2 = bpm.caseData.read(data.navigatedAccountsByCriteria[0]);"
					+"accountRead3 = bpm.caseData.read(data.navigatedAccountsByCriteria[1]);"
					+""//navigate cases - by simple search
					+"var accountRead4 = factory.com_example_testcasedatascripting.createAccount();"
					+"data.navigatedAccountsBySearch.length=0;"					
					+"data.navigatedAccountsBySearch.pushAll(bpm.caseData.navigateBySimpleSearch(data.customerCase,'accounts','FIXEDDEPOSIT', 0, 5));"
					+"var lengthNavigatedAccountsBySearch = data.navigatedAccountsBySearch.length;" //1
					+"accountRead4 = bpm.caseData.read(data.navigatedAccountsBySearch[0]);"
					+""//create subset of account cases
					+"data.accountCasesSubset.length = 0;"
					+"data.accountCasesSubset.push(data.accountCases[0]);"
					+"data.accountCasesSubset.push(data.accountCases[1]);"
					+""//unlink cases
					+"bpm.caseData.unlink(data.customerCase, data.accountCase,'accounts');"
					+"bpm.caseData.unlinkAll(data.customerCase, data.accountCasesSubset,'accounts');"
					+"bpm.caseData.unlinkAllByLinkName(data.customerCase, 'accounts');"
					+""//verify unlink by performing navigate all
					+"data.navigateUnlinkedAccounts.length = 0;"
					+"data.navigateUnlinkedAccounts.pushAll(bpm.caseData.navigateAll(data.customerCase,'accounts',0,2));"
					+"var lengthNavigateUnlinkedAccount = data.navigateUnlinkedAccounts.length;" //0
					+"data.navigateUnlinkedCustomer.length = 0;"
					+"data.navigateUnlinkedCustomer.pushAll(bpm.caseData.navigateAll(data.accountCase,'customer',0,2));"
					+"for(var i=0; i<data.accountCases.length; i++)"
					+"{"
					+"	data.navigateUnlinkedCustomer.pushAll(bpm.caseData.navigateAll(data.accountCases[i],'customer',0,2));"
					+"}"
					+"var lengthNavigateUnlinkedCustomer = data.navigateUnlinkedCustomer.length;" //0
					+ "");
			// @formatter:on
			expression.eval(scope);
			scope.getData();

			//assert the length of cases found / read
			Object lengthNavigatedAllPaginated = scope.getValue("lengthNavigatedAllPaginated");
			Assert.assertEquals(lengthNavigatedAllPaginated.toString(), "2", "incorrect number of elements");

			Object lengthNavigatedAccountsByCriteria = scope.getValue("lengthNavigatedAccountsByCriteria");
			Assert.assertEquals(lengthNavigatedAccountsByCriteria.toString(), "2", "incorrect number of elements");

			Object lengthNavigatedAccountsBySearch = scope.getValue("lengthNavigatedAccountsBySearch");
			Assert.assertEquals(lengthNavigatedAccountsBySearch.toString(), "1", "incorrect number of elements");

			Object lengthNavigateUnlinkedAccount = scope.getValue("lengthNavigateUnlinkedAccount");
			Assert.assertEquals(lengthNavigateUnlinkedAccount.toString(), "0", "incorrect number of elements");

			Object lengthNavigateUnlinkedCustomer = scope.getValue("lengthNavigateUnlinkedCustomer");
			Assert.assertEquals(lengthNavigateUnlinkedCustomer.toString(), "0", "incorrect number of elements");

			//assert casedata of the cases linked
			Object accountRead0 = scope.getValue("accountRead0");
			System.out.println("accountRead0: " + accountRead0.toString());
			Assert.assertEquals(accountRead0,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1992-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2018-01-01T00:01:01.100Z\", \"accountBalance\": 100.12, \"accountInstitution\": \"HSBC Bank Plc\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 5AP\"}}",
					"incorrect payload for the object");

			Object accountRead1 = scope.getValue("accountRead1");
			System.out.println("accountRead1: " + accountRead1.toString());
			Assert.assertEquals(accountRead1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountRead2 = scope.getValue("accountRead2");
			System.out.println("accountRead2: " + accountRead2.toString());
			Assert.assertEquals(accountRead2,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1993-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 0.12, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 6AP\"}}",
					"incorrect payload for the object");

			Object accountRead3 = scope.getValue("accountRead3");
			System.out.println("accountRead3: " + accountRead3.toString());
			Assert.assertEquals(accountRead3,
					"{\"accountId\": \"Account00004\", \"accountStatus\": \"TOBETERMINATED\", \"accountOpened\": \"1995-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 546.56, \"accountInstitution\": \"Nutmeg\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 4 line 1\", \"secondLine\": \"case 4 line 2\", \"city\": \"case 4 city 1\", \"county\": \"case 4 county 1\", \"country\": \"case 4 country 1\", \"postcode\": \"SN5 8AP\"}}",
					"incorrect payload for the object");

			Object accountRead4 = scope.getValue("accountRead4");
			System.out.println("accountRead4: " + accountRead4.toString());
			Assert.assertEquals(accountRead4,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1994-01-01T00:01:01.100Z\", \"accountLastAccessed\": \"2019-01-01T00:01:01.100Z\", \"accountBalance\": 2000.56, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"FIXEDDEPOSIT\", \"institutionAddress\": {\"firstLine\": \"case 3 line 1\", \"secondLine\": \"case 3 line 2\", \"city\": \"case 3 city 1\", \"county\": \"case 3 county 1\", \"country\": \"case 3 country 1\", \"postcode\": \"SN3 7AP\"}}",
					"incorrect payload for the object");
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void testCreateDefaultData()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testscriptcasedata_default_data/testscriptcasedata_default_data.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testcasescript.testcasescript.testscriptcasedata_default_data");
			// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+"data.accountBOM = factory.com_example_testcasedatascripting.createAccount();"
					+"var institutionAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOM.institutionAddress = institutionAddressAccount;"
					+"data.customerBOM = factory.com_example_testcasedatascripting.createCustomer();"
					+"var employmentDetails = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"data.customerBOM.employmentDetails.push(employmentDetails);"
					+"var residentialAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.customerBOM.residentialAddress = residentialAddressAccount;"
					+"data.accountCase = bpm.caseData.create(data.accountBOM,'com.example.testcasedatascripting.Account');"
					+"data.customerCase = bpm.caseData.create(data.customerBOM,'com.example.testcasedatascripting.Customer');"
					+"var readAccount = factory.com_example_testcasedatascripting.createAccount();"
					+"readAccount = bpm.caseData.read(data.accountCase);"
					+"var readCustomer = factory.com_example_testcasedatascripting.createCustomer();"
					+"readCustomer = bpm.caseData.read(data.customerCase);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			//assert casedata of the cases created
			Object readAccount = scope.getValue("readAccount");
			System.out.println("readAccount: " + readAccount.toString());
			Assert.assertEquals(readAccount,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1999-12-31T23:59:59.999Z\", \"accountLastAccessed\": \"2019-01-01T01:02:03.004Z\", \"accountBalance\": 3000, \"accountInstitution\": \"Default Institution\", \"accountType\": \"INVESTMENT\", \"institutionAddress\": {\"firstLine\": \"Default First Line\", \"secondLine\": \"Default Second Line\", \"city\": \"Default City\", \"county\": \"Default County\", \"country\": \"Default Country\", \"postcode\": \"DEFAULT\"}}",
					"incorrect payload for the object");

			Object readCustomer = scope.getValue("readCustomer");
			System.out.println("readCustomer: " + readCustomer.toString());
			Assert.assertEquals(readCustomer,
					"{\"customerID\": \"Customer01\", \"customerCategory\": \"LOWACTIVITY\", \"dateofBirth\": \"1993-08-01\", \"age\": 26.25, \"totalIncome\": 40000, \"residentialAddress\": {\"firstLine\": \"Default First Line\", \"secondLine\": \"Default Second Line\", \"city\": \"Default City\", \"county\": \"Default County\", \"country\": \"Default Country\", \"postcode\": \"DEFAULT\"}, \"employmentDetails\": [{\"numberofyears\": 2, \"yearlyRenumeration\": 15000.6, \"employmentStatus\": \"FULLTIMESALARIED\", \"employeeID\": \"DEFAULT EMP ID\"}]}",
					"incorrect payload for the object");

			//print all the case references
			String accountCase = (String) result.get("accountCase");
			System.out.println("accountCase: " + accountCase);
			String customerCase = (String) result.get("customerCase");
			System.out.println("customerCase: " + customerCase);
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void testCaseOutOfSyncException()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			InternalException, PersistenceException, DeploymentException, ScriptException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testscriptcasedata_case_out_of_sync/testscriptcasedata_case_out_of_sync.js");

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Scope scope = null;
		String accountCase = "";
		String accountCaseUpdate1 = "";

		Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
		businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
		List<String> descriptors = new ArrayList<String>();
		descriptors.add(variableDescriptor);
		scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
		scope.setData(data, "com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync");
		// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+""//case creation with default data
					+"data.accountBOM = factory.com_example_testcasedatascripting.createAccount();"
					+"var institutionAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.accountBOM.institutionAddress = institutionAddressAccount;"
					+"data.accountCase = bpm.caseData.create(data.accountBOM,'com.example.testcasedatascripting.Account');"
					+"data.accountBOMRead1 = bpm.caseData.read(data.accountCase);"
					+""//change in the balance field - minor update
					+"data.accountBOMRead1.accountBalance = 10000;"
					+"data.accountCaseUpdate1 = bpm.caseData.updateByRef(data.accountCase, data.accountBOMRead1);"
					+ "");
			// @formatter:on
		try
		{

			expression.eval(scope);
			result = scope.getData();

			//assert casedata of the cases created
			String accountBOMRead1 = (String) result.get("accountBOMRead1");
			System.out.println("accountBOMRead1: " + accountBOMRead1.toString());
			Assert.assertEquals(accountBOMRead1,
					"{\"accountId\":\"Account00001\",\"accountStatus\":\"ACTIVE\",\"accountOpened\":\"1999-12-31T22:59:59.999Z\",\"accountLastAccessed\":\"2019-01-01T00:02:03.004Z\",\"accountBalance\":10000,\"accountInstitution\":\"Default Institution\",\"accountType\":\"INVESTMENT\",\"institutionAddress\":{\"firstLine\":\"Default First Line\",\"secondLine\":\"Default Second Line\",\"city\":\"Default City\",\"county\":\"Default County\",\"country\":\"Default Country\",\"postcode\":\"DEFAULT\"}}",
					"incorrect payload for the object");

			//print all the case references
			accountCase = (String) result.get("accountCase");
			System.out.println("accountCase: " + accountCase);

			accountCaseUpdate1 = (String) result.get("accountCaseUpdate1");
			System.out.println("accountCaseUpdate1: " + accountCaseUpdate1);

			System.out.println("================= Test to catch Case Out Of Sync Exception ====================");

			try
			{

			// @formatter:off
			expression = ScriptManager.createExpression(""
					+""//read updated case
					+"data.accountBOMRead2 = bpm.caseData.read(data.accountCaseUpdate1);"
					+""//change in the balance field - minor update
					+"data.accountBOMRead2.accountBalance = 0.10;"
					+"data.accountCaseUpdate2 = bpm.caseData.updateByRef(data.accountCase, data.accountBOMRead2);"
					+ "");
			// @formatter:on
				expression.eval(scope);
				result = scope.getData();
			}
			catch (ScriptException e)
			{
				e.getStackTrace().toString();
				System.out.println(e.getMessage());
				Assert.assertEquals(e.getMessage(),
						"com.tibco.bpm.cdm.api.exception.CaseOutOfSyncException: Version in case reference (0) mismatches actual version (1): "
								+ accountCase);
			}
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void testCaseNonUniqueCIDException()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			InternalException, PersistenceException, DeploymentException, ScriptException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testscriptcasedata_non_unique_cid/testscriptcasedata_non_unique_cid.js");

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Scope scope = null;
		String customerCase = "";

		Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
		businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
		List<String> descriptors = new ArrayList<String>();
		descriptors.add(variableDescriptor);
		scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
		scope.setData(data, "com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid");
		// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+""//create data for default customer
					+"data.customerBOM = factory.com_example_testcasedatascripting.createCustomer();"
					+"var employmentDetails = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"data.customerBOM.employmentDetails.push(employmentDetails);"
					+"var residentialAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.customerBOM.residentialAddress = residentialAddressAccount;"
					+"data.customerBOM.customerID = \"customer02\";"
					+""//create customer
					+"data.customerCase = bpm.caseData.create(data.customerBOM,'com.example.testcasedatascripting.Customer');"
					+""//create data for default customer with duplicate customer id
					+"data.customerRead1 = factory.com_example_testcasedatascripting.createCustomer();"
					+"data.customerRead1 = bpm.caseData.read(data.customerCase);"
					+"data.customerBOMDuplicate = factory.com_example_testcasedatascripting.createCustomer();"
					+"var employmentDetails = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"data.customerBOMDuplicate.employmentDetails.push(employmentDetails);"
					+"var residentialAddressAccount = factory.com_example_testcasedatascripting.createAddress();"
					+"data.customerBOMDuplicate.residentialAddress = residentialAddressAccount;"
					+"data.customerBOMDuplicate.customerID = \"customer02\";"
					+ "");
			// @formatter:on
		try
		{

			expression.eval(scope);
			result = scope.getData();

			//assert casedata of the cases created
			String customerBOM = (String) result.get("customerBOM");
			System.out.println("customerBOM: " + customerBOM.toString());
			Assert.assertEquals(customerBOM,
					"{\"customerID\":\"customer02\",\"customerCategory\":\"LOWACTIVITY\",\"dateofBirth\":\"1993-08-01\",\"age\":26.25,\"totalIncome\":40000,\"residentialAddress\":{\"firstLine\":\"Default First Line\",\"secondLine\":\"Default Second Line\",\"city\":\"Default City\",\"county\":\"Default County\",\"country\":\"Default Country\",\"postcode\":\"DEFAULT\"},\"employmentDetails\":[{\"numberofyears\":2,\"yearlyRenumeration\":15000.6,\"employmentStatus\":\"FULLTIMESALARIED\",\"employeeID\":\"DEFAULT EMP ID\"}]}",
					"incorrect payload for the object");

			String customerBOMDuplicate = (String) result.get("customerBOMDuplicate");
			System.out.println("customerBOMDuplicate: " + customerBOMDuplicate.toString());
			Assert.assertEquals(customerBOMDuplicate,
					"{\"customerID\":\"customer02\",\"customerCategory\":\"LOWACTIVITY\",\"dateofBirth\":\"1993-08-01\",\"age\":26.25,\"totalIncome\":40000,\"residentialAddress\":{\"firstLine\":\"Default First Line\",\"secondLine\":\"Default Second Line\",\"city\":\"Default City\",\"county\":\"Default County\",\"country\":\"Default Country\",\"postcode\":\"DEFAULT\"},\"employmentDetails\":[{\"numberofyears\":2,\"yearlyRenumeration\":15000.6,\"employmentStatus\":\"FULLTIMESALARIED\",\"employeeID\":\"DEFAULT EMP ID\"}]}",
					"incorrect payload for the object");

			//print all the case references
			customerCase = (String) result.get("customerCase");
			System.out.println("customerCase: " + customerCase);

			System.out.println("================= Test to catch non Unique CID Exception ====================");

			try
			{
			// @formatter:off
			expression = ScriptManager.createExpression(""
					+""//create customer
					+"data.customerCase = bpm.caseData.create(data.customerBOMDuplicate,'com.example.testcasedatascripting.Customer');"
					+ "");
			// @formatter:on
				expression.eval(scope);
				result = scope.getData();
			}
			catch (ScriptException e)
			{
				e.getStackTrace().toString();
				System.out.println(e.getMessage());
				Assert.assertEquals(e.getMessage(),
						"com.tibco.bpm.cdm.api.exception.NonUniqueCaseIdentifierException: Case identifier is not unique: customer02");
			}
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void testACE2449()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/ace_2449_data/Exports/Deployment Artifacts/ace_2449_data.rasc");
		String variableDescriptor1 = readFileContents(
				"apps/scripting-tests/ace_2449_process/Exports/Deployment Artifacts/process-js/ace_2449_process/createcasedifferentbom/createcasedifferentbom.js");
		String variableDescriptor2 = readFileContents(
				"apps/scripting-tests/ace_2449_process/Exports/Deployment Artifacts/process-js/ace_2449_process/createcasedifferentapplication/createcasedifferentapplication.js");
		String variableDescriptor3 = readFileContents(
				"apps/scripting-tests/ace_2449_process/Exports/Deployment Artifacts/process-js/ace_2449_process/createcasedifferentapplicationdifferentbom/createcasedifferentapplicationdifferentbom.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.ace_2449_data", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor1);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.ace_2449_process.ace_2449_process.createcasedifferentbom");
			// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+""//create different bom case data
					+"data.FieldBOM = factory.com_example_differentbusinessobjectmodel.createDifferentBOMCase();"
					+"data.FieldBOM.caseid = \"case 1 - different BOM\";"
					+"data.FieldBOM.caseState = pkg.com_example_differentbusinessobjectmodel.states.CREATED;"
					+""//create different bom case
					+"data.FieldCase = bpm.caseData.create(data.FieldBOM,'com.example.differentbusinessobjectmodel.DifferentBOMCase');"
					+"data.FieldBOMRead = bpm.caseData.read(data.FieldCase);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			//assert casedata of the cases created
			String fieldBOM = (String) result.get("FieldBOM");
			System.out.println("FieldBOM: " + fieldBOM.toString());
			Assert.assertEquals(fieldBOM, "{\"caseState\":\"CREATED\",\"caseid\":\"case 1 - different BOM\"}",
					"incorrect payload for the object");

			//assert casedata of the cases created
			String fieldBOMRead = (String) result.get("FieldBOMRead");
			System.out.println("FieldBOMRead: " + fieldBOMRead.toString());
			Assert.assertEquals(fieldBOMRead, "{\"caseState\":\"CREATED\",\"caseid\":\"case 1 - different BOM\"}",
					"incorrect payload for the object");

			//print all the case references
			String fieldCase = (String) result.get("FieldCase");
			System.out.println("fieldCase: " + fieldCase);
			Assert.assertTrue(fieldCase.contains("com.example.differentbusinessobjectmodel.DifferentBOMCase-1-0"),
					"incorrect type");

			descriptors.clear();

			System.out.println("================= Test to create different application case ====================");

			descriptors.add(variableDescriptor2);
			scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.ace_2449_process.ace_2449_process.createcasedifferentapplication");

			// @formatter:off
			expression = ScriptManager.createExpression("" + ""//create different bom case data
					+ ""//create different application case data
					+ "data.FieldBOM = factory.com_example_differentapplicationbusinessobjectmodel.createDifferentApplicationCase();"
					+ "data.FieldBOM.caseid = \"case 1 - different Application\";"
					+ "data.FieldBOM.caseState = pkg.com_example_differentapplicationbusinessobjectmodel.states.CREATED;"
					+ ""//create different application case
					+ "data.FieldCase = bpm.caseData.create(data.FieldBOM,'com.example.differentapplicationbusinessobjectmodel.DifferentApplicationCase');"
					+ "data.FieldBOMRead = bpm.caseData.read(data.FieldCase);" 
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//assert casedata of the cases created
			fieldBOM = (String) result.get("FieldBOM");
			System.out.println("FieldBOM: " + fieldBOM.toString());
			Assert.assertEquals(fieldBOM, "{\"caseState\":\"CREATED\",\"caseid\":\"case 1 - different Application\"}",
					"incorrect payload for the object");

			//assert casedata of the cases created
			fieldBOMRead = (String) result.get("FieldBOMRead");
			System.out.println("FieldBOMRead: " + fieldBOMRead.toString());
			Assert.assertEquals(fieldBOMRead,
					"{\"caseState\":\"CREATED\",\"caseid\":\"case 1 - different Application\"}",
					"incorrect payload for the object");

			//print all the case references
			fieldCase = (String) result.get("FieldCase");
			System.out.println("fieldCase: " + fieldCase);
			Assert.assertTrue(
					fieldCase.contains(
							"com.example.differentapplicationbusinessobjectmodel.DifferentApplicationCase-1-0"),
					"incorrect type");

			descriptors.clear();

			System.out.println(
					"================= Test to create different bom different application case ====================");

			descriptors.add(variableDescriptor3);
			scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data,
					"com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ ""//create different bom different application case data
					+ "data.FieldBOM = factory.com_example_different_bom_different_application.createDifferentBOMDifferentApplication();"
					+ "data.FieldBOM.caseid = \"case 1 - different Application different BOM\";"
					+ "data.FieldBOM.caseState = pkg.com_example_different_bom_different_application.states.CREATED;"
					+ ""//create different bom different application case
					+ "data.FieldCase = bpm.caseData.create(data.FieldBOM,'com.example.different_bom_different_application.DifferentBOMDifferentApplication');"
					+ "data.FieldBOMRead = bpm.caseData.read(data.FieldCase);" 
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//assert casedata of the cases created
			fieldBOM = (String) result.get("FieldBOM");
			System.out.println("FieldBOM: " + fieldBOM.toString());
			Assert.assertEquals(fieldBOM,
					"{\"caseid\":\"case 1 - different Application different BOM\",\"caseState\":\"CREATED\"}",
					"incorrect payload for the object");

			//assert casedata of the cases created
			fieldBOMRead = (String) result.get("FieldBOMRead");
			System.out.println("FieldBOMRead: " + fieldBOMRead.toString());
			Assert.assertEquals(fieldBOMRead,
					"{\"caseid\":\"case 1 - different Application different BOM\",\"caseState\":\"CREATED\"}",
					"incorrect payload for the object");

			//print all the case references
			fieldCase = (String) result.get("FieldCase");
			System.out.println("fieldCase: " + fieldCase);
			Assert.assertTrue(
					fieldCase.contains(
							"com.example.different_bom_different_application.DifferentBOMDifferentApplication-1-0"),
					"incorrect type");
		}
		finally
		{
			forceUndeploy(deploymentId1);
		}
	}

	// TEST TEMPORARILY DISABLED - Now fails given that terminal state cases are now invisible to findByCID
	@Test
	public void testHidingTerminalStates()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testcasescriptcasedata_hiding_terminal/testcasescriptcasedata_hiding_terminal.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal");
			//fragment 1 to create cases and read cases
			// @formatter:off
			Expression expression = ScriptManager.createExpression("" 
					+""//0th case
					+"var account0 = factory.com_example_testcasedatascripting.createAccount();"
					+"account0.accountBalance = 0.1234;"
					+"account0.accountInstitution = \"Nationwide Building Society\";"
					+"account0.accountOpened = new Date();"
					+"account0.accountOpened.setFullYear(1991);"
					+"account0.accountOpened.setMonth(1);"
					+"account0.accountOpened.setDate(1);"
					+"account0.accountOpened.setHours(1);"
					+"account0.accountOpened.setMinutes(1);"
					+"account0.accountOpened.setSeconds(1);"
					+"account0.accountOpened.setMilliseconds(101);"
					+"account0.accountLastAccessed = new Date();"
					+"account0.accountLastAccessed.setFullYear(2011);"
					+"account0.accountLastAccessed.setMonth(1);"
					+"account0.accountLastAccessed.setDate(1);"
					+"account0.accountLastAccessed.setHours(1);"
					+"account0.accountLastAccessed.setMinutes(1);"
					+"account0.accountLastAccessed.setSeconds(1);"
					+"account0.accountLastAccessed.setMilliseconds(101);"
					+"account0.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.ACTIVE;"
					+"account0.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+"account0.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account0.institutionAddress.firstLine = \"case 1 line 1\";"
					+"account0.institutionAddress.secondLine = \"case 1 line 2\";"
					+"account0.institutionAddress.city = \"case 1 city 1\";"
					+"account0.institutionAddress.county = \"case 1 county 1\";"
					+"account0.institutionAddress.country = \"case 1 country 1\";"
					+"account0.institutionAddress.postcode = \"SN1 1AP\";"
					+""//1st case
					+"var account1 = factory.com_example_testcasedatascripting.createAccount();"
					+"account1.accountBalance = 0.1234;"
					+"account1.accountInstitution = \"Nationwide Building Society\";"
					+"account1.accountOpened = new Date();"
					+"account1.accountOpened.setFullYear(1992);"
					+"account1.accountOpened.setMonth(2);"
					+"account1.accountOpened.setDate(2);"
					+"account1.accountOpened.setHours(2);"
					+"account1.accountOpened.setMinutes(2);"
					+"account1.accountOpened.setSeconds(2);"
					+"account1.accountOpened.setMilliseconds(202);"
					+"account1.accountLastAccessed = new Date();"
					+"account1.accountLastAccessed.setFullYear(2019);"
					+"account1.accountLastAccessed.setMonth(2);"
					+"account1.accountLastAccessed.setDate(2);"
					+"account1.accountLastAccessed.setHours(2);"
					+"account1.accountLastAccessed.setMinutes(2);"
					+"account1.accountLastAccessed.setSeconds(2);"
					+"account1.accountLastAccessed.setMilliseconds(202);"
					+"account1.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.DORMANT;"
					+"account1.accountType = pkg.com_example_testdata_outside_global.AccountType.CURRENT;"
					+"account1.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account1.institutionAddress.firstLine = \"case 2 line 1\";"
					+"account1.institutionAddress.secondLine = \"case 2 line 2\";"
					+"account1.institutionAddress.city = \"case 2 city 1\";"
					+"account1.institutionAddress.county = \"case 2 county 1\";"
					+"account1.institutionAddress.country = \"case 2 country 1\";"
					+"account1.institutionAddress.postcode = \"SN2 2AP\";"
					+""//2nd case
					+"var account2 = factory.com_example_testcasedatascripting.createAccount();"
					+"account2.accountBalance = 0.1234;"
					+"account2.accountInstitution = \"Nationwide Building Society\";"
					+"account2.accountOpened = new Date();"
					+"account2.accountOpened.setFullYear(1993);"
					+"account2.accountOpened.setMonth(3);"
					+"account2.accountOpened.setDate(3);"
					+"account2.accountOpened.setHours(3);"
					+"account2.accountOpened.setMinutes(3);"
					+"account2.accountOpened.setSeconds(3);"
					+"account2.accountOpened.setMilliseconds(303);"
					+"account2.accountLastAccessed = new Date();"
					+"account2.accountLastAccessed.setFullYear(2013);"
					+"account2.accountLastAccessed.setMonth(3);"
					+"account2.accountLastAccessed.setDate(3);"
					+"account2.accountLastAccessed.setHours(3);"
					+"account2.accountLastAccessed.setMinutes(3);"
					+"account2.accountLastAccessed.setSeconds(3);"
					+"account2.accountLastAccessed.setMilliseconds(303);"
					+"account2.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.TOBETERMINATED;"
					+"account2.accountType = pkg.com_example_testdata_outside_global.AccountType.FIXEDDEPOSIT;"
					+"account2.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account2.institutionAddress.firstLine = \"case 3 line 1\";"
					+"account2.institutionAddress.secondLine = \"case 3 line 2\";"
					+"account2.institutionAddress.city = \"case 3 city 1\";"
					+"account2.institutionAddress.county = \"case 3 county 1\";"
					+"account2.institutionAddress.country = \"case 3 country 1\";"
					+"account2.institutionAddress.postcode = \"SN3 3AP\";"
					+""//3rd case
					+"var account3 = factory.com_example_testcasedatascripting.createAccount();"
					+"account3.accountBalance = 0.1234;"
					+"account3.accountInstitution = \"Nationwide Building Society\";"
					+"account3.accountOpened = new Date();"
					+"account3.accountOpened.setFullYear(1994);"
					+"account3.accountOpened.setMonth(4);"
					+"account3.accountOpened.setDate(4);"
					+"account3.accountOpened.setHours(4);"
					+"account3.accountOpened.setMinutes(4);"
					+"account3.accountOpened.setSeconds(4);"
					+"account3.accountOpened.setMilliseconds(404);"
					+"account3.accountLastAccessed = new Date();"
					+"account3.accountLastAccessed.setFullYear(2014);"
					+"account3.accountLastAccessed.setMonth(4);"
					+"account3.accountLastAccessed.setDate(4);"
					+"account3.accountLastAccessed.setHours(4);"
					+"account3.accountLastAccessed.setMinutes(4);"
					+"account3.accountLastAccessed.setSeconds(4);"
					+"account3.accountLastAccessed.setMilliseconds(404);"
					+"account3.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.BARRED;"
					+"account3.accountType = pkg.com_example_testdata_outside_global.AccountType.INVESTMENT;"
					+"account3.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account3.institutionAddress.firstLine = \"case 4 line 1\";"
					+"account3.institutionAddress.secondLine = \"case 4 line 2\";"
					+"account3.institutionAddress.city = \"case 4 city 1\";"
					+"account3.institutionAddress.county = \"case 4 county 1\";"
					+"account3.institutionAddress.country = \"case 4 country 1\";"
					+"account3.institutionAddress.postcode = \"SN4 4AP\";"
					+"" //push all boms into the array
					+"data.accountBOMs.push(account0);"
					+"data.accountBOMs.push(account1);"
					+"data.accountBOMs.push(account2);"
					+"data.accountBOMs.push(account3);"
					+""//create account cases
					+"data.accountCases.pushAll(bpm.caseData.createAll(data.accountBOMs,'com.example.testcasedatascripting.Account'));"					
					+""//0th customer
					+"var customerBOM0 = factory.com_example_testcasedatascripting.createCustomer();"
					+"customerBOM0.customerID = 'ID0001';"
					+"customerBOM0.age = 0.01;"
					+"customerBOM0.dateofBirth = new Date('1911-01-01');"
					+"customerBOM0.customerCategory = pkg.com_example_testcasedatascripting.CustomerCategory.MEDIUMACTIVITY;"
					+"customerBOM0.residentialAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"customerBOM0.residentialAddress.firstLine = 'customer 1 line 1';"
					+"customerBOM0.residentialAddress.secondLine = 'customer 1 line 2';"
					+"customerBOM0.residentialAddress.city = 'customer 1 city 1';"
					+"customerBOM0.residentialAddress.county = 'customer 1 county 1';"
					+"customerBOM0.residentialAddress.country = 'customer 1 country 1';"
					+"customerBOM0.residentialAddress.postcode = 'SN1 1AP';"
					+"var employmentDetails1 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"var employmentDetails2 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"employmentDetails1.employeeID = '1001';"
					+"employmentDetails1.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.FULLTIMESALARIED;"
					+"employmentDetails1.numberofyears = 1;"
					+"employmentDetails1.yearlyRenumeration = 1111.11;"
					+"employmentDetails2.employeeID = '1002';"
					+"employmentDetails2.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.PARTTIMESALARIED;"
					+"employmentDetails2.numberofyears = 1;"
					+"employmentDetails2.yearlyRenumeration = 1111.11;"
					+"customerBOM0.employmentDetails.push(employmentDetails1, employmentDetails2);"
					+""//1st customer
					+"var customerBOM1 = factory.com_example_testcasedatascripting.createCustomer();"
					+"customerBOM1.customerID = 'ID0002';"
					+"customerBOM1.age = 0.02;"
					+"customerBOM1.dateofBirth = new Date('1921-02-02');"
					+"customerBOM1.customerCategory = pkg.com_example_testcasedatascripting.CustomerCategory.TERMINATED;"
					+"customerBOM1.residentialAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"customerBOM1.residentialAddress.firstLine = 'customer 2 line 1';"
					+"customerBOM1.residentialAddress.secondLine = 'customer 2 line 2';"
					+"customerBOM1.residentialAddress.city = 'customer 2 city 1';"
					+"customerBOM1.residentialAddress.county = 'customer 2 county 1';"
					+"customerBOM1.residentialAddress.country = 'customer 2 country 1';"
					+"customerBOM1.residentialAddress.postcode = 'SN2 2AP';"
					+"var employmentDetails3 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"var employmentDetails4 = factory.com_example_testdata_outside_global.createEmploymentDetails();"
					+"employmentDetails3.employeeID = '2001';"
					+"employmentDetails3.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.RETIRED;"
					+"employmentDetails3.numberofyears = 25;"
					+"employmentDetails3.yearlyRenumeration = 0.01;"
					+"employmentDetails4.employeeID = '2002';"
					+"employmentDetails4.employmentStatus= pkg.com_example_testdata_outside_global.EmploymentStatus.RESIGNED;"
					+"employmentDetails4.numberofyears = 20;"
					+"employmentDetails4.yearlyRenumeration = 0.02;"
					+"customerBOM1.employmentDetails.push(employmentDetails3, employmentDetails4);"
					+"" //push all boms into the array
					+"data.customerBOMs.push(customerBOM0, customerBOM1);"
					+""//create customer cases
					+"data.customerCases.pushAll(bpm.caseData.createAll(data.customerBOMs,'com.example.testcasedatascripting.Customer'));"
					+""//get terminal and non-terminal cases into separate arrays
					+"data.customerCaseNonTerminal = data.customerCases[0];"
					+"data.accountCasesNonTerminal.push(data.accountCases[0]);"
					+"data.accountCasesNonTerminal.push(data.accountCases[1]);"
					+"data.accountCasesTerminal.push(data.accountCases[2]);"
					+"data.accountCasesTerminal.push(data.accountCases[3]);"
					+"" //link cases in non-terminal states
					+"bpm.caseData.linkAll(data.customerCaseNonTerminal, data.accountCasesNonTerminal, 'accounts');"
					//--affected operations - should not include terminal state cases in the result--
					+""//find all
					+"data.accountCasesFindAll.length = 0;"
					+"data.accountCasesFindAll.pushAll(bpm.caseData.findAll('com.example.testcasedatascripting.Account',0,10));"
					+"var lengthAccountCasesFindAll = data.accountCasesFindAll.length;" //2
					+"var accountBOMFindAll0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindAll1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMFindAll0 = bpm.caseData.read(data.accountCasesFindAll[0]);"
					+"accountBOMFindAll1 = bpm.caseData.read(data.accountCasesFindAll[1]);"
					+""//simple search
					+"data.accountCasesSimpleSearch.length = 0;"
					+"data.accountCasesSimpleSearch.pushAll(bpm.caseData.findBySimpleSearch('0.1234','com.example.testcasedatascripting.Account',0,10));"
					+"var lengthAccountCasesSimpleSearch = data.accountCasesSimpleSearch.length;" //2
					+"var accountBOMSimpleSearch0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMSimpleSearch1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMSimpleSearch0 = bpm.caseData.read(data.accountCasesSimpleSearch[0]);"
					+"accountBOMSimpleSearch1 = bpm.caseData.read(data.accountCasesSimpleSearch[1]);"
					+""//find by criteria
					+"data.accountCasesFindByCriteria.length = 0;"
					+"data.accountCasesFindByCriteria.pushAll(bpm.caseData.findByCriteria(\"accountBalance = 0.1234 and accountInstitution = 'Nationwide Building Society'\",'com.example.testcasedatascripting.Account',0,10));"
					+"var lengthAccountCasesFindByCriteria = data.accountCasesFindByCriteria.length;" //2
					+"var accountBOMFindByCriteria0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMFindByCriteria1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMFindByCriteria0 = bpm.caseData.read(data.accountCasesFindByCriteria[0]);"
					+"accountBOMFindByCriteria1 = bpm.caseData.read(data.accountCasesFindByCriteria[1]);"
					//--not affected--
					+""//read a case in terminal state
					+"var accountBOMReadTerminal = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMReadTerminal = bpm.caseData.read(data.accountCases[2]);"
					+""//read All mix of terminal and non-terminal
					+"data.accountBOMsReadAll.length = 0;"
					+"data.accountBOMsReadAll.pushAll(bpm.caseData.readAll(data.accountCases));"
					+"var lengthAccountBOMsReadAll = data.accountBOMsReadAll.length;"
					+""//read All terminal
					+"data.accountBOMsReadTerminal.length = 0;"
					+"data.accountBOMsReadTerminal.pushAll(bpm.caseData.readAll(data.accountCasesTerminal));"
					+"var lengthAccountBOMsReadTerminal = data.accountBOMsReadTerminal.length;"
					+""//find by cid
					+"var accountBOMReadFindCIDTerminal = factory.com_example_testcasedatascripting.createAccount();"
					+""//update a non-terminal account case to a terminal state
					+"data.accountCaseUpdateToTerminal = data.accountCases[1];"
					+"data.accountBOMUpdateToTerminal = bpm.caseData.read(data.accountCases[1]);"
					+"data.accountBOMUpdateToTerminal.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.BARRED;"
					+"data.accountCaseUpdateToTerminal = bpm.caseData.updateByRef(data.accountCaseUpdateToTerminal, data.accountBOMUpdateToTerminal);"
					+""//un-affected operations
					+""//navigate by criteria
					+"data.accountCasesNavigateByCriteria.pushAll(bpm.caseData.navigateByCriteria(data.customerCaseNonTerminal, 'accounts', \"accountStatus = 'BARRED'\", 0, 10));"
					+"var lengthAccountCasesNavigateByCriteria = data.accountCasesNavigateByCriteria.length;" //1
					+"var accountBOMNavigatedByCriteria = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMNavigatedByCriteria = bpm.caseData.read(data.accountCasesNavigateByCriteria[0]);"
					+""//navigate by search
					+"data.accountCasesNavigateBySearch.pushAll(bpm.caseData.navigateBySimpleSearch(data.customerCaseNonTerminal, 'accounts', \"Account00002\", 0, 10));"
					+"var lengthAccountCasesNavigateBySearch = data.accountCasesNavigateBySearch.length;" //1
					+"var accountBOMNavigatedBySearch = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMNavigatedBySearch = bpm.caseData.read(data.accountCasesNavigateBySearch[0]);"
					+""//navigate All
					+"data.accountCasesNavigateAll.pushAll(bpm.caseData.navigateAll(data.customerCaseNonTerminal, 'accounts', 0, 10));"
					+"var lengthAccountCasesNavigateAll = data.accountCasesNavigateAll.length;" //2
					+"var accountBOMNavigateAll0 = factory.com_example_testcasedatascripting.createAccount();"
					+"var accountBOMNavigateAll1 = factory.com_example_testcasedatascripting.createAccount();"
					+"accountBOMNavigateAll0 = bpm.caseData.read(data.accountCasesNavigateAll[0]);"
					+"accountBOMNavigateAll1 = bpm.caseData.read(data.accountCasesNavigateAll[1]);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			//assert the length of cases found / read
			Object lengthAccountCasesFindAll = scope.getValue("lengthAccountCasesFindAll");
			Assert.assertEquals(lengthAccountCasesFindAll.toString(), "2", "incorrect number of elements");

			Object lengthAccountCasesSimpleSearch = scope.getValue("lengthAccountCasesSimpleSearch");
			Assert.assertEquals(lengthAccountCasesSimpleSearch.toString(), "2", "incorrect number of elements");

			Object lengthAccountCasesFindByCriteria = scope.getValue("lengthAccountCasesFindByCriteria");
			Assert.assertEquals(lengthAccountCasesFindByCriteria.toString(), "2", "incorrect number of elements");

			Object lengthAccountBOMsReadAll = scope.getValue("lengthAccountBOMsReadAll");
			Assert.assertEquals(lengthAccountBOMsReadAll.toString(), "4", "incorrect number of elements");

			Object lengthAccountBOMsReadTerminal = scope.getValue("lengthAccountBOMsReadTerminal");
			Assert.assertEquals(lengthAccountBOMsReadTerminal.toString(), "2", "incorrect number of elements");

			Object lengthAccountCasesNavigateByCriteria = scope.getValue("lengthAccountCasesNavigateByCriteria");
			Assert.assertEquals(lengthAccountCasesNavigateByCriteria.toString(), "1", "incorrect number of elements");

			Object lengthAccountCasesNavigateBySearch = scope.getValue("lengthAccountCasesNavigateBySearch");
			Assert.assertEquals(lengthAccountCasesNavigateBySearch.toString(), "1", "incorrect number of elements");

			Object lengthAccountCasesNavigateAll = scope.getValue("lengthAccountCasesNavigateAll");
			Assert.assertEquals(lengthAccountCasesNavigateAll.toString(), "2", "incorrect number of elements");

			//assert casedata of the cases created/fetched
			Object accountBOMFindAll0 = scope.getValue("accountBOMFindAll0");
			System.out.println("accountBOMFindAll0: " + accountBOMFindAll0.toString());
			Assert.assertEquals(accountBOMFindAll0,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindAll1 = scope.getValue("accountBOMFindAll1");
			System.out.println("accountBOMFindAll1: " + accountBOMFindAll1.toString());
			Assert.assertEquals(accountBOMFindAll1,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1991-02-01T00:01:01.101Z\", \"accountLastAccessed\": \"2011-02-01T00:01:01.101Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 1AP\"}}",
					"incorrect payload for the object");

			Object accountBOMSimpleSearch0 = scope.getValue("accountBOMSimpleSearch0");
			System.out.println("accountBOMSimpleSearch0: " + accountBOMSimpleSearch0.toString());
			Assert.assertEquals(accountBOMSimpleSearch0,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			Object accountBOMSimpleSearch1 = scope.getValue("accountBOMSimpleSearch1");
			System.out.println("accountBOMSimpleSearch1: " + accountBOMSimpleSearch1.toString());
			Assert.assertEquals(accountBOMSimpleSearch1,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1991-02-01T00:01:01.101Z\", \"accountLastAccessed\": \"2011-02-01T00:01:01.101Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 1AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByCriteria0 = scope.getValue("accountBOMFindByCriteria0");
			System.out.println("accountBOMFindByCriteria0: " + accountBOMFindByCriteria0.toString());
			Assert.assertEquals(accountBOMFindByCriteria0,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"DORMANT\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			Object accountBOMFindByCriteria1 = scope.getValue("accountBOMFindByCriteria1");
			System.out.println("accountBOMFindByCriteria1: " + accountBOMFindByCriteria1.toString());
			Assert.assertEquals(accountBOMFindByCriteria1,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1991-02-01T00:01:01.101Z\", \"accountLastAccessed\": \"2011-02-01T00:01:01.101Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 1AP\"}}",
					"incorrect payload for the object");

			Object accountBOMReadTerminal = scope.getValue("accountBOMReadTerminal");
			System.out.println("accountBOMReadTerminal: " + accountBOMReadTerminal.toString());
			Assert.assertEquals(accountBOMReadTerminal,
					"{\"accountId\": \"Account00003\", \"accountStatus\": \"TOBETERMINATED\", \"accountOpened\": \"1993-04-03T02:03:03.303Z\", \"accountLastAccessed\": \"2013-04-03T02:03:03.303Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"FIXEDDEPOSIT\", \"institutionAddress\": {\"firstLine\": \"case 3 line 1\", \"secondLine\": \"case 3 line 2\", \"city\": \"case 3 city 1\", \"county\": \"case 3 county 1\", \"country\": \"case 3 country 1\", \"postcode\": \"SN3 3AP\"}}",
					"incorrect payload for the object");

			Object accountBOMsReadAll = result.get("accountBOMsReadAll");
			System.out.println("accountBOMsReadAll: " + accountBOMsReadAll.toString());
			Assert.assertEquals(accountBOMsReadAll,
					"[{\"accountId\":\"Account00001\",\"accountStatus\":\"ACTIVE\",\"accountOpened\":\"1991-02-01T00:01:01.101Z\",\"accountLastAccessed\":\"2011-02-01T00:01:01.101Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"SAVINGS\",\"institutionAddress\":{\"firstLine\":\"case 1 line 1\",\"secondLine\":\"case 1 line 2\",\"city\":\"case 1 city 1\",\"county\":\"case 1 county 1\",\"country\":\"case 1 country 1\",\"postcode\":\"SN1 1AP\"}},{\"accountId\":\"Account00002\",\"accountStatus\":\"DORMANT\",\"accountOpened\":\"1992-03-02T01:02:02.202Z\",\"accountLastAccessed\":\"2019-03-02T01:02:02.202Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"CURRENT\",\"institutionAddress\":{\"firstLine\":\"case 2 line 1\",\"secondLine\":\"case 2 line 2\",\"city\":\"case 2 city 1\",\"county\":\"case 2 county 1\",\"country\":\"case 2 country 1\",\"postcode\":\"SN2 2AP\"}},{\"accountId\":\"Account00003\",\"accountStatus\":\"TOBETERMINATED\",\"accountOpened\":\"1993-04-03T02:03:03.303Z\",\"accountLastAccessed\":\"2013-04-03T02:03:03.303Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"FIXEDDEPOSIT\",\"institutionAddress\":{\"firstLine\":\"case 3 line 1\",\"secondLine\":\"case 3 line 2\",\"city\":\"case 3 city 1\",\"county\":\"case 3 county 1\",\"country\":\"case 3 country 1\",\"postcode\":\"SN3 3AP\"}},{\"accountId\":\"Account00004\",\"accountStatus\":\"BARRED\",\"accountOpened\":\"1994-05-04T03:04:04.404Z\",\"accountLastAccessed\":\"2014-05-04T03:04:04.404Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"INVESTMENT\",\"institutionAddress\":{\"firstLine\":\"case 4 line 1\",\"secondLine\":\"case 4 line 2\",\"city\":\"case 4 city 1\",\"county\":\"case 4 county 1\",\"country\":\"case 4 country 1\",\"postcode\":\"SN4 4AP\"}}]",
					"incorrect payload for the object");

			Object accountBOMsReadTerminal = result.get("accountBOMsReadTerminal");
			System.out.println("accountBOMsReadTerminal: " + accountBOMsReadTerminal.toString());
			Assert.assertEquals(accountBOMsReadTerminal,
					"[{\"accountId\":\"Account00003\",\"accountStatus\":\"TOBETERMINATED\",\"accountOpened\":\"1993-04-03T02:03:03.303Z\",\"accountLastAccessed\":\"2013-04-03T02:03:03.303Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"FIXEDDEPOSIT\",\"institutionAddress\":{\"firstLine\":\"case 3 line 1\",\"secondLine\":\"case 3 line 2\",\"city\":\"case 3 city 1\",\"county\":\"case 3 county 1\",\"country\":\"case 3 country 1\",\"postcode\":\"SN3 3AP\"}},{\"accountId\":\"Account00004\",\"accountStatus\":\"BARRED\",\"accountOpened\":\"1994-05-04T03:04:04.404Z\",\"accountLastAccessed\":\"2014-05-04T03:04:04.404Z\",\"accountBalance\":0.1234,\"accountInstitution\":\"Nationwide Building Society\",\"accountType\":\"INVESTMENT\",\"institutionAddress\":{\"firstLine\":\"case 4 line 1\",\"secondLine\":\"case 4 line 2\",\"city\":\"case 4 city 1\",\"county\":\"case 4 county 1\",\"country\":\"case 4 country 1\",\"postcode\":\"SN4 4AP\"}}]",
					"incorrect payload for the object");

			Object accountBOMNavigatedByCriteria = scope.getValue("accountBOMNavigatedByCriteria");
			System.out.println("accountBOMNavigatedByCriteria: " + accountBOMNavigatedByCriteria.toString());
			Assert.assertEquals(accountBOMNavigatedByCriteria,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"BARRED\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			Object accountBOMNavigatedBySearch = scope.getValue("accountBOMNavigatedBySearch");
			System.out.println("accountBOMNavigatedBySearch: " + accountBOMNavigatedBySearch.toString());
			Assert.assertEquals(accountBOMNavigatedBySearch,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"BARRED\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			Object accountBOMNavigateAll0 = scope.getValue("accountBOMNavigateAll0");
			System.out.println("accountBOMNavigateAll0: " + accountBOMNavigateAll0.toString());
			Assert.assertEquals(accountBOMNavigateAll0,
					"{\"accountId\": \"Account00001\", \"accountStatus\": \"ACTIVE\", \"accountOpened\": \"1991-02-01T00:01:01.101Z\", \"accountLastAccessed\": \"2011-02-01T00:01:01.101Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"SAVINGS\", \"institutionAddress\": {\"firstLine\": \"case 1 line 1\", \"secondLine\": \"case 1 line 2\", \"city\": \"case 1 city 1\", \"county\": \"case 1 county 1\", \"country\": \"case 1 country 1\", \"postcode\": \"SN1 1AP\"}}",
					"incorrect payload for the object");

			Object accountBOMNavigateAll1 = scope.getValue("accountBOMNavigateAll1");
			System.out.println("accountBOMNavigateAll1: " + accountBOMNavigateAll1.toString());
			Assert.assertEquals(accountBOMNavigateAll1,
					"{\"accountId\": \"Account00002\", \"accountStatus\": \"BARRED\", \"accountOpened\": \"1992-03-02T01:02:02.202Z\", \"accountLastAccessed\": \"2019-03-02T01:02:02.202Z\", \"accountBalance\": 0.1234, \"accountInstitution\": \"Nationwide Building Society\", \"accountType\": \"CURRENT\", \"institutionAddress\": {\"firstLine\": \"case 2 line 1\", \"secondLine\": \"case 2 line 2\", \"city\": \"case 2 city 1\", \"county\": \"case 2 county 1\", \"country\": \"case 2 country 1\", \"postcode\": \"SN2 2AP\"}}",
					"incorrect payload for the object");

			//print all the case references
			String accountCases = (String) result.get("accountCases");
			System.out.println("accountCases: " + accountCases);
			String accountCasesFindAll = (String) result.get("accountCasesFindAll");
			System.out.println("accountCasesFindAll:" + accountCasesFindAll);
			String accountCasesSimpleSearch = (String) result.get("accountCasesSimpleSearch");
			System.out.println("accountCasesSimpleSearch:" + accountCasesSimpleSearch);
			String accountCasesFindByCriteria = (String) result.get("accountCasesFindByCriteria");
			System.out.println("accountCasesFindByCriteria: " + accountCasesFindByCriteria);

			System.out.println(
					"================= Test to findByCID() to not return a case in terminal state and throw an exception ====================");

			try
			{
			// @formatter:off
			expression = ScriptManager.createExpression(""			
					//findByCID() should not be able to find a case in terminal state
					+"data.accountCaseFindByCID = bpm.caseData.findByCaseIdentifier('Account00004','com.example.testcasedatascripting.Account');"
					+"accountBOMReadFindCIDTerminal = bpm.caseData.read(data.accountCaseFindByCID);"
			+"");
			// @formatter:on

				expression.eval(scope);
				result = scope.getData();
			}
			catch (ScriptException e)
			{

				e.getStackTrace().toString();
				System.out.println(e.getMessage());
				Assert.assertEquals(e.getMessage(),
						"com.tibco.bpm.cdm.api.exception.ReferenceException: Case with case identifier does not exist: Account00004",
						"incorrect error message");
				Assert.assertNull((String) result.get("accountCaseFindByCID"), "case is not null");
			}
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void testUpdateTerminalStateCase()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			InternalException, PersistenceException, DeploymentException, ScriptException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testcasedatascripting/Exports/Deployment Artifacts/testCasedataScripting.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testcasescript/Exports/Deployment Artifacts/process-js/testcasescript/testcasescriptcasedata_terminal_update_exception/testcasescriptcasedata_terminal_update_exception.js");

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Scope scope = null;
		String accountCase = "";

		Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
		businessDataApplicationInfo.put("com.example.testcasedatascripting", 1l);
		List<String> descriptors = new ArrayList<String>();
		descriptors.add(variableDescriptor);
		scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
		scope.setData(data,
				"com.example.testcasescript.testcasescript.testcasescriptcasedata_terminal_update_exception");
		// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+""//0th account 
					+"var account0 = factory.com_example_testcasedatascripting.createAccount();"
					+"account0.accountBalance = 0.1234;"
					+"account0.accountInstitution = \"Nationwide Building Society\";"
					+"account0.accountOpened = new Date();"
					+"account0.accountOpened.setFullYear(1991);"
					+"account0.accountOpened.setMonth(1);"
					+"account0.accountOpened.setDate(1);"
					+"account0.accountOpened.setHours(1);"
					+"account0.accountOpened.setMinutes(1);"
					+"account0.accountOpened.setSeconds(1);"
					+"account0.accountOpened.setMilliseconds(101);"
					+"account0.accountLastAccessed = new Date();"
					+"account0.accountLastAccessed.setFullYear(2011);"
					+"account0.accountLastAccessed.setMonth(1);"
					+"account0.accountLastAccessed.setDate(1);"
					+"account0.accountLastAccessed.setHours(1);"
					+"account0.accountLastAccessed.setMinutes(1);"
					+"account0.accountLastAccessed.setSeconds(1);"
					+"account0.accountLastAccessed.setMilliseconds(101);"
					+"account0.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.BARRED;"
					+"account0.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+"account0.institutionAddress = factory.com_example_testcasedatascripting.createAddress();"
					+"account0.institutionAddress.firstLine = \"case 1 line 1\";"
					+"account0.institutionAddress.secondLine = \"case 1 line 2\";"
					+"account0.institutionAddress.city = \"case 1 city 1\";"
					+"account0.institutionAddress.county = \"case 1 county 1\";"
					+"account0.institutionAddress.country = \"case 1 country 1\";"
					+"account0.institutionAddress.postcode = \"SN1 1AP\";"
					+"data.accountBOM = account0;"
					+""//create account
					+"data.accountCase = bpm.caseData.create(data.accountBOM,'com.example.testcasedatascripting.Account');"
					+""//read account
					+"data.accountBOM = bpm.caseData.read(data.accountCase);"
					+""//update account details
					+"data.accountBOM.accountStatus = pkg.com_example_testcasedatascripting.AccountStatus.TOBETERMINATED;"
					+"data.accountBOM.accountBalance = 1000.11;"
					+"data.accountBOM.accountInstitution = 'Terminal Update';"
					+"data.accountBOM.accountType = pkg.com_example_testdata_outside_global.AccountType.SAVINGS;"
					+ "");
			// @formatter:on
		try
		{

			expression.eval(scope);
			result = scope.getData();

			//assert casedata of the cases created
			String accountBOM = (String) result.get("accountBOM");
			System.out.println("accountBOM: " + accountBOM.toString());
			Assert.assertEquals(accountBOM,
					"{\"accountId\":\"Account00001\",\"accountStatus\":\"TOBETERMINATED\",\"accountOpened\":\"1991-02-01T00:01:01.101Z\",\"accountLastAccessed\":\"2011-02-01T00:01:01.101Z\",\"accountBalance\":1000.11,\"accountInstitution\":\"Terminal Update\",\"accountType\":\"SAVINGS\",\"institutionAddress\":{\"firstLine\":\"case 1 line 1\",\"secondLine\":\"case 1 line 2\",\"city\":\"case 1 city 1\",\"county\":\"case 1 county 1\",\"country\":\"case 1 country 1\",\"postcode\":\"SN1 1AP\"}}",
					"incorrect payload for the object");

			//print all the case references
			accountCase = (String) result.get("accountCase");
			System.out.println("accountCase: " + accountCase);

			System.out.println("================= Test to case update not allowed exception ====================");

			try
			{
			// @formatter:off
			expression = ScriptManager.createExpression(""			
			+""//update account
			+"data.accountCase = bpm.caseData.updateByRef(data.accountCase, data.accountBOM);"
			+"");
			// @formatter:on

				expression.eval(scope);
				result = scope.getData();
			}
			catch (ScriptException e)
			{

				e.getStackTrace().toString();
				System.out.println(e.getMessage());
				Assert.assertEquals(e.getMessage(),
						"com.tibco.bpm.cdm.api.exception.ReferenceException: Case is in a terminal state so cannot be updated: "
								+ accountCase);
			}
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}
}
