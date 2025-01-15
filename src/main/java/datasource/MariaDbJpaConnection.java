package datasource;

import jakarta.persistence.*;


/**
 * The MariaDbJpaConnection class is a singleton that manages the connection
 * to a MariaDB database using JPA (Java Persistence API). It provides an
 * EntityManager instance for interacting with the database.
 */
public class MariaDbJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Returns the singleton instance of the EntityManager.
     * This method initializes the EntityManagerFactory and EntityManager if they are not already created.
     *
     * @return The singleton EntityManager instance.
     */

    public static EntityManager getInstance() {

        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("PizzasaurusMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}