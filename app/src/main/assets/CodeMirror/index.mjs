import { basicSetup } from "codemirror"
import { javascript, javascriptLanguage } from "@codemirror/lang-javascript"
import { keymap, EditorView } from "@codemirror/view"
import { indentWithTab } from "@codemirror/commands"
import { vscodeKeymap } from "@replit/codemirror-vscode-keymap"
import * as thememirror from "thememirror"

//TODO: autocomplete 적용

let view = new EditorView({
    extensions: [
        basicSetup,
        javascript(),
        keymap.of(indentWithTab, vscodeKeymap),
        thememirror.dracula,
    ],
    parent: document.body
});

const resize = () => document.getElementsByClassName('cm-editor')[0].setAttribute("style", `height: ${window.innerHeight}px;`);

addEventListener('load', resize);
addEventListener('resize', resize);