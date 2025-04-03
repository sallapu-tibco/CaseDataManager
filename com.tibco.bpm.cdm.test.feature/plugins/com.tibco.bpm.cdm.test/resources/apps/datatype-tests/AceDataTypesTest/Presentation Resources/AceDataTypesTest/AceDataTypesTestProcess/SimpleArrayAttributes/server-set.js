// NOTE: Process uses lists for multivalued primitive attributes.
simpleArrayAttributesField.booleanArrayAttr.add(true);
simpleArrayAttributesField.dateArrayAttr.add(DateTimeUtil.createDate());
simpleArrayAttributesField.dateTimeTZArrayAttr.add(DateTimeUtil.createDateTZ());
simpleArrayAttributesField.numberDecimalArrayAttr.add(123.45);
simpleArrayAttributesField.numberDecimalArrayAttr.add(67.89);
simpleArrayAttributesField.numberIntegerArrayAttr.add(123);
simpleArrayAttributesField.numberIntegerArrayAttr.add(456);
simpleArrayAttributesField.performerArrayAttr.add("Participant");
simpleArrayAttributesField.textArrayAttr.add("Some text");
simpleArrayAttributesField.textEnumArrayAttr.add(com_tibco_ace_datatypes_Enumeration.ENUMLIT2);
simpleArrayAttributesField.timeArrayAttr.add(DateTimeUtil.createTime());
simpleArrayAttributesField.uriArrayAttr.add("https://www.tibco.com/");
logger.info("Set process fields");