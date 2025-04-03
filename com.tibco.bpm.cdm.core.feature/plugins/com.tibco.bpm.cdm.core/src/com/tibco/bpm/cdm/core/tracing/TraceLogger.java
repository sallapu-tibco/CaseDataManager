package com.tibco.bpm.cdm.core.tracing;

import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;

public class TraceLogger {
    
    private static CLFClassContext logCtx = CloudLoggingFramework.init(TraceLogger.class, CDMLoggingInfo.instance);
    
	public static void debug(String message) {
	    CLFMethodContext clf = logCtx.getMethodContext("debug"); 
	    clf.local.debug(message);
	}
	
	public static void error(String message,Throwable t) {
		t.printStackTrace();
		CLFMethodContext clf = logCtx.getMethodContext("error"); 
        clf.local.error(message);
	}
	public static void info(String message,Object msg) {
		CLFMethodContext clf = logCtx.getMethodContext("info"); 
        clf.local.info(message, msg);
	}

}