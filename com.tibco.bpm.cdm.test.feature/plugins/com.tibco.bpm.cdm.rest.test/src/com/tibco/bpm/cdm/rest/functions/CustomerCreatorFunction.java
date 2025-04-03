package com.tibco.bpm.cdm.rest.functions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CustomerCreatorFunction extends Utils
{
	private CasesFunctions	caseFunctions	= new CasesFunctions();

	private Response		response		= null;

	private JsonPath		jsonPath		= null;

	public List<String> createCustomerSpecificStateOffsetDefaultCases(Integer numberOfCustomers, String state)
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			DataModelSerializationException, DeploymentException, InternalException, CasedataException,
			ReferenceException, ArgumentException, InterruptedException
	{

		List<String> cases = new ArrayList<>();
		List<String> refs = new ArrayList<>();
		cases.clear();
		refs.clear();

		if (state != "" && !(state.equals("")))
		{
			for (int customerIterator = 0; customerIterator < numberOfCustomers; customerIterator++)
			{

				String pre_casedata = "{\"professionalDetails\":[{}],\"personalDetails\":{\"homeAddress\":{}}, \"state\":\""
						+ state + "\"}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		else if (state.length() == 0 || state.equals(""))

		{
			for (int customerIterator = 0; customerIterator < numberOfCustomers; customerIterator++)
			{

				String pre_casedata = "{\"professionalDetails\":[{}],\"personalDetails\":{\"homeAddress\":{}}}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		response = caseFunctions.createCases("com.example.bankdatamodel.Customer", 1, cases);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(response.asString()); // useful for debugging
		System.out.println(jsonPath.getString("")); // useful for debugging
		for (

				int caseIterator = 0; caseIterator < numberOfCustomers; caseIterator++)
		{
			refs.add(jsonPath.getString("[" + caseIterator + "]"));
		}
		return refs;
	}

	//TODO insterted by nashtapu [Jul 19, 2019 3:32:02 PM]
	//replace by a util method
	public List<String> customerChangState(List<String> caseRefs, String stateFrom, String stateTo) throws IOException,
			URISyntaxException, RuntimeApplicationException, PersistenceException, DataModelSerializationException,
			DeploymentException, InternalException, ArgumentException, InterruptedException
	{

		String casedata = "";
		List<String> returnCaseRef = new ArrayList<String>();
		returnCaseRef.clear();

		response = caseFunctions.getSingleCase(caseRefs.get(0), "casedata");
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		casedata = jsonPath.getString("casedata");

		casedata = casedata.replace(String.valueOf(stateFrom), String.valueOf(stateTo));

		//update
		response = caseFunctions.updateSingleCase(caseRefs.get(0), casedata);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		//clear the list and add new updated case reference
		//caseRefs.clear();
		//caseRefs.add(jsonPath.getString("[" + 0 + "]"));

		returnCaseRef.add(jsonPath.getString("[" + 0 + "]"));

		return returnCaseRef;
	}

	public List<String> createNewCaseTypeCases(Integer numberOfCases, String state) throws IOException,
			URISyntaxException, RuntimeApplicationException, PersistenceException, DataModelSerializationException,
			DeploymentException, InternalException, ArgumentException, InterruptedException
	{

		List<String> cases = new ArrayList<>();
		List<String> refs = new ArrayList<>();
		cases.clear();
		refs.clear();

		if (state != "" && !(state.equals("")) && !(state.equalsIgnoreCase("RANDOM")))
		{
			for (int caseIterator = 0; caseIterator < numberOfCases; caseIterator++)
			{

				String pre_casedata = "{\"\"state\":\"" + state + "\"}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		else if (state.length() == 0 || state.equals(""))

		{
			for (int caseIterator = 0; caseIterator < numberOfCases; caseIterator++)
			{

				String pre_casedata = "{}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		else if (state.equalsIgnoreCase("RANDOM"))
		{
			for (int caseIterator = 0; caseIterator < numberOfCases; caseIterator++)
			{
				String[] stateString = {"deployed", "undeployed", "undeploying"};
				String pre_casedata = "{\"state\":\"" + stateString[(caseIterator % 3)] + "\"}";

				String casedata = Utils.sanitize(pre_casedata);
				cases.add(casedata);
			}
		}

		response = caseFunctions.createCases("com.example.bankdatamodel.NewCaseType", 1, cases);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
		jsonPath = response.jsonPath();
		System.out.println(response.asString()); // useful for debugging
		System.out.println(jsonPath.getString("")); // useful for debugging
		for (

				int caseIterator = 0; caseIterator < numberOfCases; caseIterator++)
		{
			refs.add(jsonPath.getString("[" + caseIterator + "]"));
		}
		return refs;
	}

}
