package com.example.todoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.model.DatabaseClient;
import com.example.todoapp.model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity  extends AppCompatActivity {

    private FloatingActionButton buttonAddTodo;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle saveInstaceState){
        super.onCreate(saveInstaceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_todos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTodo = findViewById(R.id.floating_button_add);
        buttonAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivity(intent);
            }
        });
        getTodo();
    }

    private void getTodo(){
        class GetTodo extends AsyncTask<Void, Void, List<Todo>>{
            @Override
            protected List<Todo> doInBackground(Void... voids) {
                List<Todo> todoList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .todoDao()
                        .getAll();
                return todoList;
            }

            @Override
            protected void onPostExecute(List<Todo> taskList) {
                super.onPostExecute(taskList);
                TodoAdapter adapter = new TodoAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(adapter);
            }
        }
        GetTodo gt = new GetTodo();
        gt.execute();
    }


}
