package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Vardan on 09.06.2017
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String page = request.getParameter("currPage");
        request.setAttribute("message", Integer.valueOf( page!=null ? page : "0" ) );
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        List<Film> films = new FilmServiceImpl().getFilmsList(0);

        response.setContentType("text/html");
        request.setAttribute("message", "0" );
//        request.setAttribute("films", films );
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}
