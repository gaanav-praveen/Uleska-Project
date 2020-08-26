/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uleska.JobScheduler.model;

/**
 *
 * @author Gaana
 */

public class SchedulerResponse
{
    private Status currentStatus;
    private String jobId;
    private String message;

    public enum Status
    {
        RUNNING, PAUSED, STOP, DELETED, NOT_FOUND;
    }

    public SchedulerResponse (String jobId, String message, Status currentStatus)
    {
        this.jobId = jobId;
        this.message = message;
        this.currentStatus = currentStatus;
    }
    public Status getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
