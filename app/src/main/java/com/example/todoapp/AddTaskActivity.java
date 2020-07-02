package com.example.todoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc, editTextFinishBy;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

    }

    public void saveTask(){
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();

        if (sTask.isEmpty()){
            editTextTask.setError("Task Required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()){
            editTextDesc.setError("Desc Required");
            editTextDesc.requestFocus();
            return;
        }

        if (sFinishBy.isEmpty()){
            editTextFinishBy.setError("Finish Person Required");
            editTextFinishBy.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            protected Void doInBackground(Void... voids){
                //Create a new Task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);
                task.setFinished(false);

                //Adding to db
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(task);
                return null;
            }

            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();

    }

}
