// NOTE: Forms uses arrays for multivalued primitive parameters.
// BUG!!! array literal syntax not accepted
//data.setBooleanArrayField([true]);
data.setBooleanArrayField(new Array(true));
data.setDateArrayField(new Array(new Date()));
data.setDateTimeArrayField(new Array(new Date()));
data.setNumberDecimalArrayField(new Array(123.45, 67.89));
data.setNumberIntegerArrayField(new Array(123, 456));
data.setPerformerArrayField(new Array("Participant"));
data.setTextArrayField(new Array("Some text"));
data.setTimeArrayField(new Array(new Date()));
data.setUriArrayField(new Array("https://www.tibco.com/"));
logger.info("Set form data");