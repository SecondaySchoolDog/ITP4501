package com.example.itp4501project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button BtnStart, BtnClose, BtnPlayerRecord,BtnRanking;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BtnStart = findViewById(R.id.BtnPlay);
        BtnClose = findViewById(R.id.BtnClose);
        BtnRanking = findViewById(R.id.BtnRanking);
        BtnPlayerRecord = findViewById(R.id.BtnPlayerRecord);
        getSupportActionBar().hide();

        BtnStart.setOnClickListener(this);
        BtnPlayerRecord.setOnClickListener(this);
        BtnClose.setOnClickListener(this);
        BtnRanking.setOnClickListener(this);

        layout = findViewById(R.id.layout);
        layout.getBackground().setAlpha(160);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.BtnPlay:
                intent = new Intent(this, play_game.class);
                intent.putExtra(EXTRA_MESSAGE, "5573");
                startActivity(intent);
                finish();
                break;
            case R.id.BtnPlayerRecord:
                intent = new Intent(this, play_Record.class);
                intent.putExtra(EXTRA_MESSAGE, "4413");
                startActivity(intent);
                finish();
                break;
            case R.id.BtnRanking:
                intent = new Intent(this, play_Ranking.class);
                intent.putExtra(EXTRA_MESSAGE, "6671");
                startActivity(intent);
                finish();
                break;
            case R.id.BtnClose:
                finish();
                break;
        }
    }

}