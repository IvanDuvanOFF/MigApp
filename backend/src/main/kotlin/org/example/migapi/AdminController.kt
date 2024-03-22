package org.example.migapi

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin-test")
class AdminController {
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    @Operation(
        summary = "Тестовый контроллер для админа",
        description = "Просто проверочка"
    )
    fun index() = "admin conrtoller"
}