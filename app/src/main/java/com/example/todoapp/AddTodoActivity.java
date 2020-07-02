package com.example.todoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTodoActivity extends AppCompatActivity {

    private EditText editTextTodo, editTextDesc, editTextFinishBy;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        editTextTodo = findViewById(R.id.editTextTodo);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });

    }

    public void saveTodo(){
        final String sTask = editTextTodo.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();

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

        if (sFinishBy.isEmpty()){
            editTextFinishBy.setError("Finish Person Required");
            editTextFinishBy.requestFocus();
            return;
        }

        class SaveTodo extends AsyncTask<Void, Void, Void> {

            protected Void doInBackground(Void... voids){
                //Create a new Todo
                Todo todo = new Todo();
                todo.setName(sTask);
                todo.setDesc(sDesc);
                todo.setFinishBy(sFinishBy);
                todo.setFinished(false);

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
