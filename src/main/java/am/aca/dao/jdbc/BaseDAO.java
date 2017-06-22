package am.aca.dao.jdbc;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


/**
 * Created by David on 6/13/2017
 */
public class BaseDAO extends NamedParameterJdbcDaoSupport{
    //common logger for all the classes in the dao layer
    protected Logger logger;

    public BaseDAO(Class<?> daoClass) {
        logger = Logger.getLogger(daoClass);
    }

    protected boolean checkRequiredFields(String[] fields){
        boolean state = false;
        for(String field : fields){
            if (field == null || field.length() == 0)
                return state;
        }
        return true;
    }
}
