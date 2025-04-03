

if (typeof(tibcoforms) == 'undefined') tibcoforms = new Object();
if (typeof(tibcoforms.formCode) == 'undefined') tibcoforms.formCode = new Object();
tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg'] = new Object();
tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg']['defineActions'] = function() {
var fc = tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg'];
    fc['rule_enable_complexArrayField__detail'] = function(formId, context, thisObj) {
       try {
            bpm.forms.Util.handleComputationAction.call(thisObj, formId, context, thisObj, "enable_complexArrayField__detail", "enable_complexArrayField__detail", fc['action_enable_complexArrayField__detail'], false, 'pane.complexArrayField__detail');
       } catch(e) {
           tibcoforms.bridge.log_error("Rule(enable_complexArrayField__detail) Error: " + e);
           throw e;
       }
    }

        fc['action_enable_complexArrayField__detail'] = function(context, data, pane, control, factory, pkg, f , p) {
                if (context.form && context.form._marked4Cancel)
                    return;
                var resource = context.form.resource;
                var logger = tibcoforms.bridge.log_logger();
                var destName = 'pane.complexArrayField__detail';
                    var destType = 'pane';
                    var destFeatures = new Array('enabled');
                    var isComplex = false;
                    isComplex = tibcoforms.bridge.comp_isComplexFeature(context.form.id, false, destName, context.cloneUID, 'enabled');                                                                        var tempScr = 'context.newValue != null;';
                var val = eval(tempScr);
                val = bpm.forms.Util.convertExternalValueToInternalValue.call(this, isComplex, val, destName, context);
                    if (isComplex) {
                       if (tibcoforms.FormProxy.isTIBCOFormsList(val))
                           tibcoforms.bridge.compAction_updateCOListDestination(context.form.id, 'action.enable_complexArrayField__detail', destName, context.cloneUID, destType, destFeatures, val);
                       else
                           tibcoforms.bridge.compAction_updateCODestination(context.form.id, 'action.enable_complexArrayField__detail', destName, context.cloneUID, destType, destFeatures, val);
                    } else {
                        tibcoforms.bridge.compAction_updateDestination(context.form.id, 'action.enable_complexArrayField__detail', destName, context.cloneUID, destType, destFeatures, val);
                    }
        }
    fc['rule_cancel'] = function(formId, context, thisObj) {
       try {
            bpm.forms.Util.handleScriptAction.call(thisObj, formId, context, thisObj, "cancel", "cancel", fc['action_cancel']);
       } catch(e) {
           tibcoforms.bridge.log_error("Rule(cancel) Error: " + e);
           throw e;
       }
    }

    fc['rule_close'] = function(formId, context, thisObj) {
       try {
            bpm.forms.Util.handleScriptAction.call(thisObj, formId, context, thisObj, "close", "close", fc['action_close']);
       } catch(e) {
           tibcoforms.bridge.log_error("Rule(close) Error: " + e);
           throw e;
       }
    }

    fc['rule_submit'] = function(formId, context, thisObj) {
       try {
            bpm.forms.Util.handleScriptAction.call(thisObj, formId, context, thisObj, "submit", "submit", fc['action_submit']);
       } catch(e) {
           tibcoforms.bridge.log_error("Rule(submit) Error: " + e);
           throw e;
       }
    }

    fc['action_cancel'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('cancel');
    }

    fc['action_apply'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('apply');
    }
    
    fc['action_close'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('close');
    }

    fc['action_submit'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('submit');
    }
    
    fc['action_validate'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('validate');
    }
    
    fc['action_reset'] = function(context, data, pane, control, factory, pkg, f , p) {
        context.form.invokeAction('reset');
    }
    
    fc['generator_info'] = function() {
        return "TIBCO Forms for ACE Runtime 11.0.0.011 V03";
    }
};
tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg']['defineActions']();

tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg']['defineValidations'] = function() {
var fc = tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg'];
    
fc['validation_dateArrayField_dateArrayField__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "dateArrayField__datetime", true, true);
}
    
fc['validation_complexArrayField_child_childAttr2_complexArrayField_child_childAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_child_childAttr2__length", true, true);
}
    
fc['validation_complexArrayField_parentAttr1_complexArrayField_parentAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_parentAttr1__length", true, true);
}
    
fc['validation_textArrayField_textArrayField__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "textArrayField__length", true, true);
}
    
fc['validation_dateTimeArrayField_dateTimeArrayField__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "dateTimeArrayField__datetime", true, true);
}
    
    
fc['validation_uriParameter_uriParameter__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "uriParameter__length", true, true);
}
    
    
    
