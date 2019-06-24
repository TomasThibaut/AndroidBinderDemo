package cn.gc.module2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by 宫成 on 2019/6/12 下午8:10.
 */
class CannotScrollList(context:Context,attributeSet: AttributeSet):RecyclerView(context,attributeSet) {
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return true
    }
}