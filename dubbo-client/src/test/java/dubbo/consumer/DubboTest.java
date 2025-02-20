package dubbo.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 2 * @Author: han,zhansheng
 * 3 * @Date: 2019/12/12 8:28 PM
 * 4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class DubboTest {

    @Autowired
    private ConsumerDemo consumerDemo;

    @Test
    public void testDubbo() {
        consumerDemo.print();
    }
}
