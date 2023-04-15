package com.bass9030.beabot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {
    public static void setConfig(Context mContext, String key, String value) {
        SharedPreferences shardpref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = shardpref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getConfig(Context mContext, String key) {
        SharedPreferences shardpref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return shardpref.getString(key, "");
    }
}
