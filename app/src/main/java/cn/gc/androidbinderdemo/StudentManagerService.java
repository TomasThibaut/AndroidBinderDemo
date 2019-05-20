package cn.gc.androidbinderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 宫成 on 2019/5/20 上午11:29.
 */
public class StudentManagerService extends Service {
    private AtomicBoolean isDestroyed = new AtomicBoolean(false);

    //线程安全的容器
    private CopyOnWriteArrayList<Student> mStudentList = new CopyOnWriteArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyStudentManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStudentList.add(new Student(1, "Jackey", "man"));
        mStudentList.add(new Student(1, "XiaoQiao", "woman"));
    }

    @Override
    public void onDestroy() {
        isDestroyed.set(true);
        super.onDestroy();
    }

    class MyStudentManager extends StudentManagerAIDL.Stub {

        @Override
        public void addStudent(Student student) throws RemoteException {
            mStudentList.add(student);
        }

        @Override
        public List<Student> getStudentList() throws RemoteException {
            SystemClock.sleep(2000);//模拟服务端耗时操作
            return mStudentList;
        }
    }

}
