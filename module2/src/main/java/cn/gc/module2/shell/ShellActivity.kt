package cn.gc.module2.shell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.gc.module2.R
import cn.gc.module2.enumstaff.a
import cn.gc.module2.logi
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val executor:Executor = Executors.newScheduledThreadPool(30)
//val executor:Executor = Executors.newSingleThreadExecutor()

class ShellActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shell)
    }

    val disposableContainer = CompositeDisposable()
    fun click01(view: View) {
       /* var s : Subscription? = null
        val f = Flowable.create<Int>(FlowableOnSubscribe {
            for (i in 0..100) {
                it.onNext(i)
            }
            it.onComplete()
        }, BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.from(executor))
            .doOnSubscribe { s = it.apply { request(10) } }

            val dis = f.subscribe({
                ShellUtils.execCmd(Keys.topActivityFromADB)
                    .also {
                        logi("size = ${++index} /click01 return : ${Thread.currentThread().name} / ${it}")
                    }
                s?.request(10)
            }, {}, {}, {
//                it.request(10)
            })

        val runnable = Runnable {
            //            ShellUtils.execCmd(Keys.topActivityFromADB).also { logi("click01 return : ${Thread.currentThread().name}") }
            logi("click01: ${Thread.currentThread().name}")
        }*/
        for (i in 0..100) {
//            ShellUtils.execCmd(Keys.topActivityFromADB).also { logi("$i/click01 return : ${Thread.currentThread().name} / ${it}") }


//            Thread(runnable).start()

//            Observable.just(1).subscribeOn(AndroidSchedulers.mainThread())
            Observable.just(1).subscribeOn(Schedulers.from(executor))
                .doOnSubscribe { disposableContainer.add(it) }
                .subscribe(
                    {
//                        ShellUtils.a()
                        ShellUtils.execCmd(Keys.topActivityFromADB)
                            .also {
                                logi("size = ${++index} /click01 return : ${Thread.currentThread().name} / ${it}")
                            }
                    },
                    { logi("click01: ${it.localizedMessage}") })
        }
    }

    var index = 0;

    fun click02(view: View) {
        ShellUtils.execCmd(Keys.WECHAT_FTSAddFriendUI)
    }

    fun click03(view: View) {
        ShellUtils.execCmd("dumpsys window windows | grep mCurrent").also { logi("click03: $it") }
    }
}
