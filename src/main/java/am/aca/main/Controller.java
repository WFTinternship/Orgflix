package am.aca.main;

import am.aca.DAO.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/27/2017.
 */
public class Controller {
    private Dao dao = new Dao("root","");
    private int userID = -1;
    private List<Film> watchList = new ArrayList<>();
    private List<Film> wishList = new ArrayList<>();

    public Controller() {    }

    public int getUserID() {
        return userID;
    }
    public Dao getDao() {return dao; }
    public List<Film> getWatchList() {
        return watchList;
    }
    public List<Film> getWishList() {
        return wishList;
    }
}
