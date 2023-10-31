package com.example.addressesecommerceservice.utils.mappers

import com.example.addressesecommerceservice.dtos.addresses.AddNewAddressDto
import com.example.addressesecommerceservice.dtos.addresses.SavedAddressDto
import com.example.addressesecommerceservice.models.AvailableCountry
import com.example.addressesecommerceservice.models.UserAddressModel
import com.example.addressesecommerceservice.models.UserModel

object Mapper {

    fun AddNewAddressDto.toUserAddressModel(
            isDeliveryAddress: Boolean,
            isAccountAddress: Boolean = false,
            user: UserModel? = null
    ) = UserAddressModel(
            postalCode = postalCode,
            street = street,
            number = number,
            city = city,
            complement = complement,
            neighborhood = neighborhood,
            uf = uf,
            availableCountry = AvailableCountry.getByValue(country),
            isDeliveryAddress = isDeliveryAddress,
            isAccountAddress = isAccountAddress,
            user = user,
    )

    fun UserAddressModel.toSavedAddressDto() = SavedAddressDto(
            id = id,
            street = street,
            complement = complement,
            number = number,
            city = city,
            country = availableCountry.value,
            neighborhood = neighborhood,
            uf = uf,
            isDeliveryAddress = isDeliveryAddress,
            isAccountAddress = isAccountAddress,
            postalCode = postalCode
    )
}