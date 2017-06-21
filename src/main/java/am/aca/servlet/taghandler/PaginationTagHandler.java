package am.aca.servlet.taghandler;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import am.aca.service.impl.*;

/**
 * Created by David on 6/17/2017
 */
public class PaginationTagHandler extends TagSupport {

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {

            int filmnum = new FilmServiceImpl().totalNumberOfFilms();
            if (filmnum > 12) {
                out.print("<form class='pagintion_container' id='pageForm' method='POST' action='/home'><input type='hidden' id='currPage' name='currPage' value='0'>");
                for (int i = 0; i <= filmnum / 12; ++i) {
                    out.print("<div class='pagination' id='" + i + "' onclick='pagination(this.id)'>" + (i + 1) + "</div>");
                }
                out.print("</form>");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return SKIP_BODY;//will not evaluate the body content of the tag
    }
}
