package be.kdg.kandoe.kandoe.util;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.SharedPreferences;

/**
 * Created by Edward on 19/03/2016.
 */
public class SharedStorage{
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    public SharedStorage(Application activity) {
        editor = activity.getSharedPreferences("STORAGE", activity.MODE_PRIVATE).edit();
        prefs = activity.getSharedPreferences("STORAGE", activity.MODE_PRIVATE);
    }

    public void setValue(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key){
        String restoredText = prefs.getString(key, null);
        if (restoredText != null) {
            return restoredText;
        }
        else
            return null;
    }
}
