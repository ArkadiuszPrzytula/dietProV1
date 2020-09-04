package com.pl.arkadiusz.diet_pro.controllers.advicers;

import com.pl.arkadiusz.diet_pro.errors.CustomerErrorResponse;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.errors.ValidErrors;


import com.pl.arkadiusz.diet_pro.services.LoggedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private final HttpServletRequest servletRequest;

    private final LoggedUserService loggedUserService;

    @Autowired
    public GlobalExceptionHandler(HttpServletRequest servletRequest, LoggedUserService loggedUserService) {
        this.servletRequest = servletRequest;
        this.loggedUserService = loggedUserService;
    }


//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAll(Exception ex) {
//
//        CustomerErrorResponse customerErrorResponse = new CustomerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
//                ex.getLocalizedMessage(), "error occurred",
//                servletRequest.getRequestURI());
//        return new ResponseEntity<>(customerErrorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    @ExceptionHandler({AccessDeniedException.class})
//    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ade) {
        String error = "User: " + loggedUserService.getLoggedUserName()
                + " attempted to access the protected URL: "
                + servletRequest.getRequestURI();
        CustomerErrorResponse errorResponse = new CustomerErrorResponse(HttpStatus.FORBIDDEN,
                ade.getLocalizedMessage(),
                error,
                servletRequest.getRequestURI());
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


//    @ExceptionHandler(MissingServletRequestParameterException.class)
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        CustomerErrorResponse errorResponse =
                new CustomerErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error, servletRequest.getRequestURI());
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodNotAllowedException.class)
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        CustomerErrorResponse errorResponse = new CustomerErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), builder.toString(), servletRequest.getRequestURI());
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

//@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        CustomerErrorResponse errorResponse = new CustomerErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2), servletRequest.getRequestURI());
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
            headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        ValidErrors validErrors = new ValidErrors(servletRequest.getRequestURI().substring(servletRequest.getContextPath().length()));
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        validErrors.setErrors(errors);

        return new ResponseEntity<>(validErrors, headers, status);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpireException(TokenExpiredException tex) {
        String error = ""; //todo
        CustomerErrorResponse errorResponse = new CustomerErrorResponse(HttpStatus.BAD_REQUEST,
                tex.getLocalizedMessage(), error,
                servletRequest.getRequestURI());
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
