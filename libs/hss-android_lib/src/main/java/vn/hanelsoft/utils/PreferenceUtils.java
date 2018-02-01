package vn.hanelsoft.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

    private SharedPreferences sharedpreferences;
    private static PreferenceUtils instance;

    private PreferenceUtils(Context context) {
        sharedpreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        instance = new PreferenceUtils(context);
    }

    public static PreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtils(context);
        }
        return instance;
    }

    public static PreferenceUtils getInstance() {
        if (instance == null)
            throw new NullPointerException("PreferenceUtils not initialized. Please invoke initialize() method first");
        return instance;
    }

    public void putBool(String key, boolean value) {
        Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        Editor editor = sharedpreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putString(String key, String value) {
        Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public boolean contain(String key) {
        return sharedpreferences.contains(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedpreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedpreferences.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return sharedpreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return sharedpreferences.getFloat(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return sharedpreferences.getString(key, defaultValue);
    }

    public void delete(String key) {
        sharedpreferences.edit().remove(key).commit();
    }

}
