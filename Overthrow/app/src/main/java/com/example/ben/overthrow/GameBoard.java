package com.example.ben.overthrow;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GameBoard extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    public void handleButtonClick(View view) {
        ImageButton btnChip = (ImageButton)view;
        int y = btnChip.getId() / game.getSize();
        int x = btnChip.getId() % game.getSize();
        Log.w("Button:", "ID: " + btnChip.getId() + ", X: " + x + ", Y: " + y);
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
    }

    public void changeButton(ImageButton button, int player) {
        if (player == 1)
            button.setImageResource(R.drawable.redblock);
        else if (player == 2)
            button.setImageResource(R.drawable.blueblock);
    }


}
