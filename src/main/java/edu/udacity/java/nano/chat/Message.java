package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.

    private String username;
    private String msg;
    private Integer onlineCount;
    private String type;

    public Message() {
    }

    public Message(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Message(String msg, String type) {

        this.msg = msg;
        this.type = type;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJsonMessageInString(){
        final JSONObject jsonData = new JSONObject();
        if(this.type== "SPEAK" || this.type== "LEAVE" ){
            jsonData.put("username", this.username);
        }

        jsonData.put("type", this.type);
        jsonData.put("msg", this.msg);
        jsonData.put("onlineCount", this.onlineCount);
        return jsonData.toJSONString();

    }
}