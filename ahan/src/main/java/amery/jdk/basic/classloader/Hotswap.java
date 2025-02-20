package amery.jdk.basic.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ameryhan
 * @date 2019/8/21 17:11
 */
public class Hotswap {

    public static void main(String[] args)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException, InterruptedException {
        loadUser();
        System.gc();
        Thread.sleep(1000);// 等待资源回收
        // 需要被热部署的class文件
        File file1 = new File("F:\\test\\User.class");
        // 之前编译好的class文件
        File file2 = new File(
                "F:\\itmayiedujiangke2018-02-24\\itmayiedu_itmayiedu_day_17\\target\\classes\\com\\itmayiedu\\User.class");
        boolean isDelete = file2.delete();// 删除旧版本的class文件
        if (!isDelete) {
            System.out.println("热部署失败.");
            return;
        }
        file1.renameTo(file2);
        System.out.println("update success!");
        loadUser();

    }

    public static void loadUser() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        MyClassLoader myLoader = new MyClassLoader();
        Class<?> class1 = myLoader.findClass("com.itmayiedu.User");
        Object obj1 = class1.newInstance();
        Method method = class1.getMethod("add");
        method.invoke(obj1);
        System.out.println(obj1.getClass());
        System.out.println(obj1.getClass().getClassLoader());
    }

}
