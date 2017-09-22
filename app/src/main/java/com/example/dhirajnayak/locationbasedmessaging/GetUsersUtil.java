package com.example.dhirajnayak.locationbasedmessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhirajnayak on 9/20/17.
 */

public class GetUsersUtil {
    static public class GetUsersJSONParser{
        static ArrayList<User> parseGetUsers(String in) throws JSONException {
            ArrayList<User> users=new ArrayList<>();
            JSONObject root=new JSONObject(in);
            JSONArray usersArray=root.getJSONArray("result");
            for(int i=0;i<usersArray.length();i++){
                JSONObject userObject=usersArray.getJSONObject(i);
                User user=new User();
                user.setFirstName(userObject.getString("FirstName"));
                user.setLastName(userObject.getString("LastName"));
                user.setUserName(userObject.getString("UserName"));
                users.add(user);
            }
            return users;
        }
    }
}

