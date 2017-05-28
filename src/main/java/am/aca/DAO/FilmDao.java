package am.aca.DAO;

import am.aca.main.*;

import java.sql.SQLException;

/**
 * Created by David on 5/28/2017.
 */
public interface FilmDao {
    boolean addFilm(Film film, boolean isEdit) throws SQLException;
    int rateFilm(Film film, int starType, boolean isAdd) throws SQLException;
    boolean addGenre(Genre genre, Film film) throws SQLException;
}
