package com.app.kiory.spotify.datastructure;

import android.content.Context;
import android.preference.PreferenceManager;

import com.app.kiory.spotify.R;

/**
 * Created by darknoe on 10/6/15.
 */
public class SharedPreferences {

    public static String getAccessToken(Context context) {
        android.content.SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.access_token), "");
    }

    public static void setAccessToken(Context context, String token){
        android.content.SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.access_token), token);
        editor.commit();
    }
}
