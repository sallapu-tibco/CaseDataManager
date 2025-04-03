function createProcessData(form, jsonData) {
    // Can pass a JavaScript object...
/*
    var jsonData = {
        "$type": "ProcessData",
        "booleanParam": true,
        "dateParam": "2019-05-09",
        "numberParam": 666,
        "textParam": "Get me this value",
//        "objectParam": "{\"$type\": \"com.tibco.ace.datainit.test.Pet\", \"name\": \"Rex\", \"type\": \"dog\"}"
        "objectParam": {"$type": "com.tibco.ace.datainit.test.Pet", "name": "Rex", "type": "dog"}
    };
*/
    // ... or a JSON string
    var jsonData = "{\n" +
        "\"$type\": \"ProcessData\",\n" +
        "\"booleanParam\": true,\n" +
        "\"dateParam\": \"2019-05-09\",\n" +
        "\"numberParam\": 666,\n" +
        "\"textParam\": \"Get me this value\",\n" +
        "\"objectParam\": {\"$type\": \"com.tibco.ace.datainit.test.Pet\", \"name\": \"Rex\", \"type\": \"dog\"}\n" +
    "}";
    var processData = tibco.ScriptUtil.parseData(jsonData);
    return processData;
};
