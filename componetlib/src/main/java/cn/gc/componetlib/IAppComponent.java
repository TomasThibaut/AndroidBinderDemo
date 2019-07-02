package cn.gc.componetlib;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 宫成 on 2019/5/21 上午9:45.
 */
public interface IAppComponent {

    void init(@NonNull Application application);


}
