package com.test.exception;

import com.test.response.Response;
import com.test.response.ResponseFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 功能描述:捕捉MethodArgumentNotValidException异常信息返回
 *
 * @author hua_chao
 * @create 2022/3/18 下午3:58
 * @since 1.0.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Response response = ResponseFactory.getError();
        response.setCode(400);
        response.setMessage(e.getBindingResult().getFieldError().getDefaultMessage());
        return response;
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Response response = ResponseFactory.getError();
        violations.forEach(violation -> violation.getPropertyPath()
                .forEach(node -> response.setMessage(violation.getMessage())));
        response.setCode(400);
        return response;
    }
}
