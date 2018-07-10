package com.fable.demo.bussiness.websocket;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :王海瑞 2016/11/17
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
@ServerEndpoint("/websocket")
@Component
public class WebSocketService {

    private static Sender sender=Sender.getSender();

    private Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private Map<String, String> map = new HashMap<>();

    private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sender.getSessions().add(session);
        webSocketSet.add(this);
        addOnlineCount();           //在线数加1
        logger.info("有新连接加入！当前在线人数为{}", getOnlineCount());
    }

    @OnClose
    public void onClose() {
        sender.getSessions().remove(session);
        webSocketSet.remove(this);
        subOnlineCount();           //在线数减1
        logger.info("有一连接关闭！当前在线人数为{}",getOnlineCount());
    }


    //收到客户端端消息时触发
    @OnMessage
    public synchronized void  onMessage(String message, Session session) {
        logger.info("来自客户端的消息：{}",message);
        for(WebSocketService item: webSocketSet){
            try {
                map.put("type", "message");
                map.put("payload", message);
                item.sendMessage(JSONObject.toJSONString(map));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("websocket发生错误");
        error.printStackTrace();
    }
    private void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

}