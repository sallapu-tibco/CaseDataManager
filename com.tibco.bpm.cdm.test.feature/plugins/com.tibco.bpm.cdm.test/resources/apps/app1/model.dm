{
	"namespace": "org.policycorporation",
	"formatVersion": 2,
	"structuredTypes": [{
		"name": "Policy",
		"label": "Policy",
		"isCase": true,
		"stateModel": {
			"states": [{
				"label": "Created",
				"value": "CREATED"
			}, {
				"label": "Cancelled",
				"value": "CANCELLED",
				"isTerminal": true
			}]
		},
		"attributes": [{
			"name": "number",
			"label": "Number",
			"type": "base:Number",
			"isMandatory": true,
			"isIdentifier": true,
			"isSummary": true,
			"isSearchable": true
		}, {
			"name": "state",
			"label": "State",
			"type": "base:Text",
			"isState": true,
			"isSearchable": true,
			"isSummary": true,
			"isMandatory": true,
			"defaultValue": "CREATED"
		}, {
			"name": "policyStartDate",
			"label": "Policy Start Date",
			"description": "Date on which the policy starts",
			"type": "base:Date",
			"isSearchable": true,
			"isSummary": true,
			"defaultValue": "2016-12-25"
		}, {
			"name": "policyStartTime",
			"label": "Policy Start Time",
			"type": "base:Time",
			"defaultValue": "00:01:02"
		}, {
			"name": "expiresAt",
			"label": "Expires At",
			"type": "base:DateTimeTZ",
			"isSearchable": true,
			"isSummary": true,
			"defaultValue": "2099-12-31T23:59:59.999Z"
		}, {
			"name": "premium",
			"label": "Premium",
			"type": "base:FixedPointNumber",
			"defaultValue": "2000",
			"isSearchable": true,
			"isSummary": true,
			"constraints": [{
				"name": "decimalPlaces",
				"value": "2"
			}, {
				"name": "length",
				"value": "14"
			}, {
				"name": "minValue",
				"value": "0"
			}, {
				"name": "maxValueInclusive",
				"value": "true"
			}, {
				"name": "maxValue",
				"value": "2000"
			}]
		}, {
			"name": "noClaimsYears",
			"label": "No Claims Years",
			"type": "base:FixedPointNumber",
			"isSearchable": true,
			"isSummary": true,
			"constraints": [{
				"name": "minValue",
				"value": "0"
			}, {
				"name": "maxValue",
				"value": "99"
			}, {
				"name": "decimalPlaces",
				"value": "0"
			}]
		}, {
			"name": "termsAndConditions",
			"label": "Terms and Conditions",
			"type": "base:URI",
			"defaultValue": "http://terms.example.com/terms.html"
		}, {
			"name": "comments",
			"label": "Comments",
			"type": "base:Text",
			"isSearchable": true,
			"isSummary": true,
			"constraints": [{
				"name": "length",
				"value": "400"
			}]
		}, {
			"name": "claims",
			"label": "Claims",
			"type": "Claim",
			"isArray": true
		}, {
			"name": "legalCover",
			"label": "Legal Cover",
			"type": "base:Boolean"
		}]
	}, {
		"name": "Person",
		"label": "Person",
		"description": "A human",
		"isCase": true,
		"identifierInitialisationInfo": {
			"prefix": "HUMAN-",
			"suffix": "X",
			"minNumLength": 8
		},
		"stateModel": {
			"states": [{
				"label": "Alive",
				"value": "ALIVE"
			}, {
				"label": "Dead",
				"value": "DEAD",
				"isTerminal": true
			}]
		},
		"attributes": [{
			"name": "pcode",
			"label": "P Code",
			"type": "base:Text",
			"isMandatory": true,
			"isIdentifier": true,
			"isSummary": true,
			"isSearchable": true,
			"constraints": [{
				"name": "length",
				"value": "256"
			}]
		}, {
			"name": "name",
			"label": "Name",
			"type": "base:Text"
		}, {
			"name": "aliases",
			"label": "Aliases",
			"type": "base:Text",
			"isArray": true
		}, {
			"name": "age",
			"label": "Age",
			"type": "base:Number",
			"isSearchable": true,
			"constraints": [{
				"name": "maxValue",
				"value": "120"
			}]
		}, {
			"name": "lotteryNumbers",
			"label": "Lottery Numbers",
			"type": "base:Number",
			"isArray": true,
			"constraints": [{
				"name": "maxValue",
				"value": "49"
			}, {
				"name": "minValue",
				"value": "1"
			}, {
				"name": "minValueInclusive",
				"value": "true"
			}, {
				"name": "maxValueInclusive",
				"value": "true"
			}]
		}, {
			"name": "dateOfBirth",
			"label": "Date of Birth",
			"type": "base:Date"
		}, {
			"name": "timeOfBirth",
			"label": "Time of Birth",
			"type": "base:Time",
			"defaultValue": "00:01:02",
			"isSummary": true,
			"isSearchable": true
		}, {
			"name": "homeAddress",
			"label": "Home Address",
			"type": "Address"
		}, {
			"name": "workAddress",
			"label": "Work Address",
			"type": "Address"
		}, {
			"name": "otherAddresses",
			"label": "Other Addresses",
			"type": "Address",
			"isArray": true
		}, {
			"name": "personState",
			"label": "Person State",
			"type": "base:Text",
			"isState": true,
			"isSearchable": true,
			"isSummary": true,
			"isMandatory": true
		}]
	}, {
		"name": "Address",
		"label": "Address",
		"attributes": [{
			"name": "firstLine",
			"label": "First Line",
			"type": "base:Text"
		}, {
			"name": "secondLine",
			"label": "Second Line",
			"type": "base:Text"
		}]
	}, {
		"name": "Claim",
		"label": "Claim",
		"attributes": [{
			"name": "date",
			"label": "Date",
			"type": "base:Date"
		}, {
			"name": "blame",
			"label": "Blame",
			"type": "base:Text",
			"defaultValue": "POLICYHOLDER",
			"allowedValues": [{
				"label": "Policyholder",
				"value": "POLICYHOLDER"
			}, {
				"label": "Third Party",
				"value": "THIRD_PARTY"
			}, {
				"label": "Act of God",
				"value": "ACT_OF_GOD"
			}, {
				"label": "Unknown",
				"value": "UNKNOWN"
			}]
		}, {
			"name": "description",
			"label": "Description",
			"type": "base:Text",
			"isArray": true
		}]
	}],
	"links": [{
		"_comment": "One-to-many. A person can have many policies, but each policy can only have a single policyholder",
		"end1": {
			"owner": "Person",
			"name": "policies",
			"label": "Policies",
			"isArray": true
		},
		"end2": {
			"owner": "Policy",
			"name": "holder",
			"label": "Holder"
		}
	}, {
		"_comment": "Many-to-many. A person can be a named driver on many policies and each policy can have many named drivers",
		"end1": {
			"owner": "Person",
			"name": "allowedToDrive",
			"label": "Allowed To Drive",
			"isArray": true
		},
		"end2": {
			"owner": "Policy",
			"name": "namedDrivers",
			"label": "Named Drivers",
			"isArray": true
		}
	}, {
		"_comment": "Many-to-many between cases of same type. A person can have many children and be a child of many parents (well, 2 if we're being accurate, but we don't support that)",
		"end1": {
			"owner": "Person",
			"name": "isParentOf",
			"label": "Is Parent Of",
			"isArray": true
		},
		"end2": {
			"owner": "Person",
			"name": "isChildOf",
			"label": "Is Child Of",
			"isArray": true
		}
	}, {
		"_comment": "Many-to-one. Each Person can have a single favourite Policy and and a given Policy may be many people's favourite",
		"end1": {
			"owner": "Person",
			"name": "favouritePolicy",
			"label": "Favourite Policy"
		},
		"end2": {
			"owner": "Policy",
			"name": "isTheFavouriteOf",
			"label": "Is The Favourite Of",
			"isArray": true
		}		
	}]
}