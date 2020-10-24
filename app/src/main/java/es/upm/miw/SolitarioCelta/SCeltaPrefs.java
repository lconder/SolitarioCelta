package es.upm.miw.SolitarioCelta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SCeltaPrefs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SCeltaPrefsFragment())
                .commit();
        checkValues();
    }

    public static class SCeltaPrefsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
        }
    }

    public void checkValues() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SP.getString("player_name", "Random Player");
    }
}
