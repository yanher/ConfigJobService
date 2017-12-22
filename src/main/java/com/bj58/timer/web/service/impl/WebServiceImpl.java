package com.bj58.timer.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bj58.timer.web.common.JePool;
import com.bj58.timer.web.common.JedisUtil;
import com.bj58.timer.web.common.MqPublisher;
import com.bj58.timer.web.entity.JobInfo;
import com.bj58.timer.web.service.IWebService;
import com.rabbitmq.client.ConnectionFactory;

@Service("webService")
public class WebServiceImpl implements IWebService {
    Logger logger = Logger.getLogger(WebServiceImpl.class);
    
    @Autowired
    JePool jePool;
    @Autowired
    ConnectionFactory mqConnectionFactory;
    
	@Override
	public List<JobInfo> getJobInfoList(List<String> projects) throws Exception {
		Map<String,String> map = JedisUtil.loopJob(jePool);
		List<JobInfo> list = new ArrayList<JobInfo>();
		for(Map.Entry<String, String> entry : map.entrySet()){
			String[] keys = entry.getKey().split(":",-1);
		  String project = keys[1];
		  if(projects.contains(project)){
		    String[] values = entry.getValue().split(":",-1);
	      JobInfo info = new JobInfo();
	      info.setCron(values[1]).setDelay(values[3]).setLastStatus(values[7]).setNextTime(values[5])
	          .setPhoneNum(values[9]).setProject(keys[1]).setJob(keys[3]).setRunning(values[11]).setWorkspace(values[13]);
	      list.add(info);
		  }
		}
		return list;
	}

	@Override
	public boolean updateJobCron(JobInfo info) throws Exception {
		try {
			String key = String.join(":", "project",info.getProject(),"job",info.getJob());
			String value = JedisUtil.get(jePool,key);
			String[] values = value.split(":",-1);
			values[1] = info.getCron();
			values[3] = info.getDelay();
			values[9] = info.getPhoneNum();
			value = String.join(":", values);
			JedisUtil.set(jePool,key, value);
			
	    String route = info.getProject() + "_" + info.getJob();
	    MqPublisher.publish("start", mqConnectionFactory, route);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stopJob(String project, String job) throws Exception {
		try {
			String key = String.join(":", "project",project,"job",job);
			String value = JedisUtil.get(jePool,key);
			String[] values = value.split(":",-1);
			values[5] = "";
			values[11] = "false";
			value = String.join(":", values);
			JedisUtil.set(jePool,key, value);
			
	    String route = project + "_" + job;
	    MqPublisher.publish("end", mqConnectionFactory, route);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean startJob(JobInfo info) throws Exception {
		try {
			String key = String.join(":", "project",info.getProject(),"job",info.getJob());
			String value = JedisUtil.get(jePool,key);
			String[] values = value.split(":",-1);
			values[1] = info.getCron();
			values[3] = info.getDelay();
			values[9] = info.getPhoneNum();
			values[11] = "true";
			value = String.join(":", values);
			JedisUtil.set(jePool,key, value);
			
	    String route = info.getProject() + "_" + info.getJob();
	    MqPublisher.publish("start", mqConnectionFactory, route);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

  @Override
  public List<JobInfo> getJobInfoList() throws Exception {
    Map<String,String> map = JedisUtil.loopJob(jePool);
    List<JobInfo> list = new ArrayList<JobInfo>();
    for(Map.Entry<String, String> entry : map.entrySet()){
      String[] keys = entry.getKey().split(":",-1);
      String[] values = entry.getValue().split(":",-1);
      JobInfo info = new JobInfo();
      info.setCron(values[1]).setDelay(values[3]).setLastStatus(values[7]).setNextTime(values[5])
          .setPhoneNum(values[9]).setProject(keys[1]).setJob(keys[3]).setRunning(values[11]).setWorkspace(values[13]);
      list.add(info);
    }
    return list;
  }

}
