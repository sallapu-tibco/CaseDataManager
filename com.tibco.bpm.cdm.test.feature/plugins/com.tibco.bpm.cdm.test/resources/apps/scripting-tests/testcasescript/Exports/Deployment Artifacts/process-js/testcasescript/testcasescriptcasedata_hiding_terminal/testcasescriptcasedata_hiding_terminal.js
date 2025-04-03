/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ACCOUNTBOMS = "accountBOMs";
    theClass.ATTR_ACCOUNTCASES = "accountCases";
    theClass.ATTR_ACCOUNTCASESFINDALL = "accountCasesFindAll";
    theClass.ATTR_ACCOUNTCASESSIMPLESEARCH = "accountCasesSimpleSearch";
    theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA = "accountCasesFindByCriteria";
    theClass.ATTR_CUSTOMERCASES = "customerCases";
    theClass.ATTR_CUSTOMERBOMS = "customerBOMs";
    theClass.ATTR_CUSTOMERCASENONTERMINAL = "customerCaseNonTerminal";
    theClass.ATTR_ACCOUNTCASESNONTERMINAL = "accountCasesNonTerminal";
    theClass.ATTR_ACCOUNTCASESTERMINAL = "accountCasesTerminal";
    theClass.ATTR_ACCOUNTBOMSREADALL = "accountBOMsReadAll";
    theClass.ATTR_ACCOUNTBOMSREADTERMINAL = "accountBOMsReadTerminal";
    theClass.ATTR_ACCOUNTCASEFINDBYCID = "accountCaseFindByCID";
    theClass.ATTR_ACCOUNTBOMUPDATETOTERMINAL = "accountBOMUpdateToTerminal";
    theClass.ATTR_ACCOUNTCASEUPDATETOTERMINAL = "accountCaseUpdateToTerminal";
    theClass.ATTR_ACCOUNTCASESNAVIGATEALL = "accountCasesNavigateAll";
    theClass.ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA = "accountCasesNavigateByCriteria";
    theClass.ATTR_ACCOUNTCASESNAVIGATEBYSEARCH = "accountCasesNavigateBySearch";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMS] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDALL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESSIMPLESEARCH] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
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
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERCASES] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERBOMS] = {
        type: "com.example.testcasedatascripting.Customer",
        baseType: "com.example.testcasedatascripting.Customer",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_CUSTOMERCASENONTERMINAL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESNONTERMINAL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESTERMINAL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSREADALL] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSREADTERMINAL] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEFINDBYCID] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMUPDATETOTERMINAL] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEUPDATETOTERMINAL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESNAVIGATEALL] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESNAVIGATEBYSEARCH] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESSIMPLESEARCH,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_CUSTOMERCASES,
        theClass.ATTR_CUSTOMERCASENONTERMINAL,
        theClass.ATTR_ACCOUNTCASESNONTERMINAL,
        theClass.ATTR_ACCOUNTCASESTERMINAL,
        theClass.ATTR_ACCOUNTCASEFINDBYCID,
        theClass.ATTR_ACCOUNTCASEUPDATETOTERMINAL,
        theClass.ATTR_ACCOUNTCASESNAVIGATEALL,
        theClass.ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESNAVIGATEBYSEARCH
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_CUSTOMERBOMS,
        theClass.ATTR_ACCOUNTBOMSREADALL,
        theClass.ATTR_ACCOUNTBOMSREADTERMINAL,
        theClass.ATTR_ACCOUNTBOMUPDATETOTERMINAL
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESSIMPLESEARCH,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_CUSTOMERCASES,
        theClass.ATTR_CUSTOMERBOMS,
        theClass.ATTR_CUSTOMERCASENONTERMINAL,
        theClass.ATTR_ACCOUNTCASESNONTERMINAL,
        theClass.ATTR_ACCOUNTCASESTERMINAL,
        theClass.ATTR_ACCOUNTBOMSREADALL,
        theClass.ATTR_ACCOUNTBOMSREADTERMINAL,
        theClass.ATTR_ACCOUNTCASEFINDBYCID,
        theClass.ATTR_ACCOUNTBOMUPDATETOTERMINAL,
        theClass.ATTR_ACCOUNTCASEUPDATETOTERMINAL,
        theClass.ATTR_ACCOUNTCASESNAVIGATEALL,
        theClass.ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESNAVIGATEBYSEARCH
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal";
    };


    Object.defineProperty(theClass.prototype, 'accountBOMs', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTBOMS);
        },
        set: function(accountBOMs) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMs' in the type 'com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCases', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASES);
        },
        set: function(accountCases) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASES, accountCases);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindAll', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESFINDALL);
        },
        set: function(accountCasesFindAll) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESFINDALL, accountCasesFindAll);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesSimpleSearch', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESSIMPLESEARCH);
        },
        set: function(accountCasesSimpleSearch) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESSIMPLESEARCH, accountCasesSimpleSearch);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESFINDBYCRITERIA);
        },
        set: function(accountCasesFindByCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESFINDBYCRITERIA, accountCasesFindByCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerCases', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_CUSTOMERCASES);
        },
        set: function(customerCases) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_CUSTOMERCASES, customerCases);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerBOMs', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_CUSTOMERBOMS);
        },
        set: function(customerBOMs) {
            throw "Cannot re-assign multi-valued attribute 'customerBOMs' in the type 'com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'customerCaseNonTerminal', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_CUSTOMERCASENONTERMINAL);
        },
        set: function(customerCaseNonTerminal) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_CUSTOMERCASENONTERMINAL, customerCaseNonTerminal);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesNonTerminal', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNONTERMINAL);
        },
        set: function(accountCasesNonTerminal) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNONTERMINAL, accountCasesNonTerminal);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesTerminal', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESTERMINAL);
        },
        set: function(accountCasesTerminal) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESTERMINAL, accountCasesTerminal);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMsReadAll', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTBOMSREADALL);
        },
        set: function(accountBOMsReadAll) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsReadAll' in the type 'com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMsReadTerminal', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTBOMSREADTERMINAL);
        },
        set: function(accountBOMsReadTerminal) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsReadTerminal' in the type 'com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseFindByCID', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASEFINDBYCID);
        },
        set: function(accountCaseFindByCID) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASEFINDBYCID, accountCaseFindByCID);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMUpdateToTerminal', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTBOMUPDATETOTERMINAL);
        },
        set: function(accountBOMUpdateToTerminal) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal");
            var attrRef = classRef.ATTR_ACCOUNTBOMUPDATETOTERMINAL;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMUpdateToTerminal == null) || accountBOMUpdateToTerminal instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMUpdateToTerminal);
            } else {
                throw "Wrong input object type for 'accountBOMUpdateToTerminal' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMUpdateToTerminal) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseUpdateToTerminal', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASEUPDATETOTERMINAL);
        },
        set: function(accountCaseUpdateToTerminal) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASEUPDATETOTERMINAL, accountCaseUpdateToTerminal);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesNavigateAll', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEALL);
        },
        set: function(accountCasesNavigateAll) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEALL, accountCasesNavigateAll);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesNavigateByCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA);
        },
        set: function(accountCasesNavigateByCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEBYCRITERIA, accountCasesNavigateByCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesNavigateBySearch', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEBYSEARCH);
        },
        set: function(accountCasesNavigateBySearch) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal").ATTR_ACCOUNTCASESNAVIGATEBYSEARCH, accountCasesNavigateBySearch);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testcasescriptcasedata_hiding_terminal"]();
