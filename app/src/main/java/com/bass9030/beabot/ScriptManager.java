package com.bass9030.beabot;

import android.os.Environment;

import org.graalvm.polyglot.Context;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class ScriptManager {
    private final static String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    HashMap<Bot, CompiledScript>

    public void addBot(String name) {

    }

    public void removeBot(String name) {

    }

    public void compileBot(String name) {

    }

    public void callResponse(String room, String msg, String sender, boolean isGroupChat,
                             Replier replier, ImageDB imageDB, String packagename, boolean isMultiChat) {
        try(Context context = Context.create("js")) {
            context.eval
        }

        try {
            CompiledScript compiledScript = engine.compile("console.log('adsf')");
        }
    }
}
