package com.bj58.timer.web;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
@ApplicationPath("/webapi/*") 
public class AirResourceConfig extends ResourceConfig {
    public AirResourceConfig() {
        packages("com.bj58.timer.web");
        //register(MultiPartFeature.class);
    }
}
