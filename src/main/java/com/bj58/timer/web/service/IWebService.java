package com.bj58.timer.web.service;

import java.util.List;
import com.bj58.timer.web.entity.JobInfo;


public interface IWebService {

    public List<JobInfo> getJobInfoList(List<String> projects) throws Exception;
    
    public List<JobInfo> getJobInfoList() throws Exception;
    
    public boolean updateJobCron(JobInfo info) throws Exception;
    
    public boolean stopJob(String project,String job) throws Exception;
    
    public boolean startJob(JobInfo info) throws Exception;
}
