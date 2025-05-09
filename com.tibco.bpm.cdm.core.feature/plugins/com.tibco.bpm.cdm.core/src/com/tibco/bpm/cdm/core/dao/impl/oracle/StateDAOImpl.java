package com.tibco.bpm.cdm.core.dao.impl.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.dao.StateDAO;
import com.tibco.bpm.da.dm.api.DataModel;
import com.tibco.bpm.da.dm.api.State;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;

/**
 * Oracle implementation of the StateDAO interface that persists states in the cdm_states database table.
 */
public class StateDAOImpl extends com.tibco.bpm.cdm.core.dao.impl.StateDAOImpl implements StateDAO
{
	private static final String	SQL_CREATE	= "INSERT INTO cdm_states (type_id, value, label, is_terminal) "
			+ "VALUES (?, ?, ?, ?)";

	private static final String	SQL_UPDATE	= "UPDATE cdm_states SET label=?, is_terminal=? WHERE type_id=? AND value=?";

	private static final String	SQL_GET		= "SELECT s.id, s.value FROM cdm_states s WHERE type_id IN "
			+ "(SELECT id FROM cdm_types WHERE name=? AND datamodel_id IN "
			+ "(SELECT id FROM cdm_datamodels WHERE namespace=? AND major_version=?))";

	public StateDAOImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	protected BigInteger createState(Connection conn, BigInteger typeId, String value, String label, boolean isTerminal)
			throws PersistenceException
	{
		PreparedStatement ps = null;
		Statement ts = null;
		BigInteger id = null;
		try
		{

			ps = conn.prepareStatement(SQL_CREATE, new String[]{"id"});
			ps.setBigDecimal(1, new BigDecimal(typeId));
			ps.setString(2, value);
			ps.setString(3, label);
			ps.setInt(4, isTerminal ? 1 : 0);

			int success = ps.executeUpdate();

			{
				ResultSet rset = ps.getGeneratedKeys();
				if (rset.next())
				{
					id = rset.getBigDecimal(1).toBigInteger();
				}
			}
		}
		catch (SQLException e)
		{
			throw PersistenceException.newRepositoryProblem(e);
		}
		finally
		{
			cleanUp(ts, ps, null);
		}
		return id;
	}
	
	protected void updateState(Connection conn, BigInteger typeId, String value, String label, boolean isTerminal)
			throws PersistenceException
	{
		PreparedStatement ps = null;
		Statement ts = null;
		try
		{

			ps = conn.prepareStatement(SQL_UPDATE);
			ps.setString(1, label);
			ps.setInt(2, isTerminal ? 1: 0);
			ps.setBigDecimal(3, new BigDecimal(typeId));
			ps.setString(4, value);
			ps.executeUpdate();

		}
		catch (SQLException e)
		{
			throw PersistenceException.newRepositoryProblem(e);
		}
		finally
		{
			cleanUp(ts, ps, null);
		}
	}


	private void createStates(Connection conn, BigInteger typeId, StructuredType type) throws PersistenceException
	{
		for (State state : type.getStateModel().getStates())
		{
			createState(conn, typeId, state.getValue(), state.getLabel(), state.getIsTerminal());
		}
	}

	public void create(DataModel dm, Map<String, BigInteger> typeNameToIdMap) throws PersistenceException
	{
		Connection conn = getConnection();
		try
		{
			for (StructuredType type : dm.getStructuredTypes())
			{
				if (type.getIsCase())
				{
					BigInteger typeId = typeNameToIdMap.get(type.getName());
					createStates(conn, typeId, type);
				}
			}
		}
		finally

		{
			cleanUp(null, null, conn);
		}
	}


	@Override
	public void update(BigInteger id, DataModel oldDataModel, DataModel dm, Map<String, BigInteger> typeNameToIdMap)
			throws PersistenceException
	{
		Connection conn = getConnection();
		try
		{

			// Existing states can't be removed, but new states can be added and existing states can
			// have their labels and terminality changed.
			for (StructuredType type : dm.getStructuredTypes())
			{
				if (type.getIsCase())
				{
					String typeName = type.getName();
					BigInteger typeId = typeNameToIdMap.get(typeName);
					StructuredType oldType = oldDataModel.getStructuredTypeByName(typeName);
					if (oldType != null)
					{
						// Existing type
						StateModel stateModel = type.getStateModel();
						StateModel oldStateModel = oldType.getStateModel();
						for (State state : stateModel.getStates())
						{
							String stateValue = state.getValue();
							State oldState = oldStateModel.getStateByValue(stateValue);
							if (oldState != null)
							{
								// Existing state. If the label or terminality have changed, we
								// need to update it
								String label = state.getLabel();
								boolean isTerminal = state.getIsTerminal();
								String oldLabel = oldState.getLabel();
								boolean oldIsTerminal = oldState.getIsTerminal();
								if (!label.equals(oldLabel) || isTerminal != oldIsTerminal)
								{
									// Something changed, so update the state
									updateState(conn, typeId, stateValue, label, isTerminal);
								}

							}
							else
							{
								// New state added to existing type
								createState(conn, typeId, state.getValue(), state.getLabel(), state.getIsTerminal());
							}
						}
					}
					else
					{
						// New states for new type
						createStates(conn, typeId, type);
					}
				}
			}
		}
		finally
		{
			cleanUp(null, null, conn);
		}
	}
}
