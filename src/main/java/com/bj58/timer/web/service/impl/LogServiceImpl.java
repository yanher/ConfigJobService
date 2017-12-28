package com.bj58.timer.web.service.impl;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bj58.timer.web.common.LogThreadPool;
import com.bj58.timer.web.service.ILogService;
import com.bj58.timer.web.tasks.GrepTask;
import com.bj58.timer.web.tasks.TailTask;

@Service("logService")
public class LogServiceImpl implements ILogService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String[] showLog(String project, String job, String last, String workspace) throws Exception {
	  String[] ws = workspace.split(">",-1);
    String ip = ws[0];
    String home = ws[1];
    String[] re = new String[2];
    
	  Future<String[]> future = LogThreadPool.showExec.submit(new TailTask(ip,home,job,last));
	  try {
      re = future.get(2L, TimeUnit.SECONDS);
      //logger.info("tail日志,返回结果为[msg={},lastNum={}]",re[0],re[1]);
    } catch (CancellationException |ExecutionException | InterruptedException e) {
      logger.error("查询日志出现异常!");
      e.printStackTrace();
      future.cancel(true);
      re[0] = "查询日志出现异常!\r\n";
      re[1] = "0";
    } catch (TimeoutException ex) {
      logger.error("查询日志时间超时!");
      ex.printStackTrace();
      future.cancel(true);
      re[0] = "查询日志时间超时!\r\n";
      re[1] = "0";
    }
	  
    return re;
	}

	@Override
	public String execute(String project, String job, String workspace , String command) throws Exception {
	  String[] ws = workspace.split(">",-1);
    String ip = ws[0];
    String home = ws[1];
    String re = "";
	  
    Future<String> future = LogThreadPool.showExec.submit(new GrepTask(ip,home,job,command));
    
    try {
      re = future.get(2L, TimeUnit.SECONDS);
      //logger.info("grep日志,返回结果为msg={}",re);
    } catch (CancellationException |ExecutionException | InterruptedException e) {
      logger.error("检索日志出现异常!");
      e.printStackTrace();
      future.cancel(true);
      re = "检索日志出现异常!\r\n";
    } catch (TimeoutException ex) {
      logger.error("检索日志时间超时!");
      ex.printStackTrace();
      future.cancel(true);
      re = "检索日志时间超时,请缩小检索范围!\r\n";
    }
    return re;
	}
}
