package com.sourcekoza.careerpilot.resume.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.time.LocalDate;

/**
 * Validates that an end date field is not before a start date field.
 *
 * <p>Returns {@code true} (valid) when:
 * <ul>
 *   <li>startDate is null (cannot validate range without a start)</li>
 *   <li>endDate is null (open-ended ranges are acceptable)</li>
 *   <li>endDate is equal to or after startDate</li>
 * </ul>
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            LocalDate startDate = (LocalDate) getFieldValue(value, startDateField);
            LocalDate endDate = (LocalDate) getFieldValue(value, endDateField);

            if (startDate == null || endDate == null) {
                return true;
            }

            return !endDate.isBefore(startDate);
        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();

        // Support Java records by using accessor methods
        if (clazz.isRecord()) {
            for (RecordComponent component : clazz.getRecordComponents()) {
                if (component.getName().equals(fieldName)) {
                    return component.getAccessor().invoke(object);
                }
            }
        }

        // Fallback to field reflection for regular classes
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
