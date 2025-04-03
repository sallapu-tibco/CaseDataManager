/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.model;

import com.tibco.bpm.cdm.yy.rest.model.CLN;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CLNsObserverInfo 
{
    
    private List<CLN> clns = new ArrayList<CLN>() ;

    
    /**
     */
    @JsonProperty("clns")
    public List<CLN> getClns()
    {
        return clns;
    }

    public void setClns(List<CLN> aValue)
    {
        clns = aValue;
    }
    

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CLNsObserverInfo[");
        sb.append(", clns=").append(clns);
        sb.append("]");
        return sb.toString();
    }
}
