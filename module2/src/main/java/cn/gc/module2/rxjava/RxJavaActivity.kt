package cn.gc.module2.rxjava

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.gc.module2.R
import cn.gc.module2.logi
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class RxJavaActivity : AppCompatActivity() {
    companion object {
        fun launchActivity(aty: FragmentActivity) =
            aty.startActivity(Intent(aty, RxJavaActivity::class.java).apply { })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)
        rxjava01()
    }

    /**
     * rule No1:onComplete或者onError之后,发射器的onNext会继续发送,但接收器不再接受onNext
     * rule No2:onComplete和onError 是各自唯一的,并且两者互斥.
     */
    private fun rxjava01() {
        logi("rxjava01 : ")
        val observable = Observable.create<Int>(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1).also { logi("rxjava01 : emitter onNext 1") }
                emitter.onNext(2).also { logi("rxjava01 : emitter onNext 2") }
                emitter.onNext(3).also { logi("rxjava01 : emitter onNext 3") }
                emitter.onComplete()
                emitter.onNext(4).also { logi("rxjava01 : emitter onNext 4") }
                emitter.onNext(5).also { logi("rxjava01 : emitter onNext 5") }
                emitter.onNext(6).also { logi("rxjava01 : emitter onNext 6") }
            }
        })
        observable.subscribe(object : Observer<Int> {
            lateinit var disposable: Disposable
            override fun onComplete() {
                logi("rxjava01 : onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                logi("rxjava01 : onSubscribe")
                disposable = d
            }

            override fun onNext(t: Int) {
                logi("rxjava01 : $t")
                if (t == 3) disposable.dispose()
            }

            override fun onError(e: Throwable) {
                logi("rxjava01 : ${e.message}")
            }
        })
        //有许多重载的方法,都是[io.reactivex.functions.Consumer]接口,可以消费上述的除了onSubscribe的所有事件.
        observable.subscribe({ /*onNext*/ }, { /*onError*/ }, { /*onComplete*/ })

    }
}
