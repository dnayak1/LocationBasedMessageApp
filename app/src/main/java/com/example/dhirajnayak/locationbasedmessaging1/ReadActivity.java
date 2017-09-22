package com.example.dhirajnayak.locationbasedmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.DELETE_MESSAGE_URL;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.GET_MESSAGE_URL;

public class ReadActivity extends AppCompatActivity implements DeleteMessageAsyncTask.IDeleteMessage {
    TextView textViewReadFrom;
    TextView textViewReadRegion;
    TextView textViewReadMessage;
    String userName,region;
    String loggedUser,token;
    String messageId;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Read Message");
        textViewReadFrom= (TextView) findViewById(R.id.textViewReadFrom);
        textViewReadRegion= (TextView) findViewById(R.id.textViewReadRegion);
        textViewReadMessage= (TextView) findViewById(R.id.textViewReadMessage);
        Message message= (Message) getIntent().getSerializableExtra("message");
        textViewReadFrom.setText(message.getFirstName()+" "+message.getLastName());
        textViewReadRegion.setText(message.getRegion());
        textViewReadMessage.setText(message.getMessage());
        userName=message.getSender();
        region=message.getRegion();
        messageId=String.valueOf(message.getMessageId());
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=preferences.getString("token",null);
        loggedUser=preferences.getString("loggedUser",null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reply:
                Intent readIntent=new Intent(ReadActivity.this,ComposeActivity.class);
                readIntent.putExtra("to",textViewReadFrom.getText());
                readIntent.putExtra("region",textViewReadRegion.getText());
                readIntent.putExtra("userName",userName);
                startActivity(readIntent);
                break;
            case R.id.discardMessage:
                new DeleteMessageAsyncTask(ReadActivity.this).execute(DELETE_MESSAGE_URL,token,messageId);
        }
        return true;
    }

    @Override
    public void setupData(LoginDetails loginDetails) {
        if(loginDetails.getCode()==200){
            Toast.makeText(ReadActivity.this,"Message deleted",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ReadActivity.this,InboxActivity.class);
            startActivity(intent);
        }
    }
}
