package com.bj58.timer.web.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface IUserResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(final String params) throws Exception;

}
