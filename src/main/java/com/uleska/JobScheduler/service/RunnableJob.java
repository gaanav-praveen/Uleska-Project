/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uleska.JobScheduler.service;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gaana
 */
@Component
public class RunnableJob implements Job
{
    private String jobId;

    public RunnableJob(String jobId)
    {
        this.jobId = jobId;
    }
    public RunnableJob()
    {
        this.jobId = "default_job";
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException
    {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();

        String jobId = dataMap.getString("jobId");
        String jobName = dataMap.getString("jobName");
        

        System.out.println(new Date() + " Job " + jec.getJobDetail().getKey() + " is running. Job Name:" + jobName);
    }
}
