package cn.gc.module2.chatagenttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.gc.module2.R
import cn.gc.module2.logi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class ChatTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_test)
        EventBus.getDefault().register(this)
    }

    fun onBtnClick01(view: View) {
        postEvent()
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEventReceive(event: Event) {
        logi("onEventReceive: ${event.eventInt}")
        Observable.create<Int> {
            logi("onEventReceive: isOp1 = $isOp")
            if (isOp) return@create
            isOp = true
            logi("onEventReceive: isOp2 = $isOp")
            it.onNext(event.eventInt)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                //耗时操作
                Thread.sleep(200)
                isOp = false
                Observable.interval(100, TimeUnit.MILLISECONDS)
                    .take(2)
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        onEventReceive(Event(++eventInt))
                    }
            }, {
                //耗时操作

            })

    }

    private var isOp = false
    private fun postEvent() {
        onEventReceive(Event(++eventInt))
//        EventBus.getDefault().postSticky(Event(0))
        /*Observable.interval(100, TimeUnit.MILLISECONDS)
            .take(2)
            .subscribeOn(Schedulers.io())
            .subscribe {
                onEventReceive(Event(++eventInt))
            }*/
//        Observable.interval(100, TimeUnit.MILLISECONDS)
//            .take(2)
//            .subscribeOn(Schedulers.io())
//            .subscribe {
//                logi("onEventReceive test")
////                onEventReceive(Event(++eventInt))
//            }
//        Observable.interval(100, TimeUnit.MILLISECONDS)
//            .take(2)
//            .subscribeOn(Schedulers.io())
//            .subscribe {
//                logi("onEventReceive test")
////                onEventReceive(Event(++eventInt))
//            }
    }

    private var eventInt = 0

    data class Event(var eventInt: Int = 0)
}
