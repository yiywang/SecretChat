package pers.kanarien.chatroom.common;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pers.kanarien.chatroom.dao.GroupInfoDao;
import pers.kanarien.chatroom.dao.UserInfoDao;
import pers.kanarien.chatroom.web.websocket.WebSocketServer;

@Component
@Scope("singleton")
public class AppContext {

    private final Logger logger = LoggerFactory.getLogger(AppContext.class);

    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private GroupInfoDao groupDao;
    
    private Thread nettyThread;

    /**
     * Description: After Tomcat loads the ApplicationContext-main and netty files:
     * 1. Start the Netty WebSocket server;
     * 2. Load user data;
     * 3. Load user communication group data.
     */
    @PostConstruct
    public void init() {
        nettyThread = new Thread(webSocketServer);
        logger.info("Start a separate thread, start the Netty WebSocket server...");
        nettyThread.start();
        logger.info("Load user data...");
        userInfoDao.loadUserInfo();
        logger.info("Load user communication group data...");
        groupDao.loadGroupInfo();
    }

    /**
     * Description: You need to manually close the Netty Websocket related resources before shutting down the Tomcat server, otherwise it will cause a memory leak.
     * 1. Release Netty Websocket related connections;
     * 2. Close the Netty Websocket server thread. (Forced closing, is it necessary?)
     */
    @SuppressWarnings("deprecation")
    @PreDestroy
    public void close() {
        logger.info("Releasing Netty Websocket related connections...");
        webSocketServer.close();
        logger.info("Closing the Netty Websocket server thread...");
        nettyThread.stop();
        logger.info("The system shuts down successfully!");
    }
}
