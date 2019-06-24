package cn.gc.module2.timer

import android.os.Handler
import android.os.Message
import android.os.SystemClock
import cn.gc.module2.timer.CountDownTimer.CountDownTimerHandler.Companion.MSG
import java.lang.ref.WeakReference

/**
 * 原厂sdk25版本,有bug,具体可以看android team wiki
 * 注意点: [onTick]内不可以直接强转为Int类型, 因为发消息有延迟,会损失大量精度,
 * 举例:回调还剩两秒但返回的其实是1.9秒, 强转Int后, 直接变为1秒, 损失掉0.9秒的精度
 * Created by 宫成 on 2019/5/29 下午4:47.
 */
abstract class CountDownTimer(private val mMillisInFuture: Long, private val mCountdownInterval: Long) {
    internal var mStopTimeInFuture: Long = 0
    internal var mCancelled = false

    private lateinit var mHandler: CountDownTimerHandler

    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    @Synchronized
    fun start(): CountDownTimer {
        mHandler = CountDownTimerHandler(WeakReference<CountDownTimer>(this))

        mCancelled = false
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }

    abstract fun onFinish()
    abstract fun onTick(millisUntilFinished: Long)

    class CountDownTimerHandler(private val timerReference: WeakReference<CountDownTimer>) : Handler() {
        companion object {
            const val MSG = 1
        }

        private val timer = timerReference.get()
        @Synchronized
        override fun handleMessage(msg: Message?) {
            if (timer == null) return
            if (timer.mCancelled) {
                return
            }
            val millisLeft: Long = timer.mStopTimeInFuture - SystemClock.elapsedRealtime()
            if (millisLeft <= 0) {
                timer.onFinish()
            } else {
                val lastTickStart = SystemClock.elapsedRealtime()
                timer.onTick(millisLeft)

                val lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart
                var delay = 0L

                if (millisLeft < timer.mCountdownInterval) {
                    delay = millisLeft - lastTickDuration
                    if (delay < 0) delay = 0L
                } else {
                    delay = timer.mCountdownInterval - lastTickDuration
                    while (delay < 0) delay += timer.mCountdownInterval
                }
                sendMessageDelayed(obtainMessage(MSG), delay)
            }
        }
    }
}