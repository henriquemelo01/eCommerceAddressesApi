package com.example.addressesecommerceservice.controllers

import com.example.addressesecommerceservice.dtos.GenericErrorDto
import com.example.addressesecommerceservice.services.addresses.erros.AddressAlreadyRegisterException
import com.example.addressesecommerceservice.services.addresses.erros.AddressNotFoundException
import com.example.addressesecommerceservice.services.addresses.erros.CouldNotDeleteAccountAddressException
import com.example.addressesecommerceservice.services.addresses.erros.MaxUserSavedAddressesLimitExceedException
import com.example.addressesecommerceservice.services.users.erros.InvalidUsernameException
import com.example.addressesecommerceservice.services.users.erros.UserNotFoundException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class AppControllerAdvice: ResponseEntityExceptionHandler() {
    @ExceptionHandler
    fun genericExceptionHandler(exception: Exception): ResponseEntity<GenericErrorDto> {
        LoggerFactory.getLogger("GenericExceptionLog").error(exception.message, exception)

        return ResponseEntity
                .internalServerError()
                .body(
                        GenericErrorDto(
                                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                errorMessage = "Houve um erro por aqui. Tente novamente mais tarde"
                        )
                )
    }

    @ExceptionHandler
    fun userNotFoundExceptionHandler(exception: UserNotFoundException) = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            GenericErrorDto(
                    errorMessage = exception.message,
                    statusCode = HttpStatus.NOT_FOUND.value()
            )
    )

    @ExceptionHandler
    fun invalidUsernameExceptionHandler(
            exception: InvalidUsernameException
    ) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GenericErrorDto(
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                    errorMessage = exception.message
            )
    )

    @ExceptionHandler
    fun addressNotFoundExceptionHandler(
            exception: AddressNotFoundException
    ) = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            GenericErrorDto(
                    errorMessage = exception.message,
                    statusCode = HttpStatus.NOT_FOUND.value()
            )
    )

    @ExceptionHandler
    fun addressAlreadyRegisterExceptionHandler(
            exception: AddressAlreadyRegisterException
    ) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GenericErrorDto(
                    errorMessage = exception.message,
                    statusCode = HttpStatus.NOT_FOUND.value()
            )
    )

    @ExceptionHandler
    fun couldNotDeleteAccountAddressExceptionHandler(
            exception: CouldNotDeleteAccountAddressException
    ) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GenericErrorDto(
                    errorMessage = exception.message,
                    statusCode = HttpStatus.BAD_REQUEST.value()
            )
    )

    @ExceptionHandler
    fun maxUserSavedAddressesLimitExceedException(
            exception: MaxUserSavedAddressesLimitExceedException
    ) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            GenericErrorDto(
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                    errorMessage = exception.message
            )
    )

    @ExceptionHandler
    fun badCredentialsExceptionHandler(
            exception: BadCredentialsException
    ) = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            GenericErrorDto(
                    statusCode = HttpStatus.UNAUTHORIZED.value(),
                    errorMessage = "Usuário ou senha inválidos"
            )
    )

    @ExceptionHandler
    fun expiredJwtExceptionHandler(exception: ExpiredJwtException) = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            GenericErrorDto(
                    statusCode = HttpStatus.UNAUTHORIZED.value(),
                    errorMessage = "Token expirado. Faça login novamente",
            )
    )

    @ExceptionHandler
    fun jwtSecurityExceptionHandler(exception: SecurityException) = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            GenericErrorDto(
                    statusCode = HttpStatus.UNAUTHORIZED.value(),
                    errorMessage = "Token inválido"
            )
    )
}