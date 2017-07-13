package am.aca.orgflix.service;

import java.time.Year;

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
}
