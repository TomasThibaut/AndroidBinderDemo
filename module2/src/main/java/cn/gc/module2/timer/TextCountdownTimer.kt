package cn.gc.module2.timer

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView

/**
 * Created by 宫成 on 2019/5/29 上午11:38.
 */
class TextCountdownTimer(context: Context, attributeSet: AttributeSet?) : TextView(context, attributeSet),
    CountdownInterface {
    constructor(context: Context) : this(context, null)

    private lateinit var timer: SecCountDownTimer

    override fun start() {
        Log.i("G_C", "onTick start:")
        timer.start()
    }

    override fun stop() {
        timer.cancel()
    }

    override fun setCountTime(millisInFuture: Long, countDownInterval: Long) {
        timer = object : SecCountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                Log.i("G_C", "onTick onFinish: ")
                tickTime(0)
                onCountDownFinished?.onFinish()
            }

            override fun onTick(secUntilFinished: Int) {
                tickTime(secUntilFinished)
                Log.i("G_C", "onTick: $secUntilFinished")
            }
        }
    }

    private fun tickTime(secUntilFinished: Int) {
        this.text = CountDownTimeUtils.getTimeTextFromSec(secUntilFinished, CountDownTimeUtils.TYPE_MM_SS)
    }


    var onCountDownFinished: OnCountDownFinished? = null

    interface OnCountDownFinished {
        fun onFinish()
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }
}