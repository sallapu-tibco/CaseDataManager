/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.tibco.ace.datatypes.DatatypesPackage.
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.DatatypesPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.tibco.ace.datatypes.DatatypesPackage");

    // Define the enumerations declared by this package.
    theClass.Enumeration = ["ENUMLIT1", "ENUMLIT2", "ENUMLIT3", "ENUMLIT4"];
    theClass.Enumeration.ENUMLIT1 = "ENUMLIT1";
    theClass.Enumeration.ENUMLIT2 = "ENUMLIT2";
    theClass.Enumeration.ENUMLIT3 = "ENUMLIT3";
    theClass.Enumeration.ENUMLIT4 = "ENUMLIT4";
};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.DatatypesPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.tibco.ace.datatypes.DatatypesFactory. 
 */ 

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.DatatypesFactory"] = function() {
    /** Constructor. */
    var theClass = function(form) {
        this.context = new Object();
        this.context.form = form;
        if (form.getLogger)
            this.context.logger = form.getLogger();
        this.context.item = null;
        this.context.id = null;
        this.loader = form._loader;
    };
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.tibco.ace.datatypes.DatatypesFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.tibco.ace.datatypes.SimpleSingle",
        "com.tibco.ace.datatypes.ComplexParent",
        "com.tibco.ace.datatypes.SimpleArray",
        "com.tibco.ace.datatypes.ComplexChild"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.tibco.ace.datatypes.DatatypesFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createSimpleSingle = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.SimpleSingle", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateSimpleSingle = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.SimpleSingle", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createComplexParent = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.ComplexParent", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateComplexParent = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.ComplexParent", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createSimpleArray = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.SimpleArray", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateSimpleArray = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.SimpleArray", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createComplexChild = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.ComplexChild", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateComplexChild = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datatypes.ComplexChild", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.DatatypesFactory"]();

