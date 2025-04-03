package com.tibco.bpm.cdm.rest.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.TypeInfo;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class TypesTest extends Utils
{

	CaseTypesFunctions		caseTypes				= new CaseTypesFunctions();

	GetPropertiesFunction	properties				= new GetPropertiesFunction();

	private JsonPath		jsonPath				= null;

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger				deploymentIdApp1		= BigInteger.valueOf(3);

	BigInteger				deploymentIdSBOM		= BigInteger.valueOf(4);

	private Response		response				= null;

	private String[]		selectArrayAbbreviated	= {SELECT_CASE_TYPES, SELECT_CASE_TYPES_B, SELECT_CASE_TYPES_S,
			SELECT_CASE_TYPES_SA, SELECT_CASE_TYPES_A, SELECT_CASE_TYPES_D, SELECT_CASE_TYPES_L, SELECT_CASE_TYPES_B_S,
			SELECT_CASE_TYPES_B_SA, SELECT_CASE_TYPES_B_A, SELECT_CASE_TYPES_B_D, SELECT_CASE_TYPES_S_SA,
			SELECT_CASE_TYPES_S_A, SELECT_CASE_TYPES_S_D, SELECT_CASE_TYPES_SA_A, SELECT_CASE_TYPES_SA_D,
			SELECT_CASE_TYPES_A_D, SELECT_CASE_TYPES_B_S_SA, SELECT_CASE_TYPES_B_S_A, SELECT_CASE_TYPES_B_S_D,
			SELECT_CASE_TYPES_B_SA_A, SELECT_CASE_TYPES_B_SA_D, SELECT_CASE_TYPES_B_A_D, SELECT_CASE_TYPES_S_SA_A,
			SELECT_CASE_TYPES_S_SA_D, SELECT_CASE_TYPES_SA_A_D, SELECT_CASE_TYPES_S_A_D, SELECT_CASE_TYPES_B_S_SA_A,
			SELECT_CASE_TYPES_B_S_SA_D, SELECT_CASE_TYPES_S_SA_A_D, SELECT_CASE_TYPES_B_SA_A_D,
			SELECT_CASE_TYPES_B_S_A_D, SELECT_CASE_TYPES_B_S_SA_A_D, SELECT_CASE_TYPES_B_L, SELECT_CASE_TYPES_S_L,
			SELECT_CASE_TYPES_SA_L, SELECT_CASE_TYPES_A_L, SELECT_CASE_TYPES_D_L, SELECT_CASE_TYPES_B_S_L,
			SELECT_CASE_TYPES_B_SA_L, SELECT_CASE_TYPES_B_A_L, SELECT_CASE_TYPES_B_D_L, SELECT_CASE_TYPES_S_SA_L,
			SELECT_CASE_TYPES_S_A_L, SELECT_CASE_TYPES_S_D_L, SELECT_CASE_TYPES_SA_A_L, SELECT_CASE_TYPES_SA_D_L,
			SELECT_CASE_TYPES_A_D_L, SELECT_CASE_TYPES_B_S_SA_L, SELECT_CASE_TYPES_B_S_A_L, SELECT_CASE_TYPES_B_S_D_L,
			SELECT_CASE_TYPES_B_SA_A_L, SELECT_CASE_TYPES_B_SA_D_L, SELECT_CASE_TYPES_B_A_D_L,
			SELECT_CASE_TYPES_S_SA_A_L, SELECT_CASE_TYPES_S_SA_D_L, SELECT_CASE_TYPES_SA_A_D_L,
			SELECT_CASE_TYPES_S_A_D_L, SELECT_CASE_TYPES_B_S_SA_A_L, SELECT_CASE_TYPES_B_S_SA_D_L,
			SELECT_CASE_TYPES_S_SA_A_D_L, SELECT_CASE_TYPES_B_SA_A_D_L, SELECT_CASE_TYPES_B_S_A_D_L,
			SELECT_CASE_TYPES_B_S_SA_A_D_L};

	private String[]		selectArray				= {SELECT_CASE_TYPES, SELECT_CASE_TYPES_Basic,
			SELECT_CASE_TYPES_States, SELECT_CASE_TYPES_SummaryAttributes, SELECT_CASE_TYPES_Attributes,
			SELECT_CASE_TYPES_Dependencies, SELECT_CASE_TYPES_Links, SELECT_CASE_TYPES_Basic_States,
			SELECT_CASE_TYPES_Basic_SummaryAttributes, SELECT_CASE_TYPES_Basic_Attributes,
			SELECT_CASE_TYPES_Basic_Dependencies, SELECT_CASE_TYPES_States_SummaryAttributes,
			SELECT_CASE_TYPES_States_Attributes, SELECT_CASE_TYPES_States_Dependencies,
			SELECT_CASE_TYPES_SummaryAttributes_Attributes, SELECT_CASE_TYPES_SummaryAttributes_Dependencies,
			SELECT_CASE_TYPES_Attributes_Dependencies, SELECT_CASE_TYPES_Basic_States_SummaryAttributes,
			SELECT_CASE_TYPES_Basic_States_Attributes, SELECT_CASE_TYPES_Basic_States_Dependencies,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Dependencies, SELECT_CASE_TYPES_Basic_Attributes_Dependencies,
			SELECT_CASE_TYPES_States_SummaryAttributes_Attributes,
			SELECT_CASE_TYPES_States_SummaryAttributes_Dependencies,
			SELECT_CASE_TYPES_SummaryAttributes_Attributes_Dependencies,
			SELECT_CASE_TYPES_States_Attributes_Dependencies,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Dependencies,
			SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Dependencies,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Dependencies,
			SELECT_CASE_TYPES_Basic_States_Attributes_Dependencies,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Dependencies, SELECT_CASE_TYPES_Basic_Links,
			SELECT_CASE_TYPES_States_Links, SELECT_CASE_TYPES_SummaryAttributes_Links,
			SELECT_CASE_TYPES_Attributes_Links, SELECT_CASE_TYPES_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_States_Links, SELECT_CASE_TYPES_Basic_SummaryAttributes_Links,
			SELECT_CASE_TYPES_Basic_Attributes_Links, SELECT_CASE_TYPES_Basic_Dependencies_Links,
			SELECT_CASE_TYPES_States_SummaryAttributes_Links, SELECT_CASE_TYPES_States_Attributes_Links,
			SELECT_CASE_TYPES_States_Dependencies_Links, SELECT_CASE_TYPES_SummaryAttributes_Attributes_Links,
			SELECT_CASE_TYPES_SummaryAttributes_Dependencies_Links, SELECT_CASE_TYPES_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Links, SELECT_CASE_TYPES_Basic_States_Attributes_Links,
			SELECT_CASE_TYPES_Basic_States_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Links,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Links,
			SELECT_CASE_TYPES_States_SummaryAttributes_Dependencies_Links,
			SELECT_CASE_TYPES_SummaryAttributes_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_States_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Links,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Dependencies_Links,
			SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_States_Attributes_Dependencies_Links,
			SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Dependencies_Links};

	private void assertIsAddress(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "Address", "incorrect name");
			Assert.assertEquals(info.getLabel(), "Address", "incorrect label");
			Assert.assertEquals(info.getIsCase(), null, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "org.policycorporation", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.example.ProjectX", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getSummaryAttributes().isEmpty(), "summary attributes should be empty");
			Assert.assertEquals(info.getSummaryAttributes().size(), 0, "incorrect summary attributes");
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 2, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("firstLine")
					&& s.getLabel().equals("First Line") && s.getType().equals("Text")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("secondLine")
					&& s.getLabel().equals("Second Line") && s.getType().equals("Text")).count(), 1);
			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
						"structured type should not be present");
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getStates().isEmpty(), "states should be empty");
			Assert.assertEquals(info.getStates().size(), 0, "incorrect states");
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsClaim(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "Claim", "incorrect name");
			Assert.assertEquals(info.getLabel(), "Claim", "incorrect label");
			Assert.assertEquals(info.getIsCase(), null, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "org.policycorporation", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.example.ProjectX", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getSummaryAttributes().isEmpty(), "summary attributes should be empty");
			Assert.assertEquals(info.getSummaryAttributes().size(), 0, "incorrect summary attributes");
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 3, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(
					s -> s.getName().equals("date") && s.getLabel().equals("Date") && s.getType().equals("Date"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(
					s -> s.getName().equals("blame") && s.getLabel().equals("Blame") && s.getType().equals("Text"))
					.count(), 1);

			Assert.assertEquals(
					info.getAttributes().stream()
							.filter(s -> s.getName().equals("description") && s.getLabel().equals("Description")
									&& s.getType().equals("Text") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
						"structured type should not be present");
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getStates().isEmpty(), "states should be empty");
			Assert.assertEquals(info.getStates().size(), 0, "incorrect states");
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsPerson(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "Person", "incorrect name");
			Assert.assertEquals(info.getLabel(), "Person", "incorrect label");
			Assert.assertEquals(info.getIsCase(), Boolean.TRUE, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "org.policycorporation", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.example.ProjectX", "incorrect application id");
		}

		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 3, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("pcode")
					&& s.getLabel().equals("P Code") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsAutoIdentifier().toString().equals("true")
					&& s.getIsIdentifier().toString().equals("true") && s.getIsSearchable().toString().equals("true")
					&& s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("256")).count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("personState") && s.getLabel().equals("Person State")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

		}

		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 11, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("pcode")
					&& s.getLabel().equals("P Code") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsAutoIdentifier().toString().equals("true")
					&& s.getIsIdentifier().toString().equals("true") && s.getIsSearchable().toString().equals("true")
					&& s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("256")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(
					s -> s.getName().equals("name") && s.getLabel().equals("Name") && s.getType().equals("Text"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("lotteryNumbers") && s.getLabel().equals("Lottery Numbers")
							&& s.getType().equals("Number") && s.getIsArray().toString().equals("true")
							&& s.getConstraints().getMinValue().toString().equals("1")
							&& s.getConstraints().getMaxValue().toString().equals("49")
							&& s.getConstraints().getMinValueInclusive().toString().equals("true")
							&& s.getConstraints().getMaxValueInclusive().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(
					info.getAttributes().stream()
							.filter(s -> s.getName().equals("aliases") && s.getLabel().equals("Aliases")
									&& s.getType().equals("Text") && s.getIsArray().toString().equals("true"))
							.count(),
					1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("personState") && s.getLabel().equals("Person State")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("age") && s.getLabel().equals("Age") && s.getType().equals("Number")
							&& s.getConstraints().getMaxValue().toString().equals("120"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("dateOfBirth")
					&& s.getLabel().equals("Date of Birth") && s.getType().equals("Date")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("timeOfBirth")
					&& s.getLabel().equals("Time of Birth") && s.getType().equals("Time")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("homeAddress") && s.getLabel().equals("Home Address")
							&& s.getType().equals("Address") && s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("workAddress") && s.getLabel().equals("Work Address")
							&& s.getType().equals("Address") && s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("otherAddresses") && s.getLabel().equals("Other Addresses")
							&& s.getType().equals("Address") && s.getIsStructuredType().toString().equals("true")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!(info.getAttributes().get(i).getName().contains("Address")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}

		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 2, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Alive") && s.getValue().equals("ALIVE")).count(), 1);

			Assert.assertEquals(info.getStates().stream().filter(s -> s.getLabel().equals("Dead")
					&& s.getValue().equals("DEAD") && s.getIsTerminal().toString().equals("true")).count(), 1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 5, "incorrect number of links");
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("policies") && s.getLabel().equals("Policies")
									&& s.getType().equals("Policy") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("allowedToDrive") && s.getLabel().equals("Allowed To Drive")
									&& s.getType().equals("Policy") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("isParentOf") && s.getLabel().equals("Is Parent Of")
									&& s.getType().equals("Person") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("isChildOf") && s.getLabel().equals("Is Child Of")
									&& s.getType().equals("Person") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("favouritePolicy")
									&& s.getLabel().equals("Favourite Policy") && s.getType().equals("Policy"))
							.count(),
					1);
		}
	}

	private void assertIsPolicy(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{

			Assert.assertEquals(info.getName(), "Policy", "incorrect name");
			Assert.assertEquals(info.getLabel(), "Policy", "incorrect label");
			Assert.assertEquals(info.getIsCase(), Boolean.TRUE, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "org.policycorporation", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.example.ProjectX", "incorrect application id");
		}

		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{

			Assert.assertEquals(info.getSummaryAttributes().size(), 7, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("number") && s.getLabel().equals("Number")
							&& s.getType().equals("Number") && s.getIsMandatory().toString().equals("true")
							&& s.getIsIdentifier().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("policyStartDate") && s.getLabel().equals("Policy Start Date")
							&& s.getType().equals("Date") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("expiresAt") && s.getLabel().equals("Expires At")
							&& s.getType().equals("DateTimeTZ") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("premium") && s.getLabel().equals("Premium")
							&& s.getType().equals("FixedPointNumber") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getLength().toString().equals("14")
							&& s.getConstraints().getDecimalPlaces().toString().equals("2")
							&& s.getConstraints().getMaxValueInclusive().toString().equals("true")
							&& s.getConstraints().getMinValue().toString().equals("0")
							&& s.getConstraints().getMaxValue().toString().equals("2000"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("noClaimsYears") && s.getLabel().equals("No Claims Years")
							&& s.getType().equals("FixedPointNumber") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getDecimalPlaces().toString().equals("0")
							&& s.getConstraints().getMinValue().toString().equals("0")
							&& s.getConstraints().getMaxValue().toString().equals("99"))
					.count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("comments") && s.getLabel().equals("Comments")
							&& s.getType().equals("Text") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getLength().toString().equals("400"))
					.count(), 1);

		}

		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{

			Assert.assertEquals(info.getAttributes().size(), 11, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("number") && s.getLabel().equals("Number")
							&& s.getType().equals("Number") && s.getIsMandatory().toString().equals("true")
							&& s.getIsIdentifier().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("policyStartDate") && s.getLabel().equals("Policy Start Date")
							&& s.getType().equals("Date") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(
					info.getAttributes().stream()
							.filter(s -> s.getName().equals("policyStartTime")
									&& s.getLabel().equals("Policy Start Time") && s.getType().equals("Time"))
							.count(),
					1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("expiresAt") && s.getLabel().equals("Expires At")
							&& s.getType().equals("DateTimeTZ") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("premium") && s.getLabel().equals("Premium")
							&& s.getType().equals("FixedPointNumber") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getLength().toString().equals("14")
							&& s.getConstraints().getDecimalPlaces().toString().equals("2")
							&& s.getConstraints().getMaxValueInclusive().toString().equals("true")
							&& s.getConstraints().getMinValue().toString().equals("0")
							&& s.getConstraints().getMaxValue().toString().equals("2000"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("noClaimsYears") && s.getLabel().equals("No Claims Years")
							&& s.getType().equals("FixedPointNumber") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getDecimalPlaces().toString().equals("0")
							&& s.getConstraints().getMinValue().toString().equals("0")
							&& s.getConstraints().getMaxValue().toString().equals("99"))
					.count(), 1);

			Assert.assertEquals(
					info.getAttributes().stream()
							.filter(s -> s.getName().equals("termsAndConditions")
									&& s.getLabel().equals("Terms and Conditions") && s.getType().equals("URI"))
							.count(),
					1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("legalCover")
					&& s.getLabel().equals("Legal Cover") && s.getType().equals("Boolean")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("comments") && s.getLabel().equals("Comments")
							&& s.getType().equals("Text") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true")
							&& s.getConstraints().getLength().toString().equals("400"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("claims") && s.getLabel().equals("Claims")
							&& s.getType().equals("Claim") && s.getIsStructuredType().toString().equals("true")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!(info.getAttributes().get(i).getName().contains("claims")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}

		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 2, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Created") && s.getValue().equals("CREATED")).count(), 1);

			Assert.assertEquals(
					info.getStates().stream().filter(s -> s.getLabel().equals("Cancelled")
							&& s.getValue().equals("CANCELLED") && s.getIsTerminal().toString().equals("true")).count(),
					1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}

		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 3, "incorrect number of links");
			Assert.assertEquals(info.getLinks().stream().filter(
					s -> s.getName().equals("holder") && s.getLabel().equals("Holder") && s.getType().equals("Person"))
					.count(), 1);
			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("namedDrivers") && s.getLabel().equals("Named Drivers")
									&& s.getType().equals("Person") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(info.getLinks().stream()
					.filter(s -> s.getName().equals("isTheFavouriteOf") && s.getLabel().equals("Is The Favourite Of")
							&& s.getType().equals("Person") && s.getIsArray().toString().equals("true"))
					.count(), 1);
		}
	}

	private void assertIsA1(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseA1", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseA1", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.proja.modela1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.proja", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 4, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("a2") && s.getLabel().equals("a2")
							&& s.getType().equals("com.crossproj.proja.modela2.ClassA2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("d1") && s.getLabel().equals("d1")
							&& s.getType().equals("com.crossproj.projd.modeld1.ClassD1")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!((info.getAttributes().get(i).getName().contains("a2"))
						|| (info.getAttributes().get(i).getName().contains("d1"))))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 2, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case Created") && s.getValue().equals("CASECREATED")).count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case Completed") && s.getValue().equals("CASECOMPLETED")).count(),
					1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getDependencies().size(), 1, "incorrect number of dependecies");

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projd.modeld1")
							&& s.getApplicationId().equals("com.crossproj.projd")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsB2(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseB2", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseB2", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projb.modelb2", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projb", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 3, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsState().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true")
							&& s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("classb2") && s.getLabel().equals("b2")
							&& s.getType().equals("ClassB2") && s.getIsStructuredType().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!(info.getAttributes().get(i).getName().contains("classb2")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 2, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("EXCITED") && s.getValue().equals("EXCITED")).count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("BORED") && s.getValue().equals("BORED")).count(), 1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsB1(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "ClassB1", "incorrect name");
			Assert.assertEquals(info.getLabel(), "ClassB1", "incorrect label");
			Assert.assertEquals(info.getIsCase(), null, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projb.modelb1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projb", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getSummaryAttributes().isEmpty(), "summary attributes should be empty");
			Assert.assertEquals(info.getSummaryAttributes().size(), 0, "incorrect summary attributes");
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 3, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("attrB1") && s.getLabel().equals("attrB1")
							&& s.getType().equals("Text") && s.getConstraints().getLength().toString().equals("50"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("c1") && s.getLabel().equals("c1")
							&& s.getType().equals("com.crossproj.projc.modelc1.ClassC1")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("b2") && s.getLabel().equals("b2")
							&& s.getType().equals("com.crossproj.projb.modelb2.ClassB2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!((info.getAttributes().get(i).getName().contains("c1"))
						|| (info.getAttributes().get(i).getName().contains("b2"))))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getStates().isEmpty(), "states should be empty");
			Assert.assertEquals(info.getStates().size(), 0, "incorrect states");
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getDependencies().size(), 1, "incorrect number of dependecies");

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projc.modelc1")
							&& s.getApplicationId().equals("com.crossproj.projc")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsB2Class(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "ClassB2", "incorrect name");
			Assert.assertEquals(info.getLabel(), "ClassB2", "incorrect label");
			Assert.assertEquals(info.getIsCase(), null, "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projb.modelb2", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projb", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getSummaryAttributes().isEmpty(), "summary attributes should be empty");
			Assert.assertEquals(info.getSummaryAttributes().size(), 0, "incorrect summary attributes");
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 2, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(
					s -> s.getName().equals("attrB2") && s.getLabel().equals("attrB2") && s.getType().equals("URI"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("d2") && s.getLabel().equals("d2")
							&& s.getType().equals("com.crossproj.projd.modeld2.ClassD2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if (!(info.getAttributes().get(i).getName().contains("d2")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getStates().isEmpty(), "states should be empty");
			Assert.assertEquals(info.getStates().size(), 0, "incorrect states");
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getDependencies().size(), 1, "incorrect number of dependecies");

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projd.modeld2")
							&& s.getApplicationId().equals("com.crossproj.projd")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsX1(TypeInfo info, List<String> aspectNames)
	{
		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseX1", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseX1", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projx.modelx1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 441300,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projx", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("151")).count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 6, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("151")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("a2cross") && s.getLabel().equals("a2cross")
							&& s.getType().equals("com.crossproj.proja.modela2.ClassA2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("b1cross") && s.getLabel().equals("b1cross")
							&& s.getType().equals("com.crossproj.projb.modelb1.ClassB1")
							&& s.getIsStructuredType().toString().equals("true")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("c1cross") && s.getLabel().equals("c1cross")
							&& s.getType().equals("com.crossproj.projc.modelc1.ClassC1")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("d2cross") && s.getLabel().equals("d2cross")
							&& s.getType().equals("com.crossproj.projd.modeld2.ClassD2")
							&& s.getIsStructuredType().toString().equals("true")
							&& s.getIsArray().toString().equals("true") && s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if ((info.getAttributes().get(i).getName().contains("state"))
						|| (info.getAttributes().get(i).getName().contains("cid")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 3, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case Created") && s.getValue().equals("CASECREATED - X1"))
					.count(), 1);

			Assert.assertEquals(info.getStates()
					.stream().filter(s -> s.getLabel().equals("Case Completed")
							&& s.getValue().equals("CASECOMPLETED -X1") && s.getIsTerminal().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case &^%$ Halted") && s.getValue().equals("CASEHALTED-X1"))
					.count(), 1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getDependencies().size(), 4, "incorrect number of dependecies");

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projd.modeld2")
							&& s.getApplicationId().equals("com.crossproj.projd")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projc.modelc1")
							&& s.getApplicationId().equals("com.crossproj.projc")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projb.modelb1")
							&& s.getApplicationId().equals("com.crossproj.projb")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.proja.modela2")
							&& s.getApplicationId().equals("com.crossproj.proja")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getLinks().size(), 0, "incorrect number of links");
		}
	}

	private void assertIsY1(TypeInfo info, List<String> aspectNames, String version)
	{

		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseY1", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseY1", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projy.modely1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projy", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);
			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");
			}
			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getSummaryAttributes().size(), 3, "incorrect summary attributes");

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("datev2") && s.getLabel().equals("datev2")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);
			}
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cid")
					&& s.getLabel().equals("cid") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("50")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("a2cross") && s.getLabel().equals("a2cross")
							&& s.getType().equals("com.crossproj.proja.modela2.ClassA2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("b1cross") && s.getLabel().equals("b1cross")
							&& s.getType().equals("com.crossproj.projb.modelb1.ClassB1")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("c1cross") && s.getLabel().equals("c1cross")
							&& s.getType().equals("com.crossproj.projc.modelc1.ClassC1")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("d2cross") && s.getLabel().equals("d2cross")
							&& s.getType().equals("com.crossproj.projd.modeld2.ClassD2")
							&& s.getIsStructuredType().toString().equals("true"))
					.count(), 1);

			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getAttributes().size(), 6, "incorrect attributes size");
			}

			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getAttributes().size(), 8, "incorrect attributes size");

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("x2cross") && s.getLabel().equals("x2cross")
								&& s.getType().equals("com.crossproj.projx.modelx2.ClassX2")
								&& s.getIsStructuredType().toString().equals("true")
								&& s.getIsArray().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("datev2") && s.getLabel().equals("datev2")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);
			}
			for (int i = 0; i < info.getAttributes().size(); i++)
			{
				System.out.println("attribute under consideration : " + info.getAttributes().get(i).getName());
				if ((info.getAttributes().get(i).getName().contains("state"))
						|| (info.getAttributes().get(i).getName().contains("cid"))
						|| (info.getAttributes().get(i).getName().contains("datev2")))
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType(), null,
							"structured type should not be present");
				}
				else
				{
					assertEquals(info.getAttributes().get(i).getIsStructuredType().toString(), "true",
							"structured type should be present");
				}
			}
		}

		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case Created") && s.getValue().equals("CASECREATED - Y1"))
					.count(), 1);

			Assert.assertEquals(info.getStates()
					.stream().filter(s -> s.getLabel().equals("Case Completed")
							&& s.getValue().equals("CASECOMPLETED -Y1") && s.getIsTerminal().toString().equals("true"))
					.count(), 1);

			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getStates().size(), 2, "incorrect states");
			}

			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getStates().size(), 3, "incorrect states");
				Assert.assertEquals(info.getStates().stream()
						.filter(s -> s.getLabel().equals("Case Cancelled V2")
								&& s.getValue().equals("CASECANCELLED -Y1V2")
								&& s.getIsTerminal().toString().equals("true"))
						.count(), 1);
			}
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projd.modeld2")
							&& s.getApplicationId().equals("com.crossproj.projd")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projc.modelc1")
							&& s.getApplicationId().equals("com.crossproj.projc")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.projb.modelb1")
							&& s.getApplicationId().equals("com.crossproj.projb")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			Assert.assertEquals(info.getDependencies().stream()
					.filter(s -> s.getNamespace().equals("com.crossproj.proja.modela2")
							&& s.getApplicationId().equals("com.crossproj.proja")
							&& s.getApplicationMajorVersion().toString().equals("1"))
					.count(), 1);

			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getDependencies().size(), 4, "incorrect number of dependecies");
			}

			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getDependencies().size(), 5, "incorrect number of dependecies");

				Assert.assertEquals(
						info.getDependencies().stream()
								.filter(s -> s.getNamespace().equals("com.crossproj.projx.modelx2")
										&& s.getApplicationId().equals("com.crossproj.projx")
										&& s.getApplicationMajorVersion().toString().equals("441300"))
								.count(),
						1);
			}
		}

		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{

			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getLinks().size(), 1, "incorrect number of links");
				Assert.assertEquals(info.getLinks()
						.stream().filter(s -> s.getName().equals("refersthecase")
								&& s.getLabel().equals("Refers the cases") && s.getType().equals("CaseY11"))
						.count(), 1);
				Assert.assertNull(info.getLinks().get(0).getIsArray(), "is array should be null");
			}

			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getLinks().size(), 4, "incorrect number of links");

				Assert.assertEquals(info.getLinks().stream()
						.filter(s -> s.getName().equals("refersthecase") && s.getLabel().equals("Refers the cases")
								&& s.getType().equals("CaseY11") && s.getIsArray().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getLinks().stream()
						.filter(s -> s.getName().equals("lovesupgradedcases")
								&& s.getLabel().equals("Loves Upgraded Cases") && s.getType().equals("CaseY22")
								&& s.getIsArray().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getLinks()
						.stream().filter(s -> s.getName().equals("selfLink1")
								&& s.getLabel().equals("Self Link1 one to one") && s.getType().equals("CaseY1"))
						.count(), 1);
				Assert.assertNull(info.getLinks().get(2).getIsArray(), "is array should be null");

				Assert.assertEquals(info.getLinks()
						.stream().filter(s -> s.getName().equals("selfLink2")
								&& s.getLabel().equals("Self Link2 one to one") && s.getType().equals("CaseY1"))
						.count(), 1);
				Assert.assertNull(info.getLinks().get(3).getIsArray(), "is array should be null");
			}
		}
	}

	private void assertIsY1_1(TypeInfo info, List<String> aspectNames, String version)
	{

		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseY11", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseY11", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projy.modely1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projy", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cidy1")
					&& s.getLabel().equals("cidy1") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("51")).count(), 1);
			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 2, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cidy1")
					&& s.getLabel().equals("cidy1") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("51")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}

		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Case Created") && s.getValue().equals("CASECREATED - Y11"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream().filter(s -> s.getLabel().equals("Case Completed")
					&& s.getValue().equals("CASECOMPLETED - Y11") && s.getIsTerminal().toString().equals("true"))
					.count(), 1);

			if (version.equalsIgnoreCase("v1"))
			{
				Assert.assertEquals(info.getStates().size(), 2, "incorrect states");
			}

			else if (version.equalsIgnoreCase("v2"))
			{
				Assert.assertEquals(info.getStates().size(), 3, "incorrect states");
				Assert.assertEquals(info.getStates().stream()
						.filter(s -> s.getLabel().equals("Case Terminated")
								&& s.getValue().equals("CASETERMINATED - Y11")
								&& s.getIsTerminal().toString().equals("true"))
						.count(), 1);
			}
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{

			Assert.assertEquals(info.getLinks().stream()
					.filter(s -> s.getName().equals("referredbythecases")
							&& s.getLabel().equals("Referred by the cases") && s.getType().equals("CaseY1")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("likesitsownkind")
									&& s.getLabel().equals("Likes It's Own Kind") && s.getType().equals("CaseY11"))
							.count(),
					1);

			Assert.assertNull(info.getLinks().get(1).getIsArray(), "incorrect isArray for the link");

			if (version.equalsIgnoreCase("v1"))
			{
				assertEquals(info.getLinks().size(), 3, "incorrect number of links");

				Assert.assertEquals(info.getLinks().stream()
						.filter(s -> s.getName().equals("likedbyitsownkind")
								&& s.getLabel().equals("Liked By It's Own Kind") && s.getType().equals("CaseY11"))
						.count(), 1);

				Assert.assertNull(info.getLinks().get(2).getIsArray(), "incorrect isArray for the link");

			}

			else if (version.equalsIgnoreCase("v2"))
			{
				assertEquals(info.getLinks().size(), 4, "incorrect number of links");

				Assert.assertEquals(info.getLinks().stream()
						.filter(s -> s.getName().equals("likedbyitsownkind")
								&& s.getLabel().equals("Liked By It's Own Kind") && s.getType().equals("CaseY11")
								&& s.getIsArray().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getLinks().stream()
						.filter(s -> s.getName().equals("likesupgradedcases")
								&& s.getLabel().equals("Likes Upgraded Cases") && s.getType().equals("CaseY22"))
						.count(), 1);

				Assert.assertNull(info.getLinks().get(3).getIsArray(), "incorrect isArray for the link");

			}
		}
	}

	private void assertIsY1_2(TypeInfo info, List<String> aspectNames)
	{

		if (aspectNames.contains("basic") || aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getName(), "CaseY22", "incorrect name");
			Assert.assertEquals(info.getLabel(), "CaseY22", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.crossproj.projy.modely1", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.crossproj.projy", "incorrect application id");
		}
		if (aspectNames.contains("summaryAttributes") || aspectNames.contains("sa")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");

			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("cidy2")
					&& s.getLabel().equals("cidy2") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("10")).count(), 1);
			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("attributes") || aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getAttributes().size(), 2, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("cidy2")
					&& s.getLabel().equals("cidy2") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getConstraints().getLength().toString().equals("10")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("state")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("states") || aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
		{
			Assert.assertEquals(info.getStates().size(), 3, "incorrect states");

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Created") && s.getValue().equals("CASECREATED - Y22")).count(),
					1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("Completed") && s.getValue().equals("CASECOMPLETED - Y22"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream().filter(s -> s.getLabel().equals("Intermediate")
					&& s.getValue().equals("CASEINPROGRESS - Y22") && s.getIsTerminal().toString().equals("true"))
					.count(), 1);
		}
		if (aspectNames.contains("dependencies") || aspectNames.contains("d")
				|| aspectNames.get(0).equalsIgnoreCase(""))
		{
			assertTrue(info.getDependencies().isEmpty(), "dependencies should be empty");
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect dependencies");
		}
		if (aspectNames.contains("links") || aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
		{

			assertEquals(info.getLinks().size(), 4, "incorrect number of links");

			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("likedbyoldcases")
									&& s.getLabel().equals("Liked By Old Cases") && s.getType().equals("CaseY11"))
							.count(),
					1);
			Assert.assertNull(info.getLinks().get(0).getIsArray(), "incorrect isArray for the link");

			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("lovedbyoldcases")
									&& s.getLabel().equals("Loved By Old Cases") && s.getType().equals("CaseY1"))
							.count(),
					1);
			Assert.assertNull(info.getLinks().get(1).getIsArray(), "incorrect isArray for the link");

			Assert.assertEquals(
					info.getLinks().stream()
							.filter(s -> s.getName().equals("followsself") && s.getLabel().equals("Follws Self")
									&& s.getType().equals("CaseY22") && s.getIsArray().toString().equals("true"))
							.count(),
					1);
			Assert.assertEquals(
					info.getLinks()
							.stream().filter(s -> s.getName().equals("followedbyself")
									&& s.getLabel().equals("Follwed By Self") && s.getType().equals("CaseY22"))
							.count(),
					1);
			Assert.assertNull(info.getLinks().get(3).getIsArray(), "incorrect isArray for the link");

		}
	}

	//Test to select types in an application with different combinations of skip and top for all select combinations
	@Test(groups = {
			"OneDeployment"}, description = "Test to select types in an application with different combinations of skip and top  for all select combinations")
	public void testTypesAllSelect() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, InternalException, InterruptedException
	{

		try
		{
			deploymentIdApp1 = deploy("/apps/app1");
			System.out.println("deployment successful");
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment unsuccessful");
			Assert.fail();
		}

		// Call with skip/top in 0-5 range and assert that correct type(s) come back with application id specified
		for (int skip = 0; skip <= 5; skip++)
		{
			for (int top = 0; top <= 5; top++)
			{
				Map<String, String> filterMap = new HashMap<String, String>();

				filterMap.put("isCase", "");
				filterMap.put("namespace", "");
				filterMap.put("applicationId", "com.example.ProjectX");
				filterMap.put("applicationMajorVersion", "");

				for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
				{

					response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], String.valueOf(skip),
							String.valueOf(top));

					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + "skip = " + skip + " top = " + top + " select value = "
							+ selectArray[stringArrayIterator]); // useful for debugging

					String repsonseJson = response.getBody().asString();
					TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

					// Expect quantity to be equal to 4 minus the skipped types.
					Assert.assertEquals(body.size(), Math.min(top, Math.max(0, 4 - skip)));

					List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

					int idx = 0;
					if (skip < 1 && top >= 1)
					{
						TypeInfo info = body.get(idx++);
						assertIsAddress(info, aspectNames);
					}
					if (skip < 2 && skip + top >= 2)
					{
						TypeInfo info = body.get(idx++);
						assertIsClaim(info, aspectNames);
					}
					if (skip < 3 && skip + top >= 3)
					{
						TypeInfo info = body.get(idx++);
						assertIsPerson(info, aspectNames);
					}
					if (skip < 4 && skip + top >= 4)
					{
						TypeInfo info = body.get(idx++);
						assertIsPolicy(info, aspectNames);
					}
				}

				for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
				{

					response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator],
							String.valueOf(skip), String.valueOf(top));

					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + "skip = " + skip + " top = " + top + " select value = "
							+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

					String repsonseJson = response.getBody().asString();
					TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

					// Expect quantity to be equal to 4 minus the skipped types.
					Assert.assertEquals(body.size(), Math.min(top, Math.max(0, 4 - skip)));

					List<String> aspectNames = Arrays
							.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

					int idx = 0;
					if (skip < 1 && top >= 1)
					{
						TypeInfo info = body.get(idx++);
						assertIsAddress(info, aspectNames);
					}
					if (skip < 2 && skip + top >= 2)
					{
						TypeInfo info = body.get(idx++);
						assertIsClaim(info, aspectNames);
					}
					if (skip < 3 && skip + top >= 3)
					{
						TypeInfo info = body.get(idx++);
						assertIsPerson(info, aspectNames);
					}
					if (skip < 4 && skip + top >= 4)
					{
						TypeInfo info = body.get(idx++);
						assertIsPolicy(info, aspectNames);
					}
				}

			}
		}

		// Call with skip/top in 0-5 range and assert that correct type(s) come back with empty filter
		for (int skip = 0; skip <= 5; skip++)
		{
			for (int top = 0; top <= 5; top++)
			{
				Map<String, String> filterMap = new HashMap<String, String>();

				filterMap.put("isCase", "");
				filterMap.put("namespace", "");
				filterMap.put("applicationId", "");
				filterMap.put("applicationMajorVersion", "");

				for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
				{

					response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], String.valueOf(skip),
							String.valueOf(top));

					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + "skip = " + skip + " top = " + top + " select value = "
							+ selectArray[stringArrayIterator]); // useful for debugging

					String repsonseJson = response.getBody().asString();
					TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

					List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

					// Expect quantity to be equal to 4 minus the skipped types.
					Assert.assertEquals(body.size(), Math.min(top, Math.max(0, 4 - skip)));
					int idx = 0;
					if (skip < 1 && top >= 1)
					{
						TypeInfo info = body.get(idx++);
						assertIsAddress(info, aspectNames);
					}
					if (skip < 2 && skip + top >= 2)
					{
						TypeInfo info = body.get(idx++);
						assertIsClaim(info, aspectNames);
					}
					if (skip < 3 && skip + top >= 3)
					{
						TypeInfo info = body.get(idx++);
						assertIsPerson(info, aspectNames);
					}
					if (skip < 4 && skip + top >= 4)
					{
						TypeInfo info = body.get(idx++);
						assertIsPolicy(info, aspectNames);
					}
				}

				for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
				{

					response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator],
							String.valueOf(skip), String.valueOf(top));

					Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
					jsonPath = response.jsonPath();
					System.out.println(jsonPath.getString("")); // useful for debugging
					System.out.println("values in the loop " + "skip = " + skip + " top = " + top + " select value = "
							+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

					String repsonseJson = response.getBody().asString();
					TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

					List<String> aspectNames = Arrays
							.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

					// Expect quantity to be equal to 4 minus the skipped types.
					Assert.assertEquals(body.size(), Math.min(top, Math.max(0, 4 - skip)));
					int idx = 0;
					if (skip < 1 && top >= 1)
					{
						TypeInfo info = body.get(idx++);
						assertIsAddress(info, aspectNames);
					}
					if (skip < 2 && skip + top >= 2)
					{
						TypeInfo info = body.get(idx++);
						assertIsClaim(info, aspectNames);
					}
					if (skip < 3 && skip + top >= 3)
					{
						TypeInfo info = body.get(idx++);
						assertIsPerson(info, aspectNames);
					}
					if (skip < 4 && skip + top >= 4)
					{
						TypeInfo info = body.get(idx++);
						assertIsPolicy(info, aspectNames);
					}
				}
			}
		}
	}

	//Test to get types in an application without top mentioned
	@Test(description = "Test to get types in an application without top mentioned")
	public void testTypesErrorsForTop() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		//top is not specified
		for (int skip = 0; skip <= 5; skip++)
		{
			Map<String, String> filterMap = new HashMap<String, String>();

			filterMap.put("isCase", "");
			filterMap.put("namespace", "");
			filterMap.put("applicationId", "com.example.ProjectX");
			filterMap.put("applicationMajorVersion", "");

			for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
			{

				response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], String.valueOf(skip), "");

				Assert.assertEquals(response.getStatusCode(), properties.getTypesTopMandatory().errorResponse,
						"incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

				Assert.assertEquals(jsonPath.getString("errorCode"), properties.getTypesTopMandatory().errorCode,
						"incorrect error code");
				Assert.assertEquals(jsonPath.getString("errorMsg"), properties.getTypesTopMandatory().errorMsg,
						"incorrect error message");
				Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
				Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
				Assert.assertEquals(
						jsonPath.getString("stackTrace")
								.contains("com.tibco.bpm.cdm.api.exception.ArgumentException: Top is mandatory"),
						true, "incorrect stacktrace");
			}

			for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
			{

				response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator],
						String.valueOf(skip), "");

				Assert.assertEquals(response.getStatusCode(), properties.getTypesTopMandatory().errorResponse,
						"incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = "
						+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

				Assert.assertEquals(jsonPath.getString("errorCode"), properties.getTypesTopMandatory().errorCode,
						"incorrect error code");
				Assert.assertEquals(jsonPath.getString("errorMsg"), properties.getTypesTopMandatory().errorMsg,
						"incorrect error message");
				Assert.assertNotEquals(jsonPath.getString("contextAttributes"), null, "context attribute is null");
				Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
				Assert.assertEquals(
						jsonPath.getString("stackTrace")
								.contains("com.tibco.bpm.cdm.api.exception.ArgumentException: Top is mandatory"),
						true, "incorrect stacktrace");
			}
		}

		//too large value of top specified
		for (int skip = 0; skip <= 5; skip++)
		{
			Map<String, String> filterMap = new HashMap<String, String>();

			filterMap.put("applicationId", "com.example.ProjectX");

			for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
			{

				response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], String.valueOf(skip),
						"9999999999");

				System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

				//assertion against not found status code since the value specified as the top does not qualify to be an integer 
				Assert.assertEquals(response.getStatusCode(), notFoundStatusCode, "incorrect response");
			}

			for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
			{

				response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator],
						String.valueOf(skip), "9999999999");

				System.out.println("values in the loop " + " select value = "
						+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

				//assertion against not found status code since the value specified as the top does not qualify to be an integer 
				Assert.assertEquals(response.getStatusCode(), notFoundStatusCode, "incorrect response");
			}
		}
	}

	//Test to get types with filter set to different values
	@Test(dependsOnGroups = {"OneDeployment"}, description = "Test to get types with filter set to different values")
	public void testTypesWithFilterSet() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, InternalException, InterruptedException, DeploymentException
	{
		//deploy the other application 
		try
		{
			deploymentIdSBOM = deployRASC("/apps/SimpleBOMData.rasc");
			System.out.println("deployment successful");
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment unsuccessful");
			Assert.fail();
		}

		//call with skip/top in 0-5 range and assert that correct type(s) come back with all filters specified specified
		Map<String, String> filterMap = new HashMap<String, String>();

		//first filter set to select only Cases
		filterMap.put("isCase", "TRUE");
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		//second filter set to select only namespaces
		filterMap.put("namespace", "org.policycorporation");
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		//third filter set to select only application id
		filterMap.put("applicationId", "com.example.ProjectX");
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		//fourth filter set to select only application id
		filterMap.put("applicationMajorVersion", "1");
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsPerson(info, aspectNames);
			info = body.get(1);
			assertIsPolicy(info, aspectNames);
		}

		try
		{
			forceUndeploy(deploymentIdApp1);
			forceUndeploy(deploymentIdSBOM);
			System.out.println("undeployment successful");
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployment unsuccessful");
			Assert.fail();
		}

		finally
		{
			forceUndeploy(deploymentIdApp1);
			forceUndeploy(deploymentIdSBOM);
			System.out.println("undeployment successful");
		}

	}

	//Test to get types with filter set to different values for dependency checks, links and upgrade for all select combinations
	@Test(dependsOnMethods = {
			"testTypesWithFilterSet"}, description = "Test to get types with filter set to different values for dependency checks, links and upgrade for all select combinations")

	public void testTypesDependencyCheckAndUpgrade()
			throws IOException, URISyntaxException, RuntimeApplicationException, PersistenceException,
			InternalException, InterruptedException, DeploymentException
	{
		BigInteger deployIdA = null;
		BigInteger deployIdB = null;
		BigInteger deployIdC = null;
		BigInteger deployIdD = null;
		BigInteger deployIdX = null;
		BigInteger deployIdYV1 = null;
		BigInteger deployIdYV2 = null;

		Map<String, String> filterMap = new HashMap<String, String>();

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
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("deployments unsuccessful");
			Assert.fail();
		}

		//call using namespace - com.crossproj.proja.modela1
		filterMap.put("namespace", "com.crossproj.proja.modela1");

		//assert for b,sa,a,d,s
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 1, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsA1(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 1, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsA1(info, aspectNames);
		}

		//call using application id - com.crossproj.projb.modelb2
		filterMap.remove("namespace");
		filterMap.put("applicationId", "com.crossproj.projb");

		//assert for b,sa,a,d,s
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 3, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsB1(info, aspectNames);
			info = body.get(1);
			assertIsB2(info, aspectNames);
			info = body.get(2);
			assertIsB2Class(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 3, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsB1(info, aspectNames);
			info = body.get(1);
			assertIsB2(info, aspectNames);
			info = body.get(2);
			assertIsB2Class(info, aspectNames);
		}

		//call using isCase true
		filterMap.remove("applicationId");
		filterMap.put("isCase", "TRUE");

		//assert for b,s,sa,a,d,l
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "1", "5");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 4, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsB2(info, aspectNames);
			info = body.get(1);
			assertIsX1(info, aspectNames);
			info = body.get(2);
			assertIsY1(info, aspectNames, "v1");
			info = body.get(3);
			assertIsY1_1(info, aspectNames, "v1");

		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 5, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsA1(info, aspectNames);
			info = body.get(1);
			assertIsB2(info, aspectNames);
			info = body.get(2);
			assertIsX1(info, aspectNames);
			info = body.get(3);
			assertIsY1(info, aspectNames, "v1");
			info = body.get(4);
			assertIsY1_1(info, aspectNames, "v1");

		}

		//call using isCase true, namespace - com.crossproj.projx.modelx1, application id - com.crossproj.projx
		filterMap.put("namespace", "com.crossproj.projx.modelx1");
		filterMap.put("applicationId", "com.crossproj.projx");
		filterMap.put("applicationMajorVersion", "441300");

		//assert for b,sa,a,d,s
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 1, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsX1(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 1, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsX1(info, aspectNames);
		}

		//upgrade yv2
		try
		{
			deployIdYV2 = deploy("/apps/cross-project-test/ProjYv2");
			System.out.println("upgrade successful");
		}
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("upgrade unsuccessful");
			Assert.fail();
		}

		//call using isCase true, namespace - com.crossproj.projy.modely1, application id - com.crossproj.projy, applicationMajorVersion - 1
		filterMap.remove("namespace");
		filterMap.remove("applicationId");
		filterMap.remove("applicationMajorVersion");
		filterMap.put("namespace", "com.crossproj.projy.modely1");
		filterMap.put("applicationId", "com.crossproj.projy");

		//assert for b,sa,a,d,s
		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArray[stringArrayIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 3, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArray[stringArrayIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsY1(info, aspectNames, "v2");
			info = body.get(1);
			assertIsY1_1(info, aspectNames, "v2");
			info = body.get(2);
			assertIsY1_2(info, aspectNames);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			response = caseTypes.getTypes(filterMap, selectArrayAbbreviated[stringArrayAbbreviatedIterator], "0", "10");

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = "
					+ selectArrayAbbreviated[stringArrayAbbreviatedIterator]); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			Assert.assertEquals(body.size(), 3, "incorrect number is structures");

			List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[stringArrayAbbreviatedIterator].split(","));

			TypeInfo info = body.get(0);
			assertIsY1(info, aspectNames, "v2");
			info = body.get(1);
			assertIsY1_1(info, aspectNames, "v2");
			info = body.get(2);
			assertIsY1_2(info, aspectNames);
		}

		filterMap.clear();

		try
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
		catch (DeploymentException e)
		{
			e.printStackTrace();
			System.out.println("undeployments unsuccessful");
			Assert.fail();
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

	//Tests for bad select filter
	@Test(description = "Test to get types in an application with random selects mentioned")
	public void testTypesErrorsForSelect() throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, InterruptedException
	{

		String skip = "0";
		String top = "10";
		String errorMessage = "";

		Map<String, String> filterMap = new HashMap<String, String>();

		filterMap.put("isCase", "");
		filterMap.put("namespace", "");
		filterMap.put("applicationId", "com.example.ProjectX");
		filterMap.put("applicationMajorVersion", "");

		for (int stringArrayIterator = 0; stringArrayIterator < selectArray.length; stringArrayIterator++)
		{
			String randomSelectString = "";

			while (true)
			{
				randomSelectString = generateFixedString(stringArrayIterator + 1);
				if (!(randomSelectString.equals("l") || randomSelectString.equals("sa")
						|| randomSelectString.equals("a") || randomSelectString.equals("b")
						|| randomSelectString.equals("d") || randomSelectString.equals("s")
						|| randomSelectString.equals("links") || randomSelectString.equals("states")
						|| randomSelectString.equals("summaryAttributes") || randomSelectString.equals("attributes")
						|| randomSelectString.equals("basic") || randomSelectString.equals("dependencies")))
				{
					break;
				}
			}

			System.out.println("random string is : " + randomSelectString);
			String finalSelectString = "";

			if (stringArrayIterator > 0)
			{
				finalSelectString = randomSelectString + "," + selectArray[stringArrayIterator];
			}
			else if (stringArrayIterator == 0)
			{
				finalSelectString = randomSelectString;
			}
			response = caseTypes.getTypes(filterMap, finalSelectString, skip, top);
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out.println("values in the loop " + " select value = " + selectArray[stringArrayIterator]); // useful for debugging

			errorMessage = properties.getBadSelectForTypes().errorMsg;
			errorMessage = errorMessage.replaceAll("\\{.*?\\}", finalSelectString);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadSelectForTypes().errorCode);
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
			Assert.assertEquals(response.getStatusCode(), properties.getBadSelectForTypes().errorResponse);

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace")
							.contains("com.tibco.bpm.cdm.api.exception.ArgumentException: " + errorMessage),
					true, "incorrect stacktrace");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "select");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), finalSelectString);
		}

		for (int stringArrayAbbreviatedIterator = 0; stringArrayAbbreviatedIterator < selectArrayAbbreviated.length; stringArrayAbbreviatedIterator++)
		{
			String randomSelectString = "";

			while (true)
			{
				randomSelectString = generateFixedString(stringArrayAbbreviatedIterator + 1);
				if (!(randomSelectString.equals("l") || randomSelectString.equals("sa")
						|| randomSelectString.equals("a") || randomSelectString.equals("b")
						|| randomSelectString.equals("d") || randomSelectString.equals("s")
						|| randomSelectString.equals("links") || randomSelectString.equals("states")
						|| randomSelectString.equals("summaryAttributes") || randomSelectString.equals("attributes")
						|| randomSelectString.equals("basic") || randomSelectString.equals("dependencies")))
				{
					break;
				}
			}

			System.out.println("random string is : " + randomSelectString);
			String finalSelectString = "";

			if (stringArrayAbbreviatedIterator > 0)
			{
				finalSelectString = randomSelectString + "," + selectArray[stringArrayAbbreviatedIterator];
			}
			else if (stringArrayAbbreviatedIterator == 0)
			{
				finalSelectString = randomSelectString;
			}

			response = caseTypes.getTypes(filterMap, finalSelectString, skip, top);
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			System.out
					.println("values in the loop " + " select value = " + selectArray[stringArrayAbbreviatedIterator]); // useful for debugging

			errorMessage = properties.getBadSelectForTypes().errorMsg;
			errorMessage = errorMessage.replaceAll("\\{.*?\\}", finalSelectString);

			Assert.assertEquals(jsonPath.getString("errorCode"), properties.getBadSelectForTypes().errorCode);
			Assert.assertEquals(jsonPath.getString("errorMsg"), errorMessage);
			Assert.assertEquals(response.getStatusCode(), properties.getBadSelectForTypes().errorResponse);

			Assert.assertNotEquals(jsonPath.getString("stackTrace"), null, "stacktrace is null");
			Assert.assertEquals(
					jsonPath.getString("stackTrace")
							.contains("com.tibco.bpm.cdm.api.exception.ArgumentException: " + errorMessage),
					true, "incorrect stacktrace");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].name"), "select");
			Assert.assertEquals(jsonPath.getString("contextAttributes[0].value"), finalSelectString);
		}
		filterMap.clear();
	}
}
