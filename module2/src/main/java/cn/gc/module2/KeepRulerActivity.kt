package cn.gc.module2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_keep_ruler.*

fun logi(content: String?) = Log.i("G_C", content)

class KeepRulerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keep_ruler)
        ruler_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            true
        )

        ruler_list.addOnScrollListener(listListener)

        val headerFooterWidth = Utils.getScreenWidth(this) / 2
        val paramHeader = androidx.recyclerview.widget.RecyclerView.LayoutParams(headerFooterWidth, Utils.dip2px(this, 80f).toInt())
        val paramFooter = androidx.recyclerview.widget.RecyclerView.LayoutParams(headerFooterWidth, Utils.dip2px(this, 80f).toInt())

        val header = View(this).apply { layoutParams = paramHeader }
        val footer = View(this).apply { layoutParams = paramFooter }

//        val adapter = MAdapter().apply { addHeader(header);addFooter(footer) }

//        ruler_list.adapter = adapter
    }

    val listListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            val xOffset = recyclerView.computeHorizontalScrollOffset()
            logi("KeepRulerActivity scrollX = $xOffset")
        }

        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
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
