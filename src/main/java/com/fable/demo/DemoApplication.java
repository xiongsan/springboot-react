package com.fable.demo;

import com.fable.demo.bussiness.service.fileService.FileServiceImpl;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication(scanBasePackages = {"com.fable.enclosure.bussiness.controller","com.fable.demo.bussiness"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableScheduling
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

    @Bean
    public RestTemplate restTemplate(){
		return new RestTemplate();
	}

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		FileServiceImpl fileService = ctx.getBean(FileServiceImpl.class);
		KafkaTemplate<String,String> kafkaTemplate = ctx.getBean(KafkaTemplate.class);
		String bools[] = new String[]{",true",",false"};
		String[] strings = new String[]{
				"topicId,metadata,endPublish",
				"topicId,participatory,endPush",
				"topicId,discard,endPublish",
				"topicId,alternative,endPublish",
				"topicId,introspect,endPush",
				"topicId,anonymous,endPush",
				"topicId,embed,endPublish",
				"topicId,elementary,endPush",
				"topicId,capture,endPush",
				"topicId,ultimate,endPush",
				"topicId,eligible,endPublish",
				"topicId,deprecate,endPush",
				"topicId,unprecedented,endPush",
				"topicId,delegation,endPush",
		};
		Random random = new Random();
		ExecutorService service = Executors.newFixedThreadPool(3);
		service.execute(()-> {
				while (true) {
					try {
						Thread.sleep(1000);
						String fileName = strings[random.nextInt(strings.length)];
						String fileUrl = bools[random.nextInt(bools.length)];
						fileService.addFileCrazy(fileName,fileUrl);
			        	kafkaTemplate.send("test", "kafka key",fileName + fileUrl);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		});
	}
}
