package com.example.dhirajnayak.locationbasedmessaging;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhirajnayak on 9/18/17.
 */

public class LoginRegisterAsyncTask extends AsyncTask<String, Void, LoginDetails> {
    IData activity;

    public LoginRegisterAsyncTask(IData activity) {
        this.activity = activity;
    }

    @Override
    protected LoginDetails doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            String userName= params[1];
            String password= params[2];

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("userName",userName);
            jsonParam.put("password",password);
            if(params.length>3){
                String firstName=params[3];
                String lastName=params[4];
                jsonParam.put("firstName",firstName);
                jsonParam.put("lastName",lastName);
            }


            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setDoOutput (true);
            connection.setDoInput (true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");

            OutputStream outputStream=connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            int statusCode=connection.getResponseCode();
            if(statusCode== HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder=new StringBuilder();
                String line=bufferedReader.readLine();
                while ((line!=null)){
                    stringBuilder.append(line);
                    line=bufferedReader.readLine();
                }
                return LoginRegisterUtil.LoginDetailsJSONParser.parseLoginDetails(stringBuilder.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(LoginDetails loginDetails) {
        super.onPostExecute(loginDetails);
        activity.setupData(loginDetails);
    }

    static public interface IData{
        public void setupData(LoginDetails loginDetails);
    }
}
