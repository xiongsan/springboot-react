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
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication(scanBasePackages = {"com.fable.enclosure.bussiness.controller","com.fable.demo.bussiness"})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@EnableScheduling
@Controller
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
						Thread.sleep(500);
						String fileName = strings[random.nextInt(strings.length)];
						String fileUrl = bools[random.nextInt(bools.length)];
//						fileService.addFileCrazy(fileName,fileUrl);
			        	kafkaTemplate.send("test", "kafka key",fileName + fileUrl);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		});
	}

//	@RequestMapping(value = "/player", method = RequestMethod.GET)
//	public void player2(HttpServletRequest request, HttpServletResponse response) {
//		String path = "F:\\迅雷下载\\2018-08-25\\diezhongdie.mp4";
//		BufferedInputStream bis = null;
//		try {
//			File file = new File(path);
//			if (file.exists()) {
//				long p = 0L;
//				long toLength = 0L;
//				long contentLength = 0L;
//				int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
//				long fileLength;
//				String rangBytes = "";
//				fileLength = file.length();
//
//				// get file content
//				InputStream ins = new FileInputStream(file);
//				bis = new BufferedInputStream(ins);
//
//				// tell the client to allow accept-ranges
//				response.reset();
//				response.setHeader("Accept-Ranges", "bytes");
//
//				// client requests a file block download start byte
//				String range = request.getHeader("Range");
//				if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
//					response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
//					rangBytes = range.replaceAll("bytes=", "");
//					if (rangBytes.endsWith("-")) { // bytes=270000-
//						rangeSwitch = 1;
//						p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
//						contentLength = fileLength - p; // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
//					} else { // bytes=270000-320000
//						rangeSwitch = 2;
//						String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
//						String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
//						p = Long.parseLong(temp1);
//						toLength = Long.parseLong(temp2);
//						contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
//					}
//				} else {
//					contentLength = fileLength;
//				}
//
//				// 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
//				// Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
//				response.setHeader("Content-Length", new Long(contentLength).toString());
//
//				// 断点开始
//				// 响应的格式是:
//				// Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
//				if (rangeSwitch == 1) {
//					String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
//							.append(new Long(fileLength - 1).toString()).append("/")
//							.append(new Long(fileLength).toString()).toString();
//					response.setHeader("Content-Range", contentRange);
//					bis.skip(p);
//				} else if (rangeSwitch == 2) {
//					String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
//					response.setHeader("Content-Range", contentRange);
//					bis.skip(p);
//				} else {
//					String contentRange = new StringBuffer("bytes ").append("0-").append(fileLength - 1).append("/")
//							.append(fileLength).toString();
//					response.setHeader("Content-Range", contentRange);
//				}
//
//				String fileName = file.getName();
//				response.setContentType("application/octet-stream");
//				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//				OutputStream out = response.getOutputStream();
//				int n = 0;
//				long readLength = 0;
//				int bsize = 1024;
//				byte[] bytes = new byte[bsize];
//				if (rangeSwitch == 2) {
//					// 针对 bytes=27000-39000 的请求，从27000开始写数据
//					while (readLength <= contentLength - bsize) {
//						n = bis.read(bytes);
//						readLength += n;
//						out.write(bytes, 0, n);
//					}
//					if (readLength <= contentLength) {
//						n = bis.read(bytes, 0, (int) (contentLength - readLength));
//						out.write(bytes, 0, n);
//					}
//				} else {
//					while ((n = bis.read(bytes)) != -1) {
//						out.write(bytes, 0, n);
//					}
//				}
//				out.flush();
//				out.close();
//				bis.close();
//			}
//		} catch (IOException ie) {
//			// 忽略 ClientAbortException 之类的异常
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
