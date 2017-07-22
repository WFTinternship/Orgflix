package am.aca.orgflix.service.impl;

import am.aca.orgflix.exception.ServiceException;
import org.apache.log4j.Logger;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for all Service layer classes providing logger and common methods
 */
public class BaseService {
    private static final int FIRST_EVER_FILM_PRODUCTION_YEAR = 1888;
    private static final int MAX_DURATION_OF_FUTURE_FILM_ANNOUNCEMENTS = 7;
    //common logger for all the classes in the Service layer
    protected Logger LOGGER;

    public BaseService(Class<?> serviceClass) {
        LOGGER = Logger.getLogger(serviceClass);
    }

    void validateYear(int year) {
        Year currentYear = Year.now();
        if (year < FIRST_EVER_FILM_PRODUCTION_YEAR ||
                year > currentYear.getValue() + MAX_DURATION_OF_FUTURE_FILM_ANNOUNCEMENTS)
            throw new ServiceException("Invalid production year:" +
                    " must be from " + FIRST_EVER_FILM_PRODUCTION_YEAR +
                    " to " + currentYear.getValue() + MAX_DURATION_OF_FUTURE_FILM_ANNOUNCEMENTS);
    }

    void checkRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.length() == 0)
                throw new ServiceException("Required field " + field + " is empty");
        }
    }

    void validateRating(int rating) {
        if (rating < 1 || rating > 5)
            throw new ServiceException("Invalid Rating: must be from 1 to 5");
    }

    void validateEmail(String email) {
        final Pattern pattern =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find())
            throw new ServiceException("Invalid email format");
    }

    void validatePassword(String pass) {
        final Pattern pattern =
                Pattern.compile("^(?=\\S+$).{6,}$");
        Matcher matcher = pattern.matcher(pass);
        if (!matcher.find())
            throw new ServiceException("Invalid Password: " +
                    "must be at least 6 characters long and contain no spaces");
    }
}
