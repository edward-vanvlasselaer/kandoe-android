package be.kdg.kandoe.kandoe.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedStorage {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public SharedStorage(Application activity) {
        editor = activity.getSharedPreferences("STORAGE", Context.MODE_PRIVATE).edit();
        prefs = activity.getSharedPreferences("STORAGE", Context.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setObject(String key, Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key, json);
        editor.commit();
    }


    public String getValue(String key) {
        String restoredText = prefs.getString(key, null);
        if (restoredText != null) {
            return restoredText;
        } else
            return null;
    }
}
