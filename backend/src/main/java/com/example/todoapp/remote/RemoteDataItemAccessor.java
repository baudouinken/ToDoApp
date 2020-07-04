package com.example.todoapp.remote;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.TodoCRUDAccessor;
import org.apache.log4j.Logger;

public class RemoteDataItemAccessor implements TodoCRUDAccessor {

	protected static Logger logger = Logger
			.getLogger(RemoteDataItemAccessor.class);

	private static List<Todo> todoList = new ArrayList<Todo>();


	@Override
	public List<Todo> getTodoList() {
		logger.info("geTodoList(): " + todoList);
		return todoList;
	}

	@Override
	public Todo createTodo(Todo todo) {
		logger.info("createTodo(): " + todo);
		todoList.add(todo);
		return todo;
	}

	@Override
	public boolean createTodoList(List<Todo> todoList) {
		this.todoList.removeAll(this.todoList);
		this.todoList.addAll(todoList);
		logger.info("createTodoList(): " + todoList);
		return true;
	}

	@Override
	public boolean deleteTodo(final long todoId) {
		logger.info("deleteTodo(): " + todoId);
		for(Todo todo : todoList) {
			if(todo.getId() == todoId) {
				todoList.remove(todo);
				return true;
			}
		}
		return false;
	}

	@Override
	public Todo updateTodo(Todo todo) {
		logger.info("updateTodo(): " + todo);
		for(Todo t : todoList) {
			if(t.getId() == todo.getId()) {
				t.updateFrom(todo);
				return t;
			}
		}
		return null;
	}
}
