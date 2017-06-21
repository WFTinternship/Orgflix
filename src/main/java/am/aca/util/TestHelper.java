package am.aca.util;

import am.aca.dao.springjdbc.BaseDAO;
import am.aca.dao.springjdbc.implspringjdbc.JdbcUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by David on 6/21/2017
 */

@Repository
public class TestHelper extends BaseDAO {
    @Autowired
    public TestHelper(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }
    public void emptyTable(String[] tables){
        for (String table : tables) {
            final String query = "DELETE FROM " + table;
            getJdbcTemplate().update(query);
        }
    }
}
