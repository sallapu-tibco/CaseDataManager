package com.tibco.bpm.cdm.test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tibco.bpm.cdm.api.dto.QualifiedTypeName;
import com.tibco.bpm.cdm.api.exception.ArgumentException;
import com.tibco.bpm.cdm.api.exception.DeploymentException;
import com.tibco.bpm.cdm.api.exception.InternalException;
import com.tibco.bpm.cdm.api.exception.PersistenceException;
import com.tibco.bpm.cdm.core.deployment.DataModelManager;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO.TypeInfoAttributeConstraintsDTO;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO.TypeInfoAttributeDTO;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO.TypeInfoDependencyDTO;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO.TypeInfoLinkDTO;
import com.tibco.bpm.cdm.core.dto.TypeInfoDTO.TypeInfoStateDTO;
import com.tibco.bpm.cdm.test.util.FileUtils;
import com.tibco.bpm.da.dm.api.Attribute;
import com.tibco.bpm.da.dm.api.BaseType;
import com.tibco.bpm.da.dm.api.Constraint;
import com.tibco.bpm.da.dm.api.DataModel;
import com.tibco.bpm.da.dm.api.DataModelSerializationException;
import com.tibco.bpm.da.dm.api.Link;
import com.tibco.bpm.da.dm.api.LinkEnd;
import com.tibco.bpm.da.dm.api.State;
import com.tibco.bpm.da.dm.api.StateModel;
import com.tibco.bpm.da.dm.api.StructuredType;
import com.tibco.bpm.dt.rasc.exception.RuntimeApplicationException;

public class TypesTest extends BaseTest
{
	@Autowired
	@Qualifier("dataModelManager")
	private DataModelManager dataModelManager;

	/**
	 * Given typeName, asserts that a type with that name exists in both the DTO list and DataModel and that
	 * they are equivalent.
	 * 
	 * @param dtos
	 * @param dm
	 * @param typeName
	 * @throws ArgumentException 
	 */
	private void assertTypeInfoDTOMatchesDataModel(Collection<TypeInfoDTO> dtos, DataModel dm, String typeName,
			boolean includeAttributes, boolean includeSummaryAttributes, boolean includeStates, boolean includeLinks,
			boolean includeDependencies) throws ArgumentException
	{
		TypeInfoDTO dto = dtos.stream().filter(t -> typeName.equals(t.getName())).findFirst().orElse(null);
		Assert.assertNotNull(dto, "No DTO for type " + typeName);

		StructuredType st = dm.getStructuredTypeByName(typeName);
		Assert.assertNotNull(st, "No StructuredType in model called " + typeName);

		Assert.assertEquals(dto.getName(), st.getName(), "Name mismatch");
		Assert.assertEquals(dto.getLabel(), st.getLabel(), "Label mismatch");
		Assert.assertEquals(dto.getNamespace(), st.getParent().getNamespace(), "Namespace mismatch");
		Assert.assertEquals(dto.getIsCase(), st.getIsCase(), "isCase mismatch");

		// Check attributes
		if (includeAttributes)
		{
			assertTypeInfoAttributesDTOsMatchAttributeList(dto.getAttributes(), st.getAttributes());
		}
		else
		{
			Assert.assertNull(dto.getAttributes(), "No expecting attributes");
		}

		// Check summary attributes
		if (includeSummaryAttributes)
		{
			assertTypeInfoAttributesDTOsMatchAttributeList(dto.getSummaryAttributes(), st.getSummaryAttributes());
		}
		else
		{
			Assert.assertNull(dto.getSummaryAttributes(), "No expecting summary attributes");
		}

		// Check states
		if (includeStates)
		{
			if (st.getIsCase())
			{
				assertTypeInfoStateDTOsMatchStateModel(dto.getStates(), st.getStateModel());
			}
			else
			{
				Assert.assertTrue(dto.getStates() == null || dto.getStates().isEmpty(),
						"Not expecting states for non-case type");
			}
		}
		else
		{
			Assert.assertNull(dto.getStates(), "No expecting states");
		}

		if (includeLinks)
		{
			if (st.getIsCase())
			{
				assertTypeInfoLinkDTOsMatchModel(st.getName(), dto.getLinks(), dm);
			}
			else
			{
				Assert.assertTrue(dto.getLinks() == null || dto.getLinks().isEmpty(),
						"Not expecting links for non-case type");
			}
		}
		else
		{
			Assert.assertNull(dto.getLinks(), "No expecting links");
		}
	}

