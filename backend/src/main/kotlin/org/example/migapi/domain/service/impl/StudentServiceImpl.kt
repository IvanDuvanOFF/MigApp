package org.example.migapi.domain.service.impl

import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.core.domain.model.entity.Role
import org.example.migapi.core.domain.model.enums.ERole
import org.example.migapi.core.domain.repo.UserRepository
import org.example.migapi.core.domain.service.DtoService
import org.example.migapi.domain.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentServiceImpl(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val dtoService: DtoService
) : StudentService {

    //    persistence
    override fun getAll(): List<StudentDto> =
        userRepository.findUsersByRole(Role(ERole.ROLE_USER)).map { dtoService.userToStudentDto(it) }

    //    persistence, IllegalArgument, UserNotFound
    override fun getById(id: String): StudentDto {
        val student = userRepository.findById(UUID.fromString(id))
            .orElseThrow { UserNotFoundException("Student with id: $id not found") }

        return dtoService.userToStudentDto(student)
    }

//
    override fun put(studentDto: StudentDto): Boolean {
        val student = userRepository.findUserByUsername(studentDto.username)

        val user = dtoService.studentDtoToUser(studentDto)

        return false
    }

    override fun create(studentDto: StudentDto) {
        TODO("Not yet implemented")
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }
}