package com.flow.lab6.networkmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    // DOnt use butterknife for the assignment
    private static final String TAG = "MainActivity";
    private final double  farhToCelc = 272.15;
    private String resp = "";
    private HttpsURLConnection urlConnection;
    private String weatherUrlFirstPart = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String weatherUrlScnPart = "&appid=56c046a9083831b485035cdf669895ed";
    @BindView(R.id.checkConnBtn)
    Button checkConnBtn;
    @BindView(R.id.weatherBtn)
    Button getWeatherBtn;
    @BindView(R.id.parseJsonBtn)
    Button parseJsonBtn;
    @BindView(R.id.parsedTxt)
    TextView parsedTxt;
    @BindView(R.id.weatherInfoTxt)
    EditText cityInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.checkConnBtn)
    public void CheckConnectivity(Button checkConnBtn){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            NetworkCapabilities nInfo = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            Log.d(TAG, "CheckConnectivity: " + nInfo.toString());
            Toast.makeText(MainActivity.this, nInfo.toString(), Toast.LENGTH_LONG).show();
        }
        else{
            NetworkInfo nInfo = connectivityManager.getActiveNetworkInfo();
        }

        //boolean isWiFi = nInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    @OnClick(R.id.weatherBtn)
    public void setGetWeatherBtn(Button weatherBtn){

        getWeather();
    }

    @OnClick(R.id.parseJsonBtn)
    public void setParseJsonBtn(Button parseJsonBtn){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        WeatherData data = gson.fromJson(resp, WeatherData.class);

        String tempInCelc = Integer.toString((int)(data.mainData.temp - farhToCelc));
        String weatherInfo = "Country: " + data.sys.country + "\nCity: " + data.name +
                "\nTemperature: " + tempInCelc;
        parsedTxt.setText(weatherInfo);
    }

    private void getWeather(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String weatherUrl = weatherUrlFirstPart + cityInput.getText().toString() + weatherUrlScnPart;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resp = response;
                        Log.d(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
        });

        requestQueue.add(stringRequest);
    }

};

/* This works as it is
ExecutorService ex = Executors.newCachedThreadPool();
        ex.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(weatherUrl);
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Read the stream
                    StringWriter sw = new StringWriter();
                    char c;
                    while((c = (char)in.read()) != -1 && c <= 128){ // 128 max value for ascii.
                        sw.append(c);
                    }
                    String AarhusWeatherStatus = sw.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherTxt.setText(AarhusWeatherStatus);
                        }
                    });
                } catch(Exception e){
                    Log.d(TAG, "GetAarhusWeatherException: " + e.toString());
                } finally {
                    urlConnection.disconnect();
                }
            }
        });
 */


/*
     Do another time,look at link from lesson
    private class AsyncTaskRunner extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                result = getStream(urls[0]);
                Log.d(TAG, "doInBackground: " + result );
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return result;
            }
        }

        private String getStream(String urlString) throws IOException{
            StringBuilder sb = new StringBuilder();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000 );
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            //urlConnection.connect();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d(TAG, "getStream: " + in.toString());
                BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                String inputLine = "";
                while((inputLine = bf.readLine()) != null) {
                    sb.append(inputLine);
                }
            } finally {
                urlConnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
            weatherTxt.setText(s);
        }
    }
    */
