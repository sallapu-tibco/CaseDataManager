// NOTE: Forms uses lists for multivalued complex data and attributes.
var complexArrayField = factory.com_tibco_ace_datatypes.createComplexParent();
complexArrayField.setParentAttr1("Some text");
complexArrayField.setParentAttr2("Some more text");
var child = factory.com_tibco_ace_datatypes.createComplexChild();
child.setChildAttr1("Some child text");
child.setChildAttr2("Some more child text");
complexArrayField.setChild(child);
var children = factory.com_tibco_ace_datatypes.createComplexChild();
children.setChildAttr1("Some children text");
children.setChildAttr2("Some more children text");
complexArrayField.getChildren().add(children1);
data.getComplexArrayField().add(complexArrayField);
// The following line is necessary in order to populate the Detail pane.
pane.complexArrayField__master.setSelection(complexArrayField);
logger.info("Set form data");