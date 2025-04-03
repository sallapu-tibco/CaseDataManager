/*
 * This provides an implementation of the BOM class com.example.testscript.testscript.testscriptProcess. 
 */
bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testscript.testscript.testscriptProcess");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_DFTEXT = "DFText";
    theClass.ATTR_DFNUMBER = "DFNumber";
    theClass.ATTR_DFFIXEDPOINTNUMBER = "DFFixedPointNumber";
    theClass.ATTR_DFBOOLEAN = "DFBoolean";
    theClass.ATTR_DFDATE = "DFDate";
    theClass.ATTR_DFTIME = "DFTime";
    theClass.ATTR_DFDATETIMEZONE = "DFDateTimeZone";
    theClass.ATTR_BOMFIELDTESTDATA = "BOMFieldTestData";
    theClass.ATTR_DFIMPLICITTEXT = "DFImplicitText";
    theClass.ATTR_DFURI = "DFUri";
    theClass.ATTR_DFPERFORMER = "DFPerformer";
    theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT1 = "DFFixedPointNumberImplicit1";
    theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT2 = "DFFixedPointNumberImplicit2";
    theClass.ATTR_DFNUMBERIMPLICIT = "DFNumberImplicit";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_DFTEXT] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 200
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFNUMBER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 4,
        decimalPlaces: 0

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFIXEDPOINTNUMBER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 6,
        decimalPlaces: 3

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFBOOLEAN] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATE] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFTIME] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATETIMEZONE] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_BOMFIELDTESTDATA] = {
        type: "com.example.businessobjectmodel.testclass",
        baseType: "com.example.businessobjectmodel.testclass",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFIMPLICITTEXT] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 500
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFURI] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFPERFORMER] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT1] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 6,
        decimalPlaces: 3

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT2] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 6,
        decimalPlaces: 3

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFNUMBERIMPLICIT] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_DFTEXT,
        theClass.ATTR_DFNUMBER,
        theClass.ATTR_DFFIXEDPOINTNUMBER,
        theClass.ATTR_DFBOOLEAN,
        theClass.ATTR_DFDATE,
        theClass.ATTR_DFTIME,
        theClass.ATTR_DFDATETIMEZONE,
        theClass.ATTR_DFIMPLICITTEXT,
        theClass.ATTR_DFURI,
        theClass.ATTR_DFPERFORMER,
        theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT1,
        theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT2,
        theClass.ATTR_DFNUMBERIMPLICIT
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOMFIELDTESTDATA
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_DFTEXT,
        theClass.ATTR_DFNUMBER,
        theClass.ATTR_DFFIXEDPOINTNUMBER,
        theClass.ATTR_DFBOOLEAN,
        theClass.ATTR_DFDATE,
        theClass.ATTR_DFTIME,
        theClass.ATTR_DFDATETIMEZONE,
        theClass.ATTR_BOMFIELDTESTDATA,
        theClass.ATTR_DFIMPLICITTEXT,
        theClass.ATTR_DFURI,
        theClass.ATTR_DFPERFORMER,
        theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT1,
        theClass.ATTR_DFFIXEDPOINTNUMBERIMPLICIT2,
        theClass.ATTR_DFNUMBERIMPLICIT
    ];

    theClass.getName = function() {
        return "com.example.testscript.testscript.testscriptProcess";
    };


    Object.defineProperty(theClass.prototype, 'DFText', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFTEXT);
        },
        set: function(DFText) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFTEXT, DFText);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFNUMBER);
        },
        set: function(DFNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFNUMBER, DFNumber);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFixedPointNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBER);
        },
        set: function(DFFixedPointNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBER, DFFixedPointNumber);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFBoolean', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFBOOLEAN);
        },
        set: function(DFBoolean) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFBOOLEAN, DFBoolean);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDate', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFDATE);
        },
        set: function(DFDate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFDATE, DFDate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFTime', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFTIME);
        },
        set: function(DFTime) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFTIME, DFTime);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDateTimeZone', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFDATETIMEZONE);
        },
        set: function(DFDateTimeZone) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFDATETIMEZONE, DFDateTimeZone);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'BOMFieldTestData', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_BOMFIELDTESTDATA);
        },
        set: function(BOMFieldTestData) {
            var classRef = this.$loader.getClass("com.example.testscript.testscript.testscriptProcess");
            var attrRef = classRef.ATTR_BOMFIELDTESTDATA;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(BOMFieldTestData == null) || BOMFieldTestData instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, BOMFieldTestData);
            } else {
                throw "Wrong input object type for 'BOMFieldTestData' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(BOMFieldTestData) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFImplicitText', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFIMPLICITTEXT);
        },
        set: function(DFImplicitText) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFIMPLICITTEXT, DFImplicitText);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFUri', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFURI);
        },
        set: function(DFUri) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFURI, DFUri);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFPerformer', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFPERFORMER);
        },
        set: function(DFPerformer) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFPERFORMER, DFPerformer);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFixedPointNumberImplicit1', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBERIMPLICIT1);
        },
        set: function(DFFixedPointNumberImplicit1) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBERIMPLICIT1, DFFixedPointNumberImplicit1);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFixedPointNumberImplicit2', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBERIMPLICIT2);
        },
        set: function(DFFixedPointNumberImplicit2) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFFIXEDPOINTNUMBERIMPLICIT2, DFFixedPointNumberImplicit2);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFNumberImplicit', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFNUMBERIMPLICIT);
        },
        set: function(DFNumberImplicit) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess").ATTR_DFNUMBERIMPLICIT, DFNumberImplicit);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess"]();
