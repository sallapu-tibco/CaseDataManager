package com.tibco.bpm.cdm.yy.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.tibco.bpm.cdm.api.message.CaseLifecycleNotification;
import com.tibco.bpm.cdm.yy.CLNObserver;
import com.tibco.bpm.cdm.yy.rest.api.ClnsService;
import com.tibco.bpm.cdm.yy.rest.model.CLN;
import com.tibco.bpm.cdm.yy.rest.model.CLNsGetResponseBody;
import com.tibco.bpm.cdm.yy.rest.model.CLNsMessagesGetResponseBody;
import com.tibco.bpm.cdm.yy.rest.model.CLNsObserverInfo;

/**
 * YY service for CLN management. YY has a CLN observer that captures CLNS into a list.
 * The CLNs can be retrieved and removed. Capture can be enabled/disabled.
 * @author smorgan
 *
 */
public class ClnsServiceImpl implements ClnsService
{
	private CLNObserver	observerAll;

	private CLNObserver	observerUpdated;

	private CLNObserver	observerDeleted;

	private CLNObserver	observerUpdatedAndDeleted;

	// Called by Spring
	public void setClnObserverAll(CLNObserver observerAll)
	{
		this.observerAll = observerAll;
	}

	// Called by Spring
	public void setClnObserverUpdated(CLNObserver observerUpdated)
	{
		this.observerUpdated = observerUpdated;
	}

	// Called by Spring
	public void setClnObserverDeleted(CLNObserver observerDeleted)
	{
		this.observerDeleted = observerDeleted;
	}

	// Called by Spring
	public void setClnObserverUpdatedAndDeleted(CLNObserver observerUpdatedAndDeleted)
	{
		this.observerUpdatedAndDeleted = observerUpdatedAndDeleted;
	}

	public ClnsServiceImpl()
	{
		System.out.println("ClnsServiceImpl()");
	}

	private CLNsObserverInfo buildObserverInfo(CLNObserver observer)
	{
		CLNsObserverInfo info = new CLNsObserverInfo();
		List<CaseLifecycleNotification> clns = observer.getAndClear();
		for (CaseLifecycleNotification cln : clns)
		{
			CLN clnBean = new CLN();
			clnBean.setEvent(cln.getEvent().toString());
			clnBean.setCaseReferences(cln.getCaseReferences());
			info.getClns().add(clnBean);
		}
		return info;
	}

	@Override
	public Response clnsMessagesGet() throws Exception
	{
		CLNsMessagesGetResponseBody body = new CLNsMessagesGetResponseBody();
		body.setAll(buildObserverInfo(observerAll));
		body.setUpdated(buildObserverInfo(observerUpdated));
		body.setDeleted(buildObserverInfo(observerDeleted));
		body.setUpdatedAndDeleted(buildObserverInfo(observerUpdatedAndDeleted));
		return Response.ok(body).build();
	}

	@Override
	public Response clnsGet(Boolean enabled) throws Exception
	{
		if (enabled != null)
		{
			if (enabled)
			{
				observerAll.enable();
				observerUpdated.enable();
				observerDeleted.enable();
				observerUpdatedAndDeleted.enable();
			}
			else
			{
				observerAll.disable();
				observerUpdated.disable();
				observerDeleted.disable();
				observerUpdatedAndDeleted.disable();
			}
		}
		CLNsGetResponseBody body = new CLNsGetResponseBody();
		// All observers are enabled/disabled in sync, so using the enabledness of one of them here is fine.
		body.setEnabled(observerAll.isEnabled());
		return Response.ok(body).build();
	}
}
