package com.example.addressesecommerceservice.services.commons.erros

class EnvPropertyNotFoundException(propertyName: String) : Exception("Propriedade $propertyName não localizada")