package org.example.migapi.domain.account.service

import jakarta.transaction.Transactional
import org.example.migapi.auth.exception.BadCredentialsException
import org.example.migapi.auth.exception.UnauthorizedException
import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.config.exception.BadRequestException
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.domain.account.dto.*
import org.example.migapi.domain.files.model.File
import org.example.migapi.utils.MigUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

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

    override fun getAll(): List<StudentDto> {
        val users = userService.findUsersByRole(ERole.ROLE_USER)

        return users.map { dtoService.userToStudentDto(it) }
    }

    override fun getById(id: String): StudentDto = dtoService.userToStudentDto(userService.findById(id))

    override fun getByUsername(username: String): StudentDto {
        val user = userService.findUserByUsername(username)

        return dtoService.userToStudentDto(user)
    }

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

    override fun getByUsernameAndId(username: String, id: String): StudentDto =
        dtoService.userToStudentDto(userService.findUserByUsernameAndId(username, id))

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

    @Transactional
    override fun changePhone(username: String, phoneDto: PhoneDto) {
        val user = userService.findUserByUsername(username)

        if (!migUtils.validatePhone(phoneDto.phone))
            throw BadRequestException("Phone doesn't valid")

        user.phone = phoneDto.phone

        userService.saveUser(user)
    }

    @Transactional
    override fun changeEmail(username: String, emailDto: EmailDto) {
        val user = userService.findUserByUsername(username)

        if (!migUtils.validateEmail(emailDto.email))
            throw BadRequestException("Email doesn't valid")

        user.email = emailDto.email

        userService.saveUser(user)
    }

    @Transactional
    override fun turnTfa(username: String, tfaTurnDto: TfaTurnDto): TfaTurnDto = tfaTurnDto.apply {
        val user = userService.findUserByUsername(username)

        user.tfaEnabled = tfaTurnDto.tfaEnabled

        userService.saveUser(user)
    }

    @Transactional
    override fun put(studentDto: StudentDto) {
        userService.saveUser(dtoService.toUser(studentDto))
    }

    @Transactional
    override fun create(studentDto: StudentDto) {
        if (userService.userExists(studentDto.username))
            throw UserAlreadyExistsException("This student is registered")

        userService.saveUser(dtoService.toUser(studentDto))
    }

    @Transactional
    override fun delete(id: String) = userService.deleteUserById(id)
}