/*
* ENVIRONMENT:    Java Generic
*
* COPYRIGHT:      (C) 2016 TIBCO Software Inc
*/
package com.tibco.bpm.cdm.test.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.dto.CaseReference;
import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.NotAuthorisedException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.exception.ReferenceException;
import com.tibco.bpm.cdm.api.exception.ValidationException;
import com.tibco.bpm.cdm.core.CaseManager;
import com.tibco.bpm.cdm.core.deployment.DeploymentManager;
import com.tibco.bpm.cdm.test.BaseTest;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

/*
 * 
 * =====================================================
 * TYPE : OrderCreator
 * =====================================================
 */
/**
 *
 * TODO Give a useful description of the type.
 *
 *
 * <p/>&copy;2016 TIBCO Software Inc.
 * @author jaugusti
 * @since TODO
 */
public class PolicyCreator extends BaseTest
{
	@Autowired
	@Qualifier("deploymentManager")
	private DeploymentManager		deploymentManager;

	@Autowired
	@Qualifier("caseManager")
	private CaseManager				caseManager;

	public static Random			randomGenerator	= new Random();

	public static ArrayList<String>	states			= new ArrayList<String>();

	public static ArrayList<String>	firstnames		= new ArrayList<String>();

	public static ArrayList<String>	lastnames		= new ArrayList<String>();

	public static ArrayList<String>	orderDates		= new ArrayList<String>();

	public static ArrayList<String>	comments		= new ArrayList<String>();

	public static ArrayList<String>	orderTime		= new ArrayList<String>();

	public static ArrayList<String>	orderValues		= new ArrayList<String>();

	static
	{
		//populate status
		states.add("CREATED");
		states.add("CANCELLED");

		//populate first name to be replaced
		firstnames.add("Peter Jackson");
		firstnames.add("Fred Cool");
		firstnames.add("Josh Jacky");
		firstnames.add("Bob the builder");
		firstnames.add("Vicky");
		firstnames.add("Noel");
		firstnames.add("Megan");
		firstnames.add("Liam");
		firstnames.add("Alison");
		firstnames.add("Arthur");
		firstnames.add("Alex");
		firstnames.add("Jodie");
		firstnames.add("Logan");
		firstnames.add("Holly");
		firstnames.add("Benny");

		lastnames.add("débâcle");
		lastnames.add("Bloggs");
		lastnames.add("Bloch");
		lastnames.add("Panos");
		lastnames.add("Cains");
		lastnames.add("Lawrence");
		lastnames.add("Mathis");
		lastnames.add("Davidson");
		lastnames.add("Silvijo");
		lastnames.add("Pearce");
		lastnames.add("Walton");
		lastnames.add("Baxter");
		lastnames.add("Lawson");
		lastnames.add("Isabel");
		lastnames.add("Carol");

		orderValues.add("20.11");
		orderValues.add("70.10");
		orderValues.add("99.99");
		orderValues.add("0.02");

		orderDates.add("2000-01-01");
		orderDates.add("2001-02-02");
		orderDates.add("2002-03-03");
		orderDates.add("2003-04-04");
		orderDates.add("2004-05-05");
		orderDates.add("2004-06-06");
		orderDates.add("2005-07-07");
		orderDates.add("2006-08-08");
		orderDates.add("2006-09-09");
		orderDates.add("2006-10-10");
		orderDates.add("2007-11-11");
		orderDates.add("2008-12-12");
		orderDates.add("1990-01-13");
		orderDates.add("1991-02-14");
		orderDates.add("1992-03-15");
		orderDates.add("1993-04-16");
		orderDates.add("1994-05-17");
		orderDates.add("1995-06-18");
		orderDates.add("1996-07-19");
		orderDates.add("1997-08-20");
		orderDates.add("1998-09-21");
		orderDates.add("1999-10-22");
		orderDates.add("2000-11-23");
		orderDates.add("2001-12-24");

		comments.add("This could be a d�b�cle");
		comments.add(
				"really long comment - Matches documents with fields that have terms within a certain range. The type of the Lucene query depends on the field type, for string fields, the TermRangeQuery, while for number/date fields, the query is a NumericRangeQuery. The following example returns all documents where");
		//comments.add("This is utter �$%^&**(");
		comments.add("This policy was registered on 12/01/2009 ");
		comments.add("15 Clerkenwell Rd");
		comments.add("45-47 Cornhill");

		orderTime.add("10:10:10");
		orderTime.add("11:11:11");
		orderTime.add("12:12:12");
		orderTime.add("13:13:13");
		orderTime.add("14:14:14");
		orderTime.add("15:15:15");
		orderTime.add("16:16:16");
		orderTime.add("17:17:17");
		orderTime.add("18:18:18");
		orderTime.add("19:19:19");
		orderTime.add("20:20:20");
		orderTime.add("21:21:21");
		orderTime.add("22:22:22");
		orderTime.add("23:23:23");
		orderTime.add("23:59:59");
	}

