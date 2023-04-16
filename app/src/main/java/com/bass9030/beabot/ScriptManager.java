package com.bass9030.beabot;

import android.os.Environment;
import android.util.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.bass9030.beabot.FileManager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptManager {
    HashMap<String, Context> botContexts = new HashMap<>();
    HashMap<String, Scriptable> botScopes = new HashMap<>();
    HashMap<String, Bot> botState = new HashMap<>();


    public boolean addNewBot(String name) {
        if(botContexts.containsKey(name) || botScopes.containsKey(name)) return false;
        Bot bot = new Bot(name, false, null);
        FileManager.addBot(bot);

        return this.addExistBot(name);
    }

    public boolean addExistBot(String name) {
        if(botContexts.containsKey(name) || botScopes.containsKey(name)) return false;

        Bot bot = FileManager.getBot(name);
        if(bot == null) return false;

        Context rhino = Context.enter();
        rhino.setLanguageVersion(Context.VERSION_ES6);
        rhino.setOptimizationLevel(-1);
        Scriptable scope = rhino.initStandardObjects();
        try {
            rhino.evaluateReader(scope, new FileReader(bot.getCodePath()), bot.getBotName(), 1, null);
        }catch(IOException ignored) { return false; }
        botState.put(name, bot);
        botContexts.put(name, rhino);
        botScopes.put(name, scope);
        return true;
    }

    public boolean removeBot(String name) {
        if(FileManager.removeBot(name)) {
            botState.remove(name);
            botContexts.remove(name);
            botScopes.remove(name);
            return true;
        }else return false;
    }

    public boolean setPowerBot(String name, boolean power) {
        botState.get(name).setIsPower(power);
        return true;
    }

    public boolean compileBot(String name) {
        Bot bot = FileManager.getBot(name);
        if(bot == null) return false;

        try {
            Context rhino = botContexts.get(name);
            Scriptable scope = botScopes.get(name);
            if(rhino == null && scope == null) return false;

            rhino.evaluateReader(scope, new FileReader(bot.getCodePath()), bot.getBotName(), 1, null);
            return true;
        }catch(IOException ignored) { return false; }
    }

    public void callResponse(String room, String msg, String sender, boolean isGroupChat,
                             Replier replier, ImageDB imageDB, String packageName, boolean isMultiChat) {
        for(Map.Entry<String, Context> entry : botContexts.entrySet()) {
            if(!botState.get(entry.getKey()).getIsPower()) continue;
            Scriptable scope = botScopes.get(entry.getKey());
            Context rhino = entry.getValue();
            Function func = (Function) scope.get("response", scope);
            try {
                func.call(rhino, scope, scope,
                        new Object[] { room, msg, sender, isGroupChat, replier, imageDB, packageName, isMultiChat });
            }catch(Exception ex) {
                Log.e("failedCallResponse", ex.toString());
            }
        }
    }
}
