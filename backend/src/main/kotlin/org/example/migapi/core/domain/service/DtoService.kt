package org.example.migapi.core.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.domain.account.dto.AdminDto
import org.example.migapi.domain.account.dto.StudentDto
import org.example.migapi.domain.account.dto.UserDto
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.account.exception.CountryNotFoundException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.domain.account.exception.StatusNotFoundException
import java.time.DateTimeException
import java.time.format.DateTimeParseException
import kotlin.jvm.Throws

interface DtoService {
    @Throws(
        exceptionClasses = [
            CountryNotFoundException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun toUser(userDto: UserDto): User

    @Throws(
        exceptionClasses = [
            CountryNotFoundException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun studentDtoToUser(studentDto: StudentDto): User

    @Throws(exceptionClasses = [RoleNotFoundException::class])
    fun adminDtoToUser(adminDto: AdminDto): User

    @Throws(exceptionClasses = [RoleNotFoundException::class])
    fun userDtoToUser(userDto: UserDto): User

    fun userToUserDto(user: User): UserDto

    fun userToAdminDto(user: User): AdminDto

    @Throws(exceptionClasses = [DateTimeException::class])
    fun userToStudentDto(user: User): StudentDto
}