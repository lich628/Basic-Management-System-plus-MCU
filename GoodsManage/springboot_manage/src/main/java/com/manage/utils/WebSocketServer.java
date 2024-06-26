package com.manage.utils;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/sensorData")
public class WebSocketServer {
    private static Session session;
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("与前端websocket建立连接");
        WebSocketServer.session = session;
    }
    @OnMessage
    public void onMessage(String message, Session session) {

        try{
            System.out.println("前端发来消息为：" + message);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClose
    public void onClose() {
        System.out.println("与前端websocket连接断开");
    }

    // 静态方法，用于向客户端发送消息
    public static void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
