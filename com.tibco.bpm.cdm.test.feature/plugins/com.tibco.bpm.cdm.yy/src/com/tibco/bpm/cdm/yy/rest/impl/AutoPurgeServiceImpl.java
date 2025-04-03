package com.tibco.bpm.cdm.yy.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tibco.bpm.cdm.core.model.Job;
import com.tibco.bpm.cdm.yy.YYJobQueueImpl;
import com.tibco.bpm.cdm.yy.rest.api.AutoPurgeService;

public class AutoPurgeServiceImpl implements AutoPurgeService
{
	private YYJobQueueImpl jobQueue;

	public void setJobQueue(YYJobQueueImpl jobQueue)
	{
		this.jobQueue = jobQueue;
	}

	@Override
	public Response autoPurgeCancelJobsGet() throws Exception
	{
		jobQueue.purgeJobsWithCorrelationId("AP");
		return Response.status(Status.OK).build();
	}

	@Override
	public Response autoPurgeCreateJobGet(Integer delay) throws Exception
	{
		// Post auto-purge bootstrap message
		Job job = new Job();
		job.setMethod(Job.METHOD_AUTO_PURGE);
		// Queue a top level Auto Purge job 
		delay = delay == null ? 0 : Math.max(0, delay);
		jobQueue.enqueueJob(job, Job.AP_JOB_CORRELATION_ID, delay);
		return Response.status(Status.OK).build();
	}

}
