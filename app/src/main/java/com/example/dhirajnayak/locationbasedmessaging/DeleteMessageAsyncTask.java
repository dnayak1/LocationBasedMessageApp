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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dhirajnayak on 9/21/17.
 */

public class DeleteMessageAsyncTask extends AsyncTask<String,Void,LoginDetails> {
    IDeleteMessage activity;

    public DeleteMessageAsyncTask(IDeleteMessage activity) {
        this.activity = activity;
    }

    @Override
    protected LoginDetails doInBackground(String... strings) {
        try {
            URL url=new URL(strings[0]);
            String token=strings[1];
            String messageId= strings[2];

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("token",token);
            jsonParam.put("messageId",messageId);

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

    static public interface IDeleteMessage{
        public void setupData(LoginDetails loginDetails);
    }
}
