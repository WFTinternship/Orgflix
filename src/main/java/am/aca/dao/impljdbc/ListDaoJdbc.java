package am.aca.dao.impljdbc;

import am.aca.dao.ListDao;
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


        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, statement);
        ResultSet rs;
        try {
            rs = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        int resultSetLength = hasNext(rs);

        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_watched = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(finalCheckQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, statement1);
            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, statement, statement1);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not in the wishlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(finalCheckQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }

            try {
                set(User_ID, film, statement1);
                statement1.setBoolean(3, isPublic);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, statement, statement1);
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

        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, statement);
        ResultSet rs;
        try {
            rs = statement.executeQuery();
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

        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_wished = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(finalCheckQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }

            set(User_ID, film, statement1);

            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, statement, statement1);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not in the watchlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(finalCheckQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, statement1);
            try {
                statement1.setBoolean(3, isPublic);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, statement, statement1);
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

        final String query = "SELECT * FROM lists WHERE Film_ID = ? AND User_ID = ? AND Is_watched = TRUE";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, preparedStatement);

        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        int resultSetLength;
        try {
            if (!rs.next()) {
                closeAll(connection, preparedStatement);
                return false;
            }
            resultSetLength = rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        if(resultSetLength == 0){
            try {
                closeAll(connection, preparedStatement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return false;
        }

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE ";

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, statement);
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        boolean is_wished;
        try {
            resultSet.next();
            is_wished = resultSet.getBoolean("Is_wished");
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }


        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (is_wished) {
            final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, statement1);
            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, resultSet, statement, statement1);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not in the wishlist
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
    public boolean removeFromWished(Film film, int User_ID)  {
        boolean result;
        Connection connection;
        try {
            connection = DbManager.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        //check if the film is marked as wished

        final String query = "SELECT * FROM lists WHERE Film_ID = ? AND User_ID = ? AND Is_wished = TRUE";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        set(User_ID, film, preparedStatement);
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        try {
            if (!rs.next()) {
                closeAll(connection, preparedStatement);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(checkQuery);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }


        set(User_ID, film, statement);
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }

        boolean isWatched;

        try {
            resultSet.next();
            isWatched = resultSet.getBoolean("Is_watched");
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }


        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (isWatched) {
            final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1;
            try {
                statement1 = connection.prepareStatement(updateQuery);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            set(User_ID, film, statement1);
            try {
                result = statement1.execute();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            try {
                closeAll(connection, resultSet, preparedStatement, statement1, statement);
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
            return result;
        }

        //case: not in the watchlist
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
