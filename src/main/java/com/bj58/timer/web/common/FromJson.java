package com.bj58.timer.web.common;

import com.fasterxml.jackson.databind.ObjectMapper;


public class FromJson<T> {
 public String[] toArray(String jasonString) throws Exception{
     
     String[] sArray = null;
     int start = jasonString.indexOf(":") + 2;
     int end = jasonString.lastIndexOf("\"");
     String ids = jasonString.substring(start, end);
     sArray = ids.split(",");
     return sArray;
 }
 
 public T toBean(String jasonString,Class<T> T) throws Exception{
     ObjectMapper objectMapper = new ObjectMapper();
     T bean = (T) objectMapper.readValue(jasonString, T);
     return bean;
 }
 
}
