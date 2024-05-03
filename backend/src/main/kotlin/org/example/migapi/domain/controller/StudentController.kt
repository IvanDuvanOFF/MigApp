package org.example.migapi.domain.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
        description = "Администратор запрашивает список всех студентов",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список получен",
                content = [Content(array = ArraySchema(schema = Schema(implementation = StudentDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun getAll(): List<StudentDto> = studentService.getAll()

    @GetMapping("{id}")
    @Operation(
        summary = "Получить конкретного студента по id",
        description = "Администратор запрашивает запись студента по его id",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Студент получен",
                content = [Content(schema = Schema(implementation = StudentDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun getById(@PathVariable id: String): StudentDto = studentService.getById(id)

    @PutMapping
    @Operation(
        summary = "Изменить запись студента",
        description = "Администратор сохраняет отредактированную запись студента",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Запись сохранена",
                content = [Content(schema = Schema(implementation = StudentDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun put(studentDto: StudentDto) = studentService.put(studentDto)

    @PostMapping
    @Operation(
        summary = "Сохранить новую запись студента",
        description = "Администратор создает новую запись студента",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Запись сохранена",
                content = [Content(schema = Schema(implementation = StudentDto::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun create(@RequestBody studentDto: StudentDto) {
        studentService.create(studentDto)
    }

    @DeleteMapping
    @Operation(
        summary = "Удалить запись студента",
        description = "Администратор удаляет запись студента",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Запись удалена",
                content = [Content(schema = Schema(implementation = StudentDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(schema = Schema(implementation = Error::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = Error::class))]
            )
        ]
    )
    fun delete(@RequestParam id: String) = studentService.delete(id)
}