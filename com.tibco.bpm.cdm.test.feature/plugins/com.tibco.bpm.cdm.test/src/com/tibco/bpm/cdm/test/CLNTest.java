package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.CaseLifecycleNotificationObserver;
import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.NotAuthorisedException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.api.message.CaseLifecycleNotification;
import com.tibco.bpm.cdm.api.message.CaseLifecycleNotification.Event;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.cln.CLNObserverList;
import com.tibco.bpm.cdm.core.cln.CLNObserverList.CLNObserverDetails;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.core.dto.CaseUpdateDTO;
import com.tibco.bpm.cdm.util.DateTimeParser;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class CLNTest extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager	deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager			caseManager;

	@Autowired
	@Qualifier("clnObserverList")
	private CLNObserverList		clnObserverList;

	/**
	 * A CLN observer that stores CLNs in a list 
	 * @author smorgan
	 */
	public static class TestCLNObserver implements CaseLifecycleNotificationObserver
	{
		List<CaseLifecycleNotification> clns = new ArrayList<>();

		@Override
		public void process(CaseLifecycleNotification cln)
		{
			clns.add(cln);
		}

		public List<CaseLifecycleNotification> getCLNs()
		{
			return clns;
		}
	}

	@Test
	public void testUpdateCaseCLN()
			throws PersistenceException, ReferenceException, InternalException, ValidationException, ArgumentException,
			NotAuthorisedException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			// Create and register a CLN observer that stores CLNs in a list
			TestCLNObserver clnObserverAll = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverAll, null));

			// ...and another that explicitly asks for DELETED events (so won't get any)
			TestCLNObserver clnObserverDeleted = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverDeleted, Collections.singletonList(Event.DELETED)));

			// ...and another than explicitly asks for UPDATED events 
			TestCLNObserver clnObserverUpdated = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverUpdated, Collections.singletonList(Event.UPDATED)));

			// ...and finally one that asks explicitly for UPDATED AND DELETED events (so will get all,
			// but tests that this is the same as not asking for any in particular)
			TestCLNObserver clnObserverUpdatedAndDeleted = new TestCLNObserver();
			clnObserverList.add(
					new CLNObserverDetails(clnObserverUpdatedAndDeleted, Arrays.asList(Event.UPDATED, Event.DELETED)));

			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);

			// Update case
			CaseUpdateDTO dto = new CaseUpdateDTO(refs.get(0),
					"{\"state\":\"CREATED\",\"premium\":123,\"number\":999}");
			caseManager.updateCases(Collections.singletonList(dto));

			// Check that the CLN was received by clnObserverAll, clnObserverUpdatedAndDeleted and 
			// clnObserverUpdated, but not clnObserverDeleted
			List<CaseLifecycleNotification> clns = clnObserverAll.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.UPDATED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), dto.getNewCaseReference().toString());

			clns = clnObserverUpdated.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.UPDATED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), dto.getNewCaseReference().toString());

			clns = clnObserverUpdatedAndDeleted.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.UPDATED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), dto.getNewCaseReference().toString());

			clns = clnObserverDeleted.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 0);
		}
		finally
		{
			clnObserverList.clear();
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteByRefCLN()
			throws PersistenceException, ReferenceException, InternalException, ValidationException, ArgumentException,
			NotAuthorisedException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			// Create and register a CLN observer that stores CLNs in a list
			TestCLNObserver clnObserverAll = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverAll, null));

			// ...and another that explicitly asks for DELETED events
			TestCLNObserver clnObserverDeleted = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverDeleted, Collections.singletonList(Event.DELETED)));

			// ...and another than explciitly asks for UPDATED events (so won't get any)
			TestCLNObserver clnObserverUpdated = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(clnObserverUpdated, Collections.singletonList(Event.UPDATED)));

			// ...and finally one that asks explicitly for UPDATED AND DELETED events (so will get all,
			// but tests that this is the same as not asking for any in particular)
			TestCLNObserver clnObserverUpdatedAndDeleted = new TestCLNObserver();
			clnObserverList.add(
					new CLNObserverDetails(clnObserverUpdatedAndDeleted, Arrays.asList(Event.UPDATED, Event.DELETED)));

			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 1);

			// Delete single case by ref
			caseManager.deleteCase(refs.get(0));

			// Check that the CLN was received by clnObserverAll, clnObserverDeleted and 
			// clnObserverUpdatedAndDeleted, but not clnObserverUpdated
			List<CaseLifecycleNotification> clns = clnObserverAll.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.DELETED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), refs.get(0).toString());

			clns = clnObserverDeleted.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.DELETED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), refs.get(0).toString());

			clns = clnObserverUpdatedAndDeleted.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.DELETED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), refs.get(0).toString());

			clns = clnObserverUpdated.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 0);
		}
		finally
		{
			clnObserverList.clear();
			forceUndeploy(deploymentId);
		}
	}

	@Test
	public void testDeleteManyCLN()
			throws PersistenceException, ReferenceException, InternalException, ValidationException, ArgumentException,
			NotAuthorisedException, DeploymentException, IOException, URISyntaxException, RuntimeApplicationException
	{
		BigInteger deploymentId = deploy("/apps/app1");
		try
		{
			// Create and register a CLN observer that stores CLNs in a list
			TestCLNObserver testCLNObserver = new TestCLNObserver();
			clnObserverList.add(new CLNObserverDetails(testCLNObserver, null));

			List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
					Arrays.asList("{\"state\":\"CREATED\",\"number\":777}", "{\"state\":\"CREATED\",\"number\":888}",
							"{\"state\":\"CREATED\",\"number\":999}"));
			Assert.assertEquals(refs.size(), 3);

			// Delete all 3 cases
			caseManager.deleteCases(new QualifiedTypeName("org.policycorporation.Policy"), 1, "CREATED",
					DateTimeParser.parseString("2999TZ", true), null);

			// Check that the CLN was received
			List<CaseLifecycleNotification> clns = testCLNObserver.getCLNs();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 1);
			Assert.assertEquals(clns.get(0).getEvent(), Event.DELETED);
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 3);
			Assert.assertTrue(clns.get(0).getCaseReferences()
					.containsAll(refs.stream().map(CaseReference::toString).collect(Collectors.toList())));
		}
		finally
		{
			clnObserverList.clear();
			forceUndeploy(deploymentId);
		}
	}
}
