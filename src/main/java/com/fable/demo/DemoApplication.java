package com.fable.demo;

import com.fable.enclosure.bussiness.util.SpringContextUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.sql.DataSource;


@SpringBootApplication(scanBasePackages = {"com.fable.enclosure.bussiness.controller","com.fable.demo.bussiness"})
@MapperScan("com.fable.demo.bussiness.mapper")
//@ImportResource(locations= "classpath:spring-shiro-web.xml")
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class DemoApplication {

	protected static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	//DataSource配置
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	//提供SqlSeesion
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/**/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

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

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		new Thread(()-> {
//				while (true){
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					logger.error("--------------slf4j.Logger.test----------------");
//				}
//		}).start();

	}
}
