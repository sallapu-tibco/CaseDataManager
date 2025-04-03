/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.proja.modela1.Modela1Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.Modela1Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.proja.modela1.Modela1Package");

    // Define the enumerations declared by this package.
    theClass.States = ["CASECREATED", "CASECOMPLETED"];
    theClass.States.CASECREATED = "CASECREATED";
    theClass.States.CASECOMPLETED = "CASECOMPLETED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.proja.modela1.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.Modela1Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.proja.modela1.Modela1Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.Modela1Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.proja.modela1.Modela1Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.proja.modela1.CaseA1"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.proja.modela1.Modela1Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createCaseA1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela1.CaseA1", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateCaseA1 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.proja.modela1.CaseA1", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.Modela1Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.proja.modela1.CaseA1. 
 */
bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.CaseA1"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.proja.modela1.CaseA1");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_CID = "cid";
    theClass.ATTR_STATE = "state";
    theClass.ATTR_A2 = "a2";
    theClass.ATTR_D1 = "d1";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_CID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_STATE] = {
        type: "com.crossproj.proja.modela1.States",
        baseType: "com.crossproj.proja.modela1.States",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_A2] = {
        type: "com.crossproj.proja.modela2.ClassA2",
        baseType: "com.crossproj.proja.modela2.ClassA2",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_D1] = {
        type: "com.crossproj.projd.modeld1.ClassD1",
        baseType: "com.crossproj.projd.modeld1.ClassD1",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CID,
        theClass.ATTR_STATE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_A2,
        theClass.ATTR_D1
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CID,
        theClass.ATTR_STATE,
        theClass.ATTR_A2,
        theClass.ATTR_D1
    ];

    theClass.getName = function() {
        return "com.crossproj.proja.modela1.CaseA1";
    };


    Object.defineProperty(theClass.prototype, 'cid', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_CID);
        },
        set: function(cid) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_CID, cid);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'state', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_STATE);
        },
        set: function(state) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_STATE, state);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'a2', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_A2);
        },
        set: function(a2) {
            var classRef = this.$loader.getClass("com.crossproj.proja.modela1.CaseA1");
            var attrRef = classRef.ATTR_A2;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(a2 == null) || a2 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, a2);
            } else {
                throw "Wrong input object type for 'a2' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(a2) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'd1', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.proja.modela1.CaseA1").ATTR_D1);
        },
        set: function(d1) {
            var classRef = this.$loader.getClass("com.crossproj.proja.modela1.CaseA1");
            var attrRef = classRef.ATTR_D1;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(d1 == null) || d1 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, d1);
            } else {
                throw "Wrong input object type for 'd1' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(d1) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.proja.modela1.CaseA1"]();
