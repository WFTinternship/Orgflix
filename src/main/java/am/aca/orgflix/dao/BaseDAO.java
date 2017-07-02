package am.aca.orgflix.dao;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


/**
 * Base class for all DAO layer classes providing logger and common methods
 */
public class BaseDAO extends NamedParameterJdbcDaoSupport {
    //common logger for all the classes in the dao layer
    protected Logger logger;

    public BaseDAO(Class<?> daoClass) {
        logger = Logger.getLogger(daoClass);
    }

    /**
     * Helper method which ensures that all required parameters are not null (or empty for Strings)
     *
     * @param fields parameters to be checked
     * @return false if some required parameter is null or string is empty, otherwise true
     */
    protected boolean checkRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.length() == 0)
                return false;
        }
        return true;
    }
}
