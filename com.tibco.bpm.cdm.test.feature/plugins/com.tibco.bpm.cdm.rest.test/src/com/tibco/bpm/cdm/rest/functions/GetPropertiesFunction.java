package com.tibco.bpm.cdm.rest.functions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

/**
 * @author nashtapu
 * 
 */

public class GetPropertiesFunction
{

	public class errorDetails
	{
		public String	errorCode;

		public String	errorMsg;

		public String	errorContextAttribute;

		public int		errorResponse;
	}

	InputStream	inputStream;

	Properties	prop			= new Properties();

	Properties	propCasedata	= new Properties();

	public void initiate() throws IOException
	{
		prop.load(GetPropertiesFunction.class.getResourceAsStream("/Properties/ErrorCode.properties"));
	}

	public void initiateCasedata() throws IOException
	{
		propCasedata.load(GetPropertiesFunction.class.getResourceAsStream("/Properties/Casedata.properties"));
	}

	public errorDetails getTypesTopMandatory() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_TYPES_TOPMANDATORY_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_TYPES_TOPMANDATORY_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_TYPES_TOPMANDATORY_errorResponse"));

		return result;
	}

	public errorDetails getReferenceDoesNotExist() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_NOT_EXIST_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_NOT_EXIST_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_NOT_EXIST_errorResponse"));

		return result;
	}

	public errorDetails getCaseStateMandatory() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_CASE_STATE_MANDATORY_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_CASE_STATE_MANDATORY_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_CASE_STATE_MANDATORY_errorResponse"));

		return result;
	}

	public errorDetails getModificationTimestampMandatory() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_MODIFICATION_TIMESTAMP_MANDATORY_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_MODIFICATION_TIMESTAMP_MANDATORY_errorMsg");
		result.errorResponse = Integer
				.parseInt(prop.getProperty("CDM_API_MODIFICATION_TIMESTAMP_MANDATORY_errorResponse"));

		return result;
	}

	public errorDetails getCaseReferenceInvalidFormat() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_INVALID_FORMAT_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_INVALID_FORMAT_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_INVALID_FORMAT_errorResponse"));

		return result;
	}

	public errorDetails getCaseReferenceVersionMismatch() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_VERSION_MISMATCH_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_VERSION_MISMATCH_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_VERSION_MISMATCH_errorResponse"));

		return result;
	}

	public errorDetails getDuplicateCaseReferenceLink() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_DUPLICATE_LINK_TARGET_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_DUPLICATE_LINK_TARGET_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_DUPLICATE_LINK_TARGET_errorResponse"));

		return result;
	}

	public errorDetails getLinkNameDoesNotExist() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_LINK_NAME_NOT_EXIST_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_LINK_NAME_NOT_EXIST_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_LINK_NAME_NOT_EXIST_errorResponse"));

		return result;
	}

	public errorDetails getTopMandatory() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_TOP_MANDATORY_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_TOP_MANDATORY_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_TOP_MANDATORY_errorResponse"));

		return result;
	}

	public errorDetails getCaseReferenceAlreadyLinked() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_LINK_ALREADY_LINKED_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_LINK_ALREADY_LINKED_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_LINK_ALREADY_LINKED_errorResponse"));

		return result;
	}

	public errorDetails getLinkOppositeEndNotArray() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_LINK_OPPOSITE_NOT_ARRAY_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_LINK_OPPOSITE_NOT_ARRAY_errorMsg");
		result.errorResponse = Integer
				.parseInt(prop.getProperty("CDM_REFERENCE_LINK_OPPOSITE_NOT_ARRAY_errorResponse"));

		return result;
	}

	public errorDetails getLinkNotArray() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_LINK_NOT_ARRAY_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_LINK_NOT_ARRAY_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_LINK_NOT_ARRAY_errorResponse"));

		return result;
	}

	public errorDetails getBadDQL() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_BAD_DQL_errorCode");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_BAD_DQL_errorResponse"));

		return result;
	}

	public errorDetails getBadSelectForTypes() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_TYPES_BAD_SELECT_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_TYPES_BAD_SELECT_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_TYPES_BAD_SELECT_errorResponse"));

		return result;
	}

	public errorDetails getCaseReferenceNotLinked() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_LINK_NOT_LINKED_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_LINK_NOT_LINKED_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_LINK_NOT_LINKED_errorResponse"));

		return result;
	}

	public errorDetails getUnkownType() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_UNKNOWN_TYPE_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_UNKNOWN_TYPE_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REFERENCE_UNKNOWN_TYPE_errorResponse"));

		return result;
	}

	public errorDetails getApiForbidden() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_API_FORBIDDEN_errorCode");
		result.errorMsg = prop.getProperty("CDM_API_FORBIDDEN_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_API_FORBIDDEN_errorResponse"));

		return result;
	}

	public errorDetails getPreventUpdateTerminalState() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REFERENCE_TERMINAL_STATE_PREVENTS_UPDATE_errorCode");
		result.errorMsg = prop.getProperty("CDM_REFERENCE_TERMINAL_STATE_PREVENTS_UPDATE_errorMsg");
		result.errorResponse = Integer
				.parseInt(prop.getProperty("CDM_REFERENCE_TERMINAL_STATE_PREVENTS_UPDATE_errorResponse"));

		return result;
	}

	public errorDetails getBadIsInTerminalStateValue() throws IOException
	{
		initiate();

		errorDetails result = new errorDetails();
		result.errorCode = prop.getProperty("CDM_REST_BAD_IS_IN_TERMINAL_STATE_errorCode");
		result.errorMsg = prop.getProperty("CDM_REST_BAD_IS_IN_TERMINAL_STATE_errorMsg");
		result.errorResponse = Integer.parseInt(prop.getProperty("CDM_REST_BAD_IS_IN_TERMINAL_STATE_errorResponse"));

		return result;
	}

	public void assertErrorResponseRefDoesNotExist(String caseReference, Response response) throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), getReferenceDoesNotExist().errorResponse, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = getReferenceDoesNotExist().errorMsg;
		errorMessage = errorMessage.replaceAll("\\{.*?\\}", caseReference);

		Assert.assertEquals(jsonPath.getString("errorCode"), getReferenceDoesNotExist().errorCode);
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
		Assert.assertEquals(response.getStatusCode(), getReferenceDoesNotExist().errorResponse);

		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(
				jsonPath.getString("stackTrace").contains(
						"com.tibco.bpm.cdm.api.exception.ReferenceException: Case does not exist: " + caseReference),
				true, "incorrect stacktrace");

		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 1, "incorrect number of context attributes");

		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReference);
	}

	public void assertErrorResponseTopMandatory(Response response) throws IOException
	{

		Assert.assertEquals(response.getStatusCode(), getTypesTopMandatory().errorResponse, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		Assert.assertEquals(jsonPath.getString("errorCode"), getTypesTopMandatory().errorCode, "incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), getTypesTopMandatory().errorMsg, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(
				jsonPath.getString("stackTrace")
						.contains("com.tibco.bpm.cdm.api.exception.ArgumentException: Top is mandatory"),
				true, "incorrect stacktrace");
	}

	public void assertErrorResponseInvalidFormat(String caseReference, Response response) throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), getCaseReferenceInvalidFormat().errorResponse,
				"incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = getCaseReferenceInvalidFormat().errorMsg;
		errorMessage = errorMessage.replaceAll("\\{.*?\\}", caseReference);

		Assert.assertEquals(jsonPath.getString("errorCode"), getCaseReferenceInvalidFormat().errorCode,
				"incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "caseReference",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseReference,
				"incorrect name of context attribute");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace").contains(
				"com.tibco.bpm.cdm.api.exception.ReferenceException: Invalid case reference format: " + caseReference),
				true, "incorrect stacktrace");
	}

	public void assertErrorResponseUnkownType(String caseType, String majorVersion, Response response)
			throws IOException
	{
		String errorMessage = "";

		Assert.assertEquals(response.getStatusCode(), getUnkownType().errorResponse, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		errorMessage = getUnkownType().errorMsg;
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", caseType);
		errorMessage = errorMessage.replaceFirst("\\{.*?\\}", majorVersion);

		Assert.assertEquals(jsonPath.getString("errorCode"), getUnkownType().errorCode, "incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertEquals(jsonPath.getList("contextAttributes").size(), 2, "incorrect number of context attributes");

		Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "type",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), caseType,
				"incorrect name of context attribute");

		Assert.assertEquals(jsonPath.getString("contextAttributes[1].name"), "majorVersion",
				"incorrect name of context attribute");
		Assert.assertEquals(jsonPath.getString("contextAttributes[1].value"), majorVersion,
				"incorrect name of context attribute");

		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(jsonPath.getString("stackTrace")
				.contains("com.tibco.bpm.cdm.api.exception.ReferenceException: Unknown type: " + caseType
						+ " (major version " + majorVersion + ")"),
				true, "incorrect stacktrace");
	}

	public void assertErrorMethodNotAllowed(Response response) throws IOException
	{

		Assert.assertEquals(response.getStatusCode(), getApiForbidden().errorResponse, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		Assert.assertEquals(jsonPath.getString("errorCode"), getApiForbidden().errorCode, "incorrect error code");
		Assert.assertEquals(jsonPath.getString("errorMsg"), getApiForbidden().errorMsg, "incorrect error message");
		Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
		Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
		Assert.assertEquals(
				jsonPath.getString("stackTrace").contains(
						"com.tibco.bpm.cdm.api.exception.NotAuthorisedException: You are not not authorized to call this API"),
				true, "incorrect stacktrace");
	}

	public String readValue(String propertyName) throws IOException
	{
		initiateCasedata();
		String casedata = propCasedata.getProperty(propertyName);
		return casedata;
	}

}