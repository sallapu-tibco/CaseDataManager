/*==============================================================
BEGIN GENERATED SCHEMA INFORMATION
This information can be part of the generated BOM JavaScript package.
==============================================================*/
var processDataDescriptors = {
    booleanParam: {
        name: "booleanParam",
        isArray: false,
        isPrimitive: true,
        isRequired: true,
        type: "boolean"
    },
    dateParam: {
        name: "dateParam",
        isArray: false,
        isPrimitive: false,
        isRequired: true,
        type: Date
    },
    numberParam: {
        name: "numberParam",
        isArray: false,
        isPrimitive: true,
        isRequired: true,
        type: "number"
    },
    textParam: {
        name: "textParam",
        isArray: false,
        isPrimitive: true,
        isRequired: false,
        type: "string"
    },
    objectParam: {
        name: "objectParam",
        isArray: false,
        isPrimitive: false,
        isRequired: true,
        type: Pet
    }
};

// An example class, will be defined in the BOM-JS file.
function Pet(obj) {
    this.name = obj.name;
    this.type = obj.type;
}
Pet.prototype.name = "unnamed";
Pet.prototype.type = "unknown";
Pet.prototype.toString = function () {
    return "Pet{name: '" + this.name + "', type: '" + this.type + "'}"
};
/*==============================================================
END GENERATED SCHEMA INFORMATION
==============================================================*/
