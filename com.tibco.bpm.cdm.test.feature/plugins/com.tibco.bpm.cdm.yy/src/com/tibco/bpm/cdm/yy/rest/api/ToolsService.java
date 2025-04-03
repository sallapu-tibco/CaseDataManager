/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.api;

import com.tibco.bpm.cdm.yy.rest.model.Error;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;


/**
 * @GENERATED this is generated code; do not edit.
 */
@Path("/tools")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ToolsService
{
	/**
     * Sets the modification timestamp of the given case to the given ISO-8601 value
     * <p>

     * @param caseReference 
     * @param modificationTimestamp 
     *
     * @GENERATED this is generated code; do not edit.
     */
 	@GET
	@Path("/setModificationTimestamp")
	
	
	public Response toolsSetModificationTimestampGet(@QueryParam("caseReference") String caseReference,
	@QueryParam("modificationTimestamp") String modificationTimestamp) throws Exception;

}

