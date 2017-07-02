package am.aca.orgflix.servlet.taghandler;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * tag for page bottom
 */
public class FooterTagHandler extends TagSupport {
    private static final Logger LOGGER = Logger.getLogger(HeaderTagHandler.class.getName());
    public int doStartTag() throws JspException {

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try {
            final String footer = ""+
                    "<!-- Footer -->"+
                    "<footer id=\"footer\">"+
                        "<div class=\"inner\" style=\"display:table\" >"+
                            "<section style=\"min-width:350px\">"+
                                "<h2>Follow</h2>"+
                                "<ul class=\"icons\">"+
                                    "<li><a href=\"www.twitter.com\" target=\"_blank\" class=\"icon style2 fa-twitter\"><span class=\"label\">Twitter</span></a></li>"+
                                    "<li><a href=\"www.facebook.com\" target=\"_blank\" class=\"icon style2 fa-facebook\"><span class=\"label\">Facebook</span></a></li>"+
                                    "<li><a href=\"https://github.com/WFTinternship/Orgflix\" target=\"_blank\" class=\"icon style2 fa-github\"><span class=\"label\">GitHub</span></a></li>"+
                                    "<li><a id=\"contact_us\" class=\"icon style2 fa-phone\"><span class=\"label\">Phone</span></a></li>"+
                                    "<li><a href=\"#touch_us\" id=\"touch_us\" class=\"icon style2 fa-envelope-o\"><span class=\"label\">Email</span></a></li>"+
                                "</ul>"+
                            "</section>"+
                            "<section id=\"contact-section\" class=\"hiddenElement\">"+
                                "<h2>Contact us</h2>"+
                                "<div>"+
                                    "<span>Phone:</span>"+
                                    "<span> +374 99 00000000</span>"+
                                "</div>"+
                                "<div>"+
                                    "<span>Fax:</span>"+
                                    "<span> +374 99 00000000</span>"+
                                "</div>"+
                                "<div>"+
                                    "<span>Address:</span>"+
                                    "<span> Building 1, Republic square, Yerevan, RA</span>"+
                                "</div>"+
                            "</section>"+
                            "<section id=\"touch-section\" class=\"hiddenElement\">"+
                                "<h2>Get in touch</h2>"+
                                "<form method=\"post\" action=\"#\">"+
                                    "<div class=\"field half first\">"+
                                        "<input type=\"text\" name=\"name\" id=\"name\" placeholder=\"Name\" />"+
                                    "</div>"+
                                    "<div class=\"field half\">"+
                                        "<input type=\"email\" name=\"email\" id=\"email\" placeholder=\"Email\" />"+
                                    "</div>"+
                                    "<div class=\"field\">"+
                                        "<textarea name=\"message\" id=\"message\" placeholder=\"Message\"></textarea>"+
                                    "</div>"+
                                    "<ul class=\"actions\">"+
                                        "<li><input type=\"submit\" value=\"Send\" class=\"special\" /></li>"+
                                    "</ul>"+
                                "</form>"+
                            "</section>"+
                        "</div>"+
                    "</footer>";

                    out.println(footer);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SKIP_BODY;//will not evaluate the body content of the tag
    }
}
