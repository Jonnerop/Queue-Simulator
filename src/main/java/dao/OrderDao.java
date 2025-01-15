package dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import simu.model.Order;
import simu.model.customerTypes.AbstractCustomer;

import java.util.List;

/**
 * The OrderDao class provides data access operations for the Order entity.
 * It handles the persistence, retrieval, updating, and deletion of order data in the database.
 */
public class OrderDao implements IDao<Order> {

    public void persist(Order order) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
    }

    public Order find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.find(Order.class, id);
    }

    public List<Order> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.createQuery("SELECT a FROM Order a", Order.class).getResultList();
    }

    public void update(Order order) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(order);
        em.getTransaction().commit();
    }

    public void delete(Order order) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(order);
        em.getTransaction().commit();
    }
}
