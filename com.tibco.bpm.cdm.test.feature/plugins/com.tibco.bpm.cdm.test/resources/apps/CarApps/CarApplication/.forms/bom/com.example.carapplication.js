/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.carapplication.CarapplicationPackage.
 */

bpm.data.Loader.classDefiner["com.example.carapplication.CarapplicationPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.carapplication.CarapplicationPackage");

    // Define the enumerations declared by this package.
    theClass.States = ["DRIVING", "PARKED", "STOLEN", "SCRAPPED"];
    theClass.States.DRIVING = "DRIVING";
    theClass.States.PARKED = "PARKED";
    theClass.States.STOLEN = "STOLEN";
    theClass.States.SCRAPPED = "SCRAPPED";
    theClass.CustomerStates = ["ACTIVE", "INACTIVE"];
    theClass.CustomerStates.ACTIVE = "ACTIVE";
    theClass.CustomerStates.INACTIVE = "INACTIVE";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.carapplication.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.carapplication.CarapplicationPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.carapplication.CarapplicationFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.carapplication.CarapplicationFactory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.carapplication.CarapplicationFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.carapplication.Car",
        "com.example.carapplication.Customer",
        "com.example.carapplication.Address"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.carapplication.CarapplicationFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createCar = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Car", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateCar = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Car", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createCustomer = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Customer", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateCustomer = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Customer", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createAddress = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Address", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateAddress = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.carapplication.Address", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.carapplication.CarapplicationFactory"]();

/*
 * This provides an implementation of the BOM class com.example.carapplication.Customer. 
 */

bpm.data.Loader.classDefiner["com.example.carapplication.Customer"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.carapplication.Customer");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CUSTOMERID = "customerID";
    theClass.ATTR_STATE = "state";
    theClass.ATTR_FIRSTNAME = "firstName";
    theClass.ATTR_LASTNAME = "lastName";
    theClass.ATTR_AGE = "age";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_STATE] = {
        type: "com.example.carapplication.CustomerStates",
        baseType: "com.example.carapplication.CustomerStates",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_FIRSTNAME] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_LASTNAME] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_AGE] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 10,
        decimalPlaces: 0

    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERID,
        theClass.ATTR_STATE,
        theClass.ATTR_FIRSTNAME,
        theClass.ATTR_LASTNAME,
        theClass.ATTR_AGE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERID,
        theClass.ATTR_STATE,
        theClass.ATTR_FIRSTNAME,
        theClass.ATTR_LASTNAME,
        theClass.ATTR_AGE
    ];

    theClass.getName = function() {
        return "com.example.carapplication.Customer";
    };


    Object.defineProperty(theClass.prototype, 'customerID', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_CUSTOMERID);
        },
        set: function(customerID) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_CUSTOMERID, customerID);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'state', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_STATE);
        },
        set: function(state) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_STATE, state);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'firstName', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_FIRSTNAME);
        },
        set: function(firstName) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_FIRSTNAME, firstName);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'lastName', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_LASTNAME);
        },
        set: function(lastName) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_LASTNAME, lastName);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'age', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_AGE);
        },
        set: function(age) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Customer").ATTR_AGE, age);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.carapplication.Customer"]();

/*
 * This provides an implementation of the BOM class com.example.carapplication.Address. 
 */

bpm.data.Loader.classDefiner["com.example.carapplication.Address"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.carapplication.Address");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_FIRSTLINE = "firstLine";
    theClass.ATTR_SECONDLINE = "secondLine";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_FIRSTLINE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_SECONDLINE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_FIRSTLINE,
        theClass.ATTR_SECONDLINE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_FIRSTLINE,
        theClass.ATTR_SECONDLINE
    ];

    theClass.getName = function() {
        return "com.example.carapplication.Address";
    };


    Object.defineProperty(theClass.prototype, 'firstLine', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Address").ATTR_FIRSTLINE);
        },
        set: function(firstLine) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Address").ATTR_FIRSTLINE, firstLine);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'secondLine', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Address").ATTR_SECONDLINE);
        },
        set: function(secondLine) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Address").ATTR_SECONDLINE, secondLine);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.carapplication.Address"]();

/*
 * This provides an implementation of the BOM class com.example.carapplication.Car. 
 */

bpm.data.Loader.classDefiner["com.example.carapplication.Car"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.carapplication.Car");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_STATE = "state";
    theClass.ATTR_REGISTRATION = "registration";
    theClass.ATTR_MAKE = "make";
    theClass.ATTR_MODEL = "model";
    theClass.ATTR_NUMBEROFDOORS = "numberOfDoors";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_STATE] = {
        type: "com.example.carapplication.States",
        baseType: "com.example.carapplication.States",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_REGISTRATION] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_MAKE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_MODEL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBEROFDOORS] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_STATE,
        theClass.ATTR_REGISTRATION,
        theClass.ATTR_MAKE,
        theClass.ATTR_MODEL,
        theClass.ATTR_NUMBEROFDOORS
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_STATE,
        theClass.ATTR_REGISTRATION,
        theClass.ATTR_MAKE,
        theClass.ATTR_MODEL,
        theClass.ATTR_NUMBEROFDOORS
    ];

    theClass.getName = function() {
        return "com.example.carapplication.Car";
    };


    Object.defineProperty(theClass.prototype, 'state', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_STATE);
        },
        set: function(state) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_STATE, state);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'registration', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_REGISTRATION);
        },
        set: function(registration) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_REGISTRATION, registration);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'make', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_MAKE);
        },
        set: function(make) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_MAKE, make);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'model', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_MODEL);
        },
        set: function(model) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_MODEL, model);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberOfDoors', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_NUMBEROFDOORS);
        },
        set: function(numberOfDoors) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carapplication.Car").ATTR_NUMBEROFDOORS, numberOfDoors);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.carapplication.Car"]();
