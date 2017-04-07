package ca.mcmaster.plan6.erudite.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by Varun on 2014-04-01.
 *
 */
public class EruditeApplication extends Application {

    /**
     * The context of the application
     */
    private static Context context;

    /**
     * This method defines the initialization behaviour of the application
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * This method returns the context of the application
     * @return  The context of the application
     */
    public static Context getContext() {
        return context;
    }
}
