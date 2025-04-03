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
import com.tibco.bpm.cdm.api.rest.v1.model.TypeInfo;
import com.tibco.bpm.cdm.api.rest.v1.model.TypesGetResponseBody;
import com.tibco.bpm.cdm.rest.functions.CaseLinksFunction;
import com.tibco.bpm.cdm.rest.functions.CaseTypesFunctions;
import com.tibco.bpm.cdm.rest.functions.CasesFunctions;
import com.tibco.bpm.cdm.rest.functions.DirectoryEngineFunctions;
import com.tibco.bpm.cdm.rest.functions.RestDeploymentFunctions;
import com.tibco.bpm.cdm.rest.functions.Utils;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class SystemActionPositiveTest extends Utils
{

	private Response					response				= null;

	private static final String			caseTypeCustomer		= "com.example.bankdatamodel.Customer";

	private static final String			caseTypeAccount			= "com.example.bankdatamodel.Account";

	private static final String			applicationMajorVersion	= "1";

	private String						linkName				= "holdstheaccounts";

	private RestDeploymentFunctions		rest					= new RestDeploymentFunctions();

	private DirectoryEngineFunctions	de						= new DirectoryEngineFunctions();

	private CasesFunctions				cases					= new CasesFunctions();

	private CaseTypesFunctions			types					= new CaseTypesFunctions();

	private CaseLinksFunction			links					= new CaseLinksFunction();

	private BigInteger					deploymentId			= BigInteger.valueOf(8);

	private List<String>				caseReferences			= new ArrayList<>();

	private List<String>				caseReferencesUpdated	= new ArrayList<>();

	private List<String>				caseReferencesAccounts	= new ArrayList<>();

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
			"{\"customerCID\":\"WINTERFELL-00001\",\"state\": \"REGULAR1KTO10K\", \"personalDetails\": {\"age\": 8, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1974-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00002\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 11, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1993-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00003\",\"state\": \"INACTIVE\", \"personalDetails\": {\"age\": 14, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00004\",\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 15, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00005\",\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 16, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00006\",\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 17, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}",
			"{\"customerCID\":\"WINTERFELL-00007\",\"state\": \"SELDOM0TO1K\", \"personalDetails\": {\"age\": 18, \"gender\": \"MALE\", \"lastName\": \"Unpopular\", \"firstName\": \"Deflated\", \"salutation\": \"MRS\", \"dateofBirth\": \"2019-08-22\", \"homeAddress\": {\"city\": \"Bulging\", \"county\": \"Miniature azure partition wall\", \"country\": \"Hyperactive\", \"postCode\": \"SN1 5AP\", \"firstLine\": \"18 Sudeley Avenue\", \"secondLine\": \"Devizes\"}, \"numberofYearsStayingattheAddress\": 5.1}, \"professionalDetails\": [{\"placeofWork\": {\"city\": \"Throbbing maroon steve\", \"county\": \"Excited red lawnmower\", \"country\": \"Wet pink photograph\", \"postCode\": \"CG70 6VW\", \"firstLine\": \"77 Simon Close\", \"secondLine\": \"London\"}, \"dateofJoining\": \"1995-10-04\", \"typeofEmployement\": \"Confident magenta pig\", \"annualIncomeSalaryBeforeTaxes\": 8118575271}, {\"placeofWork\": {\"city\": \"Adhesive platinum assets\", \"county\": \"Adhesive red flotilla\", \"country\": \"Braggadocious maroon raspberry pi\", \"postCode\": \"XW58 5WI\", \"firstLine\": \"10 Sudeley Grove\", \"secondLine\": \"Corsham\"}, \"dateofJoining\": \"2010-01-07\", \"typeofEmployement\": \"Regretful white plant\", \"annualIncomeSalaryBeforeTaxes\": 2371640567}, {\"placeofWork\": {\"city\": \"Braggadocious gold coffee machine\", \"county\": \"Stale turquoise enderdragon\", \"country\": \"Smelly hamlindigo blue lemon\", \"postCode\": \"RA9 8CG\", \"firstLine\": \"64 Mead Grove\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"2005-12-05\", \"typeofEmployement\": \"Volatile goldenrod games console\", \"annualIncomeSalaryBeforeTaxes\": 2772476414}, {\"placeofWork\": {\"city\": \"Mind-numbing olive steve\", \"county\": \"Damp green witch\", \"country\": \"Explosive black pig\", \"postCode\": \"Z51 4GS\", \"firstLine\": \"15 Potato Way\", \"secondLine\": \"Marlborough\"}, \"dateofJoining\": \"1985-02-17\", \"typeofEmployement\": \"Threatening gold adrian\", \"annualIncomeSalaryBeforeTaxes\": 2907355661}, {\"placeofWork\": {\"city\": \"Badly-organised violet donkey\", \"county\": \"Regretful blue board room\", \"country\": \"Pressurised scarlet fig\", \"postCode\": \"Y58 1HB\", \"firstLine\": \"22 Vicky Causeway\", \"secondLine\": \"Swindon\"}, \"dateofJoining\": \"1975-08-24\", \"typeofEmployement\": \"Sad salmon pink yoghurt\", \"annualIncomeSalaryBeforeTaxes\": 45000}]}"};

	private String[]					summaryUpdatedCustomer	= {
			"{\"customerCID\":\"WINTERFELL-00001\",\"state\": \"REGULAR1KTO10K\"}",
			"{\"customerCID\":\"WINTERFELL-00002\",\"state\": \"INACTIVE\"}",
			"{\"customerCID\":\"WINTERFELL-00003\",\"state\": \"INACTIVE\"}",
			"{\"customerCID\":\"WINTERFELL-00004\",\"state\": \"SELDOM0TO1K\"}",
			"{\"customerCID\":\"WINTERFELL-00005\",\"state\": \"SELDOM0TO1K\"}",
			"{\"customerCID\":\"WINTERFELL-00006\",\"state\": \"SELDOM0TO1K\"}",
			"{\"customerCID\":\"WINTERFELL-00007\",\"state\": \"SELDOM0TO1K\"}"};

	private String[]					createAccount			= {
			"{\"state\": \"@ACTIVE&\", \"accountID\": 1, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64004\", \"nameoftheInstitution\": \"ICICI BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN1 1AB\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2079-11-08\", \"dateofAccountOpening\": \"2019-04-26\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"17:24:45\", \"accountLiabilityHolding\": 1234.56}",
			"{\"state\": \"@ACTIVE&\", \"accountID\": 2, \"accountType\": \"CURRENT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64005\", \"nameoftheInstitution\": \"HSBC BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN2 2BC\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2080-11-08\", \"dateofAccountOpening\": \"2019-05-27\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"18:24:45\", \"accountLiabilityHolding\": 2345.67}",
			"{\"state\": \"@FROZEN&\", \"accountID\": 3, \"accountType\": \"FIXEDDEPOSIT\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64006\", \"nameoftheInstitution\": \"Loyds BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN3 3DE\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2081-11-08\", \"dateofAccountOpening\": \"2019-06-28\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"19:24:45\", \"accountLiabilityHolding\": 3456.78}",
			"{\"state\": \"@WAITING FOR APPROVAL&\", \"accountID\": 4, \"accountType\": \"CASHISA\","
					+ " \"institutionDetails\": {\"institutionCode\": \"ICIC64007\", \"nameoftheInstitution\": \"Natwest BANK Plc\", \"institutionBranchAddress\": "
					+ "{\"city\": \"Swindon\", \"county\": \"WILTSHIRE\", \"country\": \"UNITED KINGDOM\", \"postCode\": \"SN4 4FG\", \"firstLine\": \"Address Line 1\", "
					+ "\"secondLine\": \"Address Line 2\"}}, \"dateofAccountClosing\": \"2082-11-08\", \"dateofAccountOpening\": \"2019-07-29\", \"timeofAccountClosing\": \"02:25:31\", "
					+ "\"timeofAccountOpening\": \"20:24:45\", \"accountLiabilityHolding\": 4567.89}"};

	private String[]					selectArrayAbbreviated	= {SELECT_CASES, SELECT_CASES_C, SELECT_CASES_S,
			SELECT_CASES_CR, SELECT_CASES_M, SELECT_CASES_C_S, SELECT_CASES_C_CR, SELECT_CASES_C_M, SELECT_CASES_S_CR,
			SELECT_CASES_S_M, SELECT_CASES_CR_M, SELECT_CASES_C_S_CR, SELECT_CASES_C_S_M, SELECT_CASES_C_CR_M,
			SELECT_CASES_S_CR_M, SELECT_CASES_C_CR_M_S};

	private String[]					selectArray				= {SELECT_CASES, SELECT_CASES_CASEDATA,
			SELECT_CASES_SUMMARY, SELECT_CASES_CASEREFERENCE, SELECT_CASES_METADATA, SELECT_CASES_CASEDATA_SUMMARY,
			SELECT_CASES_CASEDATA_CASEREFERENCE, SELECT_CASES_CASEDATA_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE,
			SELECT_CASES_SUMMARY_METADATA, SELECT_CASES_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_SUMMARY_CASEREFERENCE, SELECT_CASES_CASEDATA_SUMMARY_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA, SELECT_CASES_SUMMARY_CASEREFERENCE_METADATA,
			SELECT_CASES_CASEDATA_CASEREFERENCE_METADATA_SUMMARY};

	private String[]					selectArrayTypes		= {SELECT_CASE_TYPES, SELECT_CASE_TYPES_B,
			SELECT_CASE_TYPES_S, SELECT_CASE_TYPES_SA, SELECT_CASE_TYPES_A, SELECT_CASE_TYPES_D, SELECT_CASE_TYPES_L,
			SELECT_CASE_TYPES_B_S, SELECT_CASE_TYPES_B_SA, SELECT_CASE_TYPES_B_A, SELECT_CASE_TYPES_B_D,
			SELECT_CASE_TYPES_S_SA, SELECT_CASE_TYPES_S_A, SELECT_CASE_TYPES_S_D, SELECT_CASE_TYPES_SA_A,
			SELECT_CASE_TYPES_SA_D, SELECT_CASE_TYPES_A_D, SELECT_CASE_TYPES_B_S_SA, SELECT_CASE_TYPES_B_S_A,
			SELECT_CASE_TYPES_B_S_D, SELECT_CASE_TYPES_B_SA_A, SELECT_CASE_TYPES_B_SA_D, SELECT_CASE_TYPES_B_A_D,
			SELECT_CASE_TYPES_S_SA_A, SELECT_CASE_TYPES_S_SA_D, SELECT_CASE_TYPES_SA_A_D, SELECT_CASE_TYPES_S_A_D,
			SELECT_CASE_TYPES_B_S_SA_A, SELECT_CASE_TYPES_B_S_SA_D, SELECT_CASE_TYPES_S_SA_A_D,
			SELECT_CASE_TYPES_B_SA_A_D, SELECT_CASE_TYPES_B_S_A_D, SELECT_CASE_TYPES_B_S_SA_A_D, SELECT_CASE_TYPES_B_L,
			SELECT_CASE_TYPES_S_L, SELECT_CASE_TYPES_SA_L, SELECT_CASE_TYPES_A_L, SELECT_CASE_TYPES_D_L,
			SELECT_CASE_TYPES_B_S_L, SELECT_CASE_TYPES_B_SA_L, SELECT_CASE_TYPES_B_A_L, SELECT_CASE_TYPES_B_D_L,
			SELECT_CASE_TYPES_S_SA_L, SELECT_CASE_TYPES_S_A_L, SELECT_CASE_TYPES_S_D_L, SELECT_CASE_TYPES_SA_A_L,
			SELECT_CASE_TYPES_SA_D_L, SELECT_CASE_TYPES_A_D_L, SELECT_CASE_TYPES_B_S_SA_L, SELECT_CASE_TYPES_B_S_A_L,
			SELECT_CASE_TYPES_B_S_D_L, SELECT_CASE_TYPES_B_SA_A_L, SELECT_CASE_TYPES_B_SA_D_L,
			SELECT_CASE_TYPES_B_A_D_L, SELECT_CASE_TYPES_S_SA_A_L, SELECT_CASE_TYPES_S_SA_D_L,
			SELECT_CASE_TYPES_SA_A_D_L, SELECT_CASE_TYPES_S_A_D_L, SELECT_CASE_TYPES_B_S_SA_A_L,
			SELECT_CASE_TYPES_B_S_SA_D_L, SELECT_CASE_TYPES_S_SA_A_D_L, SELECT_CASE_TYPES_B_SA_A_D_L,
			SELECT_CASE_TYPES_B_S_A_D_L, SELECT_CASE_TYPES_B_S_SA_A_D_L};

	private void testPrivilege(String resourceName, boolean canCreate, boolean canRead, boolean canUpdate,
			boolean canDelete) throws IOException, InterruptedException, DeploymentException, PersistenceException,
			InternalException, URISyntaxException, RuntimeApplicationException
	{
		caseReferences.clear();
		caseReferencesUpdated.clear();

		try
		{
			//deploy()
			deploymentId = deploy("/apps/Accounts/AccountsDataModel");

			//tests for create
			if (canCreate == true)
			{
				//create single case
				caseReferences.addAll(createSingleCaseAndVerify(resourceName));

				//create multiple cases
				caseReferences.addAll(createMultipleCasesAndVerify(resourceName));

				System.out.println("Tests for create are successful");
			}

			if (canRead == true)
			{
				//test for /types
				readTypes(resourceName);

				//cases have to be created first
				if (canCreate == false)
				{
					if (caseReferences.size() == 0)
					{
						//create the cases as Tony
						caseReferences.addAll(createCasesAsTony());
					}
				}

				readSingleCase(resourceName);
				readMultipleCasesWithDifferentFilters(resourceName);
				readMultipleCasesWithCaseRefIn(resourceName);
				findByCriteria(resourceName);
				searchCases(resourceName);

				System.out.println("Tests for read are successful");
			}

			//tests for update
			if (canUpdate == true)
			{
				if (canCreate == false && canRead == false)
				{
					//cannot use the case references from the above loop
					//make sure caseRefs are not created
					if (caseReferences.size() == 0)
					{
						//create the cases as Tony
						caseReferences.addAll(createCasesAsTony());

					}
				}

				//update single
				caseReferencesUpdated.addAll(updateWithCaseRefParamAndVerify(resourceName));

				//update array		
				caseReferencesUpdated.addAll(updateMultipleCasesAndVerify(resourceName));

				System.out.println("Tests for update are successful");
			}

			if (canDelete == true)
			{
				//unique case where cases have to be created in order to be deleted
				if (canRead == false && canCreate == false && canUpdate == false)
				{
					//create cases
					if (caseReferences.size() == 0)
					{
						//create the cases as Tony
						caseReferences.addAll(createCasesAsTony());
					}

				}

				//delete single
				deleteSingleCase(resourceName);

				//delete multiple
				deleteMultipleCases(resourceName);

				System.out.println("Tests for delete are successful");
			}
		}
		finally

		{
			caseReferences.clear();
			caseReferencesUpdated.clear();

			//delete resource
			de.deleteResource(CONTAINER_NAME, resourceName);

			//undeploy application
			forceUndeploy(deploymentId);
		}
	}

	private void testPrivilegeTypeAndRead(String resourceName, boolean canTypes, boolean canRead)
			throws IOException, InterruptedException, DeploymentException, PersistenceException, InternalException,
			URISyntaxException, RuntimeApplicationException
	{
		caseReferences.clear();

		try
		{
			//deploy()
			deploymentId = deploy("/apps/Accounts/AccountsDataModel");

			if (canTypes == true)
			{
				//test for /types
				readTypes(resourceName);

				System.out.println("Tests for /types are successful");
			}

			//cases have to be created first
			if (canRead == true)
			{
				if (caseReferences.size() == 0)
				{
					//create the cases as Tony
					caseReferences.addAll(createCasesAsTony());
				}

				readSingleCase(resourceName);
				readMultipleCasesWithDifferentFilters(resourceName);
				readMultipleCasesWithCaseRefIn(resourceName);
				findByCriteria(resourceName);
				searchCases(resourceName);

				System.out.println("Tests for read are successful");
			}
		}
		finally
		{
			caseReferences.clear();
			caseReferencesUpdated.clear();

			//delete resource
			de.deleteResource(CONTAINER_NAME, resourceName);

			//undeploy application
			forceUndeploy(deploymentId);
		}
	}

	private void testPrivilegeLinks(String resourceName, boolean canLink, boolean canGet, boolean canUnlink)
			throws IOException, InterruptedException, DeploymentException, PersistenceException, InternalException,
			URISyntaxException, RuntimeApplicationException
	{
		caseReferences.clear();
		caseReferencesAccounts.clear();

		try
		{
			//deploy()
			deploymentId = deploy("/apps/Accounts/AccountsDataModel");

			caseReferences.addAll(createCasesAsTony());
			caseReferencesAccounts.addAll(createAccountsAsTony());

			//tests for create links
			if (canLink == true)
			{
				//create multiple links
				createLinks(resourceName);

				System.out.println("Tests for create links are successful");
			}

			if (canGet == true)
			{
				//assuming links won't be created by now
				if (canLink == false)
				{
					//create the links as Tony
					createLinks(RESOURCE_TONY);
				}

				//links are already created
				//get links with empty filter
				getLinksVariations(resourceName, "NO_FILTER");

				//get links with filter for name
				getLinksVariations(resourceName, "NAME_FILTER");

				//navigateBy(criteria)
				getLinksVariations(resourceName, "DQL_FILTER");

				System.out.println("Tests for get links are successful");
			}

			//tests for update
			if (canUnlink == true)
			{
				if (canGet == false && canLink == false)
				{
					//links have to be created as Tony
					createLinks(RESOURCE_TONY);
				}

				//unlink with name and caseReferenceIN()
				unlinkVariations(resourceName, "NAME_CASE_REFERENCE_IN");

				//unlink with name and caseReference
				unlinkVariations(resourceName, "NAME_CASE_REFERENCE");

				//unlink with name
				unlinkVariations(resourceName, "NAME");

				//unlink with no name
				unlinkVariations(resourceName, "NO_NAME");

				System.out.println("Tests for unlink (delete links) are successful");
			}
		}
		finally

		{
			caseReferences.clear();
			caseReferencesUpdated.clear();

			//delete resource
			de.deleteResource(CONTAINER_NAME, resourceName);

			//undeploy application
			forceUndeploy(deploymentId);
		}
	}

	//deploy org model and data project
	private void prerequisites() throws IOException, URISyntaxException, DeploymentException, PersistenceException,
			InternalException, RuntimeApplicationException
	{
		try
		{
			response = rest.deployApplication("", "/apps/OrgModel/AceProject3-Organisational.rasc");
		}
		catch (Exception e)
		{
			System.out.println("exception in deployment");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
	}

	// create cases as Tony
	private List<String> createCasesAsTony() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRefsOutput = new ArrayList<>();

		try
		{
			List<String> casedata = new ArrayList<>();
			casedata.add(createCustomer[0]);
			casedata.add(createCustomer[1]);
			casedata.add(createCustomer[2]);
			response = cases.createCases(caseTypeCustomer, 1, casedata, RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 3; caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating customer cases");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
		return caseRefsOutput;
	}

	//create 4 accounts cases as Tony
	private List<String> createAccountsAsTony() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException
	{
		List<String> caseRefsOutput = new ArrayList<>();

		try
		{
			List<String> casedata = new ArrayList<>();
			casedata.add(createAccount[0]);
			casedata.add(createAccount[1]);
			casedata.add(createAccount[2]);
			casedata.add(createAccount[3]);
			response = cases.createCases(caseTypeAccount, 1, casedata, RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(response.asString()); // useful for debugging
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 4; caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in creating account cases");
			System.out.println(e.getMessage());
			Assert.fail("tests for system action cannot be performed");
		}
		return caseRefsOutput;
	}

	private void genericReadSingleCaseRefAsTony(String caseRef, String casedataToMatch, String SummaryToMatch)
			throws IOException, InterruptedException
	{

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		//read as an admin user
		response = cases.getSingleCase(caseRef, SELECT_CASES_C_S_CR, RESOURCE_TONY);
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), casedataToMatch),
				"casedata does not match");
		Assert.assertEquals(jsonPath.getString("caseReference"), caseRef, "caseRef should be tagged");
		Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), SummaryToMatch),
				"caseRef should be tagged");
	}

	//create single case
	private List<String> createSingleCaseAndVerify(String resourceName) throws IOException, InterruptedException
	{
		List<String> casedata = new ArrayList<>();
		List<String> caseRefOutput = new ArrayList<>();
		try
		{
			casedata.add(createCustomer[0]);
			response = cases.createCases(caseTypeCustomer, 1, casedata, resourceName);
			System.out.println(response.asString());

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 1; caseIterator++)
			{
				caseRefOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while creating cases");
			System.out.println(e.getMessage());
		}

		genericReadSingleCaseRefAsTony(caseRefOutput.get(0), readCustomer[0], summaryCreatedCustomer[0]);
		return caseRefOutput;
	}

	//create multiple cases
	private List<String> createMultipleCasesAndVerify(String resourceName) throws IOException, InterruptedException
	{
		List<String> casedata = new ArrayList<>();
		List<String> caseRefOutput = new ArrayList<>();
		try
		{
			casedata.add(createCustomer[1]);
			casedata.add(createCustomer[2]);
			response = cases.createCases(caseTypeCustomer, 1, casedata, resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while creating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 2; caseIterator++)
			{
				caseRefOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while creating cases");
			System.out.println(e.getMessage());
		}

		genericReadSingleCaseRefAsTony(caseRefOutput.get(0), readCustomer[1], summaryCreatedCustomer[1]);
		genericReadSingleCaseRefAsTony(caseRefOutput.get(1), readCustomer[2], summaryCreatedCustomer[2]);
		return caseRefOutput;
	}

	//update case ref
	private List<String> updateWithCaseRefParamAndVerify(String resourceName) throws IOException, InterruptedException
	{
		List<String> caseRefsInput = new ArrayList<>();
		List<String> caseRefsOutput = new ArrayList<>();

		caseRefsInput.add(caseReferences.get(0));

		try
		{
			response = cases.updateSingleCase(caseRefsInput.get(0), updateCustomer[0], resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			caseRefsOutput.add(jsonPath.getString("[" + 0 + "]"));
		}
		catch (Exception e)
		{
			System.out.println("problem while creating cases");
			System.out.println(e.getMessage());
		}

		genericReadSingleCaseRefAsTony(caseRefsOutput.get(0), updateCustomer[0], summaryUpdatedCustomer[0]);
		return caseRefsOutput;
	}

	//update multiple cases
	private List<String> updateMultipleCasesAndVerify(String resourceName) throws IOException, InterruptedException
	{
		List<String> caseRefsInput = new ArrayList<>();
		List<String> caseRefsOutput = new ArrayList<>();

		caseRefsInput.add(caseReferences.get(1));
		caseRefsInput.add(caseReferences.get(2));

		CasesPutRequestBody body = new CasesPutRequestBody();
		for (int i = 0; i < 2; i++)
		{
			CasesPutRequestBodyItem item = new CasesPutRequestBodyItem();
			item.setCasedata(updateCustomer[i + 1]);
			item.setCaseReference(caseRefsInput.get(i));
			body.add(item);
		}

		try
		{
			response = cases.arrayUpdateCases(body, resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging
			for (int caseIterator = 0; caseIterator < 2; caseIterator++)
			{
				caseRefsOutput.add(jsonPath.getString("[" + caseIterator + "]"));
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while updating cases");
			System.out.println(e.getMessage());
		}

		genericReadSingleCaseRefAsTony(caseRefsOutput.get(0), updateCustomer[1], summaryUpdatedCustomer[1]);
		genericReadSingleCaseRefAsTony(caseRefsOutput.get(1), updateCustomer[2], summaryUpdatedCustomer[2]);
		return caseRefsOutput;
	}

	private void readTypes(String resourceName)
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
			for (int selectIterator = 0; selectIterator < selectArrayTypes.length; selectIterator++)
			{
				response = types.getTypes(filterMap, selectArrayTypes[selectIterator], skip, top, resourceName);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + "skip = " + skip + " top = " + top + " select value = "
						+ selectArrayTypes[selectIterator]); // useful for debugging

				String repsonseJson = response.getBody().asString();
				TypesGetResponseBody body = om.readValue(repsonseJson, TypesGetResponseBody.class);

				// Expect quantity to be equal to 4 minus the skipped types.
				Assert.assertEquals(body.size(), 2, "incorrect number of objects returned");

				List<String> aspectNames = Arrays.asList(selectArrayTypes[selectIterator].split(","));

				TypeInfo info = new TypeInfo();

				for (int i = 0; i < body.size(); i++)
				{
					if (body.get(i).getName().equals("Customer")
							|| body.get(i).getSummaryAttributes().contains("customerCID")
							|| body.get(i).getAttributes().contains("customerCID")
							|| body.get(i).getStates().contains("VERYACTIVE50KABOVE")
							|| body.get(i).getLinks().contains("holdstheaccounts"))
					{
						//storing only the type for the Customer case
						info = body.get(i);
					}
				}

				if (aspectNames.contains("b") || aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(info.getName(), "Customer", "incorrect name");
					Assert.assertEquals(info.getLabel(), "Customer", "incorrect label");
					Assert.assertEquals(info.getIsCase().toString(), "true", "incorrect isCase");
					Assert.assertEquals(info.getNamespace(), "com.example.bankdatamodel", "incorrect namespace");
					Assert.assertEquals((int) info.getApplicationMajorVersion(), (int) 1,
							"incorrect application major version");
					Assert.assertEquals(info.getApplicationId(), "com.example.bankdatamodel",
							"incorrect application id");
				}
				if (aspectNames.contains("sa") || aspectNames.get(0).equalsIgnoreCase(""))
				{

					Assert.assertEquals(info.getSummaryAttributes().size(), 2, "incorrect summary attributes");
					Assert.assertEquals(info.getSummaryAttributes().stream()
							.filter(s -> s.getName().equals("customerCID") && s.getLabel().equals("Customer CID")
									&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
									&& s.getIsIdentifier().toString().equals("true")
									&& s.getIsSearchable().toString().equals("true")
									&& s.getIsSummary().toString().equals("true")
									&& s.getIsAutoIdentifier().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getSummaryAttributes().stream()
							.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
									&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
									&& s.getIsState().toString().equals("true")
									&& s.getIsSearchable().toString().equals("true")
									&& s.getIsSummary().toString().equals("true"))
							.count(), 1);
				}
				if (aspectNames.contains("a") || aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(info.getAttributes().size(), 4, "incorrect attributes size");

					Assert.assertEquals(info.getAttributes().stream()
							.filter(s -> s.getName().equals("customerCID") && s.getLabel().equals("Customer CID")
									&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
									&& s.getIsIdentifier().toString().equals("true")
									&& s.getIsSearchable().toString().equals("true")
									&& s.getIsSummary().toString().equals("true")
									&& s.getIsAutoIdentifier().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getAttributes().stream()
							.filter(s -> s.getName().equals("state") && s.getLabel().equals("State")
									&& s.getType().equals("Text") && s.getIsMandatory().toString().equals("true")
									&& s.getIsState().toString().equals("true")
									&& s.getIsSearchable().toString().equals("true")
									&& s.getIsSummary().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getAttributes().stream()
							.filter(s -> s.getName().equals("personalDetails")
									&& s.getLabel().equals("Personal Details") && s.getType().equals("PersonalDetails")
									&& s.getIsStructuredType().toString().equals("true")
									&& s.getIsMandatory().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getAttributes().stream()
							.filter(s -> s.getName().equals("professionalDetails")
									&& s.getLabel().equals("Professional Details")
									&& s.getType().equals("ProfessionalDetails")
									&& s.getIsStructuredType().toString().equals("true")
									&& s.getIsArray().toString().equals("true"))
							.count(), 1);
				}

				if (aspectNames.contains("s") || aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(info.getStates().size(), 9, "incorrect states");

					Assert.assertEquals(
							info.getStates().stream().filter(s -> s.getLabel().equals("VERY ACTIVE - (50K ABOVE)")
									&& s.getValue().equals("VERYACTIVE50KABOVE")).count(),
							1);

					Assert.assertEquals(info.getStates().stream().filter(
							s -> s.getLabel().equals("ACTIVE - (10K TO 50K)") && s.getValue().equals("ACTIVE10KTO50K"))
							.count(), 1);

					Assert.assertEquals(info.getStates().stream().filter(
							s -> s.getLabel().equals("REGULAR - (1K TO 10K)") && s.getValue().equals("REGULAR1KTO10K"))
							.count(), 1);

					Assert.assertEquals(info.getStates().stream().filter(
							s -> s.getLabel().equals("SELDOM - (0 TO 1K)") && s.getValue().equals("SELDOM0TO1K"))
							.count(), 1);

					Assert.assertEquals(info.getStates().stream().filter(
							s -> s.getLabel().equals("ACTIVE - (10K TO 50K)") && s.getValue().equals("ACTIVE10KTO50K"))
							.count(), 1);

					Assert.assertEquals(info.getStates().stream().filter(
							s -> s.getLabel().equals("ACTIVE BUT ON HOLD") && s.getValue().equals("ACTIVEBUTONHOLD"))
							.count(), 1);

					Assert.assertEquals(info.getStates().stream()
							.filter(s -> s.getLabel().equals("INACTIVE") && s.getValue().equals("INACTIVE")).count(),
							1);

					Assert.assertEquals(info.getStates().stream()
							.filter(s -> s.getLabel().equals("BARRED OR TERMINATED")
									&& s.getValue().equals("BARREDORTERMINATED")
									&& s.getIsTerminal().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info
							.getStates().stream().filter(s -> s.getLabel().equals("CANCELLED")
									&& s.getValue().equals("CANCELLED") && s.getIsTerminal().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(
							info.getStates().stream()
									.filter(s -> s.getLabel().equals("TRIAL") && s.getValue().equals("TRIAL")).count(),
							1);
				}
				if (aspectNames.contains("d") || aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(info.getDependencies().size(), 0, "incorrect number of dependecies");

				}
				if (aspectNames.contains("l") || aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(info.getLinks().size(), 3, "incorrect number of links");

					Assert.assertEquals(info.getLinks().stream()
							.filter(s -> s.getName().equals("holdstheaccounts")
									&& s.getLabel().equals("Holds The Accounts") && s.getType().equals("Account")
									&& s.getIsArray().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getLinks().stream()
							.filter(s -> s.getName().equals("social_circle_of")
									&& s.getLabel().equals("social circle of") && s.getType().equals("Customer")
									&& s.getIsArray().toString().equals("true"))
							.count(), 1);

					Assert.assertEquals(info.getLinks().stream()
							.filter(s -> s.getName().equals("socially_mingle_with")
									&& s.getLabel().equals("socially_mingle_with") && s.getType().equals("Customer")
									&& s.getIsArray().toString().equals("true"))
							.count(), 1);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while getting types");
			System.out.println(e.getMessage());
		}

	}

	//read single case
	private void readSingleCase(String resourceName) throws IOException, InterruptedException
	{
		try
		{
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getSingleCase(caseReferences.get(0), selectArray[selectIterator], resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));
				if (aspectNames.contains("casedata") || aspectNames.contains("c")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), readCustomer[0]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || aspectNames.contains("m")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || aspectNames.contains("cr")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(jsonPath.getString("caseReference"), caseReferences.get(0),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || aspectNames.contains("s")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), summaryCreatedCustomer[0]),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getSingleCase(caseReferences.get(1), selectArrayAbbreviated[selectIterator],
						resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));
				if (aspectNames.contains("casedata") || aspectNames.contains("c")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("casedata"), readCustomer[1]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || aspectNames.contains("m")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertNotNull(jsonPath.getString("metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.creationTimestamp"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || aspectNames.contains("cr")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertEquals(jsonPath.getString("caseReference"), caseReferences.get(1),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || aspectNames.contains("s")
						|| aspectNames.get(0).equalsIgnoreCase(""))
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("summary"), summaryCreatedCustomer[1]),
							"summary does not match");
				}
			}

		}
		catch (Exception e)
		{
			System.out.println("problem while reading cases");
			System.out.println(e.getMessage());
		}
	}

	//read multiple cases
	private void readMultipleCasesWithDifferentFilters(String resourceName) throws IOException, InterruptedException
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
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCases("EQ", filterMap, selectArray[selectIterator], skip, top, "", resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 2, "incorrect number of objects returned");

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[2]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");

					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(2),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseReferences.get(1),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
				}
			}

			//add cid
			filterMap.put("cid", "WINTERFELL-00002");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top, "",
						resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1, "incorrect number of objects returned");

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[1]),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(1),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
				}
			}
			filterMap.clear();
		}
		catch (Exception e)
		{
			System.out.println("problem while reading cases");
			System.out.println(e.getMessage());
		}
	}

	//read with case ref in
	private void readMultipleCasesWithCaseRefIn(String resourceName) throws IOException, InterruptedException
	{
		String caseRefIn = "('" + caseReferences.get(0) + "','" + caseReferences.get(1) + "','" + caseReferences.get(2)
				+ "')";
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseReference", caseRefIn);

		String skip = "";
		String top = "";

		try
		{
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCases("IN", filterMap, selectArray[selectIterator], skip, top, "", resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 3);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[0]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), readCustomer[2]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
							"metadata should be tagged");

				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(0).toString(),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseReferences.get(1).toString(),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseReferences.get(2).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[0]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
				}
			}

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCases("IN", filterMap, selectArrayAbbreviated[selectIterator], skip, top, "",
						resourceName);
				System.out.println(response.asString());
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "problem while updating cases");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 3);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[0]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].casedata"), readCustomer[2]),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[2].metadata.modificationTimestamp"),
							"metadata should be tagged");

				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(0).toString(),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseReferences.get(1).toString(),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[2].caseReference"), caseReferences.get(2).toString(),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[0]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[2].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while reading cases");
			System.out.println(e.getMessage());
		}
	}

	//search case
	private void searchCases(String resourceName) throws IOException, InterruptedException
	{

		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		String searchString = "WINTERFELL-00003";

		//for case reference In filter skip and top are not allowed
		String skip = "0";
		String top = "10";

		try
		{
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArray[selectIterator], skip, top, searchString, "",
						resourceName);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[2]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(2),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
				}
			}

			//change in search string
			searchString = "VERYACTIVE50KABOVE";

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.searchCases("EQ", filterMap, selectArrayAbbreviated[selectIterator], skip, top,
						searchString, "");
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 2);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[2]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");

				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(2),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseReferences.get(1),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while searching cases");
			System.out.println(e.getMessage());
		}
	}

	//find case by criteria
	private void findByCriteria(String resourceName) throws IOException, InterruptedException
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
			for (int selectIterator = 0; selectIterator < selectArray.length; selectIterator++)
			{
				response = cases.getCasesDQL("EQ", filterMap, filterDql, selectArray[selectIterator], skip, top, "",
						resourceName);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println("values in the loop " + " select value = " + selectArray[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 2);

				List<String> aspectNames = Arrays.asList(selectArray[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("casedata") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[2]),
							"casedata does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
							"casedata does not match");
				}
				if (aspectNames.contains("metadata") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[1].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("caseReference") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(2),
							"caseRef should be tagged");
					Assert.assertEquals(jsonPath.getString("[1].caseReference"), caseReferences.get(1),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("summary") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[2]),
							"summary does not match");
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].summary"), summaryCreatedCustomer[1]),
							"summary does not match");
				}
			}

			//change the dql filter
			filterDql.clear();
			filterDql.put("customerCID", "'WINTERFELL-00001'");

			for (int selectIterator = 0; selectIterator < selectArrayAbbreviated.length; selectIterator++)
			{
				response = cases.getCasesDQL("EQ", filterMap, filterDql, selectArrayAbbreviated[selectIterator], skip,
						top, "", resourceName);
				Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
				JsonPath jsonPath = response.jsonPath();
				System.out.println(jsonPath.getString("")); // useful for debugging
				System.out.println(
						"values in the loop " + " select value = " + selectArrayAbbreviated[selectIterator].toString()); // useful for debugging

				CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(),
						CasesGetResponseBody.class);
				Assert.assertEquals(obj.size(), 1);

				List<String> aspectNames = Arrays.asList(selectArrayAbbreviated[selectIterator].split(","));

				//first select is null and does not contain any explicit select filter 
				if (aspectNames.contains("c") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[0]),
							"casedata does not match");
				}
				if (aspectNames.contains("m") || selectIterator == 0)
				{
					Assert.assertNotNull(jsonPath.getString("[0].metadata"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.creationTimestamp"),
							"metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modifiedBy"), "metadata should be tagged");
					Assert.assertNotNull(jsonPath.getString("[0].metadata.modificationTimestamp"),
							"metadata should be tagged");
				}
				if (aspectNames.contains("cr") || selectIterator == 0)
				{
					Assert.assertEquals(jsonPath.getString("[0].caseReference"), caseReferences.get(0),
							"caseRef should be tagged");
				}
				if (aspectNames.contains("s") || selectIterator == 0)
				{
					Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].summary"), summaryCreatedCustomer[0]),
							"summary does not match");
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while finding cases");
			System.out.println(e.getMessage());
		}
	}

	//delete single case
	private void deleteSingleCase(String resourceName) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		try
		{
			if (caseReferencesUpdated.size() == 0)
			{
				response = cases.purgeSingleCase(caseReferences.get(0), resourceName);
			}
			else
			{
				response = cases.purgeSingleCase(caseReferencesUpdated.get(0), resourceName);
			}

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			//read as Tony to verify the cases are deleted
			response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 2, "cases must have not been deleted user");

			if (caseReferencesUpdated.size() == 0)
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), readCustomer[2]),
						"casedata does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), readCustomer[1]),
						"casedata does not match");
			}
			else
			{
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[0].casedata"), updateCustomer[2]),
						"casedata does not match");
				Assert.assertTrue(assertUnsortedData(jsonPath.getString("[1].casedata"), updateCustomer[1]),
						"casedata does not match");
			}
		}
		catch (Exception e)
		{
			System.out.println("problem while deleting case");
			System.out.println(e.getMessage());
		}
	}

	//delete multiple cases
	private void deleteMultipleCases(String resourceName)
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		filterMap.put("caseType", caseTypeCustomer);
		filterMap.put("applicationMajorVersion", applicationMajorVersion);

		if (caseReferencesUpdated.size() == 0)
		{
			filterMap.put("caseState", "VERYACTIVE50KABOVE");
		}
		else
		{
			filterMap.put("caseState", "INACTIVE");
		}

		try
		{
			response = cases.purgeCases(filterMap, resourceName);

			//read as Tony to verify the cases are deleted
			response = cases.getCases("EQ", filterMap, SELECT_CASES, "", "10", "", RESOURCE_TONY);
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			CasesGetResponseBody obj = new ObjectMapper().readValue(response.asString(), CasesGetResponseBody.class);
			Assert.assertEquals(obj.size(), 0, "cases must have not been deleted user");
		}
		catch (Exception e)
		{
			System.out.println("problem while deleting case");
			System.out.println(e.getMessage());
		}
	}

	//create links
	private void createLinks(String resourceName) throws IOException, InterruptedException
	{
		LinksPostRequestBody linksPostRequestBody = new LinksPostRequestBody();
		for (int caseRefIterator = 0; caseRefIterator < caseReferencesAccounts.size(); caseRefIterator++)
		{
			Link link = new Link();
			link.setCaseReference(caseReferencesAccounts.get(caseRefIterator));
			link.setName(linkName);
			linksPostRequestBody.add(link);
		}

		response = links.createLinks(caseReferences.get(0), linksPostRequestBody, resourceName);
		System.out.println(response.asString());
		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		Assert.assertEquals(response.asString(), "", "checking for empty response");

		genericGetLinks(4);
	}

	private void genericGetLinks(int numberOfLinks) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";

		response = links.getLinks(filterMap, filterDql, caseReferences.get(0), skip, top, RESOURCE_TONY);
		System.out.println(response.asString());

		Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
		JsonPath jsonPath = response.jsonPath();
		System.out.println(jsonPath.getString("")); // useful for debugging

		List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
		Assert.assertEquals(obj.size(), numberOfLinks, "multiple cases are returned");

		if (numberOfLinks > 0)
		{
			for (int caseRefIterator = 0; caseRefIterator < numberOfLinks; caseRefIterator++)
			{
				Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
						caseReferencesAccounts.get(caseRefIterator), "linked case reference is incorrect");
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}
		}
	}

	//get links and navigateBy(criteria)
	private void getLinksVariations(String resourceName, String type) throws IOException, InterruptedException
	{
		Map<String, String> filterMap = new HashMap<String, String>();
		Map<String, String> filterDql = new HashMap<String, String>();

		String skip = "0";
		String top = "10";

		if (type.equals("NO_FILTER"))
		{
			response = links.getLinks(filterMap, filterDql, caseReferences.get(0), skip, top, resourceName);
			System.out.println(response.asString());

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 4, "multiple cases are returned");

			for (int caseRefIterator = 0; caseRefIterator < 4; caseRefIterator++)
			{
				Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
						caseReferencesAccounts.get(caseRefIterator), "linked case reference is incorrect");
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}
		}

		else if (type.equals("NAME_FILTER"))
		{
			//add name filter
			filterMap.put("name", linkName);

			response = links.getLinks(filterMap, filterDql, caseReferences.get(0), skip, top, resourceName);
			System.out.println(response.asString());

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 4, "multiple cases are returned");

			for (int caseRefIterator = 0; caseRefIterator < 4; caseRefIterator++)
			{
				Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
						caseReferencesAccounts.get(caseRefIterator), "linked case reference is incorrect");
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}

			//clear the filter
			filterMap.clear();
		}

		else if (type.equals("DQL_FILTER"))
		{
			//add name filter
			filterMap.put("name", linkName);

			//add dql filter
			filterDql.put("accountType", "'CURRENT'");
			filterDql.put("state", "'@ACTIVE&'");

			response = links.getLinks(filterMap, filterDql, caseReferences.get(0), skip, top, resourceName);
			System.out.println(response.asString());

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			JsonPath jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			List<Link> obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 2, "multiple cases are returned");

			for (int caseRefIterator = 0; caseRefIterator < 2; caseRefIterator++)
			{
				Assert.assertEquals(obj.get(caseRefIterator).getCaseReference(),
						caseReferencesAccounts.get(caseRefIterator), "linked case reference is incorrect");
				Assert.assertEquals(obj.get(caseRefIterator).getName(), linkName, "name is incorrect");
			}

			//change dql filter
			filterDql.clear();
			filterDql.put("accountID", "3");

			response = links.getLinks(filterMap, filterDql, caseReferences.get(0), skip, top, resourceName);
			System.out.println(response.asString());

			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			jsonPath = response.jsonPath();
			System.out.println(jsonPath.getString("")); // useful for debugging

			obj = Arrays.asList((new ObjectMapper().readValue(response.asString(), Link[].class)));
			Assert.assertEquals(obj.size(), 1, "multiple cases are returned");

			Assert.assertEquals(obj.get(0).getCaseReference(), caseReferencesAccounts.get(2),
					"linked case reference is incorrect");
			Assert.assertEquals(obj.get(0).getName(), linkName, "name is incorrect");

			//clear filters
			filterDql.clear();
			filterMap.clear();
		}
	}

	//unlink cases
	private void unlinkVariations(String resourceName, String type) throws IOException, InterruptedException
	{
		//function is called in a reverse order of if..else arrangement
		//so the number of links to be deleted is increasing

		//0th account is unlinked
		if (type.equals("NO_NAME"))
		{
			response = links.deleteLinks(caseReferences.get(0), "", "EQ", caseReferencesAccounts, resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

			genericGetLinks(0);
		}

		//1st account is unlinked
		else if (type.equals("NAME"))
		{
			response = links.deleteLinks(caseReferences.get(0), linkName, "EQ", caseReferencesAccounts.subList(1, 2),
					resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

			genericGetLinks(1);
		}

		//2nd account is unlinked
		else if (type.equals("NAME_CASE_REFERENCE"))
		{
			response = links.deleteLinks(caseReferences.get(0), linkName, "EQ", caseReferencesAccounts.subList(2, 3),
					resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

			genericGetLinks(2);
		}

		//3rd account is unlinked
		else if (type.equals("NAME_CASE_REFERENCE_IN"))
		{
			response = links.deleteLinks(caseReferences.get(0), linkName, "IN", caseReferencesAccounts.subList(3, 4),
					resourceName);
			System.out.println(response.asString());
			Assert.assertEquals(response.getStatusCode(), validStatusCode, "incorrect response");
			Assert.assertEquals(response.asString(), "1", "checking response for number of links deleted");

			genericGetLinks(3);
		}
	}

	//Test to verify that on assigning system actions user can successfully perform the actions for CRUD
	@Test(description = "Test to verify that on assigning system actions user can successfully perform the actions for CRUD")
	public void testSystemActionsCRUD() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException, InterruptedException
	{
		try
		{
			//deploy org model
			prerequisites();

			//Tony would be acting as a user to create, read and update
			de.mapResources(CONTAINER_NAME, RESOURCE_TONY, POSITION_CREATE_READ_UPDATE);

			//to create - 0001
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE);
			testPrivilege(RESOURCE_CLINT, true, false, false, false);

			//to read - 0010
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			testPrivilege(RESOURCE_CLINT, false, true, false, false);

			//to create and read - 0011
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_CREATE_READ);
			testPrivilege(RESOURCE_CLINT, true, true, false, false);

			//to update - 0100
			de.mapResources(CONTAINER_NAME, RESOURCE_JON, POSITION_UPDATE);
			testPrivilege(RESOURCE_JON, false, false, true, false);

			//to create and update - 0101
			de.mapResources(CONTAINER_NAME, RESOURCE_JOHN, POSITION_CREATE_UPDATE);
			testPrivilege(RESOURCE_JOHN, true, false, true, false);

			//to update and read - 0110
			de.mapResources(CONTAINER_NAME, RESOURCE_JOHN, POSITION_READ_UPDATE);
			testPrivilege(RESOURCE_JOHN, false, true, true, false);

			//to update read and create - 0111
			de.mapResources(CONTAINER_NAME, RESOURCE_LEON, POSITION_CREATE_READ_UPDATE);
			testPrivilege(RESOURCE_LEON, true, true, true, false);

			//to delete - 1000
			de.mapResources(CONTAINER_NAME, RESOURCE_LEON, POSITION_DELETE);
			testPrivilege(RESOURCE_LEON, false, false, false, true);

			//to delete - 1001
			de.mapResources(CONTAINER_NAME, RESOURCE_LEON, POSITION_CREATE_DELETE);
			testPrivilege(RESOURCE_LEON, true, false, false, true);

			//to delete - 1010
			de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_READ_DELETE);
			testPrivilege(RESOURCE_LIAM, false, true, false, true);

			//to delete - 1011
			de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_CREATE_READ_DELETE);
			testPrivilege(RESOURCE_LIAM, true, true, false, true);

			//to delete - 1100
			de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_UPDATE_DELETE);
			testPrivilege(RESOURCE_LIAM, false, false, true, true);

			//to delete - 1101
			de.mapResources(CONTAINER_NAME, RESOURCE_RICH, POSITION_CREATE_UPDATE_DELETE);
			testPrivilege(RESOURCE_RICH, true, false, true, true);

			//to delete - 1110
			de.mapResources(CONTAINER_NAME, RESOURCE_RICH, POSITION_READ_UPDATE_DELETE);
			testPrivilege(RESOURCE_RICH, false, true, true, true);

			//to delete - 1111
			de.mapResources(CONTAINER_NAME, RESOURCE_RICH, POSITION_CREATE_READ_UPDATE_DELETE);
			testPrivilege(RESOURCE_RICH, true, true, true, true);
		}
		finally
		{
			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}

	//Test to verify that on assigning system actions user can successfully perform the actions for link, unlink and navigateBy(criteria)
	@Test(description = "Test to verify that on assigning system actions user can successfully perform the actions for link, unlink and navigateBy(criteria)")
	public void testSystemActionsLinkUnlinkGetLinksNavigateBy()
			throws IOException, URISyntaxException, DeploymentException, PersistenceException, InternalException,
			RuntimeApplicationException, InterruptedException
	{
		try
		{
			//deploy org model
			prerequisites();

			//Tony would be acting as a user to read, update and delete
			de.mapResources(CONTAINER_NAME, RESOURCE_TONY, POSITION_CREATE_READ_UPDATE_DELETE);

			//to link - 001
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_UPDATE);
			testPrivilegeLinks(RESOURCE_CLINT, true, false, true);

			//to get - 010
			de.mapResources(CONTAINER_NAME, RESOURCE_JON, POSITION_READ);
			testPrivilegeLinks(RESOURCE_JON, false, true, false);

			//to get - 010
			de.mapResources(CONTAINER_NAME, RESOURCE_JON, POSITION_READ_WITH_NO_TYPES);
			testPrivilegeLinks(RESOURCE_JON, false, true, false);

			//to link and get - 011
			de.mapResources(CONTAINER_NAME, RESOURCE_JOHN, POSITION_READ_UPDATE);
			testPrivilegeLinks(RESOURCE_JOHN, true, true, false);

			//to link and unlink - 101
			de.mapResources(CONTAINER_NAME, RESOURCE_LIAM, POSITION_UPDATE_DELETE);
			testPrivilegeLinks(RESOURCE_LIAM, true, false, true);

			//to unlink and read - 110
			de.mapResources(CONTAINER_NAME, RESOURCE_RICH, POSITION_READ_DELETE);
			testPrivilegeLinks(RESOURCE_RICH, false, true, false);

			//to link, unlink and read - 111
			de.mapResources(CONTAINER_NAME, RESOURCE_STEVE, POSITION_READ_UPDATE_DELETE);
			testPrivilegeLinks(RESOURCE_STEVE, true, true, true);

		}
		finally
		{
			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}

	//Test to verify that system actions for /types and /read can work independently
	@Test(description = "Test to verify that system actions for /types and /read can work independently")
	public void testSystemActionsTypeAndRead() throws IOException, URISyntaxException, DeploymentException,
			PersistenceException, InternalException, RuntimeApplicationException, InterruptedException
	{
		try
		{
			//deploy org model
			prerequisites();

			//Tony would be acting as a user to create, read and update
			de.mapResources(CONTAINER_NAME, RESOURCE_TONY, POSITION_CREATE_READ);

			//to /types - 01
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_TYPES_WITH_NO_READ);
			testPrivilegeTypeAndRead(RESOURCE_CLINT, true, false);

			//to read - 10
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ_WITH_NO_TYPES);
			testPrivilegeTypeAndRead(RESOURCE_CLINT, false, true);

			//to /types and read - 11
			de.mapResources(CONTAINER_NAME, RESOURCE_CLINT, POSITION_READ);
			testPrivilegeTypeAndRead(RESOURCE_CLINT, true, true);
		}
		finally
		{
			//undeploy org model
			rest.undeployApplication("", "com.example.aceproject3organisational");
		}
	}
}