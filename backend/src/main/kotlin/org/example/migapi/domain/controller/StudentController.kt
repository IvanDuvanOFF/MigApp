package org.example.migapi.domain.controller

import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.domain.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(
    @Autowired
    private val studentService: StudentService
) {
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAll(): List<StudentDto> = studentService.getAll()

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getById(@PathVariable id: String): StudentDto = studentService.getById(id)

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun put(studentDto: StudentDto): ResponseEntity<Any> =
        ResponseEntity(if (studentService.put(studentDto)) HttpStatus.CREATED else HttpStatus.OK)

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun create(studentDto: StudentDto): ResponseEntity<Any> {
        studentService.create(studentDto)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delete(@RequestParam id: String) = studentService.delete(id)
}