
/*
 * This provides an implementation of the BOM class com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess. 
 */

bpm.data.Loader.classDefiner["com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_TEXTPARAM = "textParam";
    theClass.ATTR_NUMBERPARAM = "numberParam";
    theClass.ATTR_BOOLEANPARAM = "booleanParam";
    theClass.ATTR_DATEPARAM = "dateParam";
    theClass.ATTR_TIMEPARAM = "timeParam";
    theClass.ATTR_DATETIMETZPARAM = "dateTimeTZParam";
    theClass.ATTR_PERFORMERPARAM = "performerParam";
    theClass.ATTR_TEXTARRAYPARAM = "textArrayParam";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTPARAM] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERPARAM] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 10,
        decimalPlaces: 2

    };
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANPARAM] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEPARAM] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TIMEPARAM] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATETIMETZPARAM] = {
        type: "BomPrimitiveTypes.DateTime",
        baseType: "BomPrimitiveTypes.DateTime",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_PERFORMERPARAM] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTARRAYPARAM] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_TEXTPARAM,
        theClass.ATTR_NUMBERPARAM,
        theClass.ATTR_BOOLEANPARAM,
        theClass.ATTR_DATEPARAM,
        theClass.ATTR_TIMEPARAM,
        theClass.ATTR_DATETIMETZPARAM,
        theClass.ATTR_PERFORMERPARAM,
        theClass.ATTR_TEXTARRAYPARAM
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_TEXTPARAM,
        theClass.ATTR_NUMBERPARAM,
        theClass.ATTR_BOOLEANPARAM,
        theClass.ATTR_DATEPARAM,
        theClass.ATTR_TIMEPARAM,
        theClass.ATTR_DATETIMETZPARAM,
        theClass.ATTR_PERFORMERPARAM,
        theClass.ATTR_TEXTARRAYPARAM
    ];

    theClass.getName = function() {
        return "com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess";
    };


    Object.defineProperty(theClass.prototype, 'textParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TEXTPARAM);
        },
        set: function(textParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TEXTPARAM, textParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_NUMBERPARAM);
        },
        set: function(numberParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_NUMBERPARAM, numberParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'booleanParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_BOOLEANPARAM);
        },
        set: function(booleanParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_BOOLEANPARAM, booleanParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_DATEPARAM);
        },
        set: function(dateParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_DATEPARAM, dateParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'timeParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TIMEPARAM);
        },
        set: function(timeParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TIMEPARAM, timeParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateTimeTZParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_DATETIMETZPARAM);
        },
        set: function(dateTimeTZParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_DATETIMETZPARAM, dateTimeTZParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'performerParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_PERFORMERPARAM);
        },
        set: function(performerParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_PERFORMERPARAM, performerParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textArrayParam', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TEXTARRAYPARAM);
        },
        set: function(textArrayParam) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess").ATTR_TEXTARRAYPARAM, textArrayParam);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.processsimpledata.ProcessSimpleData.ProcessSimpleDataProcess"]();
