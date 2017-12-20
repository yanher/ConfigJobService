package com.bj58.timer.web.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bj58.timer.web.common.JePool;
import com.bj58.timer.web.common.JedisUtil;
import com.bj58.timer.web.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
    Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    JePool jePool;
    @Override
    public List<String> getProject(String username,String password) throws Exception {
      return JedisUtil.getAllList(jePool, username+":"+password);
    }
    @Override
    public boolean exists(String username, String password) throws Exception {
      return JedisUtil.exists(jePool, username+":"+password);
    }
}