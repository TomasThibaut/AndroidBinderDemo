package cn.gc.module2.pickview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.gc.module2.R
import kotlinx.android.synthetic.main.activity_pick_view.*

class PickViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_view)
        val data = mutableListOf<String>()
        for (i in 1..24) {
            data.add(i.toString())
        }
        pickView.setData(data)
    }
}
