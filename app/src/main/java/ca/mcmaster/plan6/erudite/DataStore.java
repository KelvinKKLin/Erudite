package ca.mcmaster.plan6.erudite;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStore {
    private static final DataStore ourInstance = new DataStore();

    private static Context context;
    private static SharedPreferences shared_pref;

    private DataStore() {
        context = EruditeApplication.getContext();
        shared_pref = context.getSharedPreferences(
                context.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
    }

    static DataStore get() {
        return ourInstance;
    }

    public static void store(int resID, String value) {
        SharedPreferences.Editor editor = shared_pref.edit();
        editor.putString(context.getString(resID), value);
        editor.commit();
    }

    public static String load(int resID) {
        return shared_pref.getString(context.getString(resID), null);
    }
}
