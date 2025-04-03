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
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.CaseLinksFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.test.util.FileUtils.DataModelModifier;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.Constraint;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.da.dm.api.IdentifierInitialisationInfo;
import com.tibco.bpm.da.dm.api.State;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class SuccessfulUpgradeScenariosWithCasesTest extends Utils
{

	private CaseTypesFunctions		caseTypes				= new CaseTypesFunctions();

	private CasesFunctions			cases					= new CasesFunctions();

	private GetPropertiesFunction	properties				= new GetPropertiesFunction();

	private CaseLinksFunction		links					= new CaseLinksFunction();

	private File					tempFile;

	int								validStatusCode			= 200;

	int								invalidStatusCode		= 400;

	int								notFoundStatusCode		= 404;

	private Response				response				= null;

	private JsonPath				jsonPath				= null;

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger						deploymentIdAppV1		= BigInteger.valueOf(12);

	BigInteger						deploymentIdAppV2		= BigInteger.valueOf(13);

	BigInteger						deploymentIdAppV3		= BigInteger.valueOf(14);

	BigInteger						deploymentIdAppV4		= BigInteger.valueOf(15);

	BigInteger						deploymentIdAppV5		= BigInteger.valueOf(16);

	BigInteger						deploymentIdAppV6		= BigInteger.valueOf(17);

	BigInteger						deploymentIdAppV7		= BigInteger.valueOf(18);

	private static final String		caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String		caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String		caseTypeNewCaseType		= "com.example.bankdatamodel.NewCaseType";

	private static final String		applicationMajorVersion	= "1";

	private List<String>			caseRefAccounts			= new ArrayList<>();

	private List<String>			caseRefCustomers		= new ArrayList<>();

	private List<String>			caseRefNewCasesTypes	= new ArrayList<>();

	private List<String> createCases(String caseType, Integer numberOfCases) throws IOException, URISyntaxException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRefsOutput = new ArrayList<>();
		List<String> casedata = new ArrayList<>();
		try
		{
			if (caseType.equals(caseTypeCustomer))
			{
				if (caseRefCustomers.size() == 0)
				{
					casedata.add(properties.readValue("createCustomer0V1"));
					casedata.add(properties.readValue("createCustomer1V1"));
				}
				else if (caseRefCustomers.size() > 5)
				{
					casedata.add(properties.readValue("createCustomer2V6"));
				}
			}
			else if (caseType.equals(caseTypeAccount))
			{
				casedata.add(properties.readValue("createAccount0V1"));
				casedata.add(properties.readValue("createAccount1V1"));
				casedata.add(properties.readValue("createAccount2V1"));
				casedata.add(properties.readValue("createAccount3V1"));
			}
			else if (caseType.equals(caseTypeNewCaseType))
			{
				if (caseRefNewCasesTypes.size() == 0)
				{
					casedata.add(properties.readValue("createNewCaseType0V4"));
				}
				else if (caseRefNewCasesTypes.size() == 1)
				{
					casedata.add(properties.readValue("createNewCaseType1V5"));
				}
				else if (caseRefNewCasesTypes.size() == 2)
				{
					casedata.add(properties.readValue("createNewCaseType2V6"));
				}
			}
			response = cases.createCases(caseType, 1, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < numberOfCases; caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating account cases");
			System.out.println(e.getMessage());
			Assert.fail("cases cannot be created");
		}
		return caseRefsOutput;
	}

	private void readCase(String caseReference, String casedataToMatch) throws IOException, URISyntaxException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		try
		{
			response = cases.getSingleCase(caseReference, SELECT_CASES_CASEDATA);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while reading casedata");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), casedataToMatch),
					"casedata does not match");
			System.out.println(jsonPath.getString("")); // useful for debugging
		}
		catch (Exception e)
		{
			System.out.println("exception in reading casedata");
			System.out.println(e.getMessage());
			Assert.fail("casedata cannot be read");
		}
	}

	private void readSummary(String caseReference, String summaryToMatch) throws IOException, URISyntaxException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		try
		{
			response = cases.getSingleCase(caseReference, SELECT_CASES_SUMMARY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while reading case summary");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), summaryToMatch),
					"summary does not match");
			System.out.println(jsonPath.getString("")); // useful for debugging
		}
		catch (Exception e)
		{
			System.out.println("exception in reading case summary");
			System.out.println(e.getMessage());
			Assert.fail("summary cannot be read");
		}
	}

	private void searchCase(String searchTerm, String isInTerminalState, String caseReference, String casedata,
			String summaryToMatch) throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		Map<String, String> filterMap = new HashMap<String, String>();

		filterMap.put("caseType", caseTypeAccount);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		if (isInTerminalState.equals("FALSE"))
		{
			filterMap.put("isInTerminalState", isInTerminalState);
		}

		String skip = "0";
		String top = "10";

		try
		{
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_C_S_CR, skip, top, searchTerm, "");
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while searching for cases");
			JsonPath jsonPath = response.jsonPath();
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedata),
					"casedata does not match");

			Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReference, "caseRef should be tagged");

			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
					"summary does not match");
			filterMap.clear();
		}
		catch (Exception e)
		{
			System.out.println("exception in searching for cases ");
			System.out.println(e.getMessage());
			Assert.fail("summary cannot be searched");
		}
	}

	private void findCase(String[] searchAttributes, String[] searchTerms, String isInTerminalState,
			String caseReference, String casedata, String summaryToMatch)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";

		//findBy(criteria)
		filterMap.put("caseType", caseTypeAccount);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		if (isInTerminalState.equals("FALSE"))
		{
			filterMap.put("isInTerminalState", isInTerminalState);
		}

		try
		{
			for (int searchAttributeIterator = 0; searchAttributeIterator < searchTerms.length; searchAttributeIterator++)
			{
				filterDql.put(searchAttributes[searchAttributeIterator], searchTerms[searchAttributeIterator]);
			}
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while searching for cases");
			JsonPath jsonPath = response.jsonPath();

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1);

			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedata),
					"casedata does not match");

			Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReference, "caseRef should be tagged");

			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryToMatch),
					"summary does not match");
			filterDql.clear();
			filterMap.clear();
		}
		catch (

		Exception e)
		{
			System.out.println("exception in finding for cases ");
			System.out.println(e.getMessage());
			Assert.fail("summary cannot be found");
		}
	}

	//(navigateAttributes,navigateTermDQLs,caseRefAccounts.get(6),caseRefNewCasesTypes.get(1));

	private void navigateCase(String[] searchAttributes, String[] searchTerms, String sourceCaseReference,
			String targetCaseReference)
			throws IOException, URISyntaxException, DeploymentException, PersistenceException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String linkName = "linksthenewcasetypes";

		String skip = "0";
		String top = "10";

		//findBy(criteria)
		filterMap.put("name", linkName);

		try
		{
			for (int searchAttributeIterator = 0; searchAttributeIterator < searchTerms.length; searchAttributeIterator++)
			{
				filterDql.put(searchAttributes[searchAttributeIterator], searchTerms[searchAttributeIterator]);
			}
			response = links.getLinks(filterMap, filterDql, sourceCaseReference, skip, top);
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while navigating for cases");
			JsonPath jsonPath = response.jsonPath();

			System.out.println(jsonPath.getString("")); // useful for debugging

			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 1, "multiple or no cases are returned");

			Assert.assertEquals(obj.get(0).getCaseReference(), targetCaseReference,
					"linked case reference is incorrect");
			Assert.assertEquals(obj.get(0).getName(), linkName, "name is incorrect");

			//clear the filter
			filterMap.clear();
			filterDql.clear();
		}
		catch (

		Exception e)
		{
			System.out.println("exception in finding for cases ");
			System.out.println(e.getMessage());
			Assert.fail("summary cannot be found");
		}
	}

	private String updateCase(String caseReference, String casedata) throws IOException, URISyntaxException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		String caseRefsOutput = "";
		try
		{
			response = cases.updateSingleCase(caseReference, casedata);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsOutput = jsonPath.getString("[0]");
		}
		catch (Exception e)
		{
			System.out.println("problem while updating cases");
			System.out.println(e.getMessage());
		}
		return caseRefsOutput;
	}

	//create links
	private void createLinks(String sourceCaseRef, List<String> targetCaseRef, String expectation)
			throws IOException, InterruptedException
	{
		String linkName = "linksthenewcasetypes";

		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		for (int caseRefIterator = 0; caseRefIterator < targetCaseRef.size(); caseRefIterator++)
		{
			Link link = new Link();
			link.setCaseReference(targetCaseRef.get(caseRefIterator));
			link.setName(linkName);
			linksPostRequestBody.add(link);
		}

		response = links.createLinks(sourceCaseRef, linksPostRequestBody);
		System.out.println(response.asString());

		if (expectation.equals("FAILURE"))
		{
			String errorMessage = "";

			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), properties.getLinkNameDoesNotExist().errorResponse,
					"incorrect response");

			errorMessage = properties.getLinkNameDoesNotExist().errorMsg;
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseTypeAccount);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", applicationMajorVersion);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getLinkNameDoesNotExist().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 3,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "typeName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseTypeAccount,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "majorVersion",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), applicationMajorVersion,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "linkName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), linkName,
					"incorrect name of context attribute");
			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(jsonPath.getString("stackTrace")
					.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Type '" + caseTypeAccount
							+ "' (major version " + applicationMajorVersion + ") does not have a link called '"
							+ linkName + "'"),
					true, "incorrect stacktrace");
		}

		else if (expectation.equals("SUCCESS"))
		{
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");
		}
	}

	private void undeployAllAccounts() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException
	{
		forceUndeploy(deploymentIdAppV1);
		forceUndeploy(deploymentIdAppV2);
		forceUndeploy(deploymentIdAppV3);
		forceUndeploy(deploymentIdAppV4);
		forceUndeploy(deploymentIdAppV5);
		forceUndeploy(deploymentIdAppV6);
		forceUndeploy(deploymentIdAppV7);
	}

	private File produceZip() throws IOException, URISyntaxException, RuntimeApplicationException
	{

		File temporaryFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), temporaryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		return temporaryFile;
	}

	private void assertNumberOfTypes(Integer expectedNumberOfTypes) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("namespace", "com.example.bankdatamodel");

		//assert types
		response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", "20");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		//assert that there are 6 structured types returned
		String repsonseJson = response.getBody().asString();

		TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
		Assert.assertEquals(obj.size(), (int) expectedNumberOfTypes, "incorrect number of types");
	}

	//Test to deploy v1 with additional constraints on number
	@Test(description = "Test to deploy v1 with additional constraints on number")
	public void deployV1()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			PersistenceException, InternalException, InterruptedException, DeploymentException
	{

		undeployAllAccounts();

		//get the zip file
		tempFile = produceZip();

		DataModelModifier modifierV1 = (dm) -> {
			//adding constraints for age number
			//			V1
			//			min value : 1  & minInclusive  : true
			//			max value : 4000  & maxInclusive  : true
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").newConstraint("minValue", "1");
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").newConstraint("minValueInclusive",
					"true");
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").newConstraint("maxValue", "4000");
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").newConstraint("maxValueInclusive",
					"true");

			//change constraint for account liability holding
			//			length : 11
			//			decimal places : 2
			//			min value : -999999999.99  & minInclusive  : false
			//			max value : 999999999.99  & maxInclusive  : false
			dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive").setValue("false");

			//change constraint for min value
			dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue").setValue("-999999999.99");

			//change constraint for length
			dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding").getConstraint("length")
					.setValue("11");

		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV1);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.0.0");

		//deploy v1
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV1);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//customer cases
			caseRefCustomers.addAll(createCases(caseTypeCustomer, 2));
			readCase(caseRefCustomers.get(0), properties.readValue("readCustomer0V1"));
			readCase(caseRefCustomers.get(1), properties.readValue("readCustomer1V1"));

			//account cases
			caseRefAccounts.addAll(createCases(caseTypeAccount, 4));
			readCase(caseRefAccounts.get(0), properties.readValue("createAccount0V1"));
			readCase(caseRefAccounts.get(1), properties.readValue("createAccount1V1"));
			readCase(caseRefAccounts.get(2), properties.readValue("createAccount2V1"));
			readCase(caseRefAccounts.get(3), properties.readValue("createAccount3V1"));
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}
		assertNumberOfTypes(6);
	}

	//Test to upgrade to v2 with additional allowed values and states
	@Test(dependsOnMethods = {
			"deployV1"}, description = "Test to upgrade to v2 with additional allowed values and states")
	public void upgradeV2()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		//make valid changes for upgrade to v2
		//add allowed value, add a terminal state, add a non-terminal state, make an existing terminal state non-terminal
		DataModelModifier modifierV2 = (dm) -> {
			//change labels of one of the allowed values
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("salutation").getAllowedValues().get(0)
					.setLabel("MISTER");
			//new added allowed value in Personal Details
			dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("salutation").newAllowedValue("Sir",
					"SIR");

			//change labels of one of the allowed values
			dm.getStructuredTypeByName("Account").getAttributeByName("accountType").getAllowedValues().get(0)
					.setLabel("SAVINGS");
			//new added allowed value in Account
			dm.getStructuredTypeByName("Account").getAttributeByName("accountType").newAllowedValue("Flexible ISA",
					"FLEXIBLE_ISA");
			dm.getStructuredTypeByName("Account").getStateModel().newState("@Unknown&", "@UNKNOWN&", false);
			dm.getStructuredTypeByName("Account").getStateModel().newState("@Tempered or Hacked&",
					"@TEMPERED_OR_HACKED&", true);
			State sTermToNonTerm = dm.getStructuredTypeByName("Account").getStateModel().getStateByValue("@CANCELLED&");
			sTermToNonTerm.setIsTerminal(false);
		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV2);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.1.0");

		//deploy - successful
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV2);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//update customers
			caseRefCustomers.add(updateCase(caseRefCustomers.get(0), properties.readValue("updateCustomer0V2")));
			readCase(caseRefCustomers.get(2), properties.readValue("updateCustomer0V2"));

			//update accounts
			caseRefAccounts.add(updateCase(caseRefAccounts.get(0), properties.readValue("updateAccount0V2")));
			caseRefAccounts.add(updateCase(caseRefAccounts.get(1), properties.readValue("updateAccount1V2")));
			caseRefAccounts.add(updateCase(caseRefAccounts.get(2), properties.readValue("updateAccount2V2")));
			caseRefAccounts.add(updateCase(caseRefAccounts.get(3), properties.readValue("updateAccount3V2")));
			readCase(caseRefAccounts.get(4), properties.readValue("updateAccount0V2"));
			readCase(caseRefAccounts.get(5), properties.readValue("updateAccount1V2"));
			readCase(caseRefAccounts.get(6), properties.readValue("updateAccount2V2"));
			readCase(caseRefAccounts.get(7), properties.readValue("updateAccount3V2"));
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}

		assertNumberOfTypes(6);
	}

	//Test to upgrade to v3 with new constraints added
	@Test(dependsOnMethods = {"upgradeV2"}, description = "Test to upgrade to v2 with new constraints added")
	public void upgradeV3()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			PersistenceException, InternalException, InterruptedException, DeploymentException
	{
		//make valid changes for upgrade to v3
		//add new constraints for all types of attributes
		//deploy - successful
		DataModelModifier modifierV3 = (dm) -> {
			//ACE-1597
			//TODO insterted by nashtapu [May 31, 2019 4:12:18 PM]
			//UNCOMMENT AFTER DISCUSSION WITH SIMON
			//constraints for Account liability holding change 
			//decimal places increased, min value reduced, min value inclusive changed
			//			V3
			//			length : 12
			//			decimal places : 3
			//			min value : -999999999.980  & minInclusive  : true
			//			max value : 999999999.980  & maxInclusive  : true
			//			Constraint cAccountLiability = dm.getStructuredTypeByName("Account")
			//					.getAttributeByName("accountLiabilityHolding").getConstraint("length");
			//			cAccountLiability.setValue("12");
			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
			//					.getConstraint("minValue");
			//			cAccountLiability.setValue("-999999999.980");
			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
			//					.getConstraint("maxValue");
			//			cAccountLiability.setValue("999999999.980");
			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
			//					.getConstraint("maxValueInclusive");
			//			cAccountLiability.setValue("true");
			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
			//					.getConstraint("minValueInclusive");
			//			cAccountLiability.setValue("true");
			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
			//					.getConstraint("decimalPlaces");
			//			cAccountLiability.setValue("3");

			//REMOVE THESE AFTER DISCUSSION WITH SIMON
			Constraint cAccountLiability = dm.getStructuredTypeByName("Account")
					.getAttributeByName("accountLiabilityHolding").getConstraint("length");
			cAccountLiability.setValue("12");
			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue");
			cAccountLiability.setValue("-999999999.999");
			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValue");
			cAccountLiability.setValue("999999999.999");
			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive");
			cAccountLiability.setValue("true");
			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			cAccountLiability.setValue("true");
			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("decimalPlaces");
			cAccountLiability.setValue("3");

			//constraints for Personal Details firstName change 
			Constraint cFirstName = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("firstName")
					.getConstraint("length");
			cFirstName.setValue("51");

			//constraints for Personal Details age change 
			//			V3 (range has changed)
			//			min value : -1  & minInclusive  : fale
			//			max value : 4001  & maxInclusive  : false
			Constraint cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
					.getConstraint("minValue");
			cAge.setValue("-1");

			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").getConstraint("maxValue");
			cAge.setValue("4001");

			//with the addition of these 2 constraints the test case was failing - fixed under ACE-1438
			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
					.getConstraint("minValueInclusive");
			cAge.setValue("false");

			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
					.getConstraint("maxValueInclusive");
			cAge.setValue("false");
		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV3);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.2.0");

		//deploy - successful
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV3);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//update customers
			caseRefCustomers.add(updateCase(caseRefCustomers.get(2), properties.readValue("updateCustomer0V3")));
			readCase(caseRefCustomers.get(3), properties.readValue("updateCustomer0V3"));

			//update one account in v3 and other accounts in consecutive upgrades - 1 cases to update remaining
			caseRefAccounts.add(updateCase(caseRefAccounts.get(4), properties.readValue("updateAccount0V3")));
			readCase(caseRefAccounts.get(8), properties.readValue("updateAccount0V3"));
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}

		assertNumberOfTypes(6);

	}

	//Test to upgrade to v4 with new new types and casetype added
	@Test(dependsOnMethods = {"upgradeV3"}, description = "Test to upgrade to v4 with new new types and casetype added")
	public void upgradeV4()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		// THIS test is failing to upgrade for a new case type - resulting in null pointer exception even if identifier initializtion info 
		//even if it is generated in the model -

		//	    "identifierInitialisationInfo" : {
		//	      "prefix" : "New Case - ",
		//	      "suffix" : " - Type",
		//	      "minNumLength" : 4
		//	    }

		//		make valid changes for upgrade to v4
		//		addition of new case type with a new auto id field 
		//		for new case types some fields in summary and searchable
		//		add some constraints in the type
		DataModelModifier modifierV4 = (dm) -> {
			//new case type
			//attributes - id, state, number & array, text & array, fixed point number & array, date & array, time & array, datetimeTZ & array, boolean & array, uri & array, type & array
			//searchable - id(text), state, text, number, fixed point number  
			//summary - id(text), state, text, number, fixed point number
			StructuredType stCaseType = dm.newStructuredType();
			stCaseType.setName("NewCaseType");
			stCaseType.setLabel("New Case Type");
			stCaseType.setDescription("A new case type");
			stCaseType.setIsCase(true);

			IdentifierInitialisationInfo stCaseIdentifier = stCaseType.newIdentifierInitialisationInfo();
			stCaseIdentifier.setMinNumLength(4);
			stCaseIdentifier.setPrefix("New Case - ");
			stCaseIdentifier.setSuffix(" - Type");

			StateModel stCaseModel = stCaseType.newStateModel();
			stCaseModel.newState("DEPLOYED", "deployed", false);
			stCaseModel.newState("UNDEPLOYED", "undeployed", false);
			stCaseModel.newState("UNDEPLOYING", "undeploying", true);

			Attribute aCaseId = stCaseType.newAttribute();
			aCaseId.setName("id");
			aCaseId.setLabel("ID");
			aCaseId.setType("base:Text");
			aCaseId.setIsSummary(true);
			aCaseId.setIsSearchable(true);
			aCaseId.setDescription("This is an id");
			aCaseId.setIsMandatory(true);
			aCaseId.setIsIdentifier(true);

			Attribute aCaseState = stCaseType.newAttribute();
			aCaseState.setName("state");
			aCaseState.setLabel("State");
			aCaseState.setType("base:Text");
			aCaseState.setIsSummary(true);
			aCaseState.setIsSearchable(true);
			aCaseState.setDescription("This is a state");
			aCaseState.setIsMandatory(true);
			aCaseState.setIsState(true);
			aCaseState.setDefaultValue("deployed");

			Attribute aCaseText = stCaseType.newAttribute();
			aCaseText.setName("text");
			aCaseText.setLabel("Text");
			aCaseText.setType("base:Text");
			aCaseText.setIsSummary(true);
			aCaseText.setIsSearchable(true);
			aCaseText.setDescription("This is a text");
			aCaseText.newConstraint("length", "18");

			Attribute aCaseTextArray = stCaseType.newAttribute();
			aCaseTextArray.setName("textArray");
			aCaseTextArray.setLabel("Text Array");
			aCaseTextArray.setType("base:Text");
			aCaseTextArray.setDescription("This is a text array");
			aCaseTextArray.setIsArray(true);

			Attribute aCaseFixedPointNumber = stCaseType.newAttribute();
			aCaseFixedPointNumber.setName("FixedPointNumber");
			aCaseFixedPointNumber.setLabel("FixedPointNumber");
			aCaseFixedPointNumber.setType("base:FixedPointNumber");
			aCaseFixedPointNumber.setIsSummary(true);
			aCaseFixedPointNumber.setIsSearchable(true);
			aCaseFixedPointNumber.setDescription("This is a FixedPointNumber");
			aCaseFixedPointNumber.newConstraint("length", "13");
			aCaseFixedPointNumber.newConstraint("minValue", "-19999.99999");
			aCaseFixedPointNumber.newConstraint("minValueInclusive", "true");
			aCaseFixedPointNumber.newConstraint("maxValue", "299999.99999");
			aCaseFixedPointNumber.newConstraint("maxValueInclusive", "true");
			aCaseFixedPointNumber.newConstraint("decimalPlaces", "5");

			Attribute aCaseFixedPointNumberArray = stCaseType.newAttribute();
			aCaseFixedPointNumberArray.setName("FixedPointNumberArray");
			aCaseFixedPointNumberArray.setLabel("FixedPointNumber Array");
			aCaseFixedPointNumberArray.setType("base:FixedPointNumber");
			aCaseFixedPointNumberArray.setDescription("This is a FixedPointNumber array");
			aCaseFixedPointNumberArray.setIsArray(true);

			Attribute aCaseNumber = stCaseType.newAttribute();
			aCaseNumber.setName("number");
			aCaseNumber.setLabel("Number");
			aCaseNumber.setType("base:Number");
			aCaseNumber.setIsSummary(true);
			aCaseNumber.setIsSearchable(true);
			aCaseNumber.setDescription("This is a number");
			aCaseNumber.newConstraint("minValue", "-999999999999998");
			aCaseNumber.newConstraint("maxValue", "999999999999998");

			Attribute aCaseNumberArray = stCaseType.newAttribute();
			aCaseNumberArray.setName("numberArray");
			aCaseNumberArray.setLabel("Number Array");
			aCaseNumberArray.setType("base:Number");
			aCaseNumberArray.setDescription("This is a number array");
			aCaseNumberArray.setIsArray(true);

			Attribute aCaseDate = stCaseType.newAttribute();
			aCaseDate.setName("date");
			aCaseDate.setLabel("date");
			aCaseDate.setType("base:Date");
			aCaseDate.setDescription("This is a date");

			Attribute aCaseDateArray = stCaseType.newAttribute();
			aCaseDateArray.setName("dateArray");
			aCaseDateArray.setLabel("Date Array");
			aCaseDateArray.setType("base:Date");
			aCaseDateArray.setDescription("This is a date array");
			aCaseDateArray.setIsArray(true);

			Attribute aCaseTime = stCaseType.newAttribute();
			aCaseTime.setName("Time");
			aCaseTime.setLabel("Time");
			aCaseTime.setType("base:Time");
			aCaseTime.setDescription("This is a Time");

			Attribute aCaseTimeArray = stCaseType.newAttribute();
			aCaseTimeArray.setName("TimeArray");
			aCaseTimeArray.setLabel("Time Array");
			aCaseTimeArray.setType("base:Time");
			aCaseTimeArray.setDescription("This is a Time array");
			aCaseTimeArray.setIsArray(true);

			Attribute aCaseDateTimeTZ = stCaseType.newAttribute();
			aCaseDateTimeTZ.setName("DateTimeTZ");
			aCaseDateTimeTZ.setLabel("DateTimeTZ");
			aCaseDateTimeTZ.setType("base:DateTimeTZ");
			aCaseDateTimeTZ.setDescription("This is a DateTimeTZ");

			Attribute aCaseDateTimeTZArray = stCaseType.newAttribute();
			aCaseDateTimeTZArray.setName("DateTimeTZArray");
			aCaseDateTimeTZArray.setLabel("DateTimeTZ Array");
			aCaseDateTimeTZArray.setType("base:DateTimeTZ");
			aCaseDateTimeTZArray.setDescription("This is a DateTimeTZ array");
			aCaseDateTimeTZArray.setIsArray(true);

			Attribute aCaseURI = stCaseType.newAttribute();
			aCaseURI.setName("URI");
			aCaseURI.setLabel("URI");
			aCaseURI.setType("base:URI");
			aCaseURI.setDescription("This is a URI");

			Attribute aCaseURIArray = stCaseType.newAttribute();
			aCaseURIArray.setName("URIArray");
			aCaseURIArray.setLabel("URI Array");
			aCaseURIArray.setType("base:URI");
			aCaseURIArray.setDescription("This is a URI array");
			aCaseURIArray.setIsArray(true);

			Attribute aCaseBoolean = stCaseType.newAttribute();
			aCaseBoolean.setName("BooleanAttrib");
			aCaseBoolean.setLabel("Boolean Attribute");
			aCaseBoolean.setType("base:Boolean");
			aCaseBoolean.setDescription("This is a Boolean");

			Attribute aCaseBooleanArray = stCaseType.newAttribute();
			aCaseBooleanArray.setName("BooleanArray");
			aCaseBooleanArray.setLabel("Boolean Array");
			aCaseBooleanArray.setType("base:Boolean");
			aCaseBooleanArray.setDescription("This is a Boolean array");
			aCaseBooleanArray.setIsArray(true);

			Attribute aType = stCaseType.newAttribute();
			aType.setName("Type");
			aType.setLabel("Type");
			aType.setType("NewType");

			Attribute aTypeArray = stCaseType.newAttribute();
			aTypeArray.setName("TypeArray");
			aTypeArray.setLabel("Type Array");
			aTypeArray.setType("NewType");
			aTypeArray.setIsArray(true);

			//New Type
			StructuredType stType = dm.newStructuredType();
			stType.setName("NewType");
			stType.setLabel("New  Type");
			stType.setDescription("A new  type");

			Attribute aText = stType.newAttribute();
			aText.setName("text");
			aText.setLabel("Text");
			aText.setType("base:Text");
			aText.setDescription("This is a text");
			aText.newConstraint("length", "13");

			Attribute aTextArray = stType.newAttribute();
			aTextArray.setName("textArray");
			aTextArray.setLabel("Text Array");
			aTextArray.setType("base:Text");
			aTextArray.setDescription("This is a text array");
			aTextArray.setIsArray(true);

			Attribute aFixedPointNumber = stType.newAttribute();
			aFixedPointNumber.setName("FixedPointNumber");
			aFixedPointNumber.setLabel("FixedPointNumber");
			aFixedPointNumber.setType("base:FixedPointNumber");
			aFixedPointNumber.setDescription("This is a FixedPointNumber");
			aFixedPointNumber.newConstraint("length", "13");
			aFixedPointNumber.newConstraint("minValue", "-19999.99999");
			aFixedPointNumber.newConstraint("minValueInclusive", "true");
			aFixedPointNumber.newConstraint("maxValue", "299999.99999");
			aFixedPointNumber.newConstraint("maxValueInclusive", "true");
			aFixedPointNumber.newConstraint("decimalPlaces", "5");

			Attribute aFixedPointNumberArray = stType.newAttribute();
			aFixedPointNumberArray.setName("FixedPointNumberArray");
			aFixedPointNumberArray.setLabel("FixedPointNumber Array");
			aFixedPointNumberArray.setType("base:FixedPointNumber");
			aFixedPointNumberArray.setDescription("This is a FixedPointNumber array");
			aFixedPointNumberArray.setIsArray(true);

			Attribute aNumber = stType.newAttribute();
			aNumber.setName("number");
			aNumber.setLabel("Number");
			aNumber.setType("base:Number");
			aNumber.setDescription("This is a number");
			aNumber.newConstraint("minValue", "-999999999999998");
			aNumber.newConstraint("maxValue", "999999999999998");

			Attribute aNumberArray = stType.newAttribute();
			aNumberArray.setName("numberArray");
			aNumberArray.setLabel("Number Array");
			aNumberArray.setType("base:Number");
			aNumberArray.setDescription("This is a number array");
			aNumberArray.setIsArray(true);

		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV4);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.3.0");

		//deploy - successful
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV4);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//update accounts - all case to update done
			caseRefAccounts.add(updateCase(caseRefAccounts.get(5), properties.readValue("updateAccount1V4")));
			readCase(caseRefAccounts.get(9), properties.readValue("updateAccount1V4"));

			//Assert summary and searchable attributes for accounts
			readSummary(caseRefAccounts.get(6), properties.readValue("summaryAccount2V4"));
			readSummary(caseRefAccounts.get(7), properties.readValue("summaryAccount3V4"));
			readSummary(caseRefAccounts.get(8), properties.readValue("summaryAccount0V4"));
			readSummary(caseRefAccounts.get(9), properties.readValue("summaryAccount1V4"));

			//search account cases
			String[] searchAttributes = {"state", "accountID", "accountType", "dateofAccountOpening",
					"timeofAccountOpening"};
			String[] searchTerms = {"@UNKNOWN&", "1000000000001", "FLEXIBLE_ISA", "1954-11-26", "02:22:16"};
			String[] searchTermDQLs = {"'@UNKNOWN&'", "1000000000001", "'FLEXIBLE_ISA'", "1954-11-26", "02:22:16"};
			for (int i = 0; i < searchTerms.length; i++)
			{
				searchCase(searchTerms[i], "FALSE", caseRefAccounts.get(8), properties.readValue("updateAccount0V3"),
						properties.readValue("summaryAccount0V4"));
			}

			findCase(searchAttributes, searchTermDQLs, "FALSE", caseRefAccounts.get(8),
					properties.readValue("updateAccount0V3"), properties.readValue("summaryAccount0V4"));

			//create new case type case
			caseRefNewCasesTypes.addAll(createCases(caseTypeNewCaseType, 1));

			//Assert summary and searchable attributes for new case types
			readCase(caseRefNewCasesTypes.get(0), properties.readValue("readNewCaseType0V4"));
			readSummary(caseRefNewCasesTypes.get(0), properties.readValue("summaryNewCaseType0V4"));

			//link the account case to new case type - fails
			createLinks(caseRefAccounts.get(6), caseRefNewCasesTypes.subList(0, 1), "FAILURE");
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}

		assertNumberOfTypes(8);
	}

	//Test to upgrade to v5 with change change of summary, searchable fields, addition of default values in new case/types, linking of types
	@Test(dependsOnMethods = {
			"upgradeV4"}, description = "Test to upgrade to v5 with change change of summary, searchable fields, addition of default values in new case/types, linking of types")
	public void upgradeV5()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		//make valid changes for upgrade to v5
		//changes searchable and summary fields
		//add new fields into summary, add new fields into searchable, remove existing fields from searchable, remove existing fields from summary
		//add links to the new type
		DataModelModifier modifierV5 = (dm) -> {
			//account type is not searchable but is summary
			Attribute aAccountType = dm.getStructuredTypeByName("Account").getAttributeByName("accountType");
			aAccountType.setIsSearchable(false);

			//dateofAccountClosing is summary and searchable
			Attribute aDteofAccountClosing = dm.getStructuredTypeByName("Account")
					.getAttributeByName("dateofAccountClosing");
			aDteofAccountClosing.setIsSearchable(true);
			aDteofAccountClosing.setIsSummary(true);

			//timeofAccountClosing is summary and searchable
			Attribute aTimeofAccountClosing = dm.getStructuredTypeByName("Account")
					.getAttributeByName("timeofAccountClosing");
			aTimeofAccountClosing.setIsSearchable(true);
			aTimeofAccountClosing.setIsSummary(true);

			//dateofAccountOpening is not summary and is STILL searchable
			Attribute aDteofAccountOpening = dm.getStructuredTypeByName("Account")
					.getAttributeByName("dateofAccountOpening");
			aDteofAccountOpening.setIsSummary(false);

			//accountLiabilityHolding is summary and is searchable
			Attribute aAccountLiabilityHolding = dm.getStructuredTypeByName("Account")
					.getAttributeByName("accountLiabilityHolding");
			aAccountLiabilityHolding.setIsSummary(true);
			aAccountLiabilityHolding.setIsSearchable(true);

			//new case type has 7 searchable attribute
			//text is not searchable
			Attribute aText = dm.getStructuredTypeByName("NewCaseType").getAttributeByName("text");
			aText.setIsSearchable(false);

			//date is searchable
			Attribute aDate = dm.getStructuredTypeByName("NewCaseType").getAttributeByName("date");
			aDate.setIsSearchable(true);

			//time is searchable
			Attribute aTime = dm.getStructuredTypeByName("NewCaseType").getAttributeByName("Time");
			aTime.setIsSearchable(true);

			//DateTimeTZ is searchable
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("NewCaseType").getAttributeByName("DateTimeTZ");
			aDateTimeTZ.setIsSearchable(true);

			//link new case to accounts
			com.tibco.bpm.da.dm.api.Link aNewCaseTypesToAccount = dm.newLink();
			aNewCaseTypesToAccount.getEnd1().setOwner("NewCaseType");
			aNewCaseTypesToAccount.getEnd1().setLabel("Link the account");
			aNewCaseTypesToAccount.getEnd1().setName("linktheaccount");
			aNewCaseTypesToAccount.getEnd1().setIsArray(false);
			aNewCaseTypesToAccount.getEnd2().setOwner("Account");
			aNewCaseTypesToAccount.getEnd2().setLabel("Links the new case types");
			aNewCaseTypesToAccount.getEnd2().setName("linksthenewcasetypes");
			aNewCaseTypesToAccount.getEnd2().setIsArray(true);

			//add default values for new case type
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("text").setDefaultValue("New_Case_Type_Text");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("FixedPointNumber")
					.setDefaultValue("123456.78911");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("number").setDefaultValue("123321");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("date").setDefaultValue("2019-05-24");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("Time").setDefaultValue("14:34:00");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("DateTimeTZ")
					.setDefaultValue("2019-05-24T14:34:00.010Z");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("URI")
					.setDefaultValue("https://www.jira.com?instanceName=tibco");
			dm.getStructuredTypeByName("NewCaseType").getAttributeByName("BooleanAttrib").setDefaultValue("true");

			//and default values for new type
			dm.getStructuredTypeByName("NewType").getAttributeByName("text").setDefaultValue("New_Type_Text");
			dm.getStructuredTypeByName("NewType").getAttributeByName("FixedPointNumber")
					.setDefaultValue("-19999.99999");
			dm.getStructuredTypeByName("NewType").getAttributeByName("number").setDefaultValue("108502174607712");

			//add a type of new type into customer
			Attribute aNewTypeCustomer = dm.getStructuredTypeByName("Customer").newAttribute();
			aNewTypeCustomer.setType("NewType");
			aNewTypeCustomer.setIsArray(false);
			//Setting isMandatory TRUE for a new attribute should not result in a successful upgrade //ACE-2656
			aNewTypeCustomer.setIsMandatory(false);
			aNewTypeCustomer.setName("NewTypeAttribute");
			aNewTypeCustomer.setLabel("New Type Attribute");
		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV5);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.4.0");

		//deploy - successful
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV5);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//update customers
			caseRefCustomers.add(updateCase(caseRefCustomers.get(3), properties.readValue("updateCustomer0V5")));
			caseRefCustomers.add(updateCase(caseRefCustomers.get(1), properties.readValue("updateCustomer1V5")));
			readCase(caseRefCustomers.get(4), properties.readValue("updateCustomer0V5"));
			readCase(caseRefCustomers.get(5), properties.readValue("updateCustomer1V5"));

			//create new case type cases
			caseRefNewCasesTypes.addAll(createCases(caseTypeNewCaseType, 1));

			//Assert summary and searchable attributes for new case types
			readCase(caseRefNewCasesTypes.get(1), properties.readValue("readNewCaseType1V5"));
			readSummary(caseRefNewCasesTypes.get(1), properties.readValue("summaryNewCaseType1V5"));
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}

		assertNumberOfTypes(8);

		//create a case with default data
	}

	//Test to upgrade to v6 with changes in identifiers and number constraint
	@Test(dependsOnMethods = {
			"upgradeV5"}, description = "Test to upgrade to v6 with changes in identifiers and number constraint")
	public void upgradeV6()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		//make valid changes for upgrade to v6
		//addition of new case type with a new auto field and new links added 
		//change of id -> auto to manual, manual to auto, auto : prefix, suffix, min number change
		DataModelModifier modifierV6 = (dm) -> {
			//conversion into manual id - CustomerCID
			//TODO insterted by nashtapu [Aug 23, 2019 10:14:34 AM] - once ACE-1380 is resolved uncomment the line below to make the customerCID of manual type identifier
			//dm.getStructuredTypeByName("Customer").removeIdentifierInitialisationInfo();

			//changing auto identifier - NewCaseType
			IdentifierInitialisationInfo idNewCaseType = dm.getStructuredTypeByName("NewCaseType")
					.getIdentifierInitialisationInfo();
			idNewCaseType.setMinNumLength(3);
			idNewCaseType.setPrefix("Upgraded New Case Type - ");
			idNewCaseType.setSuffix("");
			idNewCaseType.setStart(Long.getLong("99"));

			//verification of ACE-1438 second test case
			//changing age criteria back to true with no change in values but widening in the range - (-1 allowed & 4001 is still allowed but the max value Inclusive is restricted)

			//			V6 (range enhanced)
			//			min value : -1  & minInclusive  : true
			//			max value : 4001  & maxInclusive  : true

			//with the addition of these 2 constraints the test case was failing - fixed under ACE-1438

			Constraint cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
					.getConstraint("minValueInclusive");
			cAge.setValue("true");

			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
					.getConstraint("maxValueInclusive");
			cAge.setValue("true");

		};

		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV6);
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.5.0");

		//deploy - successful
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdAppV6);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//update customers
			//			caseRefCustomers.addAll(createCases(caseTypeCustomer, 1));
			//			readCase(caseRefCustomers.get(6), properties.readValue("createCustomer2V6"));
			//TODO insterted by nashtapu [Aug 23, 2019 10:14:34 AM] - once ACE-1380 is resolved uncomment the 2 lines above and delete the line below
			caseRefCustomers.add("temp value 6th");

			//create new case type cases
			caseRefNewCasesTypes.addAll(createCases(caseTypeNewCaseType, 1));

			//Assert summary and searchable attributes for new case types
			readCase(caseRefNewCasesTypes.get(2), properties.readValue("readNewCaseType2V6"));
			readSummary(caseRefNewCasesTypes.get(2), properties.readValue("summaryNewCaseType2V6"));
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment has failed");
			undeployAllAccounts();
			Assert.fail();
		}

		assertNumberOfTypes(8);

		//create a case with default data
	}

	//Test to upgrade to v6 with changes in fixed point number and number constraints
	@Test(dependsOnMethods = {
			"upgradeV6"}, description = "Test to upgrade to v6 with changes in fixed point number and number constraints")
	public void upgradeV7()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{
		
		
		// --------------------------------------------------------------------------------------------------
		// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
		// The test needs to be updated to cope with this.
		// --------------------------------------------------------------------------------------------------
		
		//
		//		//make valid changes for upgrade to v7
		//
		//		DataModelModifier modifierV7 = (dm) -> {
		//
		//			//		V7 (no change in range)
		//			//		min value : -2  & minInclusive  : false
		//			//		max value : 4002  & minInclusive  : true
		//
		//			Constraint cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
		//					.getConstraint("minValue");
		//			cAge.setValue("-2");
		//
		//			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age").getConstraint("maxValue");
		//			cAge.setValue("4002");
		//
		//			//with the addition of these 2 constraints the test case was failing - fixed under ACE-1438
		//			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
		//					.getConstraint("minValueInclusive");
		//			cAge.setValue("false");
		//
		//			cAge = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("age")
		//					.getConstraint("maxValueInclusive");
		//			cAge.setValue("true");
		//
		//			//ACE-1597
		//			//TODO insterted by nashtapu [May 31, 2019 4:35:18 PM]
		//			//UNCOMMENT AND CHECK AFTER DISCCUSION WITH SIMON
		//			//			V7
		//			//			length : 12
		//			//			decimal places : 3
		//			//			min value : -999999999999.981  & minInclusive  : false
		//			//			max value : 999999999.981  & maxInclusive  : false
		//			//			Constraint cAccountLiability = dm.getStructuredTypeByName("Account")
		//			//					.getAttributeByName("accountLiabilityHolding").getConstraint("minValue");
		//			//			cAccountLiability.setValue("-999999999999.981");
		//			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
		//			//					.getConstraint("maxValue");
		//			//			cAccountLiability.setValue("999999999.981");
		//			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
		//			//					.getConstraint("maxValueInclusive");
		//			//			cAccountLiability.setValue("false");
		//			//			cAccountLiability = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
		//			//					.getConstraint("minValueInclusive");
		//			//			cAccountLiability.setValue("false");
		//		};
		//
		//		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV7);
		//		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.6.0");
		//
		//		//deploy - successful
		//		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		//		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		//		runtimeApplication.setID(deploymentIdAppV7);
		//
		//		try
		//		{
		//			deploymentManager.deploy(runtimeApplication);
		//			caseRefCustomers.add(updateCase(caseRefCustomers.get(4), properties.readValue("updateCustomer0V7")));
		//			caseRefCustomers.add(updateCase(caseRefCustomers.get(5), properties.readValue("updateCustomer1V7")));
		//			//TODO insterted by nashtapu [Aug 23, 2019 10:14:34 AM] - once ACE-1380 is resolved uncomment the line below
		//			//caseRefCustomers.add(updateCase(caseRefCustomers.get(6), properties.readValue("updateCustomer2V7")));
		//			readCase(caseRefCustomers.get(7), properties.readValue("updateCustomer0V7"));
		//			readCase(caseRefCustomers.get(8), properties.readValue("updateCustomer1V7"));
		//			//TODO insterted by nashtapu [Aug 23, 2019 10:14:34 AM] - once ACE-1380 is resolved uncomment the line below
		//			//readCase(caseRefCustomers.get(9), properties.readValue("updateCustomer1V5"));
		//
		//			//Assert summary and searchable attributes for accounts
		//			readSummary(caseRefAccounts.get(6), properties.readValue("summaryAccount2V7"));
		//			readSummary(caseRefAccounts.get(7), properties.readValue("summaryAccount3V7"));
		//			readSummary(caseRefAccounts.get(8), properties.readValue("summaryAccount0V7"));
		//			readSummary(caseRefAccounts.get(9), properties.readValue("summaryAccount1V7"));
		//
		//			//search account cases
		//			String[] searchAttributes = {"state", "accountID", "dateofAccountOpening", "dateofAccountClosing",
		//					"timeofAccountClosing", "timeofAccountOpening", "accountLiabilityHolding"};
		//			String[] searchTerms = {"@TERMINATED&", "2100000000032", "1945-04-30", "2001-08-15", "17:01:12", "13:01:00",
		//					"-1212.98"};
		//			String[] searchTermDQLs = {"'@TERMINATED&'", "2100000000032", "1945-04-30", "2001-08-15", "17:01:12",
		//					"13:01:00", "-1212.98"};
		//			for (int i = 0; i < searchTerms.length; i++)
		//			{
		//				searchCase(searchTerms[i], "", caseRefAccounts.get(7), properties.readValue("updateAccount3V2"),
		//						properties.readValue("summaryAccount3V7"));
		//			}
		//
		//			findCase(searchAttributes, searchTermDQLs, "", caseRefAccounts.get(7),
		//					properties.readValue("updateAccount3V2"), properties.readValue("summaryAccount3V7"));
		//
		//			//link first 2 cases of new case types to account cases - success
		//			createLinks(caseRefAccounts.get(6), caseRefNewCasesTypes.subList(0, 2), "SUCCESS");
		//
		//			//navigate linked cases with new searchable attributes
		//			//navigate using the searchable attribute upgraded in v5
		//			String[] navigateAttributes = {"id", "Time", "date", "state", "number", "DateTimeTZ", "FixedPointNumber"};
		//			String[] navigateTermDQLs = {"'New Case - 0002 - Type'", "14:34:00", "2019-05-24", "'deployed'", "123321",
		//					"2019-05-24T14:34:00.010Z", "123456.78911"};
		//
		//			navigateCase(navigateAttributes, navigateTermDQLs, caseRefAccounts.get(6), caseRefNewCasesTypes.get(1));
		//			findCase(searchAttributes, searchTermDQLs, "", caseRefAccounts.get(7),
		//					properties.readValue("updateAccount3V2"), properties.readValue("summaryAccount3V7"));
		//
		//		}
		//		catch (DeploymentException e)
		//		{
		//			e.printStackTrace();
		//			System.out.println("deployment has failed");
		//			undeployAllAccounts();
		//			Assert.fail();
		//		}
		//
		//		assertNumberOfTypes(8);
		//
		//		//create a case with default data

		//undeploy all
		undeployAllAccounts();
	}
}