package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.textViewFinishBy.setText(t.getFinishBy());

        if (t.getFinished())
            holder.textViewStatus.setText("Completed");
        else
            holder.textViewStatus.setText("Not Completed");
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTodo, textViewDesc, textViewFinishBy;

        public TodoViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTodo = itemView.findViewById(R.id.textViewTodo);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewFinishBy = itemView.findViewById(R.id.textViewFinishBy);


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