package com.tibco.bpm.cdm.rest.functions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.path.json.JsonPath;
import com.tibco.bpm.cdm.test.BaseTest;

public class Utils extends BaseTest
{
	protected static final String	REST_USERNAME																	= "tibco-admin";

	protected static final String	REST_PASSWORD																	= "secret";

	protected static final String	REST_PASSWORD_USER																= "tibco123";

	private static String			hostname_property																= System
			.getProperty("docker.ip");

	private static String			port_property																	= System
			.getProperty("docker.port");

	public static final String		host																			= null == hostname_property
			? "localhost" : hostname_property;

	public static final String		port																			= null == port_property
			? "8181" : port_property;

	protected static final String	baseUrlBPM																		= "http://"
			+ host + ":" + port + "/bpm";

	protected static final String	baseUrl																			= "http://"
			+ host + ":" + port + "/bpm/case/v1";

	public static final String		URL_CM_TYPES																	= baseUrl
			+ "/types";

	public static final String		URL_CASES																		= baseUrl
			+ "/cases";

	public static final String		URL_YY																			= baseUrlBPM
			+ "/yy";

	public static final String		URL_YY_CLN																		= URL_YY
			+ "/clns";

	public static final String		URL_YY_CLN_ENABLE																= URL_YY_CLN
			+ "?enabled=true";

	public static final String		URL_YY_CLN_DISABLE																= URL_YY_CLN
			+ "?enabled=false";

	public static final String		URL_YY_CLN_MESSAGES																= URL_YY_CLN
			+ "/messages";

	public static final String		URL_SINGLE_CASES																= URL_CASES
			+ "/{caseReference}";

	public static final String		URL_CM_LINKS																	= URL_SINGLE_CASES
			+ "/links";

	//use for rest deployment
	public static final String		URL_DEPLOYMENTS																	= baseUrlBPM
			+ "/deploy/v1/deployments";

	//use for operations on containers
	public static final String		URL_DE																			= baseUrlBPM
			+ "/de/v1";

	public static final String		URL_DE_LDAP_CONTAINERS															= URL_DE
			+ "/ldapContainers";

	public static final String		URL_DE_LDAP_CONTAINER															= URL_DE_LDAP_CONTAINERS
			+ "/{containerId}";

	public static final String		URL_DE_LDAP_CONTAINER_RESOURCES													= URL_DE_LDAP_CONTAINERS
			+ "/listCandidateResources";

	//use for operations on resources
	public static final String		URL_RESOURCES																	= URL_DE
			+ "/resources";

	public static final String		URL_RESOURCES_DETAILS															= URL_RESOURCES
			+ "/{guid}";

	public static final String		CONTAINER_NAME																	= "TestContainer";

	public static final int			validStatusCode																	= 200;

	public static final int			invalidStatusCode																= 400;

	public static final int			notFoundStatusCode																= 404;

	//types can have 'b,sa,s,ad,l' in the select filter

	public static final String		SELECT_CASE_TYPES																= "";

	public static final String		SELECT_CASE_TYPES_B																= "b";

	public static final String		SELECT_CASE_TYPES_S																= "s";

	public static final String		SELECT_CASE_TYPES_SA															= "sa";

	public static final String		SELECT_CASE_TYPES_A																= "a";

	public static final String		SELECT_CASE_TYPES_D																= "d";

	public static final String		SELECT_CASE_TYPES_L																= "l";

	public static final String		SELECT_CASE_TYPES_B_S															= "b,s";

	public static final String		SELECT_CASE_TYPES_B_SA															= "b,sa";

	public static final String		SELECT_CASE_TYPES_B_A															= "b,a";

	public static final String		SELECT_CASE_TYPES_B_D															= "b,d";

	public static final String		SELECT_CASE_TYPES_S_SA															= "s,sa";

	public static final String		SELECT_CASE_TYPES_S_A															= "s,a";

	public static final String		SELECT_CASE_TYPES_S_D															= "s,d";

	public static final String		SELECT_CASE_TYPES_SA_A															= "sa,a";

	public static final String		SELECT_CASE_TYPES_SA_D															= "sa,d";

	public static final String		SELECT_CASE_TYPES_A_D															= "a,d";

	public static final String		SELECT_CASE_TYPES_B_S_SA														= "b,s,sa";

	public static final String		SELECT_CASE_TYPES_B_S_A															= "b,s,a";

	public static final String		SELECT_CASE_TYPES_B_S_D															= "b,s,d";

