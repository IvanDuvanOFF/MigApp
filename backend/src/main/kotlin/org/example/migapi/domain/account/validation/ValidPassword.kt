package org.example.migapi.domain.account.validation

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidPassword(
    val message: String = "Too weak password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)
