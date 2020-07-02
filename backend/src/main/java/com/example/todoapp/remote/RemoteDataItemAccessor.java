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

	/**
	 * we assign the ids here
	 */
	private static int idCount = 0;
	

	@Override
	public List<Todo> getTodoList() {
		logger.info("geTodoList(): " + todoList);
		return todoList;
	}

	@Override
	public Todo createTodo(Todo todo) {
		logger.info("createTodo(): " + todo);
		todo.setId(idCount++);
		todoList.add(todo);
		return todo;
	}

	@Override
	public boolean createTodoList(List<Todo> todoList) {
		return false;
	}

	@Override
	public boolean deleteTodo(int todoId) {
		logger.info("deleteTodo(): " + todoId);
		boolean removed = todoList.remove(new Todo() {
			private static final long serialVersionUID = 71193783355593985L;

			@Override
			public int getId() {
				return todoId;
			}
		});
		return removed;
	}

	@Override
	public Todo updateTodo(Todo todo) {
			logger.info("updateTodo(): " + todo);
			return todoList.get(todoList.indexOf(todo)).updateFrom(todo);
	}
}
