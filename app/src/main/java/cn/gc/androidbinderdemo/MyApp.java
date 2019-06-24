package cn.gc.androidbinderdemo;

import android.app.Application;
import cn.gc.componetlib.AppConfig;
import cn.gc.componetlib.IAppComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by 宫成 on 2019/5/20 下午2:38.
 */
public class MyApp extends Application implements IAppComponent {
    public static MyApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        init(this);
    }

    @Override
    public void init(@NotNull Application application) {
        for (String component : AppConfig.components) {
            try {
                Class<?> clazz = Class.forName(component);
                Object obj = clazz.newInstance();
                if (obj instanceof IAppComponent) {
                    ((IAppComponent) obj).init(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
