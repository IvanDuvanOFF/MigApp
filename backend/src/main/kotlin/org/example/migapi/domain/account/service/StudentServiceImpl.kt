package org.example.migapi.domain.account.service

import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.RoleNotFoundException
import org.example.migapi.auth.exception.UnauthorizedException
import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.domain.account.dto.*
import org.example.migapi.domain.account.exception.CountryNotFoundException
import org.example.migapi.domain.account.exception.StatusNotFoundException
import org.example.migapi.domain.account.exception.UserNotFoundException
import org.example.migapi.domain.files.exception.NoAccessException
import org.example.migapi.domain.files.model.File
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.DateTimeException
import java.time.format.DateTimeParseException

/**
 * Сервис для работы со студентами [StudentDto]
 */
@Service
class StudentServiceImpl(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val dtoService: DtoService,
    @Autowired
    private val passwordEncoder: PasswordEncoder,
    @Autowired
    private val migUtils: MigUtils
) : StudentService {

    /**
     * Получает всех студентов системы
     *
     * @throws PersistenceException
     * @throws DateTimeException
     */
    override fun getAll(): List<StudentDto> {
        val users = userService.findUsersByRole(ERole.ROLE_USER)

        return users.map { dtoService.userToStudentDto(it) }
    }

    /**
     * Получает студента [StudentDto] по его id [id]
     *
     * @throws CountryNotFoundException страна студента не найдена
     * @throws BadRequestException id пользователя некорректного формата
     * @throws RoleNotFoundException роль студента не найдена
     * @throws DateTimeParseException при сериализации даты произошла ошибка
     * @throws StatusNotFoundException статус студента не найдена
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    override fun getById(id: String): StudentDto = dtoService.userToStudentDto(userService.findById(id))

    /**
     * Получает студента [StudentDto] по его username [username]
     *
     * @throws CountryNotFoundException страна студента не найдена
     * @throws NoAccessException нет доступа
     * @throws BadRequestException id пользователя некорректного формата
     * @throws RoleNotFoundException роль студента не найдена
     * @throws DateTimeParseException при сериализации даты произошла ошибка
     * @throws StatusNotFoundException статус студента не найдена
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    override fun getByUsername(username: String): StudentDto {
        val user = userService.findUserByUsername(username)

        return dtoService.userToStudentDto(user)
    }

    /**
     * Меняет фото [photo] пользователя [username]
     *
     * @throws NoAccessException нет доступа
     * @throws BadRequestException id пользователя некорректного формата
     * @throws DateTimeParseException при сериализации даты произошла ошибка
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    @Transactional
    override fun changePhoto(username: String, photo: File): StudentDto {
        val user = userService.findUserByUsername(username)
        val file = File(
            name = photo.name,
            link = photo.link,
            user = user
        )
        user.photo = file

        return dtoService.userToStudentDto(userService.saveUser(user))
    }

    /**
     * Меняет пароль [passwordDto] пользователя [username]
     *
     * @throws UnauthorizedException пароли не совпадает
     * @throws BadCredentialsException пароль слабый
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    @Transactional
    override fun updatePassword(username: String, passwordDto: PasswordDto) {
        if (!migUtils.validatePassword(passwordDto.newPassword))
            throw BadCredentialsException("Password is weak")

        val user = userService.findUserByUsername(username)

        if (!passwordEncoder.matches(passwordDto.oldPassword, user.password))
            throw UnauthorizedException("Passwords are not the same")

        user.password = passwordEncoder.encode(passwordDto.newPassword)

        userService.saveUser(user)
    }

    /**
     * Меняет телефон [phoneDto] пользователя [username]
     *
     * @throws BadRequestException телефон невалидного формата
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    @Transactional
    override fun changePhone(username: String, phoneDto: PhoneDto) {
        val user = userService.findUserByUsername(username)

        if (!migUtils.validatePhone(phoneDto.phone))
            throw BadRequestException("Phone doesn't valid")

        user.phone = phoneDto.phone

        userService.saveUser(user)
    }

    /**
     * Меняет email [emailDto] пользователя [username]
     *
     * @throws BadRequestException email невалидного формата
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    @Transactional
    override fun changeEmail(username: String, emailDto: EmailDto) {
        val user = userService.findUserByUsername(username)

        if (!migUtils.validateEmail(emailDto.email))
            throw BadRequestException("Email doesn't valid")

        user.email = emailDto.email

        userService.saveUser(user)
    }

    /**
     * Включает 2х факторную аутентификацию [tfaTurnDto] пользователя [username]
     *
     * @throws UserNotFoundException студент не найден
     * @throws PersistenceException
     */
    @Transactional
    override fun turnTfa(username: String, tfaTurnDto: TfaTurnDto): TfaTurnDto = tfaTurnDto.apply {
        val user = userService.findUserByUsername(username)

        user.tfaEnabled = tfaTurnDto.tfaEnabled

        userService.saveUser(user)
    }

    /**
     * Создание или изменение студента [studentDto]
     *
     * @throws CountryNotFoundException
     * @throws RoleNotFoundException
     * @throws DateTimeParseException
     * @throws StatusNotFoundException
     * @throws PersistenceException
     */
    @Transactional
    override fun put(studentDto: StudentDto) {
        userService.saveUser(dtoService.toUser(studentDto))
    }

    /**
     * Создание нового студента [studentDto]
     *
     * @throws CountryNotFoundException
     * @throws UserAlreadyExistsException
     * @throws RoleNotFoundException
     * @throws DateTimeParseException
     * @throws StatusNotFoundException
     * @throws PersistenceException
     */
    @Transactional
    override fun create(studentDto: StudentDto) {
        if (userService.userExists(studentDto.username))
            throw UserAlreadyExistsException("This student is registered")

        userService.saveUser(dtoService.toUser(studentDto))
    }

    /**
     * Удаляет сущность студента по его id [id]
     *
     * @throws PersistenceException
     */
    @Transactional
    override fun delete(id: String) = userService.deleteUserById(id)
}