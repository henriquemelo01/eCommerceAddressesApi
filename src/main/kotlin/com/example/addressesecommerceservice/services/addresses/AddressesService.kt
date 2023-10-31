package com.example.addressesecommerceservice.services.addresses

import com.example.addressesecommerceservice.dtos.addresses.AddNewAddressDto
import com.example.addressesecommerceservice.dtos.addresses.AddressDto
import com.example.addressesecommerceservice.dtos.addresses.SavedAddressDto
import com.example.addressesecommerceservice.models.UserAddressModel
import com.example.addressesecommerceservice.repositories.UserAddressRepository
import com.example.addressesecommerceservice.security.JwtAuthorizationHeaderProvider
import com.example.addressesecommerceservice.services.addresses.erros.AddressAlreadyRegisterException
import com.example.addressesecommerceservice.services.addresses.erros.AddressNotFoundException
import com.example.addressesecommerceservice.services.addresses.erros.CouldNotDeleteAccountAddressException
import com.example.addressesecommerceservice.services.addresses.erros.MaxUserSavedAddressesLimitExceedException
import com.example.addressesecommerceservice.services.users.UserService
import com.example.addressesecommerceservice.services.jwt.JwtService
import com.example.addressesecommerceservice.utils.mappers.Mapper.toSavedAddressDto
import com.example.addressesecommerceservice.utils.mappers.Mapper.toUserAddressModel
import org.springframework.beans.BeanUtils
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AddressesService(
        private val applicationContext: GenericApplicationContext,
        private val jwtService: JwtService,
        private val userService: UserService,
        private val userAddressRepository: UserAddressRepository,
) {

    private val jwtToken get() = applicationContext.getBean(JwtAuthorizationHeaderProvider::class.java).value

    fun getUserAddresses(): List<SavedAddressDto> {
        val username = jwtService.extractUsernameFromJwt(jwtToken)
        val user = userService.loadUserByUsername(username)
        return user.addresses.map { it.toSavedAddressDto() }.sorted()
    }

    fun addNewAddress(addressDto: AddNewAddressDto): SavedAddressDto {
        val username = jwtService.extractUsernameFromJwt(jwtToken)
        val user = userService.loadUserByUsername(username)

        val savedAddresses = user.addresses

        if (savedAddresses.size == MAX_USER_ADDRESSES_ALLOWED_QUANTITY)
            throw MaxUserSavedAddressesLimitExceedException(MAX_USER_ADDRESSES_ALLOWED_QUANTITY)

        if (savedAddresses contains addressDto)
            throw AddressAlreadyRegisterException()

        savedAddresses.find { it.isDeliveryAddress }?.let { currentDelivery ->
            userAddressRepository.save(currentDelivery.apply { isDeliveryAddress = false })
        }

        val savedUserAddress = userAddressRepository.save(
                addressDto.toUserAddressModel(
                        isDeliveryAddress = true,
                        user = user,
                        isAccountAddress = false,
                )
        )

        return savedUserAddress.toSavedAddressDto()
    }

    fun deleteAddress(addressId: UUID) {
        val username = jwtService.extractUsernameFromJwt(jwtToken)
        val user = userService.loadUserByUsername(username)

        val foundedAddress = user.addresses.find { it.id == addressId } ?: throw AddressNotFoundException()

        if (foundedAddress.isAccountAddress)
            throw CouldNotDeleteAccountAddressException()

        userAddressRepository.deleteById(foundedAddress.id)
    }

    fun selectDeliveryAddress(addressId: UUID) {
        val username = jwtService.extractUsernameFromJwt(jwtToken)
        val user = userService.loadUserByUsername(username)
        val savedAddresses = user.addresses

        val foundedAddress = savedAddresses.find { it.id == addressId } ?: throw AddressNotFoundException()

        if (foundedAddress.isDeliveryAddress)
            return

        savedAddresses.find { it.isDeliveryAddress }?.let { currentDeliveryAddress ->
            userAddressRepository.save(currentDeliveryAddress.apply { isDeliveryAddress = false })
        }

        userAddressRepository.save(foundedAddress.apply { isDeliveryAddress = true })
    }

    fun editAddress(addressId: UUID, addressDto: AddNewAddressDto) {
        val username = jwtService.extractUsernameFromJwt(jwtToken)
        val user = userService.loadUserByUsername(username)

        val savedAddresses = user.addresses

        val foundedAddress = savedAddresses.find { it.id == addressId }
                ?: throw AddressNotFoundException()

        if (savedAddresses contains addressDto)
            throw AddressAlreadyRegisterException()

        val updatedAddress = foundedAddress.copyPropertiesFromSavedAddressDto(addressDto)

        userAddressRepository.save(updatedAddress)
    }

    private fun UserAddressModel.copyPropertiesFromSavedAddressDto(savedAddressDto: AddressDto): UserAddressModel {
        BeanUtils.copyProperties(savedAddressDto, this)
        return this
    }

    private infix fun List<UserAddressModel>.contains(addressDto: AddressDto) = this.any {
        it.postalCode == addressDto.postalCode && it.number == addressDto.number && it.complement == addressDto.complement
    }

    companion object {
        const val MAX_USER_ADDRESSES_ALLOWED_QUANTITY = 10
    }
}