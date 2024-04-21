package org.example.migapi.core.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.core.domain.dto.AdminDto
import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.exception.CountryNotFoundException
import org.example.migapi.auth.exception.RoleNotFoundException
import kotlin.jvm.Throws

interface DtoService {
    @Throws(exceptionClasses = [RoleNotFoundException::class, CountryNotFoundException::class, PersistenceException::class])
    fun toUser(userDto: UserDto): User

    fun userToUserDto(user: User): UserDto

    fun userToAdminDto(user: User): AdminDto

    fun userToStudentDto(user: User): StudentDto

    @Throws(exceptionClasses = [CountryNotFoundException::class, PersistenceException::class])
    fun studentDtoToUser(studentDto: StudentDto): User

    fun adminDtoToUser(adminDto: AdminDto): User

    @Throws(exceptionClasses = [RoleNotFoundException::class])
    fun userDtoToUser(userDto: UserDto): User
}