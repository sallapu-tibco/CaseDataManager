/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.tibco.ace.datainit.test.TestPackage.
 */

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.TestPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    com.tibco.data.Loader.currentLoader.registerPackage(theClass, "com.tibco.ace.datainit.test.TestPackage");

};

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.TestPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.tibco.ace.datainit.test.TestFactory. 
 */ 

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.TestFactory"] = function() {
    /** Constructor. */
    var theClass = function(form) {
        this.context = new Object();
        this.context.form = form;
        if (form.getLogger)
            this.context.logger = form.getLogger();
        this.context.item = null;
        this.context.id = null;
        this.loader = form._loader;
    };
    com.tibco.data.Loader.currentLoader.registerFactory(theClass, "com.tibco.ace.datainit.test.TestFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.tibco.ace.datainit.test.Pet"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.tibco.ace.datainit.test.TestFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createPet = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datainit.test.Pet", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreatePet = function(jsonData) {
        var instance = this.loader.newInstance("com.tibco.ace.datainit.test.Pet", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.loader.getClass(className);
    };
};

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.TestFactory"]();

/*
 * This provides an implementation of the BOM class com.tibco.ace.datainit.test.Pet. 
 */

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.Pet"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = com.tibco.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.tibco.ace.datainit.test.Pet");
    com.tibco.data.Loader.extendClass(com.tibco.data.BomBase, theClass);

    theClass.ATTR_NAME = "name";
    theClass.ATTR_TYPE = "type";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_NAME] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TYPE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_NAME,
        theClass.ATTR_TYPE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_NAME,
        theClass.ATTR_TYPE
    ];

    theClass.getName = function() {
        return "com.tibco.ace.datainit.test.Pet";
    };


    Object.defineProperty(theClass.prototype, 'name', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datainit.test.Pet").ATTR_NAME);
        },
        set: function(name) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datainit.test.Pet").ATTR_NAME, name);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'type', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datainit.test.Pet").ATTR_TYPE);
        },
        set: function(type) {
            this._setPrimitiveAttribute(this.loader.getClass("com.tibco.ace.datainit.test.Pet").ATTR_TYPE, type);
        },
        enumerable: true
    });

};

com.tibco.data.Loader.classDefiner["com.tibco.ace.datainit.test.Pet"]();
