package org.example.migapi.domain.account.validation

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [EmailValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEmail(
    val message: String = "Invalid email number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)
