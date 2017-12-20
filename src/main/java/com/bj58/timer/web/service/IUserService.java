package com.bj58.timer.web.service;

import java.util.List;

public interface IUserService {
  public List<String> getProject(String username,String password) throws Exception;
  public boolean exists(String username,String password) throws Exception;
}