package com.tibco.bpm.cdm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.CasedataException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPostResponseBody;
import com.tibco.bpm.cdm.rest.functions.AccountCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.CaseLifecycleNotificationFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.CustomerCreatorFunction;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.cdm.yy.rest.model.CLN;
import com.tibco.bpm.cdm.yy.rest.model.CLNsMessagesGetResponseBody;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.RuntimeApplication;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;
import com.tibco.bpm.dt.rasc.impl.RuntimeApplicationImpl;

/**
 * REST test for CDM's ability to broadcast case lifecycle event notifications to interested parties.
 * @author smorgan & nashtapu
 * @since 2019
 */
public class CLNTest extends Utils
{
	CasesFunctions						casesFunctions			= new CasesFunctions();

	private Response					response				= null;

	AccountCreatorFunction				accountCreator			= new AccountCreatorFunction();

	CustomerCreatorFunction				customerCreator			= new CustomerCreatorFunction();

	CaseLifecycleNotificationFunctions	cln						= new CaseLifecycleNotificationFunctions();

	private static final String			caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String			caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String			applicationMajorVersion	= "1";

	private void deployAsNewApplication(String rascVersion) throws IOException, URISyntaxException,
			DataModelSerializationException, RuntimeApplicationException, InterruptedException
	{

		BigInteger deploymentIdApp = BigInteger.valueOf(8);
		File tempFile = File.createTempFile("temp", ".zip");
		File rascFile = FileUtils.buildZipFromFolderResource("/apps/Accounts/AccountsDataModel", true);
		Files.copy(rascFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		//bump the version number
		FileUtils.setVersionInRASC(tempFile.toPath(), rascVersion);

		RuntimeApplication runtimeApplication = FileUtils.loadRuntimeApplicationFromZip(tempFile);
		runtimeApplication = new RuntimeApplicationImpl(new FileInputStream(tempFile));

		runtimeApplication.setID(deploymentIdApp);

		try
		{
			deploymentManager.deploy(runtimeApplication);
		}
		catch (DeploymentException | InternalException e)
		{
			System.out.println("deployment should have been successful");
			Assert.fail();
		}
	}

	private void assertEmptyResponse(Response response, String subscriber)
	{
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");

		CLNsMessagesGetResponseBody body = response.as(CLNsMessagesGetResponseBody.class);
		Assert.assertNotNull(body);

		if (subscriber.equals("all") || subscriber.equals(""))
		{
			List<CLN> all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 0);
		}

		else if (subscriber.equals("deleted") || subscriber.equals(""))
		{
			List<CLN> del = body.getDeleted().getClns();
			Assert.assertNotNull(del, "cln should not be null");
			Assert.assertEquals(del.size(), 0);
		}

		else if (subscriber.equals("updated") || subscriber.equals(""))
		{
			List<CLN> upd = body.getUpdated().getClns();
			Assert.assertNotNull(upd, "cln should not be null");
			Assert.assertEquals(upd.size(), 0);
		}

		else if (subscriber.equals("updatedDeleted") || subscriber.equals(""))
		{

			List<CLN> updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 0);
		}
	}

	//used only in multi-event test case
	private void assertDeleted(List<CLN> del, List<String> caseRefsUpdatedAccountsA1,
			List<String> caseRefsUpdatedCustomersA2, List<String> caseRefsAccountsA1,
			List<String> caseRefsUpdatedCustomersA1, List<String> caseRefsCustomersA1, Integer offset, Integer size)
	{
		for (int i = offset; i < (offset + size); i++)
		{
			Assert.assertEquals(del.get(i).getEvent(), "DELETED", "cln event should be delated");

			if (i == offset)
			{
				Assert.assertEquals(del.get(i).getCaseReferences().size(), 1, "case references size is not matching");
				Assert.assertEquals(del.get(i).getCaseReferences().get(0), caseRefsUpdatedAccountsA1.get(0),
						"case references is not matching");
			}

			if (i == (offset + 1))
			{
				Assert.assertEquals(del.get(i).getCaseReferences().size(), 1, "case references size is not matching");
				Assert.assertEquals(del.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomersA2.get(0),
						"case references is not matching");
			}

			if (i == (offset + 2))
			{
				Assert.assertEquals(del.get(i).getCaseReferences().size(), 2, "case references size is not matching");
				Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsAccountsA1.get(3)),
						"case references is not matching");
				Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsUpdatedAccountsA1.get(2)),
						"case references is not matching");
			}

			if (i == (offset + 3))
			{
				Assert.assertEquals(del.get(i).getCaseReferences().size(), 3, "case references size is not matching");
				Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsUpdatedCustomersA1.get(1)),
						"case references is not matching");
				for (int j = 1; j < del.get(i).getCaseReferences().size(); j++)
				{
					Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsCustomersA1.get(j + 1)),
							"case references is not matching");
				}
			}
		}
	}

	//used only in multi-event test case
	private void assertUpdated(List<CLN> upd, List<String> caseRefsUpdatedAccountsA1,
			List<String> caseRefsUpdatedCustomersA2, List<String> caseRefsAccountsA1,
			List<String> caseRefsIntermediateAccountsA1, List<String> caseRefsUpdatedCustomersA1,
			List<String> caseRefsUpdatedAccountsA2, List<String> caseRefsCustomersA1, Integer offset, Integer size)
	{
		for (int i = offset; i < (offset + size); i++)
		{
			Assert.assertEquals(upd.get(i).getEvent(), "UPDATED", "cln event should be updated");
			if (i < (offset + 5))
			{
				Assert.assertEquals(upd.get(i).getCaseReferences().size(), 2, "case references size is not matching");
				for (int j = 0; j < upd.get(i).getCaseReferences().size(); j++)
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().get(j),
							caseRefsIntermediateAccountsA1.get(j + (2 * i)), "case references is not matching");
				}
			}

			else
			{
				if (i == (offset + 5))
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomersA1.get(0),
							"case references is not matching");
				}

				if (i == (offset + 6))
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedAccountsA2.get(0),
							"case references is not matching");
				}

				if (i == (offset + 7))
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomersA2.get(0),
							"case references is not matching");
				}

				if (i == (offset + 8))
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedAccountsA1.get(2),
							"case references is not matching");
				}

				if (i == (offset + 9))
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomersA1.get(1),
							"case references is not matching");
				}
			}
		}
	}

	private void purgeAccountCases(String state) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeAccount);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		if (state.equalsIgnoreCase(""))
		{
			String[] stateArray = {"@ACTIVE&", "@FROZEN&", "@TREMPORARILY DEACTIVATED&", "@WAITING FOR APPROVAL&",
					"@REINSTATED&", "@WAITING FOR CANCELLATION&"};
			for (int i = 0; i < stateArray.length; i++)
			{
				filterMap.put("caseState", stateArray[i]);
				response = casesFunctions.purgeCases(filterMap);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
			}
			filterMap.clear();
		}

		else if (!(state.equalsIgnoreCase("")))
		{
			filterMap.put("caseState", state);
			response = casesFunctions.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			filterMap.clear();
		}
	}

	private void purgeCustomerCases(String state) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		if (state.equalsIgnoreCase(""))
		{
			String[] stateArray = {"VERYACTIVE50KABOVE", "ACTIVE10KTO50K", "REGULAR1KTO10K", "SELDOM0TO1K",
					"ACTIVEBUTONHOLD", "INACTIVE", "BARREDORTERMINATED", "CANCELLED", "TRIAL"};
			for (int i = 0; i < stateArray.length; i++)
			{
				filterMap.put("caseState", stateArray[i]);
				response = casesFunctions.purgeCases(filterMap);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
			}
			filterMap.clear();
		}

		else if (!(state.equalsIgnoreCase("")))
		{
			filterMap.put("caseState", state);
			response = casesFunctions.purgeCases(filterMap);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			filterMap.clear();
		}
	}

	@Test
	public void testCLN() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, InterruptedException
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			// Create Policy
			Response response = casesFunctions.createCases("org.policycorporation.Policy", 1,
					Collections.singletonList("{\"state\":\"CREATED\",\"number\":777}"));
			CasesPostResponseBody responseBody = response.as(CasesPostResponseBody.class);
			Assert.assertNotNull(responseBody);
			Assert.assertEquals(responseBody.size(), 1);
			String caseReference = responseBody.get(0);

			// Enable YY CLN capture and flush any existing captured messages
			cln.flushNotifications(false, true);

			// Update the Policy
			String newCaseReference = casesFunctions
					.updateSingleCase(caseReference, "{\"state\":\"CREATED\",\"number\":777,\"premium\":50.99}")
					.jsonPath().getString("[0]");

			// Delete the Policy
			casesFunctions.purgeSingleCase(newCaseReference);

			// Check that YY captured 2 CLNs (update, then delete)
			response = cln.readMessages();

			CLNsMessagesGetResponseBody body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body);
			List<CLN> clns = body.getAll().getClns();
			Assert.assertNotNull(clns);
			Assert.assertEquals(clns.size(), 2);
			Assert.assertEquals(clns.get(0).getEvent(), "UPDATED");
			Assert.assertEquals(clns.get(0).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(0).getCaseReferences().get(0), newCaseReference);
			Assert.assertEquals(clns.get(1).getEvent(), "DELETED");
			Assert.assertEquals(clns.get(1).getCaseReferences().size(), 1);
			Assert.assertEquals(clns.get(1).getCaseReferences().get(0), newCaseReference);
		}
		finally
		{
			cln.disableMessages();
			forceUndeploy(deploymentId);
		}
	}

	//Test to verify bulk update, delete notifications are sent correctly and notifications for no other events are sent out 
	@Test(description = "Test to verify bulk update, delete notifications are sent correctly and notifications for no other events are sent out")
	public void testCaseLifeCycleNotification() throws IOException, InterruptedException, PersistenceException,
			DeploymentException, InternalException, CasedataException, ReferenceException, ArgumentException,
			URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		BigInteger deploymentIdApp1 = BigInteger.valueOf(7);
		BigInteger deploymentIdApp2 = BigInteger.valueOf(8);
		try
		{
			List<String> caseRefsAccounts = new ArrayList<>();
			List<String> caseRefsUpdatedAccounts = new ArrayList<>();
			List<String> caseRefsUpdatedCustomers = new ArrayList<>();
			List<String> caseRefsCustomers = new ArrayList<>();
			List<String> intermediateRefs1 = new ArrayList<>();
			List<String> intermediateRefs2 = new ArrayList<>();
			List<String> intermediateRefs3 = new ArrayList<>();
			List<String> tempRefs = new ArrayList<>();

			//enable cln notifications, read all messages and keep it enabled
			cln.flushNotifications(false, true);

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			//create 600 accounts
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(1, 600, ""));
			caseRefsAccounts.addAll(tempRefs);
			tempRefs.clear();

			//create 80 customers
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "VERYACTIVE50KABOVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "ACTIVE10KTO50K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "REGULAR1KTO10K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "SELDOM0TO1K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "ACTIVEBUTONHOLD"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "INACTIVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "BARREDORTERMINATED"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "CANCELLED"));
			caseRefsCustomers.addAll(tempRefs);
			tempRefs.clear();

			//read the messages 
			response = cln.readMessages();

			//verify on deploy and create no notifications are generated
			assertEmptyResponse(response, "");

			//update the first case reference 10 times
			intermediateRefs1 = accountCreator.updateAccountsArrayData(caseRefsAccounts.subList(0, 1), 10, "accounts",
					true);

			//read the messages 
			response = cln.readMessages();

			//assert the response
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			CLNsMessagesGetResponseBody body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body, "events should be published");

			List<CLN> all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 10);
			for (int i = 0; i < all.size(); i++)
			{
				Assert.assertEquals(all.get(i).getEvent(), "UPDATED", "cln event should be updated");
				Assert.assertEquals(all.get(i).getCaseReferences().get(0), intermediateRefs1.get(i),
						"case references is not matching");
			}

			List<CLN> upd = body.getUpdated().getClns();
			Assert.assertNotNull(upd, "cln should not be null");
			Assert.assertEquals(upd.size(), 10);
			for (int i = 0; i < upd.size(); i++)
			{
				Assert.assertEquals(upd.get(i).getEvent(), "UPDATED", "cln event should be updated");
				Assert.assertEquals(upd.get(i).getCaseReferences().get(0), intermediateRefs1.get(i),
						"case references is not matching");
			}

			List<CLN> updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 10);
			for (int i = 0; i < updDel.size(); i++)
			{
				Assert.assertEquals(updDel.get(i).getEvent(), "UPDATED", "cln event should be updated");
				Assert.assertEquals(updDel.get(i).getCaseReferences().get(0), intermediateRefs1.get(i),
						"case references is not matching");
			}
			assertEmptyResponse(response, "deleted");

			//replace the accounts first index which was used for update
			caseRefsAccounts.set(0, intermediateRefs1.get(9));

			intermediateRefs1.clear();

			//upgrade the application
			//to make sure event subscription is not affected after upgrade
			deploymentIdApp2 = deployRASC("/apps/Accounts/AccountsDataModelV2.rasc");

			//bulk update
			intermediateRefs1 = accountCreator.updateAccountsArrayData(caseRefsAccounts.subList(0, 200), 1, "accounts");

			intermediateRefs2 = accountCreator.updateAccountsArrayData(intermediateRefs1.subList(0, 100), 1,
					"accounts");

			intermediateRefs3 = accountCreator.updateAccountsArrayData(intermediateRefs2.subList(0, 50), 1, "accounts");

			//update a customer
			caseRefsUpdatedCustomers = customerCreator.customerChangState(caseRefsCustomers, "VERYACTIVE50KABOVE",
					"CANCELLED");

			//read the messages 
			response = cln.readMessages();

			//assert the response
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body, "events should be published");

			all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 4);
			for (int i = 0; i < all.size(); i++)
			{
				Assert.assertEquals(all.get(i).getEvent(), "UPDATED", "cln event should be updated");

				if (i == 0)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 200,
							"case references size is not matching");
					for (int j = 0; j < all.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(all.get(i).getCaseReferences().get(j), intermediateRefs1.get(j),
								"case references is not matching");
					}
				}

				if (i == 1)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 100,
							"case references size is not matching");
					for (int j = 0; j < all.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(all.get(i).getCaseReferences().get(j), intermediateRefs2.get(j),
								"case references is not matching");
					}
				}

				if (i == 2)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 50,
							"case references size is not matching");
					for (int j = 0; j < all.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(all.get(i).getCaseReferences().get(j), intermediateRefs3.get(j),
								"case references is not matching");
					}
				}

				if (i == 3)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(all.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}
			}

			upd = body.getUpdated().getClns();
			Assert.assertNotNull(upd, "cln should not be null");
			Assert.assertEquals(upd.size(), 4);
			for (int i = 0; i < upd.size(); i++)
			{
				Assert.assertEquals(upd.get(i).getEvent(), "UPDATED", "cln event should be updated");

				if (i == 0)
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 200,
							"case references size is not matching");
					for (int j = 0; j < upd.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(upd.get(i).getCaseReferences().get(j), intermediateRefs1.get(j),
								"case references is not matching");
					}
				}

				if (i == 1)
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 100,
							"case references size is not matching");
					for (int j = 0; j < upd.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(upd.get(i).getCaseReferences().get(j), intermediateRefs2.get(j),
								"case references is not matching");
					}
				}

				if (i == 2)
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 50,
							"case references size is not matching");
					for (int j = 0; j < upd.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(upd.get(i).getCaseReferences().get(j), intermediateRefs3.get(j),
								"case references is not matching");
					}
				}

				if (i == 3)
				{
					Assert.assertEquals(upd.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(upd.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}
			}

			updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 4);
			for (int i = 0; i < updDel.size(); i++)
			{
				Assert.assertEquals(updDel.get(i).getEvent(), "UPDATED", "cln event should be updated");

				if (i == 0)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 200,
							"case references size is not matching");
					for (int j = 0; j < updDel.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(updDel.get(i).getCaseReferences().get(j), intermediateRefs1.get(j),
								"case references is not matching");
					}
				}

				if (i == 1)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 100,
							"case references size is not matching");
					for (int j = 0; j < updDel.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(updDel.get(i).getCaseReferences().get(j), intermediateRefs2.get(j),
								"case references is not matching");
					}
				}

				if (i == 2)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 50,
							"case references size is not matching");
					for (int j = 0; j < updDel.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertEquals(updDel.get(i).getCaseReferences().get(j), intermediateRefs3.get(j),
								"case references is not matching");
					}
				}

				if (i == 3)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(updDel.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}
			}
			assertEmptyResponse(response, "deleted");

			caseRefsUpdatedAccounts.addAll(intermediateRefs3);
			caseRefsUpdatedAccounts.addAll(intermediateRefs2.subList(50, 100));
			caseRefsUpdatedAccounts.addAll(intermediateRefs1.subList(100, 200));
			caseRefsUpdatedAccounts.addAll(caseRefsAccounts.subList(200, 600));

			//purge single account - case is deleted from @REINSTATED& state
			response = casesFunctions.purgeSingleCase(caseRefsUpdatedAccounts.get(0));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//purge single customer
			response = casesFunctions.purgeSingleCase(caseRefsUpdatedCustomers.get(0));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//purge all customer and account cases
			//account cases purge - 33 cases are deleted
			purgeAccountCases("@REINSTATED&");

			//customer cases purge - 10 cases are deleted
			purgeCustomerCases("CANCELLED");

			//read the messages 
			response = cln.readMessages();

			//assert the response
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body, "events should be published");

			all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 4);
			for (int i = 0; i < all.size(); i++)
			{
				Assert.assertEquals(all.get(i).getEvent(), "DELETED", "cln event should be deleted");

				if (i == 0)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(all.get(i).getCaseReferences().get(0), caseRefsUpdatedAccounts.get(0),
							"case references is not matching");
				}

				if (i == 1)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(all.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}

				if (i == 2)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 33,
							"case references size is not matching");
				}

				if (i == 3)
				{
					Assert.assertEquals(all.get(i).getCaseReferences().size(), 10,
							"case references size is not matching");
					for (int j = 0; j < all.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertTrue(all.get(i).getCaseReferences().contains(caseRefsCustomers.get(70 + j)),
								"case references is not matching");
					}
				}
			}

			List<CLN> del = body.getDeleted().getClns();
			del = body.getDeleted().getClns();
			Assert.assertNotNull(del, "cln should not be null");
			Assert.assertEquals(del.size(), 4);
			for (int i = 0; i < del.size(); i++)
			{
				Assert.assertEquals(del.get(i).getEvent(), "DELETED", "cln event should be deleted");

				if (i == 0)
				{
					Assert.assertEquals(del.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(del.get(i).getCaseReferences().get(0), caseRefsUpdatedAccounts.get(0),
							"case references is not matching");
				}

				if (i == 1)
				{
					Assert.assertEquals(del.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(del.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}

				if (i == 2)
				{
					Assert.assertEquals(del.get(i).getCaseReferences().size(), 33,
							"case references size is not matching");
				}

				if (i == 3)
				{
					Assert.assertEquals(del.get(i).getCaseReferences().size(), 10,
							"case references size is not matching");
					for (int j = 0; j < del.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsCustomers.get(70 + j)),
								"case references is not matching");
					}
				}
			}

			updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 4);
			for (int i = 0; i < updDel.size(); i++)
			{
				Assert.assertEquals(updDel.get(i).getEvent(), "DELETED", "cln event should be delated");

				if (i == 0)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(updDel.get(i).getCaseReferences().get(0), caseRefsUpdatedAccounts.get(0),
							"case references is not matching");
				}

				if (i == 1)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 1,
							"case references size is not matching");
					Assert.assertEquals(updDel.get(i).getCaseReferences().get(0), caseRefsUpdatedCustomers.get(0),
							"case references is not matching");
				}

				if (i == 2)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 33,
							"case references size is not matching");
				}

				if (i == 3)
				{
					Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 10,
							"case references size is not matching");
					for (int j = 0; j < updDel.get(i).getCaseReferences().size(); j++)
					{
						Assert.assertTrue(updDel.get(i).getCaseReferences().contains(caseRefsCustomers.get(70 + j)),
								"case references is not matching");
					}
				}
			}
			assertEmptyResponse(response, "updated");
		}
		finally
		{
			cln.disableMessages();
			forceUndeploy(deploymentIdApp2);
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to verify bulk delete notifications are sent correctly and notifications for no other events are sent out 
	@Test(description = "Test to verify bulk delete notifications are sent correctly and notifications for no other events are sent out")
	public void testBulkPurge() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, InterruptedException, CasedataException,
			ReferenceException, ArgumentException, DataModelSerializationException
	{
		BigInteger deploymentIdApp1 = BigInteger.valueOf(7);
		try
		{
			List<String> caseRefsAccounts = new ArrayList<>();
			List<String> caseRefsCustomers = new ArrayList<>();
			List<String> tempRefs = new ArrayList<>();

			//enable cln notifications, read all messages and keep it enabled
			cln.flushNotifications(false, true);

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			//create 600 accounts
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(1, 100, "@ACTIVE&"));
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(101, 100, "@FROZEN&"));
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(201, 100,
					"@TREMPORARILY DEACTIVATED&"));
			tempRefs.addAll(
					accountCreator.createAccountSpecificStateOffsetDefaultCases(301, 100, "@WAITING FOR APPROVAL&"));
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(401, 100, "@REINSTATED&"));
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(501, 100,
					"@WAITING FOR CANCELLATION&"));
			caseRefsAccounts.addAll(tempRefs);
			tempRefs.clear();

			//create 90 accounts
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "VERYACTIVE50KABOVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "ACTIVE10KTO50K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "REGULAR1KTO10K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "SELDOM0TO1K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "ACTIVEBUTONHOLD"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "INACTIVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "BARREDORTERMINATED"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "CANCELLED"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(10, "TRIAL"));
			caseRefsCustomers.addAll(tempRefs);
			tempRefs.clear();

			purgeAccountCases("");
			purgeCustomerCases("");

			//read the messages 
			response = cln.readMessages();

			//assert the response
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			CLNsMessagesGetResponseBody body = response.as(CLNsMessagesGetResponseBody.class);
			body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body, "events should be published");

			List<CLN> all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 15);
			for (int i = 0; i < 6; i++)
			{
				Assert.assertEquals(all.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(all.get(i).getCaseReferences().size(), 100, "case references size is not matching");
				for (int j = 0; j < 100; j++)
				{
					Assert.assertTrue(all.get(i).getCaseReferences().contains(caseRefsAccounts.get(j + (i * 100))),
							"case references is not matching");
				}
			}
			for (int i = 6; i < 15; i++)
			{
				Assert.assertEquals(all.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(all.get(i).getCaseReferences().size(), 10, "case references size is not matching");
				for (int j = 0; j < 10; j++)
				{
					Assert.assertTrue(
							all.get(i).getCaseReferences().contains(caseRefsCustomers.get(j + ((i - 6) * 10))),
							"case references is not matching");
				}
			}

			List<CLN> del = body.getDeleted().getClns();
			Assert.assertNotNull(del, "cln should not be null");
			Assert.assertEquals(del.size(), 15);
			for (int i = 0; i < 6; i++)
			{
				Assert.assertEquals(del.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(del.get(i).getCaseReferences().size(), 100, "case references size is not matching");
				for (int j = 0; j < 100; j++)
				{
					Assert.assertTrue(del.get(i).getCaseReferences().contains(caseRefsAccounts.get(j + (i * 100))),
							"case references is not matching");
				}
			}
			for (int i = 6; i < 15; i++)
			{
				Assert.assertEquals(del.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(del.get(i).getCaseReferences().size(), 10, "case references size is not matching");
				for (int j = 0; j < 10; j++)
				{
					Assert.assertTrue(
							del.get(i).getCaseReferences().contains(caseRefsCustomers.get(j + ((i - 6) * 10))),
							"case references is not matching");
				}
			}

			List<CLN> updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 15);
			for (int i = 0; i < 6; i++)
			{
				Assert.assertEquals(updDel.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 100,
						"case references size is not matching");
				for (int j = 0; j < 100; j++)
				{
					Assert.assertTrue(updDel.get(i).getCaseReferences().contains(caseRefsAccounts.get(j + (i * 100))),
							"case references is not matching");
				}
			}
			for (int i = 6; i < 15; i++)
			{
				Assert.assertEquals(updDel.get(i).getEvent(), "DELETED", "cln event should be delated");

				Assert.assertEquals(updDel.get(i).getCaseReferences().size(), 10,
						"case references size is not matching");
				for (int j = 0; j < 10; j++)
				{
					Assert.assertTrue(
							updDel.get(i).getCaseReferences().contains(caseRefsCustomers.get(j + ((i - 6) * 10))),
							"case references is not matching");
				}
			}
			assertEmptyResponse(response, "updated");
		}
		finally
		{
			cln.disableMessages();
			forceUndeploy(deploymentIdApp1);
		}
	}

	//Test to verify update, delete notifications are sent correctly on multi-event 
	@Test(description = "Test to verify update, delete notifications are sent correctly on multi-event")
	public void testCaseLifeCycleNotificationMultiEvent() throws IOException, InterruptedException,
			PersistenceException, DeploymentException, InternalException, CasedataException, ReferenceException,
			ArgumentException, URISyntaxException, RuntimeApplicationException, DataModelSerializationException
	{
		BigInteger deploymentIdApp1 = BigInteger.valueOf(7);
		BigInteger deploymentIdApp2 = BigInteger.valueOf(8);
		try
		{
			List<String> caseRefsAccountsA1 = new ArrayList<>();
			List<String> caseRefsUpdatedAccountsA1 = new ArrayList<>();
			List<String> caseRefsIntermediateAccountsA1 = new ArrayList<>();
			List<String> caseRefsUpdatedCustomersA1 = new ArrayList<>();
			List<String> caseRefsCustomersA1 = new ArrayList<>();
			List<String> caseRefsAccountsA2 = new ArrayList<>();
			List<String> caseRefsUpdatedAccountsA2 = new ArrayList<>();
			List<String> caseRefsUpdatedCustomersA2 = new ArrayList<>();
			List<String> caseRefsCustomersA2 = new ArrayList<String>();

			List<String> casedata = new ArrayList<String>();

			List<String> tempRefs = new ArrayList<>();
			JsonPath jsonPath = null;

			//enable cln notifications, read all messages and keep it enabled
			cln.flushNotifications(false, true);

			//deploy the application
			deploymentIdApp1 = deploy("/apps/Accounts/AccountsDataModel");

			//create cases for account
			//create 4 accounts
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(1, 1, "@FROZEN&"));
			tempRefs.addAll(accountCreator.createAccountSpecificStateOffsetDefaultCases(2, 1, "@ACTIVE&"));
			tempRefs.addAll(
					accountCreator.createAccountSpecificStateOffsetDefaultCases(3, 1, "@WAITING FOR APPROVAL&"));
			tempRefs.addAll(
					accountCreator.createAccountSpecificStateOffsetDefaultCases(4, 1, "@WAITING FOR CANCELLATION&"));
			caseRefsAccountsA1.addAll(tempRefs);
			tempRefs.clear();

			//create 5 customers
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "VERYACTIVE50KABOVE"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "SELDOM0TO1K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "SELDOM0TO1K"));
			tempRefs.addAll(customerCreator.createCustomerSpecificStateOffsetDefaultCases(1, "TRIAL"));
			caseRefsCustomersA1.addAll(tempRefs);
			tempRefs.clear();

			deployAsNewApplication("2.0.0.1");

			String casedataAccountA2 = "{\"state\": \"@ACTIVE&\", \"accountID\": 12345679, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11" + "}";

			//create cases for account
			casedata.add(casedataAccountA2);
			response = casesFunctions.createCases(caseTypeAccount, 2, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsAccountsA2.add(jsonPath.getString("[" + 0 + "]"));
			casedata.clear();

			String casedataCustomerA2 = "{\"professionalDetails\":[{}],\"personalDetails\":{\"homeAddress\":{}}}";

			casedata.add(casedataCustomerA2);
			response = casesFunctions.createCases(caseTypeCustomer, 2, casedata);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsCustomersA2.add(jsonPath.getString("[" + 0 + "]"));
			casedata.clear();

			//update account 0th & 1st cases - 5 times
			tempRefs = accountCreator.updateAccountsArrayData(caseRefsAccountsA1.subList(0, 2), 5, "accounts", true);
			caseRefsUpdatedAccountsA1.add(tempRefs.get(8));
			caseRefsUpdatedAccountsA1.add(tempRefs.get(9));
			caseRefsIntermediateAccountsA1.addAll(tempRefs);
			tempRefs.clear();
			//update customer 0th case - 1 time
			caseRefsUpdatedCustomersA1 = customerCreator.customerChangState(caseRefsCustomersA1, "VERYACTIVE50KABOVE",
					"TRIAL");

			//update account 2 - 0th case - 1
			String casedataAccountA2Update = "{\"state\": \"@ACTIVE&\", \"accountID\": 12345679, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Bhubneshwar\", \"county\": \"Odisa\", \"country\": \"INDIA\", \"postCode\": \"415003\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11" + "}";

			response = casesFunctions.updateSingleCase(caseRefsAccountsA2.get(0), casedataAccountA2Update);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsUpdatedAccountsA2.add(jsonPath.getString("[" + 0 + "]"));

			//update customer 2 - 0th case - 1
			caseRefsUpdatedCustomersA2 = customerCreator.customerChangState(caseRefsCustomersA2, "TRIAL",
					"VERYACTIVE50KABOVE");

			//update account 1 - 2nd case state update @WAITING FOR APPROVAL& to @WAITING FOR CANCELLATION&
			tempRefs = casesFunctions.updateChangePayload(caseRefsAccountsA1.get(2), "@WAITING FOR APPROVAL&",
					"@WAITING FOR CANCELLATION&");
			caseRefsUpdatedAccountsA1.add(tempRefs.get(0));
			tempRefs.clear();

			//update customer 1 - 1st case state update VERYACTIVE50KABOVE to SELDOM0TO1K
			tempRefs = casesFunctions.updateChangePayload(caseRefsCustomersA1.get(1), "VERYACTIVE50KABOVE",
					"SELDOM0TO1K");
			caseRefsUpdatedCustomersA1.add(tempRefs.get(0));
			tempRefs.clear();

			//delete account 1	- 0th account
			response = casesFunctions.purgeSingleCase(caseRefsUpdatedAccountsA1.get(0));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//delete customer 2  - 0th customer
			response = casesFunctions.purgeSingleCase(caseRefsUpdatedCustomersA2.get(0));
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "1", "incorrect number of cases are purged");

			//delete all accounts 1 - state @WAITING FOR CANCELLATION& (2nd and 3rd accounts deleted)
			purgeAccountCases("@WAITING FOR CANCELLATION&");

			//delete all customer 1 - state SELDOM0TO1K (1st, 2nd and 3rd customers deleted)
			purgeCustomerCases("SELDOM0TO1K");

			//get events
			response = cln.readMessages();

			//assert the response
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			CLNsMessagesGetResponseBody body = response.as(CLNsMessagesGetResponseBody.class);
			body = response.as(CLNsMessagesGetResponseBody.class);
			Assert.assertNotNull(body, "events should be published");

			List<CLN> all = body.getAll().getClns();
			Assert.assertNotNull(all, "cln should not be null");
			Assert.assertEquals(all.size(), 14);

			List<CLN> upd = body.getUpdated().getClns();
			Assert.assertNotNull(upd, "cln should not be null");
			Assert.assertEquals(upd.size(), 10);

			List<CLN> del = body.getDeleted().getClns();
			Assert.assertNotNull(del, "cln should not be null");
			Assert.assertEquals(del.size(), 4);

			List<CLN> updDel = body.getUpdatedAndDeleted().getClns();
			Assert.assertNotNull(updDel, "cln should not be null");
			Assert.assertEquals(updDel.size(), 14);

			assertUpdated(all, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsIntermediateAccountsA1, caseRefsUpdatedCustomersA1, caseRefsUpdatedAccountsA2,
					caseRefsCustomersA1, 0, 10);

			assertDeleted(all, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsUpdatedCustomersA1, caseRefsCustomersA1, 10, 4);

			assertDeleted(del, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsUpdatedCustomersA1, caseRefsCustomersA1, 0, 4);

			assertUpdated(upd, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsIntermediateAccountsA1, caseRefsUpdatedCustomersA1, caseRefsUpdatedAccountsA2,
					caseRefsCustomersA1, 0, 10);

			assertUpdated(updDel, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsIntermediateAccountsA1, caseRefsUpdatedCustomersA1, caseRefsUpdatedAccountsA2,
					caseRefsCustomersA1, 0, 10);

			assertDeleted(updDel, caseRefsUpdatedAccountsA1, caseRefsUpdatedCustomersA2, caseRefsAccountsA1,
					caseRefsUpdatedCustomersA1, caseRefsCustomersA1, 10, 4);

		}
		finally
		{
			cln.disableMessages();
			forceUndeploy(deploymentIdApp2);
			forceUndeploy(deploymentIdApp1);
		}
	}
}
