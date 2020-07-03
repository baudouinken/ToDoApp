package com.example.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapp.model.DatabaseClient;
import com.example.todoapp.model.Todo;

import java.util.Calendar;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextTodo, editTextDesc, txtDate, txtTime;
    Button btnDatePicker, btnTimePicker;
    private  int mYear, mMonth, mDay, mHour, mMinute;
    Date date = new Date();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        editTextTodo = findViewById(R.id.editTextTodo);
        editTextDesc = findViewById(R.id.editTextDesc);

        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);

        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == btnDatePicker){
            //GEt currwnt Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    txtDate.setText(day +"-"+(month+1)+"-"+year);
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                }
            }, mYear , mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btnTimePicker){
            //GEt currwnt Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            //Lauch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    txtTime.setText(hour+" : "+minute);
                    mMinute = minute;
                    mHour = hour;
                }
            }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    public void saveTodo(){
        Date date = new Date(mYear, mMonth, mDay, mHour, mMinute);

        final String sTask = editTextTodo.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final long curDate = date.getTime();

        if (sTask.isEmpty()){
            editTextTodo.setError("Todo Required");
            editTextTodo.requestFocus();
            return;
        }

        if (sDesc.isEmpty()){
            editTextDesc.setError("Desc Required");
            editTextDesc.requestFocus();
            return;
        }


        class SaveTodo extends AsyncTask<Void, Void, Void> {

            protected Void doInBackground(Void... voids){
                //Create a new Todo
                Todo todo = new Todo();
                todo.setName(sTask);
                todo.setDesc(sDesc);
                todo.setFinished(false);
                todo.setFavorite(false);
                todo.setDueDate(curDate);

                //Adding to db
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().insert(todo);
                return null;
            }

            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTodo st = new SaveTodo();
        st.execute();

    }

}
