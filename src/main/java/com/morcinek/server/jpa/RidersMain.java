package com.morcinek.server.jpa;

import com.morcinek.server.domain.Rider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class RidersMain {

    private static final String PERSISTENCE_UNIT_NAME = "riders";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {

        //Create entity manager, this step will connect to database, please check
        //JDBC driver on classpath, jdbc URL, jdbc driver name on persistence.xml
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        boolean open = em.isOpen();
        System.out.println(open);

        //end

        // Query to existing data
        Query q = em.createQuery("select rdr from Rider rdr");
        List<Rider> riderList = q.getResultList();

        //loping trough riderList and print out rider
        for (Rider rider : riderList) {
            System.out.println(rider);
        }

        //Print number of rider
        System.out.println("Size befor insert: " + riderList.size());

        //start transaction with method begin()
        em.getTransaction().begin();

        //create around 10 rider with dummy data
        for (int i = 0; i < 10; i++) {
            Rider rider = new Rider();
            rider.setName("Rider-" + i);
            rider.setPhone("99008-" + i);
            rider.setDistance(i+100*i);

            //insert into database
            em.persist(rider);
        }

        //commit transaction commit();
        em.getTransaction().commit();


        riderList = q.getResultList();
        System.out.println("Size after insert: " + riderList.size());


        em.close();

    }

}//http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC
