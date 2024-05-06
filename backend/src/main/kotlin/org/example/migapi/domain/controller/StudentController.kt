package org.example.migapi.domain.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.example.migapi.core.config.open.api.CommonApiErrors
import org.example.migapi.core.domain.dto.Error
import org.example.migapi.core.domain.dto.StudentDto
import org.example.migapi.domain.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/students")
class StudentController(
    @Autowired
    private val studentService: StudentService
) {

    @GetMapping
    @Operation(
        summary = "Получить список всех студентов",
        description = "Администратор запрашивает список всех студентов"
    )
    @ApiResponse(responseCode = "200", description = "Получен список студентов")
    @CommonApiErrors
    @SecurityRequirement(name = "JWT")
    fun getAll(): List<StudentDto> = studentService.getAll()

    @GetMapping("{id}")
    @Operation(
        summary = "Получить конкретного студента по id",
        description = "Администратор запрашивает запись студента по его id"
    )
    @ApiResponse(responseCode = "200", description = "Студент получен")
    @ApiResponse(
        responseCode = "404",
        description = "Студент не найден",
        content = [Content(schema = Schema(implementation = Error::class))]
    )
    @CommonApiErrors
    @SecurityRequirement(name = "JWT")
    fun getById(@PathVariable id: String): StudentDto = studentService.getById(id)

    @PutMapping
    @Operation(
        summary = "Изменить запись студента",
        description = "Администратор сохраняет отредактированную запись студента"
    )
    @ApiResponse(responseCode = "200", description = "Запись изменена или сохранена")
    @ApiResponse(
        responseCode = "404",
        description = "Студент не найден",
        content = [Content(schema = Schema(implementation = Error::class))]
    )
    @CommonApiErrors
    @SecurityRequirement(name = "JWT")
    fun put(studentDto: StudentDto) = studentService.put(studentDto)

    @PostMapping
    @Operation(summary = "Сохранить новую запись студента", description = "Администратор создает новую запись студента")
    @ApiResponse(responseCode = "200", description = "Запись сохранена")
    @ApiResponse(
        responseCode = "404",
        description = "Студент не найден",
        content = [Content(schema = Schema(implementation = Error::class))]
    )
    @CommonApiErrors
    @SecurityRequirement(name = "JWT")
    fun create(@RequestBody studentDto: StudentDto) {
        studentService.create(studentDto)
    }

    @DeleteMapping
    @Operation(summary = "Удалить запись студента", description = "Администратор удаляет запись студента")
    @CommonApiErrors
    @ApiResponse(responseCode = "200", description = "Запись удалена")
    @ApiResponse(
        responseCode = "404",
        description = "Студент не найден",
        content = [Content(schema = Schema(implementation = Error::class))]
    )
    @SecurityRequirement(name = "JWT")
    fun delete(@RequestParam id: String) = studentService.delete(id)
}