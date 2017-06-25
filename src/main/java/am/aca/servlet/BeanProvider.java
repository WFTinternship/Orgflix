package am.aca.servlet;

import am.aca.service.*;
import org.springframework.context.ApplicationContext;

/**
 * Provider class for beans
 */
public class BeanProvider {
    public static UserService getUserService() {
        return (UserService) ContextListener.getApplicationContext().getBean("userService");
    }
    public static CastService getCastService() {
        return (CastService) ContextListener.getApplicationContext().getBean("castService");
    }
    public static FilmService getFilmService() {
        return (FilmService) ContextListener.getApplicationContext().getBean("filmService");
    }
    public static ListService getListService() {
        return (ListService) ContextListener.getApplicationContext().getBean("listService");
    }
}
