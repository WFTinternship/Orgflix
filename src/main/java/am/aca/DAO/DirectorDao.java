package am.aca.DAO;

import am.aca.main.User;

import java.sql.SQLException;

/**
 * Created by David on 5/27/2017.
 */
public interface DirectorDao {
    int addDirector(String director, boolean hasOscar) throws SQLException;
    String listDirectors()  throws SQLException;
    boolean editDirector(int directorId) throws SQLException;
    boolean removeDirector(int directorId) throws SQLException;
}
