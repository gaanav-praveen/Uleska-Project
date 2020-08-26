/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uleska.JobScheduler;

import com.uleska.JobScheduler.config.QuartzConfiguration;
import com.uleska.JobScheduler.exceptions.JobNotFoundError;
import com.uleska.JobScheduler.model.SchedulerRequest;
import com.uleska.JobScheduler.model.SchedulerResponse;
import com.uleska.JobScheduler.model.SchedulerResponse.Status;
import com.uleska.JobScheduler.service.RunnableJob;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Gaana
 */
@RestController
public class JobSchedulerController
{

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity schedulerStatus(@RequestParam(value = "jobId", defaultValue = "1") String jobId) throws SchedulerException
    {
        //Scheduler scheduler = (Scheduler) context.getBean("scheduler");
        if (scheduler.checkExists(new JobKey(jobId, "schedule-job"))) {
            SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is running!", Status.RUNNING);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
        }
        String error = "Request job not found";
        return buildErrorResponse(jobId, new JobNotFoundError(HttpStatus.BAD_REQUEST, error));
    }

    @RequestMapping(value = "/paused", method = RequestMethod.POST)
    public ResponseEntity schedulerStopJob(@RequestParam(value = "jobId", defaultValue = "1") String jobId,
                                           @RequestParam(value = "jobName", required = false) String jobName) throws SchedulerException
    {
        //Scheduler scheduler = (Scheduler) context.getBean("scheduler");
        final JobKey jobkey = new JobKey(jobId, "schedule-job");
        if (scheduler.checkExists(jobkey)) {
            if(!isJobPaused(scheduler, jobkey))
            {
                scheduler.pauseJob(jobkey);
            SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is paused temporarily!", Status.PAUSED);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
            }else{
                SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is already in paused state!", Status.PAUSED);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
            }
        }
        String error = "Request job not found";
        return buildErrorResponse(jobId, new JobNotFoundError(HttpStatus.BAD_REQUEST, error));
    }

    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public ResponseEntity schedulerResumeJob(@RequestParam(value = "jobId", defaultValue = "1") String jobId,
                                           @RequestParam(value = "jobName", required = false) String jobName) throws SchedulerException
    {
        //Scheduler scheduler = (Scheduler) context.getBean("scheduler");
        final JobKey jobkey = new JobKey(jobId, "schedule-job");
        if (scheduler.checkExists(jobkey)) {
            if(isJobPaused(scheduler, jobkey))
            {
                scheduler.resumeJob(jobkey);
            SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is resumed back!", Status.PAUSED);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
            }else{
                SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is never paused to resume!", Status.PAUSED);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
            }
        }
        String error = "Request job not found";
        return buildErrorResponse(jobId, new JobNotFoundError(HttpStatus.BAD_REQUEST, error));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity schedulerDeleteJob(@RequestParam(value = "jobId", defaultValue = "1") String jobId,
                                           @RequestParam(value = "jobName", required = false) String jobName) throws SchedulerException
    {
        //Scheduler scheduler = (Scheduler) context.getBean("scheduler");
        final JobKey jobkey = new JobKey(jobId, "schedule-job");
        if (scheduler.checkExists(jobkey)) {
            scheduler.deleteJob(jobkey);
            SchedulerResponse scheduleResponse = new SchedulerResponse(
                jobId, "Job is deleted!", Status.PAUSED);
            return new ResponseEntity(scheduleResponse, HttpStatus.OK);
        }
        String error = "Request job not found";
        return buildErrorResponse(jobId, new JobNotFoundError(HttpStatus.BAD_REQUEST, error));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<SchedulerResponse> createScheduler(@RequestBody SchedulerRequest jsonRequest) throws SchedulerException
    {
        createJobSchedule(jsonRequest, scheduler);
        SchedulerResponse scheduleResponse = new SchedulerResponse(
            jsonRequest.getJobId(), "Job Scheduled Successfully!", Status.RUNNING);
        return ResponseEntity.ok(scheduleResponse);
    }

    private void createJobSchedule(SchedulerRequest scheduleRequest, final Scheduler sched) throws SchedulerException
    {
        sched.start();
        JobDetail jobDetail = buildJobDetail(scheduleRequest);
        Trigger trigger = buildJobTrigger(jobDetail);
        sched.scheduleJob(jobDetail, trigger);

    }

    private JobDetail buildJobDetail(SchedulerRequest scheduleRequest)
    {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("jobId", scheduleRequest.getJobId());
        jobDataMap.put("jobName", scheduleRequest.getJobName());

        return JobBuilder.newJob(RunnableJob.class)
            .withIdentity(jobDataMap.getString("jobId"), "schedule-job")
            .withDescription("Scheduled Job: " + jobDataMap.get("jobId"))
            .usingJobData(jobDataMap)
            .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail)
    {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "schedule-job-triggers")
            .withDescription("Scheduled Job Triggered for job: " + jobDetail.getJobDataMap().get("jobId"))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(4).repeatForever())
            .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity buildErrorResponse(final String jobId, final JobNotFoundError error)
    {
        return new ResponseEntity(new SchedulerResponse(jobId, "Job ID not found", Status.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    private Boolean isJobPaused(final Scheduler scheduler, final JobKey jobKey) throws SchedulerException
    {
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
        for (Trigger trigger : triggers) {
            TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            if (TriggerState.PAUSED.equals(triggerState)) {
                return true;
            }
        }
        return false;
    }
}
