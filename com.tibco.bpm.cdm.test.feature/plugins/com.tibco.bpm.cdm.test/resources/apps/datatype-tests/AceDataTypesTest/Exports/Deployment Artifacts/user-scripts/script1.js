data.booleanParameter=false; 
Log.write('---- Full year is '+data.dateParameter.getFullYear());
Log.write('--parseInt is--'+parseInt(data.numberIntegerParameter));
Log.write('---- Datetime value is '+data.dateTimeParameter.toISOString());
Log.write('---- Minutes in timeParameter is '+data.timeParameter.getMinutes());
var child1 = factory.com_tibco_ace_datatypes.createComplexChild();
child1.childAttr1='child 1';
data.complexSingleParameter.child = child1;
data.complexSingleParameter.children.push(child1);
var simpleSingle = factory.com_tibco_ace_datatypes.createSimpleSingle();
simpleSingle.booleanAttr = true;
simpleSingle.dateTimeTZAttr = new Date('2020-06-05T11:04:04Z');
data.textParameter=simpleSingle.dateTimeTZAttr; //implicit type conversion from DateTimeTZ to Text
simpleSingle.textAttr = data.dateParameter; //implicit type conversion from Date to Text
simpleSingle.numberDecimalAttr = data.numberDecimalParameter;
var c = 0.1;
simpleSingle.numberDecimalAttr = c.add(0.2); //additional functions to get around javascript floating point issues
Log.write('floating point sum of 0.1 and 0.2 is '+ (0.1+0.2)+ ' once you use add function it becomes '+simpleSingle.numberDecimalAttr);
simpleSingle.dateAttr = data.dateParameter;
simpleSingle.numberIntegerAttr = data.numberIntegerParameter+100;
simpleSingle.timeAttr = new Date('11:08:50');
simpleSingle.booleanAttr = true;
simpleSingle.textEnumAttr = pkg.com_tibco_ace_datatypes.Enumeration.ENUMLIT1;						
data.simpleSingleAttributesParameter = simpleSingle;
var parent = factory.com_tibco_ace_datatypes.createComplexParent();
parent.children.push(child1);
child1.childAttr1='updated child 1'; //within script, objects are referenced
data.numberDecimalParameter = data.numberDecimalParameter.add(0.1);