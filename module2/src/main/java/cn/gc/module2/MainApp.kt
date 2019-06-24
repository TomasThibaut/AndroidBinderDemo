package cn.gc.module2

import android.app.Application
import cn.gc.componetlib.IAppComponent

/**
 * Created by 宫成 on 2019/5/21 上午9:57.
 */
class MainApp : Application(), IAppComponent {

    companion object {
        @JvmStatic
        var application: Application? = null
    }

    override fun init(app: Application) {
        application = app
    }

    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}