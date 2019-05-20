package cn.gc.androidbinderdemo;

import android.app.Application;

/**
 * Created by 宫成 on 2019/5/20 下午2:38.
 */
public class MyApp extends Application {
    public static MyApp application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
