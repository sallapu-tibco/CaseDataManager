
/*
 * This provides an implementation of the BOM class com.example.carprocess.CarProcess.SubProcess. 
 */

bpm.data.Loader.classDefiner["com.example.carprocess.CarProcess.SubProcess"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.carprocess.CarProcess.SubProcess");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);

    theClass.ATTR_CAR = "car";

    theClass.TYPE_ARRAY = new Object();
    theClass.TYPE_ARRAY[theClass.ATTR_CAR] = {
        type: "com.example.carapplication.Car",
        baseType: "com.example.carapplication.Car",
        primitive: false,
        multivalued: false,
        required: true,
        defaultValue: ""
    };

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [

    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [
        theClass.ATTR_CAR
    ];
    theClass.ATTRIBUTE_NAMES = [
        theClass.ATTR_CAR
    ];

    theClass.getName = function() {
        return "com.example.carprocess.CarProcess.SubProcess";
    };


    Object.defineProperty(theClass.prototype, 'car', {
        get: function() {
            return this._getComplexAttribute(this.$loader.getClass("com.example.carprocess.CarProcess.SubProcess").ATTR_CAR);
        },
        set: function(car) {
            var classRef = this.$loader.getClass("com.example.carprocess.CarProcess.SubProcess");
            var attrRef = classRef.ATTR_CAR;
            var attrType = classRef._getTypeDef(attrRef);
            if (eval("(car == null) || car instanceof this.$loader.getClass(attrType.type)")) {
                this._setComplexAttribute(attrRef, car);
            } else {
                throw "Wrong input object type for 'car' in '" + classRef.getName() + "', expected an instance of '" + attrType.type+ "' but found '" + bpm.scriptUtil.getType(car) + "'.";
            }
        },
        enumerable: true
    });

};

bpm.data.Loader.classDefiner["com.example.carprocess.CarProcess.SubProcess"]();
