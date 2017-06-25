package am.aca.servlet;

import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.service.impl.FilmServiceImpl;
import am.aca.service.impl.UserServiceImpl;
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
@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String nick, userName, email, pass;

        nick = request.getParameter("nick");
        userName = request.getParameter("userName");
        email = request.getParameter("email");
        pass = request.getParameter("pass");
        final String showUser = nick + " (" + email + ")";


        User user = new User(nick, userName, email, pass);
        int userId = BeanProvider.getUserService().add(user);
        if( userId != -1) {
            List<Film> films = BeanProvider.getFilmService().getFilmsList(0);
            request.setAttribute("films", films);
            request.setAttribute("message", 0);
            request.setAttribute("userId", userId);
            request.setAttribute("user", showUser);
            request.setAttribute("userAuth", nick.hashCode()+email.hashCode());

            request.getRequestDispatcher("WEB-INF/pages/home.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(request, response);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        request.getRequestDispatcher("WEB-INF/pages/signup.jsp").forward(request, response);
    }
}
