package org.example.migapi.domain.account.service

import jakarta.transaction.Transactional
import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.domain.account.dto.StudentDto
import org.example.migapi.domain.account.dto.TfaTurnDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentServiceImpl(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val dtoService: DtoService
) : StudentService {

    override fun getAll(): List<StudentDto> {
        val users = userService.findUsersByRole(ERole.ROLE_USER)

        return users.map { dtoService.userToStudentDto(it) }
    }

    override fun getById(id: String): StudentDto = dtoService.userToStudentDto(userService.findById(id))

    override fun getByUsername(username: String): StudentDto =
        dtoService.userToStudentDto(userService.findUserByUsername(username))

    override fun getByUsernameAndId(username: String, id: String): StudentDto =
        dtoService.userToStudentDto(userService.findUserByUsernameAndId(username, id))

    @Transactional
    override fun updatePassword(username: String, password: String) {
        val user = userService.findUserByUsername(username)

        user.password = password

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