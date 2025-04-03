/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.proja.modela3.Modela3Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.Modela3Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.proja.modela3.Modela3Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.proja.modela3.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.Modela3Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.proja.modela3.Modela3Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.Modela3Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.proja.modela3.Modela3Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.proja.modela3.ClassA3"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.proja.modela3.Modela3Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassA3 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela3.ClassA3", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassA3 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela3.ClassA3", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.Modela3Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.proja.modela3.ClassA3. 
 */
bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.ClassA3"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.proja.modela3.ClassA3");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRA3 = "attrA3";
    theClass.ATTR_B1 = "b1";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRA3] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_B1] = {
        type: "com.crossproj.projb.modelb1.ClassB1",
        baseType: "com.crossproj.projb.modelb1.ClassB1",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRA3
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_B1
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRA3,
        theClass.ATTR_B1
    ];

    theClass.getName = function() {
        return "com.crossproj.proja.modela3.ClassA3";
    };


    Object.defineProperty(theClass.prototype, 'attrA3', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela3.ClassA3").ATTR_ATTRA3);
        },
        set: function(attrA3) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela3.ClassA3").ATTR_ATTRA3, attrA3);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'b1', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.proja.modela3.ClassA3").ATTR_B1);
        },
        set: function(b1) {
            var classRef = this.$loader.getClass("com.crossproj.proja.modela3.ClassA3");
            var attrRef = classRef.ATTR_B1;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(b1 == null) || b1 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, b1);
            } else {
                throw "Wrong input object type for 'b1' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(b1) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela3.ClassA3"]();
