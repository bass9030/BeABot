import { basicSetup } from "codemirror"
import {javascript} from "@codemirror/lang-javascript"
import { keymap, EditorView } from "@codemirror/view"
import { indentWithTab } from "@codemirror/commands"
import { vscodeKeymap } from "@replit/codemirror-vscode-keymap"
import { materialDark } from '@ddietr/codemirror-themes/material-dark'

let view = new EditorView({
    extensions: [
        basicSetup,
        javascript(),
        keymap.of(indentWithTab, vscodeKeymap),
        materialDark,
    ],
    parent: document.body
});