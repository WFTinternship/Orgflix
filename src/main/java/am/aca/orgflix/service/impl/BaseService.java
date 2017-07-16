package am.aca.orgflix.service.impl;

import org.apache.log4j.Logger;

/**
 * Base class for all Service layer classes providing logger and common methods
 */
public class BaseService {
    //common logger for all the classes in the Service layer
    protected Logger LOGGER;

    public BaseService(Class<?> serviceClass) {
        LOGGER = Logger.getLogger(serviceClass);
    }
}
