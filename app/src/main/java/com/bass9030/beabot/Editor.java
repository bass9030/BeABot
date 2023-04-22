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
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.TextMateSymbolPairMatch;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.DefaultGrammarDefinition;
import io.github.rosemoe.sora.langs.textmate.registry.model.GrammarDefinition;
import io.github.rosemoe.sora.langs.textmate.registry.reader.LanguageDefinitionReader;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.SymbolInputView;

public class Editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        CodeEditor editor = findViewById(R.id.editor);
        SymbolInputView symbol = findViewById(R.id.symbolInput);
//        new GrammarDefinition().
        try {
            List<GrammarDefinition> grammars = temp(new InputStreamReader(getAssets().open("TextMate/Syntaxes/JavaScriptNext.tmLanguage")));
//            Log.d("scopeName", String.valueOf(grammars.size()));
//        Log.d("scopeName", grammars.get(1).getScopeName());
            GrammarRegistry grammarRegistry = GrammarRegistry.getInstance();
//        grammarRegistry.loadGrammars("file:///android_asset/JavaScript.tmLanguage.json");
//        grammarRegistry.findLanguageConfiguration("lngpck.source.js");
            grammarRegistry.loadGrammars(grammars);
            TextMateLanguage language = TextMateLanguage.create("source.js", grammarRegistry, true);
            editor.setEditorLanguage(language);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<GrammarDefinition> temp(InputStreamReader stream) {
        return new GsonBuilder().registerTypeAdapter(GrammarDefinition.class, (JsonDeserializer<GrammarDefinition>) (json, typeOfT, context) -> {
                    var object = json.getAsJsonObject();
                    var grammarPath = object.get("grammar").getAsString();
                    var name = object.get("name").getAsString();
                    var scopeName = object.get("scopeName").getAsString();


                    var embeddedLanguagesElement = object.get("embeddedLanguages");

                    JsonObject embeddedLanguages = null;

                    if (embeddedLanguagesElement != null && embeddedLanguagesElement.isJsonObject()) {
                        embeddedLanguages = embeddedLanguagesElement.getAsJsonObject();
                    }


                    var languageConfigurationElement = object.get("languageConfiguration");

                    String languageConfiguration = null;

                    if (languageConfigurationElement != null && !languageConfigurationElement.isJsonNull()) {
                        languageConfiguration = languageConfigurationElement.getAsString();
                    }


                    var grammarSource = IGrammarSource.fromInputStream(FileProviderRegistry.getInstance().tryGetInputStream(
                            grammarPath
                    ), grammarPath, Charset.defaultCharset());

                    var grammarDefinition = DefaultGrammarDefinition.withLanguageConfiguration(grammarSource, languageConfiguration, name, scopeName);

                    if (embeddedLanguages != null) {
                        var embeddedLanguagesMap = new HashMap<String, String>();

                        for (var entry : embeddedLanguages.entrySet()) {
                            var value = entry.getValue();

                            if (!value.isJsonNull()) {
                                embeddedLanguagesMap.put(entry.getKey(), value.getAsString());
                            }

                        }

                        return grammarDefinition.withEmbeddedLanguages(embeddedLanguagesMap);
                    } else {
                        return grammarDefinition;
                    }

                })
                .create()
                .fromJson(stream, LanguageDefinitionList.class).grammarDefinition;
    }

    static class LanguageDefinitionList {
        @SerializedName("languages")
        private List<GrammarDefinition> grammarDefinition;

        public LanguageDefinitionList(List<GrammarDefinition> grammarDefinition) {
            this.grammarDefinition = grammarDefinition;
        }

        public List<GrammarDefinition> getLanguageDefinition() {
            return grammarDefinition;
        }

        public void setLanguageDefinition(List<GrammarDefinition> grammarDefinition) {
            this.grammarDefinition = grammarDefinition;
        }
    }

    private String InputStream2String(InputStreamReader stream) {
        try {
            BufferedReader reader = new BufferedReader(stream);

            StringBuilder result = new StringBuilder();

            for (int data = reader.read(); data != -1; data = reader.read()) {
                result.append((char)data);
            }

            return result.toString();
        }catch(IOException ignored) { return ""; }
    }
}