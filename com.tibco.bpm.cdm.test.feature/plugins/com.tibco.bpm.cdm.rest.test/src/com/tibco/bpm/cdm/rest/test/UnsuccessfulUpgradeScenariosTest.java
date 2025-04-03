package com.tibco.bpm.cdm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.CDMErrorData;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.AccountCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DatabaseQueriesFunction;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.test.util.FileUtils.DataModelModifier;
import com.tibco.bpm.da.dm.api.AllowedValue;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.Constraint;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.da.dm.api.State;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

public class UnsuccessfulUpgradeScenariosTest extends Utils
{

	AccountCreatorFunction	accountCreator		= new AccountCreatorFunction();

	CaseTypesFunctions		caseTypes			= new CaseTypesFunctions();

	CasesFunctions			cases				= new CasesFunctions();

	GetPropertiesFunction	properties			= new GetPropertiesFunction();

	DatabaseQueriesFunction	executeStmt			= new DatabaseQueriesFunction();

	int						validStatusCode		= 200;

	int						invalidStatusCode	= 400;

	int						notFoundStatusCode	= 404;

	private Response		response			= null;

	private JsonPath		jsonPath			= null;

	// Set deployment id. In the real system, DEM would populate this before passing the RASC to us
	BigInteger				deploymentIdApp1	= BigInteger.valueOf(7);

	BigInteger				deploymentIdApp2	= BigInteger.valueOf(8);

	private String			top					= "20";

	private void deployAccounts() throws IOException, URISyntaxException, RuntimeApplicationException
	{

		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(rascFile);

		//set deployment id. In the real system, DEM would populate this before passing the RASC to us
		runtimeApplication.setID(deploymentIdApp1);
		try
		{
			deploymentManager.deploy(runtimeApplication);
		}
		catch (DeploymentException | InternalException e1)
		{
			e1.printStackTrace();
		}
	}

	private void deployZip(File tempFile) throws IOException, URISyntaxException, RuntimeApplicationException
	{

		//File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);

		//set deployment id. In the real system, DEM would populate this before passing the RASC to us
		runtimeApplication.setID(deploymentIdApp1);
		try
		{
			deploymentManager.deploy(runtimeApplication);
		}
		catch (DeploymentException | InternalException e1)
		{
			e1.printStackTrace();
		}
	}

	private void undeployAccounts(Integer numberOfApplications) throws IOException, URISyntaxException,
			RuntimeApplicationException, DeploymentException, PersistenceException, InternalException
	{
		if (numberOfApplications == 2)
		{
			undeploy(deploymentIdApp2);
		}
		undeploy(deploymentIdApp1);
	}

