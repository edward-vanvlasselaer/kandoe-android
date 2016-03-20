package be.kdg.kandoe.kandoe.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import be.kdg.kandoe.kandoe.dom.Theme;

/**
 * Created by Edward on 19/03/2016.
 */
public class GenericSharedStorage<T> {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Class<T> clazz;

    public GenericSharedStorage(Application activity, Class<T> clazz) {
        this.clazz = clazz;
        editor = activity.getSharedPreferences("STORAGE", activity.MODE_PRIVATE).edit();
        prefs = activity.getSharedPreferences("STORAGE", activity.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setObject(String key, T object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key, json);
        editor.commit();
    }

    public T getObject(String key) {
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        T obj = gson.fromJson(json, clazz);
        return obj;
    }

    public String getValue(String key) {
        String restoredText = prefs.getString(key, null);
        if (restoredText != null) {
            return restoredText;
        } else
            return null;
    }
}
