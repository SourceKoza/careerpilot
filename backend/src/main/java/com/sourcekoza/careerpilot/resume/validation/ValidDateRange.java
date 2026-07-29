package com.sourcekoza.careerpilot.resume.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cross-field validation annotation ensuring an end date is not before a start date.
 *
 * <p>Applied at the type level. Uses reflection to read the named date fields.
 * If either date is null, validation passes (null handling is left to @NotNull).</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Repeatable(ValidDateRange.List.class)
public @interface ValidDateRange {

    String message() default "End date must not be before start date";

    String startDateField();

    String endDateField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidDateRange[] value();
    }
}
