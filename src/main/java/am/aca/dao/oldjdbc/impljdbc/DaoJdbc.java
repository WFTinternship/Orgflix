package am.aca.dao.jdbc.impljdbc;

import am.aca.util.ConnType;
import am.aca.util.DbManager;

/**
 * Created by David on 6/13/2017
 */
public class DaoJdbc {
    DbManager dataSource;

    DaoJdbc() {
        this(ConnType.PRODUCTION);
    }
    DaoJdbc(ConnType connType) {
        if(connType == ConnType.TEST)
            dataSource = DbManager.getInstanceTest();
        else
            dataSource = DbManager.getInstance();
    }
}
