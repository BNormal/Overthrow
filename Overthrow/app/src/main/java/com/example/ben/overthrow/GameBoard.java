package com.example.ben.overthrow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class GameBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutBoard);
        layout.setOrientation(LinearLayout.VERTICAL);
        int size = 7;
        for (int i = 0; i < size; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < size; j++) {
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btnTag.setText("");
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(170, 170));
                btnTag.setId(j + 1 + (i * 4));
                row.addView(btnTag);
            }
            layout.addView(row);
        }
    }
}
