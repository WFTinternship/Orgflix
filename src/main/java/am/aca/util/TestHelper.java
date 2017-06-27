package am.aca.util;

import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
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

    public final Object unwrapProxy(Object bean) throws Exception {
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {

            Advised advised = (Advised) bean;
            bean = advised.getTargetSource().getTarget();
        }

        return bean;
    }
}
