package com.bj58.timer.web.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface IWebResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobInfoList(@QueryParam("userName") final String userName,@QueryParam("password") final String password) throws Exception;
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateJobCron(final String params) throws Exception;
    
    @DELETE
    public Response stopJob(@QueryParam("project") final String project , @QueryParam("job") final String job) throws Exception;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startJob(final String params) throws Exception;

}
