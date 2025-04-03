/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelPackage.
 */

bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelPackage");

    // Define the enumerations declared by this package.
    theClass.states = ["CREATED", "COMPLETED"];
    theClass.states.CREATED = "CREATED";
    theClass.states.COMPLETED = "COMPLETED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.differentbusinessobjectmodel.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelFactory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.differentbusinessobjectmodel.DifferentBOMCase"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createDifferentBOMCase = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.differentbusinessobjectmodel.DifferentBOMCase", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateDifferentBOMCase = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.differentbusinessobjectmodel.DifferentBOMCase", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentbusinessobjectmodelFactory"]();

/*
 * This provides an implementation of the BOM class com.example.differentbusinessobjectmodel.DifferentBOMCase. 
 */

bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentBOMCase"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.differentbusinessobjectmodel.DifferentBOMCase");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CASESTATE = "caseState";
    theClass.ATTR_CASEID = "caseid";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CASESTATE] = {
        type: "com.example.differentbusinessobjectmodel.states",
        baseType: "com.example.differentbusinessobjectmodel.states",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CASEID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CASESTATE,
        theClass.ATTR_CASEID
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CASESTATE,
        theClass.ATTR_CASEID
    ];

    theClass.getName = function() {
        return "com.example.differentbusinessobjectmodel.DifferentBOMCase";
    };


    Object.defineProperty(theClass.prototype, 'caseState', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.differentbusinessobjectmodel.DifferentBOMCase").ATTR_CASESTATE);
        },
        set: function(caseState) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.differentbusinessobjectmodel.DifferentBOMCase").ATTR_CASESTATE, caseState);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'caseid', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.differentbusinessobjectmodel.DifferentBOMCase").ATTR_CASEID);
        },
        set: function(caseid) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.differentbusinessobjectmodel.DifferentBOMCase").ATTR_CASEID, caseid);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.differentbusinessobjectmodel.DifferentBOMCase"]();
