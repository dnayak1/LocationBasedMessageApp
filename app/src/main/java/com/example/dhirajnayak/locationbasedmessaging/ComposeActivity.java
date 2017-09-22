package com.example.dhirajnayak.locationbasedmessaging;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.GET_USER_URL;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.REGIONS;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.SEND_MESSAGE_URL;

public class ComposeActivity extends AppCompatActivity implements GetUsersAsyncTask.UserListData ,SendMessageAsyncTask.ISendMessageData{
    TextView textViewTo;
    TextView textViewRegion;
    ImageView imageViewTo;
    ImageView imageViewRegion;
    Button buttonSend;
    String loggedUser,token;
    SharedPreferences preferences;
    EditText editTextMsg;
    String selectedUser;
    Map<String,String> userMap;
    String toFromRead, regionFromRead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Compose Message");
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=preferences.getString("token",null);
        loggedUser=preferences.getString("loggedUser",null);

        toFromRead=getIntent().getStringExtra("to");
        regionFromRead=getIntent().getStringExtra("region");
        selectedUser=getIntent().getStringExtra("userName");


        textViewTo= (TextView) findViewById(R.id.textViewComposeReceiver);
        textViewRegion= (TextView) findViewById(R.id.textViewComposeRegion);
        imageViewRegion= (ImageView) findViewById(R.id.imageViewComposeSelectRegion);
        imageViewTo= (ImageView) findViewById(R.id.imageViewComposeSelectUser);
        buttonSend= (Button) findViewById(R.id.buttonComposeSend);
        editTextMsg= (EditText) findViewById(R.id.editTextComposeMessage);

        if(toFromRead!=null && !toFromRead.isEmpty() && regionFromRead!=null && !regionFromRead.isEmpty()){
            textViewTo.setText(toFromRead);
            textViewRegion.setText(regionFromRead);
            imageViewTo.setEnabled(false);
            imageViewRegion.setEnabled(false);
        }

        imageViewTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetUsersAsyncTask(ComposeActivity.this).execute(GET_USER_URL,loggedUser);
            }
        });

        imageViewRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(ComposeActivity.this);
                final CharSequence[] charSequence=REGIONS.values().toArray(new CharSequence[REGIONS.size()]);
                builder.setTitle("Users");
                builder.setSingleChoiceItems(charSequence, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textViewRegion.setText(charSequence[i].toString());
                        dialogInterface.dismiss();
                    }
                })
                        .setCancelable(true);
                builder.show();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiver=selectedUser;
                String region=textViewRegion.getText().toString();
                String msg=editTextMsg.getText().toString().trim();
                if(((receiver!=null && !receiver.isEmpty()) || (toFromRead!=null && !toFromRead.isEmpty())) &&
                        ((region!=null && !region.isEmpty())|| (regionFromRead!=null && !regionFromRead.isEmpty())) &&
                        msg!=null && !msg.isEmpty()){
                    java.util.Date dt = new java.util.Date();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(dt);
                    new SendMessageAsyncTask(ComposeActivity.this).execute(SEND_MESSAGE_URL,token,loggedUser,receiver,msg,region);
                }else{
                    Toast.makeText(ComposeActivity.this,"Please enter all information",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void setupData(ArrayList<User> strings) {
        userMap=new HashMap<>(strings.size());
        for(int i=0;i<strings.size();i++){
            userMap.put(strings.get(i).getUserName(),strings.get(i).getFirstName()+" "+strings.get(i).getLastName());
        }
        final AlertDialog.Builder builder=new AlertDialog.Builder(ComposeActivity.this);
        final CharSequence[] charSequence=userMap.values().toArray(new CharSequence[strings.size()]);
        builder.setTitle("Users");
        builder.setSingleChoiceItems(charSequence, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textViewTo.setText(charSequence[i].toString());
                for(String s:userMap.keySet()){
                    if(userMap.get(s).equals(charSequence[i]))
                        selectedUser=s;
                }
//                selectedUser=charSequence[i].toString();
                dialogInterface.dismiss();
            }
        })
                .setCancelable(true);
        builder.show();
    }

    @Override
    public void setupData(LoginDetails loginDetails) {
        imageViewTo.setEnabled(false);
        imageViewRegion.setEnabled(false);
        if(loginDetails.getCode()==200) {
            Intent intent = new Intent(ComposeActivity.this, InboxActivity.class);
            startActivity(intent);
        }
            Toast.makeText(ComposeActivity.this,loginDetails.getMessage(),Toast.LENGTH_LONG).show();

    }
}
