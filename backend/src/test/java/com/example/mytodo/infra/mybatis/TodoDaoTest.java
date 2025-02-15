package com.example.mytodo.infra.mybatis;

import static com.example.mytodo.web.TodoListRetrieveReq.*;

import com.example.mytodo.TestcontainersConfiguration;
import com.example.mytodo.property.domain.PropertyType;
import com.example.mytodo.property.domain.SelectProperty;
import com.example.mytodo.web.TodoListRetrieveReq;
import com.example.mytodo.web.TodoView;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Disabled
@MybatisTest
@Import(TestcontainersConfiguration.class)
class TodoDaoTest {

    @Autowired
    TodoDao todoDao;

    @Test
    void findAll() {
        // given
        Long userId = 1L;
        List<Filter> filters = List.of(new Filter("우선순위", PropertyType.SELECT, new SelectProperty("상", null)));
        TodoListRetrieveReq todoListRetrieveReq = new TodoListRetrieveReq(filters);

        // when
        List<TodoView> all = todoDao.findAll(todoListRetrieveReq, userId);

        // then
        for (TodoView todoView : all) {
            System.out.println(todoView);
        }
    }
}
