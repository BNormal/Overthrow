package com.example.ben.overthrow;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class Victory extends AppCompatActivity {

    private boolean stopTimer;
    private long timeStamp;
    private int waitTime;
    private int vStage;
    private TextView txtP1;
    private TextView txtP2;
    private TextView txtP3;
    private TextView txtP4;
    private TextView txtGrats;
    private TextView txtWinner;
    private Button btnMenu;
    private ExplosionField explosionField;
    private int data[];
    List<Player> scoreOrder = new ArrayList<Player>();;

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);
        explosionField = ExplosionField.attach2Window(this);
        waitTime = 500;
        timeStamp = System.currentTimeMillis();
        vStage = 0;
        stopTimer = false;
        txtP1 = (TextView) findViewById(R.id.txtP1);
        txtP2 = (TextView) findViewById(R.id.txtP2);
        txtP3 = (TextView) findViewById(R.id.txtP3);
        txtP4 = (TextView) findViewById(R.id.txtP4);
        txtGrats = (TextView) findViewById(R.id.txtGrats);
        txtWinner = (TextView) findViewById(R.id.txtWinner);
        btnMenu = (Button) findViewById(R.id.btnHome);
        Intent intent = getIntent();
        data = intent.getIntArrayExtra("data");
        txtP1.setText("P1 Score: " + data[0]);
        txtP2.setText("P2 Score: " + data[1]);
        for (int i = 0; i < data[4]; i++)
            scoreOrder.add(new Player(i + 1, data[i]));
        Collections.sort(scoreOrder, new PlayerComparator());
        if (data[4] == 4) {
            txtP3.setText("P3 Score: " + data[2]);
            txtP4.setText("P4 Score: " + data[3]);
        } else {
            txtP3.setText("");
            txtP4.setText("");
        }
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Victory.this, MainMenu.class));
                finish();
            }
        });
        startTimerThread();
    }
    public String getPlayerColor(int player) {
        switch (player) {
            case 1: return "#ED1C24";//red
            case 2: return "#00A2E8";//blue
            case 3: return "#22B14C";//green
            case 4: return "#A349A4";//purple
        }
        return "#ffffff";
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
                                if (vStage == 0) {
                                    vStage = 1;
                                    waitTime = 2000;
                                    txtWinner.setTextColor(Color.parseColor(getPlayerColor(scoreOrder.get(data[4] == 2 ? 1 : 3).getId())));
                                    txtWinner.setText("Player " + scoreOrder.get(data[4] == 2 ? 1 : 3).getId() + "'s Score: " + scoreOrder.get(data[4] == 2 ? 1 : 3).getScore());
                                } else if (vStage == 1) {
                                    waitTime = 1000;
                                    vStage = 2;
                                    explosionField.explode(txtWinner);
                                } else if (vStage == 2) {
                                    waitTime = 2000;
                                    vStage = 3;
                                    txtWinner.setTextColor(Color.parseColor(getPlayerColor(scoreOrder.get(data[4] == 2 ? 0 : 2).getId())));
                                    txtWinner.setText("Player " + scoreOrder.get(data[4] == 2 ? 0 : 2).getId() + "'s Score: " + scoreOrder.get(data[4] == 2 ? 0 : 2).getScore());
                                    reset(txtWinner);
                                } else if (vStage == 3) {
                                    waitTime = 1000;
                                    if (data[4] == 4)
                                        vStage = 4;
                                    else
                                        vStage = 100;
                                    explosionField.explode(txtWinner);
                                } else if (vStage == 4) {
                                    waitTime = 2000;
                                    vStage = 5;
                                    txtWinner.setTextColor(Color.parseColor(getPlayerColor(scoreOrder.get(1).getId())));
                                    txtWinner.setText("Player " + scoreOrder.get(1).getId() + "'s Score: " + scoreOrder.get(1).getScore());
                                    reset(txtWinner);
                                } else if (vStage == 5) {
                                    waitTime = 1000;
                                    vStage = 6;
                                    explosionField.explode(txtWinner);
                                } else if (vStage == 6) {
                                    waitTime = 2000;
                                    vStage = 7;
                                    txtWinner.setTextColor(Color.parseColor(getPlayerColor(scoreOrder.get(0).getId())));
                                    txtWinner.setText("Player " + scoreOrder.get(0).getId() + "'s Score: " + scoreOrder.get(0).getScore());
                                    reset(txtWinner);
                                } else if (vStage == 7) {
                                    waitTime = 1000;
                                    vStage = 100;
                                    explosionField.explode(txtWinner);
                                } else if (vStage == 100) {
                                    txtWinner.setTypeface(Typeface.createFromAsset(getAssets(),"ks_hand.ttf"));
                                    txtWinner.setTextSize(50);
                                    txtWinner.setText("Player " + data[5] + "  You're The Winner!");
                                    txtWinner.setTextColor(Color.parseColor(getPlayerColor(data[5])));
                                    reset(txtWinner);
                                    btnMenu.setVisibility(View.VISIBLE);
                                    txtP1.setVisibility(View.VISIBLE);
                                    txtP2.setVisibility(View.VISIBLE);
                                    txtP3.setVisibility(View.VISIBLE);
                                    txtP4.setVisibility(View.VISIBLE);
                                    txtGrats.setVisibility(View.VISIBLE);
                                    stopTimer = true;
                                }
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
        explosionField.clear();
    }
    protected class PlayerComparator implements Comparator<Player>
    {
        public int compare(Player c1, Player c2)
        {
            return c2.getScore() - c1.getScore();
        }
    }
}

