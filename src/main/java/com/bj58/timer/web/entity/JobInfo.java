package com.bj58.timer.web.entity;

import java.io.Serializable;

public class JobInfo implements Serializable{
	private static final long serialVersionUID = 1L;  
	
	private String project;
	private String job;
	private String cron;
    private String delay;
    private String nextTime;
    private String lastStatus;
	private String phoneNum;
	private String running;
	private String workspace;
	
	public String getWorkspace() {
		return workspace;
	}
	public JobInfo setWorkspace(String workspace) {
		this.workspace = workspace;
		return this;
	}
	
	public String getCron() {
		return cron;
	}
	public JobInfo setCron(String cron) {
		this.cron = cron;
		return this;
	}
	public String getDelay() {
		return delay;
	}
	public JobInfo setDelay(String delay) {
		this.delay = delay;
		return this;
	}
	public String getNextTime() {
		return nextTime;
	}
	public JobInfo setNextTime(String nextTime) {
		this.nextTime = nextTime;
		return this;
	}
	public String getLastStatus() {
		return lastStatus;
	}
	public JobInfo setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
		return this;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public JobInfo setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
		return this;
	}
	public String getProject() {
		return project;
	}
	public JobInfo setProject(String project) {
		this.project = project;
		return this;
	}
	public String getJob() {
		return job;
	}
	public JobInfo setJob(String job) {
		this.job = job;
		return this;
	}
	public String getRunning() {
		return running;
	}
	public JobInfo setRunning(String running) {
		this.running = running;
		return this;
	}
    
}
