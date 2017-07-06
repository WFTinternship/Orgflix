package am.aca.orgflix.servlet.taghandler;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.PrintWriter;

/**
 * Tag for general header
 */
@Component
public class ActorInnerTagHandler extends TagSupport {
    private static final Logger LOGGER = Logger.getLogger(ActorInnerTagHandler.class.getName());

    private CastService castService;

    @Autowired
    public ActorInnerTagHandler(CastService castService){
        this.castService = castService;
    }


    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<div id='actorList' class='hiddenElem'>");
            for(Cast cast : castService.listCasts()){
                sb.append(cast.getId());
                sb.append(":");
                sb.append(cast.getName());
                sb.append(":");
                sb.append(cast.isHasOscar());
                sb.append("#");
            }
            sb.append("</div>");
            out.println(sb.toString());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SKIP_BODY;//will not evaluate the body content of the tag
    }
}
