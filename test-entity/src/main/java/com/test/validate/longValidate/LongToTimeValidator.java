package com.test.validate.longValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description: long to time validate
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
public class LongToTimeValidator implements ConstraintValidator<LongToTimeValidation, Long> {

    private LongToTimeValidation longToTimeValidation;

    /**
     * Initializes the validator in preparation for
     * {@link #isValid(Object, ConstraintValidatorContext)} calls.
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
    public void initialize(LongToTimeValidation constraintAnnotation) {
        this.longToTimeValidation = constraintAnnotation;
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
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        long nowTime = System.currentTimeMillis() / 1000;
        if (value < nowTime) {
            return false;
        } else {
            return true;
        }
    }

}
