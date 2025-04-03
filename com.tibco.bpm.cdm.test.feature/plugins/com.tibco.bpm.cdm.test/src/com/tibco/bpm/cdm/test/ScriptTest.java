package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.exception.UserApplicationError;
import com.tibco.bpm.cdm.api.exception.CaseOutOfSyncError;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.NonUniqueCaseIdentifierError;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.se.api.DataType;
import com.tibco.bpm.se.api.Expression;
import com.tibco.bpm.se.api.Scope;
import com.tibco.bpm.se.api.ScriptEngineService;
import com.tibco.bpm.se.api.ScriptManager;
import com.tibco.bpm.se.api.Variable;

public class ScriptTest extends BaseTest
{

	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	@Autowired
	@Qualifier("scriptEngineService")
	private ScriptEngineService	scriptEngine;

	public static String readFileContents(String fileLocation)
	{

		InputStream inputStream = ScriptTest.class.getClassLoader().getResourceAsStream(fileLocation);
		Scanner scanner = new Scanner(inputStream);
		String lineSeparator = System.getProperty("line.separator");

		StringBuilder s = new StringBuilder();

		while (scanner.hasNextLine())
		{

			s.append(scanner.nextLine());
			s.append(lineSeparator);

		}

		scanner.close();

		return s.toString();

	}

