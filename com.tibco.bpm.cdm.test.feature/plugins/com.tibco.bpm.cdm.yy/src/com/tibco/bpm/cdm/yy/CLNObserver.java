package com.tibco.bpm.cdm.yy;

import java.util.ArrayList;
import java.util.List;

import com.tibco.bpm.cdm.api.CaseLifecycleNotificationObserver;
import com.tibco.bpm.cdm.api.message.CaseLifecycleNotification;

/**
 * Reference implementation of the CLNO interface.
 * @author smorgan
 * @since 2019
 */
public class CLNObserver implements CaseLifecycleNotificationObserver
{
	private List<CaseLifecycleNotification>	clns	= new ArrayList<>();

	private boolean							enabled	= false;

	public CLNObserver()
	{
		System.out.println("Constucting YY CLNObserver");
	}

	@Override
	public void process(CaseLifecycleNotification cln)
	{
		System.out.println(this + ": Received a CLN: " + cln);
		if (enabled)
		{
			synchronized (clns)
			{
				clns.add(cln);
			}
		}
	}

	public List<CaseLifecycleNotification> getAndClear()
	{
		synchronized (clns)
		{
			List<CaseLifecycleNotification> result = new ArrayList<>();
			result.addAll(clns);
			clns.clear();
			return result;
		}
	}

	public void disable()
	{
		enabled = false;
	}

	public void enable()
	{
		enabled = true;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public String toString()
	{
		return "YY CLN observer containing " + clns.size() + " CLNs";
	}
}
