package com.test.validate.stringArrayValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: String数组校验
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StringArrayValidation.List.class)
@Constraint(validatedBy = StringArrayValidator.class)
public @interface StringArrayValidation {

    String message() default "parameter validation failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {
        StringArrayValidation[] value();
    }

}
