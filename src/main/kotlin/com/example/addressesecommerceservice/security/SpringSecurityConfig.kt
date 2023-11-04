package com.example.addressesecommerceservice.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SpringSecurityDependenciesContainer {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager = configuration.authenticationManager

    @Bean
    fun authenticationProvider(
            passwordEncoder: PasswordEncoder,
            userDetailsService: UserDetailsService
    ) = DaoAuthenticationProvider().apply {
        setPasswordEncoder(passwordEncoder)
        setUserDetailsService(userDetailsService)
    }
}

@Configuration
@EnableWebSecurity
class SpringSecurityConfig(
        private val authenticationProvider: AuthenticationProvider,
        private val jwtAuthFilter: JwtAuthFilter,
) {
    @Bean
    fun securityFilterChain(
            http: HttpSecurity
    ): SecurityFilterChain = http
            .httpBasic()
            .and()
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.antMatchers("/ecommerce-address-api/v1/auth/**").permitAll()
                it.antMatchers("/ping").permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authenticationProvider(authenticationProvider)
            .build()
}