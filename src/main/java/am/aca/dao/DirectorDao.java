package am.aca.dao;

import am.aca.entity.Director;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 5/27/2017.
 */
public interface DirectorDao {
    Director addDirector(String director, boolean hasOscar) throws SQLException;
    List<Director> listDirectors()  throws SQLException;
    boolean editDirector(int directorId) throws SQLException;
}