	public static final String		SELECT_CASE_TYPES_B_SA_A														= "b,sa,a";

	public static final String		SELECT_CASE_TYPES_B_SA_D														= "b,sa,d";

	public static final String		SELECT_CASE_TYPES_B_A_D															= "b,a,d";

	public static final String		SELECT_CASE_TYPES_S_SA_A														= "s,sa,a";

	public static final String		SELECT_CASE_TYPES_S_SA_D														= "s,sa,d";

	public static final String		SELECT_CASE_TYPES_SA_A_D														= "sa,a,d";

	public static final String		SELECT_CASE_TYPES_S_A_D															= "s,a,d";

	public static final String		SELECT_CASE_TYPES_B_S_SA_A														= "b,s,sa,a";

	public static final String		SELECT_CASE_TYPES_B_S_SA_D														= "b,s,sa,d";

	public static final String		SELECT_CASE_TYPES_S_SA_A_D														= "s,sa,a,d";

	public static final String		SELECT_CASE_TYPES_B_SA_A_D														= "b,sa,a,d";

	public static final String		SELECT_CASE_TYPES_B_S_A_D														= "b,s,a,d";

	public static final String		SELECT_CASE_TYPES_B_S_SA_A_D													= "b,s,sa,a,d";

	public static final String		SELECT_CASE_TYPES_B_L															= "b,l";

	public static final String		SELECT_CASE_TYPES_S_L															= "s,l";

	public static final String		SELECT_CASE_TYPES_SA_L															= "sa,l";

	public static final String		SELECT_CASE_TYPES_A_L															= "a,l";

	public static final String		SELECT_CASE_TYPES_D_L															= "d,l";

	public static final String		SELECT_CASE_TYPES_B_S_L															= "b,s,l";

	public static final String		SELECT_CASE_TYPES_B_SA_L														= "b,sa,l";

	public static final String		SELECT_CASE_TYPES_B_A_L															= "b,a,l";

	public static final String		SELECT_CASE_TYPES_B_D_L															= "b,d,l";

	public static final String		SELECT_CASE_TYPES_S_SA_L														= "s,sa,l";

	public static final String		SELECT_CASE_TYPES_S_A_L															= "s,a,l";

	public static final String		SELECT_CASE_TYPES_S_D_L															= "s,d,l";

	public static final String		SELECT_CASE_TYPES_SA_A_L														= "sa,a,l";

	public static final String		SELECT_CASE_TYPES_SA_D_L														= "sa,d,l";

	public static final String		SELECT_CASE_TYPES_A_D_L															= "a,d,l";

	public static final String		SELECT_CASE_TYPES_B_S_SA_L														= "b,s,sa,l";

	public static final String		SELECT_CASE_TYPES_B_S_A_L														= "b,s,a,l";

	public static final String		SELECT_CASE_TYPES_B_S_D_L														= "b,s,d,l";

	public static final String		SELECT_CASE_TYPES_B_SA_A_L														= "b,sa,a,l";

	public static final String		SELECT_CASE_TYPES_B_SA_D_L														= "b,sa,d,l";

	public static final String		SELECT_CASE_TYPES_B_A_D_L														= "b,a,d,l";

	public static final String		SELECT_CASE_TYPES_S_SA_A_L														= "s,sa,a,l";

	public static final String		SELECT_CASE_TYPES_S_SA_D_L														= "s,sa,d,l";

	public static final String		SELECT_CASE_TYPES_SA_A_D_L														= "sa,a,d,l";

	public static final String		SELECT_CASE_TYPES_S_A_D_L														= "s,a,d,l";

	public static final String		SELECT_CASE_TYPES_B_S_SA_A_L													= "b,s,sa,a,l";

	public static final String		SELECT_CASE_TYPES_B_S_SA_D_L													= "b,s,sa,d,l";

	public static final String		SELECT_CASE_TYPES_S_SA_A_D_L													= "s,sa,a,d,l";

	public static final String		SELECT_CASE_TYPES_B_SA_A_D_L													= "b,sa,a,d,l";

	public static final String		SELECT_CASE_TYPES_B_S_A_D_L														= "b,s,a,d,l";

	public static final String		SELECT_CASE_TYPES_B_S_SA_A_D_L													= "b,s,sa,a,d,l";

	//types can have 'basic,summaryAttribute,summary,attributes,dependencies, links' in the select filter

	public static final String		SELECT_CASE_TYPES_Basic															= "basic";

	public static final String		SELECT_CASE_TYPES_States														= "states";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes												= "summaryAttributes";

