package com.example.mytodo.todo.domain;

import org.springframework.data.repository.ListCrudRepository;

public interface TodoRepository extends ListCrudRepository<Todo, Long> {}
