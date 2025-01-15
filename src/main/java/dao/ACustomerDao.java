package dao;

import datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import simu.model.customerTypes.AbstractCustomer;

import java.util.List;

/**
 * The ACustomerDao class provides data access operations for the AbstractCustomer entity.
 * It handles the persistence, retrieval, updating, and deletion of customer data in the database.
 */
public class ACustomerDao implements IDao<AbstractCustomer> {

    public void persist(AbstractCustomer abstractCustomer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(abstractCustomer);
        em.getTransaction().commit();
    }

    public AbstractCustomer find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.find(AbstractCustomer.class, id);
    }

    public List<AbstractCustomer> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.createQuery("SELECT a FROM AbstractCustomer a", AbstractCustomer.class).getResultList();
    }

    public void update(AbstractCustomer abstractCustomer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(abstractCustomer);
        em.getTransaction().commit();
    }

    public void delete(AbstractCustomer abstractCustomer) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(abstractCustomer);
        em.getTransaction().commit();
    }

    public static void truncateTables() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        Query query1 = em.createNativeQuery("TRUNCATE TABLE customers");
        query1.executeUpdate();
        Query query2 = em.createNativeQuery("TRUNCATE TABLE orders");
        query2.executeUpdate();
        Query query3 = em.createNativeQuery("TRUNCATE TABLE pizzas");
        query3.executeUpdate();
        em.getTransaction().commit();
        em.clear();
        em.getEntityManagerFactory().getCache().evictAll();
    }
}
