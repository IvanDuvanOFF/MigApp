package org.example.migapi.domain.account.model

import jakarta.persistence.*
import org.example.migapi.auth.model.Role
import org.example.migapi.core.domain.model.Model
import org.example.migapi.core.domain.model.SpringUser
import org.example.migapi.core.domain.model.enums.ESex
import org.example.migapi.domain.files.model.File
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

/**
 * Сущность пользователя
 *
 * @property id id пользователя [UUID]
 * @property username username пользователя [String]
 * @property email адрес электронной почты пользователя [String]
 * @property password пароль пользователя [String]
 * @property isActive статус активности пользователя [Boolean]
 * @property name имя пользователя [String]
 * @property surname фамилия пользователя [String]
 * @property patronymic отчество пользователя [String]
 * @property photo имя файла аватарки пользователя [File]
 * @property institute институт пользователя [String]
 * @property group группа пользователя [String]
 * @property sex пол пользователя [ESex]
 * @property phone номер телефона пользователя [String]
 * @property country страна пользователя [Country]
 * @property birthday день рождения пользователя [LocalDate]
 * @property status статус пользователя [StudentStatus]
 * @property tfaEnabled статус активности 2х факторной аутентификации для пользователя [Boolean]
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(name = "username", unique = true, nullable = false)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @ManyToOne(targetEntity = Role::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "role")
    var role: Role,

    @Column(nullable = false, name = "is_active")
    var isActive: Boolean = false,

    var name: String = "None",

    var surname: String = "None",

    var patronymic: String? = null,

    var institute: String? = null,

    @Column(name = "group_name")
    var group: String? = null,

    @OneToOne(targetEntity = File::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo")
    var photo: File? = null,

    @Enumerated(EnumType.STRING)
    var sex: ESex? = null,

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty
    var email: String = "None",

    @NotEmpty
    var phone: String = "None",

    @NotEmpty
    @ManyToOne(targetEntity = Country::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "country")
    var country: Country = Country(),

    @NotEmpty
    var birthday: LocalDate = LocalDate.EPOCH,

    @NotEmpty
    @ManyToOne(targetEntity = StudentStatus::class, fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    var status: StudentStatus = StudentStatus(),

    @Column(name = "tfa_enabled")
    var tfaEnabled: Boolean = false
) : Model {
    fun toSpringUser(): UserDetails = SpringUser.builder()
        .username(username)
        .password(password)
        .authorities(
            listOf(SimpleGrantedAuthority(role.name.name))
        )
        .disabled(!isActive)
        .build()
}