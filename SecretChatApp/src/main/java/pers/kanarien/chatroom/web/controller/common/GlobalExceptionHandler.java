package pers.kanarien.chatroom.web.controller.common;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pers.kanarien.chatroom.model.vo.ResponseJson;

/**
 * Description: Global error unified processing control center
 * @author Kanarien
 * @version 1.0
 * @date May 17, 2018 3:27:49 PM
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final ResponseJson ERROR;

    static {
        ERROR = new ResponseJson(HttpStatus.INTERNAL_SERVER_ERROR).setMsg("System error, please try again later");
    }

    /**
     * Description: Default exception prompt
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseJson defaultErrorHandler(Exception exception) {
        LOG.error(exception.getMessage(), exception);
        return ERROR;
    }

    /**
     * Description: The parameter is illegal and the default exception prompt
     * @param exception
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseJson securityExceptionHandler(Exception exception) {
        return new ResponseJson(HttpStatus.INTERNAL_SERVER_ERROR).setMsg(exception.getMessage());
    }

    /**
     * Description: Incorrect form data format exception prompt
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson illegalParamExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        String tips = "The parameter is invalid";
        ResponseJson result = new ResponseJson(HttpStatus.BAD_REQUEST);
        if (!errors.isEmpty()) {
            List<String> list = errors.stream()
                    .map(error -> error.getField() + error.getDefaultMessage())
                    .collect(Collectors.toList());
            result.put("details", list);
        }
        result.setMsg(tips);
        return result;
    }

    /**
     * Description: Abnormal prompt of missing form data
     * @param exception
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson servletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        return new ResponseJson(HttpStatus.BAD_REQUEST).setMsg(exception.getMessage());
    }

    /**
     * Description: The request method does not support exception prompts
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseJson methodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        String supportedMethods = exception.getSupportedHttpMethods().stream()
                .map(method -> method.toString())
                .collect(Collectors.joining("/"));
        
        String msg = "The request method is illegal, please use the method" + supportedMethods;
        return new ResponseJson(HttpStatus.METHOD_NOT_ALLOWED).setMsg(msg);
    }

    /**
     * Description: Data binding failure exception prompt
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson validationBindException(BindException exception) {
        String errors = exception.getFieldErrors().stream()
                .map(error -> error.getField() + error.getDefaultMessage())
                .collect(Collectors.joining(","));
        return new ResponseJson(HttpStatus.BAD_REQUEST).setMsg(errors);
    }
}
