package am.aca.dao.jdbc.impljdbc.mapper;


import am.aca.entity.Film;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 6/20/2017
 */
public class FilmRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getInt("ID"));
            film.setTitle(rs.getString("Title"));
            film.setProdYear(rs.getInt("Prod_Year"));
            film.setImage(rs.getString("image_ref"));
            film.setDirector(rs.getString("Director"));
            film.setRate_1star(rs.getInt("Rate_1star"));
            film.setRate_2star(rs.getInt("Rate_2star"));
            film.setRate_3star(rs.getInt("Rate_3star"));
            film.setRate_4star(rs.getInt("Rate_4star"));
            film.setRate_5star(rs.getInt("Rate_5star"));

        return film;
    }
}
