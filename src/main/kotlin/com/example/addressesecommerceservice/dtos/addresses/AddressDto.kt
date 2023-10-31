package com.example.addressesecommerceservice.dtos.addresses

interface AddressDto {
    val postalCode: String
    val number: Int
    val street: String
    val complement: String?
    val neighborhood: String
    val city: String
    val uf: String
    val country: String
}