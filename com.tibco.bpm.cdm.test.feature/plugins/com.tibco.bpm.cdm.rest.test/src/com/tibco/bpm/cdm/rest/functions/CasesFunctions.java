package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRefRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBodyItem;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CasesFunctions extends Utils
{
	private static final Integer	DEFAULT_APPLICATION_MAJOR_VERSION	= 1;

	public final String				DEFAULT_MODIFICATION_TIME			= "2099-12-12T00:00:59.000Z";

	Response						response							= null;

	private static final String		filterCaseType						= "caseType";

	private static final String		filterApplicationMajorVersion		= "applicationMajorVersion";

	private static final String		filterCid							= "cid";

	private static final String		filterCaseState						= "caseState";

	private static final String		filterIsInTerminalState				= "isInTerminalState";

	private static final String		filterModificationTimestamp			= "modificationTimestamp";

	private static final String		filterCaseReferencesIn				= "caseReference";

	private static final String		filterIsReferencedByProcess			= "isReferencedByProcess";

	public Response createCases(String caseTypes, Integer filterApplicationMajorVersion, List<String> casedata)
			throws IOException, InterruptedException
	{
		return createCases(caseTypes, filterApplicationMajorVersion, casedata, "tibco-admin");
	}

	public Response createCases(String caseTypes, Integer filterApplicationMajorVersion, List<String> casedata,
			String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Create Case ****");

		CasesPostRequestBody payload = new CasesPostRequestBody();
		payload.setApplicationMajorVersion(filterApplicationMajorVersion);
		payload.setCaseType(caseTypes);
		payload.setCasedata(casedata);

		response = given().auth().preemptive()
				.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(Utils.URL_CASES);

		return response;
	}

	public Response createCases(String caseTypes, List<String> casedata) throws IOException, InterruptedException
	{
		return createCases(caseTypes, casedata, "tibco-admin");
	}

	public Response createCases(String caseTypes, List<String> casedata, String username)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Create Case ****");

		CasesPostRequestBody payload = new CasesPostRequestBody();
		payload.setApplicationMajorVersion(DEFAULT_APPLICATION_MAJOR_VERSION);
		payload.setCaseType(caseTypes);
		payload.setCasedata(casedata);

		response = given().auth().preemptive()
				.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(payload).post(Utils.URL_CASES);

		return response;
	}

	public Response updateCases(String caseReference, String casedata) throws IOException, InterruptedException
	{
		return updateCases(caseReference, casedata, "tibco-admin");
	}

	public Response updateCases(String caseReference, String casedata, String username)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Update Case ****");

		CasesPutRequestBody body = new CasesPutRequestBody();
		CasesPutRequestBodyItem payload = new CasesPutRequestBodyItem();
		payload.setCasedata(casedata);
		payload.setCaseReference(caseReference);
		body.add(payload);

		response = given().auth().preemptive()
				.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body).put(Utils.URL_CASES);

		return response;
	}

	public Response updateSingleCase(String caseReference, String newCasedata) throws IOException, InterruptedException
	{
		return updateSingleCase(caseReference, newCasedata, "tibco-admin");
	}

	public Response updateSingleCase(String caseReference, String newCasedata, String username)
			throws IOException, InterruptedException
	{

		System.out.println("*** Performing Request URL to UPDATE Single Case ****");

		CasesPutRefRequestBody body = new CasesPutRefRequestBody();
		CasesPutRequestBodyItem payload = new CasesPutRequestBodyItem();
		payload.setCasedata(newCasedata);
		body.setCasedata(newCasedata);

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body)
						.pathParam("caseReference", caseReference).put(Utils.URL_SINGLE_CASES);

		return response;
	}

	public Response arrayUpdateCases(CasesPutRequestBody body) throws IOException, InterruptedException
	{
		return arrayUpdateCases(body, "tibco-admin");
	}

	public Response arrayUpdateCases(CasesPutRequestBody body, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Update Case ****");

		response = given().auth().preemptive()
				.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log().all()
				.contentType(ContentType.JSON).accept(ContentType.JSON).when().body(body).put(Utils.URL_CASES);

		return response;
	}

	public Response getCases(String filterType, Map<String, String> filters, String select, String skip, String top,
			String count) throws IOException, InterruptedException
	{
		return getCases(filterType, filters, select, skip, top, count, "tibco-admin");
	}

	public Response getCases(String filterType, Map<String, String> filters, String select, String skip, String top,
			String count, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for GET Cases ****");

		String filter = "";

		Map<String, String> params = new HashMap<String, String>(5);

		if (filterType.equalsIgnoreCase("IN"))
		{
			if (filters.containsKey(filterCaseReferencesIn) && !(filters.get(filterCaseReferencesIn).equals("")))
			{
				filter = filter + filterCaseReferencesIn + " in" + filters.get(filterCaseReferencesIn);
			}
		}

		else
		{
			if (filters.isEmpty() || filters.size() == 0)
			{
				// don't put filter
			}
			else
			{
				if (filters.containsKey(filterCaseType) && !(filters.get(filterCaseType).equals("")))
				{
					filter = filter + filterCaseType + " eq '" + filters.get(filterCaseType) + "'";
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

				if (filters.containsKey(filterCid) && !(filters.get(filterCid).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
				}

				if (filters.containsKey(filterCaseState) && !(filters.get(filterCaseState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
				}

				if (filters.containsKey(filterModificationTimestamp)
						&& !(filters.get(filterModificationTimestamp).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
				}

				if (filters.containsKey(filterIsInTerminalState) && !(filters.get(filterIsInTerminalState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterIsInTerminalState + " eq "
								+ filters.get(filterIsInTerminalState);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterIsInTerminalState + " eq " + filters.get(filterIsInTerminalState);
					}
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

		if (count != "" && !(count.equals("")))
		{
			params.put("$count", count);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.get(Utils.URL_CASES);

		return response;
	}

	public Response getSingleCase(String caseReference, String select) throws IOException, InterruptedException
	{
		return getSingleCase(caseReference, select, "tibco-admin");
	}

	public Response getSingleCase(String caseReference, String select, String username)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for GET Single Case ****");

		Map<String, String> params = new HashMap<String, String>(1);

		if (select != "" && !(select.equals("")))
		{
			params.put("$select", select);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.pathParam("caseReference", caseReference).get(Utils.URL_SINGLE_CASES);

		return response;
	}

	public Response searchCases(String filterType, Map<String, String> filters, String select, String skip, String top,
			String search, String count) throws IOException, InterruptedException
	{
		return searchCases(filterType, filters, select, skip, top, search, count, "tibco-admin");
	}

	public Response searchCases(String filterType, Map<String, String> filters, String select, String skip, String top,
			String search, String count, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for SEARCH Cases ****");

		String filter = "";

		Map<String, String> params = new HashMap<String, String>(5);

		if (filterType.equalsIgnoreCase("IN"))
		{
			if (filters.containsKey(filterCaseReferencesIn) && !(filters.get(filterCaseReferencesIn).equals("")))
			{
				filter = filter + filterCaseReferencesIn + " in" + filters.get(filterCaseReferencesIn);
			}
		}

		else
		{
			if (filters.isEmpty() || filters.size() == 0)
			{
				// don't put filter
			}
			else
			{
				if (filters.containsKey(filterCaseType) && !(filters.get(filterCaseType).equals("")))
				{
					filter = filter + filterCaseType + " eq '" + filters.get(filterCaseType) + "'";
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

				if (filters.containsKey(filterCid) && !(filters.get(filterCid).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
				}

				if (filters.containsKey(filterCaseState) && !(filters.get(filterCaseState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
				}

				if (filters.containsKey(filterModificationTimestamp)
						&& !(filters.get(filterModificationTimestamp).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
				}

				if (filters.containsKey(filterIsInTerminalState) && !(filters.get(filterIsInTerminalState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterIsInTerminalState + " eq "
								+ filters.get(filterIsInTerminalState);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterIsInTerminalState + " eq " + filters.get(filterIsInTerminalState);
					}
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
		if (count != "" && !(count.equals("")))
		{
			params.put("$count", count);
		}
		if (search != "" && !(search.equals("")))
		{
			params.put("$search", search);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.get(Utils.URL_CASES);

		return response;
	}

	public Response purgeCases(Map<String, String> filters) throws IOException, InterruptedException
	{
		return purgeCases(filters, "tibco-admin");
	}

	public Response purgeCases(Map<String, String> filters, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for DELETE Cases ****");

		String filter = "";

		Map<String, String> params = new HashMap<String, String>(5);

		if (filters.isEmpty() || filters.size() == 0)
		{
			// don't put filter
		}
		else
		{
			if (filters.containsKey(filterCaseType) && !(filters.get(filterCaseType).equals("")))
			{
				filter = filter + filterCaseType + " eq '" + filters.get(filterCaseType) + "'";
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

			if (filters.containsKey(filterIsReferencedByProcess)
					&& !(filters.get(filterIsReferencedByProcess).equals("")))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterIsReferencedByProcess + " eq "
							+ filters.get(filterIsReferencedByProcess);
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterIsReferencedByProcess + " eq " + filters.get(filterIsReferencedByProcess);
				}
			}

			if (filters.containsKey(filterCid) && !(filters.get(filterCid).equals("")))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterCid + " eq '" + filters.get(filterCid) + "'";
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterCid + " eq '" + filters.get(filterCid) + "'";
				}
			}

			if (filters.containsKey(filterCaseState) && !(filters.get(filterCaseState).equals("")))
			{
				//verify that case state is not empty test value
				if (!(filters.get(filterCaseState).equals(EMPTY_VALUE)))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
				}
			}

			if (filters.containsKey(filterModificationTimestamp)
					&& !(filters.get(filterModificationTimestamp).equals("")))
			{
				//verify that case state is not empty test value
				if (!(filters.get(filterModificationTimestamp).equals(EMPTY_VALUE)))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
				}
			}

			if (!(filters.containsKey(filterModificationTimestamp)))
			{
				if (filter != "" && !(filter.equalsIgnoreCase("")))
				{
					filter = filter + " and " + filterModificationTimestamp + " le " + DEFAULT_MODIFICATION_TIME;
				}
				else if (filter == "" && filter.equalsIgnoreCase(""))
				{
					filter = filter + filterModificationTimestamp + " le " + DEFAULT_MODIFICATION_TIME;
				}
			}
		}

		if (filter != "" && !(filter.equals("")))

		{
			params.put("$filter", filter);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.delete(Utils.URL_CASES);

		return response;
	}

	public Response purgeSingleCase(String caseReference) throws IOException, InterruptedException
	{
		return purgeSingleCase(caseReference, "tibco-admin");
	}

	public Response purgeSingleCase(String caseReference, String username) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for PURGE Single Case ****");

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when()
						.pathParam("caseReference", caseReference).delete(Utils.URL_SINGLE_CASES);

		return response;
	}

	public Response getCasesDQL(String filterType, Map<String, String> filters, Map<String, String> dqlQuery,
			String select, String skip, String top, String count) throws IOException, InterruptedException
	{
		return getCasesDQL(filterType, filters, dqlQuery, select, skip, top, count, "tibco-admin");
	}

	public Response getCasesDQL(String filterType, Map<String, String> filters, Map<String, String> dqlQuery,
			String select, String skip, String top, String count, String username)
			throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL for DQL Find By Cases ****");

		String filter = "";
		String dql = "";

		Map<String, String> params = new HashMap<String, String>(5);

		if (filterType.equalsIgnoreCase("IN"))
		{
			if (filters.containsKey(filterCaseReferencesIn) && !(filters.get(filterCaseReferencesIn).equals("")))
			{
				filter = filter + filterCaseReferencesIn + " in" + filters.get(filterCaseReferencesIn);
			}
		}

		else
		{
			if (filters.isEmpty() || filters.size() == 0)
			{
				// don't put filter
			}
			else
			{
				if (filters.containsKey(filterCaseType) && !(filters.get(filterCaseType).equals("")))
				{
					filter = filter + filterCaseType + " eq '" + filters.get(filterCaseType) + "'";
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

				if (filters.containsKey(filterCid) && !(filters.get(filterCid).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCid + " eq '" + filters.get(filterCid) + "'";
					}
				}

				if (filters.containsKey(filterCaseState) && !(filters.get(filterCaseState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterCaseState + " eq '" + filters.get(filterCaseState) + "'";
					}
				}

				if (filters.containsKey(filterModificationTimestamp)
						&& !(filters.get(filterModificationTimestamp).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterModificationTimestamp + " le "
								+ filters.get(filterModificationTimestamp);
					}
				}

				if (filters.containsKey(filterIsInTerminalState) && !(filters.get(filterIsInTerminalState).equals("")))
				{
					if (filter != "" && !(filter.equalsIgnoreCase("")))
					{
						filter = filter + " and " + filterIsInTerminalState + " eq "
								+ filters.get(filterIsInTerminalState);
					}
					else if (filter == "" && filter.equalsIgnoreCase(""))
					{
						filter = filter + filterIsInTerminalState + " eq " + filters.get(filterIsInTerminalState);
					}
				}
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

		if (dql != "" && !(dql.equals("")))
		{
			params.put("$dql", dql);
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

		if (count != "" && !(count.equals("")))
		{
			params.put("$count", count);
		}

		response =

				given().auth().preemptive()
						.basic(username, username.equals("tibco-admin") ? REST_PASSWORD : REST_PASSWORD_USER).log()
						.all().contentType(ContentType.JSON).accept(ContentType.JSON).when().queryParameters(params)
						.get(Utils.URL_CASES);

		return response;
	}

	public List<String> updateChangePayload(String caseRef, String fromValue, String toValue) throws IOException,
			URISyntaxException, RuntimeApplicationException, PersistenceException, DataModelSerializationException,
			DeploymentException, InternalException, ArgumentException, InterruptedException
	{
		return updateChangePayload(caseRef, fromValue, toValue, "tibco-admin");
	}

	public List<String> updateChangePayload(String caseRef, String fromValue, String toValue, String username)
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			DataModelSerializationException, DeploymentException, InternalException, ArgumentException,
			InterruptedException
	{

		String casedata = "";
		List<String> returnCaseRef = new ArrayList<String>();
		returnCaseRef.clear();

		response = getSingleCase(caseRef, "casedata", username);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		casedata = jsonPath.getString("casedata");

		casedata = casedata.replace(fromValue, toValue);

		//		JSONObject jsonObject = (JSONObject) readJsonSimpleDemo(casedata);
		//		
		//		JsonNode readTree = om.readTree(casedata);
		//	
		//		
		//		for (String attributeName : payloadMap.keySet())
		//		{
		//			String attributeValue = payloadMap.get(attributeName);
		//			readTree.
		//		}
		//		
		//		

		//update
		response = updateSingleCase(caseRef, casedata, username);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		returnCaseRef.add(jsonPath.getString("[" + 0 + "]"));

		return returnCaseRef;
	}

}
