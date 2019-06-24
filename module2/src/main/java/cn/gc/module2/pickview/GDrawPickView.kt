package cn.gc.module2.pickview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

/**
 * Created by 宫成 on 2019-06-20 14:28.
 */
class GDrawPickView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    companion object {
        const val MaxTextAlpha = 255f
        const val MinTextAlpha = 120f
        const val MarginScale = 2.8f
    }


    private lateinit var mPaint: Paint
    private val mTextColor = 0x333333

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.color = mTextColor
    }


    private lateinit var mDataList: MutableList<String>
    private var mCurrentSelected: Int = 0
    fun setData(dataList: MutableList<String>) {
        mDataList = dataList
        mCurrentSelected = dataList.size / 2
        invalidate()
    }

    private var mViewHeight = 0
    private var mViewWidth = 0
    //大字体大小
    private var mMaxTextSize = 0f
    //小字体大小
    private var mMinTextSize = 0f
    private var isMeasured = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        mMaxTextSize = mViewHeight / 4f
        mMinTextSize = mMaxTextSize / 2f
        isMeasured = true
    }

    //滑动的距离
    private var mMoveLen = 0f


    override fun onDraw(canvas: Canvas) {
        if (isMeasured) drawPickView(canvas)
    }

    private fun drawPickView(canvas: Canvas) {
        //先画选中的
        val scale = formulaOfTextSize(mViewHeight / 4f, mMoveLen)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint.textSize = size
        mPaint.alpha = ((MaxTextAlpha - MinTextAlpha) * scale + MinTextAlpha).toInt()

        val x = mViewWidth / 2f
        val y = mViewHeight / 2f + mMoveLen
        val fmi = mPaint.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2f + fmi.top / 2))

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint)

        //再画上面的
        var i = 1
        while (mCurrentSelected - i >= 0) {
            drawOtherText(canvas, i, -1)
            i++
        }

        //再画下面的
        var j = 1
        while (mCurrentSelected + j < mDataList.size) {
            drawOtherText(canvas, i, 1)
            j++
        }
    }

    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val x = MarginScale * mMinTextSize * position + type * mMoveLen
        val scale = formulaOfTextSize(mViewHeight / 4f, x)
    }

    private fun formulaOfTextSize(x0: Float, x: Float) =
        with(1 - Math.pow(x / x0.toDouble(), 2.0)) {
            if (this < 0) 0f else this.toFloat()
        }
}