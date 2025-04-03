/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CLN 
{
    
    private String event = null;
    private List<String> caseReferences = new ArrayList<String>() ;

    
    /**
     */
    @JsonProperty("event")
    public String getEvent()
    {
        return event;
    }

    public void setEvent(String aValue)
    {
        event = aValue;
    }
    
    /**
     */
    @JsonProperty("caseReferences")
    public List<String> getCaseReferences()
    {
        return caseReferences;
    }

    public void setCaseReferences(List<String> aValue)
    {
        caseReferences = aValue;
    }
    

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CLN[");
        sb.append(", event=").append(event);
        sb.append(", caseReferences=").append(caseReferences);
        sb.append("]");
        return sb.toString();
    }
}