	public static final String		SELECT_CASE_TYPES_Attributes													= "attributes";

	public static final String		SELECT_CASE_TYPES_Dependencies													= "dependencies";

	public static final String		SELECT_CASE_TYPES_Links															= "links";

	public static final String		SELECT_CASE_TYPES_Basic_States													= "basic,states";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes										= "basic,summaryAttributes";

	public static final String		SELECT_CASE_TYPES_Basic_Attributes												= "basic,attributes";

	public static final String		SELECT_CASE_TYPES_Basic_Dependencies											= "basic,dependencies";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes										= "states,summaryAttributes";

	public static final String		SELECT_CASE_TYPES_States_Attributes												= "states,attributes";

	public static final String		SELECT_CASE_TYPES_States_Dependencies											= "states,dependencies";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Attributes									= "summaryAttributes,attributes";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Dependencies								= "summaryAttributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Attributes_Dependencies										= "attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes								= "basic,states,summaryAttributes";

	public static final String		SELECT_CASE_TYPES_Basic_States_Attributes										= "basic,states,attributes";

	public static final String		SELECT_CASE_TYPES_Basic_States_Dependencies										= "basic,states,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes							= "basic,summaryAttributes,attributes";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Dependencies							= "basic,summaryAttributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_Attributes_Dependencies									= "basic,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Attributes							= "states,summaryAttributes,attributes";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Dependencies							= "states,summaryAttributes,dependencies";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Attributes_Dependencies						= "summaryAttributes,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_States_Attributes_Dependencies								= "states,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes						= "basic,states,summaryAttributes,attributes";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Dependencies					= "basic,states,summaryAttributes,dependencies";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Dependencies				= "states,summaryAttributes,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Dependencies				= "basic,summaryAttributes,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_States_Attributes_Dependencies							= "basic,states,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Dependencies		= "basic,states,summaryAttributes,attributes,dependencies";

	public static final String		SELECT_CASE_TYPES_Basic_Links													= "basic,links";

	public static final String		SELECT_CASE_TYPES_States_Links													= "states,links";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Links										= "summaryAttributes,links";

	public static final String		SELECT_CASE_TYPES_Attributes_Links												= "attributes,links";

	public static final String		SELECT_CASE_TYPES_Dependencies_Links											= "dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_Links											= "basic,states,links";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Links									= "basic,summaryAttributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_Attributes_Links										= "basic,attributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_Dependencies_Links										= "basic,dependencies,links";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Links								= "states,summaryAttributes,links";

	public static final String		SELECT_CASE_TYPES_States_Attributes_Links										= "states,attributes,links";

	public static final String		SELECT_CASE_TYPES_States_Dependencies_Links										= "states,dependencies,links";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Attributes_Links							= "summaryAttributes,attributes,links";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Dependencies_Links							= "summaryAttributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Attributes_Dependencies_Links									= "attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Links							= "basic,states,summaryAttributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_Attributes_Links									= "basic,states,attributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_Dependencies_Links								= "basic,states,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Links						= "basic,summaryAttributes,attributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Dependencies_Links					= "basic,summaryAttributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_Attributes_Dependencies_Links							= "basic,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Links						= "states,summaryAttributes,attributes,links";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Dependencies_Links					= "states,summaryAttributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_SummaryAttributes_Attributes_Dependencies_Links				= "summaryAttributes,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_States_Attributes_Dependencies_Links							= "states,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Links				= "basic,states,summaryAttributes,attributes,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Dependencies_Links				= "basic,states,summaryAttributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_States_SummaryAttributes_Attributes_Dependencies_Links		= "states,summaryAttributes,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_SummaryAttributes_Attributes_Dependencies_Links			= "basic,summaryAttributes,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_Attributes_Dependencies_Links					= "basic,states,attributes,dependencies,links";

	public static final String		SELECT_CASE_TYPES_Basic_States_SummaryAttributes_Attributes_Dependencies_Links	= "basic,states,summaryAttributes,attributes,dependencies,links";

	//cases can have 'casedata, summary, careReference or metadata' in the select filter 

	public static final String		SELECT_CASES																	= "";

	public static final String		SELECT_CASES_C																	= "c";

	public static final String		SELECT_CASES_S																	= "s";

	public static final String		SELECT_CASES_CR																	= "cr";

	public static final String		SELECT_CASES_M																	= "m";

	public static final String		SELECT_CASES_C_S																= "c,s";

	public static final String		SELECT_CASES_C_CR																= "c,cr";

