package am.aca.DAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by David on 5/28/2017.
 */
public class Dao {
    private Connection connection;
    private UserDao userDAO = new UserDaoJdbc(connection);
    private DirectorDao directorDao = new DirectorDaoJdbc(connection);
    private FilmDao filmDao = new FilmDaoJdbc(connection);

    public Dao(String user, String pass) {
        connection = DaoJdbc.getConnection(user, pass);
    }

    public int authentication(String email, String pass){
        int userID = -1;
        try {
            userID = userDAO.authenticate(email,pass).getId();
            System.out.println("Authentication succeeded.");
        } catch (SQLException e) {
            System.out.println("Authentication failed !!!");
            e.printStackTrace();
        }
        return userID;
    }
    public UserDao getUserDAO() {
        return userDAO;
    }
    public DirectorDao getDirectorDao(){ return directorDao; }

}