	public static String getRandomOrderState()
	{
		int index = randomGenerator.nextInt(states.size());
		return states.get(index);

	}

	public static String getRandomFirstName()
	{
		int index = randomGenerator.nextInt(firstnames.size());
		return firstnames.get(index);

	}

	public static String getRandomLastName()
	{
		int index = randomGenerator.nextInt(lastnames.size());
		return lastnames.get(index);

	}

	public static String getRandomOrderDate()
	{
		int index = randomGenerator.nextInt(orderDates.size());
		return orderDates.get(index);

	}

	public static String getRandomOrderTime()
	{
		int index = randomGenerator.nextInt(orderTime.size());
		return orderTime.get(index);

	}

	public static String getRandomComments()
	{
		int index = randomGenerator.nextInt(comments.size());
		return comments.get(index);

	}

	public static String getRandomOrderValue()
	{
		int index = randomGenerator.nextInt(orderValues.size());
		return orderValues.get(index);

	}

	public static void main(String[] args) throws Exception
	{

		for (int i = 0; i < 1; i++)
		{
			System.out.println(generatePolicyJSON());

		}

	}

	static AtomicInteger	integer	= new AtomicInteger();

	static AtomicInteger	age		= new AtomicInteger();

	static AtomicInteger	number	= new AtomicInteger();

	public static String generatePolicyJSON()
	{
		String order = FileUtils.readFileContents("casedata/app1/policy_parameterised.json");

		String policyState = PolicyCreator.getRandomOrderState();
		String policyTime = PolicyCreator.getRandomOrderTime();
		String policyDate = PolicyCreator.getRandomOrderDate();
		String comments = PolicyCreator.getRandomComments();

		String content1 = order.replaceAll("POLICYNUMBERTOBEREPLACED", Integer.toString((number.incrementAndGet())));
		String content2 = content1.replaceFirst("STATETOBEREPLACED", policyState);
		String content3 = content2.replaceFirst("DATETOBEREPLACED", policyDate);
		String content4 = content3.replaceFirst("TIMETOBEREPLACED", policyTime);
		// Must be >0 as model has min value 0, exclusive.
		String content5 = content4.replaceFirst("PREMIUMTOBEREPLACED",
				new BigDecimal(Math.random()).setScale(2, RoundingMode.DOWN).add(new BigDecimal("0.01")).toString());
		String content6 = content5.replaceAll("COMMENTSTOBEREPLACED", comments);
		String content7 = content6.replaceFirst("QUANTITYTOBEREPLACED", Integer.toString((integer.incrementAndGet())));

		return content7;
	}

	@Test
	public void createPolicies() throws IOException, URISyntaxException, RuntimeApplicationException,
			PersistenceException, DataModelSerializationException, DeploymentException, InternalException,
			ReferenceException, ArgumentException, ValidationException, NotAuthorisedException
	{

		int numPolicies = 10;

		List<String> cases = new ArrayList<>();
		for (int i = 0; i < numPolicies; i++)
		{

			cases.add(PolicyCreator.generatePolicyJSON());
		}
		List<CaseReference> refs = caseManager.createCases(new QualifiedTypeName("org.policycorporation.Policy"), 1,
				cases);
		System.out.println("Cases created: " + refs);

	}

}
