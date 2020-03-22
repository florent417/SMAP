package SMAP.au523923Flow.assignment2.wordlearnerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Inspired by (see comment from Hardian):
// https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used
public class ApplicationFirstRunChecker {

    public static void setFirstTimeRun(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getFirstTimeRun(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, true);
    }
}
