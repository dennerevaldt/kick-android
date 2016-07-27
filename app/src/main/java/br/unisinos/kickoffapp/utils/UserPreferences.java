package br.unisinos.kickoffapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import br.unisinos.kickoffapp.models.Enterprise;
import br.unisinos.kickoffapp.models.Player;

/**
 * Created by dennerevaldtmachado on 30/06/16.
 */
public class UserPreferences {
    private final static String PREFERENCE = "preference";
    private final static String PREFERENCE_USER = "preference.user";
    private final static String PREFERENCE_TYPE_USER = "preference.typeuser";
    private final static String PREFERENCE_TOKEN = "preference.token";

    public static void setUser(Context context, Enterprise enterprise, Player player) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (enterprise != null) {
            editor.putString(PREFERENCE_USER, new Gson().toJson(enterprise));
            editor.putInt(PREFERENCE_TYPE_USER, 0);
        } else if (player != null) {
            editor.putString(PREFERENCE_USER, new Gson().toJson(player));
            editor.putInt(PREFERENCE_TYPE_USER, 1);
        }
        editor.apply();
    }

    public static int getTypeUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        return sharedPreferences.getInt(PREFERENCE_TYPE_USER, -1);
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        sharedPreferences.edit().clear().apply();
    }

    public static Enterprise getUserEnteprise (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        String json = sharedPreferences.getString(PREFERENCE_USER, null);

        if (json != null) {
            return new Gson().fromJson(json, Enterprise.class);
        } else {
            return null;
        }
    }

    public static Player getUserPlayer (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        String json = sharedPreferences.getString(PREFERENCE_USER, null);

        if (json != null) {
            return new Gson().fromJson(json, Player.class);
        } else {
            return null;
        }
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCE_TOKEN, token);
        editor.apply();
    }

    public static String getToken (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        String token = sharedPreferences.getString(PREFERENCE_TOKEN, null);
        return token;
    }
}
