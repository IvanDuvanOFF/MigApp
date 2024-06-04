package org.example.migapi.core.domain.model.enums

import java.io.Serializable

/**
 * Перечисление ролей пользователей в системе
 *
 * - [ROLE_USER] роль пользователя (студента)
 * - [ROLE_ADMIN] роль админа
 */
enum class ERole : Serializable {
    ROLE_USER,
    ROLE_ADMIN
}