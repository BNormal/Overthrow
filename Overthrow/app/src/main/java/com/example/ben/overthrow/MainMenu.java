package com.example.ben.overthrow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {


    private long timeStamp = System.currentTimeMillis();
    private int waitTime;
    private int flickerStage;
    private boolean lightOn;
    private ImageButton btnPlay;
    private boolean stopTimer;
    public static SharedPreferences.Editor settingsPrefE;
    public static SharedPreferences settingsPrefR;

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopTimer = false;
        startTimerThread();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        settingsPrefE = getSharedPreferences("Settings", 0).edit();
        settingsPrefR = getSharedPreferences("Settings", 0);
        if (!Utils.hasLoadedFiles) {
            Utils.bgMusicVolume = settingsPrefR.getInt("bgMusic", 100);
            Utils.soundEffectsVolume = settingsPrefR.getInt("soundEffects", 100);
            loadSoundEffects();
            Utils.hasLoadedFiles = true;
        }
        if (Utils.bgMusic == null) {
            Utils.bgMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgmusic);
            Utils.bgMusic.setLooping(true);
        }
        if (!Utils.hasMusic && Utils.bgMusicVolume > 0) {
            Utils.bgMusic.start();
            Utils.hasMusic = true;
        }
        waitTime = 5000;
        flickerStage = 0;
        lightOn = true;
        stopTimer = false;
        final ImageButton btnMusic = (ImageButton)findViewById(R.id.btnMusic);
        if (Utils.bgMusicVolume == 0)
            btnMusic.setBackgroundResource(R.drawable.musicoff);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.bgMusicVolume = Utils.bgMusicVolume > 0 ? 0 : 100;
                if (Utils.bgMusicVolume == 0) {
                    Utils.bgMusic.pause();
                    Utils.hasMusic = false;
                    settingsPrefE.putInt("bgMusic", 0);
                    settingsPrefE.apply();
                    btnMusic.setBackgroundResource(R.drawable.musicoff);
                } else {
                    Utils.bgMusic.start();
                    Utils.hasMusic = true;
                    settingsPrefE.putInt("bgMusic", 100);
                    settingsPrefE.apply();
                    btnMusic.setBackgroundResource(R.drawable.musicon);
                }
            }
        });
        final ImageButton btnSoundEffects = (ImageButton)findViewById(R.id.btnSoundEffects);
        if (Utils.soundEffectsVolume == 0)
            btnSoundEffects.setBackgroundResource(R.drawable.speakeroff);
        btnSoundEffects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.soundEffectsVolume = Utils.soundEffectsVolume > 0 ? 0 : 100;
                if (Utils.soundEffectsVolume == 0) {
                    settingsPrefE.putInt("soundEffects", 0);
                    settingsPrefE.apply();
                    btnSoundEffects.setBackgroundResource(R.drawable.speakeroff);
                } else {
                    settingsPrefE.putInt("soundEffects", 100);
                    settingsPrefE.apply();
                    btnSoundEffects.setBackgroundResource(R.drawable.speakeron);
                }
            }
        });
        btnPlay = (ImageButton)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, OptionMenu.class));
            }
        });

        ImageButton btnCredits = (ImageButton)findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Credits.class));
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
                                        btnPlay.setBackgroundResource(R.drawable.play);
                                    else
                                        btnPlay.setBackgroundResource(R.drawable.playdim);
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

    public void loadSoundEffects() {
        Utils.soundEffects = new ArrayList<MediaPlayer>();
        for (int i = 0; i < 8; i++) {
            int resId = getResources().getIdentifier("noise" + i, "raw", getPackageName());
            Utils.soundEffects.add(MediaPlayer.create(getApplicationContext(), resId));
        }
    }
}
