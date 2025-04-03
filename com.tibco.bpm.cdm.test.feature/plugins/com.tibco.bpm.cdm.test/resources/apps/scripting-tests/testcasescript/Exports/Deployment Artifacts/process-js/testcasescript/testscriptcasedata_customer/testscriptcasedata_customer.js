
/*
 * This provides an implementation of the BOM class com.example.testcasescript.testcasescript.testscriptcasedata_customer. 
 */

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_customer"] = function() {
    /** Constructor. */
    var theClass = function(context) {
        this._init(theClass, context);
    };

    theClass.LOADER = bpm.data.Loader.currentLoader;
    theClass.LOADER.registerClass(theClass, "com.example.testcasescript.testcasescript.testscriptcasedata_customer");
    bpm.data.Loader.extendClass(bpm.data.BomBase, theClass);


    theClass.TYPE_ARRAY = new Object();

    theClass.PRIMITIVE_ATTRIBUTE_NAMES = [

    ];
    theClass.COMPOSITE_ATTRIBUTE_NAMES = [

    ];
    theClass.ATTRIBUTE_NAMES = [

    ];

    theClass.getName = function() {
        return "com.example.testcasescript.testcasescript.testscriptcasedata_customer";
    };

};

bpm.data.Loader.classDefiner["com.example.testcasescript.testcasescript.testscriptcasedata_customer"]();
