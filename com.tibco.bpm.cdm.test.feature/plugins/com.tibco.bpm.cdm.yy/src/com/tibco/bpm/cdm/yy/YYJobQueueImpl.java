package com.tibco.bpm.cdm.yy;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.cdm.core.model.Job;
import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;
import com.tibco.bpm.msg.aq.DqdMessage;
import com.tibco.bpm.msg.pq.CloudPQ;
import com.tibco.bpm.msg.pq.PQMessage;

/**
 * A simplified version of JobQueueImpl, reduced to just the manipulations YY needs to do
 *
 */
public class YYJobQueueImpl extends CloudPQ
{
	private static CLFClassContext		logCtx		= CloudLoggingFramework.init(YYJobQueueImpl.class,
			CDMLoggingInfo.instance);

	private static final ObjectMapper	om			= new ObjectMapper();

	private static final String			QUEUE_NAME	= "cdm_job_queue";

	private DataSource					dataSource	= null;

	private Connection					connection	= null;

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	private Connection getConnection() throws Exception
	{
		CLFMethodContext clf = logCtx.getMethodContext("getConnection");
		try
		{
			Connection conn = DataSourceUtils.getConnection(dataSource);
			if (conn == null)
			{
				throw new Exception("Not connected");
			}
			clf.local.trace("Got connection: %s", conn.toString());
			return conn;
		}
		catch (CannotGetJdbcConnectionException e)
		{
			throw e;
		}
	}

	private void releaseConnection(Connection conn)
	{
		CLFMethodContext clf = logCtx.getMethodContext("releaseConnection");
		DataSourceUtils.releaseConnection(conn, dataSource);
		clf.local.trace("Released connection: %s", conn.toString());
	}

	public void enqueueJob(Job job, String correlationId, int delay) throws Exception
	{
		System.out.println(String.format("Enqueue Job: %s, CorrelationId: %s, delay: %d", job, correlationId, delay));
		String jobJSON;
		try
		{
			jobJSON = om.writeValueAsString(job);
		}
		catch (JsonProcessingException e)
		{
			throw new Exception(e);
		}

		Connection connection = null;
		try
		{
			connection = getConnection();

			try
			{
				int priority = Job.getPriorityForMethod(job.getMethod());
				enqueueMessage(QUEUE_NAME, jobJSON.getBytes(StandardCharsets.UTF_8), correlationId, delay, priority,
						null, null, false, connection);
			}
			catch (Exception e)
			{
				// Wrap to something more specific
				throw new Exception(e);
			}
		}
		finally
		{
			if (connection != null)
			{
				releaseConnection(connection);
			}
		}
	}

	public void purgeJobsWithCorrelationId(String correlationId) throws Exception
	{
		try
		{
			connection = getConnection();
			DqdMessage message = null;
			do
			{
				byte[] fetchMsgId = null;
				PQMessage msg = dequeueMessage(QUEUE_NAME, fetchMsgId, correlationId, connection);
				message = msg.getMessage();
			}
			while (message != null);
		}
		catch (Exception e)
		{
			throw new Exception(e);
		}
		finally
		{
			if (connection != null)
			{
				releaseConnection(connection);
			}
		}
	}
}
