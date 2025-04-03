/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.projb.modelb1.Modelb1Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.Modelb1Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.projb.modelb1.Modelb1Package");

};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.projb.modelb1.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.Modelb1Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.projb.modelb1.Modelb1Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.Modelb1Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.projb.modelb1.Modelb1Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.projb.modelb1.ClassB1"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.projb.modelb1.Modelb1Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassB1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb1.ClassB1", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassB1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb1.ClassB1", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.Modelb1Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projb.modelb1.ClassB1. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.ClassB1"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projb.modelb1.ClassB1");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRB1 = "attrB1";
    theClass.ATTR_C1 = "c1";
    theClass.ATTR_B2 = "b2";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRB1] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "This is attrB1",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_C1] = {
        type: "com.crossproj.projc.modelc1.ClassC1",
        baseType: "com.crossproj.projc.modelc1.ClassC1",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_B2] = {
        type: "com.crossproj.projb.modelb2.ClassB2",
        baseType: "com.crossproj.projb.modelb2.ClassB2",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRB1
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_C1,
        theClass.ATTR_B2
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRB1,
        theClass.ATTR_C1,
        theClass.ATTR_B2
    ];

    theClass.getName = function() {
        return "com.crossproj.projb.modelb1.ClassB1";
    };


    Object.defineProperty(theClass.prototype, 'attrB1', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1").ATTR_ATTRB1);
        },
        set: function(attrB1) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1").ATTR_ATTRB1, attrB1);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'c1', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1").ATTR_C1);
        },
        set: function(c1) {
            var classRef = this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1");
            var attrRef = classRef.ATTR_C1;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(c1 == null) || c1 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, c1);
            } else {
                throw "Wrong input object type for 'c1' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(c1) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'b2', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1").ATTR_B2);
        },
        set: function(b2) {
            var classRef = this.$loader.getClass("com.crossproj.projb.modelb1.ClassB1");
            var attrRef = classRef.ATTR_B2;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(b2 == null) || b2 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, b2);
            } else {
                throw "Wrong input object type for 'b2' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(b2) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb1.ClassB1"]();
