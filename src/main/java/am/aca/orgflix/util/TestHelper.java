package am.aca.orgflix.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Helper methods used in testing
 */

@Repository
public class TestHelper extends NamedParameterJdbcDaoSupport {
    @Autowired
    public TestHelper(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void emptyTable(String[] tables) {
        try {
            for (String table : tables) {
                final String query = "DELETE FROM " + table;
                getJdbcTemplate().update(query);
            }
        } catch (RuntimeException e) {
            e.toString();
        }
    }

}
