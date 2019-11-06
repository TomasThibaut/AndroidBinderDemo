package cn.gc.module2.shell

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Looper
import android.widget.Toast
import cn.gc.module2.MainApp
import cn.gc.module2.logi
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscription
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.*
import java.util.logging.Logger

/**
 * Created by 宫成 on 2019-07-30 17:17.
 */
object ShellUtils {
    const val BACKPRESS_REQUEST_COUNT = 10L
    private val msgPublish = PublishSubject.create<Int>()
    private var subscription: Subscription? = null

    fun execCmd(command: String): CommandResult {
        return execCmd(arrayOf(command), true)
    }

    var index = 0

    fun a() {
        Thread.sleep(1000)
        logi("${++index} click01 ShellUtils.a() ${Thread.currentThread().name}")
    }
//    private fun doShellCommands(commands: Array<String>):CommandResult{
//
//    }

    private fun execCmd(commands: Array<String>, isRoot: Boolean, isNeedResultMsg: Boolean = true): CommandResult {
//        return CommandResult(0, ",", "").also {
//            Observable.just(1).observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//
//                    Toast.makeText(MainApp.application, "thread: ${Thread.currentThread().name}", Toast.LENGTH_LONG)
//                        .show()
//                }
//        }
        return getCommandResult(commands)
    }

//    @Synchronized
    private fun getCommandResult(commands: Array<String>): CommandResult {
        //        if (!msgPublish.hasObservers()) {
//            val subscribe = msgPublish
//                .toFlowable(BackpressureStrategy.BUFFER)
//                .doOnSubscribe { subscription = it.apply { request(BACKPRESS_REQUEST_COUNT) } }.subscribe {
//
//                }
//        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            logi("不应该在主线程调用shell命令, 会block")
        } else {
            Thread.sleep(80)
        }

        var result = -1

        logi("exec cmd is: " + Arrays.toString(commands))

        val process = Runtime.getRuntime().exec("su")
        var successResult: BufferedReader
        var errorResult: BufferedReader
        var successMessage = StringBuilder()
        var errorMessage = StringBuilder()
        val os = DataOutputStream(process!!.outputStream)
        for (command in commands) {
            os.write(command.toByteArray())
            os.writeBytes("\n")
            os.flush()
        }
        os.writeBytes("exit\n")
        os.flush()

        result = process.waitFor()

        successResult = BufferedReader(InputStreamReader(process.inputStream, "UTF-8"))
        errorResult = BufferedReader(InputStreamReader(process.errorStream, "UTF-8"))
        var readLine = successResult.readLine()
        while (readLine != null) {
            successMessage.append(readLine).append("\t")
            readLine = successResult.readLine()
        }

        readLine = errorResult.readLine()
        while (readLine != null) {
            errorMessage.append(readLine)
            readLine = errorResult.readLine()
        }
        subscription?.request(BACKPRESS_REQUEST_COUNT)
        logi("exec cmd is: subscribe finish")

        logi("exec cmd is: return")
        return CommandResult(
            result,
            successMessage.toString(),
            errorMessage.toString()
        )
    }
}