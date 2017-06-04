package am.aca.dao;

import am.aca.entity.Film;
import am.aca.util.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by karin on 6/3/2017
 */
public class ListDaoJdbc implements ListDao {
    @Override
    public boolean addToWatched(Film film, boolean isPublic, int User_ID) throws SQLException {
        boolean result;
        Connection connection = DbManager.getConnection();

        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet rs = statement.executeQuery();

        rs.next();
        int resultSetLength = rs.getInt(1);
//        int resultSetLength = 1;

        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_watched = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }

        //case: not in the wishlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            statement1.setBoolean(3, isPublic);
            result = statement1.execute();
            connection.close();
            return result;
        }
    }

    @Override
    public boolean addToWished(Film film, boolean isPublic, int User_ID) throws SQLException {
        boolean result;
        Connection connection = DbManager.getConnection();

        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet rs = statement.executeQuery();

        rs.next();
        int resultSetLength = rs.getInt(1);
//        int resultSetLength = 1;

        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_wished = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }

        //case: not in the watchlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            statement1.setBoolean(3, isPublic);
            result = statement1.execute();
            connection.close();
            return result;
        }
    }

    @Override
    public boolean removeFromWatched(Film film, int User_ID) throws SQLException {
        boolean result;
        Connection connection = DbManager.getConnection();

        //check if the film is marked as watched

        final String query = "SELECT * FROM lists WHERE Film_ID = ? AND User_ID = ? AND Is_watched = TRUE";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, film.getId());
        preparedStatement.setInt(2, User_ID);

        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            return false;
        }

        int resultSetLength = rs.getInt(1);

        if(resultSetLength == 0)
            return false;

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE ";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        boolean is_wished = resultSet.getBoolean("Is_wished");


        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (is_wished) {
            final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(updateQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }

        //case: not in the wishlist
        else {
            final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(deleteQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }
    }

    @Override
    public boolean removeFromWished(Film film, int User_ID) throws SQLException {
        boolean result;
        Connection connection = DbManager.getConnection();

        //check if the film is marked as wished

        final String query = "SELECT * FROM lists WHERE Film_ID = ? AND User_ID = ? AND Is_wished = TRUE";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, film.getId());
        preparedStatement.setInt(2, User_ID);
        ResultSet rs = preparedStatement.executeQuery();

//        rs.next();
        if (!rs.next()) {
            return false;
        }
//        System.out.println(rs.next());//////////////////////CHANGED
         int resultSetLength = rs.getInt(1);

//        if(resultSetLength == 0)
//            return false;

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        boolean is_wished = resultSet.getBoolean("Is_wished");


        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (is_wished) {
            final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(updateQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }

        //case: not in the watchlist
        else {
            final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(deleteQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            result = statement1.execute();
            connection.close();
            return result;
        }
    }

    @Override
    public ArrayList<Film> showWatched(int User_ID) throws SQLException {
        Connection connection = DbManager.getConnection();

        ArrayList<Film> watched = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, User_ID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            watched.add(new Film());
        }
        connection.close();
        return watched;
    }

    @Override
    public ArrayList<Film> showWished(int User_ID) throws SQLException {
        Connection connection = DbManager.getConnection();

        ArrayList<Film> wished = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, User_ID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            wished.add(new Film());
        }
        connection.close();
        return wished;
    }
}
