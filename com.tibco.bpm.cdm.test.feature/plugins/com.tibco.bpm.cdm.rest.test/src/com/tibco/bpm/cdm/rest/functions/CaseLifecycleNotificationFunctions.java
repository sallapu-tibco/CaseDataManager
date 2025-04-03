package com.tibco.bpm.cdm.rest.functions;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class CaseLifecycleNotificationFunctions extends Utils
{
	Response response = null;

	public Response enableMessages() throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Enable Messages on CLN ****");

		response = given().log().all().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Utils.URL_YY_CLN_ENABLE);

		return response;
	}

	public Response disableMessages() throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Disable Messages on CLN ****");

		response = given().log().all().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Utils.URL_YY_CLN_DISABLE);

		return response;
	}

	public Response readMessages() throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Read Messages on CLN ****");

		response = given().log().all().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.get(Utils.URL_YY_CLN_MESSAGES);

		return response;
	}

	public void flushNotifications(boolean isEnabled, boolean keepEnabled) throws IOException, InterruptedException
	{
		System.out.println("*** Performing Request URL to Flush Notifications ****");

		if (isEnabled == false)
		{
			enableMessages();
			System.out.println("case life cycle notifications are enabled");
		}

		System.out.println("flushing life cycle notifications by reading");
		readMessages();

		if (keepEnabled == false)
		{
			disableMessages();
			System.out.println("case life cycle notifications are disabled");
		}
	}
}
