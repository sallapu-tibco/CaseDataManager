var logger;
var factory;
var processData;
function setup(_logger, _factory, _processData) {
    logger = _logger;
    factory = _factory;
    processData = _processData;
}

/*==============================================================
BEGIN USER SCRIPT EXECUTION
The following code simulates the execution of user-defined scripts.
================================================================*/
function test(processData) {
    // This doesn't work, get: "Bombase: _setValue: data do not match to the class com.tibco.ace.datainit.test.Pet"
/*
    var cat = factory.com_tibco_ace_datainit_test.createPet({
        name: "Felix",
        type: "cat"
    });
*/
    // Passing a JSON string doesn't work either, same error.
    var cat = factory.com_tibco_ace_datainit_test.createPet(/*"{\n" +
        "\"name\": \"Felix\"," +
        "\"type\": \"cat\"" +
    "}"*/);
    cat.name = "Felix";
    cat.type = "cat";
    var Pet = cat.getClass();
    testAccessors("booleanParam", "boolean", 0);
    testAccessors("dateParam", Date, new Date());
    testAccessors("numberParam", "number", 777);
    testAccessors("textParam", "string", "a string");
    testAccessors("objectParam", Pet, cat);
}

function testAccessors(property, type, newValue) {
    assertIsInstance(property, type, processData[property]);
    logger.info("original processData." + property + " = " + processData[property] + " (type = " + (typeof processData[property]) + ")");
    processData[property] = newValue;
    assertIsInstance(property, type, processData[property]);
    logger.info("updated processData." + property + " = " + processData[property] + " (type = " + (typeof processData[property]) + ")");
}

function assertIsInstance(property, type, value) {
    if (typeof type == 'function') {
        assert(property + " cannot be undefined", value !== undefined);
        assert(property + " is not an object", typeof value === 'object');
        assert(property + " is not an instance of " + type, value instanceof type);
    } else if (typeof type == 'string') {
        assert(property + " cannot be null", value !== null);
        assert(property + " is not a " + type, typeof value === type);
    }
}

function assert(msg, b) {
    if (!b)
        throw new Error(msg);
}

/*==============================================================
END USER SCRIPT EXECUTION
================================================================*/
