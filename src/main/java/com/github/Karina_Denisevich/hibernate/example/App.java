package com.github.Karina_Denisevich.hibernate.example;

import com.github.Karina_Denisevich.hibernate.example.group.Genre;
import com.github.Karina_Denisevich.hibernate.example.util.HibernateUtil;
import org.hibernate.classic.Session;

import java.util.List;

public class App {

    public static void main(String[] args) {

        System.out.println("Hibernate many to many (XML Mapping)");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        List<Genre> list = session.createCriteria(Genre.class).list();

        System.out.println("First genre by id:");
        System.out.println(list.get(0).getGenreName());

        session.getTransaction().commit();

        HibernateUtil.shutdown();
    }
}