	private void assertTypeInfoLinkDTOsMatchModel(String typeName, List<TypeInfoLinkDTO> links, DataModel dm)
	{
		int matches = 0;
		for (Link link : dm.getLinks())
		{
			LinkEnd end1 = link.getEnd1();
			LinkEnd end2 = link.getEnd2();
			if (end1.getOwner().equals(typeName))
			{
				String name = end1.getName();
				String label = end1.getLabel();
				boolean isArray = end1.getIsArray();
				String targetType = end2.getOwner();
				if (links.stream().anyMatch(l -> l.getName().equals(name) && l.getLabel().equals(label)
						&& l.getIsArray() == isArray && l.getType().equals(targetType)))
				{
					matches++;
				}
				else
				{
					Assert.fail("No link info returned with name=" + name + ", label=" + label + ", isArray=" + isArray
							+ ", type=" + targetType);
				}

			}
			if (end2.getOwner().equals(typeName))
			{
				String name = end2.getName();
				String label = end2.getLabel();
				boolean isArray = end2.getIsArray();
				String targetType = end1.getOwner();
				if (links.stream().anyMatch(l -> l.getName().equals(name) && l.getLabel().equals(label)
						&& l.getIsArray() == isArray && l.getType().equals(targetType)))
				{
					matches++;
				}
				else
				{
					Assert.fail("No link info returned with name=" + name + ", label=" + label + ", isArray=" + isArray
							+ ", type=" + targetType);
				}

			}
		}
		Assert.assertEquals(links.size(), matches, "Wrong number of links");

	}

	private void assertTypeInfoAttributesDTOsMatchAttributeList(List<TypeInfoAttributeDTO> dtos,
			List<Attribute> attributes) throws ArgumentException
	{
		Assert.assertEquals(dtos.size(), attributes.size(), "Wrong number of attributes");

		for (Attribute attr : attributes)
		{
			TypeInfoAttributeDTO dto = dtos.stream().filter(a -> a.getName().equals(attr.getName())).findFirst()
					.orElse(null);
			Assert.assertNotNull(dto, "No DTO for " + attr.getName());
			Assert.assertEquals(dto.getLabel(), attr.getLabel());
			Assert.assertEquals(dto.getIsArray(), attr.getIsArray());
			Assert.assertEquals(dto.getIsAutoIdentifier(), attr.getParent().hasDynamicIdentifier());
			Assert.assertEquals(dto.getIsIdentifier(), attr.getIsIdentifier());
			Assert.assertEquals(dto.getIsMandatory(), attr.getIsMandatory());
			Assert.assertEquals(dto.getIsSearchable(), attr.getIsSearchable());
			Assert.assertEquals(dto.getIsState(), attr.getIsState());
			Assert.assertEquals(dto.getIsSummary(), attr.getIsSummary());
			if (!(attr.getTypeObject() instanceof BaseType))
			{
				QualifiedTypeName qtn = new QualifiedTypeName(dto.getType());
				if (!dto.getIsStructuredType())
				{
					System.out.println("bom");
				}
				Assert.assertTrue(dto.getIsStructuredType());
				Assert.assertEquals(qtn.getName(), attr.getTypeObject().getName());
			}
			else
			{
				Assert.assertEquals(dto.getType(), attr.getTypeObject().getName());
			}
			assertTypeInfoAttributeConstraintsDTOMatchesAttribute(dto.getConstraints(), attr);
		}
	}

	private void assertTypeInfoAttributeConstraintsDTOMatchesAttribute(TypeInfoAttributeConstraintsDTO dto,
			Attribute attribute)
	{
		String expectedLength = attribute.getConstraintValue(Constraint.NAME_LENGTH);
		Integer actualLength = dto == null ? null : dto.getLength();
		Assert.assertEquals(expectedLength == null ? null : Integer.valueOf(expectedLength), actualLength);

		String expectedMinValue = attribute.getConstraintValue(Constraint.NAME_MIN_VALUE);
		String actualMinValue = dto == null ? null : dto.getMinValue();
		Assert.assertEquals(expectedMinValue, actualMinValue);

		String expectedMinValueInclusive = attribute.getConstraintValue(Constraint.NAME_MIN_VALUE_INCLUSIVE);
		Boolean actualMinValueInclusive = dto == null ? null : dto.getMinValueInclusive();
		Assert.assertEquals(expectedMinValueInclusive == null ? null : Boolean.valueOf(expectedMinValueInclusive),
				actualMinValueInclusive);

		String expectedMaxValue = attribute.getConstraintValue(Constraint.NAME_MAX_VALUE);
		String actualMaxValue = dto == null ? null : dto.getMaxValue();
		Assert.assertEquals(expectedMaxValue, actualMaxValue);

		String expectedMaxValueInclusive = attribute.getConstraintValue(Constraint.NAME_MAX_VALUE_INCLUSIVE);
		Boolean actualMaxValueInclusive = dto == null ? null : dto.getMaxValueInclusive();
		Assert.assertEquals(expectedMaxValueInclusive == null ? null : Boolean.valueOf(expectedMaxValueInclusive),
				actualMaxValueInclusive);

		String expectedDecimalPlaces = attribute.getConstraintValue(Constraint.NAME_DECIMAL_PLACES);
		Integer actualDecimalPlaces = dto == null ? null : dto.getDecimalPlaces();
		Assert.assertEquals(expectedDecimalPlaces == null ? null : Integer.valueOf(expectedDecimalPlaces),
				actualDecimalPlaces);
	}

