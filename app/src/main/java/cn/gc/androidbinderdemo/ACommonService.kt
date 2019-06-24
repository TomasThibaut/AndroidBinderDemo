package cn.gc.androidbinderdemo

import android.app.Service
import android.content.Intent
import android.os.*
import java.lang.ref.WeakReference

/**
 * Created by 宫成 on 2019/5/24 下午2:33.
 */
class ACommonService : Service() {
    private lateinit var mServiceHandler: ServiceHandler
    private lateinit var mServiceLooper: Looper
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        val thread = HandlerThread("ServiceStartArguments")
        thread.start()
        mServiceLooper = thread.looper
        mServiceHandler = ServiceHandler(mServiceLooper, WeakReference(this))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    fun stop() {
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private inner class ServiceHandler(
        looper: Looper,
        private val service: WeakReference<Service>
    ) : Handler(looper) {

        override fun handleMessage(msg: Message?) {
            Thread.sleep(4000)
            service.get()?.stopSelf()
        }
    }
}