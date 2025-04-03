/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.testcasedatascripting.TestcasedatascriptingPackage.
 */

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.TestcasedatascriptingPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.testcasedatascripting.TestcasedatascriptingPackage");

    // Define the enumerations declared by this package.
    theClass.CustomerCategory = ["HIGHACTIVITY", "MEDIUMACTIVITY", "LOWACTIVITY", "TERMINATED"];
    theClass.CustomerCategory.HIGHACTIVITY = "HIGHACTIVITY";
    theClass.CustomerCategory.MEDIUMACTIVITY = "MEDIUMACTIVITY";
    theClass.CustomerCategory.LOWACTIVITY = "LOWACTIVITY";
    theClass.CustomerCategory.TERMINATED = "TERMINATED";
    theClass.AccountStatus = ["ACTIVE", "DORMANT", "TOBETERMINATED", "BARRED"];
    theClass.AccountStatus.ACTIVE = "ACTIVE";
    theClass.AccountStatus.DORMANT = "DORMANT";
    theClass.AccountStatus.TOBETERMINATED = "TOBETERMINATED";
    theClass.AccountStatus.BARRED = "BARRED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.testcasedatascripting.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.testcasedatascripting.TestcasedatascriptingPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.testcasedatascripting.TestcasedatascriptingFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.TestcasedatascriptingFactory"] = function() {
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
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.testcasedatascripting.TestcasedatascriptingFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.testcasedatascripting.Customer",
        "com.example.testcasedatascripting.Account",
        "com.example.testcasedatascripting.Address"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.testcasedatascripting.TestcasedatascriptingFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createCustomer = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Customer", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateCustomer = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Customer", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createAccount = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Account", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateAccount = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Account", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createAddress = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Address", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateAddress = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.testcasedatascripting.Address", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.TestcasedatascriptingFactory"]();

/*
 * This provides an implementation of the BOM class com.example.testcasedatascripting.Address. 
 */

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Address"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasedatascripting.Address");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_FIRSTLINE = "firstLine";
    theClass.ATTR_SECONDLINE = "secondLine";
    theClass.ATTR_CITY = "city";
    theClass.ATTR_COUNTY = "county";
    theClass.ATTR_COUNTRY = "country";
    theClass.ATTR_POSTCODE = "postcode";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_FIRSTLINE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default First Line",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_SECONDLINE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default Second Line",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CITY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default City",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_COUNTY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default County",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_COUNTRY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default Country",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_POSTCODE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "DEFAULT",
        length: 10
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_FIRSTLINE,
        theClass.ATTR_SECONDLINE,
        theClass.ATTR_CITY,
        theClass.ATTR_COUNTY,
        theClass.ATTR_COUNTRY,
        theClass.ATTR_POSTCODE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_FIRSTLINE,
        theClass.ATTR_SECONDLINE,
        theClass.ATTR_CITY,
        theClass.ATTR_COUNTY,
        theClass.ATTR_COUNTRY,
        theClass.ATTR_POSTCODE
    ];

    theClass.getName = function() {
        return "com.example.testcasedatascripting.Address";
    };


    Object.defineProperty(theClass.prototype, 'firstLine', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_FIRSTLINE);
        },
        set: function(firstLine) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_FIRSTLINE, firstLine);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'secondLine', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_SECONDLINE);
        },
        set: function(secondLine) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_SECONDLINE, secondLine);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'city', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_CITY);
        },
        set: function(city) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_CITY, city);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'county', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_COUNTY);
        },
        set: function(county) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_COUNTY, county);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'country', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_COUNTRY);
        },
        set: function(country) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_COUNTRY, country);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'postcode', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_POSTCODE);
        },
        set: function(postcode) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Address").ATTR_POSTCODE, postcode);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Address"]();