	@Test
	public void testDeployAndExecuteScripts()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{

		BigInteger deploymentId = deployRASC(
				"/apps/datatype-tests/AceDataTypesTest-BOM/Exports/Deployment Artifacts/AceDataTypesTest-BOM.rasc");
		;

		String variableDescriptor = readFileContents(
				"apps/datatype-tests/AceDataTypesTest/Exports/Deployment Artifacts/process-js/AceDataTypesTestProcess.js");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("booleanParameter", true);
		data.put("dateParameter", "2019-04-24");
		data.put("dateTimeParameter", "2019-04-24T10:11:35+02:00");
		data.put("numberDecimalParameter", 0.1264);
		data.put("numberIntegerParameter", 9999999);
		data.put("performerParameter", "resource1");
		data.put("timeParameter", "12:08:50");
		data.put("complexSingleParameter", "{\"parentAttr1\":\"parentAttr\"}");
		data.put("uriParameter", "www.google.com");
		data.put("textArrayField", Arrays.asList("a", "b"));

		try
		{

			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.tibco.ace.acedatatypestest.bom", 1l);

			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			long t1 = System.currentTimeMillis();
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
			scope.setTempValue(new Variable("Integer_var", DataType.INTEGER, null, false), "10.22");
			scope.setTempValue(new Variable("Integer_Array_var", DataType.INTEGER, null, true), "[10.22, 11]");

			long t2 = System.currentTimeMillis();

			System.out.println("Time taken to create scope with scripts" + (t2 - t1));

			// @formatter:off
			Expression expression = ScriptManager
					.createExpression(""
							+"Log.write('---- Text Array length is '+data.textArrayField.length);"
							+"data.booleanParameter=false;"
							+"Log.write('---- Full year is '+data.dateParameter.getFullYear());"
							+"Log.write('--parseInt is--'+parseInt(data.numberIntegerParameter));"
							+"Log.write('---- Datetime value is '+data.dateTimeParameter.toISOString());"
							+"Log.write('---- Minutes in timeParameter is '+data.timeParameter.getMinutes());"
							+"var child1 = factory.com_tibco_ace_datatypes.createComplexChild();"
							+"child1.childAttr1='child 1';"
							+"data.complexSingleParameter.child = child1;"
							+"data.complexSingleParameter.children.push(child1);"
							+"var simpleSingle = factory.com_tibco_ace_datatypes.createSimpleSingle();"
							+"simpleSingle.booleanAttr = true;"
							+"simpleSingle.dateTimeTZAttr = new Date('2020-06-05T11:04:04Z');"
							+"data.textParameter=simpleSingle.dateTimeTZAttr;" //implicit type conversion from DateTimeTZ to Text
							+"simpleSingle.textAttr = data.dateParameter;" //implicit type conversion from Date to Text
							+"simpleSingle.numberDecimalAttr = data.numberDecimalParameter;"
							+"var c = 0.1;"
							+"simpleSingle.numberDecimalAttr = c.add(0.2);" //additional functions to get around javascript floating point issues
							+"Log.write('floating point sum of 0.1 and 0.2 is '+ (0.1+0.2)+ ' once you use add function it becomes '+simpleSingle.numberDecimalAttr);"
							+"simpleSingle.dateAttr = data.dateParameter;"
							+"simpleSingle.numberIntegerAttr = data.numberIntegerParameter+100;"
							+"simpleSingle.timeAttr = new Date('11:08:50');"
							+"simpleSingle.booleanAttr = true;"
							+"simpleSingle.textEnumAttr = pkg.com_tibco_ace_datatypes.Enumeration.ENUMLIT1;"						
							+ "data.simpleSingleAttributesParameter = simpleSingle;"
							+"var parent = factory.com_tibco_ace_datatypes.createComplexParent();"
							+"parent.children.push(child1);"
							+"child1.childAttr1='updated child 1';" //within script, objects are referenced
							+"data.numberDecimalParameter = data.numberDecimalParameter.add(0.1);"	//implicit rounding	(0.2264 becomes 0.23)					
							+"data.textArrayField.push('text1');"
							+"data.textArrayField.push('text2');"
							+"data.numberIntegerArrayField.push(1);"
							+"data.numberIntegerArrayField.push(2);"
							+"data.dateTimeArrayField.push(new Date('2019-06-05T11:04:04Z'));"
							+"data.dateTimeArrayField.push(new Date('2020-06-05T11:04:04Z'));"
							);
			// @formatter:on

			t1 = System.currentTimeMillis();
			Object val = expression.eval(scope);
			t2 = System.currentTimeMillis();
			System.out.println("Time taken to do eval for first time" + (t2 - t1));

			Assert.assertEquals(scope.getTempValue("Integer_var"), "10");
			Assert.assertEquals(scope.getTempValue("Integer_Array_var"), "[10,11]");

			Object a = scope.getValue("a");
			System.out.println(a);
			Map<String, Object> result = scope.getData();

			System.out.println("=========================================================");
			System.out.println("booleanParameter is: " + result.get("booleanParameter"));
			System.out.println("dateParameter is: " + result.get("dateParameter"));
			System.out.println("dateTimeParameter is: " + result.get("dateTimeParameter"));
			System.out.println("numberDecimalParameter is: " + result.get("numberDecimalParameter"));
			System.out.println("numberIntegerParameter is: " + result.get("numberIntegerParameter"));
			System.out.println("performerParameter is: " + result.get("performerParameter"));
			System.out.println("timeParameter is: " + result.get("timeParameter"));
			System.out.println("uriParameter is: " + result.get("uriParameter"));

			String complexSingleParameter = (String) result.get("complexSingleParameter");
			System.out.println("complexSingleParameter is " + complexSingleParameter);

			String simpleSingleAttributesParameter = (String) result.get("simpleSingleAttributesParameter");
			System.out.println("simpleSingleAttributesParameter is " + simpleSingleAttributesParameter);
			System.out.println("=========================================================");

			Assert.assertEquals(result.get("booleanParameter"), false);
			Assert.assertEquals(result.get("dateParameter"), "2019-04-24");
			Assert.assertEquals(result.get("dateTimeParameter"), "2019-04-24T08:11:35.000Z");
			Assert.assertEquals(result.get("textParameter"), "2020-06-05T11:04:04.000Z");
			Assert.assertEquals(result.get("numberDecimalParameter"), 0.23);
			Assert.assertEquals(result.get("numberIntegerParameter"), 9999999);
			Assert.assertEquals(result.get("performerParameter"), "resource1");
			Assert.assertEquals(result.get("timeParameter"), "12:08:50");
			Assert.assertEquals(result.get("numberIntegerArrayField"), "[1,2]");
			Assert.assertEquals(result.get("dateTimeArrayField"),
					"[\"2019-06-05T11:04:04.000Z\",\"2020-06-05T11:04:04.000Z\"]");

			Assert.assertEquals(complexSingleParameter,
					"{\"parentAttr1\":\"parentAttr\",\"child\":{\"childAttr1\":\"updated child 1\"},\"children\":[{\"childAttr1\":\"updated child 1\"}]}");
			Assert.assertEquals(simpleSingleAttributesParameter,
					"{\"booleanAttr\":true,\"dateAttr\":\"2019-04-24\",\"dateTimeTZAttr\":\"2020-06-05T11:04:04.000Z\",\"numberDecimalAttr\":0.3,\"numberIntegerAttr\":10000099,\"textAttr\":\"2019-04-24\",\"textEnumAttr\":\"ENUMLIT1\",\"timeAttr\":\"11:08:50\"}");

			t1 = System.currentTimeMillis();

			//clearAllVariableState re-uses bound scripts 
			scope.clearAllVariableState();
			t2 = System.currentTimeMillis();
			System.out.println("Time taken to clear All variables" + (t2 - t1));

			t1 = System.currentTimeMillis();
			scope.setData(data, "com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
			val = expression.eval(scope);

			t2 = System.currentTimeMillis();

			System.out.println("Time taken to do eval for second time" + (t2 - t1));

			System.out.println("=========================================================");
			System.out.println("booleanParameter is: " + result.get("booleanParameter"));
			System.out.println("dateParameter is: " + result.get("dateParameter"));
			System.out.println("dateTimeParameter is: " + result.get("dateTimeParameter"));
			System.out.println("numberDecimalParameter is: " + result.get("numberDecimalParameter"));
			System.out.println("numberIntegerParameter is: " + result.get("numberIntegerParameter"));
			System.out.println("performerParameter is: " + result.get("performerParameter"));
			System.out.println("timeParameter is: " + result.get("timeParameter"));
			System.out.println("uriParameter is: " + result.get("uriParameter"));

			complexSingleParameter = (String) result.get("complexSingleParameter");
			System.out.println("complexSingleParameter is " + complexSingleParameter);

			simpleSingleAttributesParameter = (String) result.get("simpleSingleAttributesParameter");
			System.out.println("simpleSingleAttributesParameter is " + simpleSingleAttributesParameter);
			System.out.println("=========================================================");

			Assert.assertEquals(result.get("booleanParameter"), false);
			Assert.assertEquals(result.get("dateParameter"), "2019-04-24");
			Assert.assertEquals(result.get("dateTimeParameter"), "2019-04-24T08:11:35.000Z");
			Assert.assertEquals(result.get("textParameter"), "2020-06-05T11:04:04.000Z");
			Assert.assertEquals(result.get("numberDecimalParameter"), 0.23);
			Assert.assertEquals(result.get("numberIntegerParameter"), 9999999);
			Assert.assertEquals(result.get("performerParameter"), "resource1");
			Assert.assertEquals(result.get("timeParameter"), "12:08:50");
			Assert.assertEquals(complexSingleParameter,
					"{\"parentAttr1\":\"parentAttr\",\"child\":{\"childAttr1\":\"updated child 1\"},\"children\":[{\"childAttr1\":\"updated child 1\"}]}");
			Assert.assertEquals(simpleSingleAttributesParameter,
					"{\"booleanAttr\":true,\"dateAttr\":\"2019-04-24\",\"dateTimeTZAttr\":\"2020-06-05T11:04:04.000Z\",\"numberDecimalAttr\":0.3,\"numberIntegerAttr\":10000099,\"textAttr\":\"2019-04-24\",\"textEnumAttr\":\"ENUMLIT1\",\"timeAttr\":\"11:08:50\"}");

			scope.clearAllVariableState();

			data = new HashMap<String, Object>();
			data.put("simpleSingleAttributesParameter",
					"{\"booleanAttr\":true,\"dateAttr\":\"2019-04-24\",\"dateTimeTZAttr\":\"2020-06-05T11:04:04.000+01:00\",\"numberDecimalAttr\":0.3,\"numberIntegerAttr\":10000099,\"textAttr\":\"2019-04-24\",\"textEnumAttr\":\"ENUMLIT1\",\"timeAttr\":\"11:08:50\"}");
			scope.setData(data, "com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
			// @formatter:off
			expression = ScriptManager
					.createExpression("print(data.simpleSingleAttributesParameter.dateTimeTZAttr.getFullYear());");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//If a script touches the datetimeTZ value, it gets normalized to UTC
			simpleSingleAttributesParameter = (String) result.get("simpleSingleAttributesParameter");
			Assert.assertEquals(simpleSingleAttributesParameter,
					"{\"booleanAttr\":true,\"dateAttr\":\"2019-04-24\",\"dateTimeTZAttr\":\"2020-06-05T10:04:04.000Z\",\"numberDecimalAttr\":0.3,\"numberIntegerAttr\":10000099,\"textAttr\":\"2019-04-24\",\"textEnumAttr\":\"ENUMLIT1\",\"timeAttr\":\"11:08:50\"}");

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testCaseDataScripts()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{

		BigInteger deploymentId = deployRASC(
				"/apps/CarApps/CarApplication/Exports/Deployment Artifacts/CarApplication.rasc");
		;

		String variableDescriptor = readFileContents(
				"apps/CarApps/CarProcess/Exports/Deployment Artifacts/CarProcessProcess.js");

		try
		{

			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.carapplication", 1l);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("carRef", "hello");

			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);

			long t1 = System.currentTimeMillis();
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.carprocess.CarProcess.CarProcessProcess");
			long t2 = System.currentTimeMillis();

			// @formatter:off
			Expression expression = ScriptManager
					.createExpression(""
							+ "var account = factory.com_example_account.createAccount();" 
							+ "account.name = 'Natwest';" 
							+ "account.accountState = pkg.com_example_account.AccountStates.ACTIVE;" 
							+ "var address = factory.com_example_carapplication.createAddress();" 
							+ "address.firstLine = \"first line\";" 
							+ "account.address = address;" 
							+ "data.account = account;" 
							+"data.accountRef = bpm.caseData.create(data.account,'com.example.account.Account');"
							);
			// @formatter:on

			expression.eval(scope);
			Map<String, Object> result = scope.getData();
			Assert.assertNotNull(result.get("accountRef"), "account not created");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"var car = factory.com_example_carapplication.createCar();" 
							+ "car.state = pkg.com_example_carapplication.States.DRIVING;" 
							+ "car.model = 'Astra';" 
							+ "car.numberOfDoors = 4;" 
							+ "car.make = 'Vauxhall';"
							+"data.carRef = bpm.caseData.create(car,'com.example.carapplication.Car');"
							);
			// @formatter:on

			t1 = System.currentTimeMillis();
			expression.eval(scope);
			t2 = System.currentTimeMillis();

			System.out.println("=========================================================");
			Assert.assertNotNull(result.get("carRef"), "car not created");
			String originalRef = (String) result.get("carRef");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.car = bpm.caseData.read(data.carRef);"
							+"Log.write('car registation is'+data.car.registration);"
							+ "data.carRef = bpm.caseData.updateByRef(data.carRef,data.car);"
							+ "bpm.caseData.deleteByRef(data.carRef);"
							+ ""
							);
			// @formatter:on

			t1 = System.currentTimeMillis();

			expression.eval(scope);

			result = scope.getData();
			String updatedRef = (String) result.get("carRef");

			System.out.println("=========================================================");
			System.out.println("car is: " + result.get("car"));
			System.out.println("updated carReference is: " + updatedRef);
			Assert.assertNotSame(originalRef, updatedRef, "car ref is expected to be updated");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"var array = new Array();"
							+"var car1 = factory.com_example_carapplication.createCar();" 
							+ "car1.state = pkg.com_example_carapplication.States.DRIVING;" 
							+ "car1.model = 'Astra';" 
							+ "car1.numberOfDoors = 4;" 
							+ "car1.make = 'Vauxhall';"							
							+"var car2 = factory.com_example_carapplication.createCar();" 
							+ "car2.state = pkg.com_example_carapplication.States.DRIVING;" 
							+ "car2.model = 'Prius';" 
							+ "car2.numberOfDoors = 4;" 
							+ "car2.make = 'Toyota';"	
							+"array.push(car1);"
							+"array.push(car2);"
							+"print('dataArray: ' + bpm.scriptUtil.stringify(array));"							
							+"data.carRefs.pushAll(bpm.caseData.createAll(array,'com.example.carapplication.Car'));"
							+"print('cases are'+bpm.scriptUtil.stringify(bpm.caseData.readAll(data.carRefs)));"
							+"data.cars.pushAll(bpm.caseData.readAll(data.carRefs));"
							+"data.cars[1].make='Lexus';"
							+"bpm.caseData.updateAllByRef(data.carRefs,data.cars);"
							+"Log.write('rsfArray is '+data.carRefs);"
							+"data.carRef = data.carRefs[0];"
							+"bpm.caseData.read(data.carRef);"
							+"Log.write('ref length is '+data.carRef.length);"
							+"data.carRefs.length = 0;" //removeAll
							+"data.carRefs.pushAll(bpm.caseData.findAll('com.example.carapplication.Car',0,100));" //re-populate
							+"var refsLength = data.carRefs.length;"
							+ ""
							);
			// @formatter:on

			expression.eval(scope);
			result = scope.getData();
			Assert.assertEquals(scope.getValue("refsLength"), 2, "Array length doesn't match");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+ "var ref = bpm.caseData.findByCaseIdentifier(data.cars[1].registration,'com.example.carapplication.Car');"
							+"data.car = bpm.caseData.read(ref);"
							+"var foundCar = data.cars[1];"
							+ ""
							);
			// @formatter:on

			expression.eval(scope);
			result = scope.getData();

			System.out.println(result.get("cars"));
			System.out.println(result.get("car"));
			String object = (String) scope.getValue("foundCar");

			Assert.assertEquals(result.get("car"), object.replaceAll("\\s+", ""), "expecting found car to match");

			Assert.assertTrue((result.get("carRefs") instanceof String));
			Assert.assertTrue((result.get("car") instanceof String));
			Assert.assertTrue((result.get("carRef") instanceof String));

			// @formatter:off
						expression = ScriptManager
								.createExpression(""
										+ "var ref = bpm.caseData.findByCompositeIdentifier(['3','Lexus', 4],'com.example.carapplication.Car');"
										+"data.car = bpm.caseData.read(ref);"
										+"var foundCar = data.cars[1];"
										+ ""
										);
						
			expression.eval(scope);
			result = scope.getData();
	
			System.out.println(result.get("cars"));
			System.out.println(result.get("car"));
			object = (String) scope.getValue("foundCar");
	
			Assert.assertEquals(result.get("car"), object.replaceAll("\\s+", ""), "expecting found car to match");
	
			Assert.assertTrue((result.get("carRefs") instanceof String));
			Assert.assertTrue((result.get("car") instanceof String));
			Assert.assertTrue((result.get("carRef") instanceof String));
			
			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.carRefs.length=0;"
							+"data.carRefs.pushAll(bpm.caseData.findBySimpleSearch(pkg.com_example_carapplication.States.DRIVING,'com.example.carapplication.Car',0,100));"
							+"var refsLength = data.carRefs.length;"
							+ ""
							);
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			System.out.println("Ref Array is" + result.get("carRefs"));
			Assert.assertEquals(scope.getValue("refsLength"), 2, "Array length doesn't match");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.carRefs.length=0;"
							+"data.carRefs.pushAll(bpm.caseData.findBySimpleSearch('astra','com.example.carapplication.Car',0,100));"
							+"var refsLength = data.carRefs.length;"
							+ ""
							);
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			System.out.println("Ref Array is" + result.get("carRefs"));
			Assert.assertEquals(scope.getValue("refsLength"), 1, "Array length doesn't match");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.carRefs.length=0;"
							+"data.carRefs.pushAll(bpm.caseData.findBySimpleSearch(pkg.com_example_carapplication.States.DRIVING,'com.example.carapplication.Car',0,100));"
							+ ""
							);
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			System.out.println("Ref Array is" + result.get("carRefs"));
			Assert.assertEquals(scope.getValue("refsLength"), 1, "Array length doesn't match");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.carRefs.length=0;"
							+"data.carRefs.pushAll(bpm.caseData.findByCriteria(\"model='Astra'\",'com.example.carapplication.Car',0,100));"
							+ ""
							);
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			System.out.println("Ref Array is" + result.get("carRefs"));
			Assert.assertEquals(scope.getValue("refsLength"), 1, "Array length doesn't match");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"var customer = factory.com_example_carapplication.createCustomer();" 
							+ "customer.state = pkg.com_example_carapplication.CustomerStates.ACTIVE;" 
							+ "customer.customerID = '101';" 
							+ "customer.firstName = 'Fred';" 
							+ "customer.age = 30;"
							+"data.customerRef = bpm.caseData.create(customer,'com.example.carapplication.Customer');"
							+"bpm.caseData.create(customer,'com.example.carapplication.Customer');"
							);
			// @formatter:on

			boolean exception = false;

			try
			{

				expression.eval(scope);
			}
			catch (ScriptException e)
			{
				if (e.getCause() instanceof UserApplicationError)
				{

					UserApplicationError ex = (UserApplicationError) e.getCause();
					if (ex instanceof NonUniqueCaseIdentifierError)
					{

						exception = true;
						System.out.println(ex.getCode());
						System.out.println(ex.getMessage());
					}
				}
				e.printStackTrace();
			}

			Assert.assertTrue(exception, "expecting exception to occur");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.carRefs.length=0;"
							+"var customer = factory.com_example_carapplication.createCustomer();" 
							+ "customer.state = pkg.com_example_carapplication.CustomerStates.ACTIVE;" 
							+ "customer.customerID = '102';" 
							+ "customer.firstName = 'Fred';" 
							+ "customer.age = 30;"
							+"data.customerRef = bpm.caseData.create(customer,'com.example.carapplication.Customer');"
							+"data.carRef = bpm.caseData.findBySimpleSearch('astra','com.example.carapplication.Car',0,100)[0];"
							+"Log.write('read car is '+bpm.scriptUtil.stringify(bpm.caseData.read(data.carRef)));"
							+"data.customerRef = bpm.caseData.findByCriteria(\"age=30\",'com.example.carapplication.Customer',0,100)[0];"
							+"bpm.caseData.link(data.carRef,data.customerRef,'owner');"
							+"var targetRef = bpm.caseData.navigateByCriteria(data.customerRef,'car',\"model='Astra'\",0,100)[0];"
							+"var refsLength = data.carRefs.length;"
							+"var customer2 = bpm.scriptUtil.copy(customer);"
							+"customer2.customerID = '103';"
							+"var fans = new Array();"
							+"fans.push(data.customerRef);"
							+"fans.push(bpm.caseData.create(customer2,'com.example.carapplication.Customer'));"
							+"bpm.caseData.linkAll(data.carRef,fans,'fans');"
							+"data.interstestedParties.pushAll(bpm.caseData.navigateAll(data.carRef,'fans',0,100));"
							+"var numberOfInterestedParties = data.interstestedParties.length;"
							+"var interestedCars = bpm.caseData.navigateAll(data.interstestedParties[1],'car1',0,100);"
							+"var interestedCarsLength = interestedCars.length;"
							+"var fans = bpm.caseData.navigateBySimpleSearch(data.carRef,'fans','fred',0,100);"
							+"var fansLength = fans.length;"
							+"bpm.caseData.unlinkAllByLinkName(data.carRef,'fans');"
							+"var fansAfterUnlinking = bpm.caseData.navigateAll(data.carRef,'fans',0,100).length;"
							+"bpm.caseData.linkAll(data.carRef,fans,'fans');"
							+"bpm.caseData.unlinkAll(data.carRef,data.interstestedParties,'fans');"
							+"var fansAfterUnlinkAll = bpm.caseData.navigateAll(data.carRef,'fans',0,100).length;"
							+"bpm.caseData.linkAll(data.carRef,fans,'fans');"
							+"bpm.caseData.unlink(data.carRef,fans[0],'fans');"
							+"bpm.caseData.unlink(data.carRef,fans[1],'fans');"
							+"var fansAfterIndividualUnlink = bpm.caseData.navigateAll(data.carRef,'fans',0,100).length;"
							+ ""
							);
			// @formatter:on

			expression.eval(scope);
			result = scope.getData();

			Assert.assertEquals(scope.getValue("targetRef"), result.get("carRef"), "Expecting to navigate to a car");
			System.out.println(result.get("interstestedParties"));
			Assert.assertEquals(scope.getValue("numberOfInterestedParties"), 2, "Array length doesn't match");
			Assert.assertEquals(scope.getValue("interestedCarsLength"), 1, "Array length doesn't match");
			Assert.assertEquals(scope.getValue("fansLength"), 2, "Unable to find expected number of fans");
			Assert.assertEquals(scope.getValue("fansAfterUnlinking"), 0, "Not expecting to find any fans after unlink");
			Assert.assertEquals(scope.getValue("fansAfterUnlinkAll"), 0, "Not expecting to find any fans after unlink");
			Assert.assertEquals(scope.getValue("fansAfterIndividualUnlink"), 0,
					"Not expecting to find any fans after unlink");

			// @formatter:off
			expression = ScriptManager
					.createExpression(""
							+"data.car = bpm.caseData.read(data.carRef);"
							+"Log.write('car registation is'+data.car.registration);"
							+ "var ref1 = bpm.caseData.updateByRef(data.carRef,data.car);"
							+ "bpm.caseData.updateByRef(data.carRef,data.car);"
							+ ""
							);
			// @formatter:on

			t1 = System.currentTimeMillis();

			//this should throw CaseOutOfSyncError
			exception = false;

			try
			{

				expression.eval(scope);
			}
			catch (ScriptException e)
			{
				if (e.getCause() instanceof UserApplicationError)
				{

					UserApplicationError ex = (UserApplicationError) e.getCause();
					if (ex instanceof CaseOutOfSyncError)
					{

						exception = true;
						System.out.println(ex.getCode());
						System.out.println(ex.getMessage());
					}
				}
				e.printStackTrace();
			}

			Assert.assertTrue(exception, "expecting exception to occur");

		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testSubprocessMapping()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{

		BigInteger deploymentId = deployRASC(
				"/apps/CarApps/CarApplication/Exports/Deployment Artifacts/CarApplication.rasc");

		String variableDescriptor1 = readFileContents(
				"apps/CarApps/CarProcess/Exports/Deployment Artifacts/SubProcess.js");

		String variableDescriptor2 = readFileContents(
				"apps/CarApps/CarProcess/Exports/Deployment Artifacts/CarProcessProcess.js");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("car", "{\"state\": \"DRIVING\", \"make\": \"Vauxhall\", \"model\": \"Astra\", \"numberOfDoors\": 4}");

		Map<String, Object> subprocValues = new HashMap<String, Object>();

		try
		{

			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.carapplication", 1l);

			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor1);
			descriptors.add(variableDescriptor2);
			long t1 = System.currentTimeMillis();
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.carprocess.CarProcess.CarProcessProcess");
			scope.setExtraData(subprocValues, "com.example.carprocess.CarProcess.SubProcess", "subprocess");
			long t2 = System.currentTimeMillis();

			System.out.println("Time taken to create scope with scripts" + (t2 - t1));

			// @formatter:off
			Expression expression = ScriptManager
					.createExpression(""
							+"subprocess.car = data.car;" 
							);
			// @formatter:on

			expression.eval(scope);

			Map<String, Object> map = scope.getExtraData("subprocess");
			String car = (String) map.get("car");

			Assert.assertEquals(car,
					"{\"state\":\"DRIVING\",\"make\":\"Vauxhall\",\"model\":\"Astra\",\"numberOfDoors\":4}");

		}
		finally
		{
			forceUndeploy(deploymentId);

		}
	}

	@Test
	public void testParseAndStringify()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{

		Scope scope = scriptEngine.createScope();

		Object date = scope.parse("2000-10-10", DataType.DATE);
		String date_str = scope.stringify(date, DataType.DATE);
		Assert.assertEquals(date_str, "2000-10-10");

		Object date_arr = scope.parse("[\"2000-10-10\",\"2011-11-11\"]", DataType.DATE);
		String date_arr_str = scope.stringify(date_arr, DataType.DATE);
		Assert.assertEquals(date_arr_str, "[\"2000-10-10\",\"2011-11-11\"]");

		Object time = scope.parse("10:10:10", DataType.TIME);
		String time_str = scope.stringify(time, DataType.TIME);
		Assert.assertEquals(time_str, "10:10:10");

		Object time_arr = scope.parse("[\"10:10:10\",\"11:11:11\"]", DataType.TIME);
		String time_arr_str = scope.stringify(time_arr, DataType.TIME);
		Assert.assertEquals(time_arr_str, "[\"10:10:10\",\"11:11:11\"]");

		Object datetime = scope.parse("2010-10-10T10:10:10Z", DataType.DATETIME);
		String datetime_str = scope.stringify(datetime, DataType.DATETIME);
		Assert.assertEquals(datetime_str, "2010-10-10T10:10:10.000Z");

		Object datetime_arr = scope.parse("[\"2010-10-10T10:10:10Z\",\"2011-10-10T10:10:10Z\"]", DataType.DATETIME);
		String datetime_arr_str = scope.stringify(datetime_arr, DataType.DATETIME);
		Assert.assertEquals(datetime_arr_str, "[\"2010-10-10T10:10:10.000Z\",\"2011-10-10T10:10:10.000Z\"]");

		Object float1 = scope.parse("10.1", DataType.FLOAT);
		String float_str = scope.stringify(float1, DataType.FLOAT);
		Assert.assertEquals(float_str, "10.1");

		Object float_arr = scope.parse("[10.1,20.1]", DataType.FLOAT);
		String float_arr_str = scope.stringify(float_arr, DataType.FLOAT);
		Assert.assertEquals(float_arr_str, "[10.1,20.1]");

		Object integer = scope.parse("20", DataType.INTEGER);
		String integer_str = scope.stringify(integer, DataType.INTEGER);
		Assert.assertEquals(integer_str, "20");

		Object integer_arr = scope.parse("[20,30]", DataType.INTEGER);
		String integer_arr_str = scope.stringify(integer_arr, DataType.INTEGER);
		Assert.assertEquals(integer_arr_str, "[20,30]");

		Object str_Arr = scope.parse("[\"str1\",\"str2\"]", DataType.STRING);
		String str_Arr_str = scope.stringify(str_Arr, DataType.STRING);
		Assert.assertEquals(str_Arr_str, "[\"str1\",\"str2\"]");

		Object bool_Arr = scope.parse("[false,true]", DataType.BOOLEAN);
		String bool_Arr_str = scope.stringify(bool_Arr, DataType.BOOLEAN);
		Assert.assertEquals(bool_Arr_str, "[false,true]");

	}
}
