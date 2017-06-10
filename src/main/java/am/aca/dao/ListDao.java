package am.aca.dao;

import am.aca.entity.Film;

import java.util.ArrayList;

/**
 * Created by karin on 5/31/2017
 */
public interface ListDao {
    boolean addToWatched(Film film, boolean isPublic, int User_ID) ;
    boolean addToWished(Film film, boolean isPublic, int User_ID) ;
    boolean removeFromWatched(Film film, int User_ID) ;
    boolean removeFromWished(Film film, int User_ID) ;
    ArrayList<Film> showOwnWatched(int User_ID) ;
    ArrayList<Film> showOwnWished(int User_ID) ;
    ArrayList<Film> showOthersWatched(int User_ID) ;
    ArrayList<Film> showOthersWished(int User_ID) ;
}