/*
 * This provides an implementation of the BOM class com.example.testcasedatascripting.Account. 
 */

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Account"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasedatascripting.Account");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_ACCOUNTID = "accountId";
    theClass.ATTR_ACCOUNTSTATUS = "accountStatus";
    theClass.ATTR_ACCOUNTOPENED = "accountOpened";
    theClass.ATTR_ACCOUNTLASTACCESSED = "accountLastAccessed";
    theClass.ATTR_ACCOUNTBALANCE = "accountBalance";
    theClass.ATTR_ACCOUNTINSTITUTION = "accountInstitution";
    theClass.ATTR_INSTITUTIONADDRESS = "institutionAddress";
    theClass.ATTR_ACCOUNTTYPE = "accountType";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTSTATUS] = {
        type: "com.example.testcasedatascripting.AccountStatus",
        baseType: "com.example.testcasedatascripting.AccountStatus",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTOPENED] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTLASTACCESSED] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBALANCE] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "3000"
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTINSTITUTION] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "Default Institution",
        length: 100
    };
    theClass.TYPE_ARRAY[theClass.ATTR_INSTITUTIONADDRESS] = {
        type: "com.example.testcasedatascripting.Address",
        baseType: "com.example.testcasedatascripting.Address",
        primitive: false,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTTYPE] = {
        type: "com.example.testdata_outside_global.AccountType",
        baseType: "com.example.testdata_outside_global.AccountType",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTID,
        theClass.ATTR_ACCOUNTSTATUS,
        theClass.ATTR_ACCOUNTOPENED,
        theClass.ATTR_ACCOUNTLASTACCESSED,
        theClass.ATTR_ACCOUNTBALANCE,
        theClass.ATTR_ACCOUNTINSTITUTION,
        theClass.ATTR_ACCOUNTTYPE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_INSTITUTIONADDRESS
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTID,
        theClass.ATTR_ACCOUNTSTATUS,
        theClass.ATTR_ACCOUNTOPENED,
        theClass.ATTR_ACCOUNTLASTACCESSED,
        theClass.ATTR_ACCOUNTBALANCE,
        theClass.ATTR_ACCOUNTINSTITUTION,
        theClass.ATTR_INSTITUTIONADDRESS,
        theClass.ATTR_ACCOUNTTYPE
    ];

    theClass.getName = function() {
        return "com.example.testcasedatascripting.Account";
    };


    Object.defineProperty(theClass.prototype, 'accountId', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTID);
        },
        set: function(accountId) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTID, accountId);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountStatus', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTSTATUS);
        },
        set: function(accountStatus) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTSTATUS, accountStatus);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountOpened', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTOPENED);
        },
        set: function(accountOpened) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTOPENED, accountOpened);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountLastAccessed', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTLASTACCESSED);
        },
        set: function(accountLastAccessed) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTLASTACCESSED, accountLastAccessed);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBalance', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTBALANCE);
        },
        set: function(accountBalance) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTBALANCE, accountBalance);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountInstitution', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTINSTITUTION);
        },
        set: function(accountInstitution) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTINSTITUTION, accountInstitution);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'institutionAddress', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_INSTITUTIONADDRESS);
        },
        set: function(institutionAddress) {
            var classRef = this.$loader.getClass("com.example.testcasedatascripting.Account");
            var attrRef = classRef.ATTR_INSTITUTIONADDRESS;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(institutionAddress == null) || institutionAddress instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, institutionAddress);
            } else {
                throw "Wrong input object type for 'institutionAddress' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(institutionAddress) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountType', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTTYPE);
        },
        set: function(accountType) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Account").ATTR_ACCOUNTTYPE, accountType);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Account"]();

/*
 * This provides an implementation of the BOM class com.example.testcasedatascripting.Customer. 
 */

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Customer"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasedatascripting.Customer");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CUSTOMERID = "customerID";
    theClass.ATTR_CUSTOMERCATEGORY = "customerCategory";
    theClass.ATTR_DATEOFBIRTH = "dateofBirth";
    theClass.ATTR_AGE = "age";
    theClass.ATTR_TOTALINCOME = "totalIncome";
    theClass.ATTR_RESIDENTIALADDRESS = "residentialAddress";
    theClass.ATTR_EMPLOYMENTDETAILS = "employmentDetails";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "Customer01",
        length: 10
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERCATEGORY] = {
        type: "com.example.testcasedatascripting.CustomerCategory",
        baseType: "com.example.testcasedatascripting.CustomerCategory",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEOFBIRTH] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_AGE] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "26.25",
        length: 5,
        decimalPlaces: 2

    };
    theClass.TYPE_ARRAY[theClass.ATTR_TOTALINCOME] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "40000"
    };
    theClass.TYPE_ARRAY[theClass.ATTR_RESIDENTIALADDRESS] = {
        type: "com.example.testcasedatascripting.Address",
        baseType: "com.example.testcasedatascripting.Address",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_EMPLOYMENTDETAILS] = {
        type: "com.example.testdata_outside_global.EmploymentDetails",
        baseType: "com.example.testdata_outside_global.EmploymentDetails",
        primitive: false,
        multivalued: true,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERID,
        theClass.ATTR_CUSTOMERCATEGORY,
        theClass.ATTR_DATEOFBIRTH,
        theClass.ATTR_AGE,
        theClass.ATTR_TOTALINCOME
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_RESIDENTIALADDRESS,
        theClass.ATTR_EMPLOYMENTDETAILS
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERID,
        theClass.ATTR_CUSTOMERCATEGORY,
        theClass.ATTR_DATEOFBIRTH,
        theClass.ATTR_AGE,
        theClass.ATTR_TOTALINCOME,
        theClass.ATTR_RESIDENTIALADDRESS,
        theClass.ATTR_EMPLOYMENTDETAILS
    ];

    theClass.getName = function() {
        return "com.example.testcasedatascripting.Customer";
    };


    Object.defineProperty(theClass.prototype, 'customerID', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_CUSTOMERID);
        },
        set: function(customerID) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_CUSTOMERID, customerID);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerCategory', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_CUSTOMERCATEGORY);
        },
        set: function(customerCategory) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_CUSTOMERCATEGORY, customerCategory);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateofBirth', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_DATEOFBIRTH);
        },
        set: function(dateofBirth) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_DATEOFBIRTH, dateofBirth);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'age', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_AGE);
        },
        set: function(age) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_AGE, age);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'totalIncome', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_TOTALINCOME);
        },
        set: function(totalIncome) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_TOTALINCOME, totalIncome);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'residentialAddress', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_RESIDENTIALADDRESS);
        },
        set: function(residentialAddress) {
            var classRef = this.$loader.getClass("com.example.testcasedatascripting.Customer");
            var attrRef = classRef.ATTR_RESIDENTIALADDRESS;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(residentialAddress == null) || residentialAddress instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, residentialAddress);
            } else {
                throw "Wrong input object type for 'residentialAddress' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(residentialAddress) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'employmentDetails', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasedatascripting.Customer").ATTR_EMPLOYMENTDETAILS);
        },
        set: function(employmentDetails) {
            throw "Cannot re-assign multi-valued attribute 'employmentDetails' in the type 'com.example.testcasedatascripting.Customer'";
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasedatascripting.Customer"]();
