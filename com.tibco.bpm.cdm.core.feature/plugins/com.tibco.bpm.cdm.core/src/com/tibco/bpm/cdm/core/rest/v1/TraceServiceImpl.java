package com.tibco.bpm.cdm.core.rest.v1;

import com.tibco.bpm.cdm.api.rest.v1.api.TraceService;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostResponseBody;
import com.tibco.bpm.cdm.core.AbstractService;
import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.cdm.core.tracing.InstrumentationConfig;

import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;

import javax.ws.rs.core.Response;

public class TraceServiceImpl extends AbstractService implements TraceService {

	static CLFClassContext logCtx = CloudLoggingFramework.init(TraceServiceImpl.class, CDMLoggingInfo.instance);
	InstrumentationConfig instrumentationConfig;

	public void setIConfig(InstrumentationConfig instrumentationConfig) {
		CLFMethodContext clf = logCtx.getMethodContext("setIConfig");
		this.instrumentationConfig = instrumentationConfig;
		clf.local.trace("setIConfig()");
	}

	@Override
	public Response toggleTrue() {
		CLFMethodContext clf = logCtx.getMethodContext("toggleTrue");
		CasesPostResponseBody responseBody = new CasesPostResponseBody();
		instrumentationConfig.setEnabled(true);
		responseBody.add("Enabled");
		clf.local.trace("responseBody: " + responseBody);
		return Response.ok(responseBody).build();
	}

	@Override
	public Response toggleFalse() {
		CLFMethodContext clf = logCtx.getMethodContext("toggleFalse");
		CasesPostResponseBody responseBody = new CasesPostResponseBody();
		instrumentationConfig.setEnabled(false);
		responseBody.add("Disabled");
		clf.local.trace("responseBody: " + responseBody);
		return Response.ok(responseBody).build();
	}

	@Override
	public Boolean getStatus() {
		CLFMethodContext clf = logCtx.getMethodContext("getStatus");
		clf.local.trace("status : " + instrumentationConfig.isEnabled());
		return instrumentationConfig.isEnabled();
	}
}