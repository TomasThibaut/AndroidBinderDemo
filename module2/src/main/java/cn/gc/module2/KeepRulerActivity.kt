package cn.gc.module2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_keep_ruler.*

fun logi(content: String?) = Log.i("G_C", content)

class KeepRulerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keep_ruler)
        ruler_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)

        ruler_list.addOnScrollListener(listListener)

        val headerFooterWidth = Utils.getScreenWidth(this) / 2
        val paramHeader = RecyclerView.LayoutParams(headerFooterWidth, Utils.dip2px(this, 80f).toInt())
        val paramFooter = RecyclerView.LayoutParams(headerFooterWidth, Utils.dip2px(this, 80f).toInt())

        val header = View(this).apply { layoutParams = paramHeader }
        val footer = View(this).apply { layoutParams = paramFooter }

//        val adapter = MAdapter().apply { addHeader(header);addFooter(footer) }

//        ruler_list.adapter = adapter
    }

    val listListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val xOffset = recyclerView.computeHorizontalScrollOffset()
            logi("KeepRulerActivity scrollX = $xOffset")
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    }


   /* class MAdapter : BaseRecyclerAdapter<MVH>() {
        override fun _getItemCount(): Int {

        }

        override fun _onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun _onBindViewHolder(holder: MVH?, realPosition: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    class MVH(view: View) : RecyclerView.ViewHolder(view) {

    }*/
}
