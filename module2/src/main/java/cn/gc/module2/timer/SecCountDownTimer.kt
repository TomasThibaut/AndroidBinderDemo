package cn.gc.module2.timer

import android.os.Handler
import android.os.Message
import android.os.SystemClock
import java.lang.ref.WeakReference
import java.math.BigDecimal

/**
 * Created by 宫成 on 2019/5/29 下午6:08.
 */
abstract class SecCountDownTimer(private val mMillisInFuture: Long, private val mCountdownInterval: Long) {
    internal var mStopTimeInFuture: Long = 0
    internal var mCancelled = false

    private lateinit var mHandler: CountDownTimerHandler

    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(CountDownTimerHandler.MSG)
    }

    @Synchronized
    fun start(): SecCountDownTimer {
        mHandler = CountDownTimerHandler(WeakReference(this))

        mCancelled = false
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(CountDownTimerHandler.MSG))
        return this
    }

    abstract fun onFinish()
    abstract fun onTick(secUntilFinished: Int)

    class CountDownTimerHandler(private val timerReference: WeakReference<SecCountDownTimer>) : Handler() {
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
                val secValue = BigDecimal(millisLeft / 1000.toDouble()).setScale(0, BigDecimal.ROUND_HALF_UP).toInt()
                timer.onTick(secValue)

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