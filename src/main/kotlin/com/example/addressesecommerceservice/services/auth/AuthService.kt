package com.example.addressesecommerceservice.services.auth

import com.example.addressesecommerceservice.dtos.users.RegisterUserDto
import com.example.addressesecommerceservice.dtos.users.SignInDto
import com.example.addressesecommerceservice.models.UserModel
import com.example.addressesecommerceservice.repositories.UserAddressRepository
import com.example.addressesecommerceservice.repositories.UserRepository
import com.example.addressesecommerceservice.services.users.erros.UserNotFoundException
import com.example.addressesecommerceservice.services.jwt.JwtService
import com.example.addressesecommerceservice.utils.mappers.Mapper.toUserAddressModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val userRepository: UserRepository,
        private val userAddressRepository: UserAddressRepository,
        private val jwtService: JwtService
) {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun registerNewUser(registerUserDto: RegisterUserDto): String {

        val (email, password, accountAddress) = registerUserDto
        val encryptedPassword = passwordEncoder.encode(password)

        val createdUser = userRepository.save(
                UserModel(
                        email = email,
                        encryptedPassword = encryptedPassword
                )
        )

        userAddressRepository.save(
                accountAddress.toUserAddressModel(
                        isDeliveryAddress = false,
                        isAccountAddress = true,
                        user = createdUser
                )
        )

        LoggerFactory.getLogger("registerNewUser").info("created user id: ${createdUser.id}")

        return jwtService.generateJwt(createdUser)
    }

    fun signIn(signInDto: SignInDto): String {
        val (email, password) = signInDto

        val foundedUser = userRepository.findByEmail(email) ?: throw UserNotFoundException(email)

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

        LoggerFactory.getLogger("signIn").info("created user id: ${foundedUser.id}")

        return jwtService.generateJwt(foundedUser)
    }


}