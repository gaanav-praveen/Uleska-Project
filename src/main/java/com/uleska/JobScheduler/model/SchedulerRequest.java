/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uleska.JobScheduler.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Gaana
 */
@Entity
public class SchedulerRequest
{
    @Id
    private String jobId;
    private String jobName;

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.jobId);
        hash = 37 * hash + Objects.hashCode(this.jobName);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SchedulerRequest other = (SchedulerRequest) obj;
        if (!Objects.equals(this.jobId, other.jobId)) {
            return false;
        }
        if (!Objects.equals(this.jobName, other.jobName)) {
            return false;
        }
        return true;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

}
