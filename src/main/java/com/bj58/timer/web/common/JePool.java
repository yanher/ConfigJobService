package com.bj58.timer.web.common;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;

@Component
public class JePool extends JedisPool{
   
   public JePool(GenericObjectPoolConfig config,String host,int port,int timeout,String auth,int database){
	   super(config, host, port,timeout, auth,database);
   }

}
