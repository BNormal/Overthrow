package com.example.ben.overthrow;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {


    private long timeStamp = System.currentTimeMillis();
    private int waitTime;
    private int flickerStage;
    private boolean lightOn;
    private ImageButton btn;
    private boolean stopTimer;

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        btn = (ImageButton)findViewById(R.id.btnPlay);
        waitTime = 5000;
        flickerStage= 0;
        lightOn = true;
        stopTimer = false;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, OptionMenu.class));
                finish();
            }
        });
        startTimerThread();
    }
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                while (!stopTimer) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            long time = (System.currentTimeMillis() - timeStamp);
                            if (time > waitTime) {
                                timeStamp = System.currentTimeMillis();
                                if (flickerStage == 0) {
                                    flickerStage = Utils.random(6, 13);
                                    if (flickerStage % 2 != 0)
                                        flickerStage++;
                                    waitTime = 100 + Utils.random(0, 250);
                                } else {
                                    lightOn = !lightOn;
                                    if (lightOn)
                                        btn.setBackgroundResource(R.drawable.play);
                                    else
                                        btn.setBackgroundResource(R.drawable.playdim);
                                    flickerStage--;
                                    if (flickerStage == 0)
                                        waitTime = 7000;
                                    else
                                        if (Utils.random(0, 10) != 1)
                                            waitTime = 30 + Utils.random(0, 130);
                                        else
                                            waitTime = 30 + Utils.random(200, 300);
                                }

                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}
