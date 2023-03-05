package io.boxhit.socket.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageTemplates {

    private static String version = "1.0.0";
    private static String RSAversion = "1.0.0";


    public static String getDefaultHeader(){
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", version);
            jsonObject.put("RSAversion", RSAversion);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public static OutputMessage getError(String message){
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return new OutputMessage().setModule(MessageModule.ERROR.getModule()).setHeader(getDefaultHeader()).setJson(jsonObject.toString());
    }

    public static OutputMessage getError(String message, MessageModule module){
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return new OutputMessage().setModule(module.getModule()).setHeader(getDefaultHeader()).setJson(jsonObject.toString());
    }

}
