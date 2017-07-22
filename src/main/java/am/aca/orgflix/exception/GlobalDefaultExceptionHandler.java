package am.aca.orgflix.exception;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *  Exception handler for ServiceExceptions thrown in any class on service layer
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW = "error";
    private Logger LOGGER =Logger.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = ServiceException.class)
    public ModelAndView defaultErrorHandler(Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW, "message", e);
        LOGGER.warn(e.getMessage());
        return mav;
    }
}
