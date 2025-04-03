
/*
 * This provides an implementation of the BOM class com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess. 
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_BOOLEANPARAMETER = "booleanParameter";
    theClass.ATTR_DATEPARAMETER = "dateParameter";
    theClass.ATTR_DATETIMEPARAMETER = "dateTimeParameter";
    theClass.ATTR_NUMBERDECIMALPARAMETER = "numberDecimalParameter";
    theClass.ATTR_NUMBERINTEGERPARAMETER = "numberIntegerParameter";
    theClass.ATTR_PERFORMERPARAMETER = "performerParameter";
    theClass.ATTR_TEXTPARAMETER = "textParameter";
    theClass.ATTR_TIMEPARAMETER = "timeParameter";
    theClass.ATTR_URIPARAMETER = "uriParameter";
    theClass.ATTR_SIMPLESINGLEATTRIBUTESPARAMETER = "simpleSingleAttributesParameter";
    theClass.ATTR_COMPLEXSINGLEPARAMETER = "complexSingleParameter";
    theClass.ATTR_BOOLEANARRAYFIELD = "booleanArrayField";
    theClass.ATTR_DATEARRAYFIELD = "dateArrayField";
    theClass.ATTR_DATETIMEARRAYFIELD = "dateTimeArrayField";
    theClass.ATTR_NUMBERDECIMALARRAYFIELD = "numberDecimalArrayField";
    theClass.ATTR_NUMBERINTEGERARRAYFIELD = "numberIntegerArrayField";
    theClass.ATTR_PERFORMERARRAYFIELD = "performerArrayField";
    theClass.ATTR_TEXTARRAYFIELD = "textArrayField";
    theClass.ATTR_TIMEARRAYFIELD = "timeArrayField";
    theClass.ATTR_URIARRAYFIELD = "uriArrayField";
    theClass.ATTR_SIMPLEARRAYATTRIBUTESFIELD = "simpleArrayAttributesField";
    theClass.ATTR_COMPLEXARRAYFIELD = "complexArrayField";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANPARAMETER] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEPARAMETER] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATETIMEPARAMETER] = {
        type: "BomPrimitiveTypes.DateTime",
        baseType: "BomPrimitiveTypes.DateTime",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERDECIMALPARAMETER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 10,
        decimalPlaces: 2

    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERINTEGERPARAMETER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 9,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_PERFORMERPARAMETER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTPARAMETER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TIMEPARAMETER] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_URIPARAMETER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_SIMPLESINGLEATTRIBUTESPARAMETER] = {
        type: "com.tibco.ace.datatypes.SimpleSingle",
        baseType: "com.tibco.ace.datatypes.SimpleSingle",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_COMPLEXSINGLEPARAMETER] = {
        type: "com.tibco.ace.datatypes.ComplexParent",
        baseType: "com.tibco.ace.datatypes.ComplexParent",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANARRAYFIELD] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEARRAYFIELD] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATETIMEARRAYFIELD] = {
        type: "BomPrimitiveTypes.DateTime",
        baseType: "BomPrimitiveTypes.DateTime",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERDECIMALARRAYFIELD] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 10,
        decimalPlaces: 2

    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERINTEGERARRAYFIELD] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 9,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_PERFORMERARRAYFIELD] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTARRAYFIELD] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TIMEARRAYFIELD] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_URIARRAYFIELD] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_SIMPLEARRAYATTRIBUTESFIELD] = {
        type: "com.tibco.ace.datatypes.SimpleArray",
        baseType: "com.tibco.ace.datatypes.SimpleArray",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_COMPLEXARRAYFIELD] = {
        type: "com.tibco.ace.datatypes.ComplexParent",
        baseType: "com.tibco.ace.datatypes.ComplexParent",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANPARAMETER,
        theClass.ATTR_DATEPARAMETER,
        theClass.ATTR_DATETIMEPARAMETER,
        theClass.ATTR_NUMBERDECIMALPARAMETER,
        theClass.ATTR_NUMBERINTEGERPARAMETER,
        theClass.ATTR_PERFORMERPARAMETER,
        theClass.ATTR_TEXTPARAMETER,
        theClass.ATTR_TIMEPARAMETER,
        theClass.ATTR_URIPARAMETER,
        theClass.ATTR_BOOLEANARRAYFIELD,
        theClass.ATTR_DATEARRAYFIELD,
        theClass.ATTR_DATETIMEARRAYFIELD,
        theClass.ATTR_NUMBERDECIMALARRAYFIELD,
        theClass.ATTR_NUMBERINTEGERARRAYFIELD,
        theClass.ATTR_PERFORMERARRAYFIELD,
        theClass.ATTR_TEXTARRAYFIELD,
        theClass.ATTR_TIMEARRAYFIELD,
        theClass.ATTR_URIARRAYFIELD
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_SIMPLESINGLEATTRIBUTESPARAMETER,
        theClass.ATTR_COMPLEXSINGLEPARAMETER,
        theClass.ATTR_SIMPLEARRAYATTRIBUTESFIELD,
        theClass.ATTR_COMPLEXARRAYFIELD
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANPARAMETER,
        theClass.ATTR_DATEPARAMETER,
        theClass.ATTR_DATETIMEPARAMETER,
        theClass.ATTR_NUMBERDECIMALPARAMETER,
        theClass.ATTR_NUMBERINTEGERPARAMETER,
        theClass.ATTR_PERFORMERPARAMETER,
        theClass.ATTR_TEXTPARAMETER,
        theClass.ATTR_TIMEPARAMETER,
        theClass.ATTR_URIPARAMETER,
        theClass.ATTR_SIMPLESINGLEATTRIBUTESPARAMETER,
        theClass.ATTR_COMPLEXSINGLEPARAMETER,
        theClass.ATTR_BOOLEANARRAYFIELD,
        theClass.ATTR_DATEARRAYFIELD,
        theClass.ATTR_DATETIMEARRAYFIELD,
        theClass.ATTR_NUMBERDECIMALARRAYFIELD,
        theClass.ATTR_NUMBERINTEGERARRAYFIELD,
        theClass.ATTR_PERFORMERARRAYFIELD,
        theClass.ATTR_TEXTARRAYFIELD,
        theClass.ATTR_TIMEARRAYFIELD,
        theClass.ATTR_URIARRAYFIELD,
        theClass.ATTR_SIMPLEARRAYATTRIBUTESFIELD,
        theClass.ATTR_COMPLEXARRAYFIELD
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess";
    };


    Object.defineProperty(theClass.prototype, 'booleanParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_BOOLEANPARAMETER);
        },
        set: function(booleanParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_BOOLEANPARAMETER, booleanParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATEPARAMETER);
        },
        set: function(dateParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATEPARAMETER, dateParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateTimeParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATETIMEPARAMETER);
        },
        set: function(dateTimeParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATETIMEPARAMETER, dateTimeParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberDecimalParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERDECIMALPARAMETER);
        },
        set: function(numberDecimalParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERDECIMALPARAMETER, numberDecimalParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberIntegerParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERINTEGERPARAMETER);
        },
        set: function(numberIntegerParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERINTEGERPARAMETER, numberIntegerParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'performerParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_PERFORMERPARAMETER);
        },
        set: function(performerParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_PERFORMERPARAMETER, performerParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TEXTPARAMETER);
        },
        set: function(textParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TEXTPARAMETER, textParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'timeParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TIMEPARAMETER);
        },
        set: function(timeParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TIMEPARAMETER, timeParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'uriParameter', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_URIPARAMETER);
        },
        set: function(uriParameter) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_URIPARAMETER, uriParameter);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'simpleSingleAttributesParameter', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_SIMPLESINGLEATTRIBUTESPARAMETER);
        },
        set: function(simpleSingleAttributesParameter) {
            var classRef = this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
            var attrRef = classRef.ATTR_SIMPLESINGLEATTRIBUTESPARAMETER;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(simpleSingleAttributesParameter == null) || simpleSingleAttributesParameter instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, simpleSingleAttributesParameter);
            } else {
                throw "Wrong input object type for 'simpleSingleAttributesParameter' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(simpleSingleAttributesParameter) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'complexSingleParameter', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_COMPLEXSINGLEPARAMETER);
        },
        set: function(complexSingleParameter) {
            var classRef = this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
            var attrRef = classRef.ATTR_COMPLEXSINGLEPARAMETER;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(complexSingleParameter == null) || complexSingleParameter instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, complexSingleParameter);
            } else {
                throw "Wrong input object type for 'complexSingleParameter' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(complexSingleParameter) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'booleanArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_BOOLEANARRAYFIELD);
        },
        set: function(booleanArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_BOOLEANARRAYFIELD, booleanArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATEARRAYFIELD);
        },
        set: function(dateArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATEARRAYFIELD, dateArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateTimeArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATETIMEARRAYFIELD);
        },
        set: function(dateTimeArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_DATETIMEARRAYFIELD, dateTimeArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberDecimalArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERDECIMALARRAYFIELD);
        },
        set: function(numberDecimalArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERDECIMALARRAYFIELD, numberDecimalArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberIntegerArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERINTEGERARRAYFIELD);
        },
        set: function(numberIntegerArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_NUMBERINTEGERARRAYFIELD, numberIntegerArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'performerArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_PERFORMERARRAYFIELD);
        },
        set: function(performerArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_PERFORMERARRAYFIELD, performerArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TEXTARRAYFIELD);
        },
        set: function(textArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TEXTARRAYFIELD, textArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'timeArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TIMEARRAYFIELD);
        },
        set: function(timeArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_TIMEARRAYFIELD, timeArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'uriArrayField', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_URIARRAYFIELD);
        },
        set: function(uriArrayField) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_URIARRAYFIELD, uriArrayField);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'simpleArrayAttributesField', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_SIMPLEARRAYATTRIBUTESFIELD);
        },
        set: function(simpleArrayAttributesField) {
            var classRef = this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess");
            var attrRef = classRef.ATTR_SIMPLEARRAYATTRIBUTESFIELD;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(simpleArrayAttributesField == null) || simpleArrayAttributesField instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, simpleArrayAttributesField);
            } else {
                throw "Wrong input object type for 'simpleArrayAttributesField' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(simpleArrayAttributesField) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'complexArrayField', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess").ATTR_COMPLEXARRAYFIELD);
        },
        set: function(complexArrayField) {
            throw "Cannot re-assign multi-valued attribute 'complexArrayField' in the type 'com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess'";
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.test.AceDataTypesTest.AceDataTypesTestProcess"]();
