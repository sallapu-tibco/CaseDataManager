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
@Path("/autoPurge")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface AutoPurgeService
{
	/**
     * Cancels top-level Auto Purge jobs
     * <p>


     *
     * @GENERATED this is generated code; do not edit.
     */
 	@GET
	@Path("/cancelJobs")
	
	
	public Response autoPurgeCancelJobsGet() throws Exception;

	/**
     * Creates a new top-level Auto Purge job, set to run after the specified delay (seconds)
     * <p>

     * @param delay 
     *
     * @GENERATED this is generated code; do not edit.
     */
 	@GET
	@Path("/createJob")
	
	
	public Response autoPurgeCreateJobGet(@QueryParam("delay") Integer delay) throws Exception;

}

