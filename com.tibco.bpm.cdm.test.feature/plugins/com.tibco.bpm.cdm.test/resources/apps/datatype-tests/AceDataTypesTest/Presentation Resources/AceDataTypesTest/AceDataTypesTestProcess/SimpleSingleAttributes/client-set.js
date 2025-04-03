var simpleSingleAttributesParameter = data.getSimpleSingleAttributesParameter();
simpleSingleAttributesParameter.setBooleanAttr(true);
simpleSingleAttributesParameter.setDateAttr(new Date());
simpleSingleAttributesParameter.setDateTimeTZAttr(new Date());
simpleSingleAttributesParameter.setNumberDecimalAttr(123.45);
simpleSingleAttributesParameter.setNumberIntegerAttr(123);
simpleSingleAttributesParameter.setPerformerAttr("Participant");
simpleSingleAttributesParameter.setTextAttr("Some text");
// BUG!!! Referring to the enum fields gives error.
//simpleSingleAttributesParameter.setTextEnumAttr(pkg.com_tibco_ace_datatypes.Enumeration.ENUMLIT2);
simpleSingleAttributesParameter.setTextEnumAttr("ENUMLIT2");
simpleSingleAttributesParameter.setTimeAttr(new Date());
simpleSingleAttributesParameter.setUriAttr("https://www.tibco.com/");
logger.info("Set form data");