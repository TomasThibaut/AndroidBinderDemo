package cn.gc.androidbinderdemo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger

/**
 * Created by 宫成 on 2019/5/24 下午5:15.
 */
class MessengerService : Service() {
    val mMessenger = Messenger(MessengerHandler())
    override fun onBind(intent: Intent?): IBinder {
        return mMessenger.binder
    }

    private inner class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message?) {

        }
    }
}