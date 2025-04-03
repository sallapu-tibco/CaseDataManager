
/*
 * This provides an implementation of the BOM class com.example.carprocess.CarProcess.CarProcessProcess. 
 */

bpm.data.Loader.classDefiner["com.example.carprocess.CarProcess.CarProcessProcess"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.carprocess.CarProcess.CarProcessProcess");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CARREF = "carRef";
    theClass.ATTR_CAR = "car";
    theClass.ATTR_CARREFS = "carRefs";
    theClass.ATTR_CARS = "cars";
    theClass.ATTR_CUSTOMER = "customer";
    theClass.ATTR_CUSTOMERREF = "customerRef";
    theClass.ATTR_INTERSTESTEDPARTIES = "interstestedParties";
    theClass.ATTR_FIELD = "Field";
    theClass.ATTR_ACCOUNT = "account";
    theClass.ATTR_ACCOUNTREF = "accountRef";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CARREF] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CAR] = {
        type: "com.example.carapplication.Car",
        baseType: "com.example.carapplication.Car",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CARREFS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CARS] = {
        type: "com.example.carapplication.Car",
        baseType: "com.example.carapplication.Car",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERREF] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_INTERSTESTEDPARTIES] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_FIELD] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNT] = {
        type: "com.example.account.Account",
        baseType: "com.example.account.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTREF] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CARREF,
        theClass.ATTR_CARREFS,
        theClass.ATTR_CUSTOMER,
        theClass.ATTR_CUSTOMERREF,
        theClass.ATTR_INTERSTESTEDPARTIES,
        theClass.ATTR_FIELD,
        theClass.ATTR_ACCOUNTREF
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CAR,
        theClass.ATTR_CARS,
        theClass.ATTR_ACCOUNT
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CARREF,
        theClass.ATTR_CAR,
        theClass.ATTR_CARREFS,
        theClass.ATTR_CARS,
        theClass.ATTR_CUSTOMER,
        theClass.ATTR_CUSTOMERREF,
        theClass.ATTR_INTERSTESTEDPARTIES,
        theClass.ATTR_FIELD,
        theClass.ATTR_ACCOUNT,
        theClass.ATTR_ACCOUNTREF
    ];

    theClass.getName = function() {
        return "com.example.carprocess.CarProcess.CarProcessProcess";
    };


    Object.defineProperty(theClass.prototype, 'carRef', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CARREF);
        },
        set: function(carRef) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CARREF, carRef);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'car', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CAR);
        },
        set: function(car) {
            var classRef = this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess");
            var attrRef = classRef.ATTR_CAR;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(car == null) || car instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, car);
            } else {
                throw "Wrong input object type for 'car' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(car) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'carRefs', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CARREFS);
        },
        set: function(carRefs) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CARREFS, carRefs);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'cars', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CARS);
        },
        set: function(cars) {
            throw "Cannot re-assign multi-valued attribute 'cars' in the type 'com.example.carprocess.CarProcess.CarProcessProcess'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customer', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CUSTOMER);
        },
        set: function(customer) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CUSTOMER, customer);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerRef', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CUSTOMERREF);
        },
        set: function(customerRef) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_CUSTOMERREF, customerRef);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'interstestedParties', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_INTERSTESTEDPARTIES);
        },
        set: function(interstestedParties) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_INTERSTESTEDPARTIES, interstestedParties);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'Field', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_FIELD);
        },
        set: function(Field) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_FIELD, Field);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'account', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_ACCOUNT);
        },
        set: function(account) {
            var classRef = this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess");
            var attrRef = classRef.ATTR_ACCOUNT;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(account == null) || account instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, account);
            } else {
                throw "Wrong input object type for 'account' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(account) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountRef', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_ACCOUNTREF);
        },
        set: function(accountRef) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.CarProcessProcess").ATTR_ACCOUNTREF, accountRef);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.carprocess.CarProcess.CarProcessProcess"]();
