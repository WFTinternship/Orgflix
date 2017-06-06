package am.aca.service;

import am.aca.entity.Film;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by karin on 5/31/2017
 */
public interface ListService {
    boolean addToWatched(Film film, boolean isPublic, int User_ID) throws SQLException;
    boolean addToWished(Film film, boolean isPublic, int User_ID) throws SQLException;
    boolean removeFromWatched(Film film, int User_ID) throws SQLException;
    boolean removeFromWished(Film film, int User_ID) throws SQLException;
    List<Film> showOwnWatched(int User_ID) throws SQLException;
    List<Film> showOwnWished(int User_ID) throws SQLException;
    List<Film> showOthersWatched(int User_ID) throws SQLException;
    List<Film> showOthersWished(int User_ID) throws SQLException;
}
