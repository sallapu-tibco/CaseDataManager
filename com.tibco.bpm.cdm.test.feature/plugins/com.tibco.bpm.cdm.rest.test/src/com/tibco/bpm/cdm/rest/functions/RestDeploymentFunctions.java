package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.test.BaseTest;
import com.tibco.bpm.dem.model.DeploymentDetail;

public class RestDeploymentFunctions extends Utils
{
	Response	response	= null;

	JsonPath	jsonPath	= null;

	public Response deployApplication(String username, String applicationRasc) throws IOException, URISyntaxException
	{

		System.out.println("*** Performing Request URL to Deploy Application ****");

		if (username.equals("tibco-admin") || username.equals(""))
		{
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType("multipart/form-data").accept(ContentType.TEXT)
					.multiPart("appContents", new File(BaseTest.class.getResource(applicationRasc).toURI())).when()
					.post(Utils.URL_DEPLOYMENTS);
		}
		else
		{
			response = given().auth().preemptive().basic(username, REST_PASSWORD_USER).log().all()
					.contentType("multipart/form-data").accept(ContentType.TEXT)
					.multiPart("appContents", new File(BaseTest.class.getResource(applicationRasc).toURI())).when()
					.post(Utils.URL_DEPLOYMENTS);
		}

		System.out.println(response.asString()); // useful for debugging
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while deploying rasc");

		return response;
	}

	public Response getDeployments(String username, String applicationId) throws IOException, URISyntaxException
	{

		System.out.println("*** Performing Request URL to Deploy Application ****");

		Map<String, String> params = new HashMap<String, String>(5);

		if (applicationId != "" && !(applicationId.equals("")))
		{
			params.put("$filter", "application_id eq '" + applicationId + "'");
		}

		if (username.equals("tibco-admin") || username.equals(""))
		{
			response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
					.get(Utils.URL_DEPLOYMENTS);
		}
		else
		{
			response = given().auth().preemptive().basic(username, REST_PASSWORD_USER).log().all()
					.contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
					.get(Utils.URL_DEPLOYMENTS);
		}
		return response;
	}

	public Response undeployApplication(String username, String applicationId) throws IOException, URISyntaxException
	{

		getDeployments("", applicationId);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while getting deployed applications");
		System.out.println(response.asString()); // useful for debugging

		String repsonseJson = response.getBody().asString();
		jsonPath = response.jsonPath();

		DeploymentDetail[] body = (DeploymentDetail[]) om.readValue(repsonseJson, DeploymentDetail[].class);

		if (body.length == 0)
		{
			System.out.println("no deployments to undploy");
			return response;
		}
		else
		{
			Assert.assertEquals(body[0].getApplicationId(), "com.example.aceproject3organisational",
					"application id does not match");
			Assert.assertEquals(body[0].getApplicationName(), "AceProject3-Organisational",
					"application name does not match");
			Assert.assertEquals(body[0].getStatus().toString(), "Deployed", "status does not match");

			System.out.println("******* application information ********");
			System.out.println("application id : " + body[0].getApplicationId());
			System.out.println("application name : " + body[0].getApplicationName());
			System.out.println("application version : " + body[0].getApplicationVersion());
			System.out.println("id : " + body[0].getId());
			System.out.println("time created : " + body[0].getTimeCreated());
			System.out.println("time deployed : " + body[0].getTimeDeployed());
			System.out.println("application status : " + body[0].getStatus());
			System.out.println("application targets : " + body[0].getTargets());

			System.out.println("*** Performing Request URL to Undeploy Application ****");

			body[0].setStatus(DeploymentDetail.StatusEnum.Undeployed);

			if (username.equals("tibco-admin") || username.equals(""))
			{
				response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
						.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body[0])
						.put(Utils.URL_DEPLOYMENTS);
			}
			else
			{
				response = given().auth().preemptive().basic(username, REST_PASSWORD_USER).log().all()
						.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body[0])
						.put(Utils.URL_DEPLOYMENTS);
			}
			System.out.println(response.asString());
			return response;
		}
	}

}