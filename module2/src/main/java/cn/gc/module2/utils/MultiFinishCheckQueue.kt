package cn.gc.module2.utils

import android.util.Log.i
import cn.gc.module2.logi
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by 宫成 on 2019-08-12 17:12.
 */
class MultiFinishCheckQueue(private val actionOnAllFinish: () -> Unit) : MultiFinishCheck {
    override fun add(key: String) {
        if (isDisposed) return
        mQueue[key] = FinishCallbackEntity(key, 0)
    }

    override fun finish(key: String) {
        if (isDisposed) return
        mQueue[key]?.let {
            it.isFinished = 1
            mQueue[key] = it
        }
        checkIsFinished()
    }

    private val mQueue = ConcurrentHashMap<String, FinishCallbackEntity>()

    private fun checkIsFinished() {
        var isAllFinished = true
        val iterator = mQueue.iterator()
        while (mQueue.isNotEmpty() && iterator.hasNext()) {
            val currentBoolean = iterator.next().value.isFinished

            isAllFinished = isAllFinished && currentBoolean == 1
            if (currentBoolean == 1) {
                iterator.remove()
            }
        }

        if (isAllFinished) {
            actionOnAllFinish.invoke()
            isDisposed = true
        }
    }

    var isDisposed = false
        private set

    private data class FinishCallbackEntity(
        val key: String,
        var isFinished: Int
    )



    companion object {

        @JvmStatic
        fun main(args: Array<String>) {


        }
    }


}


interface MultiFinishCheck {
    fun add(key: String)
    fun finish(key: String)
}

interface FinishedCallback {
    fun isAllFinished()
}
