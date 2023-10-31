package com.example.addressesecommerceservice.dtos

data class GenericErrorDto(
        val statusCode: Int,
        val errorMessage: String = GENERIC_ERROR_MESSAGE
) {

    constructor(statusCode: Int, errorMessage: String?, outroParametro: String = "") : this(
            statusCode = statusCode,
            errorMessage = errorMessage ?: GENERIC_ERROR_MESSAGE
    )

    companion object {
        const val GENERIC_ERROR_MESSAGE = "Houve um erro por aqui. Tente novamnete mais tarde"
    }
}
