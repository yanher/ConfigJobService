package com.bj58.timer.web.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
@Component
public class JedisUtil {
  
  public static String get(JePool pool,String key) {
    try (Jedis c = pool.getResource()) {
      return c.get(key);
    }
  }

  public static void set(JePool pool,String key, String value) {
    try (Jedis c = pool.getResource()) {
      c.set(key, value);
    }
  }
  
  public static void setWithOther(JePool pool, String key, String value, String okey, String ovalue) {
	    set(pool,key,value.concat(String.format(":%s:%s", okey , ovalue)));
  }
  
  public static void setex(JePool pool,String key, int timeout, String value) {
    try (Jedis c = pool.getResource()) {
      c.setex(key, timeout, value);
    }
  }

  public static void del(JePool pool,String key) {
    try (Jedis c = pool.getResource()) {
      c.del(key);
    }
  }
  
	public static Map<String,String> loopJob(JePool pool) {
		try (Jedis c = pool.getResource()) {
			Map<String,String> resultMap = new HashMap<String,String>();
			ScanParams params = new ScanParams();
			params.match("project:*");
			ScanResult<String> scanResult = c.scan("0", params);
			List<String> keys = scanResult.getResult();
			String nextCursor = scanResult.getStringCursor();
			while (true) {
				for (String key : keys) {
					resultMap.put(key, c.get(key));
				}
				if (nextCursor.equals("0")) {
					break;
				}
				scanResult = c.scan(nextCursor, params);
				nextCursor = scanResult.getStringCursor();
				keys = scanResult.getResult();
			}
			return resultMap;
		}
	}
	
	public static List<String> getAllList(JePool pool,String key) {
    try (Jedis c = pool.getResource()) {
      return c.lrange(key, 0, -1);
    }
  }
	
	public static boolean exists(JePool pool,String key) {
    try (Jedis c = pool.getResource()) {
      return c.exists(key);
    }
  }
}

