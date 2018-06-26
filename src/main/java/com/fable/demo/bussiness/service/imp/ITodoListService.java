package com.fable.demo.bussiness.service.imp;


import com.fable.demo.common.pojo.TodoList;
import com.fable.enclosure.bussiness.interfaces.BaseRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;

import java.util.Map;

/**
 * Created by Wanghairui on 2017/6/9.
 */
public interface ITodoListService {
    BaseResponse todoList(BaseRequest<TodoList> request);

    BaseResponse modifyTodoList(BaseRequest<TodoList> request);

    BaseResponse addTodo(BaseRequest<TodoList> todo);

    BaseResponse cancelTodo(BaseRequest<TodoList> request);
}
