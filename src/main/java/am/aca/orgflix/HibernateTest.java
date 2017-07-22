package am.aca.orgflix;

import am.aca.orgflix.entity.sverxnoviypackage.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by karin on 7/22/2017.
 */
public class HibernateTest {

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        Session session = sessionFactory.openSession();

        List<Film> casts = session.createQuery("from films").list();

        System.out.println("asdfasdfa");
    }

}
