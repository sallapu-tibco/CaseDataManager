package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;

public class CaseLinksFunction extends Utils
{

	private static final String filterIsName = "name";

	public Response createLinks(String caseReference, LinksPostRequestBody body)
			throws IOException, InterruptedException
	{
		return createLinks(caseReference, body, "tibco-admin");
	}

	public Response createLinks(String caseReference, LinksPostRequestBody body, String username)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for CREATE Links ****");

		Response response;

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body)
						.pathParam("caseReference", caseReference).post(Utils.URL_CM_LINKS);

		return response;
	}

	public Response getLinks(Map<String, String> filters, Map<String, String> dqlQuery, String caseReference,
			String skip, String top) throws IOException, InterruptedException
	{
		return getLinks(filters, dqlQuery, caseReference, skip, top, "tibco-admin");
	}

	public Response getLinks(Map<String, String> filters, Map<String, String> dqlQuery, String caseReference,
			String skip, String top, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for GET Links ****");

		Response response;
		String filter = "";
		String dql = "";

		Map<String, String> params = new HashMap<String, String>(6);

		if (filters == null || filters.isEmpty() || filters.size() == 0)
		{
			// don't put filter
		}
		else
		{
			if (filters.containsKey(filterIsName) && !(filters.get(filterIsName).equals("")))
			{
				filter = filter + filterIsName + " eq '" + filters.get(filterIsName) + "'";
			}
		}

		if (dqlQuery == null || dqlQuery.isEmpty() || dqlQuery.size() == 0)
		{
			// don't put dql
		}
		else
		{
			for (String attributeName : dqlQuery.keySet())
			{
				String attributeValue = dqlQuery.get(attributeName);
				if (!(dql.equals("")))
				{
					dql = dql + " and ";
				}
				if (attributeName.contains("duplicate-"))
				{
					dql = dql + attributeName.substring(10) + " = " + attributeValue;
				}
				else
				{
					dql = dql + attributeName + " = " + attributeValue;
				}
			}
		}

		if (filter != "" && !(filter.equals("")))
		{
			params.put("$filter", filter);
		}

		if (dql != "" && !(dql.equals("")))
		{
			params.put("$dql", dql);
		}

		if (skip != "" && !(skip.equals("")))

		{
			params.put("$skip", skip);
		}

		if (top != "" && !(top.equals("")))
		{
			params.put("$top", top);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.pathParam("caseReference", caseReference).get(Utils.URL_CM_LINKS);

		return response;
	}

	public Response deleteLinks(String caseReference, String name, String targetRefFiltertype,
			List<String> targetCaseReference) throws IOException, InterruptedException
	{
		return deleteLinks(caseReference, name, targetRefFiltertype, targetCaseReference, "tibco-admin");
	}

	public Response deleteLinks(String caseReference, String name, String targetRefFiltertype,
			List<String> targetCaseReference, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for GET Links ****");

		Response response;

		Map<String, String> params = new HashMap<String, String>(6);

		if (name != "" && !(name.equals(""))
				&& (targetCaseReference.size() == 0 || targetRefFiltertype.equalsIgnoreCase("NONE")))
		{
			params.put("$filter", "name eq '" + name + "'");
		}

		else if (name != "" && !(name.equals(""))
				&& (targetCaseReference.size() != 0 && targetRefFiltertype.equalsIgnoreCase("EQ")))
		{
			params.put("$filter",
					"name eq '" + name + "' and targetCaseReference eq '" + targetCaseReference.get(0) + "'");
		}

		else if (name != "" && !(name.equals(""))
				&& (targetCaseReference.size() != 0 && targetRefFiltertype.equalsIgnoreCase("IN")))
		{
			String targetCaseReferenceFilter = "(";
			for (int targetRefIterator = 0; targetRefIterator < targetCaseReference.size(); targetRefIterator++)
			{
				if (targetRefIterator == (targetCaseReference.size() - 1))
				{
					targetCaseReferenceFilter = targetCaseReferenceFilter + "'"
							+ targetCaseReference.get(targetRefIterator) + "'";
				}
				else
				{
					targetCaseReferenceFilter = targetCaseReferenceFilter + "'"
							+ targetCaseReference.get(targetRefIterator) + "',";
				}
			}
			targetCaseReferenceFilter = targetCaseReferenceFilter + ")";
			params.put("$filter", "name eq '" + name + "' and targetCaseReference in" + targetCaseReferenceFilter);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.pathParam("caseReference", caseReference).delete(Utils.URL_CM_LINKS);

		return response;
	}

}