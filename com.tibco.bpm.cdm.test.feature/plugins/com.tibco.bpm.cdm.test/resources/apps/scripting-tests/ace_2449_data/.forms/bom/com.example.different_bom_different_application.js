/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.different_bom_different_application.Different_bom_different_applicationPackage.
 */

bpm.data.Loader.classDefiner["com.example.different_bom_different_application.Different_bom_different_applicationPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.different_bom_different_application.Different_bom_different_applicationPackage");

    // Define the enumerations declared by this package.
    theClass.states = ["CREATED", "COMPLETED"];
    theClass.states.CREATED = "CREATED";
    theClass.states.COMPLETED = "COMPLETED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.different_bom_different_application.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.different_bom_different_application.Different_bom_different_applicationPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.different_bom_different_application.Different_bom_different_applicationFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.different_bom_different_application.Different_bom_different_applicationFactory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.different_bom_different_application.Different_bom_different_applicationFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.different_bom_different_application.DifferentBOMDifferentApplication"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.different_bom_different_application.Different_bom_different_applicationFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createDifferentBOMDifferentApplication = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.different_bom_different_application.DifferentBOMDifferentApplication", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateDifferentBOMDifferentApplication = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.different_bom_different_application.DifferentBOMDifferentApplication", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.different_bom_different_application.Different_bom_different_applicationFactory"]();

/*
 * This provides an implementation of the BOM class com.example.different_bom_different_application.DifferentBOMDifferentApplication. 
 */

bpm.data.Loader.classDefiner["com.example.different_bom_different_application.DifferentBOMDifferentApplication"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.different_bom_different_application.DifferentBOMDifferentApplication");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CASEID = "caseid";
    theClass.ATTR_CASESTATE = "caseState";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CASEID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CASESTATE] = {
        type: "com.example.different_bom_different_application.states",
        baseType: "com.example.different_bom_different_application.states",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CASEID,
        theClass.ATTR_CASESTATE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CASEID,
        theClass.ATTR_CASESTATE
    ];

    theClass.getName = function() {
        return "com.example.different_bom_different_application.DifferentBOMDifferentApplication";
    };


    Object.defineProperty(theClass.prototype, 'caseid', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.different_bom_different_application.DifferentBOMDifferentApplication").ATTR_CASEID);
        },
        set: function(caseid) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.different_bom_different_application.DifferentBOMDifferentApplication").ATTR_CASEID, caseid);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'caseState', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.different_bom_different_application.DifferentBOMDifferentApplication").ATTR_CASESTATE);
        },
        set: function(caseState) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.different_bom_different_application.DifferentBOMDifferentApplication").ATTR_CASESTATE, caseState);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.different_bom_different_application.DifferentBOMDifferentApplication"]();
