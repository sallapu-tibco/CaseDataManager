/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_account. 
 */
bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_account"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_account");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_ACCOUNTBOMS = "accountBOMs";
    theClass.ATTR_ACCOUNTBOM = "accountBOM";
    theClass.ATTR_ACCOUNTCASE = "accountCase";
    theClass.ATTR_ACCOUNTCASES = "accountCases";
    theClass.ATTR_ACCOUNTBOMREAD = "accountBOMRead";
    theClass.ATTR_ACCOUNTBOMSREAD = "accountBOMsRead";
    theClass.ATTR_ACCOUNTCASESFINDALL = "accountCasesFindAll";
    theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA = "accountCasesFindByCriteria";
    theClass.ATTR_ACCOUNTCASESSEARCH = "accountCasesSearch";
    theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED = "accountCasesFindByCriteriaPaginated";
    theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA = "accountCasesFindByMultiCriteria";
    theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED = "accountCasesSearchPaginated";
    theClass.ATTR_ACCOUNTCASEDEFAULT = "accountCaseDefault";
    theClass.ATTR_ACCOUNTBOMDEFAULT = "accountBOMDefault";
    theClass.ATTR_ACCOUNTCASEUPDATE = "accountCaseUpdate";
    theClass.ATTR_ACCOUNTBOMUPDATE = "accountBOMUpdate";
    theClass.ATTR_ACCOUNTBOMSTOUPDATE = "accountBOMsToUpdate";
    theClass.ATTR_ACCOUNTCASESTOUPDATE = "accountCasesToUpdate";
    theClass.ATTR_ACCOUNTBOMSAFTERUPDATE = "accountBOMsAfterUpdate";
    theClass.ATTR_ACCOUNTCASESAFTERUPDATE = "accountCasesAfterUpdate";
    theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE = "accountCasesFindAllAfterUpdateNDelete";
    theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE = "accountCasesFindAllAfterUpdatedDelete";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMS] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASES] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMREAD] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSREAD] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESSEARCH] = {
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA] = {
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
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEDEFAULT] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMDEFAULT] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASEUPDATE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMUPDATE] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSTOUPDATE] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESTOUPDATE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTBOMSAFTERUPDATE] = {
        type: "com.example.testcasedatascripting.Account",
        baseType: "com.example.testcasedatascripting.Account",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESAFTERUPDATE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESSEARCH,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED,
        theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA,
        theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED,
        theClass.ATTR_ACCOUNTCASEDEFAULT,
        theClass.ATTR_ACCOUNTCASEUPDATE,
        theClass.ATTR_ACCOUNTCASESTOUPDATE,
        theClass.ATTR_ACCOUNTCASESAFTERUPDATE,
        theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE,
        theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_ACCOUNTBOMREAD,
        theClass.ATTR_ACCOUNTBOMSREAD,
        theClass.ATTR_ACCOUNTBOMDEFAULT,
        theClass.ATTR_ACCOUNTBOMUPDATE,
        theClass.ATTR_ACCOUNTBOMSTOUPDATE,
        theClass.ATTR_ACCOUNTBOMSAFTERUPDATE
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ACCOUNTBOMS,
        theClass.ATTR_ACCOUNTBOM,
        theClass.ATTR_ACCOUNTCASE,
        theClass.ATTR_ACCOUNTCASES,
        theClass.ATTR_ACCOUNTBOMREAD,
        theClass.ATTR_ACCOUNTBOMSREAD,
        theClass.ATTR_ACCOUNTCASESFINDALL,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIA,
        theClass.ATTR_ACCOUNTCASESSEARCH,
        theClass.ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED,
        theClass.ATTR_ACCOUNTCASESFINDBYMULTICRITERIA,
        theClass.ATTR_ACCOUNTCASESSEARCHPAGINATED,
        theClass.ATTR_ACCOUNTCASEDEFAULT,
        theClass.ATTR_ACCOUNTBOMDEFAULT,
        theClass.ATTR_ACCOUNTCASEUPDATE,
        theClass.ATTR_ACCOUNTBOMUPDATE,
        theClass.ATTR_ACCOUNTBOMSTOUPDATE,
        theClass.ATTR_ACCOUNTCASESTOUPDATE,
        theClass.ATTR_ACCOUNTBOMSAFTERUPDATE,
        theClass.ATTR_ACCOUNTCASESAFTERUPDATE,
        theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE,
        theClass.ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE
    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_account";
    };


    Object.defineProperty(theClass.prototype, 'accountBOMs', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMS);
        },
        set: function(accountBOMs) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMs' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_account'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOM);
        },
        set: function(accountBOM) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account");
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
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASE);
        },
        set: function(accountCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASE, accountCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCases', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASES);
        },
        set: function(accountCases) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASES, accountCases);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMRead', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMREAD);
        },
        set: function(accountBOMRead) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account");
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


    Object.defineProperty(theClass.prototype, 'accountBOMsRead', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMSREAD);
        },
        set: function(accountBOMsRead) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsRead' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_account'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindAll', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALL);
        },
        set: function(accountCasesFindAll) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALL, accountCasesFindAll);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYCRITERIA);
        },
        set: function(accountCasesFindByCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYCRITERIA, accountCasesFindByCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesSearch', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESSEARCH);
        },
        set: function(accountCasesSearch) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESSEARCH, accountCasesSearch);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByCriteriaPaginated', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED);
        },
        set: function(accountCasesFindByCriteriaPaginated) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYCRITERIAPAGINATED, accountCasesFindByCriteriaPaginated);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindByMultiCriteria', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYMULTICRITERIA);
        },
        set: function(accountCasesFindByMultiCriteria) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDBYMULTICRITERIA, accountCasesFindByMultiCriteria);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesSearchPaginated', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESSEARCHPAGINATED);
        },
        set: function(accountCasesSearchPaginated) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESSEARCHPAGINATED, accountCasesSearchPaginated);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseDefault', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASEDEFAULT);
        },
        set: function(accountCaseDefault) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASEDEFAULT, accountCaseDefault);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMDefault', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMDEFAULT);
        },
        set: function(accountBOMDefault) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account");
            var attrRef = classRef.ATTR_ACCOUNTBOMDEFAULT;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMDefault == null) || accountBOMDefault instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMDefault);
            } else {
                throw "Wrong input object type for 'accountBOMDefault' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMDefault) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCaseUpdate', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASEUPDATE);
        },
        set: function(accountCaseUpdate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASEUPDATE, accountCaseUpdate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMUpdate', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMUPDATE);
        },
        set: function(accountBOMUpdate) {
            var classRef = this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account");
            var attrRef = classRef.ATTR_ACCOUNTBOMUPDATE;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(accountBOMUpdate == null) || accountBOMUpdate instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, accountBOMUpdate);
            } else {
                throw "Wrong input object type for 'accountBOMUpdate' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(accountBOMUpdate) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMsToUpdate', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMSTOUPDATE);
        },
        set: function(accountBOMsToUpdate) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsToUpdate' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_account'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesToUpdate', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESTOUPDATE);
        },
        set: function(accountCasesToUpdate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESTOUPDATE, accountCasesToUpdate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountBOMsAfterUpdate', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTBOMSAFTERUPDATE);
        },
        set: function(accountBOMsAfterUpdate) {
            throw "Cannot re-assign multi-valued attribute 'accountBOMsAfterUpdate' in the type 'com.example.testcasescript.testcasescript.testscriptcasedata_account'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesAfterUpdate', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESAFTERUPDATE);
        },
        set: function(accountCasesAfterUpdate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESAFTERUPDATE, accountCasesAfterUpdate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindAllAfterUpdateNDelete', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE);
        },
        set: function(accountCasesFindAllAfterUpdateNDelete) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALLAFTERUPDATENDELETE, accountCasesFindAllAfterUpdateNDelete);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'accountCasesFindAllAfterUpdatedDelete', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE);
        },
        set: function(accountCasesFindAllAfterUpdatedDelete) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testcasescript.testcasescript.testscriptcasedata_account").ATTR_ACCOUNTCASESFINDALLAFTERUPDATEDDELETE, accountCasesFindAllAfterUpdatedDelete);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_account"]();
