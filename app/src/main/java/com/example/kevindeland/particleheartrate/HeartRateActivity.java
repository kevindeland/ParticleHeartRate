package com.example.kevindeland.particleheartrate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

public class HeartRateActivity extends AppCompatActivity {

    String TAG = "httpconnect";


    String root = "https://api.particle.io/v1/devices/";
    String deviceId = "32003e000f47343432313031";
    String variableName = "analogValue";
    String accessToken = "0254d5b08f40bd75f2a4d8878c1ed6ba13c5880c";
    String fullUrl = root + deviceId + "/" + variableName + "?access_token=" + accessToken;

    private RetrieveData dataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        Log.d(TAG, fullUrl);


    }


    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    void getData (View view) {
        dataRetriever = new RetrieveData();
        dataRetriever.execute(fullUrl);
    }

    class RetrieveData extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String stream = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                stream = readStream(in);
                Log.d(TAG, stream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("async", stream);
            return stream;
        }

        protected void onPostExecute(String stream) {
            Log.d("aync2", stream);

            try {
                JSONObject json = null;

                json = new JSONObject(stream);
                String name = json.getString("name");
                int result = json.getInt("result");

                TextView heartRateLabel = (TextView) findViewById(R.id.heartRateLabel);
                heartRateLabel.setText(name);
                TextView heartRate = (TextView) findViewById(R.id.heartRate);
                heartRate.setText(""+result);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
