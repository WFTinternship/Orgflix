package am.aca.dao;

import am.aca.entity.Film;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by karin on 5/31/2017.
 */
public interface ListDao {
    boolean addToWatched(Film film, boolean isPublic, int User_ID) throws SQLException;
    boolean addToWished(Film film, boolean isPublic, int User_ID) throws SQLException;
    boolean removeFromWatched(Film film, int User_ID) throws SQLException;
    boolean removeFromWished(Film film, int User_ID) throws SQLException;
    ArrayList<Film> showOwnWatched(int User_ID) throws SQLException;
    ArrayList<Film> showOwnWished(int User_ID) throws SQLException;
    ArrayList<Film> showOthersWatched(int User_ID) throws SQLException;
    ArrayList<Film> showOthersWished(int User_ID) throws SQLException;
}