/*
 * This provides an implementation of the BOM class com.tibco.ace.datatypes.SimpleSingle. 
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.SimpleSingle"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datatypes.SimpleSingle");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_BOOLEANATTR = "booleanAttr";
    theClass.ATTR_DATEATTR = "dateAttr";
    theClass.ATTR_DATETIMETZATTR = "dateTimeTZAttr";
    theClass.ATTR_NUMBERDECIMALATTR = "numberDecimalAttr";
    theClass.ATTR_NUMBERINTEGERATTR = "numberIntegerAttr";
    theClass.ATTR_PERFORMERATTR = "performerAttr";
    theClass.ATTR_TEXTATTR = "textAttr";
    theClass.ATTR_TEXTENUMATTR = "textEnumAttr";
    theClass.ATTR_TIMEATTR = "timeAttr";
    theClass.ATTR_URIATTR = "uriAttr";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANATTR] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEATTR] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATETIMETZATTR] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERDECIMALATTR] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERINTEGERATTR] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 10,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_PERFORMERATTR] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTATTR] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTENUMATTR] = {
        type: "com.tibco.ace.datatypes.Enumeration",
        baseType: "com.tibco.ace.datatypes.Enumeration",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TIMEATTR] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_URIATTR] = {
        type: "BomPrimitiveTypes.URI",
        baseType: "BomPrimitiveTypes.URI",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANATTR,
        theClass.ATTR_DATEATTR,
        theClass.ATTR_DATETIMETZATTR,
        theClass.ATTR_NUMBERDECIMALATTR,
        theClass.ATTR_NUMBERINTEGERATTR,
        theClass.ATTR_PERFORMERATTR,
        theClass.ATTR_TEXTATTR,
        theClass.ATTR_TEXTENUMATTR,
        theClass.ATTR_TIMEATTR,
        theClass.ATTR_URIATTR
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANATTR,
        theClass.ATTR_DATEATTR,
        theClass.ATTR_DATETIMETZATTR,
        theClass.ATTR_NUMBERDECIMALATTR,
        theClass.ATTR_NUMBERINTEGERATTR,
        theClass.ATTR_PERFORMERATTR,
        theClass.ATTR_TEXTATTR,
        theClass.ATTR_TEXTENUMATTR,
        theClass.ATTR_TIMEATTR,
        theClass.ATTR_URIATTR
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datatypes.SimpleSingle";
    };


    Object.defineProperty(theClass.prototype, 'booleanAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_BOOLEANATTR);
        },
        set: function(booleanAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_BOOLEANATTR, booleanAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_DATEATTR);
        },
        set: function(dateAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_DATEATTR, dateAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateTimeTZAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_DATETIMETZATTR);
        },
        set: function(dateTimeTZAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_DATETIMETZATTR, dateTimeTZAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberDecimalAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_NUMBERDECIMALATTR);
        },
        set: function(numberDecimalAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_NUMBERDECIMALATTR, numberDecimalAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberIntegerAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_NUMBERINTEGERATTR);
        },
        set: function(numberIntegerAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_NUMBERINTEGERATTR, numberIntegerAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'performerAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_PERFORMERATTR);
        },
        set: function(performerAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_PERFORMERATTR, performerAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TEXTATTR);
        },
        set: function(textAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TEXTATTR, textAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textEnumAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TEXTENUMATTR);
        },
        set: function(textEnumAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TEXTENUMATTR, textEnumAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'timeAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TIMEATTR);
        },
        set: function(timeAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_TIMEATTR, timeAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'uriAttr', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_URIATTR);
        },
        set: function(uriAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleSingle").ATTR_URIATTR, uriAttr);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.SimpleSingle"]();

/*
 * This provides an implementation of the BOM class com.tibco.ace.datatypes.ComplexChild. 
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.ComplexChild"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datatypes.ComplexChild");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CHILDATTR1 = "childAttr1";
    theClass.ATTR_CHILDATTR2 = "childAttr2";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CHILDATTR1] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CHILDATTR2] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CHILDATTR1,
        theClass.ATTR_CHILDATTR2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CHILDATTR1,
        theClass.ATTR_CHILDATTR2
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datatypes.ComplexChild";
    };


    Object.defineProperty(theClass.prototype, 'childAttr1', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexChild").ATTR_CHILDATTR1);
        },
        set: function(childAttr1) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexChild").ATTR_CHILDATTR1, childAttr1);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'childAttr2', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexChild").ATTR_CHILDATTR2);
        },
        set: function(childAttr2) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexChild").ATTR_CHILDATTR2, childAttr2);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.ComplexChild"]();

/*
 * This provides an implementation of the BOM class com.tibco.ace.datatypes.SimpleArray. 
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.SimpleArray"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datatypes.SimpleArray");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_BOOLEANARRAYATTR = "booleanArrayAttr";
    theClass.ATTR_DATEARRAYATTR = "dateArrayAttr";
    theClass.ATTR_DATETIMETZARRAYATTR = "dateTimeTZArrayAttr";
    theClass.ATTR_NUMBERDECIMALARRAYATTR = "numberDecimalArrayAttr";
    theClass.ATTR_NUMBERINTEGERARRAYATTR = "numberIntegerArrayAttr";
    theClass.ATTR_PERFORMERARRAYATTR = "performerArrayAttr";
    theClass.ATTR_TEXTARRAYATTR = "textArrayAttr";
    theClass.ATTR_TEXTENUMARRAYATTR = "textEnumArrayAttr";
    theClass.ATTR_TIMEARRAYATTR = "timeArrayAttr";
    theClass.ATTR_URIARRAYATTR = "uriArrayAttr";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANARRAYATTR] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEARRAYATTR] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATETIMETZARRAYATTR] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERDECIMALARRAYATTR] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERINTEGERARRAYATTR] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 10,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_PERFORMERARRAYATTR] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTARRAYATTR] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTENUMARRAYATTR] = {
        type: "com.tibco.ace.datatypes.Enumeration",
        baseType: "com.tibco.ace.datatypes.Enumeration",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TIMEARRAYATTR] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_URIARRAYATTR] = {
        type: "BomPrimitiveTypes.URI",
        baseType: "BomPrimitiveTypes.URI",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANARRAYATTR,
        theClass.ATTR_DATEARRAYATTR,
        theClass.ATTR_DATETIMETZARRAYATTR,
        theClass.ATTR_NUMBERDECIMALARRAYATTR,
        theClass.ATTR_NUMBERINTEGERARRAYATTR,
        theClass.ATTR_PERFORMERARRAYATTR,
        theClass.ATTR_TEXTARRAYATTR,
        theClass.ATTR_TEXTENUMARRAYATTR,
        theClass.ATTR_TIMEARRAYATTR,
        theClass.ATTR_URIARRAYATTR
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANARRAYATTR,
        theClass.ATTR_DATEARRAYATTR,
        theClass.ATTR_DATETIMETZARRAYATTR,
        theClass.ATTR_NUMBERDECIMALARRAYATTR,
        theClass.ATTR_NUMBERINTEGERARRAYATTR,
        theClass.ATTR_PERFORMERARRAYATTR,
        theClass.ATTR_TEXTARRAYATTR,
        theClass.ATTR_TEXTENUMARRAYATTR,
        theClass.ATTR_TIMEARRAYATTR,
        theClass.ATTR_URIARRAYATTR
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datatypes.SimpleArray";
    };


    Object.defineProperty(theClass.prototype, 'booleanArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_BOOLEANARRAYATTR);
        },
        set: function(booleanArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_BOOLEANARRAYATTR, booleanArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_DATEARRAYATTR);
        },
        set: function(dateArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_DATEARRAYATTR, dateArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateTimeTZArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_DATETIMETZARRAYATTR);
        },
        set: function(dateTimeTZArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_DATETIMETZARRAYATTR, dateTimeTZArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberDecimalArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_NUMBERDECIMALARRAYATTR);
        },
        set: function(numberDecimalArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_NUMBERDECIMALARRAYATTR, numberDecimalArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberIntegerArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_NUMBERINTEGERARRAYATTR);
        },
        set: function(numberIntegerArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_NUMBERINTEGERARRAYATTR, numberIntegerArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'performerArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_PERFORMERARRAYATTR);
        },
        set: function(performerArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_PERFORMERARRAYATTR, performerArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TEXTARRAYATTR);
        },
        set: function(textArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TEXTARRAYATTR, textArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textEnumArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TEXTENUMARRAYATTR);
        },
        set: function(textEnumArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TEXTENUMARRAYATTR, textEnumArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'timeArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TIMEARRAYATTR);
        },
        set: function(timeArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_TIMEARRAYATTR, timeArrayAttr);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'uriArrayAttr', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_URIARRAYATTR);
        },
        set: function(uriArrayAttr) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.SimpleArray").ATTR_URIARRAYATTR, uriArrayAttr);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.SimpleArray"]();

/*
 * This provides an implementation of the BOM class com.tibco.ace.datatypes.ComplexParent. 
 */

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.ComplexParent"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datatypes.ComplexParent");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_PARENTATTR1 = "parentAttr1";
    theClass.ATTR_PARENTATTR2 = "parentAttr2";
    theClass.ATTR_CHILD = "child";
    theClass.ATTR_CHILDREN = "children";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_PARENTATTR1] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_PARENTATTR2] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CHILD] = {
        type: "com.tibco.ace.datatypes.ComplexChild",
        baseType: "com.tibco.ace.datatypes.ComplexChild",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CHILDREN] = {
        type: "com.tibco.ace.datatypes.ComplexChild",
        baseType: "com.tibco.ace.datatypes.ComplexChild",
        primitive: false,
        multivalued: true,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_PARENTATTR1,
        theClass.ATTR_PARENTATTR2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CHILD,
        theClass.ATTR_CHILDREN
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_PARENTATTR1,
        theClass.ATTR_PARENTATTR2,
        theClass.ATTR_CHILD,
        theClass.ATTR_CHILDREN
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datatypes.ComplexParent";
    };


    Object.defineProperty(theClass.prototype, 'parentAttr1', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_PARENTATTR1);
        },
        set: function(parentAttr1) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_PARENTATTR1, parentAttr1);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'parentAttr2', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_PARENTATTR2);
        },
        set: function(parentAttr2) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_PARENTATTR2, parentAttr2);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'child', {
        get: function() {
            return this._getComplexAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_CHILD);
        },
        set: function(child) {
            var classRef = this.loader.getClass("com.tibco.ace.datatypes.ComplexParent");
            var attrRef = classRef.ATTR_CHILD;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("child instanceof this.loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, child);
            } else {
                throw "Wrong input object type.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'children', {
        get: function() {
            return this._getComplexArrayAttribute(this.loader.getClass("com.tibco.ace.datatypes.ComplexParent").ATTR_CHILDREN);
        },
        set: function(children) {
            throw "Unsupported complex array attribute: children";
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.tibco.ace.datatypes.ComplexParent"]();
