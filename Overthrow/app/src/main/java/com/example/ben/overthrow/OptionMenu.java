package com.example.ben.overthrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class OptionMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);
        Button btnStart = (Button)findViewById(R.id.btnStart);
        Button btnOptionBack = (Button)findViewById(R.id.btnOptionBack);
        final Spinner boardSpin = (Spinner) findViewById(R.id.boardSizeSpin);
        final Spinner playerSpin = (Spinner) findViewById(R.id.playersSpin);
        boardSpin.setAdapter(ArrayAdapter.createFromResource(this, R.array.board_sizes, R.layout.spinner));
        playerSpin.setAdapter(ArrayAdapter.createFromResource(this, R.array.players, R.layout.spinner));
        SeekBar timerBar = (SeekBar) findViewById(R.id.timerBar);
        playerSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int players = Integer.parseInt(playerSpin.getSelectedItem().toString().substring(0, 1));
                hideSomePlayersOptions(players == 2 ? true : false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                int size = Integer.parseInt(boardSpin.getSelectedItem().toString().substring(0, 1));
                int players = Integer.parseInt(playerSpin.getSelectedItem().toString().substring(0, 1));

                int switch1 = ((Switch) findViewById(R.id.switch1)).isChecked() ? 1 : 0;
                int switch2 = ((Switch) findViewById(R.id.switch2)).isChecked() ? 1 : 0;
                int switch3 = ((Switch) findViewById(R.id.switch3)).isChecked() ? 1 : 0;
                int switch4 = ((Switch) findViewById(R.id.switch4)).isChecked() ? 1 : 0;
                Intent intent = new Intent(OptionMenu.this, GameBoard.class);
                int data[];
                if (players == 2)
                    data = new int[]{size, players, time, switch1, switch2};
                else
                    data = new int[]{size, players, time, switch1, switch2, switch3, switch4};
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
            }
        });
    }

    public void hideSomePlayersOptions(boolean hide) {
        TextView txtH3 = (TextView) findViewById(R.id.txtH3);
        TextView txtC3 = (TextView) findViewById(R.id.txtC3);
        Switch switch3 = (Switch) findViewById(R.id.switch3);
        TextView txtH4 = (TextView) findViewById(R.id.txtH4);
        TextView txtC4 = (TextView) findViewById(R.id.txtC4);
        Switch switch4 = (Switch) findViewById(R.id.switch4);
        int hideIt = hide ? View.INVISIBLE : View.VISIBLE;
        txtH3.setVisibility(hideIt);
        txtC3.setVisibility(hideIt);
        switch3.setVisibility(hideIt);
        txtH4.setVisibility(hideIt);
        txtC4.setVisibility(hideIt);
        switch4.setVisibility(hideIt);
    }
}