	private File produceZip() throws IOException, URISyntaxException, RuntimeApplicationException
	{

		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);

		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		return tempFile;
	}

	private void modifyModelToUpgradeUnsuccessful(DataModelModifier modifier, String rascVersion)
			throws IOException, URISyntaxException, RuntimeApplicationException, DeploymentException,
			PersistenceException, InternalException, DataModelSerializationException, InterruptedException,
			ClassNotFoundException, SQLException
	{

		undeployAccounts(2);

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("namespace", "com.example.bankdatamodel");

		//deploy accounts
		deployAccounts();

		//get the zip file
		File tempFile = produceZip();

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), rascVersion);

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifier);

		// Check that old version has implicitly gone
		//assertTypeCountViaDatabase(deploymentId, 0);

		System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

		//deploy v2
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

		if ((!(rascVersion.equals("2.0.0.1"))) && (!(rascVersion.equals("2.0.1.0"))))
		{
			try
			{
				deploymentManager.deploy(runtimeApplication);
				//the test should never reach here
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database
				Assert.fail();
			}
			catch (DeploymentException | InternalException e)
			{
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database
				System.out.println("error in upgrade: " + e.getMessage());
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_DEPLOYMENT_INVALID_DATAMODEL_UPGRADE.getCode());
				//assert types
				response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", top);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging

				//assert that there are 6 structured types returned
				String repsonseJson = response.getBody().asString();
				TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 6, "incorrect number of types");
			}

			try
			{
				undeployAccounts(1);
			}
			catch (DeploymentException | InternalException e)
			{
				e.printStackTrace();
			}
		}

		else if (rascVersion.equals("2.0.0.1") || rascVersion.equals("2.0.1.0"))
		{
			try
			{
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				deploymentManager.deploy(runtimeApplication);
				//deployment should be successful as this is a new version of the application
				//assert types
				response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", top);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(executeStmt.getDateModel(host)); //model from the database

				//assert that there are 12 structured types returned
				String repsonseJson = response.getBody().asString();
				TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 12, "incorrect number of types");
			}
			catch (DeploymentException | InternalException e)
			{
				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_DEPLOYMENT_INVALID_DATAMODEL_UPGRADE.getCode());
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				Assert.fail();
			}

			try
			{
				undeployAccounts(2);
			}
			catch (DeploymentException | InternalException e)
			{
				e.printStackTrace();
			}
		}

		tempFile.deleteOnExit();
		deploymentManager.undeploy(deploymentIdApp2);
	}

	private void modifyV1ModelToUpgradeUnsuccessful(DataModelModifier modifierV1, DataModelModifier modifier,
			String rascVersion) throws IOException, URISyntaxException, RuntimeApplicationException,
			DeploymentException, PersistenceException, InternalException, DataModelSerializationException,
			InterruptedException, ClassNotFoundException, SQLException
	{

		undeployAccounts(2);

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("namespace", "com.example.bankdatamodel");

		//get the zip file
		File tempFile = produceZip();

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifierV1);

		//deploy accounts
		deployZip(tempFile);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), rascVersion);

		// Change the model content
		FileUtils.modifyDataModelInZip(tempFile.toPath(), "cm/com.example.bankdatamodel.dm", modifier);

		//deploy v2
		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));
		runtimeApplication.setID(deploymentIdApp2);

		if ((!(rascVersion.equals("2.0.0.1"))) && (!(rascVersion.equals("2.0.1.0"))))

		{
			try
			{
				deploymentManager.deploy(runtimeApplication);
				//the test should never reach here
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				Assert.fail();
			}
			catch (DeploymentException | InternalException e)
			{
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_DEPLOYMENT_INVALID_DATAMODEL_UPGRADE.getCode());
				//assert types
				response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", top);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging

				//assert that there are 6 structured types returned
				String repsonseJson = response.getBody().asString();
				TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 6, "incorrect number of types");
			}

			try
			{
				undeployAccounts(1);
			}
			catch (DeploymentException | InternalException e)
			{
				e.printStackTrace();
			}
		}

		else if (rascVersion.equals("2.0.0.1") || rascVersion.equals("2.0.1.0"))
		{
			try
			{
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				deploymentManager.deploy(runtimeApplication);
				//deployment should be successful as this is a new version of the application
				//assert types
				response = caseTypes.getTypes(filterMap, SELECT_CASE_TYPES_B, "0", top);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging

				//assert that there are 12 structured types returned
				String repsonseJson = response.getBody().asString();
				TypesGetResponseBody obj = om.readValue(repsonseJson, TypesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 12, "incorrect number of types");
			}
			catch (DeploymentException | InternalException e)
			{
				System.out.println("data model from database : " + executeStmt.getDateModel(host)); //model from the database

				Assert.assertEquals(e.getErrorData().getCode(),
						CDMErrorData.CDM_DEPLOYMENT_INVALID_DATAMODEL_UPGRADE.getCode());
				Assert.fail();
			}

			try
			{
				undeployAccounts(2);
			}
			catch (DeploymentException | InternalException e)
			{
				e.printStackTrace();
			}
		}

		tempFile.deleteOnExit();
		deploymentManager.undeploy(deploymentIdApp2);

	}

	//Test to deploy v1 and then upgrade to v2 with change the value of the existing state
	@Test(groups = {
			"states"}, description = "Test to deploy v1 and then upgrade to v2 with change the value of the existing state")
	public void upgradeInvalidStateChangeValueChange() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{

		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("INACTIVE");
			s.setValue("DORMANT");
			s.setIsTerminal(true);
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("INACTIVE");
			s.setValue("DORMANT");
			s.setIsTerminal(true);
		}, "2.0.0.1");

	}

	//Test to deploy v1 and then upgrade to v2 change the name of the existing non-terminal state
	@Test(groups = {
			"states"}, description = "Test to deploy v1 and then upgrade to v2 with change the name of the existing non-terminal state")
	public void upgradeInvalidStateChangeRemoveState() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("ACTIVEBUTONHOLD");
			//sm.removeState(s);
			s.setValue("Banana"); // Simon's recommendation
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("ACTIVEBUTONHOLD");
			//sm.removeState(s);
			s.setValue("Banana"); // Simon's recommendation
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 change the name of the existing non-terminal state
	@Test(groups = {
			"states"}, description = "Test to deploy v1 and then upgrade to v2 with change the name of the existing non-terminal state")
	public void upgradeInvalidStateChangeNonTerminalToTerminal() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("REGULAR1KTO10K");
			s.setIsTerminal(true);
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			State s = dm.getStructuredTypeByName("Customer").getStateModel().getStateByValue("REGULAR1KTO10K");
			s.setIsTerminal(true);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from date to time
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from date to time")
	public void upgradeInvalidAttributeChangeDateToTime() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Account").getAttributeByName("dateofAccountOpening");
			aDate.setType("base:Time");
			aDate.setDefaultValue("01:02:03");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Account").getAttributeByName("dateofAccountOpening");
			aDate.setType("base:Time");
			aDate.setDefaultValue("01:02:03");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from time to date
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from time to date")
	public void upgradeInvalidAttributeChangeTimeToDate() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Account").getAttributeByName("timeofAccountOpening");
			aTime.setType("base:Date");
			aTime.setDefaultValue("2019-01-01");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Account").getAttributeByName("timeofAccountOpening");
			aTime.setType("base:Date");
			aTime.setDefaultValue("2019-01-01");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from number to text
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from number to text")
	public void upgradeInvalidAttributeChangeNumberToText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("NumberAttribute");
			aNumber.setLabel("NumberAttribute");
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("NumberAttribute");
			aNumber.setType("base:Text");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("NumberAttribute");
			aNumber.setLabel("NumberAttribute");
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("NumberAttribute");
			aNumber.setType("base:Text");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from text to number
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from text to number")
	public void upgradeInvalidAttributeChangeTextToNumber() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("TextAttribute");
			aText.setLabel("TextAttribute");
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("TextAttribute");
			aText.setType("base:Number");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("TextAttribute");
			aText.setLabel("TextAttribute");
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("TextAttribute");
			aText.setType("base:Number");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from uri to text
	@Test(groups = {"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change uri to text")
	public void upgradeInvalidAttributeChangeURIToText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aUri = dm.getStructuredTypeByName("Customer").newAttribute();
			aUri.setType("base:URI");
			aUri.setName("UriAttribute");
			aUri.setLabel("UriAttribute");
		}, (dm) -> {
			Attribute aUri = dm.getStructuredTypeByName("Customer").getAttributeByName("UriAttribute");
			aUri.setType("base:Text");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aUri = dm.getStructuredTypeByName("Customer").newAttribute();
			aUri.setType("base:URI");
			aUri.setName("UriAttribute");
			aUri.setLabel("UriAttribute");
		}, (dm) -> {
			Attribute aUri = dm.getStructuredTypeByName("Customer").getAttributeByName("UriAttribute");
			aUri.setType("base:Text");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from boolean to text
	@Test(groups = {"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change boolean to text")
	public void upgradeInvalidAttributeChangeBooleanToText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("BooleanAttribute");
			aBoolean.setLabel("BooleanAttribute");
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("BooleanAttribute");
			aBoolean.setType("base:Text");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("BooleanAttribute");
			aBoolean.setLabel("BooleanAttribute");
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("BooleanAttribute");
			aBoolean.setType("base:Text");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from datetimeTZ to date
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change datetimeTZ to date")
	public void upgradeInvalidAttributeChangeDatetimeTZToDate() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("DatetimeTZAttribute");
			aDateTimeTZ.setLabel("DatetimeTZAttribute");
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").getAttributeByName("DatetimeTZAttribute");
			aDateTimeTZ.setType("base:Date");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("DatetimeTZAttribute");
			aDateTimeTZ.setLabel("DatetimeTZAttribute");
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").getAttributeByName("DatetimeTZAttribute");
			aDateTimeTZ.setType("base:Date");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from datetimeTZ to time
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change datetimeTZ to time")
	public void upgradeInvalidAttributeChangeDatetimeTZToTime() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("DatetimeTZAttribute");
			aDateTimeTZ.setLabel("DatetimeTZAttribute");
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").getAttributeByName("DatetimeTZAttribute");
			aDateTimeTZ.setType("base:Time");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("DatetimeTZAttribute");
			aDateTimeTZ.setLabel("DatetimeTZAttribute");
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").getAttributeByName("DatetimeTZAttribute");
			aDateTimeTZ.setType("base:Time");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with removal of an object type
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with removal of an object type")
	public void upgradeInvalidAttributeChangeObjectRemoval() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setType("base:Text");
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setType("base:Text");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array object type to non-array object 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array object type to non-array object")
	public void upgradeInvalidAttributeChangeObjectArrayToNonArray() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
			aObject.setIsArray(true);
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
			aObject.setIsArray(true);
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from non-array object type to array object 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from non-array object type to array object")
	public void upgradeInvalidAttributeChangeObjectNonArrayToArray() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
			aObject.setIsArray(false);
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").newAttribute();
			aObject.setType("PersonalDetails");
			aObject.setName("NonArrayObjectAttribute");
			aObject.setLabel("NonArrayObjectAttribute");
			aObject.setIsArray(false);
		}, (dm) -> {
			Attribute aObject = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayObjectAttribute");
			aObject.setIsArray(true);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with removal of allowed value 
	@Test(groups = {
			"allowedValues"}, description = "Test to deploy v1 and then upgrade to v2 with removal of allowed value")
	public void upgradeInvalidAllowedValueChangeAllowedValueRemoval() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aAllowedValue = dm.getStructuredTypeByName("Account").getAttributeByName("accountType");
			List<AllowedValue> aAllowedValues = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getAllowedValues();
			aAllowedValue.removeAllowedValue(aAllowedValues.get(0));
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aAllowedValue = dm.getStructuredTypeByName("Account").getAttributeByName("accountType");
			List<AllowedValue> aAllowedValues = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getAllowedValues();
			aAllowedValue.removeAllowedValue(aAllowedValues.get(0));
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change in one of the allowed values 
	@Test(groups = {
			"allowedValues"}, description = "Test to deploy v1 and then upgrade to v2 with change in one of the allowed values")
	public void upgradeInvalidAllowedValueChangeOneOfTheAllowedValues() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{

		modifyModelToUpgradeUnsuccessful((dm) -> {
			List<AllowedValue> aAllowedValues = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getAllowedValues();
			aAllowedValues.get(0).setValue("RANDOMCHANGE");
			;
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			List<AllowedValue> aAllowedValues = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getAllowedValues();
			aAllowedValues.get(0).setValue("RANDOMCHANGE");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array text type to non-array text and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array text type to non-array text and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("NonArrayTextAttribute");
			aText.setLabel("NonArrayTextAttribute");
			aText.setIsArray(false);
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayTextAttribute");
			aText.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("NonArrayTextAttribute");
			aText.setLabel("NonArrayTextAttribute");
			aText.setIsArray(false);
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayTextAttribute");
			aText.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("ArrayTextAttribute");
			aText.setLabel("ArrayTextAttribute");
			aText.setIsArray(true);
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayTextAttribute");
			aText.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").newAttribute();
			aText.setType("base:Text");
			aText.setName("ArrayTextAttribute");
			aText.setLabel("ArrayTextAttribute");
			aText.setIsArray(true);
		}, (dm) -> {
			Attribute aText = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayTextAttribute");
			aText.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array Number type to non-array Number and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array Number type to non-array Number and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayNumber() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("NonArrayNumberAttribute");
			aNumber.setLabel("NonArrayNumberAttribute");
			aNumber.setIsArray(false);
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayNumberAttribute");
			aNumber.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("NonArrayNumberAttribute");
			aNumber.setLabel("NonArrayNumberAttribute");
			aNumber.setIsArray(false);
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayNumberAttribute");
			aNumber.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("ArrayNumberAttribute");
			aNumber.setLabel("ArrayNumberAttribute");
			aNumber.setIsArray(true);
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayNumberAttribute");
			aNumber.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").newAttribute();
			aNumber.setType("base:Number");
			aNumber.setName("ArrayNumberAttribute");
			aNumber.setLabel("ArrayNumberAttribute");
			aNumber.setIsArray(true);
		}, (dm) -> {
			Attribute aNumber = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayNumberAttribute");
			aNumber.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array Date type to non-array Date and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array Date type to non-array Date and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayDate() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").newAttribute();
			aDate.setType("base:Date");
			aDate.setName("NonArrayDateAttribute");
			aDate.setLabel("NonArrayDateAttribute");
			aDate.setIsArray(false);
		}, (dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayDateAttribute");
			aDate.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").newAttribute();
			aDate.setType("base:Date");
			aDate.setName("NonArrayDateAttribute");
			aDate.setLabel("NonArrayDateAttribute");
			aDate.setIsArray(false);
		}, (dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayDateAttribute");
			aDate.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").newAttribute();
			aDate.setType("base:Date");
			aDate.setName("ArrayDateAttribute");
			aDate.setLabel("ArrayDateAttribute");
			aDate.setIsArray(true);
		}, (dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayDateAttribute");
			aDate.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").newAttribute();
			aDate.setType("base:Date");
			aDate.setName("ArrayDateAttribute");
			aDate.setLabel("ArrayDateAttribute");
			aDate.setIsArray(true);
		}, (dm) -> {
			Attribute aDate = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayDateAttribute");
			aDate.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array Time type to non-array Time and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array Time type to non-array Time and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayTime() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").newAttribute();
			aTime.setType("base:Time");
			aTime.setName("NonArrayTimeAttribute");
			aTime.setLabel("NonArrayTimeAttribute");
			aTime.setIsArray(false);
		}, (dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayTimeAttribute");
			aTime.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").newAttribute();
			aTime.setType("base:Time");
			aTime.setName("NonArrayTimeAttribute");
			aTime.setLabel("NonArrayTimeAttribute");
			aTime.setIsArray(false);
		}, (dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayTimeAttribute");
			aTime.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").newAttribute();
			aTime.setType("base:Time");
			aTime.setName("ArrayTimeAttribute");
			aTime.setLabel("ArrayTimeAttribute");
			aTime.setIsArray(true);
		}, (dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayTimeAttribute");
			aTime.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").newAttribute();
			aTime.setType("base:Time");
			aTime.setName("ArrayTimeAttribute");
			aTime.setLabel("ArrayTimeAttribute");
			aTime.setIsArray(true);
		}, (dm) -> {
			Attribute aTime = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayTimeAttribute");
			aTime.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array DateTimeTZ type to non-array DateTimeTZ and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array DateTimeTZ type to non-array DateTimeTZ and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayDateTimeTZ() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setLabel("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(false);
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer")
					.getAttributeByName("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setLabel("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(false);
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer")
					.getAttributeByName("NonArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setLabel("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(true);
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer")
					.getAttributeByName("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer").newAttribute();
			aDateTimeTZ.setType("base:DateTimeTZ");
			aDateTimeTZ.setName("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setLabel("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(true);
		}, (dm) -> {
			Attribute aDateTimeTZ = dm.getStructuredTypeByName("Customer")
					.getAttributeByName("ArrayDateTimeTZAttribute");
			aDateTimeTZ.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array Boolean type to non-array Boolean and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array Boolean type to non-array Boolean and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayBoolean() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("NonArrayBooleanAttribute");
			aBoolean.setLabel("NonArrayBooleanAttribute");
			aBoolean.setIsArray(false);
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayBooleanAttribute");
			aBoolean.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("NonArrayBooleanAttribute");
			aBoolean.setLabel("NonArrayBooleanAttribute");
			aBoolean.setIsArray(false);
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayBooleanAttribute");
			aBoolean.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("ArrayBooleanAttribute");
			aBoolean.setLabel("ArrayBooleanAttribute");
			aBoolean.setIsArray(true);
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayBooleanAttribute");
			aBoolean.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").newAttribute();
			aBoolean.setType("base:Boolean");
			aBoolean.setName("ArrayBooleanAttribute");
			aBoolean.setLabel("ArrayBooleanAttribute");
			aBoolean.setIsArray(true);
		}, (dm) -> {
			Attribute aBoolean = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayBooleanAttribute");
			aBoolean.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from array URI type to non-array URI and vice-versa 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from array URI type to non-array URI and vice-versa")
	public void upgradeInvalidAttributeChangeInterchangeArrayURI() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").newAttribute();
			aURI.setType("base:URI");
			aURI.setName("NonArrayURIAttribute");
			aURI.setLabel("NonArrayURIAttribute");
			aURI.setIsArray(false);
		}, (dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayURIAttribute");
			aURI.setIsArray(true);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").newAttribute();
			aURI.setType("base:URI");
			aURI.setName("NonArrayURIAttribute");
			aURI.setLabel("NonArrayURIAttribute");
			aURI.setIsArray(false);
		}, (dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").getAttributeByName("NonArrayURIAttribute");
			aURI.setIsArray(true);
		}, "2.0.0.1");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").newAttribute();
			aURI.setType("base:URI");
			aURI.setName("ArrayURIAttribute");
			aURI.setLabel("ArrayURIAttribute");
			aURI.setIsArray(true);
		}, (dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayURIAttribute");
			aURI.setIsArray(false);
		}, "1.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").newAttribute();
			aURI.setType("base:URI");
			aURI.setName("ArrayURIAttribute");
			aURI.setLabel("ArrayURIAttribute");
			aURI.setIsArray(true);
		}, (dm) -> {
			Attribute aURI = dm.getStructuredTypeByName("Customer").getAttributeByName("ArrayURIAttribute");
			aURI.setIsArray(false);
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with change from non-mandatory to mandatory 
	@Test(groups = {
			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with change from non-mandatory to mandatory")
	public void upgradeInvalidAttributeChangeNonMandatoryToMandatory() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account")
					.getAttributeByName("accountLiabilityHolding");
			aFixedPointNumber.setIsMandatory(true);
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account")
					.getAttributeByName("accountLiabilityHolding");
			aFixedPointNumber.setIsMandatory(true);
		}, "2.0.1.0");
	}

	//Setting isMandatory for a new attribute should not result in a successful upgrade - ACE-2656
	//TODO insterted by nashtapu [Aug 21, 2019 4:55:32 PM]
	//Once ACE-2656	is resolved	uncomment the following test
	//Test to deploy v1 and then upgrade to v2 with addition of a mandatory attribute 
	//	@Test(groups = {
	//			"attributes"}, description = "Test to deploy v1 and then upgrade to v2 with addition of a mandatory attribute")
	//
	//	public void upgradeInvalidMandatoryAttributeAddition() throws IOException, URISyntaxException,
	//			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
	//			InternalException, InterruptedException, ClassNotFoundException, SQLException
	//	{
	//		//attribute is mandatory, searchable and summary
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsMandatory(true);
	//			aFixedPointNumber.setIsSearchable(true);
	//			aFixedPointNumber.setIsSummary(true);
	//		}, "1.0.1.0");
	//
	//		//attribute is mandatory and has a default value
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsMandatory(true);
	//			aFixedPointNumber.setDefaultValue("11.11");
	//		}, "1.0.1.0");
	//
	//		//attribute is mandatory and array
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsArray(true);
	//			aFixedPointNumber.setIsMandatory(true);
	//		}, "1.0.1.0");
	//
	//		//attribute is of a structured type
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aProfessionalDetailsType = dm.getStructuredTypeByName("Account").newAttribute();
	//			aProfessionalDetailsType.setType("ProfessionalDetails");
	//			aProfessionalDetailsType.setName("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setLabel("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setIsMandatory(true);
	//		}, "1.0.1.0");
	//
	//		//attribute is of a structured type array
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aProfessionalDetailsType = dm.getStructuredTypeByName("Account").newAttribute();
	//			aProfessionalDetailsType.setType("ProfessionalDetails");
	//			aProfessionalDetailsType.setName("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setLabel("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setIsMandatory(true);
	//			aProfessionalDetailsType.setIsArray(true);
	//		}, "1.0.1.0");
	//
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsMandatory(true);
	//			aFixedPointNumber.setIsSearchable(true);
	//			aFixedPointNumber.setIsSummary(true);
	//		}, "2.0.1.0");
	//
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsMandatory(true);
	//			aFixedPointNumber.setDefaultValue("11.11");
	//		}, "2.0.1.0");
	//
	//		//attribute is mandatory and array
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aFixedPointNumber = dm.getStructuredTypeByName("Account").newAttribute();
	//			aFixedPointNumber.setType("base:FixedPointNumber");
	//			aFixedPointNumber.setName("aFixedPointNumber");
	//			aFixedPointNumber.setLabel("aFixedPointNumber");
	//			aFixedPointNumber.setIsArray(true);
	//			aFixedPointNumber.setIsMandatory(true);
	//		}, "2.0.1.0");
	//
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aProfessionalDetailsType = dm.getStructuredTypeByName("Account").newAttribute();
	//			aProfessionalDetailsType.setType("ProfessionalDetails");
	//			aProfessionalDetailsType.setName("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setLabel("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setIsMandatory(true);
	//		}, "2.0.1.0");
	//
	//		modifyModelToUpgradeUnsuccessful((dm) -> {
	//			Attribute aProfessionalDetailsType = dm.getStructuredTypeByName("Account").newAttribute();
	//			aProfessionalDetailsType.setType("ProfessionalDetails");
	//			aProfessionalDetailsType.setName("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setLabel("ProfessionalDetailsType");
	//			aProfessionalDetailsType.setIsMandatory(true);
	//			aProfessionalDetailsType.setIsArray(true);
	//		}, "2.0.1.0");
	//	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of identifier of type number
	@Test(groups = {
			"identifier_constraints"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of identifier")
	public void upgradeInvalidConstratintsChangeIdentifier() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("minValue", "100");
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("minValueInclusive", "true");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("maxValue", "2000000");
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("maxValueInclusive", "false");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("minValue", "100");
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("minValueInclusive", "true");
		}, "2.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("maxValue", "2000000");
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().newConstraint("maxValueInclusive", "false");
		}, "2.0.1.0");
	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of identifier text - length
	@Test(groups = {
			"identifier_constraints"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of identifier text - length")
	public void upgradeInvalidConstratintsChangeIdentifierText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Customer").getIdentifierAttribute().newConstraint("length", "6");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Customer").getIdentifierAttribute().newConstraint("length", "6");
		}, "2.0.1.0");

		//Changing is mandatory to FALSE is not an upgrade test, since the model is invalid
		//CDM_DEPLOYMENT_INVALID_DATAMODEL
		//modifyModelToUpgradeUnsuccessful((dm) -> {
		//dm.getStructuredTypeByName("Customer").getIdentifierAttribute().setIsMandatory(false);
		//}, "1.0.1.0");
	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of identifier of type number auto-identifier - type change
	@Test(groups = {
			"identifier_type"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of auto identifier - type change")
	public void upgradeInvalidConstratintsChangeIdentifierAuto() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, ClassNotFoundException, SQLException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Customer").removeIdentifierInitialisationInfo();
			dm.getStructuredTypeByName("Customer").getIdentifierAttribute().setType("base:Number");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Customer").removeIdentifierInitialisationInfo();
			dm.getStructuredTypeByName("Customer").getIdentifierAttribute().setType("base:Number");
		}, "2.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").newIdentifierInitialisationInfo();
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().setType("base:Text");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").newIdentifierInitialisationInfo();
			dm.getStructuredTypeByName("Account").getIdentifierAttribute().setType("base:Text");
		}, "2.0.1.0");
	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of fixed point number constraints
	@Test(groups = {
			"constraints"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of fixed point number constraints")
	public void upgradeInvalidConstratintsChangeFixedPointNumber() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, SQLException, ClassNotFoundException
	{
		//reduction in length
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("length");
			c.setValue("13");
		}, "1.0.1.0");

		//max value inclusive flag changed to false
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive");
			c.setValue("false");
		}, "1.0.1.0");

		//decrease in max value 
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValue");
			c.setValue("999999999.98");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive");
			c.setValue("true");
		}, "1.0.1.0");

		//increase in min value
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue");
			c.setValue("-999999999999.98");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("true");
		}, "1.0.1.0");

		//min value inclusive flag changed to false (from true)
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("true");
		}, (dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("false");
		}, "1.0.1.0");

		//reduction in decimal places
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding").setDefaultValue("9999");
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue");
			c.setValue("-999999999999");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValue");
			c.setValue("999999999999");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("decimalPlaces");
			c.setValue("2");
		}, (dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("decimalPlaces");
			c.setValue("1");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("length");
			c.setValue("13");
		}, "2.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValue");
			c.setValue("999999999.98");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive");
			c.setValue("true");
		}, "2.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue");
			c.setValue("-999999999999.98");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("true");
		}, "2.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding").setDefaultValue("9999");
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValue");
			c.setValue("-999999999999");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValue");
			c.setValue("999999999999");
			c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("decimalPlaces");
			c.setValue("2");
		}, (dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("decimalPlaces");
			c.setValue("1");
		}, "2.0.1.0");

		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("true");
		}, (dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("minValueInclusive");
			c.setValue("false");
		}, "2.0.1.0");

		//max value inclusive flag changed to false
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountLiabilityHolding")
					.getConstraint("maxValueInclusive");
			c.setValue("false");
		}, "2.0.1.0");
	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of numbers inclusive values and exclusive values
	@Test(groups = {
			"constraints"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of numbers inclusive values and exclusive values")
	public void upgradeInvalidConstratintsInclusiveAndExclusiveValues() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, SQLException, ClassNotFoundException
	{
		//values are same but one of the max inclusive factor has changed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "1.0.1.0");

		//values are same but one of the max inclusive factor has changed with negative values
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "-2");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "-500");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("-2");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("-500");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "1.0.1.0");

		//values are same but one of the min inclusive flag has changed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("false");
		}, "1.0.1.0");

		//values are same but the inclusive factors are reversed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("false");
		}, "1.0.1.0");

		//flags are same but the value range has reduced
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("199.99999999999");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5.000000000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "1.0.1.0");

		//flags are same but the value range has reduced - increase in min value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5.000000000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "1.0.1.0");

		//flags are same but the value range has reduced - increase in min value - negative value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "-5");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "-200");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("-199.000000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "1.0.1.0");

		//flags are same but the value range has reduced - decrease in max value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("199.99999999999");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
		}, "1.0.1.0");

		//values are same but one of the max inclusive factor has changed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "2.0.0.1");

		//values are same but one of the min inclusive flag has changed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("false");
		}, "2.0.0.1");

		//values are same but the inclusive factors are reversed
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("200");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("false");
		}, "2.0.0.1");

		//flags are same but the value range has reduced
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("199.99999999");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5.00000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "2.0.0.1");

		//flags are same but the value range has reduced - increase in min value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("5.00000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "2.0.0.1");

		//flags are same but the value range has reduced - decrease in max value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "200");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "5");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("199.99999999");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("true");
		}, "2.0.0.1");

		//values are same but one of the max inclusive factor has changed with negative values
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "-2");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "-500");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMax = aNewNumber.getConstraint("maxValue");
			cNewNumberMax.setValue("-2");
			Constraint cNewNumberMaxInc = aNewNumber.getConstraint("maxValueInclusive");
			cNewNumberMaxInc.setValue("false");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("-500");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "2.0.0.1");

		//flags are same but the value range has reduced - increase in min value
		modifyV1ModelToUpgradeUnsuccessful((dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").newAttribute();
			aNewNumber.setType("base:Number");
			aNewNumber.setName("NewNumber");
			aNewNumber.setLabel("NewNumber");
			aNewNumber.newConstraint("maxValue", "-5");
			aNewNumber.newConstraint("maxValueInclusive", "true");
			aNewNumber.newConstraint("minValue", "-200");
			aNewNumber.newConstraint("minValueInclusive", "true");
		}, (dm) -> {
			Attribute aNewNumber = dm.getStructuredTypeByName("Account").getAttributeByName("NewNumber");
			Constraint cNewNumberMin = aNewNumber.getConstraint("minValue");
			cNewNumberMin.setValue("-199.000000001");
			Constraint cNewNumberMinInc = aNewNumber.getConstraint("minValueInclusive");
			cNewNumberMinInc.setValue("true");
		}, "2.0.0.1");
	}

	//Test to deploy v1 and then upgrade to v2 with addition of constraints of text
	@Test(groups = {
			"constraints"}, description = "Test to deploy v1 and then upgrade to v2 with addition of constraints of text")
	public void upgradeInvalidConstratintsChangeText() throws IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, DeploymentException, PersistenceException,
			InternalException, InterruptedException, SQLException, ClassNotFoundException
	{
		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint cFirstName = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("firstName")
					.getConstraint("length");
			cFirstName.setValue("49");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getConstraint("length");
			c.setValue("399");
		}, "1.0.1.0");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint cFirstName = dm.getStructuredTypeByName("PersonalDetails").getAttributeByName("firstName")
					.getConstraint("length");
			cFirstName.setValue("49");
		}, "2.0.0.1");

		modifyModelToUpgradeUnsuccessful((dm) -> {
			Constraint c = dm.getStructuredTypeByName("Account").getAttributeByName("accountType")
					.getConstraint("length");
			c.setValue("399");
		}, "2.0.0.1");
	}

	//ACE-1573
	//Test to deploy v1 and then upgrade to v2 with alterations of links
	@Test(groups = {"links"}, description = "Test to deploy v1 and then upgrade to v2 with alterations of links")
	public void upgradeInvalidAlteringLinks() throws IOException, URISyntaxException, RuntimeApplicationException,
			DataModelSerializationException, DeploymentException, PersistenceException, InternalException,
			InterruptedException, SQLException, ClassNotFoundException
	{
		//revrese the ends - should it be allowed?
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setOwner("Account");
			dm.getLinks().get(0).getEnd1().setName("heldby");
			dm.getLinks().get(0).getEnd1().setLabel("Held By the");
			dm.getLinks().get(0).getEnd2().setOwner("Customer");
			dm.getLinks().get(0).getEnd2().setName("holdstheaccounts");
			dm.getLinks().get(0).getEnd2().setLabel("Holds The Accounts");
			dm.getLinks().get(0).getEnd2().setIsArray(true);
		}, "1.0.1.0");

		//owner was Customer, changed it to Account
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setOwner("Account");
		}, "1.0.1.0");

		//name of the link was holdstheaccounts, changed it to "holdtheaccount"
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setName("holdtheaccount");
		}, "1.0.1.0");

		//isArray was true, changed it to Non-Array
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setIsArray(false);
		}, "1.0.1.0");

		//removal of link
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().remove(0);
		}, "1.0.1.0");

		//revrese the ends
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setOwner("Account");
			dm.getLinks().get(0).getEnd1().setName("heldby");
			dm.getLinks().get(0).getEnd1().setLabel("Held By the");
			dm.getLinks().get(0).getEnd2().setOwner("Customer");
			dm.getLinks().get(0).getEnd2().setName("holdstheaccounts");
			dm.getLinks().get(0).getEnd2().setLabel("Holds The Accounts");
			dm.getLinks().get(0).getEnd2().setIsArray(true);
		}, "2.0.1.0");

		//owner was Customer, changed it to Account
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setOwner("Account");
		}, "2.0.1.0");

		//name of the link was holdstheaccounts, changed it to "holdtheaccount"
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setName("holdtheaccount");
		}, "2.0.1.0");

		//isArray was true, changed it to Non-Array
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().get(0).getEnd1().setIsArray(false);
		}, "2.0.1.0");

		//removal of link
		modifyModelToUpgradeUnsuccessful((dm) -> {
			dm.getLinks().remove(0);
		}, "2.0.1.0");
	}
}