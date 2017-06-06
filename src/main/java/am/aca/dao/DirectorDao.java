package am.aca.dao;

import am.aca.entity.Director;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 5/27/2017.
 */
public interface DirectorDao {
    Director addDirector(String director, boolean hasOscar);
    Director addDirector(String director);
    boolean editDirector(Director director);
    List<Director> listDirectors();
    int[] listFilmsIdByDirector(int directorId);
}
