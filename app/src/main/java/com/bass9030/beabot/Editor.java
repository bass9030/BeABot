package com.bass9030.beabot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import org.eclipse.tm4e.core.internal.grammar.Grammar;
import org.eclipse.tm4e.core.registry.IThemeSource;

import java.util.Objects;

import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.GrammarDefinition;
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.SymbolInputView;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        CodeEditor editor = findViewById(R.id.editor);
        SymbolInputView symbol = findViewById(R.id.symbolInput);

        editor.setText(getString(R.string.defaultScript));

        symbol.bindEditor(editor);

        FileProviderRegistry.getInstance().addFileProvider(
                new AssetsFileResolver(
                        getApplicationContext().getAssets()
                )
        );

        try {
            ThemeRegistry themeRegistry = ThemeRegistry.getInstance();
            themeRegistry.loadTheme(new ThemeModel(
                    IThemeSource.fromInputStream(
                            Objects.requireNonNull(FileProviderRegistry.getInstance().tryGetInputStream("TextMate/Themes/one-dark.tmTheme.json")), "TextMate/Themes/one-dark.tmTheme.json", null
                    ), "one-dark"
            ));
            themeRegistry.setTheme("one-dark");
            editor.setColorScheme(TextMateColorScheme.create(themeRegistry));
        } catch (Exception e) {
            Log.e("editorThemeLoadError", e.toString());
        }

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

        GrammarRegistry.getInstance().loadGrammars("TextMate/languages.json");
        editor.setEditorLanguage(TextMateLanguage.create("source.js", true));

        ViewCompat.setOnApplyWindowInsetsListener(this.getWindow().getDecorView(), (v, insets) -> {
            boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            int keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            if(isKeyboardVisible)
                params.setMargins(0,0,0,keyboardHeight);
            else
                params.setMargins(0,0,0,0);

            HorizontalScrollView scroll = findViewById(R.id.symbolInputScroll);
            scroll.setLayoutParams(params);

            return insets;
        });

        Log.d("Editor", "onCreate call");
    }
}