package com.sourcekoza.careerpilot.job.validation;

import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Validator that ensures maximum salary is greater than or equal to minimum salary.
 *
 * <p>Validation passes when either salary value is null (both are optional).
 * Only validates the range relationship when both values are present.</p>
 */
public class SalaryRangeValidator implements ConstraintValidator<ValidSalaryRange, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BigDecimal salaryMin = null;
        BigDecimal salaryMax = null;

        if (value instanceof JobCreateRequest request) {
            salaryMin = request.salaryMin();
            salaryMax = request.salaryMax();
        } else if (value instanceof JobUpdateRequest request) {
            salaryMin = request.salaryMin();
            salaryMax = request.salaryMax();
        }

        if (salaryMin == null || salaryMax == null) {
            return true;
        }

        return salaryMax.compareTo(salaryMin) >= 0;
    }
}
