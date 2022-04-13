package id.nfathoni.cobanetra.util;

import android.content.Context;
import android.content.SharedPreferences;

import id.nfathoni.cobanetra.R;

public class PrefUtil {

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.key_preference), Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.key_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