	/**
	 * Asserts that the list of State DTOs matches the states within the given StateModel
	 * 
	 * @param dtos
	 * @param stateModel
	 */
	private void assertTypeInfoStateDTOsMatchStateModel(List<TypeInfoStateDTO> dtos, StateModel stateModel)
	{
		Assert.assertEquals(dtos.size(), stateModel.getStates().size(), "Wrong number of states");
		for (State state : stateModel.getStates())
		{
			TypeInfoStateDTO dto = dtos.stream().filter(s -> state.getValue().equals(s.getValue())).findFirst()
					.orElse(null);
			Assert.assertNotNull(dto, "No DTO for state " + state.getValue());
			Assert.assertEquals(dto.getLabel(), state.getLabel(), "Wrong label");
			Assert.assertEquals(dto.getValue(), state.getValue(), "Wrong value");
			Assert.assertEquals(dto.getIsTerminal(), state.getIsTerminal(), "Wrong isTerminal");
		}
	}

	@Test
	public void testTypesApp1() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		BigInteger deploymentId = deploy("/apps/app1");

		try
		{
			for (int includeAttributes = 0; includeAttributes <= 1; includeAttributes++)
			{
				for (int includeSummaryAttributes = 0; includeSummaryAttributes <= 1; includeSummaryAttributes++)
				{
					for (int includeStates = 0; includeStates <= 1; includeStates++)
					{
						for (int includeLinks = 0; includeLinks <= 1; includeLinks++)
						{
							for (int includeDependencies = 0; includeDependencies <= 1; includeDependencies++)
							{
								doTestTypesApp1(includeAttributes == 1, includeSummaryAttributes == 1,
										includeStates == 1, includeLinks == 1, includeDependencies == 1);
							}
						}
					}
				}
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	private void doTestTypesApp1(boolean includeAttributes, boolean includeSummaryAttributes, boolean includeStates,
			boolean includeLinks, boolean includeDependencies)
			throws PersistenceException, InternalException, DataModelSerializationException, ArgumentException
	{
		List<TypeInfoDTO> types = dataModelManager.getTypes("com.example.ProjectX", null, 1, null, null, 999999,
				includeAttributes, includeSummaryAttributes, includeStates, includeLinks, includeDependencies);

		// Check we got 4 types back
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 4);

		// Load the DataModel to compare against
		String dataModelJson = FileUtils.readFileContents("apps/app1/model.dm");
		DataModel dataModel = DataModel.deserialize(dataModelJson);

		// Check each of the 4 types matches the model
		assertTypeInfoDTOMatchesDataModel(types, dataModel, "Address", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
		assertTypeInfoDTOMatchesDataModel(types, dataModel, "Claim", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
		assertTypeInfoDTOMatchesDataModel(types, dataModel, "Policy", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
		assertTypeInfoDTOMatchesDataModel(types, dataModel, "Person", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
	}

	@Test
	public void testTypesApp2() throws DeploymentException, PersistenceException, InternalException, IOException,
			URISyntaxException, RuntimeApplicationException, DataModelSerializationException, Exception
	{
		BigInteger deploymentId = deploy("/apps/app2");

		try
		{
			for (int includeAttributes = 0; includeAttributes <= 1; includeAttributes++)
			{
				for (int includeSummaryAttributes = 0; includeSummaryAttributes <= 1; includeSummaryAttributes++)
				{
					for (int includeStates = 0; includeStates <= 1; includeStates++)
					{
						for (int includeLinks = 0; includeLinks <= 1; includeLinks++)
						{
							for (int includeDependencies = 0; includeDependencies <= 1; includeDependencies++)
							{
								doTestTypesApp2(includeAttributes == 1, includeSummaryAttributes == 1,
										includeStates == 1, includeLinks == 1, includeDependencies == 1);
							}
						}
					}
				}
			}
		}
		finally
		{
			forceUndeploy(deploymentId);
		}
	}

	private void doTestTypesApp2(boolean includeAttributes, boolean includeSummaryAttributes, boolean includeStates,
			boolean includeLinks, boolean includeDependencies)
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, Exception
	{
		List<TypeInfoDTO> types = dataModelManager.getTypes("com.example.ProjectY", null, 1, null, null, 999999,
				includeAttributes, includeSummaryAttributes, includeStates, includeLinks, includeDependencies);

		// Check we got 3 types back
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 3);

		// Load the first DataModel to compare against
		String dataModelJson1 = FileUtils.readFileContents("apps/app2/cm/model1.dm");
		DataModel dataModel1 = DataModel.deserialize(dataModelJson1);

		// Check each of the types matches the model
		assertTypeInfoDTOMatchesDataModel(types, dataModel1, "Banana", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
		assertTypeInfoDTOMatchesDataModel(types, dataModel1, "Dimensions", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);

		// Load the second DataModel to compare against
		String dataModelJson2 = FileUtils.readFileContents("apps/app2/cm/model2.dm");
		DataModel dataModel2 = DataModel.deserialize(dataModelJson2);

		// Model 2 depends on model 1
		dataModel2.getForeignModels().add(dataModel1);

		// Check the types matches the model
		assertTypeInfoDTOMatchesDataModel(types, dataModel2, "NoodleSoup", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);
	}

	@Test
	public void testTypesHousingWithCrossAppDependency()
			throws DeploymentException, PersistenceException, InternalException, IOException, URISyntaxException,
			RuntimeApplicationException, DataModelSerializationException, Exception
	{
		// Deploy app that Housing depends on
		BigInteger deploymentIdApp1 = deploy("/apps/app1");
		BigInteger deploymentIdHousing = null;

		try
		{
			// Deploy Housing, which depends on app 1
			deploymentIdHousing = deploy("/apps/com.example.housing");
			for (int includeAttributes = 0; includeAttributes <= 1; includeAttributes++)
			{
				for (int includeSummaryAttributes = 0; includeSummaryAttributes <= 1; includeSummaryAttributes++)
				{
					for (int includeStates = 0; includeStates <= 1; includeStates++)
					{
						for (int includeLinks = 0; includeLinks <= 1; includeLinks++)
						{
							for (int includeDependencies = 0; includeDependencies <= 1; includeDependencies++)
							{
								doTestTypesHousingWithCrossAppDependency(includeAttributes == 1,
										includeSummaryAttributes == 1, includeStates == 1, includeLinks == 1,
										includeDependencies == 1);
							}
						}
					}
				}
			}
		}
		finally
		{
			if (deploymentIdHousing != null)
			{
				forceUndeploy(deploymentIdHousing);
			}
			forceUndeploy(deploymentIdApp1);
		}
	}

	public void doTestTypesHousingWithCrossAppDependency(boolean includeAttributes, boolean includeSummaryAttributes,
			boolean includeStates, boolean includeLinks, boolean includeDependencies)
			throws PersistenceException, InternalException, DataModelSerializationException, ArgumentException
	{
		List<TypeInfoDTO> types = dataModelManager.getTypes("com.example.housing", null, 1, null, null, 999999,
				includeAttributes, includeSummaryAttributes, includeStates, includeLinks, includeDependencies);

		// Check we got 1 type back
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 1);

		// Load the models to compare against
		String dataModelJson1 = FileUtils.readFileContents("apps/app1/model.dm");
		DataModel dataModel1 = DataModel.deserialize(dataModelJson1);
		String dataModelJsonHousing = FileUtils.readFileContents("apps/com.example.housing/cm/housing.dm");
		DataModel dataModelHousing = DataModel.deserialize(dataModelJsonHousing);
		dataModelHousing.getForeignModels().add(dataModel1);

		// Check the type info matches the model
		assertTypeInfoDTOMatchesDataModel(types, dataModelHousing, "House", includeAttributes, includeSummaryAttributes,
				includeStates, includeLinks, includeDependencies);

		if (includeDependencies)
		{
			// The significant point here is that we're checking that the 'dependencies' aspect 
			// mentions that House has a dependency on a foreign namespace in the other project.
			List<TypeInfoDependencyDTO> dependencies = types.get(0).getDependencies();
			Assert.assertNotNull(dependencies);
			Assert.assertEquals(dependencies.size(), 1);
			Assert.assertEquals(dependencies.get(0).getApplicationId(), "com.example.ProjectX");
			Assert.assertEquals(dependencies.get(0).getApplicationMajorVersion(), 1);
			Assert.assertEquals(dependencies.get(0).getNamespace(), "org.policycorporation");
		}

	}

}
