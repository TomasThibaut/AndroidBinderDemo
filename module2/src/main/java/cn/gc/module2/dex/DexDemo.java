package cn.gc.module2.dex;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import dalvik.system.DexClassLoader;

import java.util.List;

/**
 * Created by 宫成 on 2019-09-03 17:47.
 */
public class DexDemo {
    public void init(PackageManager packageManager, ApplicationInfo selfApplicationInfo,ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Intent intent = new Intent();
        intent.setPackage("cn.gc.dextestapp");
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        ResolveInfo resolveInfo = resolveInfos.get(0);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        String packageName = activityInfo.packageName;
        String dexPath = activityInfo.applicationInfo.sourceDir;
        String libPath = activityInfo.applicationInfo.nativeLibraryDir;
        String dexOutputDir = selfApplicationInfo.dataDir;

        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputDir, libPath, classLoader);
        Class<?> clazz = dexClassLoader.loadClass(packageName+".Hello");
        Object obj = clazz.newInstance();
    }
}
