package com.example.ben.overthrow;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
        game = new Game(7, 2);
        setContentView(R.layout.activity_game_board);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutBoard);
        layout.setOrientation(LinearLayout.VERTICAL);
        int size = 7;
        for (int i = 0; i < size; i++) {
            LinearLayout row = new LinearLayout(this);
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
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(170, 170));
                btnTag.setId(j + (i * size));
                row.addView(btnTag);
            }
            layout.addView(row);
        }
        refresh();
        final GameBoard g = this;
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopTimer) {
                    try {
                        new AsyncCaller(g).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Thread 1");
        thread.start();*/
    }

    public void updateTimer() {
        long time = (System.currentTimeMillis() - timeStamp) / 1000;
        ProgressBar timer = (ProgressBar) findViewById(R.id.timer);
        if (time <= 60) {
            timer.setProgress((int) (60 - time));
        } else if (time > 60){
            timeStamp = System.currentTimeMillis();
            game.nextPlayer();
        }
        //timer.getIndeterminateDrawable().setColorFilter(getResources().getColor(Color.GREEN), android.graphics.PorterDuff.Mode.SRC_IN);
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
        setTurnText("Player " + game.getPlayerTurn() + "\nYour Turn!");
    }

    public void changeButton(ImageButton button, int player) {
        if (player == 1)
            button.setImageResource(R.drawable.redblock);
        else if (player == 2)
            button.setImageResource(R.drawable.blueblock);
    }

    public void setTurnText(String text) {
        TextView txtTurn = (TextView) findViewById(R.id.txtTurn);
        txtTurn.setText(text);
    }

}

class AsyncCaller extends AsyncTask<Void, Void, Void> {

    private GameBoard gb;

    protected AsyncCaller(GameBoard gb) {
        this.gb = gb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //gb.updateTimer();
        //gb.refresh();
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
