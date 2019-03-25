package com.fable.demo.bussiness.websocket;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

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
 * Time :11:38
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class Sender {

    private Sender(){

    }

    private static Sender sender = new Sender();

    public static Sender getSender() {
        return sender;
    }
    private CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    public CopyOnWriteArraySet<Session> getSessions() {
        return sessions;
    }

    public synchronized  void sendData(String message){
        for (Session session:sessions){
            try {
                if(session!=null)
                    session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
