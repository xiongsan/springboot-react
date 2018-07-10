package com.fable.demo.bussiness;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/7/9
 * Time :11:07
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.fable.demo.bussiness.websocket.Sender;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch;

    private Map<String, String> map = new HashMap<>();

    private Sender sender = Sender.getSender();

    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message) {
        String[] messages = message.split(",");
        String topicId = messages[0];
        String app = messages[1];
        String isEnd = messages[2];
        String isException = messages.length > 3 ? messages[3] : "true";
        if (isEnd.startsWith("end")) {
            isEnd = isEnd.substring(3);
            message = topicId + "," + app + "," + isEnd + "," + isException;
            map.put("type", "test");
            map.put("payload", message);
            sender.sendData(JSONObject.toJSONString(map));
        }
        LOGGER.info("Received <" + message + ">");
        latch.countDown();
    }
}
