/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ACCOUNTCASESSEARCH = "accountCasesSearch";
    theClass.ATTR_ACCOUNTCASESFINDALL = "accountCasesFindAll";
    theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA = "accountCasesFindByMultiCriteria";
    theClass.ATTR_ACCOUNTBOMS = "accountBOMs";
    theClass.ATTR_ACCOUNTBOMREAD = "accountBOMRead";
    theClass.ATTR_ACCOUNTCASES = "accountCases";
    theClass.ATTR_ACCOUNTBOMSREAD = "accountBOMsRead";
    theClass.ATTR_ACCOUNTCASE = "accountCase";
    theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA = "accountCasesFindByCriteria";
    theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED = "accountCasesFindByCriteriaPaginated";
    theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED = "accountCasesSearchPaginated";
    theClass.ATTR_ACCOUNTBOM = "accountBOM";
    theClass.ATTR_CUSTOMERBOM = "customerBOM";
    theClass.ATTR_CUSTOMERCASE = "customerCase";
    theClass.ATTR_NAVIGATEDALLACCOUNTS = "navigatedAllAccounts";
    theClass.ATTR_NAVIGATEDACCOUNTSBYCRITERIA = "navigatedAccountsByCriteria";
    theClass.ATTR_NAVIGATEDACCOUNTSBYSEARCH = "navigatedAccountsBySearch";
    theClass.ATTR_ACCOUNTCASESSUBSET = "accountCasesSubset";
    theClass.ATTR_NAVIGATEUNLINKEDCUSTOMER = "navigateUnlinkedCustomer";
    theClass.ATTR_NAVIGATEUNLINKEDACCOUNTS = "navigateUnlinkedAccounts";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESSEARCH] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDALL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMS] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMREAD] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASES] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSREAD] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
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
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERCASE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NAVIGATEDALLACCOUNTS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NAVIGATEDACCOUNTSBYCRITERIA] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NAVIGATEDACCOUNTSBYSEARCH] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESSUBSET] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NAVIGATEUNLINKEDCUSTOMER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NAVIGATEUNLINKEDACCOUNTS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTCASESSEARCH,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA,
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED,
        theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED,
        theClass.ATTR_CUSTOMERCASE,
        theClass.ATTR_NAVIGATEDALLACCOUNTS,
        theClass.ATTR_NAVIGATEDACCOUNTSBYCRITERIA,
        theClass.ATTR_NAVIGATEDACCOUNTSBYSEARCH,
        theClass.ATTR_ACCOUNTCASESSUBSET,
        theClass.ATTR_NAVIGATEUNLINKEDCUSTOMER,
        theClass.ATTR_NAVIGATEUNLINKEDACCOUNTS
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_ACCOUNTBOMREAD,
        theClass.ATTR_ACCOUNTBOMSREAD,
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_CUSTOMERBOM
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTCASESSEARCH,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA,
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_ACCOUNTBOMREAD,
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTBOMSREAD,
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED,
        theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED,
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_CUSTOMERBOM,
        theClass.ATTR_CUSTOMERCASE,
        theClass.ATTR_NAVIGATEDALLACCOUNTS,
        theClass.ATTR_NAVIGATEDACCOUNTSBYCRITERIA,
        theClass.ATTR_NAVIGATEDACCOUNTSBYSEARCH,
        theClass.ATTR_ACCOUNTCASESSUBSET,
        theClass.ATTR_NAVIGATEUNLINKEDCUSTOMER,
        theClass.ATTR_NAVIGATEUNLINKEDACCOUNTS
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink";
    };


    Object.defineProperty(theClass.prototype, 'accountCasesSearch', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSEARCH);
        },
        set: function(accountCasesSearch) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSEARCH, accountCasesSearch);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindAll', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDALL);
        },
        set: function(accountCasesFindAll) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDALL, accountCasesFindAll);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByMultiCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYMULTICRITERIA);
        },
        set: function(accountCasesFindByMultiCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYMULTICRITERIA, accountCasesFindByMultiCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMs', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTBOMS);
        },
        set: function(accountBOMs) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMs' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMRead', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTBOMREAD);
        },
        set: function(accountBOMRead) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink");
            var attrRef = classRef.ATTR_ACCOUNTBOMREAD;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMRead == null) || accountBOMRead instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMRead);
            } else {
                throw "Wrong input object type for 'accountBOMRead' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMRead) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCases', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASES);
        },
        set: function(accountCases) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASES, accountCases);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMsRead', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTBOMSREAD);
        },
        set: function(accountBOMsRead) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsRead' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASE);
        },
        set: function(accountCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASE, accountCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYCRITERIA);
        },
        set: function(accountCasesFindByCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYCRITERIA, accountCasesFindByCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByCriteriaPaginated', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED);
        },
        set: function(accountCasesFindByCriteriaPaginated) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED, accountCasesFindByCriteriaPaginated);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesSearchPaginated', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSEARCHPAGINATED);
        },
        set: function(accountCasesSearchPaginated) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSEARCHPAGINATED, accountCasesSearchPaginated);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTBOM);
        },
        set: function(accountBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink");
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
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_CUSTOMERBOM);
        },
        set: function(customerBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink");
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
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_CUSTOMERCASE);
        },
        set: function(customerCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_CUSTOMERCASE, customerCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'navigatedAllAccounts', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDALLACCOUNTS);
        },
        set: function(navigatedAllAccounts) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDALLACCOUNTS, navigatedAllAccounts);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'navigatedAccountsByCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDACCOUNTSBYCRITERIA);
        },
        set: function(navigatedAccountsByCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDACCOUNTSBYCRITERIA, navigatedAccountsByCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'navigatedAccountsBySearch', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDACCOUNTSBYSEARCH);
        },
        set: function(navigatedAccountsBySearch) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEDACCOUNTSBYSEARCH, navigatedAccountsBySearch);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesSubset', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSUBSET);
        },
        set: function(accountCasesSubset) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_ACCOUNTCASESSUBSET, accountCasesSubset);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'navigateUnlinkedCustomer', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEUNLINKEDCUSTOMER);
        },
        set: function(navigateUnlinkedCustomer) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEUNLINKEDCUSTOMER, navigateUnlinkedCustomer);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'navigateUnlinkedAccounts', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEUNLINKEDACCOUNTS);
        },
        set: function(navigateUnlinkedAccounts) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink").ATTR_NAVIGATEUNLINKEDACCOUNTS, navigateUnlinkedAccounts);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_link_unlink"]();
