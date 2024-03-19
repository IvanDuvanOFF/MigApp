package org.example.migapi.controller

import org.example.migapi.domain.dto.data.AdminDto
import org.example.migapi.domain.dto.data.StudentDto
import org.example.migapi.domain.service.data.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/signup")
class SignUpController(
    @Autowired
    private val userService: UserService
) {
    @PostMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addAdmin(adminDto: AdminDto): ResponseEntity<*> {
        userService.createUser(adminDto)

        return ResponseEntity(null, HttpStatus.CREATED)
    }

    @PostMapping("student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addStudent(studentDto: StudentDto): ResponseEntity<*> {
        userService.createUser(studentDto)

        return ResponseEntity(null, HttpStatus.CREATED)
    }
}