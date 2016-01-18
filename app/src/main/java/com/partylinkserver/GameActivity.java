package com.partylinkserver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public abstract class GameActivity extends AppCompatActivity {
    protected GameCommunicationService gcs;
    protected boolean bound = false;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver broadcastReceiver;

    public void initialServiceBinding(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GameCommunicationService.GameCommunicationServiceBinder binder = (GameCommunicationService.GameCommunicationServiceBinder) service;
                gcs = binder.getService();
                bound = true;
                GameActivity.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        Intent intent = new Intent(this, GameCommunicationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String event = intent.getStringExtra("name");
                IncomingData(event);
//                onGameEvent(event, new HashMap<String, Object>());
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("game-event"));
    }

    public void IncomingData(String line){

        Log.d("DEBUG_gameActivity","line --->"+line);
        if(line != null){
            int idx = line.indexOf('|');
            String event = null;
            String[] params = null;
            if(idx > 0){
                event = line.substring(0,idx);
                params = line.substring(idx+1).split(",");
            }else if(idx < 0 && line.length() > 0){
                event = line;
            }
            if(event != null){
                Log.d("DEBUG_gameActivity",event+ " "+params);
                onGameEvent(event, params);
            }
        }

    }

    public abstract void onGameEvent(String event, String[] params);

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    protected  void onServiceConnected(){

    }
}