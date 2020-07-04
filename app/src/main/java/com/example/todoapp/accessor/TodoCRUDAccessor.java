package com.example.todoapp.accessor;

import com.example.todoapp.model.Todo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoCRUDAccessor {

    @GET
    public List<Todo> getTodoList();

    @POST
    public Todo createTodo(Todo todo);

    @POST
    @Path("/createtodolist")
    boolean createTodoList(List<Todo> todoList);

    @DELETE
    @Path("/{todoId}")
    public boolean deleteTodo(@PathParam("todoId") long todoId);

    @PUT
    public Todo updateTodo(Todo todo);
}
