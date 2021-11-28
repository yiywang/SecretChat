package pers.kanarien.chatroom.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import pers.kanarien.chatroom.model.po.GroupInfo;
import pers.kanarien.chatroom.model.po.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Global constant
 * 1. USER_TOKEN user authentication key, used to match the corresponding userId in the http session;
 * 2. The webSocketServerHandshaker table uses channelId as the key to store handshake instances. Used to respond to the request of CloseWebSocketFrame;
 * 3. In the onlineUser table, use userId as the key to store the online client connection context;
 * 4. In the groupInfo table, use groupId as the key to store group information;
 * 5. The userInfo table uses username as the key to store user information.
 * @author Kanarien
 * @version 1.0
 * @date May 18, 2018 9:17:35 PM
 */
public class Constant {

    public static final String USER_TOKEN = "userId";
    
    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = 
            new ConcurrentHashMap<String, WebSocketServerHandshaker>();
    
	public static Map<String, ChannelHandlerContext> onlineUserMap = 
	        new ConcurrentHashMap<String, ChannelHandlerContext>();

	public static Map<String, GroupInfo> groupInfoMap = 
	        new ConcurrentHashMap<String, GroupInfo>();
	
	public static Map<String, UserInfo> userInfoMap = 
	        new HashMap<String, UserInfo>();
}
