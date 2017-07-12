package com.example.ben.overthrow;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
    private long timeStamp2;
    boolean stopTimer;
    private int moveableSize = 0;
    private boolean grow = false;
    Bitmap moveable;

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStamp = System.currentTimeMillis();
        timeStamp2 = System.currentTimeMillis();
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
            row.setVerticalGravity(Gravity.CENTER);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < size; j++) {
                ImageButton btnTag = new ImageButton(this);
                btnTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        handleButtonClick(view);
                    }
                });
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(165 + 5 * size * (7 - size), 165 + 5 * size * (7 - size)));
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
                            time = (System.currentTimeMillis() - timeStamp);
                            if (time >= 1000)
                                updateTimer((int) time / 1000);
                            time = (System.currentTimeMillis() - timeStamp2);
                            if (time >= 100) {
                                timeStamp2 = System.currentTimeMillis();
                                updateMoveables();
                                if (grow)
                                    moveableSize++;
                                else
                                    moveableSize--;
                                if (moveableSize <= 0)
                                    grow = true;
                                else if (moveableSize > 5)
                                    grow = false;
                            }
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
            if (game.getSelected() != null) {
                game.setBoard(game.getSelected().x, game.getSelected().y, game.getPlayerTurn());
                game.setSelected(null);
                game.clearPossibles();
            }
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
        //game.setBoard(x, y, game.getPlayerTurn());
        try {
            if (game.isValidSelection(x, y)) {
                if(game.getBoard()[y][x] > 0) {
                    Point lastTile = game.getSelected();
                    if (lastTile != null && lastTile.x != x && lastTile.y != y) {
                        game.setBoard(lastTile.x, lastTile.y, game.getBoard()[lastTile.y][lastTile.x] - 4);
                        game.clearPossibles();
                    }
                    if (game.getSelected() != null && game.getSelected().x == x && game.getSelected().y == y) {
                        game.setSelected(null);
                        game.clearPossibles();
                        game.setBoard(x, y, game.getPlayerTurn());
                    } else {
                        game.setSelected(x, y);
                        game.setBoard(x, y, game.getPlayerTurn() + 4);
                        game.setPossibleMoves(x, y);
                    }
                    //game.nextPlayer();
                } else if (game.getBoard()[y][x] < 0) {

                }
                refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        int board[][] = game.getBoard();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                int resID = getResources().getIdentifier((y * board.length + x) + "", "id", getPackageName());
                ImageButton button = ((ImageButton) findViewById(resID));
                changeButton(button, board[y][x]);
            }
        }
        updateTurnText();
    }

    public BitmapDrawable getMoveable() {
        Rect rect = new Rect(0, 0, 50, 50);
        Rect actualRec = new Rect(moveableSize + 2, moveableSize + 1, (int) (45 - moveableSize * 1.5), (int) (45 - moveableSize * 1.5));
        Paint paint= new Paint();
        paint.setColor(Color.parseColor(game.getCurrentPlayerColor()));
        moveable = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(moveable);

        canvas.drawRect(actualRec, paint);
        BitmapDrawable d = new BitmapDrawable(getResources(), moveable);
        return d;
    }

    public void updateMoveables() {
        int board[][] = game.getBoard();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] >= 0)
                    continue;
                int resID = getResources().getIdentifier((y * board.length + x) + "", "id", getPackageName());
                ImageButton button = ((ImageButton) findViewById(resID));
                changeButton(button, board[y][x]);
            }
        }
    }

    public void changeButton(ImageButton button, int player) {
        if (player == 0)
            button.setBackgroundResource(android.R.drawable.btn_default);
        else if (player == 1)
            button.setBackgroundResource(R.drawable.redblock);
        else if (player == 2)
            button.setBackgroundResource(R.drawable.blueblock);
        else if (player == 3)
            button.setBackgroundResource(R.drawable.greenblock);
        else if (player == 4)
            button.setBackgroundResource(R.drawable.purpleblock);
        else if (player == 5)
            button.setBackgroundResource(R.drawable.redblocks);
        else if (player == 6)
            button.setBackgroundResource(R.drawable.blueblocks);
        else if (player == 7)
            button.setBackgroundResource(R.drawable.greenblocks);
        else if (player == 8)
            button.setBackgroundResource(R.drawable.purpleblocks);
        else if (player < 0)
            button.setBackgroundDrawable(getMoveable());

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

