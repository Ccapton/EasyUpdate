package com.capton.eudd;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by capton on 2018/3/2.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

    }
}
