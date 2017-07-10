package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.DaoException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class to be extended by all DAO JPA Hibernate implementations
 */
class HibernateBaseDAO {
    protected void checkRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.length() == 0)
                throw new DaoException("Required field " + field + " empty");
        }
    }

    protected boolean validateEmail(String email) {
        final Pattern pattern =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    protected boolean validatePassword(String pass) {
        final Pattern pattern =
                Pattern.compile("^(?=\\S+$).{6,}$");
        Matcher matcher = pattern.matcher(pass);
        return matcher.find();
    }
}
