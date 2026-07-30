package com.sourcekoza.careerpilot.job.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that the maximum salary is greater than or equal to the minimum salary
 * when both values are provided.
 */
@Documented
@Constraint(validatedBy = SalaryRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSalaryRange {

    String message() default "Maximum salary must be greater than or equal to minimum salary";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
