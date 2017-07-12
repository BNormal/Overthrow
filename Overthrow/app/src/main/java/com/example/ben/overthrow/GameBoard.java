package com.example.ben.overthrow;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameBoard extends AppCompatActivity {

    private Game game;
    private long timeStamp;
    boolean stopTimer;

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStamp = System.currentTimeMillis();
        stopTimer = false;
        Intent intent = getIntent();
        int data[] = intent.getIntArrayExtra("data");
        // 0 = size, 1 = players, 2 = timer
        game = new Game(data[0], data[1], data[2]);
        setContentView(R.layout.activity_game_board);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutBoard);
        if (data[1] == 2) {
            TextView txtP3 = (TextView) findViewById(R.id.txtP3);
            TextView txtP4 = (TextView) findViewById(R.id.txtP4);
            TextView txtP3Score = (TextView) findViewById(R.id.txtP3Score);
            TextView txtP4Score = (TextView) findViewById(R.id.txtP4Score);
            txtP3.setText("");
            txtP4.setText("");
            txtP3Score.setText("");
            txtP4Score.setText("");
        }
        Button btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameBoard.this, MainMenu.class));
            }
        });
        ProgressBar timer = (ProgressBar) findViewById(R.id.timer);
        timer.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        int size = game.getSize();
        for (int i = 0; i < size; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setHorizontalGravity(Gravity.CENTER);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < size; j++) {
                ImageButton btnTag = new ImageButton(this);
                btnTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        handleButtonClick(view);
                    }
                });
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(170 + 5 * size * (7 - size), 170 + 5 * size * (7 - size)));
                btnTag.setId(j + (i * size));
                row.addView(btnTag);
            }
            layout.addView(row);
        }
        refresh();
        startTimerThread();
    }

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            long time;
            private long startTime = System.currentTimeMillis();
            public void run() {
                while (!stopTimer) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            time = (System.currentTimeMillis() - timeStamp) / 1000;
                            if (time >= 1)
                                updateTimer((int) time);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    public void updateTimer(int time) {
        ProgressBar timer = (ProgressBar) findViewById(R.id.timer);
        if (time <= game.getTimer()) {
            timer.setProgress((60 / game.getTimer() * (game.getTimer() - time)));
        } else if (time > game.getTimer()){
            timeStamp = System.currentTimeMillis();
            timer.setProgress(60);
            timer.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            game.nextPlayer();
            refresh();
        }
        double timeRemainingPercentage = 100.0 / 60.0 * timer.getProgress();
        if (timeRemainingPercentage <= 50 && timeRemainingPercentage >= 15) {
            int fade = (int) (255.0 / 35.0 * (timeRemainingPercentage - 15));
            timer.setProgressTintList(ColorStateList.valueOf(Color.argb(255, 255, fade, fade)));
        }
    }

    public void handleButtonClick(View view) {
        ImageButton btnChip = (ImageButton)view;
        int y = btnChip.getId() / game.getSize();
        int x = btnChip.getId() % game.getSize();
        Log.d("Button Info:", "ID: " + btnChip.getId() + ", X: " + x + ", Y: " + y + ", time: " + ((System.currentTimeMillis() - timeStamp) / 1000) + " Seconds.");
        game.setBoard(x, y, game.getPlayerTurn());
        game.nextPlayer();
        refresh();
    }

    public void refresh() {
        int board[][] = game.getBoard();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] != 0) {
                    int resID = getResources().getIdentifier((y * board.length + x) + "", "id", getPackageName());
                    ImageButton button = ((ImageButton) findViewById(resID));
                    changeButton(button, board[y][x]);
                }
            }
        }
        updateTurnText();
    }

    public void changeButton(ImageButton button, int player) {
        if (player == 1)
            button.setBackgroundResource(R.drawable.redblock);
        else if (player == 2)
            button.setBackgroundResource(R.drawable.blueblock);
        else if (player == 3)
            button.setBackgroundResource(R.drawable.greenblock);
        else if (player == 4)
            button.setBackgroundResource(R.drawable.purpleblock);
    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    public void updateTurnText() {
        TextView txtTurn = (TextView) findViewById(R.id.txtTurn);
        txtTurn.setText("Player " + game.getPlayerTurn() + "\nYour Turn!");
        String color = game.getCurrentPlayerColor();
        txtTurn.setTextColor(Color.parseColor(color));
    }

}

