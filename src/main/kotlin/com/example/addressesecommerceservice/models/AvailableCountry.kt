package com.example.addressesecommerceservice.models

import javax.persistence.AttributeConverter

enum class AvailableCountry(val value: String) {
    BRAZIL("Brasil");

    companion object {
        fun getByValue(value: String?) = values().find { it.value == value } ?: BRAZIL
    }
}

class AvailableCountryConverter : AttributeConverter<AvailableCountry, String> {

    override fun convertToDatabaseColumn(
            attribute: AvailableCountry?
    ) = attribute?.value ?: AvailableCountry.BRAZIL.value

    override fun convertToEntityAttribute(dbData: String?) = AvailableCountry.getByValue(dbData)
}