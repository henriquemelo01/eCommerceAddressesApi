package com.example.addressesecommerceservice.services.addresses.erros

class MaxUserSavedAddressesLimitExceedException(limit: Int) : Exception(
        "A quantidade máxima de $limit endereços salvos por usuário foi excedida"
)