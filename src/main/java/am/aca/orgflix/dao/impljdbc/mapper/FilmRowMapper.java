package am.aca.orgflix.dao.impljdbc.mapper;


import am.aca.orgflix.entity.Film;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper for Film entity
 */
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("ID"));
        film.setTitle(rs.getString("TITLE"));
        film.setProdYear(rs.getInt("PROD_YEAR"));
        film.setImage(rs.getString("IMAGE_REF"));
        film.setHasOscar(rs.getBoolean("HAS_OSCAR"));
        film.setDirector(rs.getString("DIRECTOR"));
        film.setRate_1star(rs.getInt("RATE_1STAR"));
        film.setRate_2star(rs.getInt("RATE_2STAR"));
        film.setRate_3star(rs.getInt("RATE_3STAR"));
        film.setRate_4star(rs.getInt("RATE_4STAR"));
        film.setRate_5star(rs.getInt("RATE_5STAR"));
        return film;
    }
}
