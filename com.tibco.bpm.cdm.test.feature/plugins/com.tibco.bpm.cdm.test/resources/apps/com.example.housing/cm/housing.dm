{
	"namespace": "com.example.housing",
	"formatVersion": 2,
	"structuredTypes": [{
		"name": "House",
		"label": "House",
		"isCase": true,
		"stateModel": {
			"states": [{
				"label": "Built",
				"value": "BUILT"
			}, {
				"label": "Demolished",
				"value": "DEMOLISHED",
				"isTerminal": true
			}]
		},
		"attributes": [{
			"name": "name",
			"label": "name",
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
			"name": "state",
			"label": "State",
			"type": "base:Text",
			"isState": true,
			"isSearchable": true,
			"isSummary": true,
			"isMandatory": true,
			"defaultValue": "BUILT"
		}, {
			"name": "address",
			"label": "Address",
			"type": "org.policycorporation.Address",
			"description": "The location where the house physically resides - using a type defined in the com.example.ProjectX project!"
		}, {
			"name": "alternativeAddress",
			"label": "Alternative Address",
			"type": "org.policycorporation.Address",
			"description": "Another address that refers to the same location"
		}, {
			"name": "claims",
			"label": "Claims",
			"type": "org.policycorporation.Claim",
			"isArray": true,
			"description": "A bunch of Claims on the buildings insurance"
		}]
	}]
}