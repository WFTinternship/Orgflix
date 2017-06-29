package am.aca.servlet.taghandler;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import am.aca.servlet.BeanProvider;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;

/**
 *  Tag handler for pagination of films
 */
public class PaginationTagHandler extends TagSupport {

    private static final Logger LOGGER = Logger.getLogger(PaginationTagHandler.class.getName());

//    private String pageType;
//    private int userId;
//
//    public void setPageType(String pageType) {
//        this.pageType = pageType;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }


    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {
            int filmNum = 0;
//            if(pageType == "all") {
                filmNum = BeanProvider.getFilmService().totalNumberOfFilms();
//            } else if(pageType == "watch"){
//                filmNum = BeanProvider.getListService().showOwnWatched(userId).size();
//            } else if(pageType == "wish"){
//                filmNum = BeanProvider.getListService().showOwnPlanned(userId).size();
//            }
            if (filmNum > 12) {
                out.print("<div class='pagintion_container'>");
                for (int i = 0; i <= filmNum / 12; ++i) {
                    out.print("<div class='pagination' id='" + i + "' onclick='pagination(this.id)'>" + (i + 1) + "</div>");
                }
                out.print("</div>");
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SKIP_BODY;//will not evaluate the body content of the tag
    }
}
