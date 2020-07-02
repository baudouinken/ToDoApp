package com.example.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateTodoActivity extends AppCompatActivity {

    private EditText editTextTodo, editTextDesc, editTextFinishBy;
    private CheckBox checkBoxFinished;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        editTextTodo = findViewById(R.id.editTextTodo);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        checkBoxFinished = findViewById(R.id.checkBoxFinished);

        final Todo todo = (Todo) getIntent().getSerializableExtra("Todo");

        loadTodo(todo);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
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

    private void loadTodo(Todo todo){
        editTextTodo.setText(todo.getName());
        editTextDesc.setText(todo.getDesc());
        editTextFinishBy.setText(todo.getFinishBy());
        checkBoxFinished.setChecked(todo.getFinished());
    }

    private void updateTodo(final Todo todo){
        final String sTodo = editTextTodo.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();

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

        if (sFinishBy.isEmpty()){
            editTextFinishBy.setError("Finish Date Required");
            editTextFinishBy.requestFocus();
            return;
        }

        class UpdateTodo extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                todo.setName(sTodo);
                todo.setDesc(sDesc);
                todo.setFinishBy(sFinishBy);
                todo.setFinished(checkBoxFinished.isChecked());
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().todoDao().update(todo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTodoActivity.this, MainActivity.class));
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
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTodoActivity.this, MainActivity.class));
            }
        }
        DeleteTodo dt = new DeleteTodo();
        dt.execute();
    }



}
