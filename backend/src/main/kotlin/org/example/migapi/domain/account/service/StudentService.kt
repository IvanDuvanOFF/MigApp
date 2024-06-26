package org.example.migapi.domain.account.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.domain.account.dto.*
import org.example.migapi.domain.account.exception.CountryNotFoundException
import org.example.migapi.domain.account.exception.StatusNotFoundException
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.domain.files.exception.NoAccessException
import java.time.DateTimeException
import java.time.format.DateTimeParseException

interface StudentService {

    @Throws(exceptionClasses = [PersistenceException::class, DateTimeException::class])
    fun getAll(): List<StudentDto>

    @Throws(
        exceptionClasses = [
            CountryNotFoundException::class,
            BadRequestException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            UserNotFoundException::class,
            PersistenceException::class,
        ]
    )
    fun getById(id: String): StudentDto

    @Throws(
        exceptionClasses = [
            NoAccessException::class,
            BadRequestException::class,
            DateTimeParseException::class,
            UserNotFoundException::class,
            PersistenceException::class,
        ]
    )
    fun getByUsername(username: String): StudentDto

    @Throws(
        exceptionClasses = [
            NoAccessException::class,
            BadRequestException::class,
            DateTimeParseException::class,
            UserNotFoundException::class,
            PersistenceException::class,
        ]
    )
    fun changePhoto(username: String, photoFileName: String): StudentDto

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun updatePassword(username: String, passwordDto: PasswordDto)

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun changePhone(username: String, phoneDto: PhoneDto)

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun changeEmail(username: String, emailDto: EmailDto)

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun turnTfa(username: String, tfaTurnDto: TfaTurnDto): TfaTurnDto

    @Throws(
        exceptionClasses = [
            CountryNotFoundException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun put(studentDto: StudentDto)

    @Throws(
        exceptionClasses = [
            UserAlreadyExistsException::class,
            CountryNotFoundException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun create(studentDto: StudentDto)

    @Throws(
        exceptionClasses = [
            IllegalArgumentException::class,
            UserNotFoundException::class,
            PersistenceException::class
        ]
    )
    fun delete(id: String)
}