package am.aca.servlet;

import am.aca.entity.Film;
import am.aca.service.impl.ListServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
@WebServlet("/watch_list")
public class WatchListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html");
        String page = request.getParameter("currPage");
        int userId = Integer.parseInt(request.getParameter("userId"));

        int pageNum = Integer.valueOf( page!=null ? page : "0" );
        List<Film> films = BeanProvider.getListService().showOwnWatched(userId);
        request.setAttribute("films", films );
        request.setAttribute("message", pageNum );
        request.setAttribute("userId", userId);
        request.setAttribute("userAuth", request.getParameter("userAuth"));
        request.setAttribute("user", request.getParameter("user"));
        request.getRequestDispatcher("WEB-INF/pages/home.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        String page = request.getParameter("currPage");

        int pageNum = Integer.valueOf( page!=null ? page : "0" );
        List<Film> films = BeanProvider.getListService().showOwnWatched(userId);

        response.setContentType("text/html");
        request.setAttribute("message", "0" );
        request.setAttribute("films", films );
        request.setAttribute("userId", userId);
        request.setAttribute("userAuth", request.getParameter("userAuth"));
        request.setAttribute("user", request.getParameter("user"));
        request.getRequestDispatcher("WEB-INF/pages/watch_list.jsp").forward(request, response);
    }
}
