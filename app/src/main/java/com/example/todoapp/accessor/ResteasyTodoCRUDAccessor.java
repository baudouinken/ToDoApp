package com.example.todoapp.accessor;

import android.util.Log;
import com.example.todoapp.model.Todo;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.List;

public class ResteasyTodoCRUDAccessor implements TodoCRUDAccessor {

	// save server availability, checked in InitActivity
	public static boolean serverAvailable = false;
	
	protected static String logger = ResteasyTodoCRUDAccessor.class.getSimpleName();

	/**
	 * the client via which we access the rest web interface provided by the server
	 */
	private TodoCRUDAccessor restClient;

	public ResteasyTodoCRUDAccessor(String baseUrl) {

		Log.i(logger,"initialising restClient for baseUrl: " + baseUrl);
		
		// create a client for the server-side implementation of the interface
		this.restClient = ProxyFactory.create(TodoCRUDAccessor.class,
				baseUrl,
				new ApacheHttpClient4Executor());
		
		Log.i(logger,"initialised restClient: " + restClient + " of class " + restClient.getClass());
	}


	@Override
	public List<Todo> getTodoList() {
		Log.i(logger, "getTodoList()");
		List<Todo> todoList = restClient.getTodoList();
		Log.i(logger, "readAllItems(): got: " + todoList);
		return todoList;
	}

	@Override
	public Todo createTodo(Todo todo) {
		Log.i(logger, "createTodo(): send: " + todo + " " + todo.getId());
		todo = restClient.createTodo(todo);
		Log.i(logger, "createTodo(): got: " + todo);
		return todo;
	}

	@Override
	public boolean createTodoList(List<Todo> todoList) {
		Log.i(logger, "createTodoList(): send: " + todoList);
		boolean list = restClient.createTodoList(todoList);
		Log.i(logger, "createTodoList(): got: " + list);
		return list;
	}

	@Override
	public boolean deleteTodo(long todoId) {
		Log.i(logger, "deleteTodo(): send: " + todoId);
		boolean deleted = restClient.deleteTodo(todoId);
		Log.i(logger, "deleteTodo(): got: " + deleted);
		return deleted;
	}

	@Override
	public Todo updateTodo(Todo todo) {
		Log.i(logger, "updateTodo(): send: " + todo);
		todo = restClient.updateTodo(todo);
		Log.i(logger, "updateTodo(): got: " + todo);
		return todo;
	}
}
