package com.chen.hibernate;

import com.chen.hibernate.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * HibernateCacheTest
 * <p>
 * @Author LeifChen
 * @Date 2019-07-04
 */
public class HibernateCacheTest {

    private static SessionFactory factory;

    @Before
    public void init() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * 不同 Session 不共享一级缓存
     */
    @Test
    public void withoutLevel1Cache() {
        Session session = factory.openSession();

        Employee employee = session.get(Employee.class, 1);
        System.out.println(employee);

        session = factory.openSession();

        session.get(Employee.class, 1);
        System.out.println(employee);

        session.close();
    }

    /**
     * 使用 Session 一级缓存
     */
    @Test
    public void withLevel1Cache() {
        Session session = factory.openSession();

        Employee employee = session.get(Employee.class, 1);
        System.out.println(employee);

        session.get(Employee.class, 1);
        System.out.println(employee);

        session.close();
    }

    /**
     * 使用 session.evict(Object) 方法清除缓存
     */
    @Test
    public void evictLevel1Cache() {
        Session session = factory.openSession();

        Employee employee = session.get(Employee.class, 1);
        System.out.println(employee);

        session.evict(employee);

        session.get(Employee.class, 1);
        System.out.println(employee);

        session.close();
    }

    /**
     * 使用 session.clear() 方法清空缓存
     */
    @Test
    public void clearLevel1Cache() {
        Session session = factory.openSession();

        Employee employee = session.get(Employee.class, 1);
        System.out.println(employee);

        session.clear();

        session.get(Employee.class, 1);
        System.out.println(employee);

        session.close();
    }

    /**
     * query.list() 不会使用缓存
     */
    @Test
    public void queryList() {
        Session session = factory.openSession();

        Query query = session.createQuery("from Employee");
        List<Employee> list = query.list();
        for (Employee e : list) {
            System.out.println(e);
        }

        list = query.list();
        for (Employee e : list) {
            System.out.println(e);
        }

        session.close();
    }

    /**
     * query.iterate() 会先根据 id 查找缓存
     */
    @Test
    public void queryIterate() {
        Session session = factory.openSession();

        Query query = session.createQuery("from Employee");
        Iterator iterator = query.iterate();
        while(iterator.hasNext()){
            Employee e = (Employee) iterator.next();
            System.out.println(e);
        }

        session.close();
    }
}
