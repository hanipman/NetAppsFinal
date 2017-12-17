package com.example.chris.netappsfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class PlayGame extends AppCompatActivity {

    TextView mTextview;
    Thread publishThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_play_game);
        mTextview = (TextView) findViewById(R.id.textView);
        mTextview.setText(getIntent().getStringExtra("user"));
        setupConnectionFactory();
    }

    ConnectionFactory factory = new ConnectionFactory();
    private void setupConnectionFactory() {
        factory.setAutomaticRecoveryEnabled(false);
        factory.setHost("10.0.0.2");
        factory.setVirtualHost("test");
        factory.setUsername("pi");
        factory.setPassword("raspberry");
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String mess = "player1";
                try {
                    Connection connection = factory.newConnection();
                    Channel ch = connection.createChannel();
                    ch.exchangeDeclare("apptoserver", "direct");
                    ch.basicPublish("apptoserver", "server", null, mess.getBytes());
                    Log.d("", "[s] " + mess);
                } catch (Exception e) {
                    Log.d("", "[f] " + mess);
                    e.printStackTrace();
                }
            }
        });
        publishThread.start();
        publishThread.interrupt();
    }
}
