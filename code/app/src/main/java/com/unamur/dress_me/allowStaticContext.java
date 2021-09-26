package com.unamur.dress_me;

import android.app.Application;
import android.content.Context;

/**
 * @author Amélie Dieudonné
 * @version 1.0
 * Allow to call the context in a static field
 */
public class allowStaticContext extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        allowStaticContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return allowStaticContext.context;
    }
}
