/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.model;

import com.tibco.bpm.cdm.yy.rest.model.CLNsObserverInfo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CLNsMessagesGetResponseBody 
{
    
    private CLNsObserverInfo all = null;
    private CLNsObserverInfo updated = null;
    private CLNsObserverInfo deleted = null;
    private CLNsObserverInfo updatedAndDeleted = null;

    
    /**
     */
    @JsonProperty("all")
    public CLNsObserverInfo getAll()
    {
        return all;
    }

    public void setAll(CLNsObserverInfo aValue)
    {
        all = aValue;
    }
    
    /**
     */
    @JsonProperty("updated")
    public CLNsObserverInfo getUpdated()
    {
        return updated;
    }

    public void setUpdated(CLNsObserverInfo aValue)
    {
        updated = aValue;
    }
    
    /**
     */
    @JsonProperty("deleted")
    public CLNsObserverInfo getDeleted()
    {
        return deleted;
    }

    public void setDeleted(CLNsObserverInfo aValue)
    {
        deleted = aValue;
    }
    
    /**
     */
    @JsonProperty("updatedAndDeleted")
    public CLNsObserverInfo getUpdatedAndDeleted()
    {
        return updatedAndDeleted;
    }

    public void setUpdatedAndDeleted(CLNsObserverInfo aValue)
    {
        updatedAndDeleted = aValue;
    }
    

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CLNsMessagesGetResponseBody[");
        sb.append(", all=").append(all);
        sb.append(", updated=").append(updated);
        sb.append(", deleted=").append(deleted);
        sb.append(", updatedAndDeleted=").append(updatedAndDeleted);
        sb.append("]");
        return sb.toString();
    }
}
