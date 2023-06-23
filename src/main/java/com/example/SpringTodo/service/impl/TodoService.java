package com.example.SpringTodo.service.impl;

import com.example.SpringTodo.entity.Todo;
import com.example.SpringTodo.service.ITodoService;
import com.example.SpringTodo.util.ServiceHibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService implements ITodoService {

    @Autowired
    private ServiceHibernate serviceHibernate;

    private Session session;

    public TodoService(ServiceHibernate serviceHibernate){

        this.serviceHibernate =serviceHibernate;
        session = this.serviceHibernate.getSession();
    }
    @Override
    public boolean create(Todo t) {
        session.beginTransaction();
        session.save(t);
        session.getTransaction().commit();
        return true;    }

    @Override
    public boolean update(Todo t) {
        session.beginTransaction();
        session.update(t);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Todo t) {
        session.beginTransaction();
        session.delete(t);
        session.getTransaction().commit();
        return true;    }

    @Override
    public Todo findById(Integer id) {
        Todo todo = null;
        todo = (Todo) session.get(Todo.class, id);
        return todo;    }

    @Override
    public List<Todo> findAll() {
        Query<Todo> todoQuery = session.createQuery("from Todo");
        return todoQuery.list();    }
}
