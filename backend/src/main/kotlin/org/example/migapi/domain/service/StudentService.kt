package org.example.migapi.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.core.domain.exception.CountryNotFoundException
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.domain.exception.StatusNotFoundException
import java.time.DateTimeException
import java.time.format.DateTimeParseException

interface StudentService {

    @Throws(exceptionClasses = [PersistenceException::class, DateTimeException::class])
    fun getAll(): List<StudentDto>

    @Throws(
        exceptionClasses = [
            CountryNotFoundException::class,
            RoleNotFoundException::class,
            DateTimeParseException::class,
            StatusNotFoundException::class,
            UserNotFoundException::class,
            IllegalArgumentException::class,
            PersistenceException::class,
        ]
    )
    fun getById(id: String): StudentDto

    @Throws(exceptionClasses = [UserNotFoundException::class, PersistenceException::class])
    fun updatePassword(username: String, password: String)

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