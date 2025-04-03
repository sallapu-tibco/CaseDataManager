package com.tibco.bpm.cdm.api.rest.v1.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/trace")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TraceService {

    @POST
    @Path("/enable")
    public Response toggleTrue();

    @POST
    @Path("/disable")
    public Response toggleFalse();

    @GET
    public Boolean getStatus();
}