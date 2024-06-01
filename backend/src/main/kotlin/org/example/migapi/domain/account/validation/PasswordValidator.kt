package org.example.migapi.domain.account.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.example.migapi.utils.MigUtils


class PasswordValidator : ConstraintValidator<ValidPassword, String> {
    override fun isValid(p0: String, p1: ConstraintValidatorContext?): Boolean =
        MigUtils.PASSWORD_PATTERN.toRegex().matches(p0)
}