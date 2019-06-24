package cn.gc.componetlib;

/**
 * Created by 宫成 on 2019/5/21 上午10:12.
 */
public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }



}
