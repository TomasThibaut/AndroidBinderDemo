package cn.gc.module2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import cn.gc.module2.chatagenttest.ChatTestActivity
import cn.gc.module2.databinding.ItemBinding
import cn.gc.module2.pickview.PickViewActivity
import cn.gc.module2.rxjava.RxJavaActivity
import cn.gc.module2.shell.ShellActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timerTask

fun dip2px(context: Context, dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics(context))


fun getDisplayMetrics(context: Context): DisplayMetrics {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(metrics)
    return metrics
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val timer = findViewById<TextCountdownTimer>(R.id.timerText)
        //测试13分15秒
//        timer.setCountTime(1000 * 60 * 13 + 15 * 1000, 1000)
//        timer.onCountDownFinished = object : TextCountdownTimer.OnCountDownFinished {
//            override fun onFinish() {
//                Toast.makeText(this@MainActivity, "finished", Toast.LENGTH_SHORT).show()
//            }
//        }
//        timer.start()
        val floater = findViewById<View>(R.id.floater)

        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                height = floater.height.toFloat()
//                Log.i("G_C", "MainActivity height = $height")
            }
        })
        animator?.addUpdateListener {
            val value = (it.animatedValue as Float) * transX
            floater.translationX = value
            floater.translationY = getTansYFromX(value)
//            Log.i("G_C", "MainActivity translationX = ${floater.translationX}")
//            Log.i("G_C", "MainActivity translationY = ${floater.translationY}")
        }
        animator?.duration = 10000


        val datalist = mutableListOf<String>()
        for (i in 0..24) {
            datalist.add(i.toString())
        }
        datalist.reverse()
        list.layoutManager = SlowScrollLayoutManager(this)
        list.adapter = BaseBindingRecyclerAdapter<String, ItemBinding>(
            datalist,
            R.layout.item
        ) { s, i, _ ->
            i.data = s
            i.root.setOnClickListener {
                startAnimation(it.bottom)
            }
        }


    }

    fun startAnimation(totalY: Int) {
        val y = totalY + list.bottom
        val deltaX = dip2px(this, 100f)
        val a: Float = y / deltaX / deltaX
        ValueAnimator.ofFloat(0f, deltaX).apply {
            addUpdateListener {
                val x = it.animatedValue as Float
                floater.translationX = -1 * x
                floater.translationY = a * x * x

            }
        }.start()
    }

    val transX = 3000f
    var animator: ValueAnimator? = null

    var height: Float = 0f
    fun getTansYFromX(x: Float): Float {
        val fl = height * x * x / 5000
//        Log.i("G_C", "MainActivity getTansYFromX = $fl")

        return (fl)
    }

    fun start(view: View) {
        animator?.start()
//          RxJavaActivity.launchActivity(this)
        startActivity(Intent(this, ShellActivity::class.java))

    }


    fun start2(view: View) {
        Timer().schedule(timerTask {
            list.smoothScrollToPosition(index++)

        }, 2000)

        startActivity(Intent(this, PickViewActivity::class.java))
    }

    var index = 0


    class SlowScrollLayoutManager(context: Context) : androidx.recyclerview.widget.LinearLayoutManager(context) {
        var millisecondsPerInch = 10f;

        override fun smoothScrollToPosition(recyclerView: androidx.recyclerview.widget.RecyclerView?, state: androidx.recyclerview.widget.RecyclerView.State?, position: Int) {

            val linearSmoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView?.context) {
                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
//                    return super.computeScrollVectorForPosition(targetPosition)
                    return this@SlowScrollLayoutManager.computeScrollVectorForPosition(targetPosition);
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return millisecondsPerInch / (displayMetrics?.density!!)
                }

                override fun calculateDtToFit(
                    viewStart: Int,
                    viewEnd: Int,
                    boxStart: Int,
                    boxEnd: Int,
                    snapPreference: Int
                ): Int {
                    return boxStart - viewStart
                }
            }
            linearSmoothScroller.targetPosition = position
            this.startSmoothScroll(linearSmoothScroller)
        }

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return super.computeScrollVectorForPosition(targetPosition)
        }
    }
}


