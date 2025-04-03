{
	"namespace": "com.mybigcompany.commonassets",
	"formatVersion": 2,
	"structuredTypes": [{
	    "identifierInitialisationInfo": {
    	  "minNumLength": 15,
    	  "prefix": "BAN-",
    	  "suffix": "-ANA"
    	},
		"name": "Banana",
		"label": "Banana",
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
			"name": "colour",
			"label": "Colour",
			"type": "base:Text",
			"isSummary": true,
			"isSearchable": true,
			"constraints":[{
				"name": "length",
				"value": "400"
			}]
		}, {
			"name": "state",
			"label": "State",
			"type": "base:Text",
			"isState": true,
			"isSearchable": true,
			"isSummary": true,
			"isMandatory": true
		}, {
			"name": "fruitCode",
			"label": "Fruit Code",
			"type": "base:Text",
			"isIdentifier": true,
			"isSearchable": true,
			"isSummary": true,
			"isMandatory": true
		}]
	}, {
		"name": "Dimensions",
		"label": "Dimensions",
		"attributes": [{
				"name": "width",
				"label": "Width",
				"type": "base:Number"
			},
			{
				"name": "height",
				"label": "Height",
				"type": "base:Number"
			}
		]
	}]
}