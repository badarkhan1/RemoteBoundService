package com.example.remoteservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MTAG";
    Messenger messenger;
    boolean isBound;

    Button button;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        Intent intent  = new Intent(this,RemoteBoundService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: Bounded To Remote Service");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        if(!isBound) return;
        Bundle bundle = new Bundle();
        bundle.putString("toaststring","Message Received");
        Message message = Message.obtain();
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}













