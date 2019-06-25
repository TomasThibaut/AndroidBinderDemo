package cn.gc.module2.pickview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.gc.module2.R
import cn.gc.module2.logi
import kotlinx.android.synthetic.main.activity_pick_view.*

class PickViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_view)
        val data1 = mutableListOf("上午", "下午")
        val data2 = mutableListOf<String>()
        val data3 = mutableListOf<String>()
        for (i in 1..24) {
            var str = i.toString()
            if (str.length < 2) str = "0$str"
            data2.add(str)
        }
        pickView1.setData(data1)
        pickView2.setData(data2)
        for (i in 0..59) {
            var str = i.toString()
            if (str.length < 2) str = "0$str"
            data3.add(str)
        }
        pickView3.setData(data3)

        pickView1.setOnSelectListener {
            logi("PickViewActivity 上下午: $it")
            logi(
                "PickViewActivity 现在的选择: ${data1[pickView1.currentSelected]} " +
                        "${data2[pickView2.currentSelected]}时 " +
                        "${data3[pickView3.currentSelected]}分"
            )
        }
        pickView2.setOnSelectListener {
            logi("PickViewActivity 小时: $it")
            logi(
                "PickViewActivity 现在的选择: ${data1[pickView1.currentSelected]} " +
                        "${data2[pickView2.currentSelected]}时 " +
                        "${data3[pickView3.currentSelected]}分"
            )
        }
        pickView3.setOnSelectListener {
            logi("PickViewActivity 分钟: $it")
            logi(
                "PickViewActivity 现在的选择: ${data1[pickView1.currentSelected]} " +
                        "${data2[pickView2.currentSelected]}时 " +
                        "${data3[pickView3.currentSelected]}分"
            )
        }
    }
}
