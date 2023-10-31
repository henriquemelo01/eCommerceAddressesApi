package com.example.addressesecommerceservice.dtos.addresses

import com.example.addressesecommerceservice.models.AvailableCountry
import java.util.UUID

data class SavedAddressDto(
        val id: UUID,
        override val postalCode: String,
        override val street: String,
        override val number: Int,
        override val complement: String? = null,
        override val neighborhood: String,
        override val city: String,
        override val uf: String,
        override val country: String = AvailableCountry.BRAZIL.value,
        val isDeliveryAddress: Boolean,
        val isAccountAddress: Boolean
) : AddressDto, Comparable<SavedAddressDto> {
    override fun compareTo(other: SavedAddressDto): Int {
        if ((this.isDeliveryAddress && !other.isDeliveryAddress))
            return -1
        else if (!this.isDeliveryAddress && other.isDeliveryAddress)
            return 1
        else if (this.isAccountAddress && !other.isAccountAddress)
            return 1
        else if (!this.isAccountAddress && other.isAccountAddress)
            return -1

        return 0
    }

}



