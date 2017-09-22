package com.example.dhirajnayak.locationbasedmessaging;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhirajnayak on 9/20/17.
 */

public class GetUsersAsyncTask extends AsyncTask<String, Void, ArrayList<User>> {
    UserListData activity;

    public GetUsersAsyncTask(UserListData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<User> doInBackground(String... strings) {
        try {
            URL url=new URL(strings[0]);
            String userName= strings[1];

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("userName",userName);

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
                return GetUsersUtil.GetUsersJSONParser.parseGetUsers(stringBuilder.toString());
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> strings) {
        super.onPostExecute(strings);
        activity.setupData(strings);

    }

    static public interface UserListData{
        public void setupData(ArrayList<User> strings );
    }
}
