package com.example.dhirajnayak.locationbasedmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LoginRegisterAsyncTask.IData {

    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogin;
    Button buttonRegister;
    String userName, password,loggedUser;
    static final String LOGIN_URL="http://13.59.179.225:5000/api/login";
    static final String REGISTER_URL="http://13.59.179.225:5000/api/register";
    static final String GET_USER_URL="http://13.59.179.225:5000/api/getUsers";
    static final String SEND_MESSAGE_URL="http://13.59.179.225:5000/api/sendMessage";
    static final String GET_MESSAGE_URL="http://13.59.179.225:5000/api/getMessages";
    static final String DELETE_MESSAGE_URL="http://13.59.179.225:5000/api/deleteMessages";
    static final String READ_MESSAGE_URL="http://13.59.179.225:5000/api/readMessages";
    static final String UNLOCK_MESSAGE_URL="http://13.59.179.225:5000/api/unlockMessages";

    public static final String USER_PREF = "userToken";
    static final Map<String,String> REGIONS;

    static {
        Map<String, String> placesByBeacons = new HashMap<>();
//        placesByBeacons.put("1564:34409","Region1");
        placesByBeacons.put("1564:34409","Region1");
        placesByBeacons.put("15212:31506","Region2");
        placesByBeacons.put("26535:44799","Region3");
        REGIONS = Collections.unmodifiableMap(placesByBeacons);
    }

    String userToken;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Message Me!");
        editTextUsername= (EditText) findViewById(R.id.editTextLoginUserName);
        editTextPassword= (EditText) findViewById(R.id.editTextLoginPassword);
        buttonLogin= (Button) findViewById(R.id.buttonLogin);
        buttonRegister= (Button) findViewById(R.id.buttonRegister);

        userName=editTextUsername.getText().toString().trim();
        password=editTextPassword.getText().toString();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userToken=pref.getString("token",null);
        loggedUser=pref.getString("loggedUser",null);

        if(userToken!=null && !userToken.isEmpty()){
            Intent intent=new Intent(this,InboxActivity.class);
            startActivity(intent);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=editTextUsername.getText().toString().trim();
                password=editTextPassword.getText().toString();
                if(userName!=null && !userName.isEmpty() && password!=null && !password.isEmpty()){
                    new LoginRegisterAsyncTask(MainActivity.this).execute(LOGIN_URL,userName,password);
                }else{
                    Toast.makeText(MainActivity.this,"Invalid user name and password",Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setupData(LoginDetails loginDetails) {
        if(loginDetails.getCode()==200){
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("token",loginDetails.getToken());
            editor.putString("loggedUser",loginDetails.getUserName());
            editor.apply();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
            Toast.makeText(MainActivity.this,loginDetails.getMessage(),Toast.LENGTH_LONG).show();
    }
}
