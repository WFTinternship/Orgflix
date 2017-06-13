package am.aca.dao.impljdbc;

import am.aca.util.DbManager;

/**
 * Created by David on 6/13/2017
 */
public class DaoJdbc {
    DbManager dataSource;

    DaoJdbc() {
        dataSource = DbManager.getInstance();
    }
}
