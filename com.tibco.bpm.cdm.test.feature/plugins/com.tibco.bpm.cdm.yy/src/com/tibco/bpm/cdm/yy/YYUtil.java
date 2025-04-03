package com.tibco.bpm.cdm.yy;

import java.io.InputStream;
import java.util.Scanner;

public class YYUtil
{

	public static String readFileContents(String fileLocation)
	{
		InputStream inputStream = YYUtil.class.getClassLoader().getResourceAsStream(fileLocation);
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

}
