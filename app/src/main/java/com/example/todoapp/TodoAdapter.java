package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.model.Todo;

import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private Context mCtx;
    private List<Todo> todoList;

    public TodoAdapter(Context mCtx, List<Todo> todoList) {
        this.mCtx = mCtx;
        this.todoList = todoList;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo t = todoList.get(position);
        holder.textViewTodo.setText(t.getName());
        holder.textViewDesc.setText(t.getDesc());
        Date date = new Date(t.getDueDate());
        holder.textViewDueDate.setText(date.getDate()+"."+date.getMonth()+"."+date.getYear());

        if (t.getFavorite())
            holder.textViewFavorite.setText("Favorite");
        else
            holder.textViewFavorite.setText("Not favorite");

        if (t.getFinished()) {
            holder.textViewStatus.setText("Completed");
            holder.textViewStatus.setBackgroundColor(1);
        }else {
            holder.textViewStatus.setText("Not Completed");
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTodo, textViewDesc, textViewDueDate, textViewFavorite;

        public TodoViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTodo = itemView.findViewById(R.id.textViewTodo);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewFavorite = itemView.findViewById(R.id.textViewFavorite);


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
}