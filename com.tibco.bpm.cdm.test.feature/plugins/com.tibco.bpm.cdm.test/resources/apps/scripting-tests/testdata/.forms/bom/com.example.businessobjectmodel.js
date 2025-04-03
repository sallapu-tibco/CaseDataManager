/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM package com.example.businessobjectmodel.BusinessobjectmodelPackage.
 */

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.BusinessobjectmodelPackage"] = function() {
    /** Constructor. */
    var theClass = function() {
    };
    bpm.data.Loader.currentLoader.registerPackage(theClass, "com.example.businessobjectmodel.BusinessobjectmodelPackage");

    // Define the enumerations declared by this package.
    theClass.testenumeration = ["CREATED", "COMPLETED1234435645689", "INTERMEDIATE1234567890", "CANCELLED"];
    theClass.testenumeration.CREATED = "CREATED";
    theClass.testenumeration.COMPLETED1234435645689 = "COMPLETED1234435645689";
    theClass.testenumeration.INTERMEDIATE1234567890 = "INTERMEDIATE1234567890";
    theClass.testenumeration.CANCELLED = "CANCELLED";
};

bpm.scriptUtil._internal.checkVersionCompatibility("com.example.businessobjectmodel.js", "11.0.0.013");
bpm.data.Loader.classDefiner["com.example.businessobjectmodel.BusinessobjectmodelPackage"]();

/*
 * DO NOT EDIT: This is automatically generated code.
 * This provides an implementation of the BOM factory com.example.businessobjectmodel.BusinessobjectmodelFactory. 
 */ 

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.BusinessobjectmodelFactory"] = function() {
    /** Constructor. */
    var theClass = function(form) {
        this.context = new Object();
        this.context.form = form;
        if (form.getLogger)
            this.context.logger = form.getLogger();
        this.context.item = null;
        this.context.id = null;
        this.$loader = form._loader;
    };
    bpm.data.Loader.currentLoader.registerFactory(theClass, "com.example.businessobjectmodel.BusinessobjectmodelFactory");

    theClass.prototype.SUPPORTED_CLASSES = [
        "com.example.businessobjectmodel.testclass",
        "com.example.businessobjectmodel.testclasscalling",
        "com.example.businessobjectmodel.locallycalledclass"
    ];

    theClass.prototype.checkClassName = function(className) {
        for (var i = 0; i < this.SUPPORTED_CLASSES.length; i++) {
            if (className == this.SUPPORTED_CLASSES[i])
                return;
        };
        throw "Factory com.example.businessobjectmodel.BusinessobjectmodelFactory does not support class \"" + className + "\"";
    };

    theClass.prototype.create = function(className, jsonData) {
        this.checkClassName(className);
        var instance = this.$loader.newInstance(className, this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData, this.context);
        return instance;
    };

    theClass.prototype.createTestclass = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.testclass", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateTestclass = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.testclass", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createTestclasscalling = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.testclasscalling", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateTestclasscalling = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.testclasscalling", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.createLocallycalledclass = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.locallycalledclass", this.context);
        if (jsonData != undefined)
            instance = instance._setValue(instance, jsonData);
        return instance;
    };

    theClass.prototype.listCreateLocallycalledclass = function(jsonData) {
        var instance = this.$loader.newInstance("com.example.businessobjectmodel.locallycalledclass", this.context);
        return instance._setValueList(instance, jsonData);
    };
    theClass.prototype.getClass = function(className) {
        this.checkClassName(className);
        return this.$loader.getClass(className);
    };
};

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.BusinessobjectmodelFactory"]();

/*
 * This provides an implementation of the BOM class com.example.businessobjectmodel.locallycalledclass. 
 */

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.locallycalledclass"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.businessobjectmodel.locallycalledclass");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_ATTRIBUTETEXTLOCALLYCALLED = "attributeTextLocallyCalled";
    theClass.ATTR_ATTRIBUTENUMBERLOCALLYCALLED = "attributeNumberLocallyCalled";
    theClass.ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY = "attributeDateLocallyCalledArray";
    theClass.ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION = "attributeLocallyCalledEnumeration";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTETEXTLOCALLYCALLED] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 5
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTENUMBERLOCALLYCALLED] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 5,
        decimalPlaces: 1

    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION] = {
        type: "com.example.businessobjectmodel.testenumeration",
        baseType: "com.example.businessobjectmodel.testenumeration",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXTLOCALLYCALLED,
        theClass.ATTR_ATTRIBUTENUMBERLOCALLYCALLED,
        theClass.ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY,
        theClass.ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXTLOCALLYCALLED,
        theClass.ATTR_ATTRIBUTENUMBERLOCALLYCALLED,
        theClass.ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY,
        theClass.ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION
    ];

    theClass.getName = function() {
        return "com.example.businessobjectmodel.locallycalledclass";
    };


    Object.defineProperty(theClass.prototype, 'attributeTextLocallyCalled', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTETEXTLOCALLYCALLED);
        },
        set: function(attributeTextLocallyCalled) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTETEXTLOCALLYCALLED, attributeTextLocallyCalled);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeNumberLocallyCalled', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTENUMBERLOCALLYCALLED);
        },
        set: function(attributeNumberLocallyCalled) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTENUMBERLOCALLYCALLED, attributeNumberLocallyCalled);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeDateLocallyCalledArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY);
        },
        set: function(attributeDateLocallyCalledArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTEDATELOCALLYCALLEDARRAY, attributeDateLocallyCalledArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeLocallyCalledEnumeration', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION);
        },
        set: function(attributeLocallyCalledEnumeration) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.locallycalledclass").ATTR_ATTRIBUTELOCALLYCALLEDENUMERATION, attributeLocallyCalledEnumeration);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.locallycalledclass"]();

