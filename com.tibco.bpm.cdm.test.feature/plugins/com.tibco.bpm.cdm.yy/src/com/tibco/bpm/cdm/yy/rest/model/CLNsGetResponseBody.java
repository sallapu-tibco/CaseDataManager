/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.model;


import com.fasterxml.jackson.annotation.JsonProperty;


public class CLNsGetResponseBody 
{
    
    private Boolean enabled = null;

    
    /**
     */
    @JsonProperty("enabled")
    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean aValue)
    {
        enabled = aValue;
    }
    

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CLNsGetResponseBody[");
        sb.append(", enabled=").append(enabled);
        sb.append("]");
        return sb.toString();
    }
}
