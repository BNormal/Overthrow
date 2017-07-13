package com.example.ben.overthrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class OptionMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);
        Button btnStart = (Button)findViewById(R.id.btnStart);
        Button btnOptionBack = (Button)findViewById(R.id.btnOptionBack);
        Spinner boardSpin = (Spinner) findViewById(R.id.boardSizeSpin);
        Spinner playerSpin = (Spinner) findViewById(R.id.playersSpin);
        boardSpin.setAdapter(ArrayAdapter.createFromResource(this, R.array.board_sizes, R.layout.spinner));
        playerSpin.setAdapter(ArrayAdapter.createFromResource(this, R.array.players, R.layout.spinner));
        SeekBar timerBar = (SeekBar) findViewById(R.id.timerBar);
        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView t1 = (TextView)  findViewById(R.id.txtTime);
                t1.setText((progress + 10) + " seconds");
            }
        });
        btnOptionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionMenu.this, MainMenu.class));
                finish();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = ((SeekBar) findViewById(R.id.timerBar)).getProgress() + 10;
                int size = Integer.parseInt(((Spinner) findViewById(R.id.boardSizeSpin)).getSelectedItem().toString().substring(0, 1));
                int players = Integer.parseInt(((Spinner) findViewById(R.id.playersSpin)).getSelectedItem().toString().substring(0, 1));
                Intent intent = new Intent(OptionMenu.this, GameBoard.class);
                int data[] = {size, players, time};
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
            }
        });
    }
}
