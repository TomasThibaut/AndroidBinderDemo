package cn.gc.module2.rxjava

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.fragment.app.FragmentActivity
import cn.gc.module2.R
import cn.gc.module2.logi
import cn.gc.module2.utils.CpuUtils
import cn.gc.module2.utils.FinishedCallback
import cn.gc.module2.utils.MultiFinishCheckQueue
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_rxjava.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.lang.NullPointerException
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

class RxJavaActivity : AppCompatActivity() {
    companion object {
        fun launchActivity(aty: FragmentActivity) =
            aty.startActivity(Intent(aty, RxJavaActivity::class.java).apply { })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)
//        rxjava01()
//        rxjava02()
//        rxjava03()
//        rxjava04()
//        startGuardPolling()
        rxjava05()
    }

    val i = 10
    var j = 2
    private fun rxjava06() {

    }
    private fun rxjava05() {

        val multiFinishCheckQueue = MultiFinishCheckQueue{
            logi("rxjava05 isAllFinished")
        }
        multiFinishCheckQueue.add("ScheduledThreadPoolExecutor")
        ScheduledThreadPoolExecutor(1, ThreadFactory{
            Thread(it)
        })
            .scheduleAtFixedRate(Runnable {
                multiFinishCheckQueue.finish("ScheduledThreadPoolExecutor")
                logi("rxjava06 click")
            }, 15, 5, TimeUnit.SECONDS)

        val subscribe1 = Observable.interval(5, 3, TimeUnit.SECONDS)
            .doOnSubscribe {
                multiFinishCheckQueue.add("1")
                logi("rxjava05 multiFinishCheckQueue.add(\"1\")")
            }
            .observeOn(Schedulers.newThread())
            .subscribe {
                multiFinishCheckQueue.finish("1")
                logi("rxjava05 1, click")
            }

        val subscribe2 = Observable.interval(10, 3, TimeUnit.SECONDS)
            .doOnSubscribe {
                multiFinishCheckQueue.add("2")
                logi("rxjava05 multiFinishCheckQueue.add(\"2\")")
            }
            .observeOn(Schedulers.newThread())
            .subscribe {
                logi("rxjava05 2, click")
                multiFinishCheckQueue.finish("2")
            }


        /* var dis05: Disposable? = null
         dis05 = Observable.interval(0, 2, TimeUnit.SECONDS)
             .subscribe({

                 logi("rxjava05 ${i / j}")
             }, {
                 dis1.dispose()
                 logi("rxjava05 ${it.localizedMessage} / dis05 = ${dis05?.isDisposed} / dis1 = ${dis1.isDisposed}")
             })
         dis1.add(dis05)*/

//        startGuardPolling()
        /*val a = applyAction.debounce(1, TimeUnit.SECONDS)
            .subscribe ({
                logi("applyAction: $it")
            },{
                logi("applyAction error : ${it.localizedMessage}")

            })*/
        /*Thread {
            logi("isRelease1")
            while (isRelease) {

            }
            logi("isRelease2")
        }.start()*/
    }

    private var isRelease = true

    private val applyAction = PublishSubject.create<Int>()

    private fun startGuardPolling() {
        if (disGuard.size() > 0) disGuard.clear()
        val o = Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { disGuard.add(it) }
            .subscribe({
                logi("startGuardPolling hello: ${Thread.currentThread().name}")
                if (it == 3L)
                    Thread.sleep(10000)
                logi("startGuardPolling bye: ${Thread.currentThread().name}")
            }, {
                logi("startGuardPolling ${it.localizedMessage}")
            })

//        Observable.interval(1, TimeUnit.SECONDS,Schedulers.newThread())
//            .doOnSubscribe { disGuard.add(it) }
//            .subscribe {
//                logi("startGuardPolling: ${Thread.currentThread().name}")
//            }
    }


    var time = 0L
    val duration: Long = 5 * 1000
    var count = 0
    fun click02(view: View) {
        /* val currentClickTime = SystemClock.elapsedRealtime()
         if (currentClickTime - time > duration) {
             count = 0
             time = currentClickTime
         }
         count++

         if (count == 5) {
             logi("开启成功")
             count = 0
             time = 0L
         } else {
             logi("还有${5 - count}次开启")
         }*/


        j = 0
//        val cpuUsage = CpuUtils.getCpuUsage()
//        logi("cpuUsage: $cpuUsage")
//        isRelease = false
//        applyAction.onNext(10*Math.random().toInt())
        /*Observable.create<Int> {
            return@create
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                disGuard.clear()
            }*/

    }

    private var dis1: CompositeDisposable = CompositeDisposable()
    private var disGuard: CompositeDisposable = CompositeDisposable()

    private fun rxjava04() {

        /* if (action.hasObservers()) return
         action.sample(1, TimeUnit.SECONDS)
             .doOnSubscribe { dis1.add(it) }
             .observeOn(Schedulers.newThread())
             .doOnNext { logi("1") }
             .doOnNext { logi("2") }
             .doOnNext { logi("3") }
             .subscribe({
                 logi("ititit = $it")
 //                throw Throwable("list is empty")
                 throw NoSuchElementException("List is empty.")
             }, {
                 logi("ititit error2 = ${action.hasObservers()}")
                 logi("ititit error = ${dis?.isDisposed}")
             })*/


        val ob = Observable.interval(1, TimeUnit.SECONDS, Schedulers.newThread())
            .doOnSubscribe {
                logi("add it $it")
                dis.add(it)
            }
        subscribe = ob.subscribe({
            logi("ititit = $it")
            throw Throwable("list is empty")
        }, {
            aaaa()

        })
    }

    var dis: CompositeDisposable = CompositeDisposable()
    var subscribe: Disposable? = null

    private fun aaaa() {
        logi("ititit error = ${subscribe?.isDisposed}")
        logi("ititit error2 = ${dis.isDisposed}")
        logi("aaaa it $subscribe")
    }

    object window {

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEventObserve(event: Int) {
        logi("aaaa : $event")
    }


    private fun postEvent() {
        EventBus.getDefault().post(1)
    }


    val action = PublishSubject.create<String>()

    @SuppressLint("CheckResult")
    private fun rxjava03() {
        logi("rxjava03 : ")
        val PAC = "com.tencent.mm"
        val path = "/data/data/sdfsdfsd/com.tencent.mm/MicroMsg/37b1dc8a4a681d46bd764c56a2d37c12/EnMicroMsg.db"
        val mmIndex = path.indexOf("com.tencent.mm")
        val pck = path.substring("/data/data/".length, mmIndex)
        val mmPath = path.substring(0, mmIndex + PAC.length + 1)

        logi("rxjava03 : pck = $pck")
        logi("rxjava03 : mmPath = $mmPath")

        for (i in 0..100) {
            if (action.hasObservers()) continue
            action
                .delay(3, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    logi("rxjava03 : onNext  a = $a, thread: ${Thread.currentThread()}")
                }, {
                    logi("error")
                })
        }
    }

    var a = 0
}
