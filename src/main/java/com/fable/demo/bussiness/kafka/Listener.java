package com.fable.demo.bussiness.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/7/11
 * Time :16:42
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */

/**
 * kafka的listener
 */
public class Listener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private StringRedisTemplate template;
    private CountDownLatch countDownLatch;
    public Listener(StringRedisTemplate template, CountDownLatch countDownLatch){
        this.template = template;
        this.countDownLatch = countDownLatch;
    }
    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) throws InterruptedException {
       logger.info("kafka的key: " + record.key()+",kafka的value: " + record.value().toString());
        template.convertAndSend("testRedis",record.value());
        countDownLatch.await();
    }
}
