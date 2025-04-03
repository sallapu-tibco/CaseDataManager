// data can either be a global variable and/or returned by the init() function.
serverData = undefined;

/*==============================================================
BEGIN STATIC FUNCTIONS
The following static functions can be built into the product
==============================================================*/
function getValue(property) {
    return this["_" + property];
}

function setValue(propDesc, newValue) {
    if (propDesc.isArray) {
        if (!Array.isArray(newValue))
            throw new Error(property + " must be an array");

        for (var i = 0; i < newValue.length; i++)
            newValue[i] = convertValue(propDesc, newValue[i]);
    } else {
        newValue = convertValue(propDesc, newValue);
    }
    this["_" + propDesc.name] = newValue;
}

function isInstance(propDesc, newValue) {
    return propDesc.isPrimitive ?
        typeof newValue === propDesc.type :
        newValue instanceof propDesc.type;
}

function convertValue(propDesc, newValue) {
    if (propDesc.isPrimitive) {
        // Can't assign a null object reference to a property that is defined to hold a primitive value.
        if (newValue == null)
            newValue = undefined;
    } else {
        // Can't assign undefined to a property that is defined to hold an object.
        if (newValue == undefined)
            newValue = null;
    }
    var isSet = newValue !== undefined && newValue !== null;
    if (propDesc.isRequired && !isSet)
        throw new Error(propDesc.name + " is required");
    // TODO: apply rounding and other required adjustments.
    if (isSet && !isInstance(propDesc, newValue)) {
        if (propDesc.isPrimitive) {
            switch (propDesc.type) {
                case "boolean":
                    newValue = Boolean(newValue);
                    break;
                case "string":
                    newValue = String(newValue);
                    break;
                case "number":
                    newValue = Number(newValue);
                    break;
            }
        } else {
            newValue = new propDesc.type(newValue);
        }
    }
    return newValue;
}

/**
 * Initialises the <code>serverData</code> object with the correct properties from process relevant data.
 * @param {Map<string, PropertyDescriptor>} processDataDescriptors Map of property descriptors.
 * @param {Map<string, ?>} processDataValues Map of runtime process data values
 * @returns The initialised serverData object.
 */
function init(processDataDescriptors, processDataValues) {
    serverData = {};

    function createJsPropertyDescriptor(propDesc) {
        return {
            enumerable: true,
            get() {
                return getValue.call(serverData, propDesc.name);
            },
            set(newValue) {
                setValue.call(serverData, propDesc, newValue);
            }
        };
    }
    for (prop in processDataDescriptors) {
        Object.defineProperty(serverData, "_" + prop, {
            writable: true
        });
        var propDesc = processDataDescriptors[prop];
        var jsPropDesc = createJsPropertyDescriptor(propDesc);
        Object.defineProperty(serverData, prop, jsPropDesc);
        serverData[prop] = processDataValues[prop];
    }

    Object.seal(serverData);
    return serverData;
}
/*==============================================================
END STATIC FUNCTIONS
================================================================*/
