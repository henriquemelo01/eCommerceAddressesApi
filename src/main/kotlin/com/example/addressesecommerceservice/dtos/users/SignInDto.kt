package com.example.addressesecommerceservice.dtos.users

import javax.validation.constraints.Email

data class SignInDto(
        @field:Email val email: String,
        val password: String
)
