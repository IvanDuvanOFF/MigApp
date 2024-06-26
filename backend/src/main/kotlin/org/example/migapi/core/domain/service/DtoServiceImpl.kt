package org.example.migapi.core.domain.service

import jakarta.persistence.PersistenceException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.auth.model.Role
import org.example.migapi.core.config.exception.NotFoundException
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.model.enums.ESex
import org.example.migapi.core.domain.model.enums.EStudentStatus
import org.example.migapi.domain.account.dto.AdminDto
import org.example.migapi.domain.account.dto.StudentDto
import org.example.migapi.domain.account.dto.UserDto
import org.example.migapi.domain.account.exception.CountryNotFoundException
import org.example.migapi.domain.account.exception.StatusNotFoundException
import org.example.migapi.domain.account.model.Country
import org.example.migapi.domain.account.model.StudentStatus
import org.example.migapi.domain.account.model.User
import org.example.migapi.domain.account.repository.CountryRepository
import org.example.migapi.domain.files.repository.FileRepository
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.format.DateTimeParseException
import java.util.*

@Service
@Scope("prototype")
class DtoServiceImpl(
    @Autowired
    private val migUtils: MigUtils,
    @Autowired
    private val encoder: BCryptPasswordEncoder,
    @Autowired
    private val countryRepository: CountryRepository,
    @Autowired
    private val fileRepository: FileRepository,
) : DtoService {

    /**
     * Переводит объект [userDto] в [User]
     *
     * @throws CountryNotFoundException
     * @throws RoleNotFoundException
     * @throws DateTimeParseException
     * @throws StatusNotFoundException
     * @throws PersistenceException
     */
    override fun toUser(userDto: UserDto): User = when (userDto) {
        is AdminDto -> adminDtoToUser(userDto)
        is StudentDto -> studentDtoToUser(userDto)
        else -> userDtoToUser(userDto)
    }

    /**
     * Переводит объект студента [studentDto] в [User]
     *
     * @throws CountryNotFoundException
     * @throws RoleNotFoundException
     * @throws DateTimeParseException
     * @throws StatusNotFoundException
     * @throws PersistenceException
     */
    override fun studentDtoToUser(studentDto: StudentDto): User = userDtoToUser(studentDto).apply {
        name = studentDto.name
        surname = studentDto.surname
        patronymic = studentDto.patronymic
        institute = studentDto.institute
        group = studentDto.group
        photo = studentDto.photo?.let { fileRepository.findById(it).orElseThrow { NotFoundException() } }
        sex = ESex.valueOf(studentDto.sex)
        email = studentDto.email
        phone = studentDto.phone
        country = Country(studentDto.country).takeIf { countryRepository.findById(it.name).isPresent }
            ?: throw CountryNotFoundException("No country ${studentDto.country} found")
        birthday = migUtils.stringToLocalDate(studentDto.birthday)
        status = try {
            StudentStatus(EStudentStatus.valueOf(studentDto.status))
        } catch (e: IllegalArgumentException) {
            throw StatusNotFoundException()
        }
    }

    /**
     * Переводит объект админа [adminDto] в [User]
     *
     * @throws RoleNotFoundException
     */
    override fun adminDtoToUser(adminDto: AdminDto): User = userDtoToUser(adminDto).apply {
        name = adminDto.name
        surname = adminDto.surname
    }

    /**
     * Переводит объект пользователя [userDto] в [User]
     *
     * @throws RoleNotFoundException
     */
    override fun userDtoToUser(userDto: UserDto): User = User(
        id = userDto.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
        username = userDto.username,
        password = encoder.encode(userDto.password),
        email = userDto.email,
        isActive = userDto.isActive,
        role = try {
            Role(ERole.valueOf(userDto.role))
        } catch (e: IllegalArgumentException) {
            throw RoleNotFoundException("Role ${userDto.role} doesn't exists")
        },
        tfaEnabled = userDto.tfaEnabled
    )

    /**
     * Переводит объект [user] в [UserDto]
     */
    override fun userToUserDto(user: User): UserDto = UserDto(
        id = user.id.toString(),
        username = user.username,
        email = user.email,
        password = user.password,
        isActive = user.isActive,
        role = user.role.name.name
    )

    /**
     * Переводит объект [user] в [AdminDto]
     */
    override fun userToAdminDto(user: User): AdminDto = AdminDto(
        id = user.id.toString(),
        username = user.username,
        email = user.email,
        password = user.password,
        isActive = user.isActive,
        name = user.name,
        surname = user.surname
    )

    /**
     * Переводит объект [user] в [StudentDto]
     *
     * @throws IllegalArgumentException
     */
    override fun userToStudentDto(user: User): StudentDto = StudentDto(
        id = user.id.toString(),
        username = user.username,
        isActive = user.isActive,
        name = user.name,
        surname = user.surname,
        patronymic = user.patronymic,
        institute = user.institute,
        group = user.group,
        photo = user.photo?.name,
        sex = (user.sex?.name?.lowercase() ?: throw IllegalArgumentException()),
        email = user.email,
        phone = user.phone,
        country = user.country.name,
        birthday = migUtils.localDateToString(user.birthday),
        status = user.status.name.name.lowercase()
    )
}