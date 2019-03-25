package com.fable.demo.bussiness.mapper;

import com.fable.demo.common.pojo.TodoList;

import java.util.List;

/**
 * Created by Wanghairui on 2017/5/22.
 */
public interface TodoListMapper {

    List<TodoList> getTodoList(TodoList todo);

    void updateTodoList(TodoList param);

    void insertTodo(TodoList todo);

    void deleteTodo(TodoList todo);
}
