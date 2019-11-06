package cn.gc.module2.rxjava

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Xml
import android.view.View
import androidx.fragment.app.FragmentActivity
import cn.gc.module2.R
import cn.gc.module2.dex.DexDemo
import cn.gc.module2.logi
import cn.gc.module2.shell.Keys
import cn.gc.module2.shell.ShellUtils
import cn.gc.module2.sns.WeiXinUtils
import cn.gc.module2.utils.CpuUtils
import cn.gc.module2.utils.FinishedCallback
import cn.gc.module2.utils.MultiFinishCheckQueue
import dalvik.system.DexClassLoader
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_rxjava.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteQueryBuilder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.*
import java.lang.NullPointerException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.sql.Blob
import java.util.*
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
//        dexLoadWxClass()
//        readSnsDB()
//        DexDemo().init(packageManager,applicationInfo,classLoader)
            ::getHeadImage.invoke()
    }

    @SuppressLint("SdCardPath")
    private fun getHeadImage() {
        logi("getHeadImage")
        val path = "/data/data/com.tencent.mm/MicroMsg/last_avatar_dir/"
        val headFileDir = File(path)
        if (headFileDir.isDirectory) {
            val fileList = headFileDir.listFiles()
            if (fileList.isNotEmpty()) {
                val headImage:File = fileList[0]
                logi("getHeadImage headImage :$headImage")
            }
        }
    }

    private fun hello() {
        //跳转页面
        val result = ShellUtils.execCmd(Keys.findTemp)
        logi("result = $result")
    }

    /**
     * 动态加载微信的类, 并调用微信的方法
     */
    private fun dexLoadWxClass() {
        val intent = Intent()
        intent.setPackage("cn.gc.dextestapp")
        val list: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        val resolveInfo = list[0]
        val activityInfo: ActivityInfo = resolveInfo.activityInfo
        val div = System.getProperty("path.separator")
        val packageName = activityInfo.packageName
        val dexPath = activityInfo.applicationInfo.sourceDir
        val libPath = activityInfo.applicationInfo.nativeLibraryDir
        val dexOutputDir = applicationInfo.dataDir
        logi(
            """

            div = $div
            packageName = $packageName
            dexPath = $dexPath
            libPath = $libPath
            dexOutputDir = $dexOutputDir
        """.trimIndent()
        )


        val dexClassLoader = DexClassLoader(dexPath,dexOutputDir,libPath,classLoader)
        val clazz:Class<*> = dexClassLoader.loadClass("$packageName.Hello")
        val obj = clazz.newInstance()
//
//        val method = clazz.getMethod("trim",String::class.java)
//        val result = method.invoke(obj,"hello world")
//        if (result is String) {
//            logi("result = $result")
//        } else {
//            logi("result = $result")
//        }
    }

    @SuppressLint("SdCardPath")
    private fun readSnsDB() {
        SQLiteDatabase.loadLibs(application)

        val path = "/data/data/com.tencent.mm/MicroMsg/37b1dc8a4a681d46bd764c56a2d37c12/SnsMicroMsg.db"
//        val db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY)
//        db.query("SnsInfo",null,null,)
        val snsDb = SQLiteDatabase.openDatabase(
            path,
            "",
            null,
            SQLiteDatabase.NO_LOCALIZED_COLLATORS or SQLiteDatabase.OPEN_READONLY
        )
        logi("snsDb = $snsDb")
        val queryBuilder = SQLiteQueryBuilder().also { it.tables = "SnsInfo" }
        val cursor = queryBuilder.query(snsDb, null, "userName = ?", arrayOf("wxid_tlgkxiw49kvp22"), null, null, null)
//        val cursor = queryBuilder.query(snsDb, null, "userName = ?", arrayOf("wxid_tlgkxiw49kvp22"), null, null, null)
        cursor.moveToFirst()
        val snsInfoList = mutableListOf<SnsInfoEntity>()
        while (!cursor.isAfterLast) {
            val entity = SnsInfoEntity()
            entity.userName = cursor.getString(cursor.getColumnIndex("userName"))
            entity.content = cursor.getBlob(cursor.getColumnIndex("content"))
            entity.type = cursor.getInt(cursor.getColumnIndex("type"))
            snsInfoList.add(entity)
            cursor.moveToNext()
        }
        cursor.close()
        logi("${snsInfoList.size}")

        snsInfoList.forEach {

            val snsInfoEntity = it
//        logi("$snsInfoEntity")
            logi(String(snsInfoEntity.content!!, Charset.forName("UTF-8")))
            val s = String(snsInfoEntity.content!!, Charset.forName("UTF-8"))
//        val a = WeiXinUtils.dealWeiXinDataOfCircle("","","","","","",0,0L,String(snsInfoEntity.content!!, Charset.forName("UTF-8")),"")
//        logi("a = $a")
            snsInfoEntity.content?.let {
                val byteBuffer = ByteBuffer.allocate(it.size)
                byteBuffer.put(it)
                byteBuffer.flip()
                val charArray: CharArray = Charset.forName("UTF-8").decode(byteBuffer).array()
                val b = WeiXinUtils.dealWeiXinDataOfCircle(
                    "", "",
                    "", "",
                    "", "",
                    snsInfoEntity.type!!, 0L,
                    charArray, null
                )
                logi("b = $b")
            }
        }

    }

    data class SnsInfoEntity(
        var content: ByteArray? = null,
        var userName: String? = null,
        var type: Int? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            var result = content?.contentHashCode() ?: 0
            result = 31 * result + (userName?.hashCode() ?: 0)
            return result
        }
    }

    val i = 10
    var j = 2
    private fun rxjava06() {

    }

    private fun rxjava05() {

        val multiFinishCheckQueue = MultiFinishCheckQueue {
            logi("rxjava05 isAllFinished")
        }
        multiFinishCheckQueue.add("ScheduledThreadPoolExecutor")
        ScheduledThreadPoolExecutor(1, ThreadFactory {
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
                logi("startGuardPolling ${it.message}")
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
        hello()
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
