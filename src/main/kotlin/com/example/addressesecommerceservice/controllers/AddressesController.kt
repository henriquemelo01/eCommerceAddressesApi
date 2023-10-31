package com.example.addressesecommerceservice.controllers

import com.example.addressesecommerceservice.dtos.addresses.AddNewAddressDto
import com.example.addressesecommerceservice.services.addresses.AddressesService
import com.example.addressesecommerceservice.utils.mappers.Constants
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID
import javax.validation.Valid

@Controller
@RequestMapping("${Constants.BASE_URL}/addresses")
class AddressesController(
        private val addressesService: AddressesService
) {
    @GetMapping
    fun getUserAddresses() = ResponseEntity.ok(addressesService.getUserAddresses())

    @PostMapping
    fun addNewAddress(
            @RequestBody @Valid addressDto: AddNewAddressDto
    ) = ResponseEntity.status(HttpStatus.CREATED).body(
            addressesService.addNewAddress(addressDto)
    )

    @PutMapping("/{addressId}")
    fun editAddress(
            @PathVariable addressId: UUID,
            @RequestBody @Valid addressDto: AddNewAddressDto
    ): ResponseEntity<Unit> {
        addressesService.editAddress(addressId = addressId, addressDto = addressDto)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{addressId}/select")
    fun selectDeliveryAddress(@PathVariable addressId: UUID): ResponseEntity<Unit> {
        addressesService.selectDeliveryAddress(addressId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{addressId}")
    fun deleteAddress(@PathVariable addressId: UUID): ResponseEntity<Unit> {
        addressesService.deleteAddress(addressId)
        return ResponseEntity.noContent().build()
    }
}