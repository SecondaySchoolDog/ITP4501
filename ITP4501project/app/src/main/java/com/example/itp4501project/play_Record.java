package com.example.itp4501project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class play_Record extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    Button btnBack, btnChart;
    ListView lvSelection;
    String[] listItem;
    LinearLayout layout;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record);
        getSupportActionBar().hide();
        btnBack =  findViewById(R.id.BtnBack);
        btnChart = findViewById(R.id.btnChart);

        lvSelection = findViewById(R.id.tv_selection);
        btnBack.setOnClickListener(this);
        btnChart.setOnClickListener(this);
        importDatabaseData();

        layout = findViewById(R.id.layout);
        layout.getBackground().setAlpha(160);
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btnChart:
                intent = new Intent(this,play_Record_Chart.class);
                intent.putExtra(EXTRA_MESSAGE, "5573");
                startActivity(intent);
                finish();
                break;
            case R.id.BtnBack:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "5573");
                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    private void importDatabaseData(){

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4501project/MyRecord",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor cursor = db.rawQuery("select * from GamesLog", null);
            Log.d("Data", "in program");

            listItem = new String[cursor.getCount()];

            int i=0;
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String d = cursor.getString(cursor.getColumnIndex("playDate"));
                @SuppressLint("Range") String t = cursor.getString(cursor.getColumnIndex("playTime"));
                @SuppressLint("Range") String s = cursor.getString(cursor.getColumnIndex("duration"));
                @SuppressLint("Range") String w = cursor.getString(cursor.getColumnIndex("winningStatus"));
                //listItem[i++] = id + ", " + d + " ,"+ t + ", " + s + ", " + w + "\n";
                listItem[i++] = String.format("%-10s %10s %9s %20s", d, t, s+"sec" ,w) ;
            }

            Log.d("ListItem", listItem.toString());

            lvSelection.setAdapter(new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listItem));
            lvSelection.setOnItemClickListener(this);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,play_Record_gameReview.class);
        intent.putExtra(EXTRA_MESSAGE, "4409");
        intent.putExtra("Id", String.valueOf(id+=1));
        startActivityForResult(intent, 7901);
        finish();
    }
}