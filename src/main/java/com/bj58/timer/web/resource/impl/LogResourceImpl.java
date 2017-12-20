

package com.bj58.timer.web.resource.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bj58.timer.web.resource.ILogResource;
import com.bj58.timer.web.service.ILogService;

@Path("/log/")
public class LogResourceImpl implements ILogResource {
    
    protected String basePath;
    
    @Autowired
    @Qualifier("logService")
    private ILogService logService;

	@Override
	public Response showLog(final String project,final String job,final String last,final String workspace) throws Exception {

		String[] result = logService.showLog(project,job,last,workspace);
	    JSONArray json = new JSONArray();
	    JSONObject obj1 = new JSONObject();
	    obj1.put("text", result[0]);
	    json.put(obj1);
	    JSONObject obj2 = new JSONObject();
	    obj2.put("last", result[1]);
	    json.put(obj2);

      return Response.status(200).entity(json.toString()).build();
	}

	@Override
	public Response execute(final String project,final String job,final String workspace,final String command) throws Exception {
		String result = logService.execute(project, job, workspace, command);
	    JSONObject obj = new JSONObject();
	    obj.put("text", result);
		return Response.status(200).entity(obj.toString()).build();
	}

}
