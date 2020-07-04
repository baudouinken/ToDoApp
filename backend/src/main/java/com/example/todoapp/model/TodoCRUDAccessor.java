package com.example.todoapp.model;

import javax.ws.rs.*;
import java.util.List;


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
