package com.bj58.timer.web.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.bj58.timer.web.service.ILogService;

@Service("logService")
public class LogServiceImpl implements ILogService {
    Logger logger = Logger.getLogger(LogServiceImpl.class);

	@Override
	public String[] showLog(String project, String job, String last, String workspace) throws Exception {
		String[] re = new String[2];
		InputStream err = null;
		InputStream out = null;
		BufferedReader readout = null;
		BufferedReader readerr = null;
		Process pro = null;
		try {
			String[] ws = workspace.split(">",-1);
			String ip = ws[0];
			String home = ws[1];
			pro = Runtime.getRuntime().exec(new String[]{"/bin/bash","-x", "/usr/local/tomcat8/webapps/ConfigJobService/tail.sh", ip,home,job,last});  
			boolean isFinish = pro.waitFor(30l, TimeUnit.SECONDS);
			if(!isFinish) {
				re[0] = "查询日志时间超时!\r\n";
				re[1] = last;
				return re;
			}
			int returnCode = pro.exitValue();
			err = pro.getErrorStream();
			out = pro.getInputStream();  
			readout = new BufferedReader(new InputStreamReader(out,"utf-8"));  
			readerr = new BufferedReader(new InputStreamReader(err,"utf-8"));  
			String line = null;  
			String result = "";
			if(returnCode==0){
				boolean startLog = false;
				while((line = readout.readLine())!=null){ 
				  if(line.trim().equals("")){
				    //continue;
				  }
					if(line.startsWith("start_retrieve")){
						startLog = true;
					}
					if(line.startsWith("last_row_number")){
						re[1] = line.substring("last_row_number".length());
						startLog = false;
					}
					if(startLog==true){
						result = result.concat(line).concat("\r\n");
					}
			    }
			}else{
				while((line = readerr.readLine())!=null){  
					logger.error(line); 
			    }
			}
			String tmp = "";
			if(result.indexOf("\r\n") >= 0)
			  tmp = result.substring(result.indexOf("\r\n")+2);
			if(tmp.indexOf("\r\n") >= 0)
			  tmp = tmp.substring(tmp.indexOf("\r\n")+2);
			if(tmp.lastIndexOf("\r\n") >= 0)
				tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
			if(tmp.lastIndexOf("\r\n") >= 0) {
				tmp = tmp.substring(0, tmp.lastIndexOf("\r\n"));
				tmp = tmp.replace("[37", "").replace("[01;31m[K", "").replace("[m[K","");
				//tmp = tmp.replace("echo last_row_number`cat email.log |wc -l` \r\n", "");
				tmp = tmp.replaceAll(".*last_row_number.*", "");
				//tmp = tmp.replace(";40m[[32;40mroot;40m([36;40mhost/bjdhj-176-180;40m)[35;40m@[0mbjdhj-178-27 [35;40mlogs[0m]# echo last_row_number`cat email.log ", "");
				tmp = tmp.replace("exit \r\n", "");
        tmp = Pattern.compile("\n(\\s)*\r").matcher(tmp).replaceAll("");
        tmp = Pattern.compile("\r\n(\\s)*$").matcher(tmp).replaceAll("");
				if(!tmp.trim().equals("")) {
				  tmp = tmp.concat("\r\n");
				} else {
          tmp = "";
				}
			} else {
				tmp = "";
			}
			re[0] = tmp;
		} finally {
			if (pro != null)
				pro.destroy();
			if(err != null)
				err.close();
			if(out != null)
				out.close();
			if(readout != null)
				readout.close();
			if(readerr!= null)
				readerr.close();
		}
        
        return re;
	}

	@Override
	public String execute(String project, String job, String workspace , String command) throws Exception {
		String re = "";
		InputStream err = null;
		InputStream out = null;
		BufferedReader readout = null;
		BufferedReader readerr = null;
		Process pro = null;
		int limit = 1000;
		try {
			String[] ws = workspace.split(">",-1);
			String ip = ws[0];
			String home = ws[1];
			pro = Runtime.getRuntime().exec(new String[]{"/bin/bash","-x", "/usr/local/tomcat8/webapps/ConfigJobService/grep.sh", ip,home,job,command});  
			boolean isFinish = pro.waitFor(90l, TimeUnit.SECONDS);
			if(!isFinish) {
				re = "检索日志时间超时,请缩小检索范围!\r\n";
				return re;
			}
			int returnCode = pro.exitValue();
			err = pro.getErrorStream();
			out = pro.getInputStream();  
			readout = new BufferedReader(new InputStreamReader(out,"utf-8"));  
			readerr = new BufferedReader(new InputStreamReader(err,"utf-8"));  
			String line = null;  
			String result = "";
			int i = 0;
			if(returnCode==0){
				boolean startLog = false;
				while((line = readout.readLine())!=null){ 
					if(i>limit){
						break;
					}
					if(line.trim().equals("")){
            //continue;
          }
					if(line.startsWith("start_execute")){
						startLog = true;
					}
					if(startLog==true){
						result = result.concat(line).concat("\r\n");
					}
					i++;
			    }
			}else{
				while((line = readerr.readLine())!=null){  
					logger.error(line); 
			    }
			}
			//System.out.print(result);
			String tmp = "";
			if(result.indexOf("\r\n") >= 0)
			   tmp = result.substring(result.indexOf("\r\n")+2);
			if(tmp.indexOf("\r\n") >= 0)
			  tmp = tmp.substring(tmp.indexOf("\r\n")+2);
			
			tmp = tmp.replace("[A\r\n", "");
			
			if(i<=limit){
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
				      if(tmp.equals("exit ")){
				        tmp = tmp.replace("exit ", "");
				      }
				      if(!tmp.trim().equals("")) {
			          tmp = tmp.concat("\r\n");
			        } else {
			          tmp = "not found!\r\n";
			        }
				  } else {
					  tmp = "not found!\r\n";
				  }
			}
			
			re = tmp;
		} finally {
		   if(pro != null)
			 pro.destroy();
		   if(err != null)
			 err.close();
		   if(out != null)
			 out.close();
		   if(readout != null)
			 readout.close();
		   if(readerr != null)
			 readerr.close();
		}
        
        return re;
	}
}
