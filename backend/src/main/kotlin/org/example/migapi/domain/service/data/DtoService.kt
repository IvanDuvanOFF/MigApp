package org.example.migapi.domain.service.data

import jakarta.persistence.PersistenceException
import org.example.migapi.domain.dto.data.AdminDto
import org.example.migapi.domain.dto.data.StudentDto
import org.example.migapi.domain.dto.data.UserDto
import org.example.migapi.domain.model.entity.User
import org.example.migapi.exception.CountryNotFoundException
import org.example.migapi.exception.RoleNotFoundException
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