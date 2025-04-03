package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.se.api.Expression;
import com.tibco.bpm.se.api.Scope;
import com.tibco.bpm.se.api.ScriptEngineService;
import com.tibco.bpm.se.api.ScriptManager;

public class ScriptTestQA extends BaseTest
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

		InputStream inputStream = ScriptTestQA.class.getClassLoader().getResourceAsStream(fileLocation);
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
	public void simpleDataTypeScript()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testdata/Exports/Deployment Artifacts/testdata.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testscript/Exports/Deployment Artifacts/process-js/testscript/testscriptProcess/testscriptProcess.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testdata", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess");
			// @formatter:off
			Expression expression = ScriptManager.createExpression("" 
					+ "data.DFBoolean = true;"
					+ "data.DFDate = new Date(); " 
					+ "data.DFDate.setDate(26);" 
					+ "data.DFDate.setMonth(4);"
					+ "data.DFDate.setFullYear(2019);" 
					+ "data.DFTime = new Date('11:11:11');"
					+ "data.DFTime.setHours(17);"
					+ "data.DFTime.setMinutes(40);" 
					+ "data.DFTime.setSeconds(34);"
					+ "data.DFDateTimeZone = new Date();"
					+ "data.DFDateTimeZone.setFullYear(1992);"
					+ "data.DFDateTimeZone.setDate(1);"
					+ "data.DFDateTimeZone.setMonth(6);"
					+ "data.DFDateTimeZone.setHours(18);"
					+ "data.DFDateTimeZone.setMinutes(24);"
					+ "data.DFDateTimeZone.setSeconds(16);"
					+ "data.DFDateTimeZone.setMilliseconds(343);"
					+ "data.DFFixedPointNumber = 248.163;"
					+ "data.DFFixedPointNumber = data.DFFixedPointNumber.add(0.836);"
					+ "data.DFNumber = 2480;"
					+ "data.DFNumber = data.DFNumber.add(836);" 
					+ "data.DFText = \"Simple Data Text\";"
					+ "data.DFText = data.DFText.replace(\"Text\",\"String\");"
					+ "data.DFText = data.DFText.concat(\"- value\");" 
					+ "data.DFText = data.DFText.toUpperCase();"
					+ "data.DFUri = \"http://www.tibco.com\";"
					+ "data.DFUri = data.DFUri.concat(\"/customers\");"
					+ "data.DFPerformer = 'resource1';"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();
			Assert.assertEquals(result.get("DFBoolean"), Boolean.TRUE, "incorrect value");
			Assert.assertEquals(result.get("DFDate"), "2019-05-26", "incorrect value");
			//ACE-1879 is resolved
			Assert.assertEquals(result.get("DFTime"), "17:40:34", "incorrect value");
			//ACE-1879 is resolved
			Assert.assertEquals(result.get("DFDateTimeZone"), "1992-07-01T17:24:16.343Z", "incorrect value");
			Assert.assertEquals(result.get("DFFixedPointNumber"), 248.999, "incorrect value");
			Assert.assertEquals(result.get("DFNumber"), 3316, "incorrect value");
			Assert.assertEquals(result.get("DFText"), "SIMPLE DATA STRING- VALUE", "incorrect value");
			Assert.assertEquals(result.get("DFUri"), "http://www.tibco.com/customers", "incorrect value");
			Assert.assertEquals(result.get("DFPerformer"), "resource1", "incorrect value");

			System.out.println(
					"================= Test for implcit type conversion of fixed point numbers and floating point numbers ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ ""//number assigned to fixed point number
					+ "data.DFNumberImplicit = 123.4564;"
					+ "Log.write(data.DFNumberImplicit.toString());"
					+ "data.DFFixedPointNumberImplicit1 = data.DFNumberImplicit;"
					+ "data.DFNumberImplicit = 123.4567;"
					+ "data.DFFixedPointNumberImplicit2 = data.DFNumberImplicit;"
					+ ""//fixed point number assigned to number
					+ "data.DFNumberImplicit = data.DFFixedPointNumber;" 
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();
			Assert.assertEquals(result.get("DFNumberImplicit"), 248.999, "incorrect value");
			Assert.assertEquals(result.get("DFFixedPointNumberImplicit1"), 123.456, "incorrect value");
			Assert.assertEquals(result.get("DFFixedPointNumberImplicit2"), 123.457, "incorrect value");

			System.out.println("================= Test for implcit type conversion of date ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFDate;"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("2019-05-26"), "incorrect value");

			System.out
					.println("================= Test for implcit type conversion of dateTimeZone ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFDateTimeZone;"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("1992-07-01T17:24:16.343Z"),
					"incorrect value");

			System.out.println("================= Test for implcit type conversion of time ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFTime;"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("17:40:34"), "incorrect value");

			System.out.println("================= Test for implcit type conversion of uri ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFUri;"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("http://www.tibco.com/customers"),
					"incorrect value");

			System.out.println("================= Test for implcit type conversion of boolean ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFBoolean;"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("true"), "incorrect value");

			System.out.println("================= Test for implcit type conversion of performer ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = data.DFPerformer;" 
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			Assert.assertTrue(result.get("DFImplicitText").toString().equals("resource1"), "incorrect value");

			System.out
					.println("================= Test for implcit type conversion for a Log.write ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+ "data.DFImplicitText = \"Date:\" + data.DFDate + \"DateTimeZone:\" + data.DFDateTimeZone + \"Time:\" + data.DFTime + \"URI:\" + data.DFUri + \"Boolean:\" + data.DFBoolean + \"Performer:\" + data.DFPerformer;" 
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			//precision is lost on date time zone when converted into a text
			System.out.println(result.get("DFImplicitText"));
			Assert.assertTrue(result.get("DFImplicitText").toString().contains("Date:Sun May 26 2019"),
					"contains the date value");

			Assert.assertTrue(
					result.get("DFImplicitText").toString().contains(
							"GMT+0100 (BST)DateTimeZone:Wed Jul 01 1992 18:24:16 GMT+0100 (BST)Time:Sat Jan 01 2000 17:40:34 GMT+0000 (GMT)URI:http://www.tibco.com/customersBoolean:truePerformer:resource1"),
					"contains the date value");

		}

		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void simpleDataTypesArrayScript()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testdata/Exports/Deployment Artifacts/testdata.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testscript/Exports/Deployment Artifacts/process-js/testscript/testscriptProcess_arrays/testscriptProcess_arrays.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testdata", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess_arrays");
			// @formatter:off
			Expression expression = ScriptManager.createExpression("" 
					+ "data.DFBooleanArray.push(true);"
					+ "data.DFBooleanArray.push(false);" 
					+ "var booleanArrayLength = data.DFBooleanArray.length;"
					+ "data.DFBooleanArray.push(false);" 
					+ " for(var i=0; i< data.DFBooleanArray.length; i++)" 
					+ "{" + "Log.write('value of boolean array elements: ' + data.DFBooleanArray[i]);" + "}" //expected value - true, false, false
					+ "data.DFDateArray.push(new Date('1990-04-26'));" 
					+ "data.DFDateArray.push(new Date());"
					+ "data.DFDateArray[1].setDate(10);" 
					+ "data.DFDateArray[1].setMonth(10);"
					+ "data.DFDateArray[1].setFullYear(1991);" 
					+ "var dateArrayLength = data.DFDateArray.length;"
					+ "data.DFDateArray.push(new Date('1990-04-26'));" 
					+ "for(var i=0; i< data.DFDateArray.length; i++)"
					+ "{" + "Log.write('value of date array elements: ' + data.DFDateArray[i]);" + "}"//expected value - 1990-04-26, 1991-11-10, 1990-04-26
					+ "data.DFTimeArray.push(new Date('10:11:12'));" 
					+ "data.DFTimeArray.push(new Date('13:14:15'));"
					+ "data.DFTimeArray.push(new Date('16:17:18'));" 
					+ "data.DFTimeArray.push(new Date('19:20:21'));"
					+ "data.DFTimeArray.push(new Date('22:23:24'));" 
					+ "data.DFTimeArray.push(new Date('01:02:03'));"
					+ "data.DFTimeArray.push(new Date('04:05:06'));" 
					+ "data.DFTimeArray.push(new Date('13:14:15'));"
					+ "var timeArrayLength = data.DFTimeArray.length;" 
					+ "data.DFTimeArray.push(new Date('13:14:15'));"
					+ "for(var i=0; i< data.DFTimeArray.length; i++)" 
					+ "{" + "Log.write('value of time array elements: ' + data.DFTimeArray[i].toString());" + "}"//expected value - 10:11:12, 13:14:15, 16:17:18, 19:20:21, 22:23:24, 01:02:03, 04:05:06, 13:14:15, 13:14:15
					+ "data.DFDateTimeZoneArray.push(new Date('1992-06-01T18:24:16.343+00:30'));"
					+ "data.DFDateTimeZoneArray.push(new Date('1993-07-02T19:25:26.344+01:00'));"
					+ "data.DFDateTimeZoneArray.push(new Date('1994-08-03T20:26:36.444+01:30'));"
					+ "data.DFDateTimeZoneArray.push(new Date('1995-09-04T21:27:46.555+02:00'));"
					+ "data.DFDateTimeZoneArray.push(new Date('1995-09-04T21:27:46.555-11:00'));"
					+ "data.DFDateTimeZoneArray.push(new Date('1995-09-04T21:27:46.555-11:00'));"
					+ "var dateTimeZoneArrayLength = data.DFDateTimeZoneArray.length;" 
					+ "data.DFDateTimeZoneArray.pop();"
					+ "for(var i=0; i< data.DFDateTimeZoneArray.length; i++)" 
					+ "{" + "Log.write('value of date time array elements: ' + data.DFDateTimeZoneArray[i]);" + "}"//expected value - 1992-06-01T18:24:16.343+00:30, 1993-07-02T19:25:26.344+01:00, 1994-08-03T20:26:36.444+01:30, 1995-09-04T21:27:46.555+02:00, 1995-09-04T21:27:46.555-11:00
					+ "data.DFFixedPointNumberArray.push((4.5*60)+1.29811);" //‭271.29811‬
					+ "data.DFFixedPointNumberArray.push(Math.PI);" //3.14159
					+ "data.DFFixedPointNumberArray.push(Math.LN10);" //2.302
					+ "data.DFFixedPointNumberArray.push(Math.LN2);" //0.693
					+ "data.DFFixedPointNumberArray.push(Math.floor(-231.4213));" //-232
					+ "data.DFFixedPointNumberArray.push(Math.sqrt(6.25));" //-2.5
					+ "data.DFFixedPointNumberArray.push(Math.max(-111.111, -222.2222, -333.33333));" //-111.111
					+ "data.DFFixedPointNumberArray.push(Math.min(-111.111, -222.2222, -333.33333));" //-333.33333
					+ "data.DFFixedPointNumberArray.push(Math.ceil(-231.4213));" //-231
					+ "var fixedPointArrayLength = data.DFFixedPointNumberArray.length;"
					+ "for(var i=0; i< data.DFFixedPointNumberArray.length; i++)"
					+ "{" + "Log.write('value of fixed point array elements: ' + data.DFFixedPointNumberArray[i]);" + "}"
					+ "data.DFFixedPointNumberArray.pop();"
					+ "var a = 101;"
					+ "var b = 1.012;"
					+ "data.DFNumberArray.push((2.5*60)+13+(21/7));" //166
					+ "data.DFNumberArray.push(Math.PI * 100);" //314.159
					+ "data.DFNumberArray.push(Math.LN10 / 100);" //0.02302
					+ "data.DFNumberArray.push(Math.LN2 * (a));" //‭69.993‬
					+ "data.DFNumberArray.push(Math.floor(-231.4213) + (a));" //-131
					+ "data.DFNumberArray.push(Math.sqrt(6.25) + (a*b));" //104.712
					+ "data.DFNumberArray.push(Math.max(-111.111, -222.2222, -333.33333) * 100);" //-11111.1
					+ "data.DFNumberArray.push(Math.min(-111.111, -222.2222, -333.333) / 100);" //-3.3333333
					+ "var numberArrayLength = data.DFNumberArray.length;"
					+ "data.DFNumberArray.push(a+b);"//‭102.012‬
					+ "for(var i=0; i< data.DFNumberArray.length; i++)"
					+ "{" + "Log.write('value of number array elements: ' + data.DFNumberArray[i]);" + "}"
					+ "var aString = \"third\";"
					+ "var bString = ' VALUE';"
					+ "data.DFTextArray.push(\"FIRST VALUE\");" //FIRST VALUE
					+ "data.DFTextArray.push('second value');" //second value
					+ "data.DFTextArray.push(aString + bString);" //third VALUE
					+ "data.DFTextArray.push(data.DFTextArray[0].concat(bString));" //FIRST VALUE  VALUE
					+ "data.DFTextArray.push(data.DFDateArray[0].toString());" //1990-04-26
					+ "var textArrayLength = data.DFTextArray.length;"
					+ "for(var i=0; i< data.DFTextArray.length; i++)"
					+ "{" + "Log.write('value of text array elements: ' + data.DFTextArray[i]);" + "}"
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			Object booleanArrayLength = scope.getValue("booleanArrayLength");
			Assert.assertEquals(booleanArrayLength.toString(), "2", "incorrect number of elements");

			Object dateArrayLength = scope.getValue("dateArrayLength");
			Assert.assertEquals(dateArrayLength.toString(), "2", "incorrect number of elements");

			Object timeArrayLength = scope.getValue("timeArrayLength");
			Assert.assertEquals(timeArrayLength.toString(), "8", "incorrect number of elements");

			Object dateTimeZoneArrayLength = scope.getValue("dateTimeZoneArrayLength");
			Assert.assertEquals(dateTimeZoneArrayLength.toString(), "6", "incorrect number of elements");

			Object fixedPointArrayLength = scope.getValue("fixedPointArrayLength");
			Assert.assertEquals(fixedPointArrayLength.toString(), "9", "incorrect number of elements");

			Object numberArrayLength = scope.getValue("numberArrayLength");
			Assert.assertEquals(numberArrayLength.toString(), "8", "incorrect number of elements");

			Object textArrayLength = scope.getValue("textArrayLength");
			Assert.assertEquals(textArrayLength.toString(), "5", "incorrect number of elements");

			Object dFTextArray = result.get("DFTextArray");
			System.out.println("DFTextArray is " + dFTextArray);
			Assert.assertEquals(dFTextArray,
					"[\"FIRST VALUE\",\"second value\",\"third VALUE\",\"FIRST VALUE VALUE\",\"Thu Apr 26 1990 00:00:00 GMT+0100 (BST)\"]",
					"incorrect values for text array");

			Object dFNumberArray = result.get("DFNumberArray");
			System.out.println("DFNumberArray is " + dFNumberArray);

			//			Assert.assertEquals(dFNumberArray.toString(), "[166,314,0,70,-131,105,-11111,-3,102]",
			//					"incorrect values for number array");
			Assert.assertEquals(dFNumberArray.toString(),
					"[166,314.1592653589793,0.02302585092994046,70.00786523655448,-131,104.712,-11111.1,-3.33333,102.012]",
					"incorrect values for number array");

			Object dFFixedPointNumberArray = result.get("DFFixedPointNumberArray");
			System.out.println("DFFixedPointNumberArray is " + dFFixedPointNumberArray);
			Assert.assertEquals(dFFixedPointNumberArray,
					"[271.29811,3.14159,2.30259,0.69315,-232,2.5,-111.111,-333.33333]",
					"incorrect values for fixed point number array");

			Object dFBooleanArray = result.get("DFBooleanArray");
			System.out.println("DFBooleanArray is " + dFBooleanArray);
			Assert.assertEquals(dFBooleanArray.toString(), "[true,false,false]", "incorrect values for text array");

			Object dFDateTimeZoneArray = result.get("DFDateTimeZoneArray");
			System.out.println("DFDateTimeZoneArray is " + dFDateTimeZoneArray);
			Assert.assertEquals(dFDateTimeZoneArray,
					"[\"1992-06-01T17:54:16.343Z\",\"1993-07-02T18:25:26.344Z\",\"1994-08-03T18:56:36.444Z\",\"1995-09-04T19:27:46.555Z\",\"1995-09-05T08:27:46.555Z\"]",
					"incorrect values for date time zone array");

			Object dFTimeArray = result.get("DFTimeArray");
			System.out.println("DFTimeArray is " + dFTimeArray);
			Assert.assertEquals(dFTimeArray,
					"[\"10:11:12\",\"13:14:15\",\"16:17:18\",\"19:20:21\",\"22:23:24\",\"01:02:03\",\"04:05:06\",\"13:14:15\",\"13:14:15\"]",
					"incorrect values for text array");

			Object dFDateArray = result.get("DFDateArray");
			System.out.println("DFDateArray is " + dFDateArray);
			Assert.assertEquals(dFDateArray, "[\"1990-04-26\",\"1991-11-10\",\"1990-04-26\"]",
					"incorrect values for text array");
		}
		finally

		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void bomSimpleDataTypeScript()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testdata/Exports/Deployment Artifacts/testdata.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testscript/Exports/Deployment Artifacts/process-js/testscript/testscriptProcess/testscriptProcess.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testdata", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			long t1 = System.currentTimeMillis();
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess");
			// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+ "var bomData = factory.com_example_businessobjectmodel.createTestclass();"
					+ "bomData.attributeText = \"BOM Data value\";"
					+ "bomData.attributeText = bomData.attributeText.concat(\" - Simple\");"
					+ "bomData.attributeText = bomData.attributeText.toLowerCase();"
					+ "bomData.attributeBoolean = false;"
					+ "bomData.attributeUri = \"http://www.tibco-integration.com\";"
					+ "bomData.attributeUri = bomData.attributeUri.concat(\"/help/guid=abcdefghiklmnop\");"
					+ "bomData.attributeUri = bomData.attributeUri.substr(0,46);"
					+ "bomData.attributeUri = bomData.attributeUri.concat(\"#1234\");"
					+ "bomData.attributeNumber = 1234.56;" 
					+ "bomData.attributeNumber.add(7890.123);"
					+ "bomData.attributeNumber.multiply(0.1);" 
					+ "bomData.attributeNumber.divide(0.2);"
					+ "bomData.attributeNumber.subtract(1234);" 
					+ "bomData.attributeFixedPointNumber = 123456.7891;"
					+ "bomData.attributeFixedPointNumber.add(123456.7891);"
					+ "bomData.attributeFixedPointNumber.multiply(2);" 
					+ "bomData.attributeFixedPointNumber.divide(2);"
					+ "bomData.attributeFixedPointNumber.subtract(1111.1111);"
					+ "bomData.attributeDate = new Date(\"2019-01-02\");" 
					+ "bomData.attributeDate.setDate(12);"
					+ "bomData.attributeDate.setMonth(11);" 
					+ "bomData.attributeDate.setFullYear(2079);"
					+ "bomData.attributeTime = new Date(\"23:19:11\");" 
					+ "bomData.attributeTime.setHours(22);"
					+ "bomData.attributeTime.setMinutes(44);" 
					+ "bomData.attributeTime.setSeconds(11);"
					+ "bomData.attributeDateTimeTZ = new Date('2019-11-12T19:31:11.231+4:30');"
					+ "bomData.attributeDateTimeTZ.setFullYear(2012);" 
					+ "bomData.attributeDateTimeTZ.setMonth(3);"
					+ "bomData.attributeDateTimeTZ.setDate(4);" 
					+ "bomData.attributeDateTimeTZ.setHours(6);"
					+ "bomData.attributeDateTimeTZ.setMinutes(10);" 
					+ "bomData.attributeDateTimeTZ.setSeconds(24);"
					+ "bomData.attributeDateTimeTZ.setMilliseconds(100);"
					+ "Log.write(bomData.attributeDateTimeTZ.getTimezoneOffset());"
					+ "data.BOMFieldTestData = bomData;"
					+ "Log.write(bomData.attributeText);" 
					+ "Log.write(bomData.attributeUri);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			scope.getValue("a");
			Map<String, Object> result = scope.getData();

			String bomDataValues = (String) result.get("BOMFieldTestData");
			long t2 = System.currentTimeMillis();
			System.out.println("total time taken is " + (t2 - t1));
			System.out.println("BOMFieldTestData is " + bomDataValues);

			System.out.println("================= Same Test to calculate time required ====================");

			t1 = System.currentTimeMillis();
			scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess");
			// @formatter:off
			 expression = ScriptManager.createExpression(""
					+ "var bomData = factory.com_example_businessobjectmodel.createTestclass();"
					+ "bomData.attributeText = \"BOM Data value\";"
					+ "bomData.attributeText = bomData.attributeText.concat(\" - Simple\");"
					+ "bomData.attributeText = bomData.attributeText.toLowerCase();"
					+ "bomData.attributeBoolean = false;"
					+ "bomData.attributeUri = \"http://www.tibco-integration.com\";"
					+ "bomData.attributeUri = bomData.attributeUri.concat(\"/help/guid=abcdefghiklmnop\");"
					+ "bomData.attributeUri = bomData.attributeUri.substr(0,46);"
					+ "bomData.attributeUri = bomData.attributeUri.concat(\"#1234\");"
					+ "bomData.attributeNumber = 1234.56;" 
					+ "bomData.attributeNumber.add(7890.123);"
					+ "bomData.attributeNumber.multiply(0.1);" 
					+ "bomData.attributeNumber.divide(0.2);"
					+ "bomData.attributeNumber.subtract(1234);" 
					+ "bomData.attributeFixedPointNumber = 123456.7891;"
					+ "bomData.attributeFixedPointNumber.add(123456.7891);"
					+ "bomData.attributeFixedPointNumber.multiply(2);" 
					+ "bomData.attributeFixedPointNumber.divide(2);"
					+ "bomData.attributeFixedPointNumber.subtract(1111.1111);"
					+ "bomData.attributeDate = new Date(\"2019-01-02\");" 
					+ "bomData.attributeDate.setDate(12);"
					+ "bomData.attributeDate.setMonth(11);" 
					+ "bomData.attributeDate.setFullYear(2079);"
					+ "bomData.attributeTime = new Date(\"23:19:11\");" 
					+ "bomData.attributeTime.setHours(22);"
					+ "bomData.attributeTime.setMinutes(44);" 
					+ "bomData.attributeTime.setSeconds(11);"
					+ "bomData.attributeDateTimeTZ = new Date('2019-11-12T19:31:11.231+4:30');"
					+ "bomData.attributeDateTimeTZ.setFullYear(2012);" 
					+ "bomData.attributeDateTimeTZ.setMonth(3);"
					+ "bomData.attributeDateTimeTZ.setDate(4);" 
					+ "bomData.attributeDateTimeTZ.setHours(6);"
					+ "bomData.attributeDateTimeTZ.setMinutes(10);" 
					+ "bomData.attributeDateTimeTZ.setSeconds(24);"
					+ "bomData.attributeDateTimeTZ.setMilliseconds(100);" 
					+ "data.BOMFieldTestData = bomData;"
					+ "Log.write(bomData.attributeText);" 
					+ "Log.write(bomData.attributeUri);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			scope.getValue("a");
			result = scope.getData();

			bomDataValues = (String) result.get("BOMFieldTestData");
			t2 = System.currentTimeMillis();
			System.out.println("total time taken is " + (t2 - t1));

			//‭attribute number - expected value is - 3,328.3415‬
			//attributeFixedPointNumber - expected value is - 245802.4671
			//attributeTime - ‭expected value is - 22:44:11
			//attributeDateTimeTZ - expected value is - 2012-03-04T06:10:24.100+4:30Z

			Assert.assertEquals(bomDataValues,
					"{\"attributeText\":\"bom data value - simple\",\"attributeTime\":\"22:44:11\",\"attributeDate\":\"2079-12-12\",\"attributeDateTimeTZ\":\"2012-04-04T05:10:24.100Z\",\"attributeNumber\":1234.56,\"attributeUri\":\"http://www.tibco-integration.com/help/guid=abc#1234\",\"attributeBoolean\":\"false\",\"attributeFixedPointNumber\":123456.7891}");
		}
		finally
		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void bomArrayAndComplexDataTypeScript()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testdata/Exports/Deployment Artifacts/testdata.rasc");

		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testscript/Exports/Deployment Artifacts/process-js/testscript/testscriptProcess_arrays/testscriptProcess_arrays.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testdata", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess_arrays");
			// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+"var bomCallingClass = factory.com_example_businessobjectmodel.createTestclasscalling();"
					+"var bomLocallyCalledClass = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"var bomLocallyCalledClass_1 = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"var bomLocallyCalledClass_2 = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"var bomLocallyCalledClass_3 = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"var bomOutsideCalledClass_1 = factory.com_example_testdata_outside.createOutside_class();"
					+"var bomOutsideCalledClass_2 = factory.com_example_testdata_outside.createOutside_class();"
					+"var bomOutsideCalledClass_3 = factory.com_example_testdata_outside.createOutside_class();"
					+"var bomOutsideCalledClassObject_1 = factory.com_example_testdata_outside.createOutside_class_object();"
					+"var bomOutsideCalledClassObject_2 = factory.com_example_testdata_outside.createOutside_class_object();"
					+""//boolean
					+"data.DFBooleanArray.push(true);"
					+"data.DFBooleanArray.push(false);"
					+"bomCallingClass.attributeBooleanArray.pushAll(data.DFBooleanArray);"
					+"bomCallingClass.attributeBooleanArray.push(false);"
					+"bomCallingClass.attributeBooleanArray.reverse();"
					+"var objectBooleanArrayLength = bomCallingClass.attributeBooleanArray.length;"
					+""//3
					+"bomCallingClass.attributeBooleanArray.push(true);"
					+"for(var i=0; i< bomCallingClass.attributeBooleanArray.length; i++)"
					+"{"
					+"	Log.write('value of calling boolean array elements: ' + bomCallingClass.attributeBooleanArray[i]);"
					+"}"
					+""//false, false, true, true 
					+""//date
					+"data.DFDateArray.push(new Date('1992-11-19'));"
					+"data.DFDateArray.push(new Date('2002-10-18'));"
					+"bomCallingClass.attributeDateArray.pushAll(data.DFDateArray);"
					+"bomCallingClass.attributeDateArray.push(new Date('1968-02-11'), new Date('2023-09-08'), new Date('2029-01-03'));"
					+"var objectDateArrayLength = bomCallingClass.attributeDateArray.length;"
					+""//5
					+"for(var i=0; i< bomCallingClass.attributeDateArray.length; i++)"
					+"{"
					+"	Log.write('value of calling date array elements: ' + bomCallingClass.attributeDateArray[i]);"
					+"}"
					+""//1992-11-19, 2002-10-18, 1968-02-11, 2023-09-08, 2029-01-03
					+""//date time zone
					+"bomCallingClass.attributeDateTimeTZArray.push(new Date('1992-06-01T18:24:16.343+00:30'));"
					+"bomCallingClass.attributeDateTimeTZArray.push(new Date('1993-11-29T01:10:13.979-00:30'));"
					+"bomCallingClass.attributeDateTimeTZArray.reverse();"
					+"var objectDateTimeTZArrayLength = bomCallingClass.attributeDateTimeTZArray.length;"
					+""//2
					+"bomCallingClass.attributeDateTimeTZArray.push(new Date('2021-12-26T11:12:13.123'));"
					+"for(var i=0; i<bomCallingClass.attributeDateTimeTZArray.length; i++)"
					+"{"
					+"	Log.write('value of calling date time TZ array elements: ' + bomCallingClass.attributeDateTimeTZArray[i]);"
					+"}"
					+""//1992-06-01T18:54:16, 1993-11-29T00:40:13, 2021-12-26T11:12:13
					+""//text
					+"data.DFTextArray.push(\"first value in bom text array\");"
					+"data.DFTextArray.push(\"second value in bom text array\");"
					+"for(var i=0; i<data.DFTextArray.length; i++)"
					+"{"
					+"	bomCallingClass.attributeTextArray.push(data.DFTextArray[i]);"
					+"}"
					+"bomCallingClass.attributeTextArray.push(\"third value in bom text array\");"
					+"for(var i=3; i<8; i++)"
					+"{"
					+"	bomCallingClass.attributeTextArray.push((i+1).toString() + 'TH value in bom text array');"
					+"}"
					+"bomCallingClass.attributeTextArray[2] = bomCallingClass.attributeTextArray[2].toUpperCase();"
					+"bomCallingClass.attributeTextArray[4] = bomCallingClass.attributeTextArray[4].toLowerCase();"
					+"bomCallingClass.attributeTextArray[5] = bomCallingClass.attributeTextArray[5].substr(0, 5);"
					+"var objectTextArrayLength = bomCallingClass.attributeTextArray.length;"
					+""//8
					+"for(var i=0; i< bomCallingClass.attributeTextArray.length; i++)"
					+"{"
					+"	Log.write('value of calling text array elements: ' + bomCallingClass.attributeTextArray[i]);"
					+"}"
					+""//first value in bom text array, second value in bom text array, THIRD VALUE IN BOM TEXT ARRAY, 4TH value in bom text array, 5th value in bom text array,
					+""//6TH v, 7TH value in bom text array, 8TH value in bom text array
					+""//URI
					+"bomCallingClass.attributeUriArray.push(\"http://www.tibco.com\");"
					+"bomCallingClass.attributeUriArray.push(\"http://www.vista-equity.com\");"
					+"bomCallingClass.attributeUriArray.push(\"http://www.citibank.com\");"
					+"bomCallingClass.attributeUriArray[2] = bomCallingClass.attributeUriArray[2].concat(\"/guid=abcd/customer-success-story\");"
					+"bomCallingClass.attributeUriArray.reverse();"
					+"var objectUriLength = bomCallingClass.attributeUriArray.length;"
					+""//3
					+"bomCallingClass.attributeUriArray.push('http://www.spotfire.org/');"
					+"for(var i=0; i< bomCallingClass.attributeUriArray.length; i++)"
					+"{"
					+"	Log.write('value of calling uri array elements: ' + bomCallingClass.attributeUriArray[i]);"
					+"}"
					+""//http://www.citibank.com/guid=abcd/customer-success-story, http://www.vista-equity.com, http://www.tibco.com, http://www.spotfire.org/
					+""//time
					+"data.DFTimeArray.push(new Date('10:11:12'));"
					+"data.DFTimeArray.push(new Date('13:14:15'));"
					+""//bomCallingClass.attributeTimeArray = data.DFTimeArray;"
					+"for(var i=0; i<data.DFTimeArray.length; i++)"
					+"{"
					+"bomCallingClass.attributeTimeArray.push(data.DFTimeArray[i]);"
					+"}"
					+"bomCallingClass.attributeTimeArray.push(new Date('16:17:18'), new Date('19:20:21'), new Date('01:02:03'), new Date('22:23:24'));"
					+"var objectTimeArrayLength = bomCallingClass.attributeTimeArray.length;"
					+""//6
					+"bomCallingClass.attributeTimeArray.pop();"
					+"for(var i=0; i< bomCallingClass.attributeTimeArray.length; i++)"
					+"{"
					+"	Log.write('value of calling time array elements: ' + bomCallingClass.attributeTimeArray[i]);"
					+"}"
					+""//10:11:12, 13:14:15, 16:17:18, 19:20:21, 01:02:03
					+""//Fixed point number
					+"data.DFFixedPointNumberArray.push(1001.234);"
					+"data.DFFixedPointNumberArray.push(2002.345);"
					+""//bomCallingClass.attributeFixedPointNumberArray = data.DFFixedPointNumberArray;"
					+"for(var i=0; i<data.DFFixedPointNumberArray.length; i++)"
					+"{"
					+"bomCallingClass.attributeFixedPointNumberArray.push(data.DFFixedPointNumberArray[i]);"
					+"}"
					+"bomCallingClass.attributeFixedPointNumberArray.push(3003.456, 4004.567);"
					+"bomCallingClass.attributeFixedPointNumberArray.push(bomCallingClass.attributeFixedPointNumberArray[1] + bomCallingClass.attributeFixedPointNumberArray[2]);"
					+"var objectFixedPointNumberArrayLength = bomCallingClass.attributeFixedPointNumberArray.length;"
					+""//5
					+"for(var i=0; i< bomCallingClass.attributeFixedPointNumberArray.length; i++)"
					+"{"
					+"	Log.write('value of calling fixed point number array elements: ' + bomCallingClass.attributeFixedPointNumberArray[i]);"
					+"}"
					+""//1001.234, 2002.345, 3003.456, 4004.567, 5005.801
					+""//object mapping
					+"bomLocallyCalledClass_1.attributeDateLocallyCalledArray.push(new Date('1990-04-26'));"
					+"bomLocallyCalledClass_1.attributeDateLocallyCalledArray.push(new Date('2019-07-07'));"
					+"bomLocallyCalledClass_1.attributeLocallyCalledEnumeration = pkg.com_example_businessobjectmodel.testenumeration.INTERMEDIATE1234567890;"
					+"bomLocallyCalledClass_1.attributeNumberLocallyCalled = (12345 * 0.13);" //1604.85
					+"bomLocallyCalledClass_1.attributeTextLocallyCalled = \"First\";"
					+"bomCallingClass.attributeLocalCallingArray.push(bomLocallyCalledClass_1);"
					+"bomCallingClass.attributeLocalCallingArray[0].attributeDateLocallyCalledArray.push(new Date('2019-10-15'));"
					+"bomCallingClass.attributeLocalCallingArray[0].attributeNumberLocallyCalled.add(0.2);" //2469.334
					+""
					+"bomLocallyCalledClass_2.attributeLocallyCalledEnumeration = pkg.com_example_businessobjectmodel.testenumeration.COMPLETED1234435645689;"
					+"bomLocallyCalledClass_2.attributeNumberLocallyCalled = (12340 * 0.2);" //2468
					+"bomLocallyCalledClass_2.attributeTextLocallyCalled = \"SECON\";"
					+"bomCallingClass.attributeLocalCallingArray.push(bomLocallyCalledClass_2);"
					+"bomCallingClass.attributeLocalCallingArray[1].attributeNumberLocallyCalled.multiply(2);" //4936
					+"bomCallingClass.attributeLocalCallingArray[1].attributeTextLocallyCalled = bomCallingClass.attributeLocalCallingArray[1].attributeTextLocallyCalled.toLowerCase();"
					+""
					+"bomLocallyCalledClass_3.attributeDateLocallyCalledArray.push(new Date('2990-04-26'));"
					+"bomLocallyCalledClass_3.attributeDateLocallyCalledArray.push(new Date('1019-07-07'));"
					+"bomLocallyCalledClass_3.attributeDateLocallyCalledArray.push(new Date('1947-08-15'));"
					+"bomLocallyCalledClass_3.attributeDateLocallyCalledArray.push(new Date('1984-04-26'));"
					+"bomLocallyCalledClass_3.attributeTextLocallyCalled = \"third\";"
					+"bomCallingClass.attributeLocalCallingArray.push(bomLocallyCalledClass_3);"
					+"bomCallingClass.attributeLocalCallingArray[2].attributeTextLocallyCalled = bomCallingClass.attributeLocalCallingArray[2].attributeTextLocallyCalled.toUpperCase();"
					+"bomCallingClass.attributeLocalCallingArray.reverse();"
					//@Birju - As per the concat() API, it returns a new array. It does not change the existing array.
					//+"bomCallingClass.attributeLocalCallingArray.concat(bomLocallyCalledClass_1);"
					+"bomCallingClass.attributeLocalCallingArray.push(bomCallingClass.attributeLocalCallingArray[2]);"
					//pushing array-1 which is bomLocallyCalledClass_1
					//copyWithin is not not working
					//@Birju - copyWithin() is an ES6 API that does not seem to be supported in Nashorn.
					//+"bomCallingClass.attributeLocalCallingArray.copyWithin(3,2,4);"
					//but copying bomLocallyCalledClass_1 values
					//need some debugging here the value gets updated to all copies
					//This is something you would want to avoid doing. Any duplicates that you keep will be valid for that session only. Once you submit and retrieve it back, they are just two different copies(as there no references in JSON). A JavaScript/JSON programmer will ideally understand that is the way things would work. There are no proxies supported in Nashorn, which means that even if we wanted to suppress some one from doing this, it would not be possible. In Forms, our list implementation detected duplicates and rejected them if I remember. Now the current Arrays are a proxy around the same implementation, it will continue to work so.
					+"bomCallingClass.attributeLocalCallingArray[3].attributeDateLocallyCalledArray.push(new Date('1948-01-30'));"
					+"bomCallingClass.attributeLocalCallingArray[3].attributeLocallyCalledEnumeration = pkg.com_example_businessobjectmodel.testenumeration.CANCELLED;"
					+"bomCallingClass.attributeLocalCallingArray[3].attributeNumberLocallyCalled = bomCallingClass.attributeLocalCallingArray[3].attributeDateLocallyCalledArray.length;"
					+""//THIRD 2990-04-26 101907-07, 1947-08-15, 1984-04-26
					+""//secon COMPLETED1234435645689, 49.36
					+""//First INTERMEDIATE1234567890, 1604.9, 1990-04-26, 2019-07-07, 2019-10-15
					+""//First CANCELLED, 4, 1990-04-26, 2019-07-07, 2019-10-15, 1948-01-30
					+""//evaluate one of the objects in the array
					+"var objectLocallyCalledObjectArrayLength = bomCallingClass.attributeLocalCallingArray.length;"
					+"var bomLocallyCalledClass_Evaluate_1 = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"var bomLocallyCalledClass_Evaluate_2 = factory.com_example_businessobjectmodel.createLocallycalledclass();"
					+"bomLocallyCalledClass_Evaluate_1 = bomCallingClass.attributeLocalCallingArray[3];"
					+"bomLocallyCalledClass_Evaluate_2 = bomCallingClass.attributeLocalCallingArray[1];"
					+""//outside enumeration
					+"bomCallingClass.attributeOutsideEnumeration = pkg.com_example_testdata_outside.outside_enumeration.OUTSIDEENUMERATION1;"
					+""//construct outside class object - 1
					+"var xString = \"http://www.twitter.com\";"
					+"bomOutsideCalledClassObject_1.outsideClassObjectAttributeURI = xString;"
					+"bomOutsideCalledClassObject_1.outsideClassObjectAttributeURI = bomOutsideCalledClassObject_1.outsideClassObjectAttributeURI.concat(\"/celebrityProfile1\");"
					+"bomOutsideCalledClassObject_1.outsideClassObjectAttributeBoolean = true;"
					+""//construct outside class - 1
					+"data.DFDateTimeZoneArray.pop();"
					+"data.DFDateTimeZoneArray.pop();"
					+"data.DFDateTimeZoneArray.pop();"
					+"data.DFDateTimeZoneArray.push(new Date('1993-11-19'), new Date('2003-10-18'));"
					+"bomOutsideCalledClass_1.outsideAttributeDateTimeZoneArray.pushAll(data.DFDateTimeZoneArray);"
					+"bomOutsideCalledClass_1.outsideAttributeDateTimeZoneArray.push(new Date('1680-05-29'));"
					+"for(var i=0; i<2; i++)"
					+"{"
					+"data.DFTextArray.pop();"
					+"}"
					+"data.DFTextArray.push('1st text value inserted using pushAll(array)', \"2nd text value inserted using pushAll(array)\");"
					+"bomOutsideCalledClass_1.outsideAttributeTextArray.pushAll(data.DFTextArray);"
					+"bomOutsideCalledClass_1.outsideAttributeTextArray.push('3rd text value inserted using push(object)');"
					+"bomOutsideCalledClass_1.outsideAttributeObject = bomOutsideCalledClassObject_1;"
					+""//construct outside class object - 2
					+"var yString = \"http://www.instagram.com\";"
					+"bomOutsideCalledClassObject_2.outsideClassObjectAttributeURI = yString;"
					+"bomOutsideCalledClassObject_2.outsideClassObjectAttributeURI = bomOutsideCalledClassObject_1.outsideClassObjectAttributeURI.concat(\"/politicianProfile1\");"
					+"bomOutsideCalledClassObject_2.outsideClassObjectAttributeBoolean = false;"
					+""//construct outside class - 2
					+""//trying <array>.length = 0; to clear array
					+"data.DFDateTimeZoneArray.length = 0;"
					+"data.DFDateTimeZoneArray.push(new Date('1994-11-19'), new Date('2004-10-18'));"
					+"data.DFDateTimeZoneArray.push(new Date('1857-06-13'));"
					+"bomOutsideCalledClass_2.outsideAttributeDateTimeZoneArray.pushAll(data.DFDateTimeZoneArray);"
					+"bomOutsideCalledClass_2.outsideAttributeDateTimeZoneArray.push(new Date('1680-05-29'));"
					+"data.DFTextArray.length = 0;"
					+"var objectTextArrayLength2 = data.DFTextArray.length;"
					+"data.DFTextArray.push('single value in text array - should still be considered as array');"
					+"var objectTextArrayLength3 = data.DFTextArray.length;"
					+"bomOutsideCalledClass_2.outsideAttributeTextArray.pushAll(data.DFTextArray);"
					+"bomOutsideCalledClass_2.outsideAttributeObject = bomOutsideCalledClassObject_2;"
					+"data.BOMOutsideClasses.push(bomOutsideCalledClass_2, bomOutsideCalledClass_1);"
					+"bomCallingClass.attributeOutsideCallingArray.pushAll(data.BOMOutsideClasses);"
					+"data.BOMCallingClass = bomCallingClass;"
					+"");
			// @formatter:on
			expression.eval(scope);
			Object objectBooleanArrayLength = scope.getValue("objectBooleanArrayLength");
			Assert.assertEquals(objectBooleanArrayLength.toString(), "3", "incorrect number of elements");

			Object objectDateArrayLength = scope.getValue("objectDateArrayLength");
			Assert.assertEquals(objectDateArrayLength.toString(), "5", "incorrect number of elements");

			Object objectDateTimeTZArrayLength = scope.getValue("objectDateTimeTZArrayLength");
			Assert.assertEquals(objectDateTimeTZArrayLength.toString(), "2", "incorrect number of elements");

			Object objectTextArrayLength = scope.getValue("objectTextArrayLength");
			Assert.assertEquals(objectTextArrayLength.toString(), "8", "incorrect number of elements");

			Object objectTextArrayLength2 = scope.getValue("objectTextArrayLength2");
			Assert.assertEquals(objectTextArrayLength2.toString(), "0", "incorrect number of elements");

			Object objectTextArrayLength3 = scope.getValue("objectTextArrayLength3");
			Assert.assertEquals(objectTextArrayLength3.toString(), "1", "incorrect number of elements");

			Object objectUriLength = scope.getValue("objectUriLength");
			Assert.assertEquals(objectUriLength.toString(), "3", "incorrect number of elements");

			Object objectTimeArrayLength = scope.getValue("objectTimeArrayLength");
			Assert.assertEquals(objectTimeArrayLength.toString(), "6", "incorrect number of elements");

			Object bomLocallyCalledClassEvaluate = scope.getValue("bomLocallyCalledClass_Evaluate_1");
			System.out.println("bomLocallyCalledClass_Evaluate is: " + bomLocallyCalledClassEvaluate);
			//TODO insterted by nashtapu [Jul 8, 2019 5:08:50 PM] - see todo in script "need some debugging here the value gets updated to all copies"
			Assert.assertEquals(bomLocallyCalledClassEvaluate,
					"{\"attributeTextLocallyCalled\": \"First\", \"attributeNumberLocallyCalled\": 4, \"attributeDateLocallyCalledArray\": [\"1990-04-26\", \"2019-07-07\", \"2019-10-15\", \"1948-01-30\"], \"attributeLocallyCalledEnumeration\": \"CANCELLED\"}",
					"incorrect payload for the object");

			bomLocallyCalledClassEvaluate = scope.getValue("bomLocallyCalledClass_Evaluate_2");
			System.out.println("bomLocallyCalledClass_Evaluate is: " + bomLocallyCalledClassEvaluate);
			Assert.assertEquals(bomLocallyCalledClassEvaluate,
					"{\"attributeTextLocallyCalled\": \"secon\", \"attributeNumberLocallyCalled\": 2468, \"attributeLocallyCalledEnumeration\": \"COMPLETED1234435645689\"}",
					"incorrect payload for the object");

			Object objectLocallyCalledObjectArrayLength = scope.getValue("objectLocallyCalledObjectArrayLength");
			Assert.assertEquals(objectLocallyCalledObjectArrayLength.toString(), "4", "incorrect number of elements");

			Map<String, Object> result = scope.getData();

			String bomCallingClass = (String) result.get("BOMCallingClass");
			System.out.println("BOMCallingClass is " + bomCallingClass);
			Assert.assertEquals(bomCallingClass,
					"{\"attributeTextArray\":[\"first value in bom text array\",\"second value in bom text array\",\"THIRD VALUE IN BOM TEXT ARRAY\",\"4TH value in bom text array\",\"5th value in bom text array\",\"6TH v\",\"7TH value in bom text array\",\"8TH value in bom text array\"],\"attributeTimeArray\":[\"10:11:12\",\"13:14:15\",\"16:17:18\",\"19:20:21\",\"01:02:03\"],\"attributeDateArray\":[\"1992-11-19\",\"2002-10-18\",\"1968-02-11\",\"2023-09-08\",\"2029-01-03\"],\"attributeDateTimeTZArray\":[\"1993-11-28T14:40:13.979Z\",\"1992-06-01T17:54:16.343Z\",\"2021-12-26T03:12:13.123Z\"],\"attributeUriArray\":[\"http://www.citibank.com/guid=abcd/customer-success-story\",\"http://www.vista-equity.com\",\"http://www.tibco.com\",\"http://www.spotfire.org/\"],\"attributeBooleanArray\":[false,false,true,true],\"attributeFixedPointNumberArray\":[1001.234,2002.345,3003.456,4004.567,5005.801],\"attributeOutsideEnumeration\":\"OUTSIDEENUMERATION1\",\"attributeLocalCallingArray\":[{\"attributeTextLocallyCalled\":\"THIRD\",\"attributeDateLocallyCalledArray\":[\"2990-04-26\",\"1019-07-07\",\"1947-08-15\",\"1984-04-26\"]},{\"attributeTextLocallyCalled\":\"secon\",\"attributeNumberLocallyCalled\":2468,\"attributeLocallyCalledEnumeration\":\"COMPLETED1234435645689\"},{\"attributeTextLocallyCalled\":\"First\",\"attributeNumberLocallyCalled\":4,\"attributeDateLocallyCalledArray\":[\"1990-04-26\",\"2019-07-07\",\"2019-10-15\",\"1948-01-30\"],\"attributeLocallyCalledEnumeration\":\"CANCELLED\"},{\"attributeTextLocallyCalled\":\"First\",\"attributeNumberLocallyCalled\":4,\"attributeDateLocallyCalledArray\":[\"1990-04-26\",\"2019-07-07\",\"2019-10-15\",\"1948-01-30\"],\"attributeLocallyCalledEnumeration\":\"CANCELLED\"}],\"attributeOutsideCallingArray\":[{\"outsideAttributeDateTimeZoneArray\":[\"1994-11-18T20:00:00.000Z\",\"2004-10-18T00:00:00.000Z\",\"1857-06-12T21:00:00.000Z\",\"1680-05-28T23:00:00.000Z\"],\"outsideAttributeTextArray\":[\"single value in text array - should still be considered as array\"],\"outsideAttributeObject\":{\"outsideClassObjectAttributeURI\":\"http://www.twitter.com/celebrityProfile1/politicianProfile1\",\"outsideClassObjectAttributeBoolean\":false}},{\"outsideAttributeDateTimeZoneArray\":[\"1993-11-18T21:00:00.000Z\",\"2003-10-18T00:00:00.000Z\",\"1680-05-28T23:00:00.000Z\"],\"outsideAttributeTextArray\":[\"1st text value inserted using pushAll(array)\",\"2nd text value inserted using pushAll(array)\",\"3rd text value inserted using push(object)\"],\"outsideAttributeObject\":{\"outsideClassObjectAttributeURI\":\"http://www.twitter.com/celebrityProfile1\",\"outsideClassObjectAttributeBoolean\":true}}]}",
					"incorrect payload for the object");

		}
		finally

		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}

	@Test
	public void simpleDataTypesArrayImplicitConversionScript()
			throws IOException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException,
			ScriptException, InternalException, PersistenceException, DeploymentException
	{
		BigInteger deploymentId1 = deployRASC(
				"/apps/scripting-tests/testdata_outside/Exports/Deployment Artifacts/testdata_outside.rasc");
		BigInteger deploymentId2 = deployRASC(
				"/apps/scripting-tests/testdata/Exports/Deployment Artifacts/testdata.rasc");
		String variableDescriptor = readFileContents(
				"apps/scripting-tests/testscript/Exports/Deployment Artifacts/process-js/testscript/testscriptProcess_implicitConversionArrays/testscriptProcess_implicitConversionArrays.js");

		Map<String, Object> data = new HashMap<String, Object>();
		try
		{
			Map<String, Long> businessDataApplicationInfo = new HashMap<String, Long>();
			businessDataApplicationInfo.put("com.example.testdata", 1l);
			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);
			Scope scope = scriptEngine.createScope(businessDataApplicationInfo, descriptors);
			scope.setData(data, "com.example.testscript.testscript.testscriptProcess_implicitConversionArrays");
			// @formatter:off
			Expression expression = ScriptManager.createExpression(""
					+""//text
					+"data.DFTexts.push('abc012');"
					+"data.DFTexts.push('def345');"
					+"var lengthDFTexts = data.DFTexts.length;" //2
					+"data.DFImplicitTexts.push('Texts:');"
					+"data.DFImplicitTexts.pushAll(data.DFTexts);"
					+""//boolean 
					+"data.DFBoolean = true;"
					+"data.DFBooleans.push(true);"
					+"data.DFBooleans.push(false);" 
					+"var lengthDFBooleans = data.DFBooleans.length;" //2
					+"data.DFImplicitTexts.push('Booleans:');"
					+"data.DFImplicitTexts.pushAll(data.DFBooleans);"
					+"data.DFImplicitTexts.push(data.DFBoolean);"
					+""//date
					+"data.DFDates.push(new Date('1990-04-26'));"
					+"data.DFDates.push(new Date('1992-01-06'));"
					+"data.DFDate = new Date('2001-01-01');"
					+"var lengthDFDates = data.DFDates.length;" //2
					+"data.DFImplicitTexts.push('Dates:');"
					+"data.DFImplicitTexts.push(new Date('1999-04-08'));"
					+"data.DFImplicitTexts.push(data.DFDate);"
					+"data.DFImplicitTexts.pushAll(data.DFDates);"
					+""//time
					+"data.DFTimes.push(new Date('01:02:55'));"
					+"data.DFTimes.push(new Date('03:04:56'));"
					+"data.DFTime = new Date('13:24:57');"
					+"var lengthDFTimes = data.DFTimes.length;" //2
					+"data.DFImplicitTexts.push('Times:');"
					+"data.DFImplicitTexts.push(new Date('14:48:58'));"
					+"data.DFImplicitTexts.push(data.DFTime);"
					+"data.DFImplicitTexts.pushAll(data.DFTimes);"
					+""//date time zones
					+"data.DFDateTimeZones.push(new Date('1995-09-015T18:24:16.343+00:30'));"
					+"data.DFDateTimeZones.push(new Date('1999-01-19T21:27:46.555-11:00'));"
					+"data.DFDateTimeZone = new Date('2006-02-14T20:26:36.444+01:30');"
					+"var lengthDFDateTimeZones= data.DFDateTimeZones.length;" //2
					+"data.DFImplicitTexts.push('DateTimeZones:');"
					+"data.DFImplicitTexts.push(new Date('2019-08-18T18:24:16.343'));"
					+"data.DFImplicitTexts.push(data.DFDateTimeZone);"
					+"data.DFImplicitTexts.pushAll(data.DFDateTimeZones);"
					+""//uris
					+"data.DFUris.push('http://www.tibco.co.uk/');"
					+"data.DFUris.push('http://www.tibco.co.in/');"
					+"data.DFUri = 'http://www.tibco.co.de/';"
					+"var lengthDFUris = data.DFUris.length;" //2
					+"data.DFImplicitTexts.push('URIs:');"
					+"data.DFImplicitTexts.push('http://www.tibco.co.ca/');"
					+"data.DFImplicitTexts.push(data.DFUri);"
					+"data.DFImplicitTexts.pushAll(data.DFUris);"
					+""//performers
					+"data.DFPerformers.push('resource1');"
					+"data.DFPerformers.push('resource2');"
					+"data.DFPerformer = 'resource3';"
					+"var lengthDFPerformers = data.DFPerformers.length;" //2
					+"data.DFImplicitTexts.push('Performers:');"
					+"data.DFImplicitTexts.push('resource4');"
					+"data.DFImplicitTexts.push(data.DFPerformer);"
					+"data.DFImplicitTexts.pushAll(data.DFPerformers);"
					+"var lengthDFImplicitTexts = data.DFImplicitTexts.length;" //32
					+ "");
			// @formatter:on
			expression.eval(scope);
			Map<String, Object> result = scope.getData();

			Object lengthDFTexts = scope.getValue("lengthDFTexts");
			Assert.assertEquals(lengthDFTexts.toString(), "2", "incorrect number of elements");

			Object lengthDFBooleans = scope.getValue("lengthDFBooleans");
			Assert.assertEquals(lengthDFBooleans.toString(), "2", "incorrect number of elements");

			Object lengthDFDates = scope.getValue("lengthDFDates");
			Assert.assertEquals(lengthDFDates.toString(), "2", "incorrect number of elements");

			Object lengthDFTimes = scope.getValue("lengthDFTimes");
			Assert.assertEquals(lengthDFTimes.toString(), "2", "incorrect number of elements");

			Object lengthDFDateTimeZones = scope.getValue("lengthDFDateTimeZones");
			Assert.assertEquals(lengthDFDateTimeZones.toString(), "2", "incorrect number of elements");

			Object lengthDFUris = scope.getValue("lengthDFUris");
			Assert.assertEquals(lengthDFUris.toString(), "2", "incorrect number of elements");

			Object lengthDFPerformers = scope.getValue("lengthDFPerformers");
			Assert.assertEquals(lengthDFPerformers.toString(), "2", "incorrect number of elements");

			Object lengthDFImplicitTexts = scope.getValue("lengthDFImplicitTexts");
			Assert.assertEquals(lengthDFImplicitTexts.toString(), "32", "incorrect number of elements");

			Object dFImplicitTexts = result.get("DFImplicitTexts");
			System.out.println("DFImplicitTexts is " + dFImplicitTexts);
			Assert.assertEquals(dFImplicitTexts,
					"[\"Texts:\",\"abc012\",\"def345\",\"Booleans:\",\"true\",\"false\",\"true\",\"Dates:\",\"1999-04-08T00:00:00.000Z\",\"2001-01-01\",\"1990-04-26\",\"1992-01-06\",\"Times:\",\"2000-01-01T13:48:58.000Z\",\"13:24:57\",\"01:02:55\",\"03:04:56\",\"DateTimeZones:\",\"2019-08-18T18:24:16.343Z\",\"2006-02-14T17:56:36.444Z\",\"1970-01-01T00:00:00.000Z\",\"1999-01-20T05:27:46.555Z\",\"URIs:\",\"http://www.tibco.co.ca/\",\"http://www.tibco.co.de/\",\"http://www.tibco.co.uk/\",\"http://www.tibco.co.in/\",\"Performers:\",\"resource4\",\"resource3\",\"resource1\",\"resource2\"]",
					"incorrect implicit conversion");

			System.out.println(
					"================= Test for implcit type conversion of fixed point numbers and floating point numbers array ====================");

			// @formatter:off
			expression = ScriptManager.createExpression("" 
					+""//Fixed point numbers
					+"data.DFFloatingPointNumbers.push(-123.4567891);"
					+"data.DFFloatingPointNumbers.push(123.45889981);"
					+"var lengthDFFloatingPointNumbers = data.DFFloatingPointNumbers.length;" //2
					+"data.DFFloatingPointNumber = 211.11232;"
					+"data.DFFixedPointNumbers.push(-1.23445);"
					+"data.DFFixedPointNumbers.push(data.DFFloatingPointNumber);"
					+"data.DFFixedPointNumbers.pushAll(data.DFFloatingPointNumbers);"
					+""//numbers
					+"data.DFFloatingPointNumber = 1;"
					+"data.DFNumber = 2000;"
					+"data.DFFloatingPointNumbers.pushAll(data.DFFixedPointNumbers);"
					+"data.DFFloatingPointNumbers.push(-121212.3142131);"
					+"data.DFFloatingPointNumbers.push(data.DFFloatingPointNumber);"
					+"data.DFFloatingPointNumbers.push(data.DFNumber);"
					+ "");
			// @formatter:on
			expression.eval(scope);
			result = scope.getData();

			System.out.println(result.get("DFFixedPointNumbers"));
			Assert.assertEquals(result.get("DFFixedPointNumbers"), "[-1.234,211.112,-123.457,123.459]",
					"incorrect value");

			System.out.println(result.get("DFFloatingPointNumbers"));
			Assert.assertEquals(result.get("DFFloatingPointNumbers"),
					"[-123.4567891,123.45889981,-1.234,211.112,-123.457,123.459,-121212.3142131,1,2000]",
					"incorrect value");
		}
		finally

		{
			forceUndeploy(deploymentId2);
			forceUndeploy(deploymentId1);
		}
	}
}
