package cn.gc.module2.timer

/**
 * Created by 宫成 on 2019/5/29 上午11:30.
 */
interface CountdownInterface {
    fun start()
//    fun pause()

    fun setCountTime(millisInFuture:Long,countDownInterval: Long)
//    fun reset()
    fun stop()
}