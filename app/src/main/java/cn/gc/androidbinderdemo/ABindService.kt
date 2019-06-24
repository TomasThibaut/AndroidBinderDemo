package cn.gc.androidbinderdemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.EditText

/**
 * Created by 宫成 on 2019/5/24 下午4:45.
 */
class ABindService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}

