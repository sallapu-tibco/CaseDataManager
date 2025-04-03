package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBodyItem;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CrossReferenceProjectsTest extends Utils
{

	private CaseTypesFunctions		caseTypes			= new CaseTypesFunctions();

	private GetPropertiesFunction	properties			= new GetPropertiesFunction();

	private CasesFunctions			cases				= new CasesFunctions();

	int								validStatusCode		= 200;

	int								invalidStatusCode	= 400;

	int								notFoundStatusCode	= 404;

	private Response				response			= null;

	private List<String>			caseReferences		= new ArrayList<>();

	//for case reference In filter skip and top are not allowed
	private String					skip				= "0";

	private String					top					= "10";

	private void createCases(String caseType, String casedataProperty) throws IOException, URISyntaxException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> casedata = new ArrayList<>();
		casedata.add(properties.readValue(casedataProperty));
		try
		{
			if (caseType.equals("com.crossproj.projx.modelx1.CaseX1"))
			{
				//since the major version for this case type is different
				response = cases.createCases(caseType, 441300, casedata);
			}
			else
			{
				response = cases.createCases(caseType, 1, casedata);
			}
			System.out.println(response.asString()); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseReferences.add(jsonPath.getString("[0]"));
		}
		catch (Exception e)
		{
			System.out.println("exception in creating account cases");
			System.out.println(e.getMessage());
			Assert.fail("cases cannot be created");
		}
	}

	//update multiple cases
	private Response updateMultipleCases() throws IOException, InterruptedException
	{
		//the cases of Y1 case to be updated
		CasesPutRequestBody body = new CasesPutRequestBody();

		CasesPutRequestBodyItem item1 = new CasesPutRequestBodyItem();
		//1st case created in v1
		item1.setCasedata(properties.readValue("CaseY1V2Update1V2"));
		item1.setCaseReference(caseReferences.get(3));
		body.add(item1);

		CasesPutRequestBodyItem item2 = new CasesPutRequestBodyItem();
		//2nd case created in v2
		item2.setCasedata(properties.readValue("CaseY1V2Update2V2"));
		item2.setCaseReference(caseReferences.get(5));
		body.add(item2);

		try
		{
			response = cases.arrayUpdateCases(body);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 2; caseIterator++)
			{
				caseReferences.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while updating cases");
			System.out.println(e.getMessage());
		}
		return response;
	}

	private void assertGet(int size, int position, String casedata, String caseSummary, String caseReference,
			Response response) throws JsonParseException, JsonMappingException, IOException
	{
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), size);
		//assert casedata
		Assert.assertTrue(
				assertUnsortedData(jsonPath.getString("[" + position + "].casedata"), properties.readValue(casedata)),
				"casedata does not match");
		//assert metadata
		Assert.assertNotNull(jsonPath.getString("[" + position + "].metadata"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[" + position + "].metadata.creationTimestamp"),
				"metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[" + position + "].metadata.modifiedBy"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[" + position + "].metadata.modificationTimestamp"),
				"metadata should be tagged");
		//assert case reference
		Assert.assertEquals(jsonPath.getString("[" + position + "].caseReference"), caseReference,
				"caseRef should be tagged");
		//assert summary
		Assert.assertTrue(
				assertUnsortedData(jsonPath.getString("[" + position + "].summary"), properties.readValue(caseSummary)),
				"summary does not match");
	}

	private void assertRead(String casedata, String caseSummary, String caseReference, Response response)
			throws JsonParseException, JsonMappingException, IOException
	{
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		//assert casedata
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), properties.readValue(casedata)),
				"casedata does not match");
		//assert metadata
		Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"), "metadata should be tagged");
		//assert case reference
		Assert.assertEquals(jsonPath.getString("caseReference"), caseReference, "caseRef should be tagged");
		//assert summary
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), properties.readValue(caseSummary)),
				"summary does not match");
	}

	//Test to performs create, read, update and upgrade for applications having cross reference boms
	@Test(description = "Test to performs create, read, update and upgrade for applications having cross reference boms")
	public void testCRUAndUpgradeForCrossReferenceProjects()
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			InternalException, InterruptedException, DeploymentException
	{
		BigInteger deployIdA = BigInteger.valueOf(1);
		BigInteger deployIdB = BigInteger.valueOf(2);
		BigInteger deployIdC = BigInteger.valueOf(3);
		BigInteger deployIdD = BigInteger.valueOf(4);
		BigInteger deployIdX = BigInteger.valueOf(5);
		BigInteger deployIdYV1 = BigInteger.valueOf(6);
		BigInteger deployIdYV2 = BigInteger.valueOf(7);

		caseReferences.clear();

		//deploy the other application 
		try
		{
			deployIdD = deployRASC("/apps/cross-project-test/ProjD/Exports/Deployment Artifacts/ProjD.rasc");
			deployIdC = deployRASC("/apps/cross-project-test/ProjC/Exports/Deployment Artifacts/ProjC.rasc");
			deployIdB = deployRASC("/apps/cross-project-test/ProjB/Exports/Deployment Artifacts/ProjB.rasc");
			deployIdA = deployRASC("/apps/cross-project-test/ProjA/Exports/Deployment Artifacts/ProjA.rasc");
			deployIdX = deploy("/apps/cross-project-test/ProjX");
			deployIdYV1 = deploy("/apps/cross-project-test/ProjYv1");
			System.out.println("deployments successful");

			//create cases of each case class - 
			//a1
			createCases("com.crossproj.proja.modela1.CaseA1", "CaseA1V1Read1V1");
			//b2
			createCases("com.crossproj.projb.modelb2.CaseB2", "CaseB2V1Read1V1");
			//x1
			createCases("com.crossproj.projx.modelx1.CaseX1", "CaseX1V1Read1V1");
			//y1
			createCases("com.crossproj.projy.modely1.CaseY1", "CaseY1V1Read1V1");
			//y11
			createCases("com.crossproj.projy.modely1.CaseY11", "CaseY11V1Read1V1");

			//search - searching x1 case - search with caseType, applicationMajorVersion and search string
			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseType", "com.crossproj.projx.modelx1.CaseX1");
			filterMap.put("applicationMajorVersion", "441300");
			String searchString = "Case X1 01";
			response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top, searchString, "");
			assertGet(1, 0, "CaseX1V1Read1V1", "CaseX1V1Summary1V1", caseReferences.get(2), response);
			filterMap.clear();

			//get cases - getting y1 case - get with caseType, applicationMajorVersion
			filterMap.put("caseType", "com.crossproj.projy.modely1.CaseY1");
			filterMap.put("applicationMajorVersion", "1");
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "");
			assertGet(1, 0, "CaseY1V1Read1V1", "CaseY1V1Summary1V1", caseReferences.get(3), response);
			filterMap.clear();

			//get cases filter in[] of 2 different types
			String caseRefIn = "('" + caseReferences.get(0) + "','" + caseReferences.get(1) + "','"
					+ caseReferences.get(2) + "','" + caseReferences.get(3) + "')";
			filterMap.put("caseReference", caseRefIn);
			response = cases.getCases("IN", filterMap, SELECT_CASES, "", "", "");
			assertGet(4, 0, "CaseA1V1Read1V1", "CaseA1V1Summary1V1", caseReferences.get(0), response);
			assertGet(4, 1, "CaseB2V1Read1V1", "CaseB2V1Summary1V1", caseReferences.get(1), response);
			assertGet(4, 2, "CaseX1V1Read1V1", "CaseX1V1Summary1V1", caseReferences.get(2), response);
			assertGet(4, 3, "CaseY1V1Read1V1", "CaseY1V1Summary1V1", caseReferences.get(3), response);
			filterMap.clear();

			//read single case
			response = cases.getSingleCase(caseReferences.get(2), SELECT_CASES);
			assertRead("CaseX1V1Read1V1", "CaseX1V1Summary1V1", caseReferences.get(2), response);

			//findBy
			Map<String, String> filterDql = new HashMap<String, String>();
			filterDql.put("cid", "'Case X1 01'");
			filterDql.put("state", "'CASECREATED - X1'");
			filterMap.put("caseType", "com.crossproj.projx.modelx1.CaseX1");
			filterMap.put("applicationMajorVersion", "441300");
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertGet(1, 0, "CaseX1V1Read1V1", "CaseX1V1Summary1V1", caseReferences.get(2), response);
			filterDql.clear();
			filterMap.clear();

			//upgrade
			deployIdYV2 = deploy("/apps/cross-project-test/ProjYv2");
			System.out.println("upgrade successful");

			//create cases for upgraded classes
			//y1
			createCases("com.crossproj.projy.modely1.CaseY1", "CaseY1V2Read2V1");
			//y22
			createCases("com.crossproj.projy.modely1.CaseY22", "CaseY22V2Read1V1");

			//get cases - getting y1 latest case - get with caseType, applicationMajorVersion, cid, modificationTimestamp, isInTerminalState
			filterMap.put("caseType", "com.crossproj.projy.modely1.CaseY1");
			filterMap.put("applicationMajorVersion", "1");
			filterMap.put("cid", "Case Y1 V2 02");
			filterMap.put("modificationTimestamp", cases.DEFAULT_MODIFICATION_TIME);
			filterMap.put("isInTerminalState", "FALSE");
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "");
			assertGet(1, 0, "CaseY1V2Read2V1", "CaseY1V2Summary2V1", caseReferences.get(5), response);
			filterMap.clear();

			//get cases - getting all y1 cases - get with caseType, applicationMajorVersion, isInTerminalState
			filterMap.put("caseType", "com.crossproj.projy.modely1.CaseY1");
			filterMap.put("applicationMajorVersion", "1");
			filterMap.put("isInTerminalState", "FALSE");
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "");
			assertGet(2, 0, "CaseY1V2Read2V1", "CaseY1V2Summary2V1", caseReferences.get(5), response);
			assertGet(2, 1, "CaseY1V1Read1V1", "CaseY1V1Summary1V1", caseReferences.get(3), response);
			filterMap.clear();

			//update single case for x1
			//removal of structured type - state change, removal of structured types one by one from the b1cross, additional of array elements of strctured type in d2cross, removal of c1cross
			response = cases.updateSingleCase(caseReferences.get(2), properties.readValue("CaseX1V2Update1V2"));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseReferences.add(jsonPath.getString("[" + 0 + "]"));

			//update multiple cases for y1
			//addition of the arrays of structured type and addition of date
			//total removal of x2 cross, change of date, value change in a structured type of a2cross
			response = updateMultipleCases();
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//read single case
			response = cases.getSingleCase(caseReferences.get(7), SELECT_CASES);
			assertRead("CaseX1V2Read1V2", "CaseX1V2Summary1V2", caseReferences.get(7), response);

			//read single case
			response = cases.getSingleCase(caseReferences.get(8), SELECT_CASES);
			assertRead("CaseY1V2Update1V2", "CaseY1V2Summary1V2", caseReferences.get(8), response);

			//read single case
			response = cases.getSingleCase(caseReferences.get(9), SELECT_CASES);
			assertRead("CaseY1V2Update2V2", "CaseY1V2Summary2V2", caseReferences.get(9), response);

			//update single case for y1 (after upgrade)
			//total removal all structured types and removal of date as well
			response = cases.updateSingleCase(caseReferences.get(9), properties.readValue("CaseY1V2Update2V3"));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseReferences.add(jsonPath.getString("[" + 0 + "]"));

			//read single case
			response = cases.getSingleCase(caseReferences.get(10), SELECT_CASES);
			assertRead("CaseY1V2Update2V3", "CaseY1V2Summary2V3", caseReferences.get(10), response);

			//update single case for y1 (after upgrade)
			//addition of partial structures, addition of date and update to terminal state added in v2
			response = cases.updateSingleCase(caseReferences.get(10), properties.readValue("CaseY1V2Update2V4"));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseReferences.add(jsonPath.getString("[" + 0 + "]"));

			//read single case
			response = cases.getSingleCase(caseReferences.get(11), SELECT_CASES);
			assertRead("CaseY1V2Read2V4", "CaseY1V2Summary2V4", caseReferences.get(11), response);
		}
		finally
		{
			forceUndeploy(deployIdYV2);
			forceUndeploy(deployIdYV1);
			forceUndeploy(deployIdX);
			forceUndeploy(deployIdA);
			forceUndeploy(deployIdB);
			forceUndeploy(deployIdC);
			forceUndeploy(deployIdD);
			System.out.println("undeployments successful");
		}
	}

}
