// NOTE: Process uses lists for multivalued complex data and attributes.
var complexArrayField = com_tibco_ace_datatypes_Factory.createComplexParent();
complexArrayField.parentAttr1 = "Some text";
complexArrayField.parentAttr2 = "Some more text";
var child = com_tibco_ace_datatypes_Factory.createComplexChild();
child.childAttr1 = "Some child text";
child.childAttr2 = "Some more child text";
complexArrayField.setChild(child);
var children = factory.com_tibco_ace_datatypes.createComplexChild();
children.childAttr1 = "Some children text";
children.childAttr2 = "Some more children text";
complexArrayField1.children.add(children);
complexArrayField.add(complexArrayField);
Log.write("Set process fields");