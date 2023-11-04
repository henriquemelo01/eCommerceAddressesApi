package com.example.addressesecommerceservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PingController {

    @GetMapping("/ping")
    fun ping() = ResponseEntity.ok("Pong 🧙‍♂️🔥")

    @GetMapping("/hello-world")
    fun helloWold() = ResponseEntity.ok("Hello world")
}