
/*
 * This provides an implementation of the BOM class com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom. 
 */

bpm.data.Loader.classDefiner["com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_FIELDBOM = "FieldBOM";
    theClass.ATTR_FIELDCASE = "FieldCase";
    theClass.ATTR_FIELDBOMREAD = "FieldBOMRead";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_FIELDBOM] = {
        type: "com.example.different_bom_different_application.DifferentBOMDifferentApplication",
        baseType: "com.example.different_bom_different_application.DifferentBOMDifferentApplication",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };
    theClass.TYPE_ARRAY[theClass.ATTR_FIELDCASE] = {
        type: "BomPrimitiveTypes.Text",
        baseType: "BomPrimitiveTypes.Text",
        primitive: true,
        multivalued: false,
        required: true,
        defaultValue: "",
        length: -1
    };
    theClass.TYPE_ARRAY[theClass.ATTR_FIELDBOMREAD] = {
        type: "com.example.different_bom_different_application.DifferentBOMDifferentApplication",
        baseType: "com.example.different_bom_different_application.DifferentBOMDifferentApplication",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [
        theClass.ATTR_FIELDCASE
    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_FIELDBOM,
        theClass.ATTR_FIELDBOMREAD
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_FIELDBOM,
        theClass.ATTR_FIELDCASE,
        theClass.ATTR_FIELDBOMREAD
    ];

    theClass.getName = function() {
        return "com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom";
    };


    Object.defineProperty(theClass.prototype, 'FieldBOM', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom").ATTR_FIELDBOM);
        },
        set: function(FieldBOM) {
            var classRef = this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom");
            var attrRef = classRef.ATTR_FIELDBOM;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(FieldBOM == null) || FieldBOM instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, FieldBOM);
            } else {
                throw "Wrong input object type for 'FieldBOM' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(FieldBOM) + "'.";
            }
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'FieldCase', {
        get: function() {
            return this._getPrimitiveAttribute(this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom").ATTR_FIELDCASE);
        },
        set: function(FieldCase) {
            this._setPrimitiveAttribute(this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom").ATTR_FIELDCASE, FieldCase);
        },
        enumerable: true
    });


    Object.defineProperty(theClass.prototype, 'FieldBOMRead', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom").ATTR_FIELDBOMREAD);
        },
        set: function(FieldBOMRead) {
            var classRef = this.$loader.getClass("com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom");
            var attrRef = classRef.ATTR_FIELDBOMREAD;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(FieldBOMRead == null) || FieldBOMRead instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, FieldBOMRead);
            } else {
                throw "Wrong input object type for 'FieldBOMRead' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(FieldBOMRead) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.ace_2449_process.ace_2449_process.createcasedifferentapplicationdifferentbom"]();
