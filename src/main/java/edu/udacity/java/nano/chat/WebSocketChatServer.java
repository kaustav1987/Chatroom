package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;



import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        //TODO: add send message method.


        for (Session sess : onlineSessions.values()) {

            try {
                sess.getBasicRemote().sendText(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        //TODO: add on open connection.
        onlineSessions.putIfAbsent(session.getId(), session);
        String msg = session.getId()+ "Entered the chat";
        Message message = new Message( msg, "ENTER"  );
        message.setOnlineCount(onlineSessions.size());
        sendMessageToAll(message.getJsonMessageInString());
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //TODO: add send message.
        JSONObject jsonData = JSON.parseObject(jsonStr);

        String msg = jsonData.getString("msg");

        System.out.println("string data:"+ jsonStr);
        System.out.println(msg);
        String username = jsonData.getString("username");
        session.getUserProperties().put("username", username);

        Message message = new Message(msg,"SPEAK" );
        message.setUsername(username);

        sendMessageToAll(message.getJsonMessageInString());
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
        String msg = session.getUserProperties().get("username")+ " Left the chat";
        onlineSessions.remove(session.getId());

        Message message = new Message(msg,  "LEAVE"  );
        String username  = (String) session.getUserProperties().get("username");
        message.setUsername(username);
        message.setOnlineCount(onlineSessions.size());

        System.out.println(message.getJsonMessageInString());
        sendMessageToAll(message.getJsonMessageInString());

    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
