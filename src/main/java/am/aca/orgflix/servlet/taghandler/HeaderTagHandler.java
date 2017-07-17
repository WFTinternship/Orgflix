package am.aca.orgflix.servlet.taghandler;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag for general header
 */
public class HeaderTagHandler extends TagSupport {
    private static final Logger LOGGER = Logger.getLogger(HeaderTagHandler.class.getName());

    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {
            Object obj = pageContext.getSession().getAttribute("user");
            String userName = "";
            if(obj != null){
                String tmp =((String) obj);
                userName = "<i class=\"fa fa-user fa-fw\"></i> "+tmp.substring(0,tmp.indexOf('('));
            }
            final String header = "" +
                    "<header id=\"header\">" +
                    "<div class=\"inner\">" +
                    "<!-- Logo -->" +
                    "<a href=\"\\\" class=\"logo\">" +
                    "<span class=\"symbol\"><img src='../resources/images/logo.svg' /></span>" +
                    "<span class=\"title\">Orgflix</span>" +
                    "</a>"+
                    "<!-- Nav -->" +
                    "<nav>" +
                    "<ul>" +
                    "<li><b>"+userName+"</b> <a href=\"#menu\">Menu</a></li>" +
                    "</ul>" +
                    "</nav>" +
                    "</div>" +
                    "</header>";
            out.println(header);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SKIP_BODY;//will not evaluate the body content of the tag
    }
}
