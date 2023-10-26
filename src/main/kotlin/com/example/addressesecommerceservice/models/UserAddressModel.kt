package com.example.addressesecommerceservice.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "user_address")
class UserAddressModel(
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "addressId")
        var id: UUID = UUID.randomUUID(),

        var postalCode: String,

        var number: Int,

        var street: String,

        var city: String,

        @Convert(converter = AvailableCountryConverter::class)
        @Column(name = "country")
        var availableCountry: AvailableCountry = AvailableCountry.BRAZIL,

        @JsonIgnore // TODO - Será removido e utilizado no DTO posteriormente
        @JoinColumn(name = "userId")
        @ManyToOne
        var user: UserModel
)