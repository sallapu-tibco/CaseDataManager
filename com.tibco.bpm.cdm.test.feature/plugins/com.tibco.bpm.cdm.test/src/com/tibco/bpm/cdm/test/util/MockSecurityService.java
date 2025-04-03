package com.tibco.bpm.cdm.test.util;

import java.util.ArrayList;
import java.util.List;

import com.tibco.bpm.de.rest.model.AuthorizationRequest;
import com.tibco.bpm.de.rest.model.AuthorizationResponse;
import com.tibco.bpm.de.rest.model.GetUserPrivilegesRequest;
import com.tibco.bpm.de.rest.model.GetUserPrivilegesResponse;
import com.tibco.bpm.de.rest.model.ListActionAuthorizedEntitiesRequest;
import com.tibco.bpm.de.rest.model.ListActionAuthorizedEntitiesResponse;
import com.tibco.bpm.de.rest.model.ListAuthorizedOrgsRequest;
import com.tibco.bpm.de.rest.model.ListAuthorizedOrgsResponse;
import com.tibco.bpm.de.rest.model.SystemActionRequest;
import com.tibco.bpm.de.rest.model.SystemActionResponse;
import com.tibco.n2.common.auth.SystemActionId;
import com.tibco.n2.de.api.services.SecurityService;
import com.tibco.n2.de.api.services.UserDetails;
import com.tibco.n2.de.services.InternalServiceFault;
import com.tibco.n2.de.services.InvalidEntityReferenceFault;
import com.tibco.n2.de.services.InvalidServiceRequestFault;
import com.tibco.n2.de.services.SecurityFault;

public class MockSecurityService implements SecurityService
{
	private static List<SystemActionId> authorisedSystemActions = new ArrayList<SystemActionId>();

	// add one or more allowed SystemActions for our Mock tests
	static
	{
		authorisedSystemActions.add(SystemActionId.authenticatedUser);
		authorisedSystemActions.add(SystemActionId.createUpdateCase);
		authorisedSystemActions.add(SystemActionId.readCase);
		authorisedSystemActions.add(SystemActionId.createUpdateCase);
		authorisedSystemActions.add(SystemActionId.deleteCase);
		authorisedSystemActions.add(SystemActionId.readCase);
	}

	@Override
	public UserDetails lookupUser(String username) throws InternalServiceFault, InvalidServiceRequestFault
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetUserPrivilegesResponse getUserPrivileges(GetUserPrivilegesRequest request)
			throws InvalidEntityReferenceFault, InvalidServiceRequestFault, InternalServiceFault
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthorizationResponse isActionAuthorized(AuthorizationRequest actions, Boolean raiseFault) throws Exception
	{
		AuthorizationResponse response = new AuthorizationResponse();

		boolean matchedOverall = true; // optimistic default - changed if any item in list gets a false authorisation

		if (null == actions)
		{
			matchedOverall = false;
		}
		else
		{
			response.setScope(actions.getScope()); // don't bother with significance of ModelEntityId in scope

			List<SystemActionRequest> requestList = actions.getActions();

			final int listSize = null == requestList ? 0 : requestList.size();

			List<SystemActionResponse> responseList = new ArrayList<SystemActionResponse>(listSize);

			response.setActions(responseList);

			response.setOverall(Boolean.FALSE); // pessimistic overall default

			if (listSize > 0)
			{
				// there must be 1:1 response for each request
				// if any response action is false then overall will be false

				for (int index = 0; index < listSize; index++)
				{
					SystemActionRequest req = requestList.get(index);

					String action = req.getAction();
					String component = req.getComponent();

					// match against allowed system actions

					boolean matched = false;

					for (int j = 0; j < authorisedSystemActions.size(); j++)
					{
						String cfComponent = String.valueOf(authorisedSystemActions.get(j).getComponent());
						String cfAction = String.valueOf(authorisedSystemActions.get(j).getName());

						if (component.equals(cfComponent) && action.equals(cfAction))
						{
							matched = true;
							break;
						}
					}

					SystemActionResponse resp = new SystemActionResponse();

					resp.setAction(action);
					resp.setComponent(component);
					resp.setAuthorized(Boolean.valueOf(matched));

					responseList.add(resp);

					if (!matched)
					{
						matchedOverall = false;
					}
				}
			}
		}

		if (matchedOverall)
		{
			response.setOverall(Boolean.TRUE);
		}
		else if (raiseFault)
		{
			throw new Exception("Not authorized overall");
		}

		return response;
	}

	@Override
	public AuthorizationResponse isActionAuthorized(AuthorizationRequest actions) throws Exception
	{
		// TODO Auto-generated method stub
		return isActionAuthorized(actions, Boolean.FALSE);

	}

	@Override
	public ListActionAuthorizedEntitiesResponse listActionAuthorisedEntities(
			ListActionAuthorizedEntitiesRequest actions)
			throws SecurityFault, InvalidServiceRequestFault, InternalServiceFault
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAuthorizedOrgsResponse listAuthorizedOrgs(ListAuthorizedOrgsRequest actions)
			throws SecurityFault, InvalidServiceRequestFault, InternalServiceFault
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(String alias, String userDn, String password)
			throws SecurityFault, InvalidServiceRequestFault
	{
		// TODO Auto-generated method stub
		return false;
	}

}
