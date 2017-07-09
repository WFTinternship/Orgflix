package am.aca.orgflix.dao.impljdbc.mapper;

import am.aca.orgflix.entity.Cast;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper for Cast entity
 */
public class CastRowMapper implements RowMapper {
    @Override
    public Cast mapRow(ResultSet resultSet, int i) throws SQLException {
        Cast cast = new Cast();
        cast.setId(resultSet.getInt("ID"));
        cast.setName(resultSet.getString("ACTOR_NAME"));
        cast.setHasOscar(resultSet.getBoolean("HAS_OSCAR"));
        return cast;
    }
}
