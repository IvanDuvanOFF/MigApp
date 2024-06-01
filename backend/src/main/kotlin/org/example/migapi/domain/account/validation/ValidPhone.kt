package org.example.migapi.domain.account.validation

import jakarta.validation.Constraint
import kotlin.reflect.KClass


@MustBeDocumented
@Constraint(validatedBy = [NumberValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidPhone(
    val message: String = "Invalid phone number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)