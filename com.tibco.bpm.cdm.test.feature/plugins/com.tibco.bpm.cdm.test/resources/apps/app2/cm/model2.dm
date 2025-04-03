{
  "namespace" : "com.mybigcompany.otherstuff",
  "formatVersion" : 2,
  "structuredTypes" : [ {
    "name" : "NoodleSoup",
    "label" : "Noodle Soup",
    "description": "Convenient lunchtime snack",
    "isCase" : true,
    "identifierInitialisationInfo": {
      "minNumLength": 3
    },
    "stateModel" : {
      "states" : [ {
        "label" : "Created",
        "value" : "CREATED"
      }, {
        "label" : "Cancelled",
        "value" : "CANCELLED",
        "isTerminal" : true
      } ]
    },
    "attributes" : [ {
      "name" : "name",
      "label" : "Name",
      "type" : "base:Text",
      "isSummary": true,
      "description": "What it's called",
      "defaultValue": "Shin Cup"
    }, {
      "name" : "state",
      "label" : "State",
      "type" : "base:Text",
      "isState" : true,
      "isSearchable": true,
      "isSummary": true,
      "isMandatory": true
    }, {
      "name": "potCode",
      "label": "Pot Code",
      "isIdentifier": true,
      "type": "base:Text",
      "isSummary": true,
      "isSearchable": true,
      "isMandatory": true,
      "constraints":[{
        "name": "length",
        "value": "256"
      }]      
    }, {
      "name": "isTasty",
      "label": "Is Tasty",
      "description": "True if bursting with flavour; False if bland and uninspiring",
      "type": "base:Boolean",
      "defaultValue": "true"
    }, {
      "name": "size",
      "label": "Size",
      "type": "com.mybigcompany.commonassets.Dimensions"
    } ]
  } ]    
}