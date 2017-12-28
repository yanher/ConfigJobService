package com.bj58.timer.web.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrepTask implements Callable<String> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String ip;
    private String home;
    private String job;
    private String command;

    public GrepTask(String ip, String home, String job, String command) {
        this.ip = ip;
        this.home = home;
        this.job = job;
        this.command = command;
    }

    public String call() {
      
      log.info("开始grep日志线程,params=[{},{},{},{}]", ip,home,job,command);
      String re = "";
      InputStream out = null;
      BufferedReader readout = null;
      Process pro = null;
      try {
        pro = Runtime.getRuntime().exec(new String[]{"/bin/bash","-x", "/usr/local/tomcat8/webapps/ConfigJobService/grep.sh", ip,home,job,command});  
        out = pro.getInputStream();  
        readout = new BufferedReader(new InputStreamReader(out,"utf-8"));   
        String line = null;  
        String result = "";
        boolean startLog = false;
        while((line = readout.readLine())!=null){ 
          if(Thread.interrupted()) {
            throw new TimeoutException();
          }
          if(line.startsWith("start_execute")){
            startLog = true;
          }
          if(startLog==true){
            result = result.concat(line).concat("\r\n");
          }
        }
        String tmp = "";
        if(result.indexOf("\r\n") >= 0)
           tmp = result.substring(result.indexOf("\r\n")+2);
        if(tmp.indexOf("\r\n") >= 0)
          tmp = tmp.substring(tmp.indexOf("\r\n")+2);
        
        tmp = tmp.replace("[A\r\n", "");
        
        if(tmp.lastIndexOf("\r\n") >= 0)
          tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
        if(tmp.lastIndexOf("\r\n") >= 0)
          tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
        if(tmp.lastIndexOf("\r\n") >= 0)
          tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
        if(tmp.lastIndexOf("\r\n") >= 0)
          tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
        if(tmp.lastIndexOf("\r\n") >= 0) {
          tmp = tmp.substring(0, tmp.lastIndexOf("\r\n")+2);
          tmp = tmp.replace("[37", "").replace("[01;31m[K", "").replace("[m[K","");
          tmp = Pattern.compile("\n(\\s)*\r").matcher(tmp).replaceAll("");
          tmp = Pattern.compile("\r\n(\\s)*$").matcher(tmp).replaceAll("");
          tmp = tmp.replace("exit \r\n", "");
          tmp = tmp.replace("exit  \r\n", "");
          if(tmp.trim().equals("exit")){
             tmp = "";
          }
          if(!tmp.trim().equals("")) {
             tmp = tmp.concat("\r\n");
          } else {
             tmp = "not found!\r\n";
          }
        } else {
          tmp = "not found!\r\n";
        }
        re = tmp;
      } catch (TimeoutException ex) {
        log.error("grep任务执行超时,被中断!", ex);
        re = "";
      } catch (Exception e) {
        log.error("grep任务日志信息出错", e);
        re = "";
      } finally {
        log.info("开始释放资源");
        release(pro, out, readout);
      }
      return re;
    }
    
    public void release(Process pro, InputStream out, BufferedReader readout) {
      if(pro != null)
        pro.destroy();
        if(out != null){
          try {
            out.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if(readout != null) {
          try {
            readout.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
    }
}