package com.fable.demo;

import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


@SpringBootApplication(scanBasePackages = {"com.fable.enclosure.bussiness.controller","com.fable.demo.bussiness"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class DemoApplication {

	protected static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@Bean
	public SpringContextUtil contextUtil(){
		return new SpringContextUtil();
	}

	@Bean("multipartResolver")
	public CommonsMultipartResolver resolver(){
		return new CommonsMultipartResolver();
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx=SpringApplication.run(DemoApplication.class, args);
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		final String[] strings = new String[]{
				"topicId,metadata,endPublish, true",
				"topicId,participatory,endPush,true",
				"topicId,discard,endPublish, false",
				"topicId,alternative,endPublish, true",
				"topicId,introspect,endPush, false",
				"topicId,anonymous,endPush,true",
				"topicId,embed,endPublish,false",
				"topicId,elementary,endPush,true",
				"topicId,capture,endPush,false",
				"topicId,ultimate,endPush,false",
				"topicId,eligible,endPublish,true",
				"topicId,deprecate,endPush,false",
				"topicId,unprecedented,endPush,true",
				"topicId,delegation,endPush,true",
		};
		Random random = new Random();
		while (true) {
			try {
				Thread.sleep(50);
				template.convertAndSend("interactive", strings[random.nextInt(strings.length)]);
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
