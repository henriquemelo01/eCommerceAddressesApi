package com.example.addressesecommerceservice.services.users

import com.example.addressesecommerceservice.repositories.UserRepository
import com.example.addressesecommerceservice.services.users.erros.InvalidUsernameException
import com.example.addressesecommerceservice.services.users.erros.UserNotFoundException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String?) = username
            ?.let { userRepository.findByEmail(username) ?: throw UserNotFoundException(username) }
            ?: throw InvalidUsernameException()
}