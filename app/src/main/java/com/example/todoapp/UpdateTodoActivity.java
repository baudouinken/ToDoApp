package com.example.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapp.room.DatabaseClient;
import com.example.todoapp.accessor.ResteasyTodoCRUDAccessor;
import com.example.todoapp.model.Todo;

import java.util.Calendar;
import java.util.Date;

public class UpdateTodoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextTodo, editTextDesc;
    private TextView editTextDate, editTextTime;
    private CheckBox checkBoxFinished, checkBoxFavorite;
    private Button btn_edit_date, btn_edit_time, btn_contacts;
    private  int mYear, mMonth, mDay, mHour, mMinute;

    Todo todo;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        editTextTodo = findViewById(R.id.editTextTodo);
        editTextDesc = findViewById(R.id.editTextDesc);

        checkBoxFinished = findViewById(R.id.checkBoxFinished);
        checkBoxFavorite = findViewById(R.id.checkBoxFavorite);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);

        btn_edit_date = findViewById(R.id.btn_edit_date);
        btn_edit_time = findViewById(R.id.btn_edit_time);

        btn_contacts = findViewById(R.id.contacts);

        btn_edit_date.setOnClickListener(this);
        btn_edit_time.setOnClickListener(this);
        btn_contacts.setOnClickListener(this);



        todo = (Todo) getIntent().getSerializableExtra("Todo");

        Date date = new Date(todo.getDueDate());
        mYear = date.getYear();
        mMonth = date.getMonth();
        mDay = date.getDate();
        mHour = date.getHours();
        mMinute = date.getMinutes();

        loadTodo(todo);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodo(todo);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTodoActivity.this);
                builder.setTitle("Are You sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTodo(todo);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btn_edit_date){
            //Get currwnt Date
            final Calendar c = Calendar.getInstance();
            int dpYear = c.get(Calendar.YEAR);
            int dpMonth = c.get(Calendar.MONTH);
            int dpDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    editTextDate.setText(day +"-"+(month+1)+"-"+year);
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                }
            }, dpYear, dpMonth, dpDay);
            datePickerDialog.show();
        }
        else if (view == btn_edit_time){
            //GEt currwnt Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            //Lauch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    editTextTime.setText(hour+" : "+minute);
                    mMinute = minute;
                    mHour = hour;
                }
            }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        else if(view == btn_contacts){
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            intent.putExtra("Todo", todo);

            startActivity(intent);
        }
    }

    private void loadTodo(Todo todo){
        editTextTodo.setText(todo.getName());
        editTextDesc.setText(todo.getDesc());

        Date date = new Date(todo.getDueDate());
        editTextDate.setText(date.getDate()+"."+(date.getMonth()+1)+"."+date.getYear());
        editTextTime.setText(date.getHours()+":"+date.getMinutes());

        checkBoxFinished.setChecked(todo.getFinished());
        checkBoxFavorite.setChecked(todo.getFavorite());

    }

    private void updateTodo(final Todo todo){
        Date date = new Date(mYear, mMonth, mDay, mHour, mMinute);
        final String sTodo = editTextTodo.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final long curDate = date.getTime();

        if (sTodo.isEmpty()){
            editTextTodo.setError("Todo Required");
            editTextTodo.requestFocus();
            return;
        }

        if (sDesc.isEmpty()){
            editTextDesc.setError("Desc Required");
            editTextDesc.requestFocus();
            return;
        }

        class UpdateTodo extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                todo.setName(sTodo);
                todo.setDesc(sDesc);
                todo.setFinished(checkBoxFinished.isChecked());
                todo.setFavorite(checkBoxFavorite.isChecked());
                todo.setDueDate(curDate);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().update(todo);
                if (ResteasyTodoCRUDAccessor.serverAvailable) {
                    ResteasyTodoCRUDAccessor remoteDBAccessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/");
                    remoteDBAccessor.updateTodo(todo);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTodoActivity.this, TodoListActivity.class));
            }
        }
        UpdateTodo ut = new UpdateTodo();
        ut.execute();
    }

    private void deleteTodo(final Todo todo){
        class DeleteTodo extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().delete(todo);
                if (ResteasyTodoCRUDAccessor.serverAvailable) {
                    ResteasyTodoCRUDAccessor remoteDBAccessor = new ResteasyTodoCRUDAccessor("http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/");
                    remoteDBAccessor.deleteTodo(todo.getId());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTodoActivity.this, TodoListActivity.class));
            }
        }
        DeleteTodo dt = new DeleteTodo();
        dt.execute();
    }



}
