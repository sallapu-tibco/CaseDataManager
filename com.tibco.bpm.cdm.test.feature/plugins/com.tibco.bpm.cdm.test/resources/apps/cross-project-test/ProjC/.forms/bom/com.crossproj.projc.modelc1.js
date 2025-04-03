/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.projc.modelc1.Modelc1Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.Modelc1Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.projc.modelc1.Modelc1Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.projc.modelc1.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.Modelc1Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.projc.modelc1.Modelc1Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.Modelc1Factory"] = function() {
    /** Constructor. */
    var theClass = function(form) {
        this.context = {};
        this.context.form = form;
        if (form.getLogger)
            this.context.logger = form.getLogger();
        this.context.item = null;
        this.context.id = null;
        this.$loader = form._loader;
    };
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.projc.modelc1.Modelc1Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.projc.modelc1.ClassC1"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.projc.modelc1.Modelc1Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassC1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projc.modelc1.ClassC1", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassC1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projc.modelc1.ClassC1", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.Modelc1Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projc.modelc1.ClassC1. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.ClassC1"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projc.modelc1.ClassC1");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRC1 = "attrC1";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRC1] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "false"
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRC1
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRC1
    ];

    theClass.getName = function() {
        return "com.crossproj.projc.modelc1.ClassC1";
    };


    Object.defineProperty(theClass.prototype, 'attrC1', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projc.modelc1.ClassC1").ATTR_ATTRC1);
        },
        set: function(attrC1) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projc.modelc1.ClassC1").ATTR_ATTRC1, attrC1);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projc.modelc1.ClassC1"]();
