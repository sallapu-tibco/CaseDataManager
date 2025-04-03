package com.tibco.bpm.cdm.yy;

import com.tibco.bpm.se.api.ScriptEngineService;

public class ScriptEngineInvoker
{

	public ScriptEngineService scriptEngine = null;

	public ScriptEngineService getScriptEngine()
	{
		return scriptEngine;
	}

	public void setScriptEngine(ScriptEngineService scriptEngine)
	{
		this.scriptEngine = scriptEngine;
	}
}
