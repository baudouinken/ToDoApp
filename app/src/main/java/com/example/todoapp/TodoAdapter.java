package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.model.DatabaseClient;
import com.example.todoapp.model.Todo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private Context mCtx;
    private List<Todo> todoList;
    private boolean isFavorite = false;
    private CardView itemView;

    public TodoAdapter(Context mCtx, List<Todo> todoList) {
        this.mCtx = mCtx;
        this.todoList = todoList;
    }

    public TodoAdapter (){}

    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder holder, int position) {
        final Todo t = todoList.get(position);
        holder.textViewTodo.setText(t.getName());
        holder.textViewDesc.setText(t.getDesc());
        Date date = new Date(t.getDueDate());
        holder.textViewDueDate.setText(date.getDate()+"."+(date.getMonth()+1)+"."+date.getYear());

        final Calendar c = Calendar.getInstance();

        if ( (!t.getFinished() && date.getYear()< c.get(Calendar.YEAR)) || (!t.getFinished() && date.getMonth() < c.get(Calendar.MONTH))
             || (!t.getFinished() && date.getDate() < c.get(Calendar.DAY_OF_MONTH)) )
            holder.itemCard.setCardBackgroundColor(Color.RED);


        if (t.getFavorite())
            holder.textViewFavorite.setText("Favorite");
        else
            holder.textViewFavorite.setText(" ");

        if (t.getFinished()) {
            holder.textViewStatus.setText("Completed");
        }else {
            holder.textViewStatus.setText("Not Completed");
        }

        holder.textViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodo(t, holder);

            }
        });

        holder.textViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodoStatus(t, holder);

            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTodo, textViewDesc, textViewDueDate, textViewFavorite;
        CardView itemCard;
        public TodoViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTodo = itemView.findViewById(R.id.textViewTodo);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewFavorite = itemView.findViewById(R.id.textViewFavorite);
            itemCard = itemView.findViewById(R.id.itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

                Todo todo = todoList.get(getAdapterPosition());

                Intent intent = new Intent(mCtx, UpdateTodoActivity.class);
                intent.putExtra("Todo", todo);

                mCtx.startActivity(intent);


        }
    }


    public void updateTodo(final Todo todo, final TodoViewHolder holder){

        class UpdateTodo extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                if (todo.getFavorite()){
                    holder.textViewFavorite.setText("No Favorite");
                    todo.setFavorite(false);
                } else {
                    holder.textViewFavorite.setText("Favorite");
                    todo.setFavorite(true);
                }



                DatabaseClient.getInstance(mCtx.getApplicationContext()).getAppDatabase().todoDao().update(todo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(mCtx.getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                //finish();

            }
        }
        UpdateTodo ut = new UpdateTodo();
        ut.execute();
    }

    private void updateTodoStatus(final Todo todo, final TodoViewHolder holder){

        class UpdateTodo extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                if (todo.getFinished()){
                    holder.textViewStatus.setText("No Completed");
                    todo.setFinished(false);
                } else {
                    holder.textViewStatus.setText("Completed");
                    todo.setFinished(true);
                }



                DatabaseClient.getInstance(mCtx.getApplicationContext()).getAppDatabase().todoDao().update(todo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(mCtx.getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                //finish();

            }
        }
        UpdateTodo ut = new UpdateTodo();
        ut.execute();
    }


}