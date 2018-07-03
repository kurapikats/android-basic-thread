package com.idooh.basicthreads;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG=MainActivity.class.getSimpleName();
    private Boolean mStopLoop;
    private Button buttonStart, buttonStop;
    private TextView txtCounter;
    private int count = 0;
    private  Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Thread id: " + Thread.currentThread().getId());

        buttonStart = findViewById(R.id.startThread);
        buttonStop = findViewById(R.id.stopThread);
        txtCounter = findViewById(R.id.txtCounter);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        /**
           This is a basic app that creates a sub thread that updates the main UI

           Notes: You can't update UI from a sub thread directly
           You need to use Handler.
           Initialize Handler with reference to Main Looper (UI Thread)
           Handler needs to post a task with a Runnable instance
           then inside the run() you can update UI
         **/
        handler = new Handler(getApplicationContext().getMainLooper());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.startThread:
                mStopLoop = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(mStopLoop) {
                            try {
                                Thread.sleep(1000);
                                count++;
                            } catch (InterruptedException e) {
                                Log.i(TAG, e.getMessage());
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtCounter.setText(" " + count);
                                }
                            });
                            Log.i(TAG, "Thread id in while loop: " + count + " - " + Thread.currentThread().getId());
                        }
                    }
                }).start();
                break;
            case R.id.stopThread:
                mStopLoop = false;
                Log.i(TAG, "Stop Thread");
                break;
        }
    }
}
