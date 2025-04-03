/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.projd.modeld1.Modeld1Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.Modeld1Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.projd.modeld1.Modeld1Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.projd.modeld1.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.Modeld1Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.projd.modeld1.Modeld1Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.Modeld1Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.projd.modeld1.Modeld1Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.projd.modeld1.ClassD1"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.projd.modeld1.Modeld1Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassD1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projd.modeld1.ClassD1", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassD1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projd.modeld1.ClassD1", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.Modeld1Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projd.modeld1.ClassD1. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.ClassD1"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projd.modeld1.ClassD1");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRD1 = "attrD1";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRD1] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "This is attrD1",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRD1
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRD1
    ];

    theClass.getName = function() {
        return "com.crossproj.projd.modeld1.ClassD1";
    };


    Object.defineProperty(theClass.prototype, 'attrD1', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projd.modeld1.ClassD1").ATTR_ATTRD1);
        },
        set: function(attrD1) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projd.modeld1.ClassD1").ATTR_ATTRD1, attrD1);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld1.ClassD1"]();
