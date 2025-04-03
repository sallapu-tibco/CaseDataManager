
/*
 * This provides an implementation of the BOM class $Process.Data. 
 */

com.tibco.data.Loader.classDefiner["$Process.Data"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = com.tibco.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "$Process.Data");
    com.tibco.data.Loader.extendClass(com.tibco.data.BomBase, theClass);

    theClass.ATTR_BOOLEANPARAM = "booleanParam";
    theClass.ATTR_DATEPARAM = "dateParam";
    theClass.ATTR_NUMBERPARAM = "numberParam";
    theClass.ATTR_TEXTPARAM = "textParam";
    theClass.ATTR_OBJECTPARAM = "objectParam";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_BOOLEANPARAM] = {
        type: "BomPrimitiveTypes.Boolean",
        baseType: "BomPrimitiveTypes.Boolean",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEPARAM] = {
        type: "BomPrimitiveTypes.DateTime",
        baseType: "BomPrimitiveTypes.DateTime",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERPARAM] = {
        type: "BomPrimitiveTypes.Decimal",
        baseType: "BomPrimitiveTypes.Decimal",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 10,
        decimalPlaces: 2

    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTPARAM] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: 50
    };
    theClass.TYPE_ARRAY[theClass.ATTR_OBJECTPARAM] = {
        type: "com.tibco.ace.datainit.test.Pet",
        baseType: "com.tibco.ace.datainit.test.Pet",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANPARAM,
        theClass.ATTR_DATEPARAM,
        theClass.ATTR_NUMBERPARAM,
        theClass.ATTR_TEXTPARAM
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_OBJECTPARAM
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_BOOLEANPARAM,
        theClass.ATTR_DATEPARAM,
        theClass.ATTR_NUMBERPARAM,
        theClass.ATTR_TEXTPARAM,
        theClass.ATTR_OBJECTPARAM
    ];

    theClass.getName = function() {
        return "$Process.Data";
    };


    Object.defineProperty(theClass.prototype, 'booleanParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_BOOLEANPARAM);
        },
        set: function(booleanParam) {
            this._setPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_BOOLEANPARAM, booleanParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_DATEPARAM);
        },
        set: function(dateParam) {
            this._setPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_DATEPARAM, dateParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_NUMBERPARAM);
        },
        set: function(numberParam) {
            this._setPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_NUMBERPARAM, numberParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_TEXTPARAM);
        },
        set: function(textParam) {
            this._setPrimitiveAttribute(this.loader.getClass("$Process.Data").ATTR_TEXTPARAM, textParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'objectParam', {
        get: function() {
            return this._getComplexAttribute(this.loader.getClass("$Process.Data").ATTR_OBJECTPARAM);
        },
        set: function(objectParam) {
            var classRef = this.loader.getClass("$Process.Data");
            var attrRef = classRef.ATTR_OBJECTPARAM;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("objectParam instanceof this.loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, objectParam);
            } else {
                throw "Wrong input object type.";
            }
        },
        enumerable: true
    });

};

com.tibco.data.Loader.classDefiner["$Process.Data"]();
