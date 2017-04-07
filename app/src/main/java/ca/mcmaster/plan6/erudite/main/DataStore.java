package ca.mcmaster.plan6.erudite.main;

import android.content.Context;
import android.content.SharedPreferences;

import ca.mcmaster.plan6.erudite.R;

/**
 * Single class for interfacing with the Android shared preferences API.
 */
public class DataStore {
    // Singleton object.
    private static final DataStore ourInstance = new DataStore();

    private static Context context;
    private static SharedPreferences shared_pref;

    /**
     * Singleton constructor.
     */
    private DataStore() {
        // Get application context.
        context = EruditeApplication.getContext();
        // Get shared preferences.
        shared_pref = context.getSharedPreferences(
                context.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
    }

    /**
     * Get DataStore instance.
     * @return
     */
    static DataStore get() {
        return ourInstance;
    }

    /**
     * Store key-value pair in shared preferences.
     * @param resID key
     * @param value
     */
    public static void store(int resID, String value) {
        SharedPreferences.Editor editor = shared_pref.edit();
        editor.putString(context.getString(resID), value);
        editor.commit();
    }

    /**
     * Get value for a given key.
     * @param resID
     * @return Value store for that key.
     */
    public static String load(int resID) {
        return shared_pref.getString(context.getString(resID), null);
    }
}
