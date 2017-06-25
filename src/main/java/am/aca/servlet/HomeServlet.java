package am.aca.servlet;

import am.aca.entity.Film;
import am.aca.service.FilmService;
import am.aca.service.impl.FilmServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String page = request.getParameter("currPage");

        int pageNum = Integer.valueOf( page!=null ? page : "0" );
        List<Film> films = BeanProvider.getFilmService().getFilmsList(pageNum*12);
        request.setAttribute("films", films );
        request.setAttribute("message", pageNum );
        request.setAttribute("userId", request.getParameter("userId"));
        request.setAttribute("userAuth", request.getParameter("userAuth"));
        request.setAttribute("user", request.getParameter("user"));
        request.getRequestDispatcher("WEB-INF/pages/home.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FilmService filmService = BeanProvider.getFilmService();

        List<Film> films = filmService.getFilmsList(0);

        response.setContentType("text/html");
        request.setAttribute("message", "0" );
        request.setAttribute("films", films );
        request.setAttribute("userId", -1);
        request.setAttribute("userAuth", 0);
        request.setAttribute("user", "");
        request.getRequestDispatcher("WEB-INF/pages/home.jsp").forward(request, response);
    }
}
