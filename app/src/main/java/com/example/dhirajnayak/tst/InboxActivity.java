package com.example.dhirajnayak.locationbasedmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.GET_MESSAGE_URL;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.READ_MESSAGE_URL;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.REGIONS;
import static com.example.dhirajnayak.locationbasedmessaging.MainActivity.UNLOCK_MESSAGE_URL;

public class InboxActivity extends AppCompatActivity implements MessageRecyclerAdapter.IMessageListener,
        GetMessagesAsyncTask.IMessageListData, ReadAsyncTask.IReadData, UnlockMessagesAsyncTask.IUnlockData {

    String loggedUser,token;
    SharedPreferences preferences;
    MessageRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    private BeaconManager beaconManager;
    private BeaconRegion region;
    String urlCheck;
    String url="All Regions";
    int countR1=0, countR2=0, countR3=0;
    static  int DEFAULT=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Inbox");
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=preferences.getString("token",null);
        loggedUser=preferences.getString("loggedUser",null);
        new GetMessagesAsyncTask(this).execute(GET_MESSAGE_URL,token,loggedUser);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                //Toast.makeText(MainActivity.this,"beacon 1: "+countG+". Beacon 2: "+countL+". Beacon 3: "+countP,Toast.LENGTH_SHORT).show();
                if (!list.isEmpty()) {
                    List<Beacon> filteredBeacons=new ArrayList<Beacon>();
                    for(int i=0;i<list.size();i++){
                        Beacon checkBeacon=list.get(i);
                        if(placesNearBeacon(checkBeacon) !=null && !placesNearBeacon(checkBeacon).isEmpty()){
                            filteredBeacons.add(checkBeacon);
                        }
                    }
                    Collections.sort(filteredBeacons, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon beacon, Beacon t1) {
                            return beacon.getRssi() - t1.getRssi();
                        }
                    });
                    if(!filteredBeacons.isEmpty()){
                        Beacon nearestBeacon = filteredBeacons.get(0);
                        urlCheck = placesNearBeacon(nearestBeacon);
                        if(urlCheck.equals("Region1")){
                            if(countR1<DEFAULT)
                                countR1++;
                            if(countR3>0)
                                countR3--;
                            if(countR2>0)
                                countR2--;
                        } else if (urlCheck.equals("Region2")){
                            if(countR2<DEFAULT)
                                countR2++;
                            if(countR3>0)
                                countR3--;
                            if(countR1>0)
                                countR1--;
                        } else if(urlCheck.equals("Region3")){
                            if(countR3<DEFAULT)
                                countR2++;
                            if(countR2>0)
                                countR2--;
                            if(countR1>0)
                                countR1--;
                        }
                    }

                    if((url!=null && !url.isEmpty() && !url.equals(urlCheck)) && ((countR1==DEFAULT || countR2==DEFAULT || countR3==DEFAULT))){
                        url=urlCheck;
                        new UnlockMessagesAsyncTask(InboxActivity.this).execute(UNLOCK_MESSAGE_URL,token,loggedUser,url);
                    }


                }
//                else{
//                    if(countR1>0)
//                        countR1--;
//                    else if(countR2>0)
//                        countR2--;
//                    else if(countR3>0)
//                        countR3--;
//                    if((url!=null && !url.equals(urlCheck)) && (countR1==0 && countR2==0 && countR3==0)){
//                        url=urlCheck;
//                        new GetMessagesAsyncTask(InboxActivity.this).execute(GET_MESSAGE_URL,token,loggedUser);
//                    }
//                }
            }
        });
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inbox_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.composeMail:
                Intent composeIntent=new Intent(InboxActivity.this,ComposeActivity.class);
                startActivity(composeIntent);
                break;
            case R.id.refreshInbox:
                progressBar.setVisibility(View.VISIBLE);
                new GetMessagesAsyncTask(this).execute(GET_MESSAGE_URL,token,loggedUser);
                break;
        }
        return true;

    }

    @Override
    public void messageDetail(Message message) {
        if(message.getLocked()==1){
            Toast.makeText(InboxActivity.this,"Message is locked",Toast.LENGTH_LONG).show();
        }else{
            String messageId=String.valueOf(message.getMessageId());
            new ReadAsyncTask(InboxActivity.this).execute(READ_MESSAGE_URL,token,messageId);
            Intent intent=new Intent(InboxActivity.this,ReadActivity.class);
            intent.putExtra("message",message);
            startActivity(intent);
        }
    }

    @Override
    public void setupData(ArrayList<Message> messages) {
        progressBar.setVisibility(View.INVISIBLE);
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                return t1.getDate().compareTo(message.getDate());
            }
        });
        adapter=new MessageRecyclerAdapter(InboxActivity.this,messages,InboxActivity.this);
        recyclerView.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(InboxActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();;
    }

    @Override
    public void setupReadData(LoginDetails loginDetails) {
        progressBar.setVisibility(View.VISIBLE);
        new GetMessagesAsyncTask(this).execute(GET_MESSAGE_URL,token,loggedUser);
    }

    private String placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        String tempUrl="";
        if (REGIONS.containsKey(beaconKey)) {
            tempUrl= REGIONS.get(beaconKey);
        }
        return tempUrl ;
    }

    @Override
    public void setupUnlockData(LoginDetails loginDetails) {
        new GetMessagesAsyncTask(this).execute(GET_MESSAGE_URL,token,loggedUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
                new GetMessagesAsyncTask(InboxActivity.this).execute(GET_MESSAGE_URL,token,loggedUser);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.stopRanging(region);
    }
}
