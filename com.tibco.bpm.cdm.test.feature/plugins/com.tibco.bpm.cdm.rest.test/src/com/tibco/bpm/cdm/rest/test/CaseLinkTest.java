package com.tibco.bpm.cdm.rest.test;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
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
import com.tibco.bpm.da.dm.api.IdentifierInitialisationInfo;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class CaseLinkTest extends Utils
{
	private static ObjectMapper	om						= new ObjectMapper();

	AccountCreatorFunction		accountCreator			= new AccountCreatorFunction();

	CustomerCreatorFunction		customerCreator			= new CustomerCreatorFunction();

	CaseLinksFunction			linksFunction			= new CaseLinksFunction();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	CaseTypesFunctions			caseTypes				= new CaseTypesFunctions();

	private Response			response				= null;

	private static final String	caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String	caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String	caseTypeNewCaseType		= "com.example.bankdatamodel.NewCaseType";

	private static final String	applicationMajorVersion	= "1";

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

	private void upgrade(String rascVersion) throws IOException, URISyntaxException, DataModelSerializationException,
			RuntimeApplicationException, InterruptedException
	{

		DataModelModifier modifierVx = null;

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("isCase", "TRUE");

		//get the zip file
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), rascVersion);

		if (rascVersion.equalsIgnoreCase("1.0.1.0"))
		{

			modifierVx = (dm) -> {
				//new case type
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

				//link new case to accounts - one to one
				com.tibco.bpm.da.dm.api.Link aNewCaseTypesToAccount = dm.newLink();
				aNewCaseTypesToAccount.getEnd1().setOwner("NewCaseType");
				aNewCaseTypesToAccount.getEnd1().setLabel("account to new case type");
				aNewCaseTypesToAccount.getEnd1().setName("account_to_new_case_type");
				aNewCaseTypesToAccount.getEnd2().setOwner("Account");
				aNewCaseTypesToAccount.getEnd2().setLabel("new case type to account");
				aNewCaseTypesToAccount.getEnd2().setName("new_case_type_to_account");

				//link new case to customers - one to many 			
				com.tibco.bpm.da.dm.api.Link aNewCaseTypesToCustomer = dm.newLink();
				aNewCaseTypesToCustomer.getEnd1().setOwner("NewCaseType");
				aNewCaseTypesToCustomer.getEnd1().setLabel("customers to new case types");
				aNewCaseTypesToCustomer.getEnd1().setName("customers_to_new_case_types");
				//as ACE-1571 is resolved, changed the setIsArray back to false i.e. uncommented the next line and deleted the line after that
				aNewCaseTypesToCustomer.getEnd1().setIsArray(false);
				aNewCaseTypesToCustomer.getEnd2().setOwner("Customer");
				aNewCaseTypesToCustomer.getEnd2().setLabel("new case types to customers");
				aNewCaseTypesToCustomer.getEnd2().setName("new_case_types_to_customers");
				aNewCaseTypesToCustomer.getEnd2().setIsArray(true);

			};
		}

		else if (rascVersion.equalsIgnoreCase("1.0.2.0"))
		{

			modifierVx = (dm) -> {
				//new case type
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

				//link new case to accounts - one to one
				com.tibco.bpm.da.dm.api.Link aNewCaseTypesToAccount = dm.newLink();
				aNewCaseTypesToAccount.getEnd1().setOwner("NewCaseType");
				aNewCaseTypesToAccount.getEnd1().setLabel("account to new case type");
				aNewCaseTypesToAccount.getEnd1().setName("account_to_new_case_type");
				aNewCaseTypesToAccount.getEnd2().setOwner("Account");
				aNewCaseTypesToAccount.getEnd2().setLabel("new case type to account");
				aNewCaseTypesToAccount.getEnd2().setName("new_case_type_to_account");

				//link new case to customers - many to many 			
				com.tibco.bpm.da.dm.api.Link aNewCaseTypesToCustomer = dm.newLink();
				aNewCaseTypesToCustomer.getEnd1().setOwner("NewCaseType");
				aNewCaseTypesToCustomer.getEnd1().setLabel("customers to new case types");
				aNewCaseTypesToCustomer.getEnd1().setName("customers_to_new_case_types");
				aNewCaseTypesToCustomer.getEnd1().setIsArray(true);
				aNewCaseTypesToCustomer.getEnd2().setOwner("Customer");
				aNewCaseTypesToCustomer.getEnd2().setLabel("new case types to customers");
				aNewCaseTypesToCustomer.getEnd2().setName("new_case_types_to_customers");
				aNewCaseTypesToCustomer.getEnd2().setIsArray(true);

				//link new case to new case : self - one to one
				com.tibco.bpm.da.dm.api.Link aNewCaseTypesToNewCaseType = dm.newLink();
				aNewCaseTypesToNewCaseType.getEnd1().setOwner("NewCaseType");
				aNewCaseTypesToNewCaseType.getEnd1().setLabel("new case type to new case type 1");
				aNewCaseTypesToNewCaseType.getEnd1().setName("new_case_type_to_new_case_type_1");
				aNewCaseTypesToNewCaseType.getEnd2().setOwner("NewCaseType");
				aNewCaseTypesToNewCaseType.getEnd2().setLabel("new case type to new case type 2");
				aNewCaseTypesToNewCaseType.getEnd2().setName("new_case_type_to_new_case_type_2");
			};
		}

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierVx);

		//deploy Vx
		BigInteger deploymentIdApp2 = BigInteger.valueOf(8);
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//the test should never reach here
			response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", "10");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//assert that there are 3 structured types returned
			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 3, "incorrect number of types");

		}
		catch (DeploymentException | InternalException e)
		{
			System.out.println("upgrade should be successful");
			Assert.fail();
		}

	}

	private void assertErrorResponseDuplicateCaseReference(String caseReference, String caseReferenceTarget,

			String linkName, Response response) throws IOException
	{
		String errorMessage = "";

		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		Assert.assertEquals(response.getStatusCode(), properties.getDuplicateCaseReferenceLink().errorResponse,
				"incorrect response");

		errorMessage = properties.getDuplicateCaseReferenceLink().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReference);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReferenceTarget);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getDuplicateCaseReferenceLink().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 3, "incorrect number of context attributes");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "targetCaseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReferenceTarget,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "caseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), caseReference,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "linkName",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), linkName,
				"incorrect name of context attribute");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace")
				.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Case '" + caseReference
						+ "' cannot be linked twice to '" + caseReferenceTarget + "' via link '" + linkName + "'"),
				true, "incorrect stacktrace");
	}

	private void assertErrorResponseAlreadyLinked(String caseReference, String caseReferenceTarget, String linkName,
			Response response) throws IOException
	{
		String errorMessage = "";

		System.out.println(response.asString()); // useful for debugging
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		Assert.assertEquals(response.getStatusCode(), properties.getCaseReferenceAlreadyLinked().errorResponse,
				"incorrect response");

		errorMessage = properties.getCaseReferenceAlreadyLinked().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReference);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReferenceTarget);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getCaseReferenceAlreadyLinked().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 3, "incorrect number of context attributes");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "targetCaseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReferenceTarget,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "caseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), caseReference,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "linkName",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), linkName,
				"incorrect name of context attribute");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(
				jsonPath.getString("stackTrace")
						.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Case " + caseReference
								+ " is already linked to " + caseReferenceTarget + " via link '" + linkName + "'"),
				true, "incorrect stacktrace");
	}

	private void assertErrorResponseLinkDoesNotExist(String caseType, String majorVersion, String linkName,
			Response response) throws IOException
	{
		String errorMessage = "";

		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		Assert.assertEquals(response.getStatusCode(), properties.getLinkNameDoesNotExist().errorResponse,
				"incorrect response");

		errorMessage = properties.getLinkNameDoesNotExist().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseType);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", majorVersion);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getLinkNameDoesNotExist().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 3, "incorrect number of context attributes");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "typeName",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseType,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "majorVersion",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), majorVersion,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "linkName",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), linkName,
				"incorrect name of context attribute");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace")
				.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Type '" + caseType + "' (major version "
						+ majorVersion + ") does not have a link called '" + linkName + "'"),
				true, "incorrect stacktrace");
	}

	private void assertErrorResponseCaseReferenceNotLinked(String caseReference, String targetCaseReference,
			String linkName, Response response) throws IOException
	{
		String errorMessage = "";

		JsonPath jsonPath;

		Assert.assertEquals(response.getStatusCode(), properties.getCaseReferenceNotLinked().errorResponse,
				"incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		errorMessage = properties.getCaseReferenceNotLinked().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseReference);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", targetCaseReference);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);

		Assert.assertEquals(jsonPath.getString("errorCode"), properties.getCaseReferenceNotLinked().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "targetCaseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), targetCaseReference,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "caseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), caseReference,
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "linkName",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), linkName,
				"incorrect name of context attribute");

		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(
				jsonPath.getString("stackTrace")
						.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Case " + caseReference
								+ " is not linked to " + targetCaseReference + " via link '" + linkName + "'"),
				true, "incorrect stacktrace");
	}

	private void assertErrorResponseNotAnArrayArray(String caseType, String majorVersion, String linkName,
			String linkOppositeEnd, Response response, boolean linkThroughOppositeEnd, int size) throws IOException
	{
		String errorMessage = "";

		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		if (linkThroughOppositeEnd == true && size == 0)
		{
			Assert.assertEquals(response.getStatusCode(), properties.getLinkOppositeEndNotArray().errorResponse,
					"incorrect response");

			errorMessage = properties.getLinkOppositeEndNotArray().errorMsg;
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseType);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", majorVersion);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkOppositeEnd);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getLinkOppositeEndNotArray().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 4,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "oppositeLinkName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), linkOppositeEnd,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "typeName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), caseType,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "majorVersion",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), majorVersion,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[3].name"), "linkName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[3].value"), linkName,
					"incorrect name of context attribute");

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace")
							.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Type '" + caseType
									+ "' (major version " + majorVersion + ") '" + linkName + "' link's opposite end ('"
									+ linkOppositeEnd + "') is not an array, so cannot be linked from multiple cases"),
					true, "incorrect stacktrace");
		}

		else if (linkThroughOppositeEnd == false && (linkOppositeEnd.equals("") || linkOppositeEnd.contains("")))
		{
			Assert.assertEquals(response.getStatusCode(), properties.getLinkNotArray().errorResponse,
					"incorrect response");

			errorMessage = properties.getLinkNotArray().errorMsg;
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseType);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", majorVersion);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", linkName);
			errorMessage = errorMessage.replaceFirst("\\{.*?\\}", String.valueOf(size));

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getLinkNotArray().errorCode,
					"incorrect error code");
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
			Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
			Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 4,
					"incorrect number of context attributes");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "size",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), String.valueOf(size),
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "typeName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), caseType,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].name"), "majorVersion",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[2].value"), majorVersion,
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[3].name"), "linkName",
					"incorrect name of context attribute");
			Assert.assertEquals(jsonPath.getString("contextAttributes[3].value"), linkName,
					"incorrect name of context attribute");

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace")
							.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Type '" + caseType
									+ "' (major version " + majorVersion + ") link '" + linkName
									+ "' is not an array, so cannot link to " + String.valueOf(size) + " cases"),
					true, "incorrect stacktrace");
		}

	}

	private LinksPostRequestBody formulateBodyForLinkCases(int beginIndex, int endIndex, List<String> caseRefs,
			String linkName)
	{
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		for (int caseRefIterator = Integer.valueOf(beginIndex); caseRefIterator < Integer
				.valueOf(endIndex); caseRefIterator++)
		{
			Link link = new Link();
			link.setCaseReference(caseRefs.get(caseRefIterator));
			link.setName(linkName);
			linksPostRequestBody.add(link);
		}

		return linksPostRequestBody;
	}

	private void assertLinkedCases(int beginIndex, int endIndex, int totalLinks, List<String> caseRefs, int offset,
			String linkName, Response response) throws JsonParseException, JsonMappingException, IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		if (beginIndex < endIndex)
		{
			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), totalLinks, String.valueOf(totalLinks) + " cases are returned");

			for (int caseRefIterator = Integer.valueOf(beginIndex); caseRefIterator < Integer

					.valueOf(endIndex); caseRefIterator++)
			{
				if (offset == 0)
				{
					Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(), caseRefs.get(caseRefIterator),
							"linked case reference is incorrect");
				}
				else
				{
					Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
							caseRefs.get(caseRefIterator + offset), "linked case reference is incorrect");
				}
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}
		}

		else if (beginIndex == endIndex)
		{
			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), totalLinks, String.valueOf(totalLinks) + " cases are returned");
			Assert.assertEquals(response.asString(), "[]", "response should be empty");
			Assert.assertEquals(obj.isEmpty(), true, "object should be empty");
		}
	}

	@Test
	public void testLinkAndUnlinkSingleCase() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create 1x Policy
			CasesPostRequestBody createBody = new CasesPostRequestBody();
			createBody.setCaseType("org.policycorporation.Policy");
			createBody.setApplicationMajorVersion(1);
			createBody.setCasedata(Collections.singletonList("{\"state\":\"CREATED\",\"number\":777}"));
			String payload = om.writeValueAsString(createBody);
			Response response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(URL_CASES);

			CasesPostResponseBody responseBody = response.as(CasesPostResponseBody.class);
			Assert.assertNotNull(responseBody);
			Assert.assertEquals(responseBody.size(), 1);
			String polRef = responseBody.get(0);

			// Fetch links (none exist)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.get(URL_CASES + "/" + polRef + "/links?$top=999999");
			Assert.assertEquals(response.asString(), "[]");

			// Create 1x Person
			createBody = new CasesPostRequestBody();
			createBody.setCaseType("org.policycorporation.Person");
			createBody.setApplicationMajorVersion(1);
			createBody.setCasedata(Collections.singletonList("{\"personState\":\"ALIVE\"}"));
			payload = om.writeValueAsString(createBody);
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(URL_CASES);
			responseBody = response.as(CasesPostResponseBody.class);
			Assert.assertNotNull(responseBody);
			Assert.assertEquals(responseBody.size(), 1);
			String perRef = responseBody.get(0);

			// Link Policy to Person via 'holder'
			LinksPostRequestBody linkBody = new LinksPostRequestBody();
			Link link = new Link();
			link.setCaseReference(perRef);
			link.setName("holder");
			linkBody.add(link);
			payload = om.writeValueAsString(linkBody);
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload)
					.post(URL_CASES + "/" + polRef + "/links");
			Assert.assertEquals(response.getStatusCode(), 200);

			// Fetch links for Policy (1 exists - that we just created)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.get(URL_CASES + "/" + polRef + "/links?$top=999999");
			ArrayNode arrayNode = response.as(ArrayNode.class);
			Assert.assertNotNull(arrayNode);
			Assert.assertEquals(arrayNode.size(), 1);
			JsonNode jsonNode = arrayNode.get(0);
			Assert.assertTrue(jsonNode instanceof ObjectNode);
			ObjectNode objectNode = (ObjectNode) jsonNode;
			Assert.assertEquals(objectNode.get("name").asText(), "holder");
			Assert.assertEquals(objectNode.get("caseReference").asText(), perRef);

			// Delete links (1 exists)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.delete(URL_CASES + "/" + polRef + "/links");
			Assert.assertEquals(response.getStatusCode(), 200);

			// Fetch links for Policy (none exist, as we just deleted the one we created)
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.get(URL_CASES + "/" + polRef + "/links?$top=999999");
			arrayNode = response.as(ArrayNode.class);
			Assert.assertNotNull(arrayNode);
			Assert.assertEquals(arrayNode.size(), 0);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	//Test link, unlink and purge of many to one - accounts to customer
	@Test(description = "Test link, unlink and purge of many to one - accounts to customer")
	public void testLinkAndUnlinkMultipleCases() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException,
			ArgumentException, DataModelSerializationException, InterruptedException

	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempRefs;
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 100 accounts of each type
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(1000, 100, "@ACTIVE&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(2000, 100, "@FROZEN&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(3000, 100,
					"@TREMPORARILY DEACTIVATED&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(4000, 100, "@WAITING FOR APPROVAL&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(5000, 100, "@REINSTATED&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(6000, 100,
					"@WAITING FOR CANCELLATION&");
			caseRefsAccounts.addAll(tempRefs);

			//Thread.sleep(CASES_TO_UPDATE);

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//get links when no cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "10");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 0, "no cases are linked");

			//link first 100 accounts to the customer - ACTIVE state
			linksPostRequestBody = formulateBodyForLinkCases(0, 100, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when first 100 cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "1000");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 0, "holdstheaccounts", response);

			//link next 100 accounts to the customer - FROZEN state
			linksPostRequestBody = formulateBodyForLinkCases(100, 200, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when next 100 cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "100", "300");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 100, "holdstheaccounts", response);

			//link next 100 accounts to the customer - TERMINATED state
			linksPostRequestBody = formulateBodyForLinkCases(200, 300, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when next 100 cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "200", "400");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 200, "holdstheaccounts", response);

			//link next 100 accounts to the customer - WAITING FOR APPROVAL state
			linksPostRequestBody = formulateBodyForLinkCases(300, 400, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when next 100 cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "300", "500");
			assertLinkedCases(0, 100, 100, caseRefsAccounts, 300, "holdstheaccounts", response);

			//get links all linked cases
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "500");
			assertLinkedCases(0, 400, 400, caseRefsAccounts, 0, "holdstheaccounts", response);

			//add the filter map
			filterMap.put("name", "holdstheaccounts");

			//get all links with filter name passed
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "201", "48");
			assertLinkedCases(0, 48, 48, caseRefsAccounts, 201, "holdstheaccounts", response);

			//get the linked cases from other side (accounts)
			filterMap.clear();
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(146), "0", "1000");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);

			//get the linked cases from other side (accounts) with name in filter
			filterMap.clear();
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(146), "0", "1000");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);

			//link all remaining accounts to the customer - CANCELLED & WAITING FOR CANCELLATION
			linksPostRequestBody = formulateBodyForLinkCases(400, 600, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get all links with filter name passed
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "", "1000");
			assertLinkedCases(0, 600, 600, caseRefsAccounts, 0, "holdstheaccounts", response);

			//purge accounts in @REINSTATED& state
			filterMap.clear();
			filterMap.put("caseType", caseTypeAccount);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@REINSTATED&");
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "100", "incorrect number of cases are purged");

			//verify that the number of links in get links are reduced
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "", "600");
			assertLinkedCases(0, 400, 500, caseRefsAccounts, 0, "holdstheaccounts", response);
			assertLinkedCases(400, 500, 500, caseRefsAccounts, 100, "holdstheaccounts", response);

			//purge accounts in @WAITING FOR CANCELLATION& state
			filterMap.clear();
			filterMap.put("caseType", caseTypeAccount);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "@WAITING FOR CANCELLATION&");
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "100", "incorrect number of cases are purged");

			//verify that the number of links in get links are reduced
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "", "600");
			assertLinkedCases(0, 400, 400, caseRefsAccounts, 0, "holdstheaccounts", response);

			//purge customer in VERYACTIVE50KABOVE state
			filterMap.clear();
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("caseState", "VERYACTIVE50KABOVE");
			response = cases.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//verify that the link on accounts is not present anymore
			filterMap.clear();
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(10), "", "10");
			assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "heldby", response);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
	// The test needs to be updated to cope with this.
	// --------------------------------------------------------------------------------------------------
	//Test link, unlink and purge of self links
	//@Test(description = "Test link, unlink and purge of self links")
	public void testLinkAndUnlinkSelfLinksAndLinkFilter() throws DeploymentException, PersistenceException,
			InternalException, IOException, URISyntaxException, RuntimeApplicationException, CasedataException,
			ReferenceException, ArgumentException, DataModelSerializationException, InterruptedException
	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempRefs = new ArrayList<>();
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 10 accounts of each type
			caseRefsAccounts = accountCreator.createAccountsArrayData(12, "accounts");

			//Thread.sleep(CASES_TO_UPDATE);

			//create 9 customers
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "ACTIVE10KTO50K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "REGULAR1KTO10K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "SELDOM0TO1K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "ACTIVEBUTONHOLD"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "INACTIVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "BARREDORTERMINATED"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "CANCELLED"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "TRIAL"));
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//link first 5 accounts to 1st customer
			linksPostRequestBody = formulateBodyForLinkCases(0, 5, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links for first 5 accounts linked to 1st customer
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 5, 5, caseRefsAccounts, 0, "holdstheaccounts", response);

			//link next 5 accounts to 2nd customer
			linksPostRequestBody = formulateBodyForLinkCases(5, 10, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(1), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links for next 5 accounts linked to 2nd customer
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(1), "0", "10");
			assertLinkedCases(0, 5, 5, caseRefsAccounts, 5, "holdstheaccounts", response);

			//try to add 11 parents (excluding itself) for 1 account - should not be allowed
			linksPostRequestBody = formulateBodyForLinkCases(0, 11, caseRefsAccounts, "parent_account");
			response = linksFunction.createLinks(caseRefsAccounts.get(11), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeAccount, applicationMajorVersion, "parent_account", "", response,
					false, 11);

			//try to add 2 parents (including itself) for 1 account - should not be allowed
			linksPostRequestBody = formulateBodyForLinkCases(10, 12, caseRefsAccounts, "parent_account");
			response = linksFunction.createLinks(caseRefsAccounts.get(11), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeAccount, applicationMajorVersion, "parent_account", "", response,
					false, 2);

			//link first 10 accounts to 10th account
			linksPostRequestBody = formulateBodyForLinkCases(0, 10, caseRefsAccounts, "children_of_the_accounts");
			response = linksFunction.createLinks(caseRefsAccounts.get(9), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links for 10 accounts that are linked to the 10th account - filter: children_of_the_accounts
			filterMap.put("name", "children_of_the_accounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(9), "0", "15");
			assertLinkedCases(0, 10, 10, caseRefsAccounts, 0, "children_of_the_accounts", response);

			//get links for 10 accounts that are linked to the 10th account - filter: parent_account
			filterMap.put("name", "parent_account");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(9), "0", "15");
			assertLinkedCases(0, 1, 1, caseRefsAccounts, 9, "parent_account", response);

			//get links for 10 accounts that are linked to the 10th account - filter: heldby
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(9), "0", "15");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 1, "heldby", response);

			//get links for 10 accounts that are linked to the 10th account - filter: no filter
			filterMap.clear();
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(9), "0", "15");
			assertLinkedCases(0, 10, 12, caseRefsAccounts, 0, "children_of_the_accounts", response);
			assertLinkedCases(10, 11, 12, caseRefsCustomers, -9, "heldby", response);
			assertLinkedCases(11, 12, 12, caseRefsAccounts, -2, "parent_account", response);

			//link first 3 customers to 4th customer
			linksPostRequestBody = formulateBodyForLinkCases(0, 3, caseRefsCustomers, "social_circle_of");
			response = linksFunction.createLinks(caseRefsCustomers.get(3), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link 5th, 6th, 7th and 8th customers to 5th customer
			linksPostRequestBody = formulateBodyForLinkCases(5, 9, caseRefsCustomers, "socially_mingle_with");
			response = linksFunction.createLinks(caseRefsCustomers.get(5), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links for first 3 customers - filter: socially_mingle_with 
			filterMap.put("name", "socially_mingle_with");
			for (int i = 0; i < 3; i++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(i), "0", "5");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 3, "socially_mingle_with", response);
			}
			filterMap.clear();

			//get links for first 3 customers - filter: social_circle_of
			filterMap.put("name", "social_circle_of");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(3), "0", "5");
			assertLinkedCases(0, 3, 3, caseRefsCustomers, 0, "social_circle_of", response);

			//get links for last 4 customers - filter: socially_mingle_with 
			filterMap.put("name", "social_circle_of");
			for (int i = 0; i < 4; i++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(i + 5), "0", "10");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 5, "social_circle_of", response);
			}
			filterMap.clear();

			//get links for last 4 customers - filter: no filter 
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(5), "0", "10");
			assertLinkedCases(0, 1, 5, caseRefsCustomers, 5, "social_circle_of", response);
			assertLinkedCases(1, 5, 5, caseRefsCustomers, 4, "socially_mingle_with", response);

			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 9, caseRefsCustomers, "socially_mingle_with");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(4), caseRefsCustomers.get(4), "socially_mingle_with",
					response);

			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 9, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(4), caseRefsCustomers.get(4), "socially_mingle_with",
					response);

			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 9, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(4), caseRefsCustomers.get(4), "socially_mingle_with",
					response);

			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 3, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(5, 9, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			assertErrorResponseDuplicateCaseReference(caseRefsCustomers.get(4), caseRefsAccounts.get(10),
					"holdstheaccounts", response);

			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 3, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(5, 8, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 11, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeCustomer, applicationMajorVersion, "holdstheaccounts", "heldby",
					response, true, 0);

			//4th customer become extremely important as all accounts and customers are linked to it
			//but 9th customer does not like it back and does not hold 12th account 
			//link already linked cases to self 
			linksPostRequestBody = formulateBodyForLinkCases(0, 8, caseRefsCustomers, "social_circle_of");
			linksPostRequestBody.addAll(formulateBodyForLinkCases(0, 3, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(5, 8, caseRefsCustomers, "socially_mingle_with"));
			linksPostRequestBody.addAll(formulateBodyForLinkCases(10, 12, caseRefsAccounts, "holdstheaccounts"));
			response = linksFunction.createLinks(caseRefsCustomers.get(4), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//keep finding links 
			//what all links are present?
			// customers 0 to 7 are in social circle of customer 4
			// customer 4 have socially mingles with customer 0 to 7
			// customer 0 to 2 and 5 to 7 socially mingle with customer 4
			// customer 4 hold accounts 11 and 12
			// accounts 11 and 12 are held by customer 4
			//use filters heldby, holdstheaccount, social_circle_of, socially_mingle_with, children_of_the_accounts, parent_account

			//get links for first 3 customers - filter: socially_mingle_with
			filterMap.put("name", "socially_mingle_with");
			for (int i = 0; i < 3; i++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(i), "0", "5");
				assertLinkedCases(0, 2, 2, caseRefsCustomers, 3, "socially_mingle_with", response);
			}
			filterMap.clear();

			filterMap.put("name", "socially_mingle_with");
			for (int i = 0; i < 3; i++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(i), "1", "2");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 4, "socially_mingle_with", response);
			}
			filterMap.clear();

			//get links for first 3 customers - filter: social_circle_of
			//customer 4 have in social circle customer 0 to 2, 3 and 5 to 7
			filterMap.put("name", "social_circle_of");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "4");
			assertLinkedCases(0, 4, 4, caseRefsCustomers, 0, "social_circle_of", response);
			filterMap.clear();

			filterMap.put("name", "social_circle_of");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "4", "5");
			assertLinkedCases(0, 4, 4, caseRefsCustomers, 4, "social_circle_of", response);
			filterMap.clear();

			//get links for 4th customer - filter: holdstheaccounts
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "10");
			assertLinkedCases(0, 2, 2, caseRefsAccounts, 10, "holdstheaccounts", response);
			filterMap.clear();

			//get links for 4th customer - filter: heldby
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(10), "0", "10");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 4, "heldby", response);
			filterMap.clear();

			//get links for 4th customer - filter: heldby
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(11), "0", "10");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 4, "heldby", response);
			filterMap.clear();

			//all links for customer 4
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "100");
			assertLinkedCases(0, 2, 17, caseRefsAccounts, 10, "holdstheaccounts", response);
			assertLinkedCases(2, 10, 17, caseRefsCustomers, -2, "social_circle_of", response);
			assertLinkedCases(10, 13, 17, caseRefsCustomers, -10, "socially_mingle_with", response);
			assertLinkedCases(13, 17, 17, caseRefsCustomers, -9, "socially_mingle_with", response);

			//all links for customer 3
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(3), "0", "100");
			assertLinkedCases(0, 3, 4, caseRefsCustomers, 0, "social_circle_of", response);
			assertLinkedCases(3, 4, 4, caseRefsCustomers, 1, "socially_mingle_with", response);

			//delete links with self and with accounts with filter name and IN
			response = linksFunction.deleteLinks(caseRefsCustomers.get(4), "holdstheaccounts", "IN",
					caseRefsAccounts.subList(10, 12));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking for empty response");

			//verify that account links are deleted
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "100");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "holdstheaccounts", response);
			filterMap.clear();

			//assertion on accounts to see if the links are deleted 
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(10), "0", "100");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "heldby", response);
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsAccounts.get(11), "0", "100");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "heldby", response);
			filterMap.clear();

			//delete links with self and with customers with filter name
			response = linksFunction.deleteLinks(caseRefsCustomers.get(4), "social_circle_of", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "8", "checking response for number of links deleted");

			//assertion on customers that links are deleted
			filterMap.put("name", "social_circle_of");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "10");
			assertLinkedCases(0, 0, 0, caseRefsCustomers, 0, "social_circle_of", response);
			filterMap.clear();

			//socially mingle with customers for first 3 will only be the 4th customer (customer(3))
			filterMap.put("name", "socially_mingle_with");
			for (int i = 0; i < 3; i++)
			{
				response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(i), "0", "5");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 3, "socially_mingle_with", response);
			}
			filterMap.clear();

			//delete links with self and with customers with filter and EQ
			filterMap.put("name", "socially_mingle_with");
			for (int i = 0; i < 3; i++)
			{
				response = linksFunction.deleteLinks(caseRefsCustomers.get(4), "socially_mingle_with", "EQ",
						caseRefsCustomers.subList((i + 5), 8));
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");
			}
			filterMap.clear();

			//all links for customer 4
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "100");
			assertLinkedCases(0, 3, 3, caseRefsCustomers, 0, "socially_mingle_with", response);

			//delete all links no filter specified 
			//Need another test like this since only socially_mingle_with links are left
			response = linksFunction.deleteLinks(caseRefsCustomers.get(4), "", "NONE", new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "3", "checking response for number of links deleted");

			//check customer 4 is aloof again
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(4), "0", "100");
			assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);

			//customer 5 to only have 1 link to customer 9
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(5), "0", "100");
			assertLinkedCases(0, 1, 5, caseRefsCustomers, 5, "social_circle_of", response);
			assertLinkedCases(1, 5, 5, caseRefsCustomers, 4, "socially_mingle_with", response);

			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(6), "0", "100");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 5, "social_circle_of", response);

			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(7), "0", "100");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 5, "social_circle_of", response);

			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(8), "0", "100");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 5, "social_circle_of", response);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	//Test link and unlink when the cases are updated
	@Test(description = "Test link and unlink when the cases are updated")
	public void testLinkAndUnlinkUpdateCases() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			DataModelSerializationException, InterruptedException, CasedataException, ReferenceException

	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> updatedCaseRefAccounts = new ArrayList<>();
		List<String> updatedCaseRefCustomers = new ArrayList<>();
		List<String> tempCaseRefHolder = new ArrayList<>();
		List<String> tempRefs;
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 100 accounts of each type
			caseRefsAccounts = accountCreator.createAccountsArrayData(16, "accounts");

			//Thread.sleep(CASES_TO_UPDATE);

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);
			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "SELDOM0TO1K");
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//link first 9 accounts to the customer - ACTIVE state
			linksPostRequestBody = formulateBodyForLinkCases(0, 9, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when first 9 cases that are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 9, 9, caseRefsAccounts, 0, "holdstheaccounts", response);

			//create link from 10th account case to 1st customer case
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, caseRefsCustomers, "heldby");
			response = linksFunction.createLinks(caseRefsAccounts.get(9), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links on accounts when the last link created was on Customer
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "15");
			assertLinkedCases(0, 10, 10, caseRefsAccounts, 0, "holdstheaccounts", response);

			//updating the cases numerous times
			tempCaseRefHolder.clear();
			tempCaseRefHolder.addAll(caseRefsAccounts.subList(0, 10));
			for (int i = 10; i >= 0; i--)
			{
				tempCaseRefHolder = accountCreator.updateAccountsArrayData(tempCaseRefHolder.subList(0, i), i,
						"accounts");
				if (i > 0)
				{
					updatedCaseRefAccounts.add(0, tempCaseRefHolder.get(i - 1));
				}
			}

			//get links on accounts when the last link created was on Customer
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "15");
			assertLinkedCases(0, 10, 10, updatedCaseRefAccounts, 0, "holdstheaccounts", response);

			//get customer from accounts updated list which are updated
			filterMap.clear();
			filterMap.put("name", "heldby");
			for (int i = 0; i < 10; i++)
			{
				//get the linked cases from other side (accounts) with name in filter
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(i), "0", "123");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);
			}

			//get customer from accounts updated list which were original case references
			filterMap.clear();
			filterMap.put("name", "heldby");
			for (int i = 0; i < 10; i++)
			{
				//get the linked cases from other side (accounts) with name in filter
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(i), "0", "123");
				assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "heldby", response);
			}

			//update customer change state 
			updatedCaseRefCustomers = customerCreator.customerChangState(caseRefsCustomers, "VERYACTIVE50KABOVE",
					"CANCELLED");

			//get customer from accounts updated list which are updated
			filterMap.clear();
			filterMap.put("name", "heldby");
			for (int i = 0; i < 10; i++)
			{
				//get the linked cases from other side (accounts) with name in filter
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(i), "0", "123");
				assertLinkedCases(0, 1, 1, updatedCaseRefCustomers, 0, "heldby", response);
			}

			//get customer from accounts updated list which were original case references
			filterMap.clear();
			filterMap.put("name", "heldby");
			for (int i = 0; i < 10; i++)
			{
				//get the linked cases from other side (accounts) with name in filter
				response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefAccounts.get(i), "0", "123");
				assertLinkedCases(0, 1, 1, updatedCaseRefCustomers, 0, "heldby", response);
			}

			//get links on accounts when the last link created was on Customer
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "15");
			assertLinkedCases(0, 10, 10, updatedCaseRefAccounts, 0, "holdstheaccounts", response);

			//get links on accounts when the last link created was on Customer
			response = linksFunction.getLinks(filterMap, filterDql, updatedCaseRefCustomers.get(0), "0", "15");
			assertLinkedCases(0, 10, 10, updatedCaseRefAccounts, 0, "holdstheaccounts", response);
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	//Test create, get and delete link error scenarios
	@Test(description = "Test create, get and delete link error scenarios")
	public void testLinkAndUnlinkErrorScenarios() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException,
			ArgumentException, DataModelSerializationException, InterruptedException
	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		List<String> updatedCaseRefAccounts = new ArrayList<>();
		List<String> tempCaseRefHolder = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempRefs;
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();
		String caseType = "com.example.bankdatamodel.Customer";
		String majorVersion = "1";

		try
		{
			// Create 1 account and 1 customer 
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(1, 1, "@ACTIVE&");
			caseRefsAccounts.addAll(tempRefs);

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);

			//link the same account to the customer twice - duplicate case reference
			//ACE-1482
			LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
			caseRefsAccounts.add(caseRefsAccounts.get(0));
			linksPostRequestBody = formulateBodyForLinkCases(0, 2, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			assertErrorResponseDuplicateCaseReference(caseRefsCustomers.get(0), caseRefsAccounts.get(0),
					"holdstheaccounts", response);
			caseRefsAccounts.remove(1);

			//link non existing case reference - non existing target case reference 
			linksPostRequestBody = new LinksPostRequestBody();
			caseRefsAccounts.add("3700-com.example.bankdatamodel.Account-99901-0");
			linksPostRequestBody = formulateBodyForLinkCases(0, 2, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			properties.assertErrorResponseRefDoesNotExist("3700-com.example.bankdatamodel.Account-99901-0", response);
			caseRefsAccounts.remove(1);

			//linkName does not exist - create, get and delete
			//create an invalid link - link name does not exist
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, caseRefsAccounts, "Non_Existing_Link");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			assertErrorResponseLinkDoesNotExist(caseType, majorVersion, "Non_Existing_Link", response);

			//create a valid link - check successful 
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//consecutive call to link the same case again
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(0), caseRefsAccounts.get(0), "holdstheaccounts",
					response);

			//ACE-1492
			filterMap.put("name", "Non_Existing_Link");
			//get all links with filter name passed
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "1", "0");
			assertErrorResponseLinkDoesNotExist(caseType, majorVersion, "Non_Existing_Link", response);

			//delete non-existing link
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "Non_Existing_Link", "EQ", caseRefsAccounts);
			assertErrorResponseLinkDoesNotExist(caseType, majorVersion, "Non_Existing_Link", response);

			//get links on accounts without top
			filterMap.clear();
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "");
			properties.assertErrorResponseTopMandatory(response);

			//get links on accounts without top
			filterMap.clear();
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(0), "0", "");
			properties.assertErrorResponseTopMandatory(response);

			//create another customer
			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "REGULAR1KTO10K");
			caseRefsCustomers.addAll(tempRefs);

			//try linking an existing linked account to a new customer
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(1), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeCustomer, majorVersion, "holdstheaccounts", "heldby", response,
					true, 0);

			//try linking the new customer to an existing linked account
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(1, 2, caseRefsCustomers, "heldby");
			response = linksFunction.createLinks(caseRefsAccounts.get(0), linksPostRequestBody);
			System.out.println(response.asString());
			assertErrorResponseNotAnArrayArray(caseTypeAccount, majorVersion, "heldby", "", response, false, 2);

			//try linking an updated version of existing linked account to a new customer
			//update 1st account and try to link the new case reference to customer
			tempCaseRefHolder.clear();
			tempCaseRefHolder.addAll(caseRefsAccounts);
			tempCaseRefHolder = accountCreator.updateAccountsArrayData(tempCaseRefHolder, 1, "accounts");
			updatedCaseRefAccounts.add(0, tempCaseRefHolder.get(0));
			//link updated version
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, updatedCaseRefAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(0), updatedCaseRefAccounts.get(0),
					"holdstheaccounts", response);

			//update multiple times
			tempCaseRefHolder.clear();
			tempCaseRefHolder.addAll(updatedCaseRefAccounts);
			tempCaseRefHolder = accountCreator.updateAccountsArrayData(tempCaseRefHolder, 10, "accounts");
			updatedCaseRefAccounts.clear();
			updatedCaseRefAccounts.add(0, tempCaseRefHolder.get(0));
			//link updated version
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 1, updatedCaseRefAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			assertErrorResponseAlreadyLinked(caseRefsCustomers.get(0), updatedCaseRefAccounts.get(0),
					"holdstheaccounts", response);

			//try linking the new customer to an existing updated linked account
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(1, 2, caseRefsCustomers, "heldby");
			response = linksFunction.createLinks(updatedCaseRefAccounts.get(0), linksPostRequestBody);
			System.out.println(response.asString());
			assertErrorResponseNotAnArrayArray(caseTypeAccount, majorVersion, "heldby", "", response, false, 2);

			//try linking 2 customers to a newly created account
			//create a new account  
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(2, 1, "@WAITING FOR APPROVAL&");
			caseRefsAccounts.addAll(tempRefs);
			//link 2 customers to a new account - check successful 
			linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases(0, 2, caseRefsCustomers, "heldby");
			response = linksFunction.createLinks(caseRefsAccounts.get(1), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeAccount, majorVersion, "heldby", "", response, false, 2);
		}
		finally

		{
			forceUndeploy(deploymentId);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// TEMPORARILY DISABLED - As part of ACE-695, it is no longer possible to link a terminal-state case.
	// The test needs to be updated to cope with this.
	// --------------------------------------------------------------------------------------------------
	//Test link and unlink of cases with upgrade and version independent create/delete links")
	//@Test(description = "Test link and unlink of cases with upgrade and version independent create/delete links")
	public void testLinkAndUnlinkWithUpgrade() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, ArgumentException,
			DataModelSerializationException, InterruptedException, CasedataException, ReferenceException

	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> updatesCaseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		List<String> caseRefsNewCaseTypes = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		List<String> tempRefs;
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 100 accounts of each type
			tempRefs = accountCreator.createAccountsArrayData(1000, "accounts");

			caseRefsAccounts.addAll(tempRefs);

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(100, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);

			Map<String, String> filterMap = new HashMap<String, String>();
			Map<String, String> filterDql = new HashMap<String, String>();

			//link first 100 accounts to the customer
			linksPostRequestBody = formulateBodyForLinkCases(0, 100, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(1), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link next 100 accounts to the customer
			linksPostRequestBody = formulateBodyForLinkCases(100, 200, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(2), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//update accounts
			updatesCaseRefsAccounts = accountCreator.updateAccountsArrayData(caseRefsAccounts, 1, "accounts");

			//link next 100 accounts to the customer (version independent call)
			linksPostRequestBody = formulateBodyForLinkCases(200, 400, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(3), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link 400 accounts to account 401 (version independent call)
			linksPostRequestBody = formulateBodyForLinkCases(0, 399, updatesCaseRefsAccounts,
					"children_of_the_accounts");
			response = linksFunction.createLinks(caseRefsAccounts.get(401), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			linksPostRequestBody = formulateBodyForLinkCases(401, 402, updatesCaseRefsAccounts, "parent_account");
			response = linksFunction.createLinks(updatesCaseRefsAccounts.get(400), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			// link customers 4 to 49 to customer 50
			linksPostRequestBody = formulateBodyForLinkCases(4, 50, caseRefsCustomers, "social_circle_of");
			response = linksFunction.createLinks(caseRefsCustomers.get(50), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//upgrade - new link and new case types
			upgrade("1.0.1.0");

			//create 10 new cases
			caseRefsNewCaseTypes = customerCreator.createNewCaseTypeCases(10, "RANDOM");

			// link accounts 401 to 500 to account 501
			linksPostRequestBody = formulateBodyForLinkCases(401, 501, updatesCaseRefsAccounts,
					"children_of_the_accounts");
			response = linksFunction.createLinks(updatesCaseRefsAccounts.get(501), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			// link account 501 to new case type 1
			linksPostRequestBody = formulateBodyForLinkCases(501, 502, updatesCaseRefsAccounts,
					"account_to_new_case_type");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(1), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			// link account 501 to customer 81
			linksPostRequestBody = formulateBodyForLinkCases(81, 82, caseRefsCustomers, "heldby");
			response = linksFunction.createLinks(updatesCaseRefsAccounts.get(501), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//verify that link between new case ref types is not present
			linksPostRequestBody = formulateBodyForLinkCases(1, 2, caseRefsNewCaseTypes,
					"new_case_type_to_new_case_type");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(1), linksPostRequestBody);
			assertErrorResponseLinkDoesNotExist(caseTypeNewCaseType, applicationMajorVersion,
					"new_case_type_to_new_case_type", response);

			//as ACE-1571 is resolved, uncommented the block of the code below
			//i.e. tested whether the linking of 51 to 80 customers could NOT be done to new case type 1
			// verify link customer 51 to 80 to  new case types 1 to 5 cannot be done in v2  
			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(1), linksPostRequestBody);
			assertErrorResponseNotAnArrayArray(caseTypeNewCaseType, applicationMajorVersion,
					"customers_to_new_case_types", "", response, false, 30);

			//update accounts
			updatesCaseRefsAccounts = accountCreator.updateAccountsArrayData(updatesCaseRefsAccounts, 1, "accounts");

			//upgrade - new links
			upgrade("1.0.2.0");

			// link customer 51 to 80 to  new case types 1 to 5
			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(1), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(2), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(3), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(4), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			linksPostRequestBody = formulateBodyForLinkCases(51, 81, caseRefsCustomers, "customers_to_new_case_types");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(5), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link new case type 2 to new case type 1
			linksPostRequestBody = formulateBodyForLinkCases(2, 3, caseRefsNewCaseTypes,
					"new_case_type_to_new_case_type_2");
			response = linksFunction.createLinks(caseRefsNewCaseTypes.get(1), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link customer 2 to customer 3 to 100
			linksPostRequestBody = formulateBodyForLinkCases(3, 100, caseRefsCustomers, "socially_mingle_with");
			response = linksFunction.createLinks(caseRefsCustomers.get(2), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//link customer 2 to new case type 1 to 10
			linksPostRequestBody = formulateBodyForLinkCases(1, 10, caseRefsNewCaseTypes,
					"new_case_types_to_customers");
			response = linksFunction.createLinks(caseRefsCustomers.get(2), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links

			//customer 1 - account 100 updated refs
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(1), "0", "101");
			assertLinkedCases(0, 100, 100, updatesCaseRefsAccounts, 0, "holdstheaccounts", response);
			filterMap.clear();

			//customer 2 - account 100 to 200 updated
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), "0", "101");
			assertLinkedCases(0, 100, 100, updatesCaseRefsAccounts, 100, "holdstheaccounts", response);
			filterMap.clear();

			//accounts 400 - linked to 401 
			filterMap.put("name", "holdstheaccounts");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), "0", "101");
			assertLinkedCases(0, 100, 100, updatesCaseRefsAccounts, 100, "holdstheaccounts", response);
			filterMap.clear();

			// new case type 1 linked to account 501, new case type 2, and 51 to 80 customers, customer 2
			// use filter 1
			filterMap.put("name", "customers_to_new_case_types");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsNewCaseTypes.get(1), "0", "100");
			assertLinkedCases(0, 1, 31, caseRefsCustomers, 2, "customers_to_new_case_types", response);
			assertLinkedCases(1, 31, 31, caseRefsCustomers, 50, "customers_to_new_case_types", response);
			filterMap.clear();
			// use filter 2
			filterMap.put("name", "account_to_new_case_type");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsNewCaseTypes.get(1), "0", "100");
			assertLinkedCases(0, 1, 1, updatesCaseRefsAccounts, 501, "account_to_new_case_type", response);
			filterMap.clear();
			// use filter 3
			filterMap.put("name", "new_case_type_to_new_case_type_1");
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsNewCaseTypes.get(2), "0", "100");
			assertLinkedCases(0, 1, 1, caseRefsNewCaseTypes, 1, "new_case_type_to_new_case_type_1", response);
			filterMap.clear();
			// no filter
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsNewCaseTypes.get(1), "0", "40");
			assertLinkedCases(0, 1, 33, updatesCaseRefsAccounts, 501, "account_to_new_case_type", response);
			assertLinkedCases(1, 2, 33, caseRefsCustomers, 1, "customers_to_new_case_types", response);
			assertLinkedCases(2, 32, 33, caseRefsCustomers, 49, "customers_to_new_case_types", response);
			assertLinkedCases(32, 33, 33, caseRefsNewCaseTypes, -30, "new_case_type_to_new_case_type_2", response);

			// accounts 501 - linked 401 to 500 accounts, new case types 1, customer 81
			// use filter 1
			filterMap.put("name", "children_of_the_accounts");
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "150");
			assertLinkedCases(0, 100, 100, updatesCaseRefsAccounts, 401, "children_of_the_accounts", response);
			filterMap.clear();
			// use filter 2
			filterMap.put("name", "new_case_type_to_account");
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "1000");
			assertLinkedCases(0, 1, 1, caseRefsNewCaseTypes, 1, "new_case_type_to_account", response);
			filterMap.clear();
			// use filter 3
			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "150");
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 81, "heldby", response);
			filterMap.clear();
			// no filter
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "1000");
			assertLinkedCases(0, 100, 102, updatesCaseRefsAccounts, 401, "children_of_the_accounts", response);
			assertLinkedCases(100, 101, 102, caseRefsCustomers, -19, "heldby", response);
			assertLinkedCases(101, 102, 102, caseRefsNewCaseTypes, -100, "new_case_type_to_account", response);

			// customer 2 - customers 3 to 100, new case type 1 to 10, accounts 100 to 200
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), "0", "100000");
			assertLinkedCases(0, 100, 206, updatesCaseRefsAccounts, 100, "holdstheaccounts", response);
			assertLinkedCases(100, 109, 206, caseRefsNewCaseTypes, -99, "new_case_types_to_customers", response);
			assertLinkedCases(109, 206, 206, caseRefsCustomers, -106, "socially_mingle_with", response);

			// delete customers 3 to 5 IN filter 
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "socially_mingle_with", "IN",
					caseRefsCustomers.subList(3, 5));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking response for number of links deleted");
			// delete accounts 100 to 150 IN filter (version independent call)
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "holdstheaccounts", "IN",
					caseRefsAccounts.subList(100, 150));
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "50", "checking response for number of links deleted");
			//links already deleted in the previous call
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "holdstheaccounts", "IN",
					updatesCaseRefsAccounts.subList(100, 150));
			Assert.assertEquals(response.getStatusCode(), invalidStatusCode, "incorrect response");
			// delete new case types 1 to 5
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "new_case_types_to_customers", "IN",
					caseRefsNewCaseTypes.subList(1, 5));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "4", "checking response for number of links deleted");

			// delete
			// delete all - no filter
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "", "NONE", new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "150", "checking response for number of links deleted");

			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), "0", "10");
			assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);

			// delete
			// delete using IN for accounts
			response = linksFunction.deleteLinks(updatesCaseRefsAccounts.get(501), "children_of_the_accounts", "IN",
					updatesCaseRefsAccounts.subList(401, 501));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "100", "checking response for number of links deleted");
			// delete using EQ for case type 1
			response = linksFunction.deleteLinks(caseRefsNewCaseTypes.get(1), "account_to_new_case_type", "EQ",
					updatesCaseRefsAccounts.subList(501, 502));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");
			// delete using name for customer invalid link name		
			response = linksFunction.deleteLinks(caseRefsCustomers.get(81), "holdstheaccount", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), invalidStatusCode, "incorrect response");

			// delete using name for customer valid link name	
			response = linksFunction.deleteLinks(caseRefsCustomers.get(81), "holdstheaccounts", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

			// get all
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "10");
			assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);

			filterMap.put("name", "heldby");
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "150");
			assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);
			filterMap.clear();

			filterMap.put("name", "new_case_type_to_account");
			response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(501), "0", "1000");
			assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);
			filterMap.clear();

			for (int i = 401; i < 501; i++)
			{
				filterMap.put("name", "parent_account");
				response = linksFunction.getLinks(filterMap, filterDql, updatesCaseRefsAccounts.get(i), "0", "150");
				assertLinkedCases(0, 0, 0, new ArrayList<String>(), 0, "", response);
				filterMap.clear();
			}

		}
		finally
		{
			BigInteger deploymentIdApp2 = BigInteger.valueOf(8);
			forceUndeploy(deploymentId);
			forceUndeploy(deploymentIdApp2);
		}
	}

	//Test delete links error scenarios
	@Test(description = "Test delete links error scenarios")
	public void testUnlinkErrorScenarios() throws DeploymentException, PersistenceException, InternalException,
			IOException, URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException,
			ArgumentException, DataModelSerializationException, InterruptedException

	{
		BigInteger deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		List<String> caseRefsAccounts = new ArrayList<>();
		List<String> caseRefsCustomers = new ArrayList<>();
		List<String> formulatedCaseReferences = new ArrayList<>();
		List<String> updatedCaseRefsAccounts = new ArrayList<>();
		List<String> tempCaseRefHolder = new ArrayList<>();
		List<String> updatedCaseRefCustomers = new ArrayList<>();
		caseRefsAccounts.clear();
		caseRefsCustomers.clear();
		formulatedCaseReferences.clear();
		updatedCaseRefsAccounts.clear();
		tempCaseRefHolder.clear();
		updatedCaseRefCustomers.clear();
		List<String> tempRefs;
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			// Create 10 accounts
			tempRefs = accountCreator.createAccountSpecificStateOffsetDefaultCases(1, 10, "@ACTIVE&");
			caseRefsAccounts.addAll(tempRefs);
			tempRefs.clear();

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);
			tempRefs.clear();

			Map<String, String> filterMap = new HashMap<String, String>();

			//link first 7 accounts to the customer - ACTIVE state
			linksPostRequestBody = formulateBodyForLinkCases(0, 7, caseRefsAccounts, "holdstheaccounts");
			response = linksFunction.createLinks(caseRefsCustomers.get(0), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//get links when first 7 cases are linked
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 7, 7, caseRefsAccounts, 0, "holdstheaccounts", response);

			//ACE-2143
			//in - same case twice
			formulatedCaseReferences.add(caseRefsAccounts.get(0));
			formulatedCaseReferences.add(caseRefsAccounts.get(0));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "IN",
					formulatedCaseReferences);
			//TODO insterted by nashtapu [Jul 12, 2019 2:19:58 PM]
			//incorrect error response is appearing here - "Case 6222-com.example.bankdatamodel.Customer-1-0 is not linked to 6212-com.example.bankdatamodel.Account-1-0 via link 'holdstheaccounts'","errorCode":"CDM_REFERENCE_LINK_NOT_LINKED"
			//ideally, it should be - duplicate case reference is mentioned
			//hence, just asserting for the invalid status code
			Assert.assertEquals(response.getStatusCode(), invalidStatusCode, "incorrect response");
			formulatedCaseReferences.clear();

			//in - one case existing and other non-existing
			formulatedCaseReferences.add(caseRefsAccounts.get(0));
			formulatedCaseReferences.add("NON_EXISTING_CASE_REFERENCE");
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "IN",
					formulatedCaseReferences);
			//TODO insterted by nashtapu [Jul 12, 2019 2:19:58 PM]
			//incorrect error response is appearing here - "targetCaseReference in(...) must have a comma-separated list of single-quoted case references, not: '6212-com.example.bankdatamodel.Account-1-0','NON_EXISTING_CASE_REFERENCE'"
			//ideally, it should be - that the case reference is of invalid format
			//hence, just asserting for the invalid status code
			Assert.assertEquals(response.getStatusCode(), invalidStatusCode, "incorrect response");
			formulatedCaseReferences.clear();

			//in - one case existing and other not linked
			formulatedCaseReferences.add(caseRefsAccounts.get(0));
			formulatedCaseReferences.add(caseRefsAccounts.get(7));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "IN",
					formulatedCaseReferences);
			assertErrorResponseCaseReferenceNotLinked(caseRefsCustomers.get(0), caseRefsAccounts.get(7),
					"holdstheaccounts", response);
			formulatedCaseReferences.clear();

			//update 1st account - 4 times
			tempCaseRefHolder.clear();
			tempCaseRefHolder.addAll(caseRefsAccounts.subList(0, 4));
			for (int i = 4; i >= 0; i--)
			{
				tempCaseRefHolder = accountCreator.updateAccountsArrayData(tempCaseRefHolder.subList(0, i), i,
						"accounts");
				if (i > 0)
				{
					updatedCaseRefsAccounts.add(0, tempCaseRefHolder.get(i - 1));
				}
			}

			//in - one case existing and linked but other is of a different version
			//version independent
			formulatedCaseReferences.add(caseRefsAccounts.get(4)); //this case is actually updated now and has become updatesCaseRefsAccounts.get(3)
			formulatedCaseReferences.add(caseRefsAccounts.get(6)); //this is is linked and is not updated
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "IN",
					formulatedCaseReferences);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking for empty response");
			formulatedCaseReferences.clear();
			//links that are exisitng now - 0, 1, 2, 3, 5, (6 & 4 are unlinked)

			//in - incorrect name
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(0)); //this case is actually updated now and has become updatesCaseRefsAccounts.get(1)
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(1)); //this is is linked and is not updated
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccount", "IN",
					formulatedCaseReferences); // note the filter name is not right
			assertErrorResponseLinkDoesNotExist(caseTypeCustomer, applicationMajorVersion, "holdstheaccount", response);
			formulatedCaseReferences.clear();

			//eq - non existing case
			formulatedCaseReferences.add("NON_EXISTING_CASE_REFERENCE");
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			properties.assertErrorResponseInvalidFormat("NON_EXISTING_CASE_REFERENCE", response);
			formulatedCaseReferences.clear();

			//eq - non existing case
			//version independent
			formulatedCaseReferences.add(caseRefsAccounts.get(2)); //this is actually now updatesCaseRefsAccounts.get(2)
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking for empty response");
			formulatedCaseReferences.clear();
			//links that are exisitng now - 0, 1, 3, 5, (2 is unlinked)

			//eq - not linked case
			formulatedCaseReferences.add(caseRefsAccounts.get(8));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			System.out.println(response.asString());
			assertErrorResponseCaseReferenceNotLinked(caseRefsCustomers.get(0), caseRefsAccounts.get(8),
					"holdstheaccounts", response);
			formulatedCaseReferences.clear();

			//eq - incorrect name
			formulatedCaseReferences.add(caseRefsAccounts.get(1));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(0), "oldstheaccounts", "EQ",
					formulatedCaseReferences); // note the filter name is not right
			System.out.println(response.asString());
			assertErrorResponseLinkDoesNotExist(caseTypeCustomer, applicationMajorVersion, "oldstheaccounts", response);
			formulatedCaseReferences.clear();

			//update customer case reference
			updatedCaseRefCustomers = customerCreator.customerChangState(caseRefsCustomers, "VERYACTIVE50KABOVE",
					"CANCELLED");

			//in - incorrect format case reference
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(0));
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(1));
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(3));
			formulatedCaseReferences.add(caseRefsAccounts.get(5));
			response = linksFunction.deleteLinks("NON_EXISTING_CASE_REFERENCE", "holdstheaccounts", "IN",
					formulatedCaseReferences);
			properties.assertErrorResponseInvalidFormat("NON_EXISTING_CASE_REFERENCE", response);
			formulatedCaseReferences.clear();

			//eq - incorrect format case reference
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(0));
			response = linksFunction.deleteLinks("NON_EXISTING_CASE_REFERENCE", "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			properties.assertErrorResponseInvalidFormat("NON_EXISTING_CASE_REFERENCE", response);
			formulatedCaseReferences.clear();

			//filter - incorrect format case reference
			response = linksFunction.deleteLinks("NON_EXISTING_CASE_REFERENCE", "holdstheaccounts", "NONE",
					new ArrayList<>());
			properties.assertErrorResponseInvalidFormat("NON_EXISTING_CASE_REFERENCE", response);
			formulatedCaseReferences.clear();

			//change the customer case reference version to a non-existing one
			String updatedCaseReferenceNonExisting = alterCaseReference(updatedCaseRefCustomers.get(0), "valid");

			tempRefs = customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE");
			caseRefsCustomers.addAll(tempRefs);
			tempRefs.clear();

			//in - different version of case reference - case not linked
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(0));
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(1));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(1), "holdstheaccounts", "IN",
					formulatedCaseReferences);
			assertErrorResponseCaseReferenceNotLinked(caseRefsCustomers.get(1), updatedCaseRefsAccounts.get(0),
					"holdstheaccounts", response);
			formulatedCaseReferences.clear();

			//eq - case reference does not exist
			//version independent
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(3));
			response = linksFunction.deleteLinks(caseRefsCustomers.get(1), "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			assertErrorResponseCaseReferenceNotLinked(caseRefsCustomers.get(1), updatedCaseRefsAccounts.get(3),
					"holdstheaccounts", response);
			formulatedCaseReferences.clear();

			//filter - case reference does not exist
			//version independent
			response = linksFunction.deleteLinks(caseRefsCustomers.get(1), "holdstheaccounts", "NONE",
					new ArrayList<>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "0", "checking for empty response");
			formulatedCaseReferences.clear();

			//in - different version of case reference
			//version independent
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(0));
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(1));
			response = linksFunction.deleteLinks(updatedCaseReferenceNonExisting, "holdstheaccounts", "IN",
					formulatedCaseReferences);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking for empty response");
			formulatedCaseReferences.clear();

			//eq - case reference does not exist
			//version independent
			formulatedCaseReferences.add(updatedCaseRefsAccounts.get(3));
			response = linksFunction.deleteLinks(updatedCaseReferenceNonExisting, "holdstheaccounts", "EQ",
					formulatedCaseReferences);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking for empty response");
			formulatedCaseReferences.clear();

			//filter - case reference does not exist
			//version independent
			response = linksFunction.deleteLinks(updatedCaseReferenceNonExisting, "holdstheaccounts", "NONE",
					new ArrayList<>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking for empty response");
			formulatedCaseReferences.clear();

			//verify that there are no links now
			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(0), "0", "10");
			assertLinkedCases(0, 0, 0, updatedCaseRefsAccounts, 0, "holdstheaccounts", response);

			response = linksFunction.getLinks(filterMap, filterMap, updatedCaseRefCustomers.get(0), "0", "10");
			assertLinkedCases(0, 0, 0, updatedCaseRefsAccounts, 0, "holdstheaccounts", response);

			response = linksFunction.getLinks(filterMap, filterMap, caseRefsCustomers.get(1), "0", "10");
			assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "holdstheaccounts", response);

			for (int i = 0; i < caseRefsAccounts.size(); i++)
			{
				response = linksFunction.getLinks(filterMap, filterMap, caseRefsAccounts.get(i), "0", "10");
				assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "heldBy", response);
			}

			for (int i = 0; i < updatedCaseRefsAccounts.size(); i++)
			{
				response = linksFunction.getLinks(filterMap, filterMap, updatedCaseRefsAccounts.get(i), "0", "10");
				assertLinkedCases(0, 0, 0, caseRefsAccounts, 0, "heldBy", response);
			}
		}
		finally

		{
			forceUndeploy(deploymentId);
		}
	}

}