/*
 * This provides an implementation of the BOM class com.example.businessobjectmodel.testclasscalling. 
 */

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.testclasscalling"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.businessobjectmodel.testclasscalling");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_ATTRIBUTETEXTARRAY = "attributeTextArray";
    theClass.ATTR_ATTRIBUTETIMEARRAY = "attributeTimeArray";
    theClass.ATTR_ATTRIBUTEDATEARRAY = "attributeDateArray";
    theClass.ATTR_ATTRIBUTEDATETIMETZARRAY = "attributeDateTimeTZArray";
    theClass.ATTR_ATTRIBUTEURIARRAY = "attributeUriArray";
    theClass.ATTR_ATTRIBUTEBOOLEANARRAY = "attributeBooleanArray";
    theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY = "attributeFixedPointNumberArray";
    theClass.ATTR_ATTRIBUTELOCALCALLINGARRAY = "attributeLocalCallingArray";
    theClass.ATTR_ATTRIBUTEOUTSIDEENUMERATION = "attributeOutsideEnumeration";
    theClass.ATTR_ATTRIBUTEOUTSIDECALLINGARRAY = "attributeOutsideCallingArray";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTETEXTARRAY] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 10
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTETIMEARRAY] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEDATEARRAY] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEDATETIMETZARRAY] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEURIARRAY] = {
        type: "BomPrimitiveTypes.URI",
        baseType: "BomPrimitiveTypes.URI",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEBOOLEANARRAY] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: true,
        required: false,
        defaultValue: "",
        length: 10,
        decimalPlaces: 4

    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTELOCALCALLINGARRAY] = {
        type: "com.example.businessobjectmodel.locallycalledclass",
        baseType: "com.example.businessobjectmodel.locallycalledclass",
        primitive: false,
        multivalued: true,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEOUTSIDEENUMERATION] = {
        type: "com.example.testdata_outside.outside_enumeration",
        baseType: "com.example.testdata_outside.outside_enumeration",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEOUTSIDECALLINGARRAY] = {
        type: "com.example.testdata_outside.outside_class",
        baseType: "com.example.testdata_outside.outside_class",
        primitive: false,
        multivalued: true,
        required: false,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXTARRAY,
        theClass.ATTR_ATTRIBUTETIMEARRAY,
        theClass.ATTR_ATTRIBUTEDATEARRAY,
        theClass.ATTR_ATTRIBUTEDATETIMETZARRAY,
        theClass.ATTR_ATTRIBUTEURIARRAY,
        theClass.ATTR_ATTRIBUTEBOOLEANARRAY,
        theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY,
        theClass.ATTR_ATTRIBUTEOUTSIDEENUMERATION
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTELOCALCALLINGARRAY,
        theClass.ATTR_ATTRIBUTEOUTSIDECALLINGARRAY
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXTARRAY,
        theClass.ATTR_ATTRIBUTETIMEARRAY,
        theClass.ATTR_ATTRIBUTEDATEARRAY,
        theClass.ATTR_ATTRIBUTEDATETIMETZARRAY,
        theClass.ATTR_ATTRIBUTEURIARRAY,
        theClass.ATTR_ATTRIBUTEBOOLEANARRAY,
        theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY,
        theClass.ATTR_ATTRIBUTELOCALCALLINGARRAY,
        theClass.ATTR_ATTRIBUTEOUTSIDEENUMERATION,
        theClass.ATTR_ATTRIBUTEOUTSIDECALLINGARRAY
    ];

    theClass.getName = function() {
        return "com.example.businessobjectmodel.testclasscalling";
    };


    Object.defineProperty(theClass.prototype, 'attributeTextArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTETEXTARRAY);
        },
        set: function(attributeTextArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTETEXTARRAY, attributeTextArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeTimeArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTETIMEARRAY);
        },
        set: function(attributeTimeArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTETIMEARRAY, attributeTimeArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeDateArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEDATEARRAY);
        },
        set: function(attributeDateArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEDATEARRAY, attributeDateArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeDateTimeTZArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEDATETIMETZARRAY);
        },
        set: function(attributeDateTimeTZArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEDATETIMETZARRAY, attributeDateTimeTZArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeUriArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEURIARRAY);
        },
        set: function(attributeUriArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEURIARRAY, attributeUriArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeBooleanArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEBOOLEANARRAY);
        },
        set: function(attributeBooleanArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEBOOLEANARRAY, attributeBooleanArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeFixedPointNumberArray', {
        get: function() {
            return this._getPrimitiveArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY);
        },
        set: function(attributeFixedPointNumberArray) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEFIXEDPOINTNUMBERARRAY, attributeFixedPointNumberArray);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeLocalCallingArray', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTELOCALCALLINGARRAY);
        },
        set: function(attributeLocalCallingArray) {
            throw "Cannot re-assign multi-valued attribute 'attributeLocalCallingArray' in the type 'com.example.businessobjectmodel.testclasscalling'";
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeOutsideEnumeration', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEOUTSIDEENUMERATION);
        },
        set: function(attributeOutsideEnumeration) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEOUTSIDEENUMERATION, attributeOutsideEnumeration);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeOutsideCallingArray', {
        get: function() {
            return this._getComplexArrayAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclasscalling").ATTR_ATTRIBUTEOUTSIDECALLINGARRAY);
        },
        set: function(attributeOutsideCallingArray) {
            throw "Cannot re-assign multi-valued attribute 'attributeOutsideCallingArray' in the type 'com.example.businessobjectmodel.testclasscalling'";
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.testclasscalling"]();

/*
 * This provides an implementation of the BOM class com.example.businessobjectmodel.testclass. 
 */

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.testclass"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.businessobjectmodel.testclass");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_ATTRIBUTETEXT = "attributeText";
    theClass.ATTR_ATTRIBUTETIME = "attributeTime";
    theClass.ATTR_ATTRIBUTEDATE = "attributeDate";
    theClass.ATTR_ATTRIBUTEDATETIMETZ = "attributeDateTimeTZ";
    theClass.ATTR_ATTRIBUTENUMBER = "attributeNumber";
    theClass.ATTR_ATTRIBUTEURI = "attributeUri";
    theClass.ATTR_ATTRIBUTEBOOLEAN = "attributeBoolean";
    theClass.ATTR_ATTRIBUTEENUMERATION = "attributeEnumeration";
    theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBER = "attributeFixedPointNumber";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTETEXT] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTETIME] = {
        type: "BomPrimitiveTypes.Time",
        baseType: "BomPrimitiveTypes.Time",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEDATE] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEDATETIMETZ] = {
        type: "BomPrimitiveTypes.DateTimeTZ",
        baseType: "BomPrimitiveTypes.DateTimeTZ",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTENUMBER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEURI] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEBOOLEAN] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEENUMERATION] = {
        type: "com.example.businessobjectmodel.testenumeration",
        baseType: "com.example.businessobjectmodel.testenumeration",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBER] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: "",
        length: 10,
        decimalPlaces: 4

    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXT,
        theClass.ATTR_ATTRIBUTETIME,
        theClass.ATTR_ATTRIBUTEDATE,
        theClass.ATTR_ATTRIBUTEDATETIMETZ,
        theClass.ATTR_ATTRIBUTENUMBER,
        theClass.ATTR_ATTRIBUTEURI,
        theClass.ATTR_ATTRIBUTEBOOLEAN,
        theClass.ATTR_ATTRIBUTEENUMERATION,
        theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBER
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_ATTRIBUTETEXT,
        theClass.ATTR_ATTRIBUTETIME,
        theClass.ATTR_ATTRIBUTEDATE,
        theClass.ATTR_ATTRIBUTEDATETIMETZ,
        theClass.ATTR_ATTRIBUTENUMBER,
        theClass.ATTR_ATTRIBUTEURI,
        theClass.ATTR_ATTRIBUTEBOOLEAN,
        theClass.ATTR_ATTRIBUTEENUMERATION,
        theClass.ATTR_ATTRIBUTEFIXEDPOINTNUMBER
    ];

    theClass.getName = function() {
        return "com.example.businessobjectmodel.testclass";
    };


    Object.defineProperty(theClass.prototype, 'attributeText', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTETEXT);
        },
        set: function(attributeText) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTETEXT, attributeText);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeTime', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTETIME);
        },
        set: function(attributeTime) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTETIME, attributeTime);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeDate', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEDATE);
        },
        set: function(attributeDate) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEDATE, attributeDate);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeDateTimeTZ', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEDATETIMETZ);
        },
        set: function(attributeDateTimeTZ) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEDATETIMETZ, attributeDateTimeTZ);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTENUMBER);
        },
        set: function(attributeNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTENUMBER, attributeNumber);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeUri', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEURI);
        },
        set: function(attributeUri) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEURI, attributeUri);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeBoolean', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEBOOLEAN);
        },
        set: function(attributeBoolean) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEBOOLEAN, attributeBoolean);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeEnumeration', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEENUMERATION);
        },
        set: function(attributeEnumeration) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEENUMERATION, attributeEnumeration);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'attributeFixedPointNumber', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEFIXEDPOINTNUMBER);
        },
        set: function(attributeFixedPointNumber) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.businessobjectmodel.testclass").ATTR_ATTRIBUTEFIXEDPOINTNUMBER, attributeFixedPointNumber);
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.businessobjectmodel.testclass"]();
