// NOTE: Forms uses arrays for multivalued primitive parameters.
// BUG!!! array literal syntax not accepted
//data.setBooleanArrayField([]);
data.setBooleanArrayField(new Array());
data.setDateArrayField(new Array());
data.setDateTimeArrayField(new Array());
data.setNumberDecimalArrayField(new Array());
data.setNumberIntegerArrayField(new Array());
data.setPerformerArrayField(new Array());
data.setTextArrayField(new Array());
data.setTimeArrayField(new Array());
data.setUriArrayField(new Array());
logger.info("Cleared form data");