/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ACCOUNTBOM = "accountBOM";
    theClass.ATTR_ACCOUNTCASE = "accountCase";
    theClass.ATTR_ACCOUNTBOMREAD1 = "accountBOMRead1";
    theClass.ATTR_ACCOUNTCASEUPDATE1 = "accountCaseUpdate1";
    theClass.ATTR_ACCOUNTBOMREAD2 = "accountBOMRead2";
    theClass.ATTR_ACCOUNTCASEUPDATE2 = "accountCaseUpdate2";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOM] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMREAD1] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEUPDATE1] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMREAD2] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEUPDATE2] = {
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
        theClass.ATTR_ACCOUNTCASEUPDATE1,
        theClass.ATTR_ACCOUNTCASEUPDATE2
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_ACCOUNTBOMREAD1,
        theClass.ATTR_ACCOUNTBOMREAD2
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_ACCOUNTBOMREAD1,
        theClass.ATTR_ACCOUNTCASEUPDATE1,
        theClass.ATTR_ACCOUNTBOMREAD2,
        theClass.ATTR_ACCOUNTCASEUPDATE2
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync";
    };


    Object.defineProperty(theClass.prototype, 'accountBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTBOM);
        },
        set: function(accountBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync");
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


    Object.defineProperty(theClass.prototype, 'accountCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASE);
        },
        set: function(accountCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASE, accountCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMRead1', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTBOMREAD1);
        },
        set: function(accountBOMRead1) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync");
            var attrRef = classRef.ATTR_ACCOUNTBOMREAD1;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMRead1 == null) || accountBOMRead1 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMRead1);
            } else {
                throw "Wrong input object type for 'accountBOMRead1' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMRead1) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseUpdate1', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASEUPDATE1);
        },
        set: function(accountCaseUpdate1) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASEUPDATE1, accountCaseUpdate1);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMRead2', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTBOMREAD2);
        },
        set: function(accountBOMRead2) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync");
            var attrRef = classRef.ATTR_ACCOUNTBOMREAD2;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMRead2 == null) || accountBOMRead2 instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMRead2);
            } else {
                throw "Wrong input object type for 'accountBOMRead2' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMRead2) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseUpdate2', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASEUPDATE2);
        },
        set: function(accountCaseUpdate2) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync").ATTR_ACCOUNTCASEUPDATE2, accountCaseUpdate2);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_case_out_of_sync"]();
