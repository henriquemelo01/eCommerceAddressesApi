package com.example.addressesecommerceservice.dtos.users

import com.example.addressesecommerceservice.dtos.addresses.AddNewAddressDto
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern

data class RegisterUserDto(
        @field:Email val email: String,
        @field:Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#\$%&])(?=.*[a-zA-Z0-9]).{9,11}\$") val password: String,
        val accountAddress: AddNewAddressDto,
)