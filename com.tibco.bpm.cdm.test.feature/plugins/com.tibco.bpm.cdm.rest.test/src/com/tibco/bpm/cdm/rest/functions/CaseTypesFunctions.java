package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class CaseTypesFunctions extends Utils
{

	private static final String	filterIsCase					= "isCase";

	private static final String	filterNamespace					= "namespace";

	private static final String	filterApplicationId				= "applicationId";

	private static final String	filterApplicationMajorVersion	= "applicationMajorVersion";

	public Response getTypes(Map filters, String select, String skip, String top)
			throws IOException, InterruptedException
	{
		return getTypes(filters, select, skip, top, "tibco-admin");
	}

	public Response getTypes(Map filters, String select, String skip, String top, String username)
			throws IOException, InterruptedException
	{

		System.out.println("*** Performing Request URL for GET Types ****");

		Response response;
		String filter = "";

		Map<String, String> params = new HashMap<String, String>(5);

		if (filters.isEmpty() || filters.size() == 0)
		{
			// don't put filter
		}
		else
		{
			if (filters.containsKey(filterIsCase) && !(filters.get(filterIsCase).equals("")))
			{
				filter = filter + filterIsCase + " eq " + filters.get(filterIsCase);
			}

			if (filters.containsKey(filterApplicationId) && !(filters.get(filterApplicationId).equals("")))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterApplicationId + " eq '" + filters.get(filterApplicationId) + "'";
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterApplicationId + " eq '" + filters.get(filterApplicationId) + "'";
				}
			}

			if (filters.containsKey(filterApplicationMajorVersion)
					&& !(filters.get(filterApplicationMajorVersion).equals("")))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterApplicationMajorVersion + " eq "
							+ filters.get(filterApplicationMajorVersion);
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterApplicationMajorVersion + " eq "
							+ filters.get(filterApplicationMajorVersion);
				}
			}

			if (filters.containsKey(filterNamespace) && !(filters.get(filterNamespace).equals("")))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterNamespace + " eq '" + filters.get(filterNamespace) + "'";
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterNamespace + " eq '" + filters.get(filterNamespace) + "'";
				}
			}
		}

		if (filter != "" && !(filter.equals("")))
		{
			params.put("$filter", filter);
		}

		if (select != "" && !(select.equals("")))
		{
			params.put("$select", select);
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
						.get(Utils.URL_CM_TYPES);

		return response;
	}
}