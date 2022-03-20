package com.fable.demo;

import com.fable.demo.bussiness.websocket.Sender;
import com.fable.enclosure.bussiness.util.SpringContextUtil;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@SpringBootApplication(scanBasePackages = {"com.fable.enclosure.bussiness.controller","com.fable.demo.bussiness"},exclude = {MultipartAutoConfiguration.class})
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

	@Scheduled(cron = "0/5 * * * * *")
	public void test(){
//		Map<String, Object> map = new HashMap<>();
//		map.put("type", "message");
//		map.put("payload", "hello..."+new Random().nextDouble());
//		Sender.getSender().sendData(JSONObject.toJSONString(map));
	}

    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
	}


}
