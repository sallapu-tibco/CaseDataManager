// NOTE: Forms uses lists for multivalued complex data and attributes.
// BUG!!! Must clear master pane selection programmatically first in order to clear the child and children pane values.
pane.complexArrayField__master.setSelection(null);
var complexArrayField = data.getComplexArrayField();
complexArrayField.clear();
logger.info("Cleared form data");