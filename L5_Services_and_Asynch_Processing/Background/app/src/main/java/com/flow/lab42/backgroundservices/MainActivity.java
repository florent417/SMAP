package com.flow.lab42.backgroundservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.startServBtn) Button startServBtn;
    @BindView(R.id.stopServBtn) Button stopServBtn;
    @BindView(R.id.bindServBtn) Button bindServBtn;
    @BindView(R.id.unbindServBtn) Button unbindServBtn;
    @BindView(R.id.getStateBtn) Button getStateBtn;

    private static final String LOG = "MAIN";

    //for bound counting service
    // For some reason it keeps it alive, even though ondestroy is called
    // a strong reference
    BoundService boundService;
    private boolean servBound = false;
    //private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*
        // Don't get the bg service
        startServBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackgroundService.class);

                startService(intent);
            }
        }); */
        bindServBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindToBoundService();
            }
        });

        unbindServBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindBoundService();
            }
        });

        getStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = boundService.getNumberSpeacial();
                Toast.makeText(MainActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ServiceConnection boundServConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                // This is called when the connection with the service has been
                // established, giving us the service object we can use to
                // interact with the service.  Because we have bound to a explicit
                // service that we know is running in our own process, we can
                // cast its IBinder to a concrete class and directly access it.
                //ref: http://developer.android.com/reference/android/app/Service.html
                BoundService.LocalBinder localBinder = (BoundService.LocalBinder) service;
                boundService = localBinder.getService();
                servBound = true;

                //TODO: probably a good place to update UI after data loading
            }

            @Override
            public void onServiceDisconnected(ComponentName className) {
                // This is called when the connection with the service has been
                // unexpectedly disconnected -- that is, its process crashed.
                // Because it is running in our same process, we should never
                // see this happen.
                //ref: http://developer.android.com/reference/android/app/Service.html
                boundService = null;
                Log.d(LOG, "Counting service disconnected");
        }
    };

    void bindToBoundService() {
        bindService(new Intent(MainActivity.this,
                BoundService.class), boundServConn, Context.BIND_AUTO_CREATE);
        servBound = true;
    }

    void unbindBoundService(){
        boundService = null;
        unbindService(boundServConn);
        servBound = false;
    }

}
