{
  "namespace" : "com.crossproj.projx.modelx1",
  "formatVersion" : 2,
  "structuredTypes" : [ {
    "name" : "CaseX1",
    "label" : "CaseX1",
    "isCase" : true,
    "stateModel" : {
      "states" : [ {
        "label" : "Case Created",
        "value" : "CASECREATED - X1"
      }, {
        "label" : "Case Completed",
        "value" : "CASECOMPLETED -X1",
	"isTerminal" : true
      }, {
        "label" : "Case &^%$ Halted",
        "value" : "CASEHALTED-X1",
	"isTerminal" : false
      } ]
    },
    "attributes" : [ {
      "name" : "cid",
      "label" : "cid",
      "type" : "base:Text",
      "isIdentifier" : true,
      "isSearchable" : true,
      "isSummary" : true,
      "constraints" : [ {
        "name" : "length",
        "value" : "151"
      } ],
      "isMandatory" : true
    }, {
      "name" : "state",
      "label" : "state",
      "type" : "base:Text",
      "isSearchable" : true,
      "isState" : true,
      "isSummary" : true,
      "isMandatory" : true
    }, {
      "name" : "a2cross",
      "label" : "a2cross",
      "type" : "com.crossproj.proja.modela2.ClassA2"
    }, {
      "name" : "b1cross",
      "label" : "b1cross",
      "type" : "com.crossproj.projb.modelb1.ClassB1",
      "isArray" : true
    }, {
      "name" : "c1cross",
      "label" : "c1cross",
      "type" : "com.crossproj.projc.modelc1.ClassC1"
    }, {
      "name" : "d2cross",
      "label" : "d2cross",
      "type" : "com.crossproj.projd.modeld2.ClassD2",
      "isArray" : true,
      "isMandatory" : true
    }]
  } ]
}