package am.aca.orgflix.service;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementations of methods used throughout Service implementations
 */
public class BaseServiceImpl {
    private static final int FIRST_EVER_FILM_PRODUCTION_YEAR = 1888;
    private static final int MAX_DURATION_OF_FUTURE_FILM_ANNOUNCEMENTS = 7;

    protected void validateYear(int year) {
        Year currentYear = Year.now();
        if (year < FIRST_EVER_FILM_PRODUCTION_YEAR ||
                year > currentYear.getValue() + MAX_DURATION_OF_FUTURE_FILM_ANNOUNCEMENTS)
            throw new ServiceException("Invalid field production year");
    }

    protected void checkRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.length() == 0)
                throw new ServiceException("Required field " + field + " empty");
        }
    }

    protected void validateEmail(String email) {
        final Pattern pattern =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find())
            throw new ServiceException("Invalid Email");
    }

    protected void validatePassword(String pass) {
        final Pattern pattern =
                Pattern.compile("^(?=\\S+$).{6,}$");
        Matcher matcher = pattern.matcher(pass);
        if (!matcher.find())
            throw new ServiceException("Invalid Password, " +
                    "must be at least 6 characters long and contain no spaces");
    }
}