	public static final String		SELECT_CASES_C_M																= "c,m";

	public static final String		SELECT_CASES_S_CR																= "s,cr";

	public static final String		SELECT_CASES_S_M																= "s,m";

	public static final String		SELECT_CASES_CR_M																= "cr,m";

	public static final String		SELECT_CASES_C_S_CR																= "c,s,cr";

	public static final String		SELECT_CASES_C_S_M																= "c,s,m";

	public static final String		SELECT_CASES_C_CR_M																= "c,cr,m";

	public static final String		SELECT_CASES_S_CR_M																= "s,cr,m";

	public static final String		SELECT_CASES_C_CR_M_S															= "c,cr,m,s";

	public static final String		SELECT_CASES_CASEDATA															= "casedata";

	public static final String		SELECT_CASES_SUMMARY															= "summary";

	public static final String		SELECT_CASES_CASEREFERENCE														= "caseReference";

	public static final String		SELECT_CASES_METADATA															= "metadata";

	public static final String		SELECT_CASES_CASEDATA_SUMMARY													= "casedata,summary";

	public static final String		SELECT_CASES_CASEDATA_CASEREFERENCE												= "casedata,caseReference";

	public static final String		SELECT_CASES_CASEDATA_METADATA													= "casedata,metadata";

	public static final String		SELECT_CASES_SUMMARY_CASEREFERENCE												= "summary,caseReference";

	public static final String		SELECT_CASES_SUMMARY_METADATA													= "summary,metadata";

	public static final String		SELECT_CASES_CASEREFERENCE_METADATA												= "caseReference,metadata";

	public static final String		SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE										= "casedata,summary,caseReference";

	public static final String		SELECT_CASES_CASEDATA_SUMMARY_METADATA											= "casedata,summary,metadata";

	public static final String		SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA									= "casedata,caseReference,metadata";

	public static final String		SELECT_CASES_SUMMARY_CASEREFERENCE_METADATA										= "summary,caseReference,metadata";

	public static final String		SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY							= "casedata,caseReference,metadata,summary";

	public static final String		RESOURCE_ADMIN																	= "tibco-admin";

	public static final String		RESOURCE_CLINT																	= "Clint Hill";

	public static final String		RESOURCE_JON																	= "Jon Parkin";

	public static final String		RESOURCE_JOHN																	= "John Eustace";

	public static final String		RESOURCE_LIAM																	= "Liam Lawrence";

	public static final String		RESOURCE_LEON																	= "Leon Court";

	public static final String		RESOURCE_RICH																	= "Richard Cresswell";

	public static final String		RESOURCE_STEVE																	= "Steve Simonsen";

	public static final String		RESOURCE_TONY																	= "Tony Pulis";

	public static final String		POSITION_NONE_ACTIONS															= "_bITT0JgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE																	= "_Vyg64JgKEemuXdPsdF_iWg";

	public static final String		POSITION_DELETE																	= "_SI1ecJgKEemuXdPsdF_iWg";

	public static final String		POSITION_UPDATE																	= "_SrNywJgKEemuXdPsdF_iWg";

	public static final String		POSITION_READ																	= "_T1rawJgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_READ															= "_YV2aUJgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_DELETE															= "_WPLowJgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_UPDATE															= "_Wti_wJgKEemuXdPsdF_iWg";

	public static final String		POSITION_UPDATE_DELETE															= "_TVL80JgKEemuXdPsdF_iWg";

	public static final String		POSITION_READ_DELETE															= "_UVC3UJgKEemuXdPsdF_iWg";

	public static final String		POSITION_READ_UPDATE															= "_U2tZ8JgKEemuXdPsdF_iWg";

	public static final String		POSITION_READ_UPDATE_DELETE														= "_VVDi0JgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_UPDATE_DELETE													= "_X6eF4JgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_READ_DELETE														= "_Y4a74JgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_READ_UPDATE														= "_ZWyS4JgKEemuXdPsdF_iWg";

	public static final String		POSITION_CREATE_READ_UPDATE_DELETE												= "_Z4qQ4JgKEemuXdPsdF_iWg";

	public static final String		POSITION_TYPES_WITH_NO_READ														= "_EqfkENSREem1AJuverY7Lg";

	public static final String		POSITION_READ_WITH_NO_TYPES														= "_EwDZgNSREem1AJuverY7Lg";

	//sleep time for some calls
	public static final Integer		CASES_TO_UPDATE																	= 200;

	public static final Integer		CASES_TO_PURGE																	= 100;

