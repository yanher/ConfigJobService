package com.bj58.timer.web.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface ILogResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response showLog(@QueryParam("project") final String project , 
    		@QueryParam("job") final String job , 
    		@QueryParam("last") final String last,
    		@QueryParam("workspace") final String workspace) throws Exception;
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response execute(@QueryParam("project") final String project , 
    		@QueryParam("job") final String job , 
    		@QueryParam("workspace") final String workspace, 
    		@QueryParam("command") final String command) throws Exception;
}
