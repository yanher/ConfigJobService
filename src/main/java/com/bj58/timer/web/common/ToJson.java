package com.bj58.timer.web.common;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;



public class ToJson {
   public <T> JSONArray toJSONArray(List<T> rs) throws Exception{
     JSONArray json = new JSONArray();
     Iterator<T> it = rs.iterator();    
     while(it.hasNext()){
         JSONObject obj = new JSONObject();
         T bean = it.next();

         //@SuppressWarnings("unchecked")
         //Class<T> clazz = (Class<T>)(bean.getClass().getSuperclass());     //得到父类     
         Class<T> clazz = (Class<T>) bean.getClass();
         Field[] fields = clazz.getDeclaredFields();

         for(Field field : fields){ 
             field.setAccessible(true);
             if(field.getGenericType().toString().equalsIgnoreCase("class java.lang.Integer")){
                 Object value = clazz.getDeclaredMethod("get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1, field.getName().length())).invoke(bean);
                 obj.put(field.getName(), value==null?"":value);
             }else if(field.getGenericType().toString().equalsIgnoreCase("class java.lang.String")){
                 Object value = clazz.getDeclaredMethod("get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1, field.getName().length())).invoke(bean);
                 obj.put(field.getName(), value==null?"":value);
             }
         }
         json.put(obj);
     }
     return json;
 }

}
