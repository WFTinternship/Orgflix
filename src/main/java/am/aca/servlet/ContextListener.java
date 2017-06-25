package am.aca.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Context listener class
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private static ApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
