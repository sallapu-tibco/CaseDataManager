/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.testdata_outside_global.Testdata_outside_globalPackage.
 */

bpm.data.Loader.classDefiner["com.example.testdata_outside_global.Testdata_outside_globalPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.testdata_outside_global.Testdata_outside_globalPackage");

    // Define the enumerations declared by this package.
    theClass.EmploymentStatus = ["SELFEMPLOYED", "FULLTIMESALARIED", "PARTTIMESALARIED", "RETIRED", "ONVACATION", "RESIGNED"];
    theClass.EmploymentStatus.SELFEMPLOYED = "SELFEMPLOYED";
    theClass.EmploymentStatus.FULLTIMESALARIED = "FULLTIMESALARIED";
    theClass.EmploymentStatus.PARTTIMESALARIED = "PARTTIMESALARIED";
    theClass.EmploymentStatus.RETIRED = "RETIRED";
    theClass.EmploymentStatus.ONVACATION = "ONVACATION";
    theClass.EmploymentStatus.RESIGNED = "RESIGNED";
    theClass.AccountType = ["SAVINGS", "CURRENT", "FIXEDDEPOSIT", "INVESTMENT"];
    theClass.AccountType.SAVINGS = "SAVINGS";
    theClass.AccountType.CURRENT = "CURRENT";
    theClass.AccountType.FIXEDDEPOSIT = "FIXEDDEPOSIT";
    theClass.AccountType.INVESTMENT = "INVESTMENT";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.testdata_outside_global.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.testdata_outside_global.Testdata_outside_globalPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.testdata_outside_global.Testdata_outside_globalFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.testdata_outside_global.Testdata_outside_globalFactory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.testdata_outside_global.Testdata_outside_globalFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.testdata_outside_global.EmploymentDetails"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.testdata_outside_global.Testdata_outside_globalFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createEmploymentDetails = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside_global.EmploymentDetails", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateEmploymentDetails = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testdata_outside_global.EmploymentDetails", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.testdata_outside_global.Testdata_outside_globalFactory"]();

/*
 * This provides an implementation of the BOM class com.example.testdata_outside_global.EmploymentDetails. 
 */

bpm.data.Loader.classDefiner["com.example.testdata_outside_global.EmploymentDetails"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testdata_outside_global.EmploymentDetails");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_NUMBEROFYEARS = "numberofyears";
    theClass.ATTR_YEARLYRENUMERATION = "yearlyRenumeration";
    theClass.ATTR_EMPLOYMENTSTATUS = "employmentStatus";
    theClass.ATTR_EMPLOYEEID = "employeeID";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBEROFYEARS] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "2",
        length: 2,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_YEARLYRENUMERATION] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "15000.60"
    };
    theClass.TYPE_ARRAY[theClass.ATTR_EMPLOYMENTSTATUS] = {
        type: "com.example.testdata_outside_global.EmploymentStatus",
        baseType: "com.example.testdata_outside_global.EmploymentStatus",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_EMPLOYEEID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "DEFAULT EMP ID",
        length: 50
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_NUMBEROFYEARS,
        theClass.ATTR_YEARLYRENUMERATION,
        theClass.ATTR_EMPLOYMENTSTATUS,
        theClass.ATTR_EMPLOYEEID
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_NUMBEROFYEARS,
        theClass.ATTR_YEARLYRENUMERATION,
        theClass.ATTR_EMPLOYMENTSTATUS,
        theClass.ATTR_EMPLOYEEID
    ];

    theClass.getName = function() {
        return "com.example.testdata_outside_global.EmploymentDetails";
    };


    Object.defineProperty(theClass.prototype, 'numberofyears', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_NUMBEROFYEARS);
        },
        set: function(numberofyears) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_NUMBEROFYEARS, numberofyears);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'yearlyRenumeration', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_YEARLYRENUMERATION);
        },
        set: function(yearlyRenumeration) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_YEARLYRENUMERATION, yearlyRenumeration);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'employmentStatus', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_EMPLOYMENTSTATUS);
        },
        set: function(employmentStatus) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_EMPLOYMENTSTATUS, employmentStatus);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'employeeID', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_EMPLOYEEID);
        },
        set: function(employeeID) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testdata_outside_global.EmploymentDetails").ATTR_EMPLOYEEID, employeeID);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testdata_outside_global.EmploymentDetails"]();
