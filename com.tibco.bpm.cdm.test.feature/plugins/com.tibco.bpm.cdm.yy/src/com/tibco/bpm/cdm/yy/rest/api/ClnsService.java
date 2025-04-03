/*
 * Copyright (c) TIBCO Software Inc 2004 - 2016. All rights reserved.
 *
 */

package com.tibco.bpm.cdm.yy.rest.api;

import com.tibco.bpm.cdm.yy.rest.model.Error;
import com.tibco.bpm.cdm.yy.rest.model.CLNsGetResponseBody;
import com.tibco.bpm.cdm.yy.rest.model.CLNsMessagesGetResponseBody;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;


/**
 * @GENERATED this is generated code; do not edit.
 */
@Path("/clns")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ClnsService
{
	/**
     * Fetch state of CLN capturer, optionally enabling/disabling it first according to the &#39;enabled&#39; query parameter.
     * <p>

     * @param enabled 
     *
     * @GENERATED this is generated code; do not edit.
     */
 	@GET
	
	
	
	public Response clnsGet(@QueryParam("enabled") Boolean enabled) throws Exception;

	/**
     * Fetch all captured CLNs and remove them from the server
     * <p>


     *
     * @GENERATED this is generated code; do not edit.
     */
 	@GET
	@Path("/messages")
	
	
	public Response clnsMessagesGet() throws Exception;

}

