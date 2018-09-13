package com.test.collectionService.TestPlatformServer.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

import static com.test.collectionService.TestPlatformServer.util.SessionManage.checkValidSession;
import static com.test.collectionService.TestPlatformServer.websocket.WebSocketServer.closeWebsocket;
import static com.test.collectionService.TestPlatformServer.websocket.WebSocketServer.subOnlineCount;

/**
 * @Author You Jia
 * @Date 8/13/2018 10:36 AM
 */
@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener{
    @Override
    public void sessionCreated(HttpSessionEvent event){
        log.debug("---###########Session is Created ##############---");
        HttpSession session = event.getSession();
        session.setMaxInactiveInterval(3600); //second

    }
    @Override
    public void sessionDestroyed(HttpSessionEvent event){
        log.debug("---###########Session is Destroyed ###########---");
        //Remove websocket session as well.
        log.debug("---###########Remove Websocket Session from SessionMap ###########---");
        closeWebsocket(checkValidSession(event.getSession().getId()));
        //Remove session from Session2Map
        log.debug("---###########Remove Session from SessionMap ###########---");
        SessionManage.removeSession2Map(event.getSession().getId());

    }

}
