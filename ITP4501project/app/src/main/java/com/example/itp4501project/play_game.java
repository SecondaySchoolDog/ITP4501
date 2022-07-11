package com.example.itp4501project;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class play_game extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase db;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,BtnReset,BtnBack;
    private LinearLayout Layout1, Layout2;
    String RoleSwitch = "X";
    int UserInputTimes = 0;
    TextView TxtTimer;
    Boolean GameIsFinish = false;
    String DateSql,TimeSql;
    private Handler mHandler = new Handler();
    public static final String EXTRA_MESSAGE = "com.example.ITP4501.MESSAGE";
    private int seconds = 0;
    private boolean running;

    private static final int[] BUTTON_IDS = {
            R.id.btn1,
            R.id.btn2,
            R.id.btn3,
            R.id.btn4,
            R.id.btn5,
            R.id.btn6,
            R.id.btn7,
            R.id.btn8,
            R.id.btn9,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        BtnReset = (Button)findViewById(R.id.BtnReset);
        BtnBack = (Button) findViewById(R.id.BtnBack);

        TxtTimer = findViewById(R.id.TxtTimer);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        BtnReset.setOnClickListener(this);
        BtnBack.setOnClickListener(this);

        Layout1 = findViewById(R.id.Layout);
        Layout2 = findViewById(R.id.Layout2);

        runTimer();
    }

    private void runTimer()
    {
        // Creates Handler
        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Data Formatting
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, secs);

                TxtTimer.setText(time);

                // If running is true, increment the seconds.
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    protected void onPause()
    {
        super.onPause();
        running = false;
    }

    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
    }

    // computer player delay setting
    private Runnable DelayRunnale = new Runnable() {
        @Override
        public void run() {
            ComputerRandomControl();
        }
    };

    // user click button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnReset:
                // replay the game
                resetTheGame();
                onClickReset(v); // refresh the timer
                break;
            case R.id.BtnBack:
                // go back main menu
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "5573");
                startActivity(intent);
                finish();
            default:
                if(!running && seconds == 0) running=true; // click game button, game process
                if(!GameIsFinish) {
                    if(RoleSwitch== "X") PlayerClickButtonControl(v); // player round
                    if(RoleSwitch== "O" && !GameIsFinish)
                        mHandler.postDelayed(DelayRunnale,200); //call computer player
                }
        }
    }

    private void ComputerRandomControl() {
        int randomnum = (int) (Math.random() * 9);
        Button button = (findViewById(BUTTON_IDS[randomnum]));
        if(button.getText()!="")
            ComputerRandomControl();
        else {
            button.setText(RoleSwitch);
            checkGameIsCompleted();
            SwitchRole();
        }
    }

    private void DisplayMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void resetTheGame(){
        GameIsFinish = false;
        for(int x:BUTTON_IDS){
            Button temp = (findViewById(x));
            temp.setText("");
        }
        UserInputTimes = 0;
        RoleSwitch = "X";
    }

    private void checkGameIsCompleted(){
        if(checkTreeNumberIsMatch(btn1.getText(),btn2.getText(),btn3.getText())) //direction: -
            IsBingo();
        else if(checkTreeNumberIsMatch(btn4.getText(),btn5.getText(),btn6.getText()))
            IsBingo();
        else if(checkTreeNumberIsMatch(btn7.getText(),btn8.getText(),btn9.getText()))
            IsBingo();

        else if(checkTreeNumberIsMatch(btn1.getText(),btn4.getText(),btn7.getText())) // direction : |
            IsBingo();
        else if(checkTreeNumberIsMatch(btn2.getText(),btn5.getText(),btn8.getText()))
            IsBingo();
        else if(checkTreeNumberIsMatch(btn3.getText(),btn6.getText(),btn9.getText()))
            IsBingo();

        else if(checkTreeNumberIsMatch(btn1.getText(),btn5.getText(),btn9.getText()))    //direction : X
            IsBingo();
        else if(checkTreeNumberIsMatch(btn3.getText(),btn5.getText(),btn7.getText()))
            IsBingo();
        else if(UserInputTimes >= 5)
            StopTheGame("Draw");
        Log.d("finish checking","able to check game is finish");
    }

    private void IsBingo(){
        StopTheGame(RoleSwitch=="X"?"You win":"You lose");
    }

    private void StopTheGame(String Winner){
        GameIsFinish = true;
        onPause(); // stop the timer
        DisplayMessage("Game over", Winner);
        getDateTime();
        insertDataIntoLocalDatabase(DateSql, TimeSql, String.valueOf(seconds) , Winner);
    }

    private void insertDataIntoLocalDatabase(String D, String T, String S, String winner){
        checkTableIfExist();
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4501project/MyRecord",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);

            ContentValues pairValue = new ContentValues();
            pairValue.put("playDate", D);
            pairValue.put("playTime", T);
            pairValue.put("duration", S);
            pairValue.put("winningStatus", winner);
            pairValue.put("Btn1", btn1.getText()!=""?(String) btn1.getText():"");
            pairValue.put("Btn2", btn2.getText()!=""?(String) btn2.getText():"");
            pairValue.put("Btn3", btn3.getText()!=""?(String) btn3.getText():"");
            pairValue.put("Btn4", btn4.getText()!=""?(String) btn4.getText():"");
            pairValue.put("Btn5", btn5.getText()!=""?(String) btn5.getText():"");
            pairValue.put("Btn6", btn6.getText()!=""?(String) btn6.getText():"");
            pairValue.put("Btn7", btn7.getText()!=""?(String) btn7.getText():"");
            pairValue.put("Btn8", btn8.getText()!=""?(String) btn8.getText():"");
            pairValue.put("Btn9", btn9.getText()!=""?(String) btn9.getText():"");
            db.insert("GamesLog", null, pairValue);
            db.close();
        }catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }

    private void getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        DateSql =  dateFormat.format(date);
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        TimeSql = dateFormat.format(date);
    }

    private void checkTableIfExist(){

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.itp4501project/MyRecord",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String sql = "";
            //db.execSQL("DROP TABLE IF EXISTS GamesLog");// for testing
            sql = "CREATE TABLE GamesLog (gameID INTEGER PRIMARY KEY AUTOINCREMENT, playDate text, playTime text, duration INTEGER, winningStatus text, Btn1 text, Btn2 text, Btn3 text, Btn4 text, Btn5 text, Btn6 text, Btn7 text, Btn8 text, Btn9 text)";
            db.execSQL(sql);   // if the table not exist, go back exception
        }catch (Exception ex){
            Log.d("Try To Create table", "The table is exist");
        }
        db.close();
    }

    private boolean checkTreeNumberIsMatch(CharSequence text1, CharSequence text2, CharSequence text3){
        return text1 == RoleSwitch && text2 == RoleSwitch && text3 == RoleSwitch;
    }

        private void PlayerClickButtonControl(View v) {
        switch (v.getId()){
            case R.id.btn1:
                if (btn1.getText()=="") btn1.setText(RoleSwitch);    // input repeat checking
                    else return;
                break;
            case R.id.btn2:
                if (btn2.getText()=="") btn2.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn3:
                if (btn3.getText()=="") btn3.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn4:
                if (btn4.getText()=="") btn4.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn5:
                if (btn5.getText()=="") btn5.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn6:
                if (btn6.getText()=="") btn6.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn7:
                if (btn7.getText()=="") btn7.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn8:
                if (btn8.getText()=="") btn8.setText(RoleSwitch);
                else return;
                break;
            case R.id.btn9:
                if (btn9.getText()=="") btn9.setText(RoleSwitch);
                else return;
                break;
        }
        UserInputTimes++;
        Log.d("able to click button","able to click button");
        checkGameIsCompleted();
        SwitchRole();
    }

    private void SwitchRole(){
        if(Objects.equals(RoleSwitch, "X"))
            RoleSwitch = "O";
        else RoleSwitch = "X";
    }
}