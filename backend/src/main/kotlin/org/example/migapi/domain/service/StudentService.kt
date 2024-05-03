package org.example.migapi.domain.service

import org.example.migapi.core.domain.dto.StudentDto

interface StudentService {
    fun getAll(): List<StudentDto>

    fun getById(id: String): StudentDto

    fun put(studentDto: StudentDto)

    fun create(studentDto: StudentDto)

    fun delete(id: String)
}