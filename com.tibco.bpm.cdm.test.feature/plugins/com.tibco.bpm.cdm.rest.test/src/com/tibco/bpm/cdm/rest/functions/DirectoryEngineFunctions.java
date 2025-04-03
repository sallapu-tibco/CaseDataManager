package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.de.rest.model.CandidateResourcesResponse;
import com.tibco.bpm.de.rest.model.LdapContainer;
import com.tibco.bpm.de.rest.model.PositionHolding;
import com.tibco.bpm.de.rest.model.Resource;

public class DirectoryEngineFunctions extends Utils
{

	Response response = null;

	public Response createEasyAsContainers(String containerName) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Create Container ****");

		String payload = "{\"name\": \"" + containerName + "\",\"description\": \"" + containerName
				+ "\",\"primarySource\": {\"alias\": \"easyAs\","
				+ " \"ldapQuery\": {\"query\": \"(ObjectClass=person)\",\"baseDn\": \"o=easyAsInsurance\",\"scope\": {\"type\": \"subtree\"}}}}";

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.TEXT).when().body(payload)
				.post(Utils.URL_DE_LDAP_CONTAINERS);

		System.out.println(response.asString());

		//Thread.sleep(2000);

		return response;
	}

	public Response getContainers() throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Create Container ****");

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().get(Utils.URL_DE_LDAP_CONTAINERS);

		return response;
	}

	public void deleteContainer(String containerName) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Delete Container ****");

		String containerId = getContainerId(containerName);

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().pathParam("containerId", containerId)
				.delete(Utils.URL_DE_LDAP_CONTAINER);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");
	}

	//create the resource
	public String createResource(String containerName, String candidateName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{

		String containerId = getContainerId(containerName);

		String resourceGuid = "No guid";

		String payload = "{\"containerId\": \"" + containerId + "\",\"include\": \"existing\",\"pageSize\": 10}";

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload)
				.post(Utils.URL_DE_LDAP_CONTAINER_RESOURCES);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		String responseJson = response.getBody().asString();

		System.out.println(responseJson);

		CandidateResourcesResponse body = (CandidateResourcesResponse) om.readValue(responseJson,
				CandidateResourcesResponse.class);

		//if the resource exists
		for (int i = 0; i < body.getCandidates().size(); i++)
		{
			if (body.getCandidates().get(i).getName().equals(candidateName))
			{
				resourceGuid = body.getCandidates().get(i).getGuid();
				return resourceGuid;
			}
		}

		//code reaching here implies the resource does not exist

		System.out.println("**** resource does not exist so the resource has to be created *****");

		containerId = getContainerId(containerName);

		payload = "";

		if (candidateName.equals(RESOURCE_CLINT) || candidateName.equals(RESOURCE_JOHN))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=Swindon, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		else if (candidateName.equals(RESOURCE_JON) || candidateName.equals(RESOURCE_LEON))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=Paris, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		else if (candidateName.equals(RESOURCE_LIAM) || candidateName.equals(RESOURCE_RICH)
				|| candidateName.equals(RESOURCE_STEVE) || candidateName.equals(RESOURCE_TONY))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=London, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		if (payload.equals(""))
		{
			Assert.fail("incorrect candidate name");
		}
		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(Utils.URL_RESOURCES);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		return "success";
	}

	//return container id
	private String getContainerId(String containerName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to List Containers ****");

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().get(Utils.URL_DE_LDAP_CONTAINERS);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		String responseJson = response.getBody().asString();

		LdapContainer[] body = (LdapContainer[]) om.readValue(responseJson, LdapContainer[].class);
		String containerId = "No Conatiner";

		//if the container exists
		for (int i = 0; i < body.length; i++)
		{
			if (body[i].getName().equalsIgnoreCase(containerName))
			{
				containerId = body[i].getId();
				return containerId;
			}
		}

		//code reaching here implies the container does not exist

		System.out.println("**** container does not exist so the resource has to be created *****");
		createEasyAsContainers(containerName);
		System.out.println("**** container should be created now *****");

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().get(Utils.URL_DE_LDAP_CONTAINERS);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		responseJson = response.getBody().asString();

		body = (LdapContainer[]) om.readValue(responseJson, LdapContainer[].class);

		//if the container exists
		for (int i = 0; i < body.length; i++)
		{
			if (body[i].getName().equalsIgnoreCase(containerName))
			{
				containerId = body[i].getId();
				return containerId;
			}
		}
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");
		responseJson = response.getBody().asString();
		body = (LdapContainer[]) om.readValue(responseJson, LdapContainer[].class);
		return containerId;
	}

	//return resource guid
	private String listCandidateResources(String containerName, String candidateName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to List Candidate resources ****");

		String containerId = getContainerId(containerName);

		String resourceGuid = "No guid";

		String payload = "{\"containerId\": \"" + containerId + "\",\"include\": \"existing\",\"pageSize\": 10}";

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload)
				.post(Utils.URL_DE_LDAP_CONTAINER_RESOURCES);

		System.out.println(response.asString());

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		String responseJson = response.getBody().asString();

		System.out.println(responseJson);

		CandidateResourcesResponse body = (CandidateResourcesResponse) om.readValue(responseJson,
				CandidateResourcesResponse.class);

		//if the resource exists
		for (int i = 0; i < body.getCandidates().size(); i++)
		{
			if (body.getCandidates().get(i).getName().equals(candidateName))
			{
				resourceGuid = body.getCandidates().get(i).getGuid();
				return resourceGuid;
			}
		}

		//code reaching here implies the resource does not exist

		System.out.println("**** resource does not exist so the resource has to be created *****");

		createResource(containerName, containerId, candidateName);

		System.out.println("**** resource should be created now *****");

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload)
				.post(Utils.URL_DE_LDAP_CONTAINER_RESOURCES);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");
		responseJson = response.getBody().asString();
		body = (CandidateResourcesResponse) om.readValue(responseJson, CandidateResourcesResponse.class);

		//the resource should exist now
		for (int i = 0; i < body.getCandidates().size(); i++)
		{
			if (body.getCandidates().get(i).getName().equals(candidateName))
			{
				resourceGuid = body.getCandidates().get(i).getGuid();
				return resourceGuid;
			}
		}

		return resourceGuid;
	}

	//create the resource
	private void createResource(String containerName, String containerId, String candidateName)
	{

		String payload = "";

		if (candidateName.equals(RESOURCE_CLINT) || candidateName.equals(RESOURCE_JOHN))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=Swindon, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		else if (candidateName.equals(RESOURCE_JON) || candidateName.equals(RESOURCE_LEON))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=Paris, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		else if (candidateName.equals(RESOURCE_LIAM) || candidateName.equals(RESOURCE_RICH)
				|| candidateName.equals(RESOURCE_STEVE) || candidateName.equals(RESOURCE_TONY))
		{
			payload = "[{\"name\": \"" + candidateName + "\"," + "\"label\": \"" + candidateName
					+ "\",\"containerId\": \"" + containerId + "\"," + "\"ldapOrigin\": " + "{" + "\"containerId\": \""
					+ containerId + "\"," + "\"primaryDn\": " + "{" + "\"alias\": \"easyAs\"," + "\"dn\": \"OU="
					+ candidateName + ", OU=London, OU=AllEmployees, O=easyAsInsurance\"," + "\"duplicates\":0" + "}}"
					+ "}]";
		}

		if (payload.equals(""))
		{
			Assert.fail("incorrect candidate name");
		}

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(Utils.URL_RESOURCES);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");
	}

	private Resource getResourceDetails(String resourceGuid)
			throws JsonParseException, JsonMappingException, IOException
	{

		System.out.println("*** Performing Request URL to Get Resource Details ****");

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().pathParam("guid", resourceGuid)
				.get(Utils.URL_RESOURCES_DETAILS);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		String responseJson = response.getBody().asString();
		Resource body = (Resource) om.readValue(responseJson, Resource.class);

		return body;
	}

	public void deleteResource(String containerName, String resourceName)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{

		System.out.println("*** Performing Request URL to Delete Resource Details ****");

		String resourceGuid = listCandidateResources(containerName, resourceName);

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().pathParam("guid", resourceGuid)
				.delete(Utils.URL_RESOURCES_DETAILS);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");

		String responseJson = response.getBody().asString();

		System.out.println(responseJson);
	}

	public void mapResources(String containerName, String resourceName, String entityName)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Map Resource ****");

		String resourceGuid = listCandidateResources(containerName, resourceName);

		Resource body = getResourceDetails(resourceGuid);

		if (!(body.getPositions().isEmpty()))
		{
			body.getPositions().clear();
		}

		PositionHolding position = new PositionHolding();
		position.setGuid(entityName);

		body.getPositions().add(position);

		response = given().auth().preemptive().basic(REST_USERNAME, REST_PASSWORD).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body)
				.pathParam("guid", resourceGuid).put(Utils.URL_RESOURCES_DETAILS);

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect status code");
	}
}
