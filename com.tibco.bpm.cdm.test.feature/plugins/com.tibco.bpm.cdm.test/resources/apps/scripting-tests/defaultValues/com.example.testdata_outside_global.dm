{
  "namespace" : "com.example.testdata_outside_global",
  "formatVersion" : 2,
  "structuredTypes" : [ {
    "name" : "EmploymentDetails",
    "label" : "Employment Details",
    "attributes" : [ {
      "name" : "numberofyears",
      "label" : "Number of years",
      "type" : "base:FixedPointNumber",
      "constraints" : [ {
        "name" : "length",
        "value" : "2"
      }, {
        "name" : "decimalPlaces",
        "value" : "0"
      } ],
      "defaultValue" : "2"
    }, {
      "name" : "yearlyRenumeration",
      "label" : "Yearly Renumeration",
      "type" : "base:Number",
      "defaultValue" : "15000.60"
    }, {
      "name" : "employmentStatus",
      "label" : "Employment Status",
      "type" : "base:Text",
      "allowedValues" : [ {
        "label" : "Self Employed",
        "value" : "SELFEMPLOYED"
      }, {
        "label" : "Full Time Salaried",
        "value" : "FULLTIMESALARIED"
      }, {
        "label" : "Part Time Salaried",
        "value" : "PARTTIMESALARIED"
      }, {
        "label" : "Retired",
        "value" : "RETIRED"
      }, {
        "label" : "On Vacation",
        "value" : "ONVACATION"
      }, {
        "label" : "Resigned",
        "value" : "RESIGNED"
      } ],
      "isMandatory" : true,
      "defaultValue" : "FULLTIMESALARIED"
    }, {
      "name" : "employeeID",
      "label" : "Employee ID",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "isMandatory" : true,
      "defaultValue" : "DEFAULT EMP ID"
    } ]
  } ]
}