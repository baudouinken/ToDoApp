package com.example.todoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;

public class InitActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_init);

        new AsyncTask<Void, Void, Object>() {

            private ProgressBar bar = InitActivity.this.findViewById(R.id.progressBar);

            @Override
            protected void onPreExecute() {
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Void... arg) {
                try {
                    // check for server-availability
                    String request = "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/available";
                    URL url = new URL(request);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6000);
                    // check connection result
                    return new Integer(conn.getResponseCode());
                } catch (Exception e) {
                    Log.i(LoginActivity.class.getName(), e.getMessage());
                    return e;
                }

            }

            @Override
            protected void onPostExecute(Object result) {
                bar.setVisibility(View.INVISIBLE);
                Log.i(InitActivity.class.getName(), result+"");
                // handle connection result
                if (result instanceof Integer) {
                    if (((Integer) result).intValue() == HttpURLConnection.HTTP_OK) {
                        startActivity(new Intent(InitActivity.this, LoginActivity.class));
                        return;
                    } else {
                        Log.i(InitActivity.class.getName(), "Check Availability: got HTTP Code "+((Integer) result).intValue()+"");
                    }
                }
                Toast.makeText(InitActivity.this, "Couldn't connect to server...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InitActivity.this, MainActivity.class));
            }
        }.execute();
    }

}
