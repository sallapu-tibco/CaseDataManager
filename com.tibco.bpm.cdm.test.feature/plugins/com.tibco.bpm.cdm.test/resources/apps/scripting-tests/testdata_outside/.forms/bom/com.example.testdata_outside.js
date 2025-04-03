/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.testdata_outside.Testdata_outsidePackage.
 */

bpm.data.Loader.classDefiner["com.example.testdata_outside.Testdata_outsidePackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.testdata_outside.Testdata_outsidePackage");

    // Define the enumerations declared by this package.
    theClass.outside_enumeration = ["OUTSIDEENUMERATION1", "OUTSIDEENUMERATION2", "OUTSIDEENUMERATION3", "OUTSIDEENUMERATION4"];
    theClass.outside_enumeration.OUTSIDEENUMERATION1 = "OUTSIDEENUMERATION1";
    theClass.outside_enumeration.OUTSIDEENUMERATION2 = "OUTSIDEENUMERATION2";
    theClass.outside_enumeration.OUTSIDEENUMERATION3 = "OUTSIDEENUMERATION3";
    theClass.outside_enumeration.OUTSIDEENUMERATION4 = "OUTSIDEENUMERATION4";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.testdata_outside.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.testdata_outside.Testdata_outsidePackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.testdata_outside.Testdata_outsideFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.testdata_outside.Testdata_outsideFactory"] = function() {
    /** Constructor. */
    var theClass = function(form) {
        this.context = new Object();
        this.context.form = form;
        if (form.getLogger)
            this.context.logger = form.getLogger();
        this.context.item = null;
        this.context.id = null;
        this.$loader = form._loader;
    };
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.testdata_outside.Testdata_outsideFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.testdata_outside.outside_class",
        "com.example.testdata_outside.outside_class_object"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.testdata_outside.Testdata_outsideFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createOutside_class = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside.outside_class", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateOutside_class = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside.outside_class", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createOutside_class_object = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside.outside_class_object", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateOutside_class_object = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside.outside_class_object", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.testdata_outside.Testdata_outsideFactory"]();

/*
 * This provides an implementation of the BOM class com.example.testdata_outside.outside_class. 
 */

bpm.data.Loader.classDefiner["com.example.testdata_outside.outside_class"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testdata_outside.outside_class");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY = "outsideAttributeDateTimeZoneArray";
    theClass.ATTR_OUTSIDEATTRIBUTETEXTARRAY = "outsideAttributeTextArray";
    theClass.ATTR_OUTSIDEATTRIBUTEOBJECT = "outsideAttributeObject";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_OUTSIDEATTRIBUTETEXTARRAY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_OUTSIDEATTRIBUTEOBJECT] = {
        type: "com.example.testdata_outside.outside_class_object",
        baseType: "com.example.testdata_outside.outside_class_object",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY,
        theClass.ATTR_OUTSIDEATTRIBUTETEXTARRAY
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_OUTSIDEATTRIBUTEOBJECT
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY,
        theClass.ATTR_OUTSIDEATTRIBUTETEXTARRAY,
        theClass.ATTR_OUTSIDEATTRIBUTEOBJECT
    ];

    theClass.getName = function() {
        return "com.example.testdata_outside.outside_class";
    };


    Object.defineProperty(theClass.prototype, 'outsideAttributeDateTimeZoneArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class").ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY);
        },
        set: function(outsideAttributeDateTimeZoneArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class").ATTR_OUTSIDEATTRIBUTEDATETIMEZONEARRAY, outsideAttributeDateTimeZoneArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'outsideAttributeTextArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class").ATTR_OUTSIDEATTRIBUTETEXTARRAY);
        },
        set: function(outsideAttributeTextArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class").ATTR_OUTSIDEATTRIBUTETEXTARRAY, outsideAttributeTextArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'outsideAttributeObject', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class").ATTR_OUTSIDEATTRIBUTEOBJECT);
        },
        set: function(outsideAttributeObject) {
            var classRef = this.$loader.getClass("com.example.testdata_outside.outside_class");
            var attrRef = classRef.ATTR_OUTSIDEATTRIBUTEOBJECT;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(outsideAttributeObject == null) || outsideAttributeObject instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, outsideAttributeObject);
            } else {
                throw "Wrong input object type for 'outsideAttributeObject' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(outsideAttributeObject) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testdata_outside.outside_class"]();

/*
 * This provides an implementation of the BOM class com.example.testdata_outside.outside_class_object. 
 */

bpm.data.Loader.classDefiner["com.example.testdata_outside.outside_class_object"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testdata_outside.outside_class_object");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI = "outsideClassObjectAttributeURI";
    theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN = "outsideClassObjectAttributeBoolean";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI] = {
        type: "BomPrimitiveTypes.URI",
        baseType: "BomPrimitiveTypes.URI",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI,
        theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI,
        theClass.ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN
    ];

    theClass.getName = function() {
        return "com.example.testdata_outside.outside_class_object";
    };


    Object.defineProperty(theClass.prototype, 'outsideClassObjectAttributeURI', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class_object").ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI);
        },
        set: function(outsideClassObjectAttributeURI) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class_object").ATTR_OUTSIDECLASSOBJECTATTRIBUTEURI, outsideClassObjectAttributeURI);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'outsideClassObjectAttributeBoolean', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class_object").ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN);
        },
        set: function(outsideClassObjectAttributeBoolean) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside.outside_class_object").ATTR_OUTSIDECLASSOBJECTATTRIBUTEBOOLEAN, outsideClassObjectAttributeBoolean);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testdata_outside.outside_class_object"]();
