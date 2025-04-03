{
  "namespace" : "com.example.testcasedatascripting",
  "formatVersion" : 2,
  "structuredTypes" : [ {
    "name" : "Customer",
    "label" : "Customer",
    "isCase" : true,
    "stateModel" : {
      "states" : [ {
        "label" : "High Activity",
        "value" : "HIGHACTIVITY"
      }, {
        "label" : "Medium Activity",
        "value" : "MEDIUMACTIVITY"
      }, {
        "label" : "Low Activity",
        "value" : "LOWACTIVITY"
      }, {
        "label" : "Terminated",
        "value" : "TERMINATED",
        "isTerminal" : true
      } ]
    },
    "attributes" : [ {
      "name" : "customerID",
      "label" : "Customer ID",
      "type" : "base:Text",
      "isIdentifier" : true,
      "isSearchable" : true,
      "isSummary" : true,
      "constraints" : [ {
        "name" : "length",
        "value" : "10"
      } ],
      "isMandatory" : true,
	  "defaultValue" : "Customer01"	  
    }, {
      "name" : "customerCategory",
      "label" : "Customer Category",
      "type" : "base:Text",
      "isSearchable" : true,
      "isState" : true,
      "isSummary" : true,
      "isMandatory" : true,
      "defaultValue" : "LOWACTIVITY"
    }, {
      "name" : "dateofBirth",
      "label" : "Date of Birth",
      "type" : "base:Date",
      "isSearchable" : true,
      "isSummary" : true,
      "defaultValue": "1993-08-01"
    }, {
      "name" : "age",
      "label" : "Age",
      "type" : "base:FixedPointNumber",
      "isSearchable" : true,
      "isSummary" : true,
      "constraints" : [ {
        "name" : "length",
        "value" : "5"
      }, {
        "name" : "decimalPlaces",
        "value" : "2"
      }, {
        "name" : "minValue",
        "value" : "0.00"
      }, {
        "name" : "minValueInclusive",
        "value" : "false"
      }, {
        "name" : "maxValue",
        "value" : "125.00"
      }, {
        "name" : "maxValueInclusive",
        "value" : "true"
      } ],
      "defaultValue" : "26.25"
    }, {
      "name" : "totalIncome",
      "label" : "Total Income",
      "type" : "base:Number",
      "isSearchable" : true,
      "isSummary" : true,
      "constraints" : [ {
        "name" : "minValue",
        "value" : "0.1"
      }, {
        "name" : "minValueInclusive",
        "value" : "true"
      }, {
        "name" : "maxValue",
        "value" : "999999999.99"
      }, {
        "name" : "maxValueInclusive",
        "value" : "true"
      } ],
      "defaultValue" : "40000"
    }, {
      "name" : "residentialAddress",
      "label" : "Residential Address",
      "type" : "Address",
      "isMandatory" : true
    }, {
      "name" : "employmentDetails",
      "label" : "Employment Details",
      "type" : "com.example.testdata_outside_global.EmploymentDetails",
      "isArray" : true
    } ]
  }, {
    "name" : "Account",
    "label" : "Account",
    "isCase" : true,
    "stateModel" : {
      "states" : [ {
        "label" : "Active",
        "value" : "ACTIVE"
      }, {
        "label" : "Dormant",
        "value" : "DORMANT"
      }, {
        "label" : "To be Terminated",
        "value" : "TOBETERMINATED"
      }, {
        "label" : "Barred",
        "value" : "BARRED"
      } ]
    },
    "attributes" : [ {
      "name" : "accountId",
      "label" : "Account Id",
      "type" : "base:Text",
      "isIdentifier" : true,
      "isSearchable" : true,
      "isSummary" : true,
      "isMandatory" : true
    }, {
      "name" : "accountStatus",
      "label" : "Account Status",
      "type" : "base:Text",
      "isSearchable" : true,
      "isState" : true,
      "isSummary" : true,
      "isMandatory" : true,
      "defaultValue":"ACTIVE"
    }, {
      "name" : "accountOpened",
      "label" : "Account Opened",
      "type" : "base:DateTimeTZ",
      "isSearchable" : true,
      "defaultValue": "1999-12-31T23:59:59.999Z"
    }, {
      "name" : "accountLastAccessed",
      "label" : "Account Last Accessed",
      "type" : "base:DateTimeTZ",
      "isSearchable" : true,
      "isSummary" : true,
      "defaultValue": "2019-01-01T01:02:03.004Z"
    }, {
      "name" : "accountBalance",
      "label" : "Account Balance",
      "type" : "base:Number",
      "isSearchable" : true,
      "isSummary" : true,
      "defaultValue" : "3000"
    }, {
      "name" : "accountInstitution",
      "label" : "Account Institution",
      "type" : "base:Text",
      "isSearchable" : true,
      "isSummary" : true,
      "constraints" : [ {
        "name" : "length",
        "value" : "100"
      } ],
      "defaultValue" : "Default Institution"
    }, {
      "name" : "institutionAddress",
      "label" : "Institution Address",
      "type" : "Address"
    }, {
      "name" : "accountType",
      "label" : "Account Type",
      "type" : "base:Text",
      "isSearchable" : true,
      "isSummary" : true,
      "allowedValues" : [ {
        "label" : "Savings",
        "value" : "SAVINGS"
      }, {
        "label" : "Current",
        "value" : "CURRENT"
      }, {
        "label" : "Fixed Deposit",
        "value" : "FIXEDDEPOSIT"
      }, {
        "label" : "Investment",
        "value" : "INVESTMENT"
      } ],
      "constraints" : [ {
        "name" : "length",
        "value" : "400"
      } ],
      "defaultValue" : "INVESTMENT"
    } ],
    "identifierInitialisationInfo" : {
      "prefix" : "Account",
      "minNumLength" : 5
    }
  }, {
    "name" : "Address",
    "label" : "Address",
    "attributes" : [ {
      "name" : "firstLine",
      "label" : "First Line",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "defaultValue" : "Default First Line"
    }, {
      "name" : "secondLine",
      "label" : "Second Line",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "defaultValue" : "Default Second Line"
    }, {
      "name" : "city",
      "label" : "City",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "defaultValue" : "Default City"
    }, {
      "name" : "county",
      "label" : "County",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "defaultValue" : "Default County"
    }, {
      "name" : "country",
      "label" : "Country",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "50"
      } ],
      "defaultValue" : "Default Country"
    }, {
      "name" : "postcode",
      "label" : "Postcode",
      "type" : "base:Text",
      "constraints" : [ {
        "name" : "length",
        "value" : "10"
      } ],
      "defaultValue" : "DEFAULT"
    } ]
  } ],
  "links" : [ {
    "end1" : {
      "name" : "customer",
      "label" : "Customer",
      "owner" : "Account"
    },
    "end2" : {
      "name" : "accounts",
      "label" : "Accounts",
      "isArray" : true,
      "owner" : "Customer"
    }
  } ]
}