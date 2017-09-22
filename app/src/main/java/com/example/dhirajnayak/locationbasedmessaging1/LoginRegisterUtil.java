package com.example.dhirajnayak.locationbasedmessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhirajnayak on 9/18/17.
 */

public class LoginRegisterUtil {
    static public class LoginDetailsJSONParser{
        static LoginDetails parseLoginDetails(String in) throws JSONException {
            LoginDetails loginDetails=new LoginDetails();
            JSONObject root=new JSONObject(in);
            loginDetails.setCode(root.getInt("code"));
            loginDetails.setMessage(root.getString("message"));
            if(root.has("userName")){
                loginDetails.setUserName(root.getString("userName"));
            }
            if(root.has("userName")){
                loginDetails.setToken(root.getString("token"));
            }
            return loginDetails;
        }
    }
}
