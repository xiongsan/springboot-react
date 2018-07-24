package com.fable.demo.bussiness.service.todoService;


import com.fable.demo.bussiness.mapper.TodoListMapper;
import com.fable.demo.common.pojo.TodoList;
import com.fable.enclosure.bussiness.interfaces.BaseRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.service.impl.BaseServiceImpl;
import com.fable.enclosure.bussiness.util.ResultKit;
import com.fable.enclosure.bussiness.util.Tool;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Wanghairui on 2017/6/9.
 */
@Service
public class TodoListServiceIml extends BaseServiceImpl implements ITodoListService {

    @Autowired
    private TodoListMapper mapper;

    @Autowired
    private CacheManager cacheManager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public BaseResponse todoList(BaseRequest<TodoList> request) {
//        Cache cache=cacheManager.getCache("test");
//        if(cache.get("todolist")!=null){
//            return ResultKit.serviceResponse((List<TodoList>) cache.get("todolist").getValue());
//        }
        List<TodoList> list = mapper.getTodoList(request.getParam());
//        Element element = new Element("todolist",list);
//        cache.put(element);
        return ResultKit.serviceResponse(list);
    }

    @Override
    public BaseResponse modifyTodoList(BaseRequest<TodoList> request) {
         mapper.updateTodoList(request.getParam());
        return ResultKit.success();
    }

    public BaseResponse addTodo(BaseRequest<TodoList> request) {
        Tool.startTransaction();
        try{
            TodoList todo = request.getParam();
            todo.setId(Tool.newGuid());
            todo.setChecked("1");
            todo.setSex("男");
            mapper.insertTodo(todo);
            Tool.endTransaction();
            return ResultKit.success();
        }catch(Exception e){
            Tool.rollBack();
            e.printStackTrace();
            return ResultKit.fail(e.getMessage());
        }
    }

    @Override
    public BaseResponse cancelTodo(BaseRequest<TodoList> request) {
        mapper.deleteTodo(request.getParam());
        return ResultKit.success();
    }

    public BaseResponse getPageData(BaseRequest<TodoList> param){
        Page<TodoList> result = PageHelper.startPage(param.getPageNo(),param.getPageSize());
        mapper.getTodoList(param.getParam());
        return ResultKit.wrap(result);
    }
}
