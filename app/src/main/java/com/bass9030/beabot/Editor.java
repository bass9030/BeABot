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
import android.webkit.WebViewClient;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.registry.IGrammarSource;
import org.eclipse.tm4e.core.registry.IThemeSource;
import org.eclipse.tm4e.languageconfiguration.model.LanguageConfiguration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.langs.textmate.TextMateAnalyzer;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.TextMateSymbolPairMatch;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.DefaultGrammarDefinition;
import io.github.rosemoe.sora.langs.textmate.registry.model.GrammarDefinition;
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;
import io.github.rosemoe.sora.langs.textmate.registry.reader.LanguageDefinitionReader;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.SymbolInputView;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        CodeEditor editor = findViewById(R.id.editor);
        SymbolInputView symbol = findViewById(R.id.symbolInput);

        String[] symbols = { "->",
                "{",
                "}",
                "(",
                ")",
                ",",
                ".",
                ";",
                "\"",
                "?",
                "+",
                "-",
                "*",
                "/"
        };
        String[] insertSymbols = {
                "\t", "{}", "}", "()", ")", ",", ".", ";", "\"", "?", "+", "-", "*", "/"
        };

        symbol.addSymbols(symbols, insertSymbols);
        symbol.bindEditor(editor);

        FileProviderRegistry.getInstance().addFileProvider(
                new AssetsFileResolver(
                        getApplicationContext().getAssets()
                )
        );
        ThemeRegistry themeRegistry = ThemeRegistry.getInstance();
        try {
            themeRegistry.loadTheme(new ThemeModel(
                    IThemeSource.fromInputStream(
                            FileProviderRegistry.getInstance().tryGetInputStream("TextMate/Themes/one-dark.tmTheme.json"), "TextMate/Themes/one-dark.tmTheme.json", null
                    ), "one-dark"
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        themeRegistry.setTheme("one-dark");
        EditorColorScheme colorScheme = editor.getColorScheme();
        try {
            colorScheme = TextMateColorScheme.create(ThemeRegistry.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.setColorScheme(colorScheme);

        GrammarRegistry.getInstance().loadGrammars("TextMate/languages.json");
        editor.setEditorLanguage(TextMateLanguage.create("source.js", true));
    }
}