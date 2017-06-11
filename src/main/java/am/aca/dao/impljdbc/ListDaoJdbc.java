package am.aca.dao.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.ListDao;
import am.aca.dao.UserDao;
import am.aca.entity.Film;
import am.aca.util.DbManager;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by karine on 6/3/2017
 */
public class ListDaoJdbc implements ListDao {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger( UserDao.class.getName() );


    
    @Override
    public boolean addToWatched(Film film, boolean isPublic, int User_ID) {
        boolean result;
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection(DbManager.ConnectionType.POOL);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if given film and user are related
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement checkStatement;
        try {
            checkStatement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, checkStatement);
        ResultSet rs;
        try {
            rs = checkStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        int checkSetLength = hasNext(rs);

        //check whether or not the given film is marked as planned

        //case: planned => UPDATE
        if (checkSetLength == 1) {
            final String updateQuery = "UPDATE lists SET Is_watched = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement updateStatement;
            try {
                updateStatement = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, updateStatement);
            try {
                result = updateStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, checkStatement, updateStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not planned to watch => INSERT
        else{
            final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement insertStatement;
            try {
                insertStatement = connection.prepareStatement(insertQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }

            try {
                set(User_ID, film, insertStatement);
                insertStatement.setBoolean(3, isPublic);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                result = insertStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, checkStatement, insertStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }
    }

    @Override
    public boolean addToWished(Film film, boolean isPublic, int User_ID)  {
        boolean result;
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if given film and user are related
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement checkStatement;
        try {
            checkStatement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, checkStatement);
        ResultSet rs;
        try {
            rs = checkStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        int resultSetLength;

        try {
            rs.next();
            resultSetLength = rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if the film is watched

        //case: film marked as watched
        if (resultSetLength == 1) {
            final String updateQuery = "UPDATE lists SET Is_wished = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement updateStatement;
            try {
                updateStatement = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }

            set(User_ID, film, updateStatement);

            try {
                result = updateStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, checkStatement, updateStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not marked as watched
        else{
            final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement insertStatement;
            try {
                insertStatement = connection.prepareStatement(insertQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, insertStatement);
            try {
                insertStatement.setBoolean(3, isPublic);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                result = insertStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, checkStatement, insertStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }
    }

    @Override
    public boolean removeFromWatched(Film film, int User_ID) {
        boolean result;
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if the film is marked as watched

        final String watchedCheckQuery = "SELECT * FROM lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE";
        PreparedStatement watchedCheckStatement;
        try {
            watchedCheckStatement = connection.prepareStatement(watchedCheckQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, watchedCheckStatement);

        ResultSet watchedSet;
        try {
            watchedSet = watchedCheckStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

//        int watchedSetSize;

        if(!checkForResultSet(watchedSet, connection, watchedCheckStatement)){
            return false;
        }

        //case: marked as watched

        //check if film is marked as planned
        final String plannedCheckQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";

        PreparedStatement plannedCheckStatement;
        try {
            plannedCheckStatement = connection.prepareStatement(plannedCheckQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, plannedCheckStatement);
        ResultSet plannedSet;
        try {
            plannedSet = plannedCheckStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        boolean planned = false;
        try {
            if (plannedSet.next()) {
                planned = plannedSet.getBoolean("Is_wished");
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }



        //case: film marked as planned => UPDATE
        if (planned) {
            final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement updateStatement;
            try {
                updateStatement = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, updateStatement);
            try {
                result = updateStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, plannedSet, plannedCheckStatement, updateStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not marked as planned
        else {
            try {
                return delete(connection, User_ID, film);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
    }

    private boolean checkForResultSet(ResultSet resultSet, Connection connection, PreparedStatement watchedCheckStatement) {

        try {
            //case: marked as watched => terminated
            if (!resultSet.next()) {
                closeAll(connection, watchedCheckStatement);
                return false;
            }
//            watchedSetSize = watchedSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        return true;
    }

    @Override
    public boolean removeFromWished(Film film, int User_ID)  {
        boolean result;
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if the film is marked as planned

        final String plannedCheckQuery = "SELECT * FROM lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE";
        PreparedStatement plannedCheckStatement;
        try {
            plannedCheckStatement = connection.prepareStatement(plannedCheckQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, plannedCheckStatement);

        ResultSet plannedSet;
        try {
            plannedSet = plannedCheckStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        if(!checkForResultSet(plannedSet, connection, plannedCheckStatement)){
            return false;
        }

        //case: marked as planned

        //check if film is marked as watched

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE ";

        PreparedStatement watchedCheckStatement;
        try {
            watchedCheckStatement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }


        set(User_ID, film, watchedCheckStatement);
        ResultSet watchedSet;
        try {
            watchedSet = watchedCheckStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        boolean watched = false;

        try {
            if(watchedSet.next())
                watched = watchedSet.getBoolean("Is_watched");
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }


        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (watched) {
            final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement updateStatement;
            try {
                updateStatement = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, updateStatement);
            try {
                result = updateStatement.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, watchedSet, plannedCheckStatement, updateStatement, watchedCheckStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not
        else {
            try {
                return delete(connection, User_ID, film);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
    }

    @Override
    public ArrayList<Film> showOwnWatched(int User_ID) {
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        ArrayList<Film> watched = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE ";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            statement.setInt(1, User_ID);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        add(resultSet, watched);
        try {
            closeAll(connection, resultSet, statement);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return watched;
    }

    @Override
    public ArrayList<Film> showOwnWished(int User_ID)  {
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        ArrayList<Film> wished = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            statement.setInt(1, User_ID);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        add(resultSet, wished);
        try {
            closeAll(connection, resultSet, statement);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return wished;
    }

    @Override
    public ArrayList<Film> showOthersWatched(int User_ID)  {
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        ArrayList<Film> watched = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE AND Is_public = TRUE ";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            statement.setInt(1, User_ID);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            assert resultSet != null;
            while (resultSet.next()){
                watched.add(new Film());
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            closeAll(connection, resultSet, statement);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return watched;
    }

    @Override
    public ArrayList<Film> showOthersWished(int User_ID)  {
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        ArrayList<Film> wished = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE AND Is_public = TRUE ";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            statement.setInt(1, User_ID);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {
            while (resultSet != null && resultSet.next()){
                wished.add(new Film());
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        try {

            closeAll(connection, resultSet, statement);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return wished;
    }

    private boolean delete(Connection connection, int User_ID, Film film) throws SQLException {
        final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
        PreparedStatement statement1 = connection.prepareStatement(deleteQuery);
        statement1.setInt(1, User_ID);
        statement1.setInt(2, film.getId());
        return statement1.execute();
    }

    private void set(int User_ID, Film film, PreparedStatement statement){
        try {
            statement.setInt(1, User_ID);
            statement.setInt(2, film.getId());
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    private int hasNext(ResultSet rs){
        try {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    private void add(ResultSet resultSet, ArrayList<Film> list) {
        try {
            while (resultSet.next()){
                list.add(new Film());
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
    }

    private void closeAll(Connection connection, PreparedStatement... statements) throws SQLException {
        for (PreparedStatement statement: statements) {
            statement.close();
        }
        connection.close();
    }

    private void closeAll(Connection connection, ResultSet resultSet, PreparedStatement... statements) throws SQLException {
        resultSet.close();
        closeAll(connection, statements);
    }

}
