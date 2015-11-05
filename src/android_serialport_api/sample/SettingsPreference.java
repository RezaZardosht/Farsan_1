package android_serialport_api.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Zardosht on 09/12/2015.
 */

public class SettingsPreference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String myString = prefs.getString("myStringName", "");
        Boolean myBoolean = prefs.getBoolean("myBooleanName", true);
    }

}
