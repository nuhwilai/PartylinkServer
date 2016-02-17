package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import pl.engine.GameContext;
import pl.engine.GameShakeEngine;
import pl.engine.Main;
import pl.engine.RegistrarEngine;

public class RegistrarActivity extends GameActivity{
    private GameContext gc;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialServiceBinding();
        gc = GameContext.getInstance();
        setContentView(R.layout.activity_registrar);

        Log.d("REGIS", "This is RegistraActivity");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        wv = (WebView) findViewById(R.id.registerWebview);
        wv.loadUrl("file:///android_asset/gameLobby.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);
        wv.addJavascriptInterface(javaScriptInterface, "Android");

//        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
//            @Override
//            public void ready() {
//                //call engine that game numeric_game ready to play;
//                sendGameEvent("RegisterServerUI_Start");
//            }
//        });
    }

    @Override
    public void onGameEvent(String event, String[] params) throws ClassNotFoundException {
        Log.d("REGIS", "event: " + event);
        if(event.equals("change_engine")){
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            startActivity(intent);
        }else if(event.equals("setPlayer")){
            //params[0] -> 'A' : params[1] -> 'B'
            wv.loadUrl("javascript:setPlayer("+ params[0]+",'"+params[1]+"')");
        }else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
