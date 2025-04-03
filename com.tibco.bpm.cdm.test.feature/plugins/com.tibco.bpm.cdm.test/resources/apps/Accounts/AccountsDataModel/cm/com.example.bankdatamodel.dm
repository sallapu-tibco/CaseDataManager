{  
   "namespace":"com.example.bankdatamodel",
   "formatVersion":2,
   "structuredTypes":[  
      {  
         "name":"Customer",
         "label":"Customer",
         "identifierInitialisationInfo":{  
            "minNumLength":5,
            "prefix":"WINTERFELL-"
         },
         "description":"Details of a customer",
         "isCase":true,
         "stateModel":{  
            "states":[  
               {  
                  "label":"VERY ACTIVE - (50K ABOVE)",
                  "value":"VERYACTIVE50KABOVE"
               },
               {  
                  "label":"ACTIVE - (10K TO 50K)",
                  "value":"ACTIVE10KTO50K"
               },
               {  
                  "label":"REGULAR - (1K TO 10K)",
                  "value":"REGULAR1KTO10K"
               },
               {  
                  "label":"SELDOM - (0 TO 1K)",
                  "value":"SELDOM0TO1K"
               },
               {  
                  "label":"ACTIVE BUT ON HOLD",
                  "value":"ACTIVEBUTONHOLD"
               },
               {  
                  "label":"INACTIVE",
                  "value":"INACTIVE"
               },
               {  
                  "label":"BARRED OR TERMINATED",
                  "value":"BARREDORTERMINATED",
                  "isTerminal":true
               },
               {  
                  "label":"CANCELLED",
                  "value":"CANCELLED",
                  "isTerminal":true
               },
               {  
                  "label":"TRIAL",
                  "value":"TRIAL"
               }
            ]
         },
         "attributes":[  
            {  
               "name":"customerCID",
               "label":"Customer CID",
               "type":"base:Text",
               "isSummary":true,
               "description":"This is customer's unique id",
               "isMandatory":true,
               "isSearchable":true,
               "isIdentifier":true
            },
            {  
               "name":"state",
               "label":"State",
               "type":"base:Text",
               "isState":true,
               "isSearchable":true,
               "isSummary":true,
               "isMandatory":true,
               "defaultValue":"TRIAL"
            },
            {  
               "name":"personalDetails",
               "label":"Personal Details",
               "type":"PersonalDetails",
               "isMandatory":true
            },
            {  
               "name":"professionalDetails",
               "label":"Professional Details",
               "type":"ProfessionalDetails",
               "isArray":true
            }
         ]
      },
      {  
         "name":"Account",
         "label":"Account",
         "isCase":true,
         "stateModel":{  
            "states":[  
               {  
                  "label":"@ACTIVE",
                  "value":"@ACTIVE&"
               },
               {  
                  "label":"@FROZEN",
                  "value":"@FROZEN&"
               },
               {  
                  "label":"@TERMINATED",
                  "value":"@TERMINATED&",
                  "isTerminal":true
               },
               {  
                  "label":"@WAITING FOR APPROVAL",
                  "value":"@WAITING FOR APPROVAL&"
               },
               {  
                  "label":"@CANCELLED",
                  "value":"@CANCELLED&",
                  "isTerminal":true
               },
               {  
                  "label":"@WAITING FOR CANCELLATION",
                  "value":"@WAITING FOR CANCELLATION&"
               },
               {  
                  "label":"@TREMPORARILY DEACTIVATED",
                  "value":"@TREMPORARILY DEACTIVATED&"
               },
               {  
                  "label":"@REINSTATED",
                  "value":"@REINSTATED&"
               }
            ]
         },
         "attributes":[  
            {  
               "name":"accountID",
               "label":"Account ID",
               "type":"base:Number",
               "isMandatory":true,
               "isSearchable":true,
               "isSummary":true,
               "isIdentifier":true,
               "defaultValue":"999999",
               "_comment":"type number search in v1"
            },
            {  
               "name":"accountType",
               "label":"Account Type",
               "type":"base:Text",
               "allowedValues":[  
                  {  
                     "label":"SAVING",
                     "value":"SAVING"
                  },
                  {  
                     "label":"MORTGAGE",
                     "value":"MORTGAGE"
                  },
                  {  
                     "label":"CURRENT",
                     "value":"CURRENT"
                  },
                  {  
                     "label":"FIXED DEPOSIT",
                     "value":"FIXEDDEPOSIT"
                  },
                  {  
                     "label":"CASH ISA",
                     "value":"CASHISA"
                  },
                  {  
                     "label":"INSURANCE",
                     "value":"INSURANCE"
                  }
               ],
               "isSearchable":true,
               "isSummary":true,
               "defaultValue":"CURRENT",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"400"
                  }
               ],
               "_comment":"type text search in v1"
            },
            {  
               "name":"institutionDetails",
               "label":"Institution Details",
               "type":"InstitutionDetails"
            },
            {  
               "name":"dateofAccountOpening",
               "label":"Date of Account Opening",
               "type":"base:Date",
               "isSearchable":true,
               "isSummary":true,
               "defaultValue":"2019-04-26",
               "_comment":"type date search in v1"
            },
            {  
               "name":"dateofAccountClosing",
               "label":"Date of Account Closing",
               "type":"base:Date",
               "defaultValue":"2079-11-08"
            },
            {  
               "name":"timeofAccountOpening",
               "label":"Time of Account Opening",
               "type":"base:Time",
               "isSearchable":true,
               "isSummary":true,
               "defaultValue":"17:24:45",
               "_comment":"type time search in v1"
            },
            {  
               "name":"timeofAccountClosing",
               "label":"Time of Account Closing",
               "type":"base:Time",
               "defaultValue":"02:25:31"
            },
            {  
               "name":"state",
               "label":"State",
               "type":"base:Text",
               "isState":true,
               "isSearchable":true,
               "isSummary":true,
               "isMandatory":true,
               "defaultValue":"@ACTIVE&",
               "_comment":"type text(state) search in v1"
            },
            {  
               "name":"accountLiabilityHolding",
               "label":"Account Liability / Holding",
               "type":"base:FixedPointNumber",
               "defaultValue":"991212.11",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"14"
                  },
                  {  
                     "name":"decimalPlaces",
                     "value":"2"
                  },
                  {  
                     "name":"maxValue",
                     "value":"999999999.99"
                  },
                  {  
                     "name":"minValue",
                     "value":"-999999999999.99"
                  },
                  {  
                     "name":"maxValueInclusive",
                     "value":"true"
                  },
                  {  
                     "name":"minValueInclusive",
                     "value":"false"
                  }
               ]
            }
         ]
      },
      {  
         "name":"PersonalDetails",
         "label":"Personal Details",
         "attributes":[  
            {  
               "name":"salutation",
               "label":"Salutation",
               "type":"base:Text",
               "allowedValues":[  
                  {  
                     "label":"MR.",
                     "value":"MR"
                  },
                  {  
                     "label":"MRS.",
                     "value":"MRS"
                  },
                  {  
                     "label":"MS.",
                     "value":"MS"
                  },
                  {  
                     "label":"DR.",
                     "value":"DR"
                  },
                  {  
                     "label":"JUSTICE",
                     "value":"JUSTICE"
                  },
                  {  
                     "label":"LADY",
                     "value":"LADY"
                  }
               ],
               "defaultValue":"LADY"
            },
            {  
               "name":"firstName",
               "label":"First Name",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"Sansa"
            },
            {  
               "name":"lastName",
               "label":"Last Name",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"Stark"
            },
            {  
               "name":"gender",
               "label":"Gender",
               "type":"base:Text",
               "allowedValues":[  
                  {  
                     "label":"MALE",
                     "value":"MALE"
                  },
                  {  
                     "label":"FEMALE",
                     "value":"FEMALE"
                  },
                  {  
                     "label":"PREFER NOT TO SAY",
                     "value":"PREFER NOT TO SAY"
                  }
               ],
               "defaultValue":"FEMALE"
            },
            {  
               "name":"dateofBirth",
               "label":"Date of Birth",
               "type":"base:Date",
               "defaultValue":"1992-11-01"
            },
            {  
               "name":"age",
               "label":"Age",
               "type":"base:Number",
               "defaultValue":"25"
            },
            {  
               "name":"homeAddress",
               "label":"Home Address",
               "type":"Address",
               "isMandatory":true
            },
            {  
               "name":"numberofYearsStayingattheAddress",
               "label":"Number of Years Staying at the Address ",
               "type":"base:FixedPointNumber",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"4"
                  },
                  {  
                     "name":"minValue",
                     "value":"0.1"
                  },
                  {  
                     "name":"minValueInclusive",
                     "value":"true"
                  },
                  {  
                     "name":"maxValue",
                     "value":"999.9"
                  },
                  {  
                     "name":"maxValueInclusive",
                     "value":"false"
                  },
                  {  
                     "name":"decimalPlaces",
                     "value":"1"
                  }
               ],
               "defaultValue":"1.0"
            }
         ]
      },
      {  
         "name":"ProfessionalDetails",
         "label":"Professional Details",
         "attributes":[  
            {  
               "name":"typeofEmployement",
               "label":"Type of Employement",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"Self-Employed"
            },
            {  
               "name":"placeofWork",
               "label":"Place of Work",
               "type":"Address"
            },
            {  
               "name":"annualIncomeSalaryBeforeTaxes",
               "label":"Annual Income / Salary Before Taxes",
               "type":"base:FixedPointNumber",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"10"
                  }
               ],
               "defaultValue":"1234.56"
            },
            {  
               "name":"dateofJoining",
               "label":"Date of Joining",
               "type":"base:Date",
               "defaultValue":"2017-12-31"
            }
         ]
      },
      {  
         "name":"Address",
         "label":"Address",
         "attributes":[  
            {  
               "name":"firstLine",
               "label":"First Line",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"467/7A, Delight Colony"
            },
            {  
               "name":"secondLine",
               "label":"Second Line",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"Sadar-Bazar"
            },
            {  
               "name":"city",
               "label":"City",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"Satara"
            },
            {  
               "name":"county",
               "label":"County",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"MAHARASHTRA"
            },
            {  
               "name":"country",
               "label":"Country",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"INDIA"
            },
            {  
               "name":"postCode",
               "label":"Post Code",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ],
               "defaultValue":"415001"
            }
         ]
      },
      {  
         "name":"InstitutionDetails",
         "label":"Institution Details",
         "attributes":[  
            {  
               "name":"institutionCode",
               "label":"Institution Code",
               "type":"base:Text",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"100"
                  }
               ],
               "isMandatory":true,
               "defaultValue":"ICIC64004"
            },
            {  
               "name":"nameoftheInstitution",
               "label":"Name of the Institution",
               "type":"base:Text",
               "defaultValue":"ICICI BANK Plc",
               "constraints":[  
                  {  
                     "name":"length",
                     "value":"50"
                  }
               ]
            },
            {  
               "name":"institutionBranchAddress",
               "label":"Institution Branch Address",
               "type":"Address"
            }
         ]
      }
   ],
   "links":[  
      {  
         "id":"248163264",
         "end1":{  
            "owner":"Customer",
            "name":"holdstheaccounts",
            "label":"Holds The Accounts",
			"isArray":true
         },
         "end2":{  
            "owner":"Account",
            "name":"heldby",
            "label":"Held By the"
         }
      },

      {  
         "id":"248163265",
         "end1":{  
            "owner":"Customer",
            "name":"social_circle_of",
            "label":"social circle of",
			"isArray":true
         },
         "end2":{  
            "owner":"Customer",
            "name":"socially_mingle_with",
            "label":"socially_mingle_with",
			"isArray":true
         }
      },

      {  
         "id":"248163266",
         "end1":{  
            "owner":"Account",
            "name":"children_of_the_accounts",
            "label":"children of the accounts",
			"isArray":true
         },
         "end2":{  
            "owner":"Account",
            "name":"parent_account",
            "label":"parent account"
         }
       }
   ]
}