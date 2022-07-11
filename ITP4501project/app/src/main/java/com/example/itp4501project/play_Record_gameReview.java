package com.example.itp4501project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class play_Record_gameReview extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.ITP4501";
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record_game_review);
        getSupportActionBar().hide();
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);

        Intent intent = getIntent();
        id = intent.getStringExtra("Id");
        Log.d("get data", "id:" + id);
        loadDatabasButtonData();
    }

    public void onClickBlack(View v){
        Intent intent = new Intent(this, play_Record.class);
        intent.putExtra(EXTRA_MESSAGE, "4471");
        startActivity(intent);
        finish();
    }

    @SuppressLint("Range")
    private void loadDatabasButtonData(){
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4501project/MyRecord",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor cursor = db.rawQuery("select * from GamesLog WHERE gameID="+id, null);
            //Cursor cursor = db.rawQuery("select * from GamesLog Where gameID ="+id, null);

            Log.d("Data", "in program");
            while (cursor.moveToNext()) {
                btn1.setText(cursor.getString(cursor.getColumnIndex("Btn1")));
                btn2.setText(cursor.getString(cursor.getColumnIndex("Btn2")));
                btn3.setText(cursor.getString(cursor.getColumnIndex("Btn3")));
                btn4.setText(cursor.getString(cursor.getColumnIndex("Btn4")));
                btn5.setText(cursor.getString(cursor.getColumnIndex("Btn5")));
                btn6.setText(cursor.getString(cursor.getColumnIndex("Btn6")));
                btn7.setText(cursor.getString(cursor.getColumnIndex("Btn7")));
                btn8.setText(cursor.getString(cursor.getColumnIndex("Btn8")));
                btn9.setText(cursor.getString(cursor.getColumnIndex("Btn9")));
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}