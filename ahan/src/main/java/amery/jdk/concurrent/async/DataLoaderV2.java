package amery.jdk.concurrent.async;

import java.util.concurrent.TimeUnit;

/**
 * 阻塞（串行）数据加载器
 *
 * @author 小马哥
 * @since 2018/6/20
 */
public class DataLoaderV2 {

    public final void load() {
        long startTime = System.currentTimeMillis(); // 开始时间
        doLoad(); // 具体执行
        long costTime = System.currentTimeMillis() - startTime; // 消耗时间
        System.out.println("load() 总耗时：" + costTime + " 毫秒");
    }

    protected void doLoad() { // 串行计算
        loadConfigurations();    //  耗时 1s
        loadUsers();                  //  耗时 2s
        loadOrders();                // 耗时 3s
    } // 总耗时 1s + 2s  + 3s  = 6s

    protected final long loadConfigurations() {
        return loadMock("loadConfigurations()", 1);
    }

    protected final long loadUsers() {
        return loadMock("loadUsers()", 2);
    }

    protected final long loadOrders() {
        return loadMock("loadOrders()", 3);
    }

    private long loadMock(String source, int seconds) {
        try {
            long startTime = System.currentTimeMillis();
            long milliseconds = TimeUnit.SECONDS.toMillis(seconds);
            Thread.sleep(milliseconds);
            long costTime = System.currentTimeMillis() - startTime;
            System.out.printf("[线程 : %s] %s 耗时 :  %d 毫秒\n",
                    Thread.currentThread().getName(), source, costTime);
            return costTime;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new DataLoaderV2().load();
    }

}
