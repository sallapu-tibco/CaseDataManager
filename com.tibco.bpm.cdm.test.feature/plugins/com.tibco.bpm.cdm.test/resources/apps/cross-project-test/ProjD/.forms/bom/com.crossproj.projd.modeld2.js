/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.projd.modeld2.Modeld2Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.Modeld2Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.projd.modeld2.Modeld2Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.projd.modeld2.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.Modeld2Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.projd.modeld2.Modeld2Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.Modeld2Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.projd.modeld2.Modeld2Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.projd.modeld2.ClassD2"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.projd.modeld2.Modeld2Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassD2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projd.modeld2.ClassD2", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassD2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projd.modeld2.ClassD2", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.Modeld2Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projd.modeld2.ClassD2. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.ClassD2"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projd.modeld2.ClassD2");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRD2 = "attrD2";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRD2] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "This is attrD2",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRD2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRD2
    ];

    theClass.getName = function() {
        return "com.crossproj.projd.modeld2.ClassD2";
    };


    Object.defineProperty(theClass.prototype, 'attrD2', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projd.modeld2.ClassD2").ATTR_ATTRD2);
        },
        set: function(attrD2) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projd.modeld2.ClassD2").ATTR_ATTRD2, attrD2);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projd.modeld2.ClassD2"]();