	//empty value specifically used to pass the parameter with empty value
	//e.g. '?$select=', '?$top='
	public static final String		EMPTY_VALUE																		= "empty value sent for testing no value";

	GetPropertiesFunction			properties																		= new GetPropertiesFunction();

	/**
	 * This method ignores CR/LF/spaces and returns true if the Strings match close enough
	 *
	 * @param result
	 * @param expectedResult
	 * @return true if a match
	 */

	public static boolean closeEnough(String a, String b)
	{
		String a2 = a.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').replace(" ", "");
		String b2 = b.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').replace(" ", "");

		boolean closeEnough = a2.equals(b2);

		if (!closeEnough)
		{
			closeEnough = matchMaps(a, b, true);
		}

		return closeEnough;
	}

	public static String sanitize(String a)
	{
		String a2 = a.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').replace("\\\\", "");
		return a2;
	}

	static boolean matchMaps(String jsonExpected, String jsonActual)
	{
		return matchMaps(jsonExpected, jsonActual, false);
	}

	static boolean matchMaps(String jsonExpected, String jsonActual, boolean noAssert)
	{
		// cannot rely upon order of key values - so use a map

		HashMap<String, Object> mapExpected = JsonPath.with(jsonExpected).get("");
		HashMap<String, Object> mapActual = JsonPath.with(jsonActual).get("");

		if (null == mapExpected || null == mapActual)
		{
			return false;
		}

		boolean metaMatch = false;

		for (String key : mapExpected.keySet())
		{
			Object value = mapExpected.get(key);

			if (null == value)
			{
				return false;
			}

			if (mapActual.get(key) instanceof ArrayList) // probably encapsulated value [ { "_value": value, "_id": id } ]
			{
				metaMatch = true;

				ArrayList<Object> actual = (ArrayList) mapActual.get(key);

				ArrayList<Object> expected = (ArrayList) mapExpected.get(key);

				for (int i = 0; i < actual.size(); i++)
				{
					Object actObj = actual.get(i);

					if (actObj instanceof HashMap)
					{
						HashMap<String, Object> actMap = (HashMap<String, Object>) actObj;

						if (actMap.containsKey("_value")) // only need to match against this value - ignore _id
						{
							String actValue = (String) actMap.get("_value");

							Object expObj = expected.get(i);

							HashMap<String, Object> expMap = (HashMap<String, Object>) expObj;

							String expValue = (String) expMap.get("_value");

							if (!actValue.equals(expValue))
							{
								metaMatch = false;

								System.err.println("Expected array " + expected + " but got array " + actual);
								break;
							}
						}
					}
					else
					{
						metaMatch = false;
					}
				}

				if (!metaMatch)
				{
					break;
				}
			}
			else if (!value.equals(mapActual.get(key)))
			{
				if (noAssert)
				{
					System.err.println("For " + key + " expected " + value + " not " + mapActual.get(key));

					break;
				}
				System.err.println("Expected:\t" + jsonExpected + "\nActual:  \t" + jsonActual);

				assert false : "For " + key + " expected " + value + " not " + mapActual.get(key);
			}
		}

		if (metaMatch)
		{
			return true;
		}

		return mapActual.equals(mapExpected);
	}

	public boolean assertUnsortedData(String payloadToRead, String payloadToMatch) throws IOException

	{
		JsonNode readTree = om.readTree(payloadToRead);
		JsonNode readTree2 = om.readTree(payloadToMatch);
		return readTree.equals(readTree2);
		//Assert.assertTrue(readTree.equals(readTree2));
	}

	public static String readFileContents(String fileLocation)
	{

		InputStream inputStream = Utils.class.getResourceAsStream(fileLocation);
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		String lineSeparator = System.getProperty("line.separator");

		StringBuilder s = new StringBuilder();

		while (scanner.hasNextLine())
		{

			s.append(scanner.nextLine());
			s.append(lineSeparator);

		}

		scanner.close();

		return s.toString();

	}

	public String generateRandomString(int length)
	{
		// chose a Character random from this String 
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString 
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++)
		{

			// generate a random number between 
			// 0 to AlphaNumericString variable length 
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb 
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public String generateFixedString(int length)
	{
		// chose a Character random from this String 
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "!#~%^*()" + "abcdefghijklmnopqrst";

		// create StringBuffer size of AlphaNumericString 
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++)
		{
			// add Character one by one in end of sb 
			sb.append(AlphaNumericString.charAt(i));
		}
		return sb.toString();
	}

}