fc['validation_textParameter_textParameter__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "textParameter__length", true, true);
}
    
    
fc['validation_simpleArrayAttributesField_performerArrayAttr_simpleArrayAttributesField_performerArrayAttr__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_performerArrayAttr__length", true, true);
}
fc['validation_simpleArrayAttributesField_performerArrayAttr_simpleArrayAttributesField_performerArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_performerArrayAttr__multiplicity", true, true);
}
    
fc['validation_simpleSingleAttributesParameter_textAttr_simpleSingleAttributesParameter_textAttr__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_textAttr__length", true, true);
}
    
fc['validation_complexSingleParameter_parentAttr1_complexSingleParameter_parentAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_parentAttr1__length", true, true);
}
    
    
    
    
fc['validation_simpleSingleAttributesParameter_performerAttr_simpleSingleAttributesParameter_performerAttr__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_performerAttr__length", true, true);
}
    
    
    
fc['validation_complexSingleParameter_children_childAttr1_complexSingleParameter_children_childAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_children_childAttr1__length", true, true);
}
    
fc['validation_numberDecimalArrayField_numberDecimalArrayField__fixed'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkNumberConstraint(context.stringValue, 10, 2) ? true : [context.control.label,\'10\', \'2\'] : !isNaN(context.value) && context.form.numberFormat(context.value, 10, 2);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "numberDecimalArrayField__fixed", true, true);
}
    
fc['validation_simpleSingleAttributesParameter_uriAttr_simpleSingleAttributesParameter_uriAttr__pattern'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'if (typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\') {\
  \n bpm.forms.Util.checkRegExp(context.value, new RegExp(\"^([a-zA-Z0-9+.\\\\-]+):(//((([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:]|%[0-9A-F]{2})*)@)?(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=]|%[0-9A-F]{2})*)(:(\\\\d*))?(/([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@]|%[0-9A-F]{2})+([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?)(\\\\?(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?(#(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?$\")) ? true : [context.control.label, \'URI\'];\
  \n} else {\
  \nvar regex = new RegExp(\"^([a-zA-Z0-9+.\\-]+):(//((([a-zA-Z0-9\\-._~!$&\'()*+,;=:]|%[0-9A-F]{2})*)@)?(([a-zA-Z0-9\\-._~!$&\'()*+,;=]|%[0-9A-F]{2})*)(:(\\d*))?(/([a-zA-Z0-9\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?([a-zA-Z0-9\\-._~!$&\'()*+,;=:@]|%[0-9A-F]{2})+([a-zA-Z0-9\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?)(\\?(([a-zA-Z0-9\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?(#(([a-zA-Z0-9\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?$\");\
  \nregex.test(context.value);\
  \n}';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_uriAttr__pattern", true, true);
}
    
fc['validation_complexSingleParameter_child_childAttr2_complexSingleParameter_child_childAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_child_childAttr2__length", true, true);
}
    
fc['validation_complexArrayField_parentAttr2_complexArrayField_parentAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_parentAttr2__length", true, true);
}
    
fc['validation_numberDecimalParameter_numberDecimalParameter__fixed'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkNumberConstraint(context.stringValue, 10, 2) ? true : [context.control.label,\'10\', \'2\'] : !isNaN(context.value) && context.form.numberFormat(context.value, 10, 2);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "numberDecimalParameter__fixed", true, true);
}
    
fc['validation_complexSingleParameter_child_childAttr1_complexSingleParameter_child_childAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_child_childAttr1__length", true, true);
}
    
    
fc['validation_complexArrayField_parentAttr2__master_complexArrayField_parentAttr2__master__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_parentAttr2__master__length", true, true);
}
    
fc['validation_simpleSingleAttributesParameter_timeAttr_simpleSingleAttributesParameter_timeAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_timeAttr__datetime", true, true);
}
    
fc['validation_complexSingleParameter_children_childAttr2_complexSingleParameter_children_childAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_children_childAttr2__length", true, true);
}
    
fc['validation_complexArrayField_children_childAttr2_complexArrayField_children_childAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_children_childAttr2__length", true, true);
}
    
    
fc['validation_simpleArrayAttributesField_numberIntegerArrayAttr_simpleArrayAttributesField_numberIntegerArrayAttr__fixed'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkNumberConstraint(context.stringValue, 10, 0) ? true : [context.control.label,\'10\', \'0\'] : !isNaN(context.value) && context.form.numberFormat(context.value, 10, 0);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_numberIntegerArrayAttr__fixed", true, true);
}
fc['validation_simpleArrayAttributesField_numberIntegerArrayAttr_simpleArrayAttributesField_numberIntegerArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_numberIntegerArrayAttr__multiplicity", true, true);
}
    
    
    
    
fc['validation_complexArrayField_child_childAttr1_complexArrayField_child_childAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_child_childAttr1__length", true, true);
}
    
    
fc['validation_simpleSingleAttributesParameter_numberDecimalAttr_simpleSingleAttributesParameter_numberDecimalAttr__float'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\'? context.value !== \'\' && !isNaN(context.value) ? true : [context.control.label] : context.value !== \'\' && !isNaN(context.value);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_numberDecimalAttr__float", true, true);
}
    
fc['validation_simpleArrayAttributesField_textArrayAttr_simpleArrayAttributesField_textArrayAttr__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_textArrayAttr__length", true, true);
}
fc['validation_simpleArrayAttributesField_textArrayAttr_simpleArrayAttributesField_textArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_textArrayAttr__multiplicity", true, true);
}
    
    
fc['validation_dateParameter_dateParameter__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "dateParameter__datetime", true, true);
}
    
    
    
fc['validation_simpleArrayAttributesField_uriArrayAttr_simpleArrayAttributesField_uriArrayAttr__pattern'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'if (typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\') {\
  \n bpm.forms.Util.checkRegExp(context.value, new RegExp(\"^([a-zA-Z0-9+.\\\\-]+):(//((([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:]|%[0-9A-F]{2})*)@)?(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=]|%[0-9A-F]{2})*)(:(\\\\d*))?(/([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@]|%[0-9A-F]{2})+([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?)(\\\\?(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?(#(([a-zA-Z0-9\\\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?$\")) ? true : [context.control.label, \'URI\'];\
  \n} else {\
  \nvar regex = new RegExp(\"^([a-zA-Z0-9+.\\-]+):(//((([a-zA-Z0-9\\-._~!$&\'()*+,;=:]|%[0-9A-F]{2})*)@)?(([a-zA-Z0-9\\-._~!$&\'()*+,;=]|%[0-9A-F]{2})*)(:(\\d*))?(/([a-zA-Z0-9\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?|(/?([a-zA-Z0-9\\-._~!$&\'()*+,;=:@]|%[0-9A-F]{2})+([a-zA-Z0-9\\-._~!$&\'()*+,;=:@/]|%[0-9A-F]{2})*)?)(\\?(([a-zA-Z0-9\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?(#(([a-zA-Z0-9\\-._~!$&\'()*+,;=:/?@]|%[0-9A-F]{2})*))?$\");\
  \nregex.test(context.value);\
  \n}';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_uriArrayAttr__pattern", true, true);
}
fc['validation_simpleArrayAttributesField_uriArrayAttr_simpleArrayAttributesField_uriArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_uriArrayAttr__multiplicity", true, true);
}
    
fc['validation_complexArrayField_children_complexArrayField_children__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_children__multiplicity", true, false);
}
    
fc['validation_complexArrayField_children_childAttr1_complexArrayField_children_childAttr1__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_children_childAttr1__length", true, true);
}
    
    
    
fc['validation_dateTimeParameter_dateTimeParameter__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "dateTimeParameter__datetime", true, true);
}
    
fc['validation_simpleArrayAttributesField_dateArrayAttr_simpleArrayAttributesField_dateArrayAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_dateArrayAttr__datetime", true, true);
}
fc['validation_simpleArrayAttributesField_dateArrayAttr_simpleArrayAttributesField_dateArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_dateArrayAttr__multiplicity", true, true);
}
    
    
    
    
fc['validation_complexArrayField_parentAttr1__master_complexArrayField_parentAttr1__master__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexArrayField_parentAttr1__master__length", true, true);
}
    
fc['validation_simpleSingleAttributesParameter_numberIntegerAttr_simpleSingleAttributesParameter_numberIntegerAttr__fixed'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkNumberConstraint(context.stringValue, 10, 0) ? true : [context.control.label,\'10\', \'0\'] : !isNaN(context.value) && context.form.numberFormat(context.value, 10, 0);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_numberIntegerAttr__fixed", true, true);
}
    
    
fc['validation_simpleArrayAttributesField_timeArrayAttr_simpleArrayAttributesField_timeArrayAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_timeArrayAttr__datetime", true, true);
}
fc['validation_simpleArrayAttributesField_timeArrayAttr_simpleArrayAttributesField_timeArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_timeArrayAttr__multiplicity", true, true);
}
    
fc['validation_simpleArrayAttributesField_numberDecimalArrayAttr_simpleArrayAttributesField_numberDecimalArrayAttr__float'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\'? context.value !== \'\' && !isNaN(context.value) ? true : [context.control.label] : context.value !== \'\' && !isNaN(context.value);';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_numberDecimalArrayAttr__float", true, true);
}
fc['validation_simpleArrayAttributesField_numberDecimalArrayAttr_simpleArrayAttributesField_numberDecimalArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_numberDecimalArrayAttr__multiplicity", true, true);
}
    
fc['validation_complexSingleParameter_children_complexSingleParameter_children__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_children__multiplicity", true, false);
}
    
fc['validation_simpleSingleAttributesParameter_dateAttr_simpleSingleAttributesParameter_dateAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_dateAttr__datetime", true, true);
}
    
fc['validation_simpleArrayAttributesField_dateTimeTZArrayAttr_simpleArrayAttributesField_dateTimeTZArrayAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_dateTimeTZArrayAttr__datetime", true, true);
}
fc['validation_simpleArrayAttributesField_dateTimeTZArrayAttr_simpleArrayAttributesField_dateTimeTZArrayAttr__multiplicity'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof bpm.forms.Util.checkMultiplicity != \'undefined\' ? bpm.forms.Util.checkMultiplicity(context.value, 0, 2147483647) ?  true : [context.control.label, \'0\'] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleArrayAttributesField_dateTimeTZArrayAttr__multiplicity", true, true);
}
    
    
    
fc['validation_simpleSingleAttributesParameter_dateTimeTZAttr_simpleSingleAttributesParameter_dateTimeTZAttr__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkDateTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "simpleSingleAttributesParameter_dateTimeTZAttr__datetime", true, true);
}
    
fc['validation_uriArrayField_uriArrayField__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "uriArrayField__length", true, true);
}
    
fc['validation_timeParameter_timeParameter__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "timeParameter__datetime", true, true);
}
    
    
fc['validation_timeArrayField_timeArrayField__datetime'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTimeFormat(context.stringValue) ?  true : [context.control.label] : true;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "timeArrayField__datetime", true, true);
}
    
fc['validation_complexSingleParameter_parentAttr2_complexSingleParameter_parentAttr2__length'] = function(formId, controlName, cloneUID, listIndex) {
    var valScr = 'typeof context.stringValue != \'undefined\' && typeof bpm.forms.Util != \'undefined\' ? bpm.forms.Util.checkTextLength(context.stringValue, 50) ? true : [context.control.label, \'50\'] : context.value.length <= 50;';
    return bpm.forms.Util.handleInlineValidation.call(this, formId, this, cloneUID, listIndex, valScr, "complexSingleParameter_parentAttr2__length", true, true);
}
    
    
    
    
    fc['validate_required'] = function(formId, controlName, cloneUID, listIndex) {
    var context = new Object();
    var form = tibcoforms.formCache[formId];
    var logger = tibcoforms.bridge.log_logger();
    context.control = this;
    if (listIndex == -1) {
        context.value = context.control.value;
        if (context.control.getStringValue)
            context.stringValue = context.control.getStringValue();
    } else {
        context.value = context.control.value[listIndex];
        if (context.control.getStringValue)
            context.stringValue = context.control.getStringValue()[listIndex];
    }
    if (context.value == null)
        context.value = '';
        var controlType = context.control.controlType;
        var strContxtControlValue = context.control.value;
        if (listIndex >= 0) {
           strContxtControlValue = strContxtControlValue[listIndex];
        }
        return !(context.control.required && 
                 (strContxtControlValue == null || strContxtControlValue.toString().replace(/^\s+|\s+$/gm,'').length == 0) ||
                 (("com.tibco.forms.controls.checkbox" == controlType) && 'true' != strContxtControlValue.toString().toLowerCase()));
    }
    fc['register_pkgs_and_fcts'] = function(formId) {
       var form = tibcoforms.formCache[formId];
       form.registerPackages(['com.tibco.ace.datatypes.DatatypesPackage']);
       form.registerFactories(['com.tibco.ace.datatypes.DatatypesFactory']);
    }
    fc['DataModel'] = function(formId) {
        if (this.form) return;
        this.form = tibcoforms.formCache[formId];

        this.getBooleanParameter = function(useInternal) {
            return this.form.dataMap['booleanParameter'].getValue(useInternal);
        };
        this.setBooleanParameter = function(value) {
            this.form.dataMap['booleanParameter'].setValue(value);
        };
        Object.defineProperty(this, 'booleanParameter', {
            get: function() {
                return this.form.dataMap['booleanParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['booleanParameter'].value = value;
            },
            enumerable: true
        });

        this.getDateParameter = function(useInternal) {
            return this.form.dataMap['dateParameter'].getValue(useInternal);
        };
        this.setDateParameter = function(value) {
            this.form.dataMap['dateParameter'].setValue(value);
        };
        Object.defineProperty(this, 'dateParameter', {
            get: function() {
                return this.form.dataMap['dateParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['dateParameter'].value = value;
            },
            enumerable: true
        });

        this.getDateTimeParameter = function(useInternal) {
            return this.form.dataMap['dateTimeParameter'].getValue(useInternal);
        };
        this.setDateTimeParameter = function(value) {
            this.form.dataMap['dateTimeParameter'].setValue(value);
        };
        Object.defineProperty(this, 'dateTimeParameter', {
            get: function() {
                return this.form.dataMap['dateTimeParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['dateTimeParameter'].value = value;
            },
            enumerable: true
        });

        this.getNumberDecimalParameter = function(useInternal) {
            return this.form.dataMap['numberDecimalParameter'].getValue(useInternal);
        };
        this.setNumberDecimalParameter = function(value) {
            this.form.dataMap['numberDecimalParameter'].setValue(value);
        };
        Object.defineProperty(this, 'numberDecimalParameter', {
            get: function() {
                return this.form.dataMap['numberDecimalParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['numberDecimalParameter'].value = value;
            },
            enumerable: true
        });

        this.getNumberIntegerParameter = function(useInternal) {
            return this.form.dataMap['numberIntegerParameter'].getValue(useInternal);
        };
        this.setNumberIntegerParameter = function(value) {
            this.form.dataMap['numberIntegerParameter'].setValue(value);
        };
        Object.defineProperty(this, 'numberIntegerParameter', {
            get: function() {
                return this.form.dataMap['numberIntegerParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['numberIntegerParameter'].value = value;
            },
            enumerable: true
        });

        this.getPerformerParameter = function(useInternal) {
            return this.form.dataMap['performerParameter'].getValue(useInternal);
        };
        this.setPerformerParameter = function(value) {
            this.form.dataMap['performerParameter'].setValue(value);
        };
        Object.defineProperty(this, 'performerParameter', {
            get: function() {
                return this.form.dataMap['performerParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['performerParameter'].value = value;
            },
            enumerable: true
        });

        this.getTextParameter = function(useInternal) {
            return this.form.dataMap['textParameter'].getValue(useInternal);
        };
        this.setTextParameter = function(value) {
            this.form.dataMap['textParameter'].setValue(value);
        };
        Object.defineProperty(this, 'textParameter', {
            get: function() {
                return this.form.dataMap['textParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['textParameter'].value = value;
            },
            enumerable: true
        });

        this.getTimeParameter = function(useInternal) {
            return this.form.dataMap['timeParameter'].getValue(useInternal);
        };
        this.setTimeParameter = function(value) {
            this.form.dataMap['timeParameter'].setValue(value);
        };
        Object.defineProperty(this, 'timeParameter', {
            get: function() {
                return this.form.dataMap['timeParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['timeParameter'].value = value;
            },
            enumerable: true
        });

        this.getUriParameter = function(useInternal) {
            return this.form.dataMap['uriParameter'].getValue(useInternal);
        };
        this.setUriParameter = function(value) {
            this.form.dataMap['uriParameter'].setValue(value);
        };
        Object.defineProperty(this, 'uriParameter', {
            get: function() {
                return this.form.dataMap['uriParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['uriParameter'].value = value;
            },
            enumerable: true
        });

        this.getSimpleSingleAttributesParameter = function() {
            return this.form.dataMap['simpleSingleAttributesParameter'].getValue();
        };
        this.setSimpleSingleAttributesParameter = function(value) {
            this.form.dataMap['simpleSingleAttributesParameter'].setValue(value);
        };
        Object.defineProperty(this, 'simpleSingleAttributesParameter', {
            get: function() {
                return this.form.dataMap['simpleSingleAttributesParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['simpleSingleAttributesParameter'].value = value;
            },
            enumerable: true
        });

        this.getComplexSingleParameter = function() {
            return this.form.dataMap['complexSingleParameter'].getValue();
        };
        this.setComplexSingleParameter = function(value) {
            this.form.dataMap['complexSingleParameter'].setValue(value);
        };
        Object.defineProperty(this, 'complexSingleParameter', {
            get: function() {
                return this.form.dataMap['complexSingleParameter'].value;
            },
            set: function(value) {
                this.form.dataMap['complexSingleParameter'].value = value;
            },
            enumerable: true
        });

        this.getBooleanArrayField = function(useInternal) {
            return this.form.dataMap['booleanArrayField'].getValue(useInternal);
        };
        this.setBooleanArrayField = function(value) {
            this.form.dataMap['booleanArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'booleanArrayField', {
            get: function() {
                return this.form.dataMap['booleanArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getDateArrayField = function(useInternal) {
            return this.form.dataMap['dateArrayField'].getValue(useInternal);
        };
        this.setDateArrayField = function(value) {
            this.form.dataMap['dateArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'dateArrayField', {
            get: function() {
                return this.form.dataMap['dateArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getDateTimeArrayField = function(useInternal) {
            return this.form.dataMap['dateTimeArrayField'].getValue(useInternal);
        };
        this.setDateTimeArrayField = function(value) {
            this.form.dataMap['dateTimeArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'dateTimeArrayField', {
            get: function() {
                return this.form.dataMap['dateTimeArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getNumberDecimalArrayField = function(useInternal) {
            return this.form.dataMap['numberDecimalArrayField'].getValue(useInternal);
        };
        this.setNumberDecimalArrayField = function(value) {
            this.form.dataMap['numberDecimalArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'numberDecimalArrayField', {
            get: function() {
                return this.form.dataMap['numberDecimalArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getNumberIntegerArrayField = function(useInternal) {
            return this.form.dataMap['numberIntegerArrayField'].getValue(useInternal);
        };
        this.setNumberIntegerArrayField = function(value) {
            this.form.dataMap['numberIntegerArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'numberIntegerArrayField', {
            get: function() {
                return this.form.dataMap['numberIntegerArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getPerformerArrayField = function(useInternal) {
            return this.form.dataMap['performerArrayField'].getValue(useInternal);
        };
        this.setPerformerArrayField = function(value) {
            this.form.dataMap['performerArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'performerArrayField', {
            get: function() {
                return this.form.dataMap['performerArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getTextArrayField = function(useInternal) {
            return this.form.dataMap['textArrayField'].getValue(useInternal);
        };
        this.setTextArrayField = function(value) {
            this.form.dataMap['textArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'textArrayField', {
            get: function() {
                return this.form.dataMap['textArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getTimeArrayField = function(useInternal) {
            return this.form.dataMap['timeArrayField'].getValue(useInternal);
        };
        this.setTimeArrayField = function(value) {
            this.form.dataMap['timeArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'timeArrayField', {
            get: function() {
                return this.form.dataMap['timeArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getUriArrayField = function(useInternal) {
            return this.form.dataMap['uriArrayField'].getValue(useInternal);
        };
        this.setUriArrayField = function(value) {
            this.form.dataMap['uriArrayField'].setValue(value);
        };
        Object.defineProperty(this, 'uriArrayField', {
            get: function() {
                return this.form.dataMap['uriArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });

        this.getSimpleArrayAttributesField = function() {
            return this.form.dataMap['simpleArrayAttributesField'].getValue();
        };
        this.setSimpleArrayAttributesField = function(value) {
            this.form.dataMap['simpleArrayAttributesField'].setValue(value);
        };
        Object.defineProperty(this, 'simpleArrayAttributesField', {
            get: function() {
                return this.form.dataMap['simpleArrayAttributesField'].value;
            },
            set: function(value) {
                this.form.dataMap['simpleArrayAttributesField'].value = value;
            },
            enumerable: true
        });

        this.getComplexArrayField = function() {
            return this.form.dataMap['complexArrayField'].getValue();
        };
        Object.defineProperty(this, 'complexArrayField', {
            get: function() {
                return this.form.dataMap['complexArrayField'].value;
            },
            set: function(value) {
                throw "Cannot assign value on a multi-valued parameter/datafield.";
            },
            enumerable: true
        });
    }
       
            
            
            
            
            
            
            
            
            
            
            
            
            
       
       
};
tibcoforms.formCode['_ydk5ILngEemEnOSmxnZnsg']['defineValidations']();
