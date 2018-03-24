package com.example.phil.myapplication.app;

import android.app.Application;

/**
 * Created by habibokanla on 24/03/2018.
 */

public class App extends Application {

    public App() {
        INSTANCE = this;
    }

    private static App INSTANCE;

    public static App getApp() {
        return INSTANCE;
    }
}
