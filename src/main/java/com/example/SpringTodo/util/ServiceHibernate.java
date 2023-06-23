package com.example.SpringTodo.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class ServiceHibernate {

    private Session session;

    private ServiceHibernate(){
        try{
            session = HibernateUtil.getSessionFactory().openSession();
        }catch(HibernateException e){
            throw new RuntimeException();
        }
    }

    public Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
}
