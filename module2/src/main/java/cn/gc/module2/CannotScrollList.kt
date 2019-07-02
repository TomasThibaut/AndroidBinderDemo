package cn.gc.module2

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by 宫成 on 2019/6/12 下午8:10.
 */
class CannotScrollList(context:Context,attributeSet: AttributeSet):
    androidx.recyclerview.widget.RecyclerView(context,attributeSet) {
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return true
    }
}