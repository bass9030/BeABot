/*
 * decaffeinate suggestions:
 * DS101: Remove unnecessary use of Array.from
 * DS102: Remove unnecessary code created because of implicit returns
 * DS207: Consider shorter variations of null checks
 * Full docs: https://github.com/decaffeinate/decaffeinate/blob/main/docs/suggestions.md
 */
/*
 * Composition Mod for CodeMirror
 * v2.0.2
 * Zhusee <zhusee2@gmail.com>
 *
 * Additional instance properties added to CodeMirror:
 *   - cm.display.inCompositionMode (Boolen)
 *   - cm.display.compositionHead (Pos)
 *   - cm.display.textMarkerInComposition (TextMarker)
 */

const modInitialized = false;
const TEXT_MARKER_CLASS_NAME = "CodeMirror-text-in-composition";
const TEXT_MARKER_OPTIONS = {
  inclusiveLeft: true,
  inclusiveRight: true,
  className: TEXT_MARKER_CLASS_NAME
};
const PREFIX_LIST = ['webkit', 'moz', 'o'];


const capitalizeString = string => string.charAt(0).toUpperCase() + string.slice(1);

const getPrefixedPropertyName = function(propertyName) {
  const tempElem = document.createElement('div');

  if (tempElem.style[propertyName] != null) { return propertyName; }

  for (let prefix of Array.from(PREFIX_LIST)) {
    const prefixedPropertyName = prefix + capitalizeString(propertyName);
    if (tempElem.style[prefixedPropertyName] != null) { return prefixedPropertyName; }
  }

  return false;
};

CodeMirror.defineOption('enableCompositionMod', false, function(cm, newVal, oldVal) {
  if (newVal && !modInitialized) {
    if (window.CompositionEvent != null) {
      return initCompositionMode(cm);
    } else {
      console.warn("Your browser doesn't support CompositionEvent.");
      return cm.setOption('enableCompositionMod', false);
    }
  }
});

CodeMirror.defineOption('debugCompositionMod', false, function(cm, newVal, oldVal) {
  const inputField = cm.display.input;

  if ((typeof jQuery === 'undefined' || jQuery === null)) { return; }

  if (newVal) {
    $(inputField).on('input.composition-debug keypress.composition-debug compositionupdate.composition-debug', e => console.log(`[${e.type}]`, e.originalEvent.data, inputField.value, e.timeStamp));

    return $(inputField).on('compositionstart.composition-debug compositionend.composition-debug', e => console.warn(`[${e.type}]`, e.originalEvent.data, inputField.value, cm.getCursor(), e.timeStamp));
  } else {
    return $(inputField).off('.composition-debug');
  }
});


const setInputTranslate = function(cm, translateValue) {
  const transformProperty = getPrefixedPropertyName("transform");
  return cm.display.input.style[transformProperty] = translateValue;
};

const resetInputTranslate = cm => setInputTranslate("");

const clearCompositionTextMarkers = function(cm){
  // Clear previous text markers
  const textMarkersArray = cm.getAllMarks();
  for (let textMarker of Array.from(textMarkersArray)) {
    if ((textMarker != null) && (textMarker.className === TEXT_MARKER_CLASS_NAME)) {
      textMarker.clear();
      if (cm.options.debugCompositionMod) { console.log("[TextMarker] Cleared"); }
    }
  }

  return true;
};

var initCompositionMode = function(cm) {
  const inputField = cm.display.input;
  const inputWrapper = cm.display.inputDiv;
  inputWrapper.classList.add('CodeMirror-input-wrapper');

  CodeMirror.on(inputField, 'compositionstart', function(event) {
    if (!cm.options.enableCompositionMod) { return; }

    cm.display.inCompositionMode = true;
    cm.setOption('readOnly', true);

    if (cm.somethingSelected()) { cm.replaceSelection(""); } // Clear the selected text first

    cm.display.compositionHead = cm.getCursor();
    if (cm.options.debugCompositionMod) { console.log("[compositionstart] Update Composition Head", cm.display.compositionHead); }

    inputField.value = "";
    if (cm.options.debugCompositionMod) { console.log("[compositionstart] Clear cm.display.input", cm.display.compositionHead); }

    return inputWrapper.classList.add('in-composition');
  });

  CodeMirror.on(inputField, 'compositionupdate', function(event) {
    if (!cm.options.enableCompositionMod) { return; }
    const headPos = cm.display.compositionHead;

    if (cm.display.textMarkerInComposition) {
      const markerRange = cm.display.textMarkerInComposition.find();

      cm.replaceRange(event.data, headPos, markerRange.to);
      cm.display.textMarkerInComposition.clear();
      cm.display.textMarkerInComposition = undefined;
    } else {
      cm.replaceRange(event.data, headPos, headPos);
    }

    const endPos = cm.getCursor();
    cm.display.textMarkerInComposition = cm.markText(headPos, endPos, TEXT_MARKER_OPTIONS);

    const pixelToTranslate = cm.charCoords(endPos).left - cm.charCoords(headPos).left;
    return setInputTranslate(cm, `translateX(-${pixelToTranslate}px)`);
  });

  return CodeMirror.on(inputField, 'compositionend', function(event) {
    if (!cm.options.enableCompositionMod) { return; }

    const textLeftComposition = event.data;
    const headPos = cm.display.compositionHead;
    const endPos = cm.getCursor();

    cm.replaceRange(textLeftComposition, headPos, endPos);

    cm.display.inCompositionMode = false;
    cm.display.compositionHead = undefined;
    if (cm.display.textMarkerInComposition != null) {
      cm.display.textMarkerInComposition.clear();
    }
    cm.display.textMarkerInComposition = undefined;
    cm.setOption('readOnly', false);

    inputWrapper.classList.remove('in-composition');

    clearCompositionTextMarkers(cm);

    var postCompositionEnd = function() {
      if (cm.display.inCompositionMode) { return false; }

      inputField.value = "";
      if (cm.options.debugCompositionMod) { console.warn("[postCompositionEnd] Input Cleared"); }

      CodeMirror.off(inputField, 'input', postCompositionEnd);
      if (cm.options.debugCompositionMod) { return console.log("[postCompositionEnd] Handler unregistered for future input events"); }
    };

    return CodeMirror.on(inputField, 'input', postCompositionEnd);
  });
};