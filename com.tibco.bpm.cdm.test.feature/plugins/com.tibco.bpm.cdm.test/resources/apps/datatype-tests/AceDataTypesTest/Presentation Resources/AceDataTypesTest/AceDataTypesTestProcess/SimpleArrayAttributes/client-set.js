// NOTE: Forms uses arrays for multivalued primitive attributes.
var simpleArrayAttributesField = data.getSimpleArrayAttributesField();
// BUG!!! Array literal syntax not supported.
//simpleArrayAttributesField.setBooleanArrayAttr([true]);
simpleArrayAttributesField.setBooleanArrayAttr(new Array(true));
simpleArrayAttributesField.setDateArrayAttr(new Array(new Date()));
simpleArrayAttributesField.setDateTimeTZArrayAttr(new Array(new Date()));
simpleArrayAttributesField.setNumberDecimalArrayAttr(new Array(123.45, 67.89));
simpleArrayAttributesField.setNumberIntegerArrayAttr(new Array(123, 456));
simpleArrayAttributesField.setPerformerArrayAttr(new Array("Participant"));
simpleArrayAttributesField.setTextArrayAttr(new Array("Some text"));
// BUG!!! Referring to the enum fields gives error.
//simpleArrayAttributesField.setTextEnumArrayAttr(new Array(pkg.com_tibco_ace_datatypes.Enumeration.ENUMLIT2));
simpleArrayAttributesField.setTextEnumArrayAttr(new Array("ENUMLIT2"));
simpleArrayAttributesField.setTimeArrayAttr(new Array(new Date()));
simpleArrayAttributesField.setUriArrayAttr(new Array("https://www.tibco.com/"));
logger.info("Set form data");