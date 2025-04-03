/*
 * Provides an implementation of the ProcessData for process DataInitTestProcess. 
 */

com.tibco.data.Loader.classDefiner["ProcessData"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = com.tibco.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "ProcessData");
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
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_DATEPARAM] = {
        type: "BomPrimitiveTypes.Date",
        baseType: "BomPrimitiveTypes.Date",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_NUMBERPARAM] = {
        type: "BomPrimitiveTypes.Integer",
        baseType: "BomPrimitiveTypes.Integer",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_TEXTPARAM] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: false,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_OBJECTPARAM] = {
        type: "com.tibco.ace.datainit.test.Pet",
        baseType: "com.tibco.ace.datainit.test.Pet",
        primitive: false,
        multivalued: false,
        required: false,
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
        return "ProcessData";
    };

    Object.defineProperty(theClass.prototype, 'booleanParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_BOOLEANPARAM);
        },
        set: function(booleanParam) {
            this._setPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_BOOLEANPARAM, booleanParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'dateParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_DATEPARAM);
        },
        set: function(dateParam) {
            this._setPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_DATEPARAM, dateParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'numberParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_NUMBERPARAM);
        },
        set: function(numberParam) {
            this._setPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_NUMBERPARAM, numberParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'textParam', {
        get: function() {
            return this._getPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_TEXTPARAM);
        },
        set: function(textParam) {
            this._setPrimitiveAttribute(this.loader.getClass("ProcessData").ATTR_TEXTPARAM, textParam);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'objectParam', {
        get: function() {
            return this._getComplexAttribute(this.loader.getClass("ProcessData").ATTR_OBJECTPARAM);
        },
        set: function(objectParam) {
            var classRef = this.loader.getClass("ProcessData");
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

com.tibco.data.Loader.classDefiner["ProcessData"]();
