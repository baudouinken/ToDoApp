package com.example.todoapp;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                err1.setVisibility(View.INVISIBLE);
                err2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pwd.getText().toString().length() == 6){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
                err2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LoginActivity.class.getName(), "onClick(): " + v);
                Log.i(LoginActivity.class.getName(), email.getText().toString() + ", " + pwd.getText().toString());

                if(!TextUtils.isEmpty(email.getText()) && !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    err1.setVisibility(View.VISIBLE);
                }

                final boolean[] valid = {false};
                final ProgressBar pb = findViewById(R.id.progressBar);
                final Timer t = new Timer();
                final TimerTask tt = new TimerTask() {
                    int counter = 0;
                    @Override
                    public void run() {
                        counter++;
                        pb.setProgress(counter);
                        if(counter == 50 && !valid[0]) {
                            pb.setVisibility(View.INVISIBLE);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    err2.setVisibility(View.VISIBLE);
                                }
                            });
                            t.purge();
                            cancel();
                        }
                        if(counter == 100){
                            t.purge();
                            //startActivity(new Intent(LoginActivity.this, ToDoListActivity.class));
                        }
                    }
                };

                if(!(email.getText().toString().equals("paul@gmail.com") && pwd.getText().toString().equals("123456"))){
                    pb.setVisibility(View.VISIBLE);
                    valid[0] = true;
                    t.schedule(tt, 0, 30);
                }
                else{
                    pb.setVisibility(View.VISIBLE);
                    valid[0] = false;
                    t.schedule(tt, 0, 30);
                }
            }
        });

    }
}
