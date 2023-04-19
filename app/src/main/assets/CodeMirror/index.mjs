"use static"

// import './tern';
// // import './codemirror5/lib/codemirror'
// // import './codemirror5/mode/javascript/javascript';
// // import './codemirror5/addon/tern/tern';
// import './codemirror';
// import './codemirror/mode/javascript/javascript';
// import './codemirror/addon/tern/worker';
// import './codemirror/addon/tern/tern';
// import 'codemirror/addon/tern';
// import ECMA from './ecmascript.json'
// import 'codemirror/mode/css/css';

// tern.Server
const editor = CodeMirror.fromTextArea(document.getElementById('editor'), {
    value: 'function test() { return \'hello world\'; }',
    mode: 'javascript',
    // hintOptions: {
    //     alignWithWord: false,
    //     completeSingle: false,
    // },
    lineNumbers: true,
    theme: 'material'
});

var server = new CodeMirror.TernServer({defs: [ECMA, Chatbot]});
editor.setOption("extraKeys", {
    "Ctrl-Space": function(cm) { server.complete(cm); },
    // ".": function(cm) { server.complete(cm); },
    "Ctrl-I": function(cm) { server.showType(cm); },
    "Ctrl-O": function(cm) { server.showDocs(cm); },
    "Alt-.": function(cm) { server.jumpToDef(cm); },
    "Alt-,": function(cm) { server.jumpBack(cm); },
    "Ctrl-Q": function(cm) { server.rename(cm); },
    "Ctrl-.": function(cm) { server.selectName(cm); }
});

CodeMirror.commands.autocomplete = function(cm) {
    server.complete(cm);
};

editor.on("cursorActivity", function(cm) {
    server.updateArgHints(cm);
});

editor.on('change', (cm, change) => {
    if (change.text.length === 1 && change.text[0] === '.') {
      server.complete(cm)
    }
})

// editor.on('')

const resize = () => {
    document.getElementsByClassName('CodeMirror')[0].setAttribute('style', 'height: ' + (window.innerHeight - document.getElementById('autocomplete').offsetHeight) + 'px;')
    document.getElementById('autocomplete').setAttribute('style', 'width: ' + window.innerWidth + 'px;')
    
}

resize();
addEventListener('resize', resize);

// CodeMirror.