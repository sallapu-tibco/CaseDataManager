package com.tibco.bpm.cdm.rest.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesGetResponseBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.CasesPutRequestBodyItem;
import com.tibco.bpm.cdm.api.rest.v1.model.Link;
import com.tibco.bpm.cdm.api.rest.v1.model.LinksPostRequestBody;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.CaseLinksFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DirectoryEngineFunctions;
import com.tibco.bpm.cdm.rest.functions.GetPropertiesFunction;
import com.tibco.bpm.cdm.rest.functions.RestDeploymentFunctions;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class SystemActionNegativeTest extends Utils
{

	private JsonPath					jsonPath				= null;

	private Response					response				= null;

	private static final String			caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String			caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String			applicationMajorVersion	= "1";

	private List<String>				caseReferenceCustomers	= new ArrayList<>();

	private List<String>				caseReferenceAccounts	= new ArrayList<>();

	private String						linkName				= "holdstheaccounts";

	private RestDeploymentFunctions		rest					= new RestDeploymentFunctions();

	private DirectoryEngineFunctions	de						= new DirectoryEngineFunctions();

	private CasesFunctions				cases					= new CasesFunctions();

	private GetPropertiesFunction		properties				= new GetPropertiesFunction();

	private CaseTypesFunctions			types					= new CaseTypesFunctions();

	private CaseLinksFunction			links					= new CaseLinksFunction();

	private BigInteger					deploymentId			= BigInteger.valueOf(8);

	private String[]					createCustomer			= {
			"{\"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}",
			"{\"state\": \"VERYACTIVE50KABOVE\", \"personalDetails\": {\"age\": 10, \"gender\": \"MALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MR\", \"dateofBirth\": \"2009-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 16.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 10234}]}",
			"{\"state\": \"VERYACTIVE50KABOVE\", \"personalDetails\": {\"age\": 13, \"gender\": \"MALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"JUSTICE\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 167.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 1024}]}",
			"{\"state\": \"TRIAL\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"state\": \"REGULAR1KTO10K\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"state\": \"ACTIVEBUTONHOLD\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}"};

	private String[]					readCustomer			= {
			"{\"customerCID\":\"WINTERFELL-00001\",\"state\": \"ACTIVE10KTO50K\", \"personalDetails\": {\"age\": 7, \"gender\": \"PREFER NOT TO SAY\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2008-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"LW13 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 164.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 7449698697}]}",
			"{\"customerCID\":\"WINTERFELL-00002\",\"state\": \"VERYACTIVE50KABOVE\", \"personalDetails\": {\"age\": 10, \"gender\": \"MALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MR\", \"dateofBirth\": \"2009-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 16.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 10234}]}",
			"{\"customerCID\":\"WINTERFELL-00003\",\"state\": \"VERYACTIVE50KABOVE\", \"personalDetails\": {\"age\": 13, \"gender\": \"MALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"JUSTICE\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 167.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 1024}]}",
			"{\"customerCID\":\"WINTERFELL-00004\",\"state\": \"TRIAL\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"customerCID\":\"WINTERFELL-00005\",\"state\": \"REGULAR1KTO10K\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"customerCID\":\"WINTERFELL-00006\",\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}",
			"{\"customerCID\":\"WINTERFELL-00007\",\"state\": \"ACTIVEBUTONHOLD\", \"personalDetails\": {\"age\": 13, \"gender\": \"FEMALE\", \"lastName\": \"Unpopular blue meal\", \"firstName\": \"Deflated grey vacuum cleaner\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging aqua squeezebox\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive aqua shoes\", \"postCode\": \"SN1 8SL\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 13.8}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 104}]}"};

	private String[]					summaryCreatedCustomer	= {
			"{\"customerCID\":\"WINTERFELL-00001\",\"state\": \"ACTIVE10KTO50K\"}",
			"{\"customerCID\":\"WINTERFELL-00002\",\"state\": \"VERYACTIVE50KABOVE\"}",
			"{\"customerCID\":\"WINTERFELL-00003\",\"state\": \"VERYACTIVE50KABOVE\"}",
			"{\"customerCID\":\"WINTERFELL-00004\",\"state\": \"TRIAL\"}",
			"{\"customerCID\":\"WINTERFELL-00005\",\"state\": \"REGULAR1KTO10K\"}",
			"{\"customerCID\":\"WINTERFELL-00006\",\"state\": \"SELDOM0TO1K\"}",
			"{\"customerCID\":\"WINTERFELL-00007\",\"state\": \"ACTIVEBUTONHOLD\"}"};

	private String[]					updateCustomer			= {
			"{\"customerCID\":\"WINTERFELL-00001\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 8, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00002\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 11, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00003\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 14, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00004\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 15, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00005\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 16, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00006\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 17, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00007\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 18, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}"};

	private String[]					createAccount			= {
			"{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11}",
			"{\"state\": \"@ACTIVE&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Satara\", \"county\": \"MAHARASHTRA\", \"country\": \"INDIA\", \"postCode\": \"415001\", \"firstLine\": \"467/7A, Delight Colony\", "
					+ "\"secondLine\": \"Sadar-Bazar\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 991212.11}"};

	//deploy org model and data project
	private void prerequisites() throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		try
		{
			response = rest.deployApplication(RESOURCE_ADMIN, "/apps/OrgModel/AceProject3-Organisational.rasc");
			deploymentId = deploy("/apps/Accounts/AccountsDataModel");
		}
		catch (Exception e)
		{
			System.out.println("exception in deployment");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
	}

	//create 5 cases as tibco-admin
	private List<String> prerequisitesCreateCases() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> refs = new ArrayList<>();

		try
		{
			List<String> casedata = new ArrayList<>();
			casedata.add(createCustomer[0]);
			casedata.add(createCustomer[1]);
			casedata.add(createCustomer[2]);
			casedata.add(createCustomer[3]);
			casedata.add(createCustomer[4]);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_ADMIN);

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 5; caseIterator++)
			{
				refs.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating cases");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
		return refs;
	}

	//create 5 cases as tibco-admin
	private List<String> prerequisitesCreateCasesAccounts() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> refs = new ArrayList<>();

		try
		{
			List<String> casedata = new ArrayList<>();
			casedata.add(createAccount[0]);
			casedata.add(createAccount[1]);
			response = cases.createCases(caseTypeAccount, 1, casedata, RESOURCE_ADMIN);

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 2; caseIterator++)
			{
				refs.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating cases");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
		return refs;
	}

	private void verifyUnsuccessfulCreate(int expectedCount) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		//read as an admin user
		response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_ADMIN);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), expectedCount, "cases must have been created by unauthorized user");

		//read as a different user
		de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_READ);

		response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_LIAM);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), expectedCount, "cases must have been created by unauthorized user");

	}

	private void verifyUnsuccessfulUpdate(List<String> caseRefs) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		//read as an admin user
		response = cases.getCases("EQ", filterMap, SELECT_CASES_C_CR, "", "10", "", RESOURCE_ADMIN);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), caseRefs.size(), "cases must have been maipulated unauthorized user");

		for (int i = 0; i < obj.size(); i++)
		{
			//cases are read (got) in reverse order of creation
			//since they are sorted by modification timestamp
			Assert.assertEquals(obj.get(i).getCaseReference(), caseRefs.get(4 - i),
					"case reference has changed due to update");
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[" + i + "].casedata"), readCustomer[(4 - i)]),
					"casedata has changed due to update");
			Assert.assertFalse(assertUnsortedData(jsonPath.getString("[" + i + "].casedata"), updateCustomer[(4 - i)]),
					"casedata has changed due to update");
		}

		//read as a different user
		de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_READ);

		response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE, "", "10", "", RESOURCE_LIAM);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), caseRefs.size(), "cases must have been maipulated unauthorized user");

		for (int i = 0; i < obj.size(); i++)
		{
			//cases are read (got) in reverse order of creation
			//since they are sorted by modification timestamp
			Assert.assertEquals(obj.get(i).getCaseReference(), caseRefs.get(4 - i),
					"case reference has changed due to update");
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("[" + i + "].casedata"), readCustomer[(4 - i)]),
					"casedata has changed due to update");
			Assert.assertFalse(assertUnsortedData(jsonPath.getString("[" + i + "].casedata"), updateCustomer[(4 - i)]),
					"casedata has changed due to update");
		}

	}

	private void verifyUnsuccessfulDelete(int expectedCount) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		//read as an admin user
		response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_ADMIN);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), expectedCount, "cases must have been deleted user");

		//read as a different user
		de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_READ);

		response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_LIAM);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
		Assert.assertEquals(obj.size(), expectedCount, "cases must have been deleted user");
	}

	private LinksPostRequestBody formulateBodyForLinkCases()
	{
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		for (int caseRefIterator = 0; caseRefIterator < 2; caseRefIterator++)
		{
			Link link = new Link();
			link.setCaseReference(caseReferenceAccounts.get(caseRefIterator));
			link.setName(linkName);
			linksPostRequestBody.add(link);
		}

		return linksPostRequestBody;
	}

	private void verifyCasesLinked(Response response) throws JsonParseException, JsonMappingException, IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
		Assert.assertEquals(obj.size(), 2, "multiple cases are returned");

		for (int caseRefIterator = 0; caseRefIterator < 2; caseRefIterator++)
		{
			Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(), caseReferenceAccounts.get(caseRefIterator),
					"linked case reference is incorrect");
			Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
		}
	}

	private void verifyCasesUnlinked(Response response) throws JsonParseException, JsonMappingException, IOException
	{
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
		Assert.assertEquals(obj.size(), 0, "multiple links are returned");
	}

	//Negative Test to verify system action for create a single case
	@Test(description = "Negative Test to verify system action for create a single case")
	public void testSystemActionCreateSingle() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		try
		{
			prerequisites();

			List<String> casedata = new ArrayList<>();
			casedata.add(createCustomer[0]);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulCreate(0);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}
	//Negative Test to verify system action for create a multiple cases

	//Negative Test to verify system action for create a multiple cases
	@Test(description = "Negative Test to verify system action for create a multiple cases")
	public void testSystemActionCreateMultiple() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		try
		{
			prerequisites();

			List<String> casedata = new ArrayList<>();
			casedata.add(createCustomer[0]);
			casedata.add(createCustomer[1]);
			casedata.add(createCustomer[2]);
			casedata.add(createCustomer[3]);
			casedata.add(createCustomer[4]);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE_DELETE);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulCreate(0);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for update with case ref param
	@Test(description = "Negative Test to verify system action for update with case ref param")
	public void testSystemActionUpdateWithCaseRefParam() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();
		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_DELETE);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.updateSingleCase(caseRef.get(0), updateCustomer[0], RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulUpdate(caseRef);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for update single case
	@Test(description = "Negative Test to verify system action for update single case")
	public void testSystemActionUpdateSingle() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();
		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_DELETE);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.updateCases(caseRef.get(0), updateCustomer[0], RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulUpdate(caseRef);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for update multiple cases
	@Test(description = "Negative Test to verify system action for update multiple cases")
	public void testSystemActionUpdateMultiple() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();
		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			CasesPutRequestBody body = new CasesPutRequestBody();
			for (int i = 0; i < 5; i++)
			{
				CasesPutRequestBodyItem item = new CasesPutRequestBodyItem();
				item.setCasedata(updateCustomer[i]);
				item.setCaseReference(caseRef.get(i));
				body.add(item);
			}

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_DELETE);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.arrayUpdateCases(body, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.arrayUpdateCases(body, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulUpdate(caseRef);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for delete multiple cases
	@Test(description = "Negative Test to verify system action for delete multiple cases")
	public void testSystemActionDeleteMultiple() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("caseState", "VERYACTIVE50KABOVE");

		try
		{
			prerequisites();
			prerequisitesCreateCases();

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_UPDATE);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.purgeCases(filterMap, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.purgeCases(filterMap, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulDelete(5);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for delete with case ref param
	@Test(description = "Negative Test to verify system action for delete with case ref param")
	public void testSystemActionDeleteWithCaseRefParam() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();

		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.purgeSingleCase(caseRef.get(0), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.purgeSingleCase(caseRef.get(1), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = cases.purgeSingleCase(caseRef.get(1), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.purgeSingleCase(caseRef.get(1), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.purgeSingleCase(caseRef.get(2), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = cases.purgeSingleCase(caseRef.get(3), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.purgeSingleCase(caseRef.get(4), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_UPDATE);
			response = cases.purgeSingleCase(caseRef.get(0), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_UPDATE);
			response = cases.purgeSingleCase(caseRef.get(1), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.purgeSingleCase(caseRef.get(2), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.purgeSingleCase(caseRef.get(0), RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			verifyUnsuccessfulDelete(5);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			de.deleteResource(CONTAINER_NAME, RESOURCE_LIAM);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type and major version
	@Test(description = "Negative Test to verify system action for read multiple cases with case type and major version")
	public void testSystemActionReadMultipleWithTypeAndVersion()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select c
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,cr
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_CR, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,cr,m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_CR_M, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,cr,m,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_CR_M_S, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_M, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s,cr
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S_CR, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s,m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S_M, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select cr,m
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CR_M, skip, top, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 5, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type, major version and cid
	@Test(description = "Negative Test to verify system action for read multiple cases with case type, major version and cid")
	public void testSystemActionReadMultipleWithTypeVersionAndCid()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("cid", "WINTERFELL-00001");
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select s,cr
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_S_CR, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select s,cr,m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_S_CR_M, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select s,m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_S_M, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select m
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_M, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, metadata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY, skip, top,
					"", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "false", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type, major version and state
	@Test(description = "Negative Test to verify system action for read multiple cases with case type, major version and state")
	public void testSystemActionReadMultipleWithTypeVersionAndState()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{

		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("caseState", "VERYACTIVE50KABOVE");
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select casedata, summary, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY_METADATA, skip, top, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEREFERENCE, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEREFERENCE_METADATA, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_METADATA, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE, skip, top, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, metadata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY, skip, top,
					"", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "false", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type, major version and modification timestamp
	@Test(description = "Negative Test to verify system action for read multiple cases with case type, major version and modification timestamp")
	public void testSystemActionReadMultipleWithTypeVersionAndModificationTS()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("modificationTimestamp", cases.DEFAULT_MODIFICATION_TIME);
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 5, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type, major version state and modification timestamp
	@Test(description = "Negative Test to verify system action for read multiple cases with case type, major version state and modification timestamp")
	public void testSystemActionReadMultipleWithTypeVersionStateAndModificationTS()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("modificationTimestamp", cases.DEFAULT_MODIFICATION_TIME);
		filterMap.put("caseState", "VERYACTIVE50KABOVE");
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//selecting summary and casedata since this call is used by the client
			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "false", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read multiple cases with case type, major version cid state and modification timestamp
	@Test(description = "Negative Test to verify system action for read multiple cases with case type, major version cid state and modification timestamp")
	public void testSystemActionReadMultipleWithTypeVersionCidStateAndModificationTS()
			throws IOException, URISyntaxException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		filterMap.put("cid", "WINTERFELL-00002");
		filterMap.put("modificationTimestamp", cases.DEFAULT_MODIFICATION_TIME);
		filterMap.put("caseState", "VERYACTIVE50KABOVE");
		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "false", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("EQ", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read single case reference
	@Test(description = "Negative Test to verify system action for read single case reference")
	public void testSystemActionReadSingle() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();
		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_CASEDATA, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_SUMMARY, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_CASEREFERENCE, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY,
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_C, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_S, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_CR, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c, cr, m, s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_C_CR_M_S, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_C, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES_CASEDATA, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getSingleCase(caseRef.get(0), SELECT_CASES, RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), readCustomer[0]),
					"casedata does not match");
			Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), summaryCreatedCustomer[0]),
					"summary does not match");
			Assert.assertEquals(jsonPath.getString("caseReference"), caseRef.get(0), "case reference does not match");
			//metadata is not asserted here
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for read case reference in
	@Test(description = "Negative Test to verify system action for read case reference in")
	public void testSystemActionReadCaseRefIn() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRef = new ArrayList<>();

		//for case reference In filter skip and top are not allowed
		String skip = "";
		String top = "";

		String caseRefIn = "";
		try
		{
			prerequisites();
			caseRef = prerequisitesCreateCases();

			caseRefIn = "('" + caseRef.get(0) + "','" + caseRef.get(1) + "','" + caseRef.get(2) + "')";

			Map<String, String> filterMap = new HashMap<String, String>();
			filterMap.put("caseReference", caseRefIn);

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_CASEDATA, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY, skip, top,
					"", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_C, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCases("IN", filterMap, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCases("IN", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c, cr, m, s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCases("IN", filterMap, SELECT_CASES_C_CR_M_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCases("IN", filterMap, SELECT_CASES, skip, top, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCases("IN", filterMap, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for search
	@Test(description = "Negative Test to verify system action for search")
	public void testSystemActionSearch() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		String searchString = "WINTERFELL-00001";

		//for case reference In filter skip and top are not allowed
		String skip = "0";
		String top = "10";

		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA, skip, top, searchString, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_SUMMARY, skip, top, searchString, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY, skip, top, searchString, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, skip, top,
					searchString, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY, skip,
					top, searchString, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_C, skip, top, searchString, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_S, skip, top, searchString, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_C_S, skip, top, searchString, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top, searchString, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c, cr, m, s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_C_CR_M_S, skip, top, searchString, "false",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top, searchString, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA_CASEREFERENCE, skip, top, searchString,
					"", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, summary
			response = cases.searchCases("EQ", filterMap, SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, skip, top,
					searchString, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.searchCases("EQ", filterMap, SELECT_CASES, skip, top, searchString, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 1, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for findBy(criteria)
	@Test(description = "Negative Test to verify system action for findBy(criteria)")
	public void testSystemActionFindBy() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		//dql filter for case state
		Map<String, String> filterDql = new HashMap<String, String>();
		filterDql.put("state", "'VERYACTIVE50KABOVE'");

		//for case reference In filter skip and top are not allowed
		String skip = "0";
		String top = "10";

		try
		{
			prerequisites();
			prerequisitesCreateCases();

			//select casedata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_CASEDATA, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_SUMMARY, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_CASEDATA_SUMMARY, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, skip,
					top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, summary, caseReference, metadata
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql,
					SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_C, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_C_S, skip, top, "", RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select c, cr, m, s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_C_CR_M_S, skip, top, "",
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES, skip, top, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_CASEDATA_CASEREFERENCE, skip, top, "",
					RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select casedata, caseReference, summary
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, skip,
					top, "", RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = cases.getCasesDQL("EQ", filterMap, filterDql, SELECT_CASES, skip, top, "", RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2, "cases cannot be read by an authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for types
	@Test(description = "Negative Test to verify system action for types")
	public void testSystemActionReadTypes() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		String skip = "0";
		String top = "10";

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("isCase", "TRUE");
		filterMap.put("namespace", "com.example.bankdatamodel");
		filterMap.put("applicationId", "com.example.bankdatamodel");
		filterMap.put("applicationMajorVersion", applicationMajorVersion);
		try
		{
			prerequisites();

			//select b
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select b,s
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B_S, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select b,s,sa
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B_S_SA, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select b,s,sa,a,l
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B_S_SA_L, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select b,s,sa,a,l,d
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B_S_SA_D_L, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select basic
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_Basic, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_Links, skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//all select 
			response = types.getTypes(filterMap, SELECT_CASE_TYPES_B_SA_A_D_L, skip, top, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//select all - authorized user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = types.getTypes(filterMap, SELECT_CASE_TYPES, skip, top, RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			String repsonseJson = response.getBody().asString();
			TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);
			Assert.assertEquals(body.size(), 2, "types cannot be fetched by authorized user");
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//Negative Test to verify system action for create links
	@Test(description = "Negative Test to verify system action for create links")
	public void testSystemActionCreateLinks() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";

		try
		{
			prerequisites();
			caseReferenceCustomers = prerequisitesCreateCases();
			caseReferenceAccounts = prerequisitesCreateCasesAccounts();

			LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases();

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_DELETE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//create links with appropriate user
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_CLINT);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			//response is an empty string
			//jsonPath = response.jsonPath();
			//System.out.println(jsonPath.getString("")); // useful for debugging
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//verify links - not possible without read action
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//verify links - possible with read action
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			verifyCasesLinked(response);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//combinations of different filters used
	//- get links without filter
	//- get links with filter name
	//Negative Test to verify system action for read(get) links and navigateBy(criteria)
	@Test(description = "Negative Test to verify system action for read(get) links and navigateBy(criteria)")
	public void testSystemActionGetLinksAndNavigate() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{

		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";

		try
		{
			prerequisites();
			caseReferenceCustomers = prerequisitesCreateCases();
			caseReferenceAccounts = prerequisitesCreateCasesAccounts();

			LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases();

			//Tony can create, read and delete links
			de.mapResources(CONTAINER_NAME, RESOURCE_TONY, POSITION_READ_UPDATE_DELETE);

			//Tony links the cases
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//Tony gets the links - he has created
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_TONY);
			System.out.println(response.asString());
			verifyCasesLinked(response);

			//filter is empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//add values in filter
			filterMap.put("name", linkName);

			//filter is NOT empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is NOT empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is NOT empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is NOT empty
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//filter is NOT empty
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//verify links - possible with read action
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			verifyCasesLinked(response);

			//clear the filter
			filterMap.clear();

			//tests for dql - navigateBy(criteria)
			//add values in filter
			filterMap.put("name", linkName);

			//add values in DQL
			filterDql.put("accountType", "'CURRENT'");
			filterDql.put("state", "'@ACTIVE&'");

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_UPDATE_DELETE);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//verify navigateBy(criteria) - possible with read action
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			verifyCasesLinked(response);

			//assertion for empty results
			filterDql.clear();
			filterDql.put("accountID", "3");

			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			//mis-leading assertion but there should be no cases for the given criteria
			verifyCasesUnlinked(response);

			filterDql.clear();
			filterMap.clear();
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

	//TODO insterted by nashtapu [Jul 29, 2019 3:23:38 PM] 
	//There might be a change in future the required system action for this call to be - UPDATE and not DELETE
	//At the moment, it is tested with DELETE system action
	//Unlink has 4 variations 
	//- unlink without filter and casereference
	//- unlink with filter name
	//- unlink with filter name and casereference
	//- unlink with filter name and casereference in()
	//all 4 variations are tried here in this test for some combination of all system actions
	//Negative Test to verify system action for delete links (unlink)
	@Test(description = "Negative Test to verify system action for delete links (unlink)")
	public void testSystemActionUnlink() throws IOException, URISyntaxException, InterruptedException,
			DeploymentException, PersistenceException, InternalException, RuntimeApplicationException
	{

		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";
		try
		{
			prerequisites();
			caseReferenceCustomers = prerequisitesCreateCases();
			caseReferenceAccounts = prerequisitesCreateCasesAccounts();

			LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
			linksPostRequestBody = formulateBodyForLinkCases();

			//Tony can create, read and delete links
			de.mapResources(CONTAINER_NAME, RESOURCE_TONY, POSITION_READ_UPDATE_DELETE);

			//Tony links the cases
			response = links.createLinks(caseReferenceCustomers.get(0), linksPostRequestBody, RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "", "checking for empty response");

			//Tony gets the links - he has created
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_TONY);
			System.out.println(response.asString());
			verifyCasesLinked(response);

			//unlink without filter and casereference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), "", "EQ", caseReferenceAccounts,
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink without filter and casereference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_DELETE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), "", "EQ", caseReferenceAccounts,
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_DELETE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name and casereference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_DELETE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", caseReferenceAccounts,
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name and casereference
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", caseReferenceAccounts,
					RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name and casereference in()
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ_DELETE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "IN",
					caseReferenceAccounts.subList(0, 2), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name and casereference in()
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_NONE_ACTIONS);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "IN",
					caseReferenceAccounts.subList(0, 2), RESOURCE_CLINT);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//unlink with filter name
			de.createResource(CONTAINER_NAME, RESOURCE_JON);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_JON);
			System.out.println(response.asString());
			properties.assertErrorMethodNotAllowed(response);

			//delete links - possible with delete action
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			response = links.deleteLinks(caseReferenceCustomers.get(0), linkName, "EQ", new ArrayList<>(),
					RESOURCE_CLINT);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "2", "checking response for number of links deleted");

			//verify links - possible with read action
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			response = links.getLinks(filterMap, filterDql, caseReferenceCustomers.get(0), skip, top, RESOURCE_CLINT);
			System.out.println(response.asString());
			verifyCasesUnlinked(response);
		}
		finally
		{
			de.deleteResource(CONTAINER_NAME, RESOURCE_CLINT);
			de.deleteResource(CONTAINER_NAME, RESOURCE_JON);
			rest.undeployApplication("", "com.example.aceproject3organisational");
			forceUndeploy(deploymentId);
		}
	}

}