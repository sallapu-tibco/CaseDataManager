/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.crossproj.projb.modelb2.Modelb2Package.
 */

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.Modelb2Package"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.crossproj.projb.modelb2.Modelb2Package");

    // Define the enumerations declared by this package.
    theClass.Mood = ["EXCITED", "BORED"];
    theClass.Mood.EXCITED = "EXCITED";
    theClass.Mood.BORED = "BORED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.crossproj.projb.modelb2.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.Modelb2Package"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.crossproj.projb.modelb2.Modelb2Factory. 
 */ 

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.Modelb2Factory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.crossproj.projb.modelb2.Modelb2Factory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.crossproj.projb.modelb2.ClassB2",
        "com.crossproj.projb.modelb2.CaseB2"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.crossproj.projb.modelb2.Modelb2Factory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createClassB2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb2.ClassB2", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateClassB2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb2.ClassB2", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createCaseB2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb2.CaseB2", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateCaseB2 = function(jsonData) {
        var instance = this.$loader.newInstance("com.crossproj.projb.modelb2.CaseB2", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.Modelb2Factory"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projb.modelb2.CaseB2. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.CaseB2"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projb.modelb2.CaseB2");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_CID = "cid";
    theClass.ATTR_STATE = "state";
    theClass.ATTR_CLASSB2 = "classb2";

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
        type: "com.crossproj.projb.modelb2.Mood",
        baseType: "com.crossproj.projb.modelb2.Mood",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CLASSB2] = {
        type: "com.crossproj.projb.modelb2.ClassB2",
        baseType: "com.crossproj.projb.modelb2.ClassB2",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CID,
        theClass.ATTR_STATE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CLASSB2
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CID,
        theClass.ATTR_STATE,
        theClass.ATTR_CLASSB2
    ];

    theClass.getName = function() {
        return "com.crossproj.projb.modelb2.CaseB2";
    };


    Object.defineProperty(theClass.prototype, 'cid', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2").ATTR_CID);
        },
        set: function(cid) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2").ATTR_CID, cid);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'state', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2").ATTR_STATE);
        },
        set: function(state) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2").ATTR_STATE, state);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'classb2', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2").ATTR_CLASSB2);
        },
        set: function(classb2) {
            var classRef = this.$loader.getClass("com.crossproj.projb.modelb2.CaseB2");
            var attrRef = classRef.ATTR_CLASSB2;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(classb2 == null) || classb2 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, classb2);
            } else {
                throw "Wrong input object type for 'classb2' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(classb2) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.CaseB2"]();
/*
 * This provides an implementation of the BOM class com.crossproj.projb.modelb2.ClassB2. 
 */
bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.ClassB2"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.crossproj.projb.modelb2.ClassB2");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ATTRB2 = "attrB2";
    theClass.ATTR_D2 = "d2";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRB2] = {
        type: "BomPrimitiveTypes.URI",
        baseType: "BomPrimitiveTypes.URI",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "https://www.attrB2.co.in"
    };
    theClass.TYPE_ARRAY[theClass.ATTR_D2] = {
        type: "com.crossproj.projd.modeld2.ClassD2",
        baseType: "com.crossproj.projd.modeld2.ClassD2",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRB2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_D2
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRB2,
        theClass.ATTR_D2
    ];

    theClass.getName = function() {
        return "com.crossproj.projb.modelb2.ClassB2";
    };


    Object.defineProperty(theClass.prototype, 'attrB2', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.ClassB2").ATTR_ATTRB2);
        },
        set: function(attrB2) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.ClassB2").ATTR_ATTRB2, attrB2);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'd2', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.crossproj.projb.modelb2.ClassB2").ATTR_D2);
        },
        set: function(d2) {
            var classRef = this.$loader.getClass("com.crossproj.projb.modelb2.ClassB2");
            var attrRef = classRef.ATTR_D2;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(d2 == null) || d2 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, d2);
            } else {
                throw "Wrong input object type for 'd2' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(d2) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.crossproj.projb.modelb2.ClassB2"]();
