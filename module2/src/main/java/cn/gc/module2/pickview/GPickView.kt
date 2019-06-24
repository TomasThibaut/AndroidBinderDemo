package cn.gc.module2.pickview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.gc.module2.BaseBindingRecyclerAdapter
import cn.gc.module2.R
import cn.gc.module2.Utils
import cn.gc.module2.databinding.GpickviewItemLayoutBinding
import cn.gc.module2.logi
import java.util.jar.Attributes

/**
 * Created by 宫成 on 2019-06-20 10:24.
 */
class GPickView(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    init {
        orientation = HORIZONTAL
        build3List()
    }

    private fun build3List() {
        val data1 = mutableListOf<String>().apply { add("上午");add("下午") }
        val data2 = mutableListOf<String>()
        val data3 = mutableListOf<String>()

        for (i in 1..12) {
            data2.add(i.toString())
        }

        for (i in 1..59) {
            data3.add(i.toString())
        }


        val halfDayList = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = LayoutParams(Utils.dip2px(context, 35f).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
            adapter = BaseBindingRecyclerAdapter<String, GpickviewItemLayoutBinding>(
                data1,
                R.layout.gpickview_item_layout
            ) { text, binding, p ->
                binding.text = text
            }
        }


        val hourList = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = LayoutParams(Utils.dip2px(context, 35f).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
//            addOnScrollListener(scrollListener)
            adapter = BaseBindingRecyclerAdapter<String, GpickviewItemLayoutBinding>(
                data2,
                R.layout.gpickview_item_layout
            ) { text, binding, p ->
                binding.text = text
            }
        }

        val minList = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = LayoutParams(Utils.dip2px(context, 35f).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
//            addOnScrollListener(scrollListener)
            adapter = BaseBindingRecyclerAdapter<String, GpickviewItemLayoutBinding>(
                data3,
                R.layout.gpickview_item_layout
            ) { text, binding, _ ->
                binding.text = text
            }
        }

        addView(halfDayList)
        addView(hourList)
        addView(minList)
    }

    var deltaX = 0

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            deltaX += dx
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == 0) {
                logi("scrollListener: $newState")
                recyclerView.smoothScrollBy(-deltaX, 0)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}