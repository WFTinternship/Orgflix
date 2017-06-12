package am.aca.dao;

import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.dao.impljdbc.ListDaoJdbc;
import am.aca.dao.impljdbc.UserDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.entity.User;

import java.util.ArrayList;

/**
 * Created by David on 6/4/2017.
 */
public class Test {
    private UserDao userDao = new UserDaoJdbc();

    public static void main(String[] args) {
//        Test test = new Test();
//        User user = new User("gago","Gagik Petrosyan","gagik6@gmail.com","pass");
//        test.userDao.addUser(user);
//        System.out.println(1);
//        user.setUserName("Gago");
//        test.userDao.editUser(user);
//        System.out.println(2);
//        User otheruser = test.userDao.getUser(user.getId());
//        System.out.println(3);
//        otheruser.setUserName("Other name");
//        test.userDao.editUser(otheruser);
//        System.out.println(4);
//        User anotherUser = test.userDao.getUser(user.getEmail());
//        System.out.println(5);
//        anotherUser.setPass("44444");
//        test.userDao.editUser(anotherUser);
//        System.out.println(6);

//        Film film = new Film();
//        Cast director;
//        FilmDao filmDao = new FilmDaoJdbc();
//
//        director = new CastDaoJdbc().addCast("Matt Ross", false);
//        film.setTitle("Captain Fantastic");
//        film.addCast(director);
//        film.addGeners(Genre.DRAMA);
//        filmDao.addFilm(film);
//        System.out.println(filmDao.getFilmById(film.getId()).toString());

        ListDao listDao;
        Film film;
        User user;
        ArrayList<Cast> dirs = new ArrayList<>();

        dirs.add(new CastDaoJdbc().addCast("Brian De Palma", false));

        //setup Film and Film DAO
        film = new Film();
        film.setTitle("Scarface");
        film.addGeners(Genre.ACTION);
        film.setProdYear(1983);
        film.setRate_5star(1);
        film.setCasts(dirs);

        FilmDao filmDao = new FilmDaoJdbc();

        filmDao.addFilm(film);

        //setup User and User DAO
        user = new User("MrSmith","John Smith","JhonSmith@gmail.com","pass");

        UserDao userDao =  new UserDaoJdbc();
        userDao.addUser(user);

        //setup List DAO
        listDao = new ListDaoJdbc();

        listDao.addToWatched(film, true, user.getId());
        System.out.println(listDao.showOwnWatched(user.getId()));

    }
}
