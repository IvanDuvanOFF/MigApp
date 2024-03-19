package org.example.migapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/index")
class IndexController {
    @GetMapping
    fun index() = "index"

    @GetMapping("hello")
    fun hello() = "hello"
}