package com.example.itp4501project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class play_Ranking extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    ListView lvSelection;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ranking);
        lvSelection = findViewById(R.id.tv_selection);
        getSupportActionBar().hide();
        new loadJsonDataInBackground().execute();
    }

    public void onClickBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "4471");
        startActivity(intent);
        finish();
    }

    private class loadJsonDataInBackground extends AsyncTask<Void, Void, String> {
        JSONArray array0;
        JSONObject object0;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        // load the json code in background
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            try {
                URL url = new URL("http://10.0.2.2/ranking_api.php");

                Log.d("connect", "try to connect URL");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();    // url connection
                Log.d("connect", "able to connect URL");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                int number = 0;
                String result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += (line + "\n");
                }

                array0 = new JSONArray(result);
                object0 = array0.getJSONObject(0);

                Log.d("test",buffer.toString());
                return null;
            } catch (Exception e) {
                Log.e("error", e.getLocalizedMessage());
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        private String[] listToArray(List record){  // transfer list to array
            String[] array = new String[record.size()];
            for (int i = 0 ; i < array.length ; i++)
                array[i] = (i+1) + record.get(i).toString();    // set ranking number and add list.toString to array
            return array;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                List<GamePlayer> PlayerRecord = new ArrayList<>();
                for (int i = 0; i < array0.length(); i++) {
                    String Name = array0.getJSONObject(i).getString("Name");
                    String Duration = array0.getJSONObject(i).getString("Duration");
                    PlayerRecord.add(new GamePlayer(Name, Integer.parseInt(Duration)));
                }
                Collections.sort(PlayerRecord, new Comparator<GamePlayer>() {   // sort the JSON data
                    @Override
                    public int compare(GamePlayer o1, GamePlayer o2) {
                        return o1.duration - o2.duration;
                    }
                });

                String[] array = listToArray(PlayerRecord);     // list to array

                lvSelection.setAdapter(new ArrayAdapter<String>(
                        play_Ranking.this,
                        android.R.layout.simple_list_item_1,
                        array));

            }catch (Exception e){
                Log.e("error", e.getMessage());
            }
        }
    }

    class GamePlayer{   // json format setting
        private String name;
        private int duration;

        GamePlayer(String name, int duration){
            this.name = name;
            this.duration = duration;
        }

        public String toString(){
            return  String.format("\t\t\t\t %20s %30s" , duration+"sec", name);
        }
    }
}