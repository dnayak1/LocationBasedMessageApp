package com.example.dhirajnayak.locationbasedmessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dhirajnayak on 9/20/17.
 */


public class GetMessagesUtil {
    static public class GetMessagesJSONParser {
        static ArrayList<Message> parseGetMessages(String in) throws JSONException {
            ArrayList<Message> messages = new ArrayList<>();
            Date outputDate=null;
            JSONObject root = new JSONObject(in);
            JSONArray messagesArray = root.getJSONArray("result");
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONObject jsonMessage = messagesArray.getJSONObject(i);
                Message message = new Message();
                message.setFirstName(jsonMessage.getString("FirstName"));
                message.setLastName(jsonMessage.getString("LastName"));
                message.setSender(jsonMessage.getString("Sender"));
                message.setMessage(jsonMessage.getString("Message"));
                message.setRead(jsonMessage.getInt("isRead"));
                String stringDate=jsonMessage.getString("Date");
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    String changedDateString=new SimpleDateFormat("MM/dd/yy, hh:mm a").format(dateFormat.parse(stringDate));
                    outputDate=new SimpleDateFormat("MM/dd/yy, hh:mm a").parse(changedDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                message.setLocked(jsonMessage.getInt("isLocked"));
                message.setDate(outputDate);
                message.setRegion(jsonMessage.getString("Region"));
                message.setMessageId(jsonMessage.getInt("messageId"));

                messages.add(message);
            }
            return messages;
        }
    }
}
