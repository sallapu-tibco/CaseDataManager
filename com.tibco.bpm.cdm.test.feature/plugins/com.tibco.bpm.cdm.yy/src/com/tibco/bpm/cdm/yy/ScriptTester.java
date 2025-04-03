package com.tibco.bpm.cdm.yy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tibco.bpm.se.api.Expression;
import com.tibco.bpm.se.api.Scope;
import com.tibco.bpm.se.api.ScriptManager;

public class ScriptTester
{

	public ScriptEngineInvoker invoker;

	public ScriptEngineInvoker getInvoker()
	{
		return invoker;
	}

	public void setInvoker(ScriptEngineInvoker invoker)
	{
		this.invoker = invoker;

		try
		{
			System.out.println("about to invoke scriptengine");
			String variableDescriptor = YYUtil
					.readFileContents("com/tibco/bpm/cdm/yy/resources/ProcessSimpleDataProcess.js");

			List<String> descriptors = new ArrayList<String>();
			descriptors.add(variableDescriptor);

			long t1 = System.currentTimeMillis();
			Scope scope = invoker.scriptEngine.createScope(null, descriptors);
			scope.setData(null, "com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess");
			long t2 = System.currentTimeMillis();

			System.out.println("Time taken to create scope first time " + (t2 - t1));

			// @formatter:off
			Expression expression = ScriptManager
					.createExpression(""
							+"data.booleanParam = true;"
							+"data.dateParam = new Date();"
							+"data.dateTimeTZParam = new Date();"
							+"data.numberParam = 10;"
							+"data.performerParam = 'performer';"
							+"data.textParam = 'text';"
							+"data.timeParam = new Date();"
							+"data.textArrayParam.push('text1');"
							+"data.textArrayParam.push('text2');"
							);
			// @formatter:on

			t1 = System.currentTimeMillis();
			Object val = expression.eval(scope);
			t2 = System.currentTimeMillis();
			System.out.println("Time taken to do eval for first time" + (t2 - t1));

			Object a = scope.getValue("a");
			System.out.println(a);
			Map<String, Object> result = scope.getData();

			System.out.println("=========================================================");
			System.out.println("booleanParameter is: " + result.get("booleanParam"));
			System.out.println("dateParameter is: " + result.get("dateParam"));
			System.out.println("dateTimeTZParam is: " + result.get("dateTimeTZParam"));
			System.out.println("numberParam is: " + result.get("numberParam"));
			System.out.println("performerParam is: " + result.get("performerParam"));
			System.out.println("textParam is: " + result.get("textParam"));
			System.out.println("timeParam is: " + result.get("timeParam"));
			System.out.println("textArrayParam is: " + result.get("textArrayParam"));

			t1 = System.currentTimeMillis();
			scope = invoker.scriptEngine.createScope(null, descriptors);
			scope.setData(null, "com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess");
			t2 = System.currentTimeMillis();
			//Sample Metric: Time taken to create scope second time 4896
			System.out.println("Time taken to create scope second time" + (t2 - t1));

			scope.clearAllVariableState();
			t1 = System.currentTimeMillis();
			scope.setData(null, "com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess");
			expression.eval(scope);
			t2 = System.currentTimeMillis();
			System.out.println("Time taken to do eval for second time" + (t2 - t1));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		ScriptTester tester = new ScriptTester();
		tester.setInvoker(null);
	}

}
