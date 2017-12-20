

package com.bj58.timer.web.resource.impl;

import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.bj58.timer.web.common.FromJson;
import com.bj58.timer.web.common.ToJson;
import com.bj58.timer.web.entity.JobInfo;
import com.bj58.timer.web.resource.IWebResource;
import com.bj58.timer.web.service.IUserService;
import com.bj58.timer.web.service.IWebService;

@Path("/jobinfo/")
public class WebResourceImpl implements IWebResource {
    
    protected String basePath;
    
    @Autowired
    @Qualifier("webService")
    private IWebService webService;
    
    @Autowired
    @Qualifier("userService")
    private IUserService userService;
    
	@Override
	public Response getJobInfoList(String userName,String password) throws Exception {
	  List<JobInfo> list = null;
	  if(userName.equalsIgnoreCase("admin")){
	    list = webService.getJobInfoList();
	  }else{
	    List<String> projects = userService.getProject(userName,password);
	    list = webService.getJobInfoList(projects);
	  }
		ToJson converter = new ToJson();
		JSONArray json = converter.toJSONArray(list);
		return Response.status(200).entity(json.toString()).build();
	}

	@Override
	public Response updateJobCron(final String params) throws Exception {
		FromJson<JobInfo> convert = new FromJson<JobInfo>();       
        JobInfo info  = convert.toBean(params,JobInfo.class);
        boolean result = webService.updateJobCron(info);
        if(result){
        	return Response.status(200).build();
        }else{
        	return Response.status(500).build();
        }
	}

	@Override
	public Response stopJob(final String project,final String job) throws Exception {
		boolean result = webService.stopJob(project,job);
        if(result){
        	return Response.status(200).build();
        }else{
        	return Response.status(500).build();
        }
	}

	@Override
	public Response startJob(final String params) throws Exception {
		FromJson<JobInfo> convert = new FromJson<JobInfo>();       
        JobInfo info  = convert.toBean(params,JobInfo.class);
		boolean result = webService.startJob(info);
        if(result){
        	return Response.status(200).build();
        }else{
        	return Response.status(500).build();
        }
	}
    

}
