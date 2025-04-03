/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.proja.modela2.Modela2Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.Modela2Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.proja.modela2.Modela2Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.proja.modela2.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.Modela2Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.proja.modela2.Modela2Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.Modela2Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.proja.modela2.Modela2Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.proja.modela2.ClassA2"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.proja.modela2.Modela2Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassA2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela2.ClassA2", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassA2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela2.ClassA2", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.Modela2Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.proja.modela2.ClassA2. 
 */
bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.ClassA2"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.proja.modela2.ClassA2");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRA2 = "attrA2";
    theClass.ATTR_A3 = "a3";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRA2] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_A3] = {
        type: "com.crossproj.proja.modela3.ClassA3",
        baseType: "com.crossproj.proja.modela3.ClassA3",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRA2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_A3
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRA2,
        theClass.ATTR_A3
    ];

    theClass.getName = function() {
        return "com.crossproj.proja.modela2.ClassA2";
    };


    Object.defineProperty(theClass.prototype, 'attrA2', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela2.ClassA2").ATTR_ATTRA2);
        },
        set: function(attrA2) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela2.ClassA2").ATTR_ATTRA2, attrA2);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'a3', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.proja.modela2.ClassA2").ATTR_A3);
        },
        set: function(a3) {
            var classRef = this.$loader.getClass("com.crossproj.proja.modela2.ClassA2");
            var attrRef = classRef.ATTR_A3;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(a3 == null) || a3 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, a3);
            } else {
                throw "Wrong input object type for 'a3' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(a3) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela2.ClassA2"]();
