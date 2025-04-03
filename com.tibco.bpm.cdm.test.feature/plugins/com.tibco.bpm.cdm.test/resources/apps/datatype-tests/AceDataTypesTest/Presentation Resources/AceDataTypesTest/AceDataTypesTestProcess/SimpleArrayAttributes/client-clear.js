// NOTE: Forms uses arrays for multivalued primitive attributes.
var simpleArrayAttributesField = data.getSimpleArrayAttributesField();
// BUG!!! Array literal syntax not supported.
//simpleArrayAttributesField.setBooleanArrayAttr([]);
simpleArrayAttributesField.setBooleanArrayAttr(new Array());
simpleArrayAttributesField.setDateArrayAttr(new Array());
simpleArrayAttributesField.setDateTimeTZArrayAttr(new Array());
simpleArrayAttributesField.setNumberDecimalArrayAttr(new Array());
simpleArrayAttributesField.setNumberIntegerArrayAttr(new Array());
simpleArrayAttributesField.setPerformerArrayAttr(new Array());
simpleArrayAttributesField.setTextArrayAttr(new Array());
simpleArrayAttributesField.setTextEnumArrayAttr(new Array());
simpleArrayAttributesField.setTimeArrayAttr(new Array());
simpleArrayAttributesField.setUriArrayAttr(new Array());
logger.info("Cleared form data");