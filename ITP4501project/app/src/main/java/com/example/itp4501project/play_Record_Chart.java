package com.example.itp4501project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class play_Record_Chart extends AppCompatActivity implements View.OnClickListener{
    Button btnBack;
    LinearLayout linearLayout1, linearLayout2;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record_chart);
        btnBack = findViewById(R.id.BtnBack);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);

        getSupportActionBar().hide();
        btnBack.setOnClickListener(this);
        //setContentView(new Panel(this));

        Panel panel = new Panel(play_Record_Chart.this);
        linearLayout1.addView(panel);
    }

    static class Panel extends View {
        private int DataCount, winCount, loseCount, drawCount;
        private float win, lose, draw;
        String title = "Your Winning Status";
        String item[] = {"Win","Lose","Draw"};
        //float data[];
        //float data[]={50,25,25};
        int rColor[] = {0xffff0000,0xffffff00,0xff32cd32};
        float iDegree = 0;

        public Panel(Context context){
            super (context);
        }

        public void onDraw(Canvas c){
            super.onDraw(c);
            @SuppressLint("DrawAllocation") Paint paint = new Paint();

            LoadDatabaseData();
            float data[] = {win, lose, draw};

            paint.setStyle(Paint.Style.FILL);
            for(int i=0;i<data.length;i++){
                float drawDegree = data[i]*360/100;

                paint.setColor(rColor[i]);
                RectF rec = new RectF(50,140,getWidth()-50,
                        getWidth()-10);
                c.drawArc(rec,iDegree,drawDegree,true,paint);
                iDegree+=drawDegree;
            }




            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(60);
            paint.setTypeface(Typeface.SERIF);

            paint.setColor(Color.WHITE);
            c.drawRect(10,1100,550,1450,paint);
            c.drawRect(750,1100,1000,1400,paint);


            paint.setColor(Color.RED);

            c.drawText("Win Count: " + winCount,80,1180,paint);
            c.drawText("Lose Count: " + loseCount,80,1280,paint);
            c.drawText("Draw Count: " + drawCount,80,1380,paint);

            paint.setTextSize(80);
            paint.setColor(Color.BLACK);

            c.drawText(title,20,80,paint);

            int vertSpace = getHeight()-500;
            paint.setTextSize(60);
            for(int i=data.length-1;i>=0;i--){
                paint.setColor(rColor[i]);
                c.drawRect(getWidth()-300,vertSpace,getWidth()-260,
                        vertSpace+40,paint);

                paint.setColor(Color.BLACK);

                c.drawText(item[i],getWidth()-250,vertSpace+40,paint);
                vertSpace -=80;
            }
        }

        private void LoadDatabaseData(){

            try {
                SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4501project/MyRecord",
                        null, SQLiteDatabase.CREATE_IF_NECESSARY);

                Cursor cursor = db.rawQuery("select * from GamesLog", null);

                Log.d("Data", "in program");
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String w = cursor.getString(cursor.getColumnIndex("winningStatus"));
                    if(w.equals("You win")) winCount++;
                    else if(w.equals("You lose")) loseCount++;
                    else drawCount++;
                    Log.d("Get Status Data", w);
                }
                DataCount = cursor.getCount();
                win = ((float) winCount/DataCount)*100;
                lose = ((float)loseCount/DataCount)*100;
                draw = ((float)drawCount/DataCount)*100;

                Log.e("Get data", "Win=" + win + " lose=" + lose + " draw=" +draw);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.BtnBack:
                intent = new Intent(this, play_Record.class);
                intent.putExtra(EXTRA_MESSAGE, "4471");
                startActivity(intent);
                finish();
                break;
        }
    }
}