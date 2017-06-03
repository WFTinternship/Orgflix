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
        Connection connection = new DbManager().getConnection();

        final String checkQuery = "SELECT * FROM Lists where User_ID = ? AND Film_ID = ?";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        int resultSetLength = statement.executeUpdate();

        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_watched = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }

        //case: not in the wishlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            statement1.setBoolean(3, isPublic);
            return statement1.execute();
        }
    }

    @Override
    public boolean addToWished(Film film, boolean isPublic, int User_ID) throws SQLException {
        Connection connection = new DbManager().getConnection();

        final String checkQuery = "SELECT * FROM Lists where User_ID = ? AND Film_ID = ?";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        int resultSetLength = statement.executeUpdate();

        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (resultSetLength == 1) {
            final String finalCheckQuery = "UPDATE lists SET Is_wished = TRUE " +
                    "WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }

        //case: not in the watchlist
        else{
            final String finalCheckQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
            PreparedStatement statement1 = connection.prepareStatement(finalCheckQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            statement1.setBoolean(3, isPublic);
            return statement1.execute();
        }
    }

    @Override
    public boolean removeFromWatched(Film film, int User_ID) throws SQLException {
        Connection connection = new DbManager().getConnection();

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE ";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet resultSet = statement.executeQuery();
        String is_wished = resultSet.getString("Is_wished");


        //check whether or not the given film is in the wishlist

        //case: in the wishlist
        if (is_wished.equalsIgnoreCase("true")) {
            final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(updateQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }

        //case: not in the wishlist
        else {
            final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(deleteQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }
    }

    @Override
    public boolean removeFromWished(Film film, int User_ID) throws SQLException {
        Connection connection = new DbManager().getConnection();

        final String checkQuery = "SELECT * FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";

        PreparedStatement statement = connection.prepareStatement(checkQuery);

        statement.setInt(1, User_ID);
        statement.setInt(2, film.getId());
        ResultSet resultSet = statement.executeQuery();
        String is_wished = resultSet.getString("Is_wished");


        //check whether or not the given film is in the watchlist

        //case: in the watchlist
        if (is_wished.equalsIgnoreCase("true")) {
            final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(updateQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }

        //case: not in the watchlist
        else {
            final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(deleteQuery);
            statement1.setInt(1, User_ID);
            statement1.setInt(2, film.getId());
            return statement1.execute();
        }
    }

    @Override
    public ArrayList<Film> showWatched(int User_ID) throws SQLException {
        Connection connection = new DbManager().getConnection();

        ArrayList<Film> watched = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, User_ID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            watched.add(new Film());
        }
        return watched;
    }

    @Override
    public ArrayList<Film> showWished(int User_ID) throws SQLException {
        Connection connection = new DbManager().getConnection();

        ArrayList<Film> wished = new ArrayList<>();
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films on lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, User_ID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            wished.add(new Film());
        }
        return wished;
    }
}