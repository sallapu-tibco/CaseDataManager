/*
 * This provides an implementation of the BOM class com.example.testscript.testscript.testscriptProcess_arrays. 
 */
bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess_arrays"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testscript.testscript.testscriptProcess_arrays");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);
    theClass.ATTR_DFTEXTARRAY = "DFTextArray";
    theClass.ATTR_DFNUMBERARRAY = "DFNumberArray";
    theClass.ATTR_DFFIXEDPOINTNUMBERARRAY = "DFFixedPointNumberArray";
    theClass.ATTR_DFBOOLEANARRAY = "DFBooleanArray";
    theClass.ATTR_DFDATEARRAY = "DFDateArray";
    theClass.ATTR_DFTIMEARRAY = "DFTimeArray";
    theClass.ATTR_DFDATETIMEZONEARRAY = "DFDateTimeZoneArray";
    theClass.ATTR_BOMCALLINGCLASS = "BOMCallingClass";
    theClass.ATTR_BOMOUTSIDECLASSES = "BOMOutsideClasses";

    theClass.TYPE_ARRAY = {};
    theClass.TYPE_ARRAY[theClass.ATTR_DFTEXTARRAY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 200
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFNUMBERARRAY] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFFIXEDPOINTNUMBERARRAY] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: "",
        length: 8,
        decimalPlaces: 5

    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFBOOLEANARRAY] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATEARRAY] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFTIMEARRAY] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DFDATETIMEZONEARRAY] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: true,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_BOMCALLINGCLASS] = {
        type: "com.example.businessobjectmodel.testclasscalling",
        baseType: "com.example.businessobjectmodel.testclasscalling",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_BOMOUTSIDECLASSES] = {
        type: "com.example.testdata_outside.outside_class",
        baseType: "com.example.testdata_outside.outside_class",
        primitive: false,
        multivalued: true,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_DFTEXTARRAY,
        theClass.ATTR_DFNUMBERARRAY,
        theClass.ATTR_DFFIXEDPOINTNUMBERARRAY,
        theClass.ATTR_DFBOOLEANARRAY,
        theClass.ATTR_DFDATEARRAY,
        theClass.ATTR_DFTIMEARRAY,
        theClass.ATTR_DFDATETIMEZONEARRAY
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOMCALLINGCLASS,
        theClass.ATTR_BOMOUTSIDECLASSES
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_DFTEXTARRAY,
        theClass.ATTR_DFNUMBERARRAY,
        theClass.ATTR_DFFIXEDPOINTNUMBERARRAY,
        theClass.ATTR_DFBOOLEANARRAY,
        theClass.ATTR_DFDATEARRAY,
        theClass.ATTR_DFTIMEARRAY,
        theClass.ATTR_DFDATETIMEZONEARRAY,
        theClass.ATTR_BOMCALLINGCLASS,
        theClass.ATTR_BOMOUTSIDECLASSES
    ];

    theClass.getName = function() {
        return "com.example.testscript.testscript.testscriptProcess_arrays";
    };


    Object.defineProperty(theClass.prototype, 'DFTextArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFTEXTARRAY);
        },
        set: function(DFTextArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFTEXTARRAY, DFTextArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFNumberArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFNUMBERARRAY);
        },
        set: function(DFNumberArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFNUMBERARRAY, DFNumberArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFFixedPointNumberArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFFIXEDPOINTNUMBERARRAY);
        },
        set: function(DFFixedPointNumberArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFFIXEDPOINTNUMBERARRAY, DFFixedPointNumberArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFBooleanArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFBOOLEANARRAY);
        },
        set: function(DFBooleanArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFBOOLEANARRAY, DFBooleanArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDateArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFDATEARRAY);
        },
        set: function(DFDateArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFDATEARRAY, DFDateArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFTimeArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFTIMEARRAY);
        },
        set: function(DFTimeArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFTIMEARRAY, DFTimeArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'DFDateTimeZoneArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFDATETIMEZONEARRAY);
        },
        set: function(DFDateTimeZoneArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_DFDATETIMEZONEARRAY, DFDateTimeZoneArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'BOMCallingClass', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_BOMCALLINGCLASS);
        },
        set: function(BOMCallingClass) {
            var classRef = this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays");
            var attrRef = classRef.ATTR_BOMCALLINGCLASS;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(BOMCallingClass == null) || BOMCallingClass instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, BOMCallingClass);
            } else {
                throw "Wrong input object type for 'BOMCallingClass' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(BOMCallingClass) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'BOMOutsideClasses', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.testscript.testscript.testscriptProcess_arrays").ATTR_BOMOUTSIDECLASSES);
        },
        set: function(BOMOutsideClasses) {
            throw "Cannot re-assign multi-valued attribute 'BOMOutsideClasses' in the type 'com.example.testscript.testscript.testscriptProcess_arrays'";
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.testscript.testscript.testscriptProcess_arrays"]();
