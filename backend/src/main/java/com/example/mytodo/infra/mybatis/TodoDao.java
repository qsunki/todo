package com.example.mytodo.infra.mybatis;

import com.example.mytodo.web.TodoListRetrieveReq;
import com.example.mytodo.web.TodoView;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
@MapperScan("com.example.mytodo.infra.mybatis")
public interface TodoDao {
    List<TodoView> findAll(TodoListRetrieveReq todoListRetrieveReq, Long userId);
}
