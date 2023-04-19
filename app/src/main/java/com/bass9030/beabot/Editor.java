package com.bass9030.beabot;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        WebView webView = findViewById(R.id.codeEditor);
        //            String html = InputStream2String(getAssets().open("vs/index.html"));
//            Log.d("html", html);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebView", consoleMessage.lineNumber() + ": " + consoleMessage.message());
                return true;
            }

        });
        Log.d("script", getString(R.string.defaultScript));
        webView.loadUrl("file:///android_asset/CodeMirror/index.html");
        webView.evaluateJavascript("var onMonacoEditorLoaded = () => monaco.editor.getModels()[0].setValue(`"
                + getString(R.string.defaultScript) + "`);", null);
//        webView.evaluateJavascript("recoveryCode(`" + getString(R.string.defaultScript) + "`);", null);
    }

    private String InputStream2String(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder result = new StringBuilder();

            for (int data = reader.read(); data != -1; data = reader.read()) {
                result.append((char)data);
            }

            return result.toString();
        }catch(IOException ignored) { return ""; }
    }
}