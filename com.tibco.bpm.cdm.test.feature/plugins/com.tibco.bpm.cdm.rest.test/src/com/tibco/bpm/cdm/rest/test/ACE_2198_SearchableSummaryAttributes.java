package com.tibco.bpm.cdm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.TypeInfo;
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
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class ACE_2198_SearchableSummaryAttributes extends Utils
{
	AccountCreatorFunction		accountCreator			= new AccountCreatorFunction();

	CustomerCreatorFunction		customerCreator			= new CustomerCreatorFunction();

	CaseLinksFunction			linksFunction			= new CaseLinksFunction();

	CasesFunctions				cases					= new CasesFunctions();

	GetPropertiesFunction		properties				= new GetPropertiesFunction();

	private CaseTypesFunctions	types					= new CaseTypesFunctions();

	private Response			response				= null;

	private JsonPath			jsonPath				= null;

	private static final String	caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String	applicationMajorVersion	= "1";

	private BigInteger			deploymentIdApp1		= BigInteger.valueOf(7);

	private BigInteger			deploymentIdApp2		= BigInteger.valueOf(8);

	//			7 searchable
	//			id - text 
	//			state - text
	//			date1 - searchable
	//			date2 - searchable
	//			time1 - searchable
	//			time2 - searchable
	//			number1 - searchable
	//
	//			text(2), date(2), time(2), number(1)
	//
	//			7 summary
	//			id - text
	//			state - text 
	//			dateTimeZone - summary
	//			dateTimeZone - summary
	//			number2 - summary
	//			number3 - summary
	//			text1 - summary
	//
	//			text(2), dateTimeZone(2), number(2), text(1)- allowed value
	//
	//			boolean
	//			uri

	private void deployV1() throws IOException, URISyntaxException, DataModelSerializationException,
			RuntimeApplicationException, InterruptedException
	{

		DataModelModifier modifierVx = null;

		//get the zip file
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		modifierVx = (dm) -> {

			StructuredType stCaseType = dm.getStructuredTypeByName("Customer");

			//date1 attribute - searchable
			Attribute aCaseDate1 = stCaseType.newAttribute();
			aCaseDate1.setName("date1Searchable");
			aCaseDate1.setLabel("Date1-Searchable");
			aCaseDate1.setType("base:Date");
			aCaseDate1.setIsSearchable(true);
			aCaseDate1.setIsSummary(false);

			//date2 attribute - searchable
			Attribute aCaseDate2 = stCaseType.newAttribute();
			aCaseDate2.setName("date2Searchable");
			aCaseDate2.setLabel("Date2-Searchable");
			aCaseDate2.setType("base:Date");
			aCaseDate2.setIsSearchable(true);
			aCaseDate2.setIsSummary(false);

			//time1 attribute - searchable
			Attribute aCaseTime1 = stCaseType.newAttribute();
			aCaseTime1.setName("time1Searchable");
			aCaseTime1.setLabel("Time1-Searchable");
			aCaseTime1.setType("base:Time");
			aCaseTime1.setIsSearchable(true);
			aCaseTime1.setIsSummary(false);

			//time2 attribute - searchable
			Attribute aCaseTime2 = stCaseType.newAttribute();
			aCaseTime2.setName("time2Searchable");
			aCaseTime2.setLabel("Time2-Searchable");
			aCaseTime2.setType("base:Time");
			aCaseTime2.setIsSearchable(true);
			aCaseTime2.setIsSummary(false);

			//number attribute - searchable
			Attribute aCaseNumber1 = stCaseType.newAttribute();
			aCaseNumber1.setName("number1Searchable");
			aCaseNumber1.setLabel("Number1-Searchable");
			aCaseNumber1.setType("base:Number");
			aCaseNumber1.setIsSearchable(true);
			aCaseNumber1.setIsSummary(false);
			aCaseNumber1.newConstraint("minValue", "-999999999999998");
			aCaseNumber1.newConstraint("maxValue", "999999999999998");

			//dateTimeZone1 attribute - summary
			Attribute aCaseDateTimeTZ1 = stCaseType.newAttribute();
			aCaseDateTimeTZ1.setName("dateTimeTZ1Summary");
			aCaseDateTimeTZ1.setLabel("DateTimeTZ1-Summary");
			aCaseDateTimeTZ1.setType("base:DateTimeTZ");
			aCaseDateTimeTZ1.setIsSearchable(false);
			aCaseDateTimeTZ1.setIsSummary(true);

			//dateTimeZone2 attribute - summary
			Attribute aCaseDateTimeTZ2 = stCaseType.newAttribute();
			aCaseDateTimeTZ2.setName("dateTimeTZ2Summary");
			aCaseDateTimeTZ2.setLabel("DateTimeTZ2-Summary");
			aCaseDateTimeTZ2.setType("base:DateTimeTZ");
			aCaseDateTimeTZ2.setIsSearchable(false);
			aCaseDateTimeTZ2.setIsSummary(true);

			//number2 attribute - summary
			Attribute aCaseNumber2 = stCaseType.newAttribute();
			aCaseNumber2.setName("number2Summary");
			aCaseNumber2.setLabel("Number2-Summary");
			aCaseNumber2.setType("base:Number");
			aCaseNumber2.setIsSummary(true);
			aCaseNumber2.setIsSearchable(false);
			aCaseNumber2.newConstraint("minValue", "-999999999999998");
			aCaseNumber2.newConstraint("maxValue", "999999999999998");

			//number3 attribute - summary
			Attribute aCaseNumber3 = stCaseType.newAttribute();
			aCaseNumber3.setName("number3Summary");
			aCaseNumber3.setLabel("Number3-Summary");
			aCaseNumber3.setType("base:Number");
			aCaseNumber3.setIsSummary(true);
			aCaseNumber3.setIsSearchable(false);
			aCaseNumber3.newConstraint("minValue", "-999999999999998");
			aCaseNumber3.newConstraint("maxValue", "999999999999998");

			//text3 attribute - summary
			Attribute aCaseText3 = stCaseType.newAttribute();
			aCaseText3.setName("text3Summary");
			aCaseText3.setLabel("Text3-Summary");
			aCaseText3.setType("base:Text");
			aCaseText3.setIsSummary(true);
			aCaseText3.setIsSearchable(false);
			aCaseText3.newAllowedValue("RED", "RED");
			aCaseText3.newAllowedValue("BLUE", "BLUE");
			aCaseText3.newAllowedValue("GREEN", "GREEN");
			aCaseText3.newConstraint("length", "5");

			//boolean attribute
			Attribute aCaseBoolean = stCaseType.newAttribute();
			aCaseBoolean.setName("boolean");
			aCaseBoolean.setLabel("Boolean");
			aCaseBoolean.setType("base:Boolean");
			aCaseBoolean.setDefaultValue("true");

			//uri attribute
			Attribute aCaseUri = stCaseType.newAttribute();
			aCaseUri.setName("uRI");
			aCaseUri.setLabel("URI");
			aCaseUri.setType("base:URI");
			aCaseUri.setDefaultValue("http://www.tibco.com/");

			//performer attribute
			Attribute aCasePerfomer = stCaseType.newAttribute();
			aCasePerfomer.setName("performer");
			aCasePerfomer.setLabel("Performer");
			aCasePerfomer.setType("base:Text");
			aCasePerfomer.setDefaultValue("tibco-admin");
		};

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierVx);

		//deploy Vx
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp1);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//the test should never reach here
		}
		catch (DeploymentException |

				InternalException e)
		{
			System.out.println("deployment is not successful");
			Assert.fail();
		}
	}

	private void deployV2() throws IOException, URISyntaxException, DataModelSerializationException,
			RuntimeApplicationException, InterruptedException
	{

		DataModelModifier modifierVx = null;

		//get the zip file
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), "1.0.1.0");

		modifierVx = (dm) -> {

			StructuredType stCaseType = dm.getStructuredTypeByName("Customer");

			//date1 attribute - searchable
			Attribute aCaseDate1 = stCaseType.newAttribute();
			aCaseDate1.setName("date1Searchable");
			aCaseDate1.setLabel("Date1-Searchable");
			aCaseDate1.setType("base:Date");
			aCaseDate1.setIsSearchable(false);
			aCaseDate1.setIsSummary(true);

			//date2 attribute - searchable
			Attribute aCaseDate2 = stCaseType.newAttribute();
			aCaseDate2.setName("date2Searchable");
			aCaseDate2.setLabel("Date2-Searchable");
			aCaseDate2.setType("base:Date");
			aCaseDate2.setIsSearchable(false);
			aCaseDate2.setIsSummary(true);

			//time1 attribute - searchable
			Attribute aCaseTime1 = stCaseType.newAttribute();
			aCaseTime1.setName("time1Searchable");
			aCaseTime1.setLabel("Time1-Searchable");
			aCaseTime1.setType("base:Time");
			aCaseTime1.setIsSearchable(false);
			aCaseTime1.setIsSummary(true);

			//time2 attribute - searchable
			Attribute aCaseTime2 = stCaseType.newAttribute();
			aCaseTime2.setName("time2Searchable");
			aCaseTime2.setLabel("Time2-Searchable");
			aCaseTime2.setType("base:Time");
			aCaseTime2.setIsSearchable(false);
			aCaseTime2.setIsSummary(true);

			//number attribute - searchable
			Attribute aCaseNumber1 = stCaseType.newAttribute();
			aCaseNumber1.setName("number1Searchable");
			aCaseNumber1.setLabel("Number1-Searchable");
			aCaseNumber1.setType("base:Number");
			aCaseNumber1.setIsSearchable(false);
			aCaseNumber1.setIsSummary(true);
			aCaseNumber1.newConstraint("minValue", "-999999999999998");
			aCaseNumber1.newConstraint("maxValue", "999999999999998");

			//dateTimeZone1 attribute - summary
			Attribute aCaseDateTimeTZ1 = stCaseType.newAttribute();
			aCaseDateTimeTZ1.setName("dateTimeTZ1Summary");
			aCaseDateTimeTZ1.setLabel("DateTimeTZ1-Summary");
			aCaseDateTimeTZ1.setType("base:DateTimeTZ");
			aCaseDateTimeTZ1.setIsSearchable(true);
			aCaseDateTimeTZ1.setIsSummary(false);

			//dateTimeZone2 attribute - summary
			Attribute aCaseDateTimeTZ2 = stCaseType.newAttribute();
			aCaseDateTimeTZ2.setName("dateTimeTZ2Summary");
			aCaseDateTimeTZ2.setLabel("DateTimeTZ2-Summary");
			aCaseDateTimeTZ2.setType("base:DateTimeTZ");
			aCaseDateTimeTZ2.setIsSearchable(true);
			aCaseDateTimeTZ2.setIsSummary(false);

			//number2 attribute - summary
			Attribute aCaseNumber2 = stCaseType.newAttribute();
			aCaseNumber2.setName("number2Summary");
			aCaseNumber2.setLabel("Number2-Summary");
			aCaseNumber2.setType("base:Number");
			aCaseNumber2.setIsSearchable(true);
			aCaseNumber2.setIsSummary(false);
			aCaseNumber2.newConstraint("minValue", "-999999999999998");
			aCaseNumber2.newConstraint("maxValue", "999999999999998");

			//number3 attribute - summary
			Attribute aCaseNumber3 = stCaseType.newAttribute();
			aCaseNumber3.setName("number3Summary");
			aCaseNumber3.setLabel("Number3-Summary");
			aCaseNumber3.setType("base:Number");
			aCaseNumber3.setIsSearchable(true);
			aCaseNumber3.setIsSummary(false);
			aCaseNumber3.newConstraint("minValue", "-999999999999998");
			aCaseNumber3.newConstraint("maxValue", "999999999999998");

			//text3 attribute - summary
			Attribute aCaseText3 = stCaseType.newAttribute();
			aCaseText3.setName("text3Summary");
			aCaseText3.setLabel("Text3-Summary");
			aCaseText3.setType("base:Text");
			aCaseText3.setIsSearchable(true);
			aCaseText3.setIsSummary(false);
			aCaseText3.newAllowedValue("RED", "RED");
			aCaseText3.newAllowedValue("BLUE", "BLUE");
			aCaseText3.newAllowedValue("GREEN", "GREEN");
			aCaseText3.newConstraint("length", "6");

			//boolean attribute
			Attribute aCaseBoolean = stCaseType.newAttribute();
			aCaseBoolean.setName("boolean");
			aCaseBoolean.setLabel("Boolean");
			aCaseBoolean.setType("base:Boolean");
			aCaseBoolean.setDefaultValue("false");

			//uri attribute
			Attribute aCaseUri = stCaseType.newAttribute();
			aCaseUri.setName("uRI");
			aCaseUri.setLabel("URI");
			aCaseUri.setType("base:URI");
			aCaseUri.setDefaultValue("http://www.vista.com/");

			//performer attribute
			Attribute aCasePerfomer = stCaseType.newAttribute();
			aCasePerfomer.setName("performer");
			aCasePerfomer.setLabel("Performer");
			aCasePerfomer.setType("base:Text");
			aCasePerfomer.setDefaultValue("vista-admin");
		};

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierVx);

		//deploy Vx
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

		try
		{
			deploymentManager.deploy(runtimeApplication);
			//the test should never reach here
		}
		catch (DeploymentException |

				InternalException e)
		{
			System.out.println("upgrade is not successful");
			Assert.fail();
		}

	}

	private void assertTypes(String version, Response response)
	{
		try
		{
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

			// Expect quantity to be equal to 4 minus the skipped types.
			Assert.assertEquals(body.size(), 2, "incorrect number of objects returned");

			TypeInfo info = new TypeInfo();

			for (int i = 0; i < body.size(); i++)
			{
				if (body.get(i).getName().equals("Customer")
						|| body.get(i).getSummaryAttributes().contains("customerCID")
						|| body.get(i).getAttributes().contains("customerCID")
						|| body.get(i).getStates().contains("VERYACTIVE50KABOVE")
						|| body.get(i).getLinks().contains("holdstheaccounts"))
				{
					//storing only the type for the Customer case
					info = body.get(i);
				}
			}

			//assert basic
			Assert.assertEquals(info.getName(), "Customer", "incorrect name");
			Assert.assertEquals(info.getLabel(), "Customer", "incorrect label");
			Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
			Assert.assertEquals(info.getNamespace(), "com.example.bankdatamodel", "incorrect namespace");
			Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
					"incorrect application major version");
			Assert.assertEquals(info.getApplicationId(), "com.example.bankdatamodel", "incorrect application id");

			//assert summary attributes
			Assert.assertEquals(info.getSummaryAttributes().size(), 7, "incorrect summary attributes");
			Assert.assertEquals(info.getSummaryAttributes().stream().filter(s -> s.getName().equals("customerCID")
					&& s.getLabel().equals("Customer CID") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getIsAutoIdentifier().toString().equals("true")).count(), 1);

			Assert.assertEquals(info.getSummaryAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			//assert additional summary attributes
			if (version.equals("v1"))
			{
				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ1Summary")
								&& s.getLabel().equals("DateTimeTZ1-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ2Summary")
								&& s.getLabel().equals("DateTimeTZ2-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("number2Summary") && s.getLabel().equals("Number2-Summary")
								&& s.getType().equals("Number") && s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("number3Summary") && s.getLabel().equals("Number3-Summary")
								&& s.getType().equals("Number") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("text3Summary") && s.getLabel().equals("Text3-Summary")
								&& s.getType().equals("Text") && s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getLength().toString().equals("5"))
						.count(), 1);
			}
			else if (version.equals("v2"))
			{
				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("date1Searchable") && s.getLabel().equals("Date1-Searchable")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("date2Searchable") && s.getLabel().equals("Date2-Searchable")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("time1Searchable") && s.getLabel().equals("Time1-Searchable")
								&& s.getType().equals("Time") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("time2Searchable") && s.getLabel().equals("Time2-Searchable")
								&& s.getType().equals("Time") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getSummaryAttributes().stream()
						.filter(s -> s.getName().equals("number1Searchable")
								&& s.getLabel().equals("Number1-Searchable") && s.getType().equals("Number")
								&& s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);
			}

			//assert attributes
			Assert.assertEquals(info.getAttributes().size(), 17, "incorrect attributes size");

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("customerCID")
					&& s.getLabel().equals("Customer CID") && s.getType().equals("Text")
					&& s.getIsMandatory().toString().equals("true") && s.getIsIdentifier().toString().equals("true")
					&& s.getIsSearchable().toString().equals("true") && s.getIsSummary().toString().equals("true")
					&& s.getIsAutoIdentifier().toString().equals("true")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
							&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
							&& s.getIsState().toString().equals("true") && s.getIsSearchable().toString().equals("true")
							&& s.getIsSummary().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("personalDetails") && s.getLabel().equals("Personal Details")
							&& s.getType().equals("PersonalDetails")
							&& s.getIsStructuredType().toString().equals("true")
							&& s.getIsMandatory().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("professionalDetails")
							&& s.getLabel().equals("Professional Details") && s.getType().equals("ProfessionalDetails")
							&& s.getIsStructuredType().toString().equals("true")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream()
					.filter(s -> s.getName().equals("uRI") && s.getLabel().equals("URI") && s.getType().equals("URI"))
					.count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("boolean")
					&& s.getLabel().equals("Boolean") && s.getType().equals("Boolean")).count(), 1);

			Assert.assertEquals(info.getAttributes().stream().filter(s -> s.getName().equals("performer")
					&& s.getLabel().equals("Performer") && s.getType().equals("Text")).count(), 1);

			//assert additional attributes
			if (version.equals("v1"))
			{
				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("date1Searchable") && s.getLabel().equals("Date1-Searchable")
								&& s.getType().equals("Date") && s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("date2Searchable") && s.getLabel().equals("Date2-Searchable")
								&& s.getType().equals("Date") && s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("time1Searchable") && s.getLabel().equals("Time1-Searchable")
								&& s.getType().equals("Time") && s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("time2Searchable") && s.getLabel().equals("Time2-Searchable")
								&& s.getType().equals("Time") && s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number1Searchable")
								&& s.getLabel().equals("Number1-Searchable") && s.getType().equals("Number")
								&& s.getIsSearchable().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ1Summary")
								&& s.getLabel().equals("DateTimeTZ1-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ2Summary")
								&& s.getLabel().equals("DateTimeTZ2-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number2Summary") && s.getLabel().equals("Number2-Summary")
								&& s.getType().equals("Number") && s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number3Summary") && s.getLabel().equals("Number3-Summary")
								&& s.getType().equals("Number") && s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("text3Summary") && s.getLabel().equals("Text3-Summary")
								&& s.getType().equals("Text") && s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getLength().toString().equals("5"))
						.count(), 1);
			}
			else if (version.equals("v2"))
			{
				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("date1Searchable") && s.getLabel().equals("Date1-Searchable")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("date2Searchable") && s.getLabel().equals("Date2-Searchable")
								&& s.getType().equals("Date") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("time1Searchable") && s.getLabel().equals("Time1-Searchable")
								&& s.getType().equals("Time") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("time2Searchable") && s.getLabel().equals("Time2-Searchable")
								&& s.getType().equals("Time") && s.getIsSummary().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number1Searchable")
								&& s.getLabel().equals("Number1-Searchable") && s.getType().equals("Number")
								&& s.getIsSummary().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ1Summary")
								&& s.getLabel().equals("DateTimeTZ1-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("dateTimeTZ2Summary")
								&& s.getLabel().equals("DateTimeTZ2-Summary") && s.getType().equals("DateTimeTZ")
								&& s.getIsSearchable().toString().equals("true"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number2Summary") && s.getLabel().equals("Number2-Summary")
								&& s.getType().equals("Number") && s.getIsSearchable().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("number3Summary") && s.getLabel().equals("Number3-Summary")
								&& s.getType().equals("Number") && s.getIsSearchable().toString().equals("true")
								&& s.getConstraints().getMinValue().toString().equals("-999999999999998")
								&& s.getConstraints().getMaxValue().toString().equals("999999999999998"))
						.count(), 1);

				Assert.assertEquals(info.getAttributes().stream()
						.filter(s -> s.getName().equals("text3Summary") && s.getLabel().equals("Text3-Summary")
								&& s.getType().equals("Text") && s.getIsSearchable().toString().equals("true")
								&& s.getConstraints().getLength().toString().equals("6"))
						.count(), 1);
			}

			//assert states
			Assert.assertEquals(info.getStates().size(), 9, "incorrect states");

			Assert.assertEquals(info.getStates().stream().filter(
					s -> s.getLabel().equals("VERY ACTIVE - (50K ABOVE)") && s.getValue().equals("VERYACTIVE50KABOVE"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("ACTIVE - (10K TO 50K)") && s.getValue().equals("ACTIVE10KTO50K"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("REGULAR - (1K TO 10K)") && s.getValue().equals("REGULAR1KTO10K"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("SELDOM - (0 TO 1K)") && s.getValue().equals("SELDOM0TO1K"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("ACTIVE - (10K TO 50K)") && s.getValue().equals("ACTIVE10KTO50K"))
					.count(), 1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("ACTIVE BUT ON HOLD") && s.getValue().equals("ACTIVEBUTONHOLD"))
					.count(), 1);

			Assert.assertEquals(
					info.getStates().stream()
							.filter(s -> s.getLabel().equals("INACTIVE") && s.getValue().equals("INACTIVE")).count(),
					1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("BARRED OR TERMINATED")
							&& s.getValue().equals("BARREDORTERMINATED") && s.getIsTerminal().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(
					info.getStates().stream().filter(s -> s.getLabel().equals("CANCELLED")
							&& s.getValue().equals("CANCELLED") && s.getIsTerminal().toString().equals("true")).count(),
					1);

			Assert.assertEquals(info.getStates().stream()
					.filter(s -> s.getLabel().equals("TRIAL") && s.getValue().equals("TRIAL")).count(), 1);

			//assert dependencies
			Assert.assertEquals(info.getDependencies().size(), 0, "incorrect number of dependecies");

			//assert links
			Assert.assertEquals(info.getLinks().size(), 3, "incorrect number of links");

			Assert.assertEquals(info.getLinks().stream()
					.filter(s -> s.getName().equals("holdstheaccounts") && s.getLabel().equals("Holds The Accounts")
							&& s.getType().equals("Account") && s.getIsArray().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getLinks().stream()
					.filter(s -> s.getName().equals("social_circle_of") && s.getLabel().equals("social circle of")
							&& s.getType().equals("Customer") && s.getIsArray().toString().equals("true"))
					.count(), 1);

			Assert.assertEquals(info.getLinks().stream()
					.filter(s -> s.getName().equals("socially_mingle_with")
							&& s.getLabel().equals("socially_mingle_with") && s.getType().equals("Customer")
							&& s.getIsArray().toString().equals("true"))
					.count(), 1);
		}
		catch (Exception e)
		{
			System.out.println("problem while getting types");
			System.out.println(e.getMessage());
			Assert.fail();
		}

	}

	private void assertCasedataSummaryReference(String casedata, String caseSummary, String caseReference,
			Response response) throws JsonParseException, JsonMappingException, IOException
	{
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), 1);
		//assert casedata
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), casedata), "casedata does not match");
		//assert metadata
		Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
		Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"), "metadata should be tagged");
		//assert case reference
		Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReference, "caseRef should be tagged");
		//assert summary
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), caseSummary), "summary does not match");
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
				Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(), caseRefs.get(caseRefIterator + offset),
						"linked case reference is incorrect");
			}
			Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
		}
	}

	//Test search and findBy(criteria) with 7 searchable and 7 summary attributes
	@Test(description = "Test search and findBy(criteria) with 7 searchable and 7 summary attributes")
	public void testSearchAndFindBy() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, CasedataException, ReferenceException, ArgumentException,
			DataModelSerializationException, InterruptedException
	{
		List<String> caseRefsCustomers = new ArrayList<>();
		caseRefsCustomers.clear();

		String skip = "0";
		String top = "5";

		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();

		try
		{
			//values of searchable attributes
			String[] searchValuesV1 = {"WINTERFELL-00001", "ACTIVEBUTONHOLD", "1998-08-15", "2018-12-25", "11:54:56",
					"04:02:16", "5.22"};
			String[] searchValuesV2 = {"WINTERFELL-00001", "ACTIVEBUTONHOLD", "BLUE", "-295653.11", "731232.232",
					"1994-12-13T09:13:41.698-08:00", "1970-07-16T01:46:20.699+06:00"};
			String[] dqlValuesV1 = {"'WINTERFELL-00001'", "'ACTIVEBUTONHOLD'", "1998-08-15", "2018-12-25", "11:54:56",
					"04:02:16", "5.22"};
			String[] dqlValuesV2 = {"'WINTERFELL-00001'", "'ACTIVEBUTONHOLD'", "'BLUE'", "-295653.11", "731232.232",
					"1994-12-13T09:13:41.698-08:00", "1970-07-16T01:46:20.699+06:00"};

			//searchable attributes
			String[] searchTermsV1 = {"customerCID", "state", "date1Searchable", "date2Searchable", "time1Searchable",
					"time2Searchable", "number1Searchable"};
			String[] searchTermsV2 = {"customerCID", "state", "text3Summary", "number2Summary", "number3Summary",
					"dateTimeTZ1Summary", "dateTimeTZ2Summary"};

			String createCustomerV1 = "{\"uRI\": \"https://www.tibco.com\", \"state\": \"ACTIVEBUTONHOLD\", \"boolean\": true, \"performer\": \"tibco-admin\", \"text3Summary\": \"BLUE\", \"date1Searchable\": \"1998-08-15\", \"date2Searchable\": \"2018-12-25\", \"personalDetails\": {\"age\": 33, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Morreti\", \"firstName\": \"Alessandra\", \"salutation\": \"LADY\", \"dateofBirth\": \"1983-11-18\", \"homeAddress\": {\"city\": \"Swindon\", \"county\": \"Wiltshire\", \"country\": \"United Kingdom\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"First Line V1\", \"secondLine\": \"Second Line V1\"}, \"numberofYearsStayingattheAddress\": 4.9}, \"time1Searchable\": \"11:54:56\", \"time2Searchable\": \"04:02:16\", \"number1Searchable\": 5.22, \"number2Summary\": -295653.11, \"number3Summary\": 731232.232, \"dateTimeTZ1Summary\": \"1994-12-13T09:13:41.698-08:00\", \"dateTimeTZ2Summary\": \"1970-07-16T01:46:20.699+06:00\", \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Venice\", \"county\": \"Venezia\", \"country\": \"Italy\", \"postCode\": \"10086\", \"firstLine\": \"First Line V1\", \"secondLine\": \"Second Line V1\"}, \"dateofJoining\": \"2002-11-22\", \"typeofEmployement\": \"Gondola Rider\", \"annualIncomeSalaryBeforeTaxes\": 3684.44}]}";
			String casedataCustomerV1 = "{\"customerCID\": \"WINTERFELL-00001\", \"uRI\": \"https://www.tibco.com\", \"state\": \"ACTIVEBUTONHOLD\", \"boolean\": true, \"performer\": \"tibco-admin\", \"text3Summary\": \"BLUE\", \"date1Searchable\": \"1998-08-15\", \"date2Searchable\": \"2018-12-25\", \"personalDetails\": {\"age\": 33, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Morreti\", \"firstName\": \"Alessandra\", \"salutation\": \"LADY\", \"dateofBirth\": \"1983-11-18\", \"homeAddress\": {\"city\": \"Swindon\", \"county\": \"Wiltshire\", \"country\": \"United Kingdom\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"First Line V1\", \"secondLine\": \"Second Line V1\"}, \"numberofYearsStayingattheAddress\": 4.9}, \"time1Searchable\": \"11:54:56\", \"time2Searchable\": \"04:02:16\", \"number1Searchable\": 5.22, \"number2Summary\": -295653.11, \"number3Summary\": 731232.232, \"dateTimeTZ1Summary\": \"1994-12-13T09:13:41.698-08:00\", \"dateTimeTZ2Summary\": \"1970-07-16T01:46:20.699+06:00\", \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Venice\", \"county\": \"Venezia\", \"country\": \"Italy\", \"postCode\": \"10086\", \"firstLine\": \"First Line V1\", \"secondLine\": \"Second Line V1\"}, \"dateofJoining\": \"2002-11-22\", \"typeofEmployement\": \"Gondola Rider\", \"annualIncomeSalaryBeforeTaxes\": 3684.44}]}";
			String summaryCustomerV1 = "{\"customerCID\":\"WINTERFELL-00001\", \"state\":\"ACTIVEBUTONHOLD\", \"dateTimeTZ1Summary\":\"1994-12-13T09:13:41.698-08:00\",  \"dateTimeTZ2Summary\":\"1970-07-16T01:46:20.699+06:00\", \"number2Summary\":-295653.11, \"number3Summary\":731232.232, \"text3Summary\":\"BLUE\"}";
			String summaryCustomerV2 = "{\"customerCID\":\"WINTERFELL-00001\", \"state\":\"ACTIVEBUTONHOLD\", \"date1Searchable\":\"1998-08-15\",  \"date2Searchable\":\"2018-12-25\", \"time1Searchable\":\"11:54:56\", \"time2Searchable\":\"04:02:16\", \"number1Searchable\":5.22}";

			//deploy v1
			deployV1();

			//get types
			filterMap.put("isCase", "TRUE");
			filterMap.put("namespace", "com.example.bankdatamodel");
			filterMap.put("applicationId", "com.example.bankdatamodel");
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top);
			//assert response
			assertTypes("v1", response);
			//empty filter
			filterMap.clear();

			//create new case type cases and store the case references			
			List<String> casedataCustomer = new ArrayList<String>();
			casedataCustomer.add(createCustomerV1);
			response = cases.createCases(caseTypeCustomer, casedataCustomer);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsCustomers.add(jsonPath.getString("[0]"));

			//create 2 extra cases
			caseRefsCustomers
					.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "REGULAR1KTO10K"));
			caseRefsCustomers.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "SELDOM0TO1K"));

			//link customer 0 and 1 to customer 2
			linksPostRequestBody = formulateBodyForLinkCases(0, 2, caseRefsCustomers, "socially_mingle_with");
			response = linksFunction.createLinks(caseRefsCustomers.get(2), linksPostRequestBody);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//search cases
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchTermsIterator = 0; searchTermsIterator < searchValuesV1.length; searchTermsIterator++)
			{
				response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top,
						searchValuesV1[searchTermsIterator], "");
				assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV1, caseRefsCustomers.get(0),
						response);
			}
			//empty filter
			filterMap.clear();

			//search cases with isInTerminalState flag set to FALSE
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("isInTerminalState", "FALSE");
			for (int searchTermsIterator = 0; searchTermsIterator < searchValuesV1.length; searchTermsIterator++)
			{
				response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top,
						searchValuesV1[searchTermsIterator], "");
				assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV1, caseRefsCustomers.get(0),
						response);
			}
			//empty filter
			filterMap.clear();

			//findBy(criteria) with big criteria having all the searchable attributes including id and state
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int dqlIterator = 0; dqlIterator < dqlValuesV1.length; dqlIterator++)
			{
				filterDql.put(searchTermsV1[dqlIterator], dqlValuesV1[dqlIterator]);
			}
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV1, caseRefsCustomers.get(0), response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//navigateBy(criteria) using all values for individual searchable attributes 
			filterMap.put("name", "socially_mingle_with");
			for (int dqlIterator = 0; dqlIterator < dqlValuesV1.length; dqlIterator++)
			{
				filterDql.put(searchTermsV1[dqlIterator], dqlValuesV1[dqlIterator]);
			}
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), skip, top);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "socially_mingle_with", response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//upgrade the application to switch searchable attributes as summary attribute and vice-versa
			deployV2();

			//get types
			filterMap.put("isCase", "TRUE");
			filterMap.put("namespace", "com.example.bankdatamodel");
			filterMap.put("applicationId", "com.example.bankdatamodel");
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top);
			//assert response
			assertTypes("v2", response);
			//empty filter
			filterMap.clear();

			//search cases
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int searchTermsIterator = 0; searchTermsIterator < searchValuesV2.length; searchTermsIterator++)
			{
				response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top,
						searchValuesV2[searchTermsIterator], "");
				assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV2, caseRefsCustomers.get(0),
						response);
			}
			//empty filter
			filterMap.clear();

			//search cases with isInTerminalState flag set to FALSE
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			filterMap.put("isInTerminalState", "FALSE");
			for (int searchTermsIterator = 0; searchTermsIterator < searchValuesV2.length; searchTermsIterator++)
			{
				response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top,
						searchValuesV2[searchTermsIterator], "");
				assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV2, caseRefsCustomers.get(0),
						response);
			}
			//empty filter
			filterMap.clear();

			//findBy(criteria) with big criteria having all the searchable attributes including id and state
			filterMap.put("caseType", caseTypeCustomer);
			filterMap.put("applicationMajorVersion", applicationMajorVersion);
			for (int dqlIterator = 0; dqlIterator < dqlValuesV2.length; dqlIterator++)
			{
				filterDql.put(searchTermsV2[dqlIterator], dqlValuesV2[dqlIterator]);
			}
			response = cases.getCasesDQL("DQL", filterMap, filterDql, SELECT_CASES, skip, top, "");
			assertCasedataSummaryReference(casedataCustomerV1, summaryCustomerV2, caseRefsCustomers.get(0), response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//navigateBy(criteria) using all values for individual searchable attributes 
			filterMap.put("name", "socially_mingle_with");
			for (int dqlIterator = 0; dqlIterator < dqlValuesV2.length; dqlIterator++)
			{
				filterDql.put(searchTermsV2[dqlIterator], dqlValuesV2[dqlIterator]);
			}
			response = linksFunction.getLinks(filterMap, filterDql, caseRefsCustomers.get(2), skip, top);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			assertLinkedCases(0, 1, 1, caseRefsCustomers, 0, "socially_mingle_with", response);
			//empty filter
			filterDql.clear();
			filterMap.clear();

			//delete all links
			response = linksFunction.deleteLinks(caseRefsCustomers.get(2), "socially_mingle_with", "NONE",
					new ArrayList<String>());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking response for number of links deleted");
		}
		finally
		{
			forceUndeploy(deploymentIdApp2);
			forceUndeploy(deploymentIdApp1);
		}
	}
}
