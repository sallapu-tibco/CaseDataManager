package com.tibco.bpm.cdm.core.tracing;

import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;

public class InstrumentationConfig {
   
    static CLFClassContext logCtx = CloudLoggingFramework.init(InstrumentationConfig.class, CDMLoggingInfo.instance);

    private volatile boolean enabled;

    public void init() {
        
        CLFMethodContext clf = logCtx.getMethodContext("init");
        clf.local.debug("Initializing InstrumentationConfig with enabled= "+ enabled);
        System.out.println("InstrumentationConfig.init()"+ enabled);
    }

    public boolean isEnabled() {
    	CLFMethodContext clf = logCtx.getMethodContext("isEnabled");
    	clf.local.debug("Instrumentation: ",enabled);
    	System.out.println("Instrumentation from isEnabled: "+enabled);
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            CLFMethodContext clf = logCtx.getMethodContext("setEnabled");
            System.out.println("Instrumentation state changed to: "+( enabled ? "enabled" : "disabled"));
            clf.local.debug("Instrumentation state changed to: {}", enabled ? "enabled" : "disabled");
            this.enabled = enabled;
        }
    }
}