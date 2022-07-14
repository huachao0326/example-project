package com.test.validate.stringArrayValidate;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description: long to time validate
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
public class StringArrayValidator implements ConstraintValidator<StringArrayValidation, String[]> {

    private StringArrayValidation validation;

    /**
     * Initializes the validator in preparation for
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(StringArrayValidation constraintAnnotation) {
        this.validation = constraintAnnotation;
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        for (String str : value) {
            if (StringUtils.isBlank(str)) {
                return false;
            }
        }
        return true;
    }

}
