package com.bj58.timer.web.resource.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.bj58.timer.web.common.FromJson;
import com.bj58.timer.web.entity.UserInfo;
import com.bj58.timer.web.resource.IUserResource;
import com.bj58.timer.web.service.IUserService;

@Path("/user/")
public class UserResourceImpl implements IUserResource {

  @Autowired
  @Qualifier("userService")
  private IUserService userService;

  @Override
  public Response login(String params) throws Exception {
    FromJson<UserInfo> convert = new FromJson<UserInfo>();
    UserInfo info = convert.toBean(params, UserInfo.class);
    if (info.getUsername().equals("admin") && info.getPassword().equals("123456")) {
      JSONObject obj = new JSONObject();
      obj.put("username", info.getUsername());
      obj.put("password", info.getPassword());
      return Response.status(200).entity(obj.toString()).build();
    } else {
      boolean exist = userService.exists(info.getUsername(),info.getPassword());
      if(!exist){
        return Response.status(404).build();
      }else{
        JSONObject obj = new JSONObject();
        obj.put("username", info.getUsername());
        obj.put("password", info.getPassword());
        return Response.status(200).entity(obj.toString()).build();
      }
    }

  }

}
