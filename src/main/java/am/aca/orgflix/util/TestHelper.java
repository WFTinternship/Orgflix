package am.aca.orgflix.util;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.impljdbc.JdbcUserDAO;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Helper methods used in testing
 */

@Repository
public class TestHelper extends BaseDAO {
    @Autowired
    public TestHelper(DataSource dataSource) {
        super(JdbcUserDAO.class);
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