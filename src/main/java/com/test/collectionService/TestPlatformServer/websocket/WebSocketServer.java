package com.test.collectionService.TestPlatformServer.websocket;

import com.alibaba.fastjson.JSONObject;
import com.test.collectionService.TestPlatformServer.model.ExceptionEnum;
import com.test.collectionService.TestPlatformServer.model.Result;
import com.test.collectionService.TestPlatformServer.util.SessionManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.test.collectionService.TestPlatformServer.util.SessionManage.checkUserHasLogin;

/**
 * @Author You Jia
 * @Date 8/7/2018 9:51 AM
 */
@Slf4j
@ServerEndpoint("/websocket/homepage/{username}")
@Component
public class WebSocketServer {
    //记录当前在线连接的人数
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，存放每个客户端的websocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;
    private String userName;
    private static String admin ="admin";
    private JSONObject jsonObject = new JSONObject();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String userName) throws Exception {
        if(!checkUserHasLogin(userName)){
            throw new Exception("没有登录");
        }
        this.session = session;
        this.userName = userName;
        webSocketSet.add(this);
        addOnlineCount();
        if(userName.equals(admin)){
            log.info("The Total Online: " + getOnlineCount());
            jsonObject.put("TotalOnline",String.valueOf(getOnlineCount()));
            try {
                sendMessage(jsonObject.toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * 收到客户端消息后的方法
    * */
    @OnMessage
    public void onMessage(String message) {
        log.debug("Receive message from client: " + userName + ", Message= " + message);
//        if (session != null) {
//            try {
//                sendMessage("Hello!" + userName);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }
    //群发消息给客户端或者只推送消息给username。
    public static void sendInfo(String message,String username) throws IOException {
        for(WebSocketServer webSocketServer: webSocketSet){
            //当username为null，认为是admin登录，推送所有消息给它。
            if(webSocketServer.userName.equals(admin)){
                log.debug("Push all messages to admin");
                webSocketServer.sendMessage(message);
            }else if(webSocketServer.userName.equals(username)){
                log.debug("Push messages to " + username);
                webSocketServer.sendMessage(message);
            }
        }
    }
    //用户session过期以后，也需要close websocket
    public static void closeWebsocket(String userName){
        for(WebSocketServer webSocketServer: webSocketSet){
            if(webSocketServer.userName.equals(userName)){
                log.debug("Session expired," + userName + " has to logout websocket");
                webSocketServer.onClose();
            }
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        if(session!=null){
            subOnlineCount();
            log.debug("OnClose is called, Websocket Session is Closed");
        }

    }
    @OnError
    public void onError(Session session, Throwable error){
        log.error("Websocket error");
    }

    public void sendMessage(String message) throws IOException {
            this.session.getBasicRemote().sendText(message);
    }
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        if(onlineCount <= 0){
            onlineCount = 0;
        }else{
            onlineCount--;
        }

    }

}
