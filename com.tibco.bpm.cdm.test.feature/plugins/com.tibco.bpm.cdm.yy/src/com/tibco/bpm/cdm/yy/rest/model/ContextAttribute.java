/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.model;


import com.fasterxml.jackson.annotation.JsonProperty;


public class ContextAttribute 
{
    
    private String name = null;
    private String value = null;

    
    /**
     */
    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    public void setName(String aValue)
    {
        name = aValue;
    }
    
    /**
     */
    @JsonProperty("value")
    public String getValue()
    {
        return value;
    }

    public void setValue(String aValue)
    {
        value = aValue;
    }
    

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ContextAttribute[");
        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append("]");
        return sb.toString();
    }
}
