package com.test.validate.longValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: long转时间后校验
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongToTimeValidator.class)
public @interface LongToTimeValidation {

    String message() default "parameter validation failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
