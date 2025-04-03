/*
l* ENVIRONMENT:    Java Generic
*
* COPYRIGHT:      (C) 2016 TIBCO Software Inc
*/
package com.tibco.bpm.cdm.rest.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseQueriesFunction
{
	private String				CMuser				= "bpm_cm_user";

	private String				CMpassword			= "bpm_cm_Staff123";

	BigDecimal					caseRefCM;

	String						caseData;

	String						caseDataCache;

	BigDecimal					createdBy;

	BigDecimal					modifiedBy;

	BigDecimal					lockedBy;

	String						creationTimestamp;

	String						modificationTimestamp;

	Timestamp					lockExpiryTimestamp;

	private String				BPuser				= "bpm_bp_user";

	private String				BPpassword			= "bpm_bp_Staff123";

	private String				aceUser				= "bpm_ace_user";

	private String				acePassword			= "bpm_ace_Staff123";

	BigDecimal					caseRefBP;

	private static final int	READ_RETRIES		= 10;

	private static final int	READ_RETRY_SLEEP	= 500;

	//	private PoolDataSource		pds					= null;

	private static int			connectionRequests	= 0;

	public BigDecimal getUniqueCaseRefForCaseData(String ip, String applicationId, String typeId, String caseData)
			throws ClassNotFoundException, SQLException
	{
		return getUniqueCaseRefForCaseData(ip, applicationId, typeId, caseData, true);
	}

	public BigInteger jobQueueCorrelationCount(String ip, String correlationId)
			throws ClassNotFoundException, SQLException
	{

		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet countTable = null;
		BigInteger count = BigInteger.ZERO;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select count(*) FROM cm_job_q where cm_job_q.correlation_id = " + correlationId);
			countTable = statement.executeQuery(
					"select count(*) FROM cm_job_q where cm_job_q.correlation_id = '" + correlationId + "'");

			if (countTable != null)
			{
				while (countTable.next())
				{
					count = countTable.getBigDecimal(1).toBigInteger();
				}
			}
			else if (countTable == null)
			{
				return count;
			}
			return count;
		}
		finally
		{
			if (countTable != null)
			{
				countTable.close();
				countTable = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public Connection getCachedConnection(String url, String user, String password) throws SQLException
	{
		Connection connection = null;

		connectionRequests++;

		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		props.setProperty("ssl", "false");
		connection = DriverManager.getConnection(url, props);

		System.err.println("Connection requests:" + String.valueOf(connectionRequests));

		//		connection = DriverManager.getConnection(url, user, password);
		// replace with pool

		//		if (null == pds)
		//		{
		//			pds = PoolDataSourceFactory.getPoolDataSource();
		//			pds.setMinPoolSize(1);
		//			pds.setMaxPoolSize(10);
		//			pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
		//			pds.setURL(url);
		//			pds.setUser(user);
		//			pds.setPassword(password);
		//
		//			connection = pds.getConnection();
		//		}
		//		else
		//		{
		//			connection = pds.getConnection(user, password);
		//		}
		//
		//		System.err.println("Connection requests:" + String.valueOf(connectionRequests) + "\tBorrowed:"
		//				+ String.valueOf(pds.getBorrowedConnectionsCount()) + "\tAvailable:"
		//				+ String.valueOf(pds.getAvailableConnectionsCount()) + "\tMinimum:"
		//				+ String.valueOf(pds.getMinPoolSize()) + "\tMaximum:" + String.valueOf(pds.getMaxPoolSize()));

		return connection;
	}

	public BigDecimal getUniqueCaseRefForCaseData(String ip, String applicationId, String typeId, String caseData,
			boolean useRetry) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		caseRefCM = BigDecimal.ZERO;

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			int tryCount = 0;
			int maxTries = useRetry ? READ_RETRIES : 1;

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			System.out.println("select case_reference from cm_cases where application_id = " + applicationId
					+ " and type_id = " + typeId + " and caseData like '" + caseData + "'");

			while (caseRefCM.equals(BigDecimal.ZERO) && (tryCount < maxTries))
			{
				cases = statement.executeQuery("select case_reference, casedata from cm_cases where application_id = "
						+ applicationId + " and type_id = " + typeId);

				tryCount++;
				if (cases != null)
				{
					while (cases.next())
					{
						String actual = cases.getString(2);

						if (Utils.closeEnough(caseData, actual))
						{
							caseRefCM = cases.getBigDecimal(1);
							// first column is for the caseReference 

							break;
						}
					}
				}

				if (caseRefCM.equals(BigDecimal.ZERO))
				{
					System.out.println("Case not found after " + tryCount + " " + (tryCount == 1 ? "try" : "tries"));
					// Not found. Unless we've exhausted retries, sleep and try again.
					if (tryCount < maxTries)
					{
						try
						{
							Thread.sleep(READ_RETRY_SLEEP);
						}
						catch (InterruptedException e)
						{
							// ignore
						}
					}
				}
			}
			return caseRefCM;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public BigDecimal getUniqueCaseRefForCaseDataBP(String ip, String applicationId, String typeId, String caseData)
			throws ClassNotFoundException, SQLException
	{

		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		caseRefBP = BigDecimal.ZERO;

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, BPuser, BPpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select case_ref from cm_case_cache where application_id = " + applicationId
					+ " and type_id = " + typeId + " and case_data like '" + caseData + "'");
			cases = statement.executeQuery("select case_ref, case_data from cm_case_cache where application_id = "
					+ applicationId + " and type_id = " + typeId);

			if (cases != null)
			{
				while (cases.next())
				{
					caseRefBP = cases.getBigDecimal(1);

					// first column is for the caseReference, 2nd is case_data

					String casedata = cases.getString(2);

					if (Utils.closeEnough(casedata, caseData))
					{
						break;
					}
				}
			}
			else if (cases == null)
			{
				return BigDecimal.ZERO;
			}
			return caseRefBP;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public BigDecimal getCreatedByForCaseRef(String ip, BigDecimal caseRef) throws ClassNotFoundException, SQLException
	{

		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select created_by from cm_cases where case_reference = " + caseRef);
			cases = statement.executeQuery("select created_by from cm_cases where case_reference = " + caseRef);

			if (cases != null)
			{
				while (cases.next())
				{
					createdBy = cases.getBigDecimal(1);
				}
			}
			else if (cases == null)
			{
				return BigDecimal.ZERO;
			}
			return createdBy;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public BigDecimal getModifiedByForCaseRef(String ip, BigDecimal caseRef) throws ClassNotFoundException, SQLException
	{

		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select modified_by from cm_cases where case_reference = " + caseRef);
			cases = statement.executeQuery("select modified_by from cm_cases where case_reference = " + caseRef);

			if (cases != null)
			{
				while (cases.next())
				{
					modifiedBy = cases.getBigDecimal(1);
				}
			}
			else if (cases == null)
			{
				return BigDecimal.ZERO;
			}
			return modifiedBy;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public String getCreationTimestampForCID(String ip, String cid) throws ClassNotFoundException, SQLException
	{

		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			connection = getCachedConnection(url, aceUser, acePassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select creation_timestamp from cdm_cases_int where cid = '" + cid + "'");
			cases = statement.executeQuery("select creation_timestamp from cdm_cases_int where cid = '" + cid + "'");
			if (cases != null)
			{
				while (cases.next())
				{
					creationTimestamp = cases.getString(1);
				}
			}
			else if (cases == null)
			{
				return "";
			}
			return creationTimestamp;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public String getModificationTimestampForCID(String ip, String cid) throws ClassNotFoundException, SQLException
	{
		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			connection = getCachedConnection(url, aceUser, acePassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select modification_timestamp from cdm_cases_int where cid = '" + cid + "'");
			cases = statement
					.executeQuery("select modification_timestamp from cdm_cases_int where cid = '" + cid + "'");
			if (cases != null)
			{
				while (cases.next())
				{
					modificationTimestamp = cases.getString(1);
				}
			}
			else if (cases == null)
			{
				return "";
			}
			return modificationTimestamp;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public String getCasedataForCaseRef(String ip, BigDecimal caseRef)
			throws ClassNotFoundException, SQLException, InterruptedException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		int tryCount = 0;
		int maxTries = 2;

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			while (tryCount < maxTries)
			{
				tryCount++;
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				System.out.println("select casedata from cm_cases where case_reference = " + caseRef);
				cases = statement.executeQuery("select casedata from cm_cases where case_reference = " + caseRef);
				if (cases != null)
				{
					while (cases.next())
					{
						caseData = cases.getString(1);
					}
				}
				//			else if (cases == null)
				//			{
				//				return "NoData";
				//			}
				System.out.println("Retrying " + tryCount + " time(s) to retrive the data");
				Thread.sleep(1000);
			}
			return caseData;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	@SuppressWarnings("null")
	public BigDecimal getCaseCacheForCaseData(String ip, String applicationId, String typeId, String caseData)
			throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, BPuser, BPpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select case_reference from cm_cases where application_id = " + applicationId
					+ " and type_id = " + typeId + " and caseData like '" + caseData + "'");
			//		ResultSet cases = statement.executeQuery("select case_reference from cm_case_cache where application_id = "
			//				+ applicationId + " and type_id = " + typeId + " and caseData like '" + caseData + "'");
			cases = statement.executeQuery("select case_reference, casedata from cm_case_cache where application_id = "
					+ applicationId + " and type_id = " + typeId);
			if (cases != null)
			{
				while (cases.next())
				{
					if (Utils.closeEnough(caseData, cases.getString(2)))
					{
						caseRefBP = cases.getBigDecimal(1);
						break;
					}
				}
			}
			else if (cases.wasNull())
			{
				return BigDecimal.ZERO;
			}
			return caseRefBP;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	@SuppressWarnings("null")
	public String getCasedataForCaseRefFromCache(String ip, BigDecimal caseRef)
			throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, BPuser, BPpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select case_data from cm_case_cache where case_ref = " + caseRef);
			cases = statement.executeQuery("select case_data from cm_case_cache where case_ref = " + caseRef);
			if (cases != null)
			{
				while (cases.next())
				{
					caseDataCache = cases.getString(1);
				}
			}
			else if (cases == null)
			{
				return "NoData";
			}
			return caseDataCache;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public void deleteCaseDataEntryInCM(String ip, BigDecimal caseReference) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		//		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			System.out.println("delete from cm_cases where case_reference = " + caseReference);
			statement.executeQuery("delete from cm_cases where case_reference = " + caseReference);
			return;
		}
		finally
		{
			//			if (cases != null)
			//			{
			//				cases.close();
			//				cases = null;
			//			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public void deleteCaseDataEntryInBP(String ip, BigDecimal caseReference) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, BPuser, BPpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			System.out.println("delete from cm_case_cache where case_ref = " + caseReference);
			statement.executeQuery("delete from cm_case_cache where case_ref = " + caseReference);
			return;
		}
		finally
		{
			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public String getFirstSearchIndexForSubscription(String ip, String subscriptionId)
			throws ClassNotFoundException, SQLException
	{
		//		String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		String index = "Empty String";

		//BigDecimal	subscription = new BigDecimal(subscriptionId);

		Connection connection = null;
		Statement statement = null;
		ResultSet indexes = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select es_index from cm_indexes where subscription_id = " + subscriptionId);
			indexes = statement
					.executeQuery("select es_index from cm_indexes where subscription_id = " + subscriptionId);
			if (indexes != null)
			{
				while (indexes.next())
				{
					index = indexes.getString(1);
					System.out.println("Index is : " + index);
				}
			}
			return index;
		}
		finally
		{
			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public BigDecimal getLockedByDetails(String ip, BigDecimal caseRef) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{

			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select LOCKED_BY from cm_cases where case_reference = " + caseRef);
			cases = statement.executeQuery("select LOCKED_BY from cm_cases where case_reference = " + caseRef);
			if (cases != null)
			{
				while (cases.next())
				{
					lockedBy = cases.getBigDecimal(1);
				}
			}
			else if (cases == null)
			{
				return BigDecimal.ZERO;
			}
			return lockedBy;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	public Timestamp getLockExpiryTimestampForCaseRef(String ip, BigDecimal caseRef)
			throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet cases = null;

		try
		{
			//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select LOCK_EXPIRY_TIMESTAMP from cm_cases where case_reference = " + caseRef);
			cases = statement
					.executeQuery("select LOCK_EXPIRY_TIMESTAMP from cm_cases where case_reference = " + caseRef);
			if (cases != null)
			{
				while (cases.next())
				{
					lockExpiryTimestamp = cases.getTimestamp(1);
				}
			}
			else if (cases == null)
			{
				return Timestamp.valueOf("");
			}
			return lockExpiryTimestamp;
		}
		finally
		{
			if (cases != null)
			{
				cases.close();
				cases = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	/**
	 * getIdForUser uses the EOM table ae_user to return the id for a given username
	 * 
	 * @param ip
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public BigInteger getIdForUser(String ip, String name) throws SQLException
	{
		BigInteger id = null;

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement();

			rs = statement.executeQuery("select id from ae_user where username = '" + name + "';");

			if (null != rs)
			{
				if (rs.next())
				{
					BigDecimal idbd = rs.getBigDecimal(1);
					id = idbd.toBigInteger();
				}
			}

			return id;
		}
		finally
		{
			if (null != rs)
			{
				rs.close();
			}

			if (null != statement)
			{
				statement.close();
			}

			if (null != connection)
			{
				connection.close();
			}
		}
	}

	/**
	 * getIdForUser uses the EOM table ae_user to return the id for a given username
	 * 
	 * @param ip
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public BigInteger getIdForGroup(String ip, BigInteger subscriptionId, String name) throws SQLException
	{
		BigInteger id = null;

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement();

			rs = statement.executeQuery("select id from ae_group where subscription_id = " + subscriptionId.toString()
					+ " and name = '" + name + "';");

			if (null != rs)
			{
				if (rs.next())
				{
					BigDecimal idbd = rs.getBigDecimal(1);
					id = idbd.toBigInteger();
				}
			}

			return id;
		}
		finally
		{
			if (null != rs)
			{
				rs.close();
			}

			if (null != statement)
			{
				statement.close();
			}

			if (null != connection)
			{
				connection.close();
			}
		}
	}

	/**
	 * getIdForUser uses the EOM table ae_user to return the id for a given username
	 * 
	 * @param ip
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public String getNameForSandbox(String ip, BigInteger subscriptionId, BigInteger sandboxId) throws SQLException
	{
		String name = null;

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement();

			rs = statement.executeQuery("select name from ae_sandbox where subscription_id = "
					+ subscriptionId.toString() + " and id = " + sandboxId.toString() + ";");

			if (null != rs)
			{
				if (rs.next())
				{
					name = rs.getString(1);
				}
			}

			return name;
		}
		finally
		{
			if (null != rs)
			{
				rs.close();
			}

			if (null != statement)
			{
				statement.close();
			}

			if (null != connection)
			{
				connection.close();
			}
		}
	}

	//select group_id from ae_user_group_map where sandbox_id = 22 and user_id = 100010000020005;

	/**
	 * getGroupIdsForUserAndSandbox uses the EOM table ae_user_group_map to return a list of groupIds that are associated with the userId
	 * 
	 * @param ip
	 * @param sandboxId
	 * @param userId
	 * @return List<BigInteger> groupIds
	 * 
	 * @throws SQLException
	 */
	public Map<BigInteger, String> getGroupsAndNamesForUserAndSandbox(String ip, BigInteger sandboxId,
			BigInteger userId) throws SQLException
	{
		Map<BigInteger, String> groupsAndNames = new HashMap<BigInteger, String>();

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement();

			String query = "select group_id from ae_user_group_map where sandbox_id = " + sandboxId.toString()
					+ " and user_id = " + userId.toString() + ";";

			rs = statement.executeQuery(query);

			if (null != rs)
			{
				while (rs.next())
				{
					BigDecimal idbd = rs.getBigDecimal(1);

					BigInteger groupId = idbd.toBigInteger();

					String name = getNameForGroupId(ip, groupId);

					groupsAndNames.put(groupId, name);
				}
			}

			return groupsAndNames;
		}
		finally
		{
			if (null != rs)
			{
				rs.close();
			}

			if (null != statement)
			{
				statement.close();
			}

			if (null != connection)
			{
				connection.close();
			}
		}
	}

	/**
	 * getGroupIdsForUserAndSandbox uses the EOM table ae_user_group_map to return a list of groupIds that are associated with the userId
	 * 
	 * @param ip
	 * @param sandboxId
	 * @param userId
	 * @return List<BigInteger> groupIds
	 * 
	 * @throws SQLException
	 */
	public String getNameForGroupId(String ip, BigInteger groupId) throws SQLException
	{
		String name = null;

		String subscriptionId = null;

		String type = null;

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			connection = getCachedConnection(url, CMuser, CMpassword);

			statement = connection.createStatement();

			rs = statement.executeQuery("select name, subscription_id, type from ae_group where id = " + groupId + ";");

			if (null != rs)
			{
				if (rs.next())
				{
					name = rs.getString(1);

					BigDecimal subscriptionbd = rs.getBigDecimal(2);

					subscriptionId = subscriptionbd.toBigInteger().toString();

					BigDecimal typebd = rs.getBigDecimal(3);

					type = typebd.toBigInteger().toString();
				}
			}

			return name;
		}
		finally
		{
			if (null != rs)
			{
				rs.close();
			}

			if (null != statement)
			{
				statement.close();
			}

			if (null != connection)
			{
				connection.close();
			}
		}
	}

	public String updateCasedataForCaseRef(String ip, BigInteger caseRef, boolean updateAll, String newCasedata)
			throws SQLException, InterruptedException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		int tryCount = 0;
		int maxTries = 2;

		Connection connectionCM = null;
		Statement statementCM = null;
		Connection connectionBP = null;
		Statement statementBP = null;
		ResultSet updatedCasedata = null;
		ResultSet updatedCasedataBP = null;

		//			connection = DriverManager.getConnection(url, CMuser, CMpassword);
		try
		{
			connectionCM = getCachedConnection(url, CMuser, CMpassword);
			connectionBP = getCachedConnection(url, BPuser, BPpassword);
			while (tryCount < maxTries)
			{
				tryCount++;
				statementCM = connectionCM.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				System.out.println(
						"update cm_cases set casedata = '" + newCasedata + "' where case_reference = " + caseRef);
				statementCM.executeUpdate(
						"update cm_cases set casedata = '" + newCasedata + "' where case_reference = " + caseRef);
				if (updateAll)
				{
					statementBP = connectionBP.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
					System.out.println(
							"update cm_case_cache set case_data = '" + newCasedata + "' where case_ref = " + caseRef);
					statementBP.executeUpdate(
							"update cm_case_cache set case_data = '" + newCasedata + "' where case_ref = " + caseRef);
				}
				System.out.println("Retrying " + tryCount + " time(s) to update the data");
				Thread.sleep(1000);
			}
			System.out.println("select casedata from cm_cases where case_reference = " + caseRef);
			updatedCasedata = statementCM
					.executeQuery("select casedata from cm_cases where case_reference = " + caseRef);
			if (updatedCasedata != null)
			{
				while (updatedCasedata.next())
				{
					caseData = updatedCasedata.getString(1);
				}
			}
			if (updateAll)
			{
				System.out.println("select case_data from cm_case_cache where case_ref = " + caseRef);
				updatedCasedataBP = statementBP
						.executeQuery("select case_data from cm_case_cache where case_ref = " + caseRef);
				if (updatedCasedataBP != null)
				{
					while (updatedCasedataBP.next())
					{
						caseDataCache = updatedCasedataBP.getString(1);
					}
				}
				if (caseData.equals(caseDataCache))
				{
					return caseData;
				}
				else
				{
					return "ERROR IN UPDATE";
				}
			}
			else if (updateAll == false)
			{
				return caseData;
			}
			return "ERROR IN UPDATE";
		}
		finally

		{
			if (updatedCasedataBP != null)
			{
				updatedCasedataBP.close();
				updatedCasedataBP = null;
			}

			if (updatedCasedata != null)
			{
				updatedCasedata.close();
				updatedCasedata = null;
			}

			if (statementCM != null)
			{
				statementCM.close();
				statementCM = null;
			}

			if (null != connectionCM)
			{
				connectionCM.close();
				connectionCM = null;
			}
			if (statementBP != null)
			{
				statementBP.close();
				statementBP = null;
			}

			if (null != connectionBP)
			{
				connectionBP.close();
				connectionBP = null;
			}
		}
	}

	@SuppressWarnings("null")
	public String getCasesCountForType(String ip) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet count = null;
		String casesCount = "";

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, aceUser, acePassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select count(*) from cdm_cases_int");
			count = statement.executeQuery("select count(*) from cdm_cases_int");
			if (count != null)
			{
				while (count.next())
				{
					casesCount = count.getString(1);
				}
			}
			else if (count == null)
			{
				return "something went wrong while fecthing the count";
			}
			return casesCount;
		}
		finally
		{
			if (count != null)
			{
				count.close();
				count = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	@SuppressWarnings("null")
	public String getDateModel(String ip) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet model = null;
		String dataModel = "";

		try
		{
			//			connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, aceUser, acePassword);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select model from cdm_datamodels");
			model = statement.executeQuery("select model from cdm_datamodels");
			if (model != null)
			{
				while (model.next())
				{
					dataModel = model.getString(1);
				}
			}
			else if (model == null)
			{
				return "something went wrong while fecthing the model";
			}
			return dataModel;
		}
		finally
		{
			if (model != null)
			{
				model.close();
				model = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

	@SuppressWarnings("null")
	public Map<String, String> getDataFromView(String ip) throws ClassNotFoundException, SQLException
	{
		//String url = "jdbc:oracle:thin:@" + ip + ":1521/orcl";

		String url = "jdbc:postgresql://" + ip + ":5432" + "/postgres";

		Connection connection = null;
		Statement statement = null;
		ResultSet data = null;

		HashMap<String, String> resultData = new HashMap<String, String>();

		try
		{
			//connection = DriverManager.getConnection(url, BPuser, BPpassword);
			connection = getCachedConnection(url, aceUser, acePassword);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("select * from bpm_ace_user.cdm_cases");
			data = statement.executeQuery("select * from bpm_ace_user.cdm_cases");
			if (data != null)
			{
				while (data.next())
				{
					ResultSetMetaData resultSetMetaData = data.getMetaData();
					for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
					{
						int type = resultSetMetaData.getColumnType(i);
						if (type == Types.VARCHAR)
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types.VARCHAR");
							resultData.put(resultSetMetaData.getColumnName(i), data.getString(i));
						}
						else if (type == Types.CHAR)
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types.CHAR");
							resultData.put(resultSetMetaData.getColumnName(i), data.getString(i));
						}
						else if (type == Types.NUMERIC)
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types.NUMERIC");
							resultData.put(resultSetMetaData.getColumnName(i), Integer.toString(data.getInt(i)));
						}
						else if (type == Types.TIMESTAMP)
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types.TIMESTAMP");
							resultData.put(resultSetMetaData.getColumnName(i), data.getTimestamp(i).toString());
						}
						else if (type == Types.OTHER)
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types.OTHER");
							if (resultSetMetaData.getColumnName(i).equals("completed_case_duration")
									&& (!(data.getString(i).equals("00:00:00"))))
							{
								String duration = data.getString(i);
								if (duration.length() == 8)
								{
									duration = duration + ".";
								}
								while (duration.length() < 12)
								{
									duration = duration + "0";
								}
								resultData.put(resultSetMetaData.getColumnName(i), duration);
							}
							else
							{
								resultData.put(resultSetMetaData.getColumnName(i), data.getString(i));
							}
						}
						else
						{
							System.out.println(resultSetMetaData.getColumnName(i) + " is of type Types Unkown");
							resultData.put(resultSetMetaData.getColumnName(i), Boolean.toString(data.getBoolean(i)));
						}
					}
				}
			}
			else if (data == null)
			{
				return resultData;
			}
			return resultData;
		}
		finally
		{
			if (data != null)
			{
				data.close();
				data = null;
			}

			if (statement != null)
			{
				statement.close();
				statement = null;
			}

			if (null != connection)
			{
				connection.close();
				connection = null;
			}
		}
	}

}