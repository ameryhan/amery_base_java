package amery.jdk.concurrent.async;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并行数据加载器
 *
 * @author 小马哥
 * @since 2018/6/20
 */
public class ParallelDataLoaderV2Take extends DataLoaderV2 {

    @Override
    protected void doLoad() {  // 并行计算
        ExecutorService executorService = Executors.newFixedThreadPool(3); // 创建线程池
        CompletionService completionService = new ExecutorCompletionService(executorService);

        completionService.submit(this::loadConfigurations);
        completionService.submit(() -> loadUsers());
        completionService.submit(() -> loadOrders());

        executorService.shutdown();

        //https://cloud.tencent.com/developer/article/1444259

        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("此次任务执行的结果 " + completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


    }  // 总耗时 max(1s, 2s, 3s)  >= 3s

    public static void main(String[] args) {
        new ParallelDataLoaderV2Take().load();
    }

}
