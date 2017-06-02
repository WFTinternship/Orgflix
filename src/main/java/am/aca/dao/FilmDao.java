package am.aca.dao;

import am.aca.entity.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 5/28/2017.
 */
public interface FilmDao {
    boolean addFilm(Film film, List<Director> directors, Connection connection) throws SQLException;
    boolean editFilm(Film film, List<Director> directors, Connection connection) throws SQLException;
    Film getFilmById(int id, Connection connection) throws SQLException;
    boolean rateFilm(int filmId, int starType, Connection connection) throws SQLException;
    boolean addGenreToFilm(Genre genre, Film film, Connection connection) throws SQLException;
}
