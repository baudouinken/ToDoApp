package com.example.todoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

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
        loginButton.setEnabled(false);

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                err1.setVisibility(View.INVISIBLE);
                err2.setVisibility(View.INVISIBLE);
                if(pwd.getText().toString().length() == 6 && email.getText().toString().length() != 0){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pwd.getText().toString().length() == 6 && email.getText().toString().length() != 0){
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

                if(!TextUtils.isEmpty(email.getText()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    err1.setVisibility(View.VISIBLE);
                }
                else {
                    new AsyncTask<Void, Void, Object>() {

                        private ProgressDialog dialog = null;

                        @Override
                        protected void onPreExecute() {

                            dialog = ProgressDialog.show(LoginActivity.this,
                                    "Login...", "Please wait...");
                        }

                        @Override
                        protected Object doInBackground(Void... arg) {
                            try {
                                String urlParameters = "email="+email.getText().toString()+"&pwd="+pwd.getText().toString();
                                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                                int postDataLength = postData.length;
                                String request = "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/login";
                                URL url = new URL(request);
                                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                                conn.setDoOutput(true);
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestProperty("charset", "utf-8");
                                conn.setRequestProperty("Content-Length", Integer.toString( postDataLength));
                                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                                wr.write( postData );
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    return new Boolean(true);
                                } else {
                                     return new Boolean(false);
                                }
                            } catch (Exception e) {
                                Log.i(LoginActivity.class.getName(), e.getMessage());
                                return e;
                            }

                        }

                        @Override
                        protected void onPostExecute(Object result) {
                            dialog.cancel();
                            Log.i(LoginActivity.class.getName(), result.toString());
                            if (result instanceof Boolean) {
                                if(result == Boolean.TRUE) {
                                    Log.i(LoginActivity.class.getName(), "yes");
                                } else {
                                    Log.i(LoginActivity.class.getName(), "no");
                                }
                            } else {
                                Log.i(LoginActivity.class.getName(), "Exception");
                            }
                        }
                    }.execute();
                }

            }
        });

    }
}
