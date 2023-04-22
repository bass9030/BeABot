"use static"

// import './tern';
// import './codemirror5/lib/codemirror'
// import './codemirror5/mode/javascript/javascript';
// import './codemirror5/addon/tern/tern';
// const CodeMirror = require('./codemirror');
// import './codemirror/mode/javascript/javascript';
// import './codemirror/addon/tern/worker';
// import './codemirror/addon/tern/tern';
// import 'codemirror/addon/tern';
// import ECMA from './ecmascript.json'
// import 'codemirror/mode/css/css';

// tern.Server
const editor = CodeMirror(document.getElementById('editor'), {
    value: '',
    mode: 'javascript',
    // indentWithTabs: true,
    tabSize: 4,
    indentUnit: 4,
    autoCloseBrackets: true,
    enableCompositionMod: true,
    // inputStyle: 'textarea',
    matchBrackets: true,
    lineNumbers: true,
    theme: 'material'
});

var server = new CodeMirror.TernServer(
{
    defs: [ECMA, Replier, ImageDB],
    plugins: {
        doc_comment: {
            strong: true
        }
    }
});
editor.setOption("extraKeys", {
    "Ctrl-Space": function(cm) { server.complete(cm); },
    "Tab": function(cm){ cm.replaceSelection(' '.repeat(cm.getOption('tabSize'))); },
    "Ctrl-I": function(cm) { server.showType(cm); },
    "Ctrl-O": function(cm) { server.showDocs(cm); },
    "Alt-.": function(cm) { server.jumpToDef(cm); },
    "Alt-,": function(cm) { server.jumpBack(cm); },
    "Ctrl-Q": function(cm) { server.rename(cm); },
    "Ctrl-.": function(cm) { server.selectName(cm); },
});

CodeMirror.commands.autocomplete = function(cm) {
    server.complete(cm);
};

editor.on("cursorActivity", function(cm) {
    server.updateArgHints(cm);
});

editor.on('change', (cm, change) => {
    if (change.text.length === 1 && change.text[0] === '.') {
    console.log(change);
      server.complete(cm)
    }
})

document.querySelector("body").addEventListener('click', function(e) {
    var anchor = e.target.closest('a');
    if(anchor !== null) {
        console.log(anchor.textContent);
        // editor.state.keyMaps[0]['\'(\''](editor)
        if(!!editor.state.keyMaps[0][`'${anchor.textContent}'`]) editor.state.keyMaps[0][`'${anchor.textContent}'`](editor);
        else editor.replaceSelection(anchor.textContent);
    }
}, false);
// editor.on('')

const resize = () => {
    document.getElementsByClassName('CodeMirror')[0].setAttribute('style', 'height: ' + (window.innerHeight - document.getElementById('autocomplete').offsetHeight) + 'px;')
    document.getElementById('autocomplete').setAttribute('style', 'width: ' + window.innerWidth + 'px;')
    
}

resize();
addEventListener('resize', resize);

// CodeMirror.