package am.aca.servlet;

import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.service.impl.FilmServiceImpl;
import am.aca.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Davit on 15.06.2017
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String email = request.getParameter("email");
        User user = new UserServiceImpl().getUser(email);

        if(user != null){
            final String showUser = user.getNick() + " (" + email + ")";

            List<Film> films = new FilmServiceImpl().getFilmsList(0);
            request.setAttribute("films", films);
            request.setAttribute("message", 0);
            request.setAttribute("userId", user.getId());
            request.setAttribute("user", showUser);
            request.setAttribute("userAuth", user.getNick().hashCode()+email.hashCode());

            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
