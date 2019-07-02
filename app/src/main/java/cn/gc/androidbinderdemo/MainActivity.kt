package cn.gc.androidbinderdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mRemoteStudentManager: StudentManagerAIDL? = null
    private var mStudentSize = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Student.NAME = "ChangedName"
        btn1.setOnClickListener { startActivity(Intent(this@MainActivity, SecondActivity::class.java)) }
        logi(Student.NAME)
        val conn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mRemoteStudentManager = null
                logi("onServiceDisconnected . threadName: ${Thread.currentThread().name}")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                //获取到StudentManagerAIDL对象
                mRemoteStudentManager = StudentManagerAIDL.Stub.asInterface(service)

            }

        }
        bindService(Intent(this@MainActivity, StudentManagerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    fun get_student_list(view: View) {
        toast("正在获取...学生列表")
        //模拟耗时操作
        Thread(Runnable {
            val studentList = mRemoteStudentManager?.studentList
            mStudentSize = studentList?.size ?: 0
            logi("获取到学生列表: ${studentList?.toString()}")
        }).start()
    }

    fun addStudent(view: View) {
        mRemoteStudentManager?.addStudent(
            Student(mStudentSize + 1, "卢本伟${mStudentSize + 1}", "???").also {
                logi("添加一位学生$it")
            }
        )
    }

    fun btn5(view: View) {

    }

    fun btn6(view: View) {
        bindService(
            Intent(this, ABindService::class.java),
            object : ServiceConnection {
                override fun onServiceDisconnected(name: ComponentName?) {

                }

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                }
            },
            Context.BIND_AUTO_CREATE
        )
    }

}

fun logi(content: String?) = Log.i("G_C", content)
fun toast(content: String?) = Toast.makeText(MyApp.application, content, Toast.LENGTH_SHORT).show()
