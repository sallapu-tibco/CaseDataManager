/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_CUSTOMERBOM = "customerBOM";
    theClass.ATTR_CUSTOMERCASE = "customerCase";
    theClass.ATTR_CUSTOMERREAD1 = "customerRead1";
    theClass.ATTR_CUSTOMERBOMDUPLICATE = "customerBOMDuplicate";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERBOM] = {
        type: "com.example.testcasedatascripting.Customer",
        baseType: "com.example.testcasedatascripting.Customer",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
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
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERREAD1] = {
        type: "com.example.testcasedatascripting.Customer",
        baseType: "com.example.testcasedatascripting.Customer",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERBOMDUPLICATE] = {
        type: "com.example.testcasedatascripting.Customer",
        baseType: "com.example.testcasedatascripting.Customer",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERCASE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERBOM,
        theClass.ATTR_CUSTOMERREAD1,
        theClass.ATTR_CUSTOMERBOMDUPLICATE
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CUSTOMERBOM,
        theClass.ATTR_CUSTOMERCASE,
        theClass.ATTR_CUSTOMERREAD1,
        theClass.ATTR_CUSTOMERBOMDUPLICATE
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid";
    };


    Object.defineProperty(theClass.prototype, 'customerBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid").ATTR_CUSTOMERBOM);
        },
        set: function(customerBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid");
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


    Object.defineProperty(theClass.prototype, 'customerCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid").ATTR_CUSTOMERCASE);
        },
        set: function(customerCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid").ATTR_CUSTOMERCASE, customerCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerRead1', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid").ATTR_CUSTOMERREAD1);
        },
        set: function(customerRead1) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid");
            var attrRef = classRef.ATTR_CUSTOMERREAD1;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(customerRead1 == null) || customerRead1 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, customerRead1);
            } else {
                throw "Wrong input object type for 'customerRead1' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(customerRead1) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerBOMDuplicate', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid").ATTR_CUSTOMERBOMDUPLICATE);
        },
        set: function(customerBOMDuplicate) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid");
            var attrRef = classRef.ATTR_CUSTOMERBOMDUPLICATE;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(customerBOMDuplicate == null) || customerBOMDuplicate instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, customerBOMDuplicate);
            } else {
                throw "Wrong input object type for 'customerBOMDuplicate' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(customerBOMDuplicate) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_non_unique_cid"]();
