package am.aca.orgflix.servlet.taghandler;


import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag handler for pagination of films
 */
@Component
public class PaginationTagHandler extends TagSupport {

    private static final Logger LOGGER = Logger.getLogger(PaginationTagHandler.class.getName());

    private FilmService filmService;
    private ListService listService;
    private String pageType;
    private int userId;

    public PaginationTagHandler() {
    }

    @Autowired
    public PaginationTagHandler(ListService listService, FilmService filmService) {
        this.listService = listService;
        this.filmService = filmService;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {
            int filmNum = 0;
            if (pageType.equals("main")) {
                filmNum = filmService.totalNumberOfFilms();
            } else if (pageType.equals("watch")) {
                filmNum = listService.totalNumberOfWatched(userId, true);
            } else if (pageType.equals("wish")) {
                filmNum = listService.totalNumberOfWatched(userId, false);
            }
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
