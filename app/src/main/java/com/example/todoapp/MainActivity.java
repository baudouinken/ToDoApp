package com.example.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private boolean isTodoFalse = true;
    private Button button_list_date, button_list_favorite;

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

        //Sortierung Nach Completed
        button_list_date = findViewById(R.id.button_list_com);
        button_list_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTodoFalse = false;
                button_list_date.setBackgroundColor(Color.LTGRAY);
                button_list_favorite.setBackgroundColor(Color.BLUE);
                getTodoDate();
            }
        });

        //Sortierung Nach Wichtigkeit
        button_list_favorite = findViewById(R.id.button_list_favorite);
        button_list_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTodoFalse = false;
                button_list_favorite.setBackgroundColor(Color.LTGRAY);
                button_list_date.setBackgroundColor(Color.BLUE);
                getTodoFavorite();
            }
        });

        if(isTodoFalse){
            getTodo();
        }

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

    public void getTodoDate(){
        class GetTodo extends AsyncTask<Void, Void, List<Todo>>{
            @Override
            protected List<Todo> doInBackground(Void... voids) {
                List<Todo> todoList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .todoDao()
                        .getAllWithDate();
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

    public void getTodoFavorite(){
        class GetTodo1 extends AsyncTask<Void, Void, List<Todo>>{
            @Override
            protected List<Todo> doInBackground(Void... voids) {
                List<Todo> todoList1 = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .todoDao()
                        .getAllWithFavorite();
                return todoList1;
            }

            @Override
            protected void onPostExecute(List<Todo> taskList) {
                super.onPostExecute(taskList);
                TodoAdapter adapter = new TodoAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(adapter);
            }
        }
        GetTodo1 gt1 = new GetTodo1();
        gt1.execute();
    }


}
