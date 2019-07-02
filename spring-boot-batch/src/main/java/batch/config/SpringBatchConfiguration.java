package batch.config;

import batch.JobListener;
import batch.Message;
import batch.MessageProcessor;
import batch.MessageWriter;
import batch.User;
import batch.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SpringBatchConfiguration.class);

    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MessageWriter messageWriter;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private JobListener jobListener;

    @Bean
    @Primary
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public MessageWriter messageWriter() {
        return new MessageWriter();
    }

    @Bean
    public MessageProcessor messageProcessor() {
        return new MessageProcessor();
    }

    @Bean
    public Job dataHandleJob() {
        return jobBuilderFactory.get("dataHandleJob").
                incrementer(new RunIdIncrementer()).
                start(handleDataStep()).
                listener(jobListener).
                build();
                //start是JOB执行的第一个step
//                next(xxxStep()).
//                next(xxxStep()).
//                ...

    }

    /**
     * 一个简单基础的Step主要分为三个部分
     * ItemReader : 用于读取数据
     * ItemProcessor : 用于处理数据
     * ItemWriter : 用于写数据
     */
    @Bean
    public Step handleDataStep() {
        return stepBuilderFactory.get("getData")
                // <输入,输出> 。chunk通俗的讲类似于SQL的commit; 这里表示处理(processor)100条后写入(writer)一次。
                .<User, Message>chunk(100).
                faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class)
                //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
                .reader(getDataReader())
                .processor(getDataProcessor())
                .writer(getDataWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends User> getDataReader() {
        FlatFileItemReader flatFileItemReader = new FlatFileItemReader();

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setFieldSetMapper(userMapper);
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());

        ClassPathResource resource = new ClassPathResource("classpath:user.txt");

        flatFileItemReader.setLineMapper(lineMapper);
        flatFileItemReader.setResource(resource);

        return flatFileItemReader;
    }

    @Bean
    public ItemProcessor<User, Message> getDataProcessor() {
        return messageProcessor();
    }

    @Bean
    public ItemWriter<Message> getDataWriter() {
        return messageWriter;
    }
}
