package com.example.addressesecommerceservice.controllers

import com.example.addressesecommerceservice.dtos.users.RegisterUserDto
import com.example.addressesecommerceservice.dtos.users.SignInDto
import com.example.addressesecommerceservice.services.auth.AuthService
import com.example.addressesecommerceservice.utils.mappers.Constants
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping("${Constants.BASE_URL}/auth")
class AuthController(
        private val authService: AuthService,
) {
    @PostMapping("/register")
    fun registerUser(
            @RequestBody @Valid registerUserDto: RegisterUserDto
    ) = ResponseEntity.status(HttpStatus.CREATED).body((authService.registerNewUser(registerUserDto)))

    @PostMapping("/sign-in")
    fun signIn(
            @RequestBody @Valid signInDto: SignInDto
    ) = ResponseEntity.ok(authService.signIn(signInDto))
}