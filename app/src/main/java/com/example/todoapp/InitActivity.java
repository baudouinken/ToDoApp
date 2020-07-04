package com.example.todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapp.model.DatabaseClient;
import com.example.todoapp.model.ResteasyTodoCRUDAccessor;
import com.example.todoapp.model.Todo;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

//                        // sync db with server
//                        ResteasyTodoCRUDAccessor serverAccessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/");
//                        List<Todo> todosRemote = serverAccessor.getTodoList();
//                        List<Todo> todosLocal = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().getAll();
//                        if(todosLocal.size() > 0) {
//                            serverAccessor.createTodoList(todosLocal);
//                        } else if(todosLocal.size() > 0 && todosRemote.size() > 0) {
//                            for(Todo t : todosRemote) {
//                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().insert(t);
//                            }
//                        }


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
                                startActivity(new Intent(InitActivity.this, MainActivity.class));
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
