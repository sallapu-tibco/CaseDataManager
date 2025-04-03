{
   "namespace":"com.crossproj.projy.modely1",
   "formatVersion":2,
   "structuredTypes":[
      {
         "name":"CaseY1",
         "label":"CaseY1",
         "isCase":true,
         "stateModel":{
            "states":[
               {
                  "label":"Case Created",
                  "value":"CASECREATED - Y1"
               },
               {
                  "label":"Case Completed",
                  "value":"CASECOMPLETED -Y1",
                  "isTerminal":true
               },
               {
                  "label":"Case Cancelled V2",
                  "value":"CASECANCELLED -Y1V2",
                  "isTerminal":true
               }
            ]
         },
         "attributes":[
            {
               "name":"cid",
               "label":"cid",
               "type":"base:Text",
               "isIdentifier":true,
               "isSearchable":true,
               "isSummary":true,
               "constraints":[
                  {
                     "name":"length",
                     "value":"50"
                  }
               ],
               "isMandatory":true
            },
            {
               "name":"datev2",
               "label":"datev2",
               "type":"base:Date",
               "isSummary":true
            },
            {
               "name":"state",
               "label":"state",
               "type":"base:Text",
               "isSearchable":true,
               "isState":true,
               "isSummary":true,
               "isMandatory":true
            },
            {
               "name":"a2cross",
               "label":"a2cross",
               "type":"com.crossproj.proja.modela2.ClassA2"
            },
            {
               "name":"b1cross",
               "label":"b1cross",
               "type":"com.crossproj.projb.modelb1.ClassB1"
            },
            {
               "name":"c1cross",
               "label":"c1cross",
               "type":"com.crossproj.projc.modelc1.ClassC1"
            },
            {
               "name":"d2cross",
               "label":"d2cross",
               "type":"com.crossproj.projd.modeld2.ClassD2"
            },
            {
               "name":"x2cross",
               "label":"x2cross",
               "type":"com.crossproj.projx.modelx2.ClassX2",
               "isArray":true
            }
         ]
      },
      {
         "name":"CaseY11",
         "label":"CaseY11",
         "isCase":true,
         "stateModel":{
            "states":[
               {
                  "label":"Case Created",
                  "value":"CASECREATED - Y11"
               },
               {
                  "label":"Case Completed",
                  "value":"CASECOMPLETED - Y11",
                  "isTerminal":true
               },
               {
                  "label":"Case Terminated",
                  "value":"CASETERMINATED - Y11",
                  "isTerminal":true
               }
            ]
         },
         "attributes":[
            {
               "name":"cidy1",
               "label":"cidy1",
               "type":"base:Text",
               "isIdentifier":true,
               "isSearchable":true,
               "isSummary":true,
               "constraints":[
                  {
                     "name":"length",
                     "value":"51"
                  }
               ],
               "isMandatory":true
            },
            {
               "name":"state",
               "label":"state",
               "type":"base:Text",
               "isSearchable":true,
               "isState":true,
               "isSummary":true,
               "isMandatory":true
            }
         ]
      },
      {
         "name":"CaseY22",
         "label":"CaseY22",
         "isCase":true,
         "stateModel":{
            "states":[
               {
                  "label":"Created",
                  "value":"CASECREATED - Y22"
               },
               {
                  "label":"Completed",
                  "value":"CASECOMPLETED - Y22",
                  "isTerminal":false
               },
               {
                  "label":"Intermediate",
                  "value":"CASEINPROGRESS - Y22",
                  "isTerminal":true
               }
            ]
         },
         "attributes":[
            {
               "name":"cidy2",
               "label":"cidy2",
               "type":"base:Text",
               "isIdentifier":true,
               "isSearchable":true,
               "isSummary":true,
               "constraints":[
                  {
                     "name":"length",
                     "value":"10"
                  }
               ],
               "isMandatory":true
            },
            {
               "name":"state",
               "label":"state",
               "type":"base:Text",
               "isSearchable":true,
               "isState":true,
               "isSummary":true,
               "isMandatory":true
            }
         ]
      }
   ],
   "links":[
      {
         "id":"248163264",
         "_comment":"link has been changed in V2 : Many to Many",
         "end1":{
            "owner":"CaseY1",
            "name":"refersthecase",
            "label":"Refers the cases",
            "isArray":true
         },
         "end2":{
            "owner":"CaseY11",
            "name":"referredbythecases",
            "label":"Referred by the cases",
            "isArray":true
         }
      },
      {
         "id":"249164267",
         "end1":{
            "owner":"CaseY11",
            "name":"likesitsownkind",
            "label":"Likes It's Own Kind",
            "isArray":false
         },
         "end2":{
            "owner":"CaseY11",
            "name":"likedbyitsownkind",
            "label":"Liked By It's Own Kind",
            "isArray":true
         }
      },
      {
         "id":"250165268",
         "_comment":"new link has been added for the new case type : One to One",
         "end1":{
            "owner":"CaseY11",
            "name":"likesupgradedcases",
            "label":"Likes Upgraded Cases",
            "isArray":false
         },
         "end2":{
            "owner":"CaseY22",
            "name":"likedbyoldcases",
            "label":"Liked By Old Cases"
         }
      },
      {
         "id":"251166269",
         "_comment":"new link has been added for the new case type : Many to Many",
         "end1":{
            "owner":"CaseY1",
            "name":"lovesupgradedcases",
            "label":"Loves Upgraded Cases",
            "isArray":true
         },
         "end2":{
            "owner":"CaseY22",
            "name":"lovedbyoldcases",
            "label":"Loved By Old Cases",
            "isArray":false
         }
      },
      {
         "id":"251166269",
         "_comment":"new self link has been added for the new case type : One to Many",
         "end1":{
            "owner":"CaseY22",
            "name":"followsself",
            "label":"Follws Self",
            "isArray":true
         },
         "end2":{
            "owner":"CaseY22",
            "name":"followedbyself",
            "label":"Follwed By Self",
            "isArray":false
         }
      },
	  {
         "id":"1232252167270",
         "_comment":"new self link has been added for the new case type : One to One",
         "end1":{
            "owner":"CaseY1",
            "name":"selfLink1",
            "label":"Self Link1 one to one",
            "isArray":false
         },
         "end2":{
            "owner":"CaseY1",
            "name":"selfLink2",
            "label":"Self Link2 one to one"
         }
      }
   ]
}