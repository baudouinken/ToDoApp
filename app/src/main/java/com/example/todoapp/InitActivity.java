package com.example.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapp.accessor.ResteasyTodoCRUDAccessor;

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
                        ResteasyTodoCRUDAccessor.serverAvailable = true;

                        startActivity(new Intent(InitActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Log.i(InitActivity.class.getName(), "Check Availability: got HTTP Code "+((Integer) result).intValue()+"");
                    }
                }
                createAlertDialog().show();
            }
        }.execute();
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The server is currently unavailable.")
                .setPositiveButton("Only use local storage",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(InitActivity.this, TodoListActivity.class));
                                finish();
                            }
                        })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finishAndRemoveTask();
                    }
                });
        return builder.create();
    }

}
