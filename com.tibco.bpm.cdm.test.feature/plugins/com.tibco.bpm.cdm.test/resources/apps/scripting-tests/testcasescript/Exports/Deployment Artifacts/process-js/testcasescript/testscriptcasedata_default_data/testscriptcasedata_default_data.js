/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_default_data. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_default_data"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_default_data");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ACCOUNTBOM = "accountBOM";
    theClass.ATTR_CUSTOMERBOM = "customerBOM";
    theClass.ATTR_ACCOUNTCASE = "accountCase";
    theClass.ATTR_CUSTOMERCASE = "customerCase";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOM] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERBOM] = {
        type: "com.example.testcasedatascripting.Customer",
        baseType: "com.example.testcasedatascripting.Customer",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERCASE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_CUSTOMERCASE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_CUSTOMERBOM
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_CUSTOMERBOM,
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_CUSTOMERCASE
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_default_data";
    };


    Object.defineProperty(theClass.prototype, 'accountBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_ACCOUNTBOM);
        },
        set: function(accountBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data");
            var attrRef = classRef.ATTR_ACCOUNTBOM;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOM == null) || accountBOM instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOM);
            } else {
                throw "Wrong input object type for 'accountBOM' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOM) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_CUSTOMERBOM);
        },
        set: function(customerBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data");
            var attrRef = classRef.ATTR_CUSTOMERBOM;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(customerBOM == null) || customerBOM instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, customerBOM);
            } else {
                throw "Wrong input object type for 'customerBOM' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(customerBOM) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_ACCOUNTCASE);
        },
        set: function(accountCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_ACCOUNTCASE, accountCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_CUSTOMERCASE);
        },
        set: function(customerCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_default_data").ATTR_CUSTOMERCASE, customerCase);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_default_data"]();
