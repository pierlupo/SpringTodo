package com.example.SpringTodo.service;

import com.example.SpringTodo.entity.Todo;

import java.util.List;

public interface ITodoService {

    boolean create(Todo t);

    boolean update(Todo t);

    boolean delete(Todo t);

    Todo findById(Integer id);

    List<Todo> findAll();



}
