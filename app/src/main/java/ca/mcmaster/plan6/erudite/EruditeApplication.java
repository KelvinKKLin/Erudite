package ca.mcmaster.plan6.erudite;

import android.app.Application;
import android.content.Context;

public class EruditeApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
