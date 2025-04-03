// NOTE: process uses lists for multivalued primitive fields.
// BUG!!! array literal syntax not accepted
//booleanArrayField = [true];
booleanArrayField.add(true);
dateArrayField.add(DateTimeUtil.createDate());
dateTimeArrayField.add(DateTimeUtil.createDatetime());
numberDecimalArrayField.add(123.45);
numberDecimalArrayField.add(67.89);
numberIntegerArrayField.add(123, 456);
performerArrayField.add("Participant");
textArrayField.add("Some text");
timeArrayField.add(DateTimeUtil.createTime());
uriArrayField.add("https://www.tibco.com/");
Log.write("Set process fields");