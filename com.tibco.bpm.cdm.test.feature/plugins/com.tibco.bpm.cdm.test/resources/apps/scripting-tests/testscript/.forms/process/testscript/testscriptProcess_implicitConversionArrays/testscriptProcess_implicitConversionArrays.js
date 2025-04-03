/*
 * This provides an implementation of the BOM class com.example.testscript.testscript.testscriptProcess_implicitConversionArrays. 
 */
bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess_implicitConversionArrays"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testscript.testscript.testscriptProcess_implicitConversionArrays");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_DFNUMBER = "DFNumber";
    theClass.ATTR_DFTIME = "DFTime";
    theClass.ATTR_DFURI = "DFUri";
    theClass.ATTR_DFDATETIMEZONE = "DFDateTimeZone";
    theClass.ATTR_DFDATE = "DFDate";
    theClass.ATTR_DFIMPLICITTEXTS = "DFImplicitTexts";
    theClass.ATTR_DFBOOLEAN = "DFBoolean";
    theClass.ATTR_DFPERFORMER = "DFPerformer";
    theClass.ATTR_DFDATETIMEZONES = "DFDateTimeZones";
    theClass.ATTR_DFBOOLEANS = "DFBooleans";
    theClass.ATTR_DFTIMES = "DFTimes";
    theClass.ATTR_DFURIS = "DFUris";
    theClass.ATTR_DFPERFORMERS = "DFPerformers";
    theClass.ATTR_DFDATES = "DFDates";
    theClass.ATTR_DFTEXTS = "DFTexts";
    theClass.ATTR_DFFIXEDPOINTNUMBERS = "DFFixedPointNumbers";
    theClass.ATTR_DFFLOATINGPOINTNUMBERS = "DFFloatingPointNumbers";
    theClass.ATTR_DFFLOATINGPOINTNUMBER = "DFFloatingPointNumber";

    theClass.TYPE_ARRAY = {};
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
    theClass.TYPE_ARRAY[theClass.ATTR_DFTIME] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
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
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATETIMEZONE] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
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
    theClass.TYPE_ARRAY[theClass.ATTR_DFIMPLICITTEXTS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 500
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFBOOLEAN] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
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
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATETIMEZONES] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFBOOLEANS] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFTIMES] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFURIS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFPERFORMERS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATES] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFTEXTS] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 200
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFIXEDPOINTNUMBERS] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 6,
        decimalPlaces: 3

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFLOATINGPOINTNUMBERS] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFLOATINGPOINTNUMBER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_DFNUMBER,
        theClass.ATTR_DFTIME,
        theClass.ATTR_DFURI,
        theClass.ATTR_DFDATETIMEZONE,
        theClass.ATTR_DFDATE,
        theClass.ATTR_DFIMPLICITTEXTS,
        theClass.ATTR_DFBOOLEAN,
        theClass.ATTR_DFPERFORMER,
        theClass.ATTR_DFDATETIMEZONES,
        theClass.ATTR_DFBOOLEANS,
        theClass.ATTR_DFTIMES,
        theClass.ATTR_DFURIS,
        theClass.ATTR_DFPERFORMERS,
        theClass.ATTR_DFDATES,
        theClass.ATTR_DFTEXTS,
        theClass.ATTR_DFFIXEDPOINTNUMBERS,
        theClass.ATTR_DFFLOATINGPOINTNUMBERS,
        theClass.ATTR_DFFLOATINGPOINTNUMBER
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_DFNUMBER,
        theClass.ATTR_DFTIME,
        theClass.ATTR_DFURI,
        theClass.ATTR_DFDATETIMEZONE,
        theClass.ATTR_DFDATE,
        theClass.ATTR_DFIMPLICITTEXTS,
        theClass.ATTR_DFBOOLEAN,
        theClass.ATTR_DFPERFORMER,
        theClass.ATTR_DFDATETIMEZONES,
        theClass.ATTR_DFBOOLEANS,
        theClass.ATTR_DFTIMES,
        theClass.ATTR_DFURIS,
        theClass.ATTR_DFPERFORMERS,
        theClass.ATTR_DFDATES,
        theClass.ATTR_DFTEXTS,
        theClass.ATTR_DFFIXEDPOINTNUMBERS,
        theClass.ATTR_DFFLOATINGPOINTNUMBERS,
        theClass.ATTR_DFFLOATINGPOINTNUMBER
    ];

    theClass.getName = function() {
        return "com.example.testscript.testscript.testscriptProcess_implicitConversionArrays";
    };


    Object.defineProperty(theClass.prototype, 'DFNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFNUMBER);
        },
        set: function(DFNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFNUMBER, DFNumber);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFTime', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTIME);
        },
        set: function(DFTime) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTIME, DFTime);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFUri', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFURI);
        },
        set: function(DFUri) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFURI, DFUri);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDateTimeZone', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATETIMEZONE);
        },
        set: function(DFDateTimeZone) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATETIMEZONE, DFDateTimeZone);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDate', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATE);
        },
        set: function(DFDate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATE, DFDate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFImplicitTexts', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFIMPLICITTEXTS);
        },
        set: function(DFImplicitTexts) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFIMPLICITTEXTS, DFImplicitTexts);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFBoolean', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFBOOLEAN);
        },
        set: function(DFBoolean) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFBOOLEAN, DFBoolean);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFPerformer', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFPERFORMER);
        },
        set: function(DFPerformer) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFPERFORMER, DFPerformer);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDateTimeZones', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATETIMEZONES);
        },
        set: function(DFDateTimeZones) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATETIMEZONES, DFDateTimeZones);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFBooleans', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFBOOLEANS);
        },
        set: function(DFBooleans) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFBOOLEANS, DFBooleans);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFTimes', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTIMES);
        },
        set: function(DFTimes) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTIMES, DFTimes);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFUris', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFURIS);
        },
        set: function(DFUris) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFURIS, DFUris);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFPerformers', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFPERFORMERS);
        },
        set: function(DFPerformers) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFPERFORMERS, DFPerformers);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDates', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATES);
        },
        set: function(DFDates) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFDATES, DFDates);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFTexts', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTEXTS);
        },
        set: function(DFTexts) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFTEXTS, DFTexts);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFixedPointNumbers', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFIXEDPOINTNUMBERS);
        },
        set: function(DFFixedPointNumbers) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFIXEDPOINTNUMBERS, DFFixedPointNumbers);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFloatingPointNumbers', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFLOATINGPOINTNUMBERS);
        },
        set: function(DFFloatingPointNumbers) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFLOATINGPOINTNUMBERS, DFFloatingPointNumbers);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFloatingPointNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFLOATINGPOINTNUMBER);
        },
        set: function(DFFloatingPointNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_implicitConversionArrays").ATTR_DFFLOATINGPOINTNUMBER, DFFloatingPointNumber);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess_implicitConversionArrays"]();
