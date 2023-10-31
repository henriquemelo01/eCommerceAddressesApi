package com.example.addressesecommerceservice.dtos.addresses

import com.example.addressesecommerceservice.models.AvailableCountry
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class AddNewAddressDto(
        @field:Pattern(regexp = "\\d{8}") override val postalCode: String,
        override val number: Int,
        override val street: String,
        override val complement: String? = null,
        override val neighborhood: String,
        override val city: String,
        @field:Size(min = 2, max = 2) override val uf: String,
        override val country: String = AvailableCountry.BRAZIL.value
) : AddressDto

