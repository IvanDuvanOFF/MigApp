package org.example.migapi.core.domain.service

import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.core.domain.dto.AdminDto
import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.core.domain.dto.UserDto
import org.example.migapi.core.domain.exception.CountryNotFoundException
import org.example.migapi.core.domain.model.entity.Country
import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.entity.StudentStatus
import org.example.migapi.core.domain.model.entity.User
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.model.enums.ESex
import org.example.migapi.core.domain.model.enums.EStudentStatus
import org.example.migapi.core.domain.repo.CountryRepository
import org.example.migapi.domain.exception.StatusNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class DtoServiceImpl(
    @Autowired
    private val formatter: DateTimeFormatter,
    @Autowired
    private val encoder: BCryptPasswordEncoder,
    @Autowired
    private val countryRepository: CountryRepository
) : DtoService {

    override fun toUser(userDto: UserDto): User = when (userDto) {
        is AdminDto -> adminDtoToUser(userDto)
        is StudentDto -> studentDtoToUser(userDto)
        else -> userDtoToUser(userDto)
    }

    override fun studentDtoToUser(studentDto: StudentDto): User = userDtoToUser(studentDto).apply {
        name = studentDto.name
        surname = studentDto.surname
        patronymic = studentDto.patronymic
        sex = ESex.valueOf(studentDto.sex)
        email = studentDto.email
        phone = studentDto.phone
        country = Country(studentDto.country).takeIf { countryRepository.findById(it.name).isPresent }
            ?: throw CountryNotFoundException("No country ${studentDto.country} found")
        birthday = LocalDate.parse(studentDto.birthday, formatter)
        status = try {
            StudentStatus(EStudentStatus.valueOf(studentDto.status))
        } catch (e: IllegalArgumentException) {
            throw StatusNotFoundException()
        }
    }

    override fun adminDtoToUser(adminDto: AdminDto): User = userDtoToUser(adminDto).apply {
        name = adminDto.name
        surname = adminDto.surname
    }

    override fun userDtoToUser(userDto: UserDto): User = User(
        id = userDto.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
        username = userDto.username,
        password = encoder.encode(userDto.password),
        isActive = userDto.isActive,
        role = try {
            Role(ERole.valueOf(userDto.role))
        } catch (e: IllegalArgumentException) {
            throw RoleNotFoundException("Role ${userDto.role} doesn't exists")
        }
    )

    override fun userToUserDto(user: User): UserDto = UserDto(
        id = user.id.toString(),
        username = user.username,
        email = user.email,
        password = user.password,
        isActive = user.isActive,
        role = user.role.name.name
    )

    override fun userToAdminDto(user: User): AdminDto = AdminDto(
        id = user.id.toString(),
        username = user.username,
        email = user.email,
        password = user.password,
        isActive = user.isActive,
        name = user.name,
        surname = user.surname
    )

    override fun userToStudentDto(user: User): StudentDto = StudentDto(
        id = user.id.toString(),
        username = user.username,
        isActive = user.isActive,
        name = user.name,
        surname = user.surname,
        patronymic = user.patronymic,
        sex = (user.sex?.name ?: throw IllegalArgumentException()),
        email = user.email,
        phone = user.phone,
        country = user.country.name,
        birthday = user.birthday.format(formatter),
        status = user.status.name.name
    )
}