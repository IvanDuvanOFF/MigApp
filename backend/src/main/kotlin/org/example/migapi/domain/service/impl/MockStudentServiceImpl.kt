package org.example.migapi.domain.service.impl

import org.example.migapi.auth.exception.UserAlreadyExistsException
import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.core.domain.exception.UserNotFoundException
import org.example.migapi.domain.service.StudentService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class MockStudentServiceImpl : StudentService {

    private val students = mutableListOf(
        StudentDto(
            id = UUID.randomUUID().toString(),
            username = "student1",
            email = "student1@mail.com",
            password = "password",
            name = "Name1",
            surname = "Surname1",
            phone = "8(888)888-88-88",
            country = "India",
            birthday = LocalDate.parse("2002-02-02").toString(),
            status = "NONE"
        ),
        StudentDto(
            id = UUID.randomUUID().toString(),
            username = "student2",
            email = "student2@mail.com",
            password = "password",
            name = "Name2",
            surname = "Surname2",
            patronymic = "Patronymic2",
            phone = "8(888)888-88-88",
            country = "India",
            birthday = LocalDate.parse("2002-01-02").toString(),
            status = "NONE"
        ),
        StudentDto(
            id = UUID.randomUUID().toString(),
            username = "student3",
            email = "student3@mail.com",
            password = "password",
            name = "Name3",
            surname = "Surname3",
            patronymic = "Patronymic3",
            phone = "8(888)888-88-88",
            country = "India",
            birthday = LocalDate.parse("2002-01-02").toString(),
            status = "NONE"
        ),
        StudentDto(
            id = UUID.randomUUID().toString(),
            username = "student4",
            email = "student4@mail.com",
            password = "password",
            name = "Name4",
            surname = "Surname4",
            patronymic = "Patronymic4",
            phone = "8(888)888-88-88",
            country = "India",
            birthday = LocalDate.parse("2002-01-02").toString(),
            status = "NONE"
        ),
        StudentDto(
            id = UUID.randomUUID().toString(),
            username = "student5",
            email = "student5@mail.com",
            password = "password",
            name = "Name5",
            surname = "Surname5",
            patronymic = "Patronymic5",
            phone = "8(888)888-88-88",
            country = "India",
            birthday = LocalDate.parse("2002-01-02").toString(),
            status = "NONE"
        )
    )

    override fun getAll(): List<StudentDto> = ArrayList(students)

    override fun getById(id: String): StudentDto =
        students.firstOrNull { it.id == id } ?: throw UserNotFoundException("This student doesn't exist")

    override fun put(studentDto: StudentDto): Boolean = students.add(studentDto)

    override fun create(studentDto: StudentDto) {
        if (students.any { it.id == studentDto.id })
            throw UserAlreadyExistsException("This student already exists")

        students.add(studentDto)
    }

    override fun delete(id: String) {
        if (!students.removeIf { it.id == id }) throw UserNotFoundException("")
    }
}