package com.example.todoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.todoapp.model.DatabaseClient;
import com.example.todoapp.model.ResteasyTodoCRUDAccessor;
import com.example.todoapp.model.Todo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final TextView err1 = findViewById(R.id.errormessage1);
        final TextView err2 = findViewById(R.id.errormessage2);
        final EditText email = findViewById(R.id.email);
        final EditText pwd = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        final boolean[] emailValid = {false};

        loginButton.setEnabled(false);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                err1.setVisibility(View.INVISIBLE);
                err2.setVisibility(View.INVISIBLE);
                if(!TextUtils.isEmpty(email.getText()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    emailValid[0] = false;
                }
                else emailValid[0] = true;
                if(pwd.getText().toString().length() == 6 && emailValid[0]){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!emailValid[0]){
                        err1.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pwd.getText().toString().length() == 6 && emailValid[0]){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
                err2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LoginActivity.class.getName(), "onClick(): " + v);
                Log.i(LoginActivity.class.getName(), email.getText().toString() + ", " + pwd.getText().toString());

                if(emailValid[0]) {

                    // hide keyboard
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(loginButton.getWindowToken(), 0);

                    new AsyncTask<Void, Void, Object>() {

                        private ProgressDialog dialog = null;

                        @Override
                        protected void onPreExecute() {
                            dialog = ProgressDialog.show(LoginActivity.this, "Login...", "Please wait...");
                        }

                        @Override
                        protected Object doInBackground(Void... arg) {
                            try {
                                // connect to backend
                                String request = "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/login?email="+email.getText().toString()+"&pwd="+pwd.getText().toString();
                                URL url = new URL(request);
                                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestProperty("charset", "utf-8");
                                conn.setConnectTimeout(10000);
                                // check connection result
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                                    // if valid login sync db and server
                                    ResteasyTodoCRUDAccessor serverAccessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/");
                                    List<Todo> todosRemote = serverAccessor.getTodoList();
                                    List<Todo> todosLocal = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().getAll();
                                    if(todosLocal.size() > 0) {
                                        serverAccessor.createTodoList(todosLocal);
                                    } else if(todosLocal.size() == 0 && todosRemote.size() > 0) {
                                        for(Todo t : todosRemote) {
                                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().insert(t);
                                        }
                                    }

                                    return new Boolean(true);
                                } else if(conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                                     return new Boolean(false);
                                } else {
                                    throw new Exception("Connection Error");
                                }
                            } catch (Exception e) {
                                Log.i(LoginActivity.class.getName(), e.getMessage());
                                return e;
                            }

                        }

                        @Override
                        protected void onPostExecute(Object result) {
                            dialog.cancel();
                            // handle connection result
                            Log.i(LoginActivity.class.getName(), result.toString());
                            if (result instanceof Boolean) {
                                if(result.equals(Boolean.TRUE)) {
                                    startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
                                } else {
                                    err2.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                }
            }
        });

    }
}
