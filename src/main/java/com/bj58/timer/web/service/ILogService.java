package com.bj58.timer.web.service;



public interface ILogService {

    public String[] showLog(String project,String job,String last,String workspace) throws Exception;

    public String execute(String project, String job, String workspace, String command) throws Exception;

}