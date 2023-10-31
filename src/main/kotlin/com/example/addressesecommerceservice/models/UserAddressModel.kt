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

        var neighborhood: String,

        var city: String,

        var complement: String? = null,

        var uf: String,

        @Convert(converter = AvailableCountryConverter::class)
        @Column(name = "country")
        var availableCountry: AvailableCountry = AvailableCountry.BRAZIL,

        @JsonIgnore // TODO - Será removido e utilizado no DTO posteriormente
        @JoinColumn(name = "userId")
        @ManyToOne
        var user: UserModel? = null,

        var isAccountAddress: Boolean = false,

        var isDeliveryAddress: Boolean = false,
) {
        override fun toString(): String {
                return "UserAddressModel(id=$id, postalCode='$postalCode', number=$number, street='$street', neighborhood='$neighborhood', city='$city', complement=$complement, uf='$uf', availableCountry=$availableCountry, user=$user, isAccountAddress=$isAccountAddress, isDeliveryAddress=$isDeliveryAddress)"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UserAddressModel

                if (id != other.id) return false
                if (postalCode != other.postalCode) return false
                if (number != other.number) return false
                if (street != other.street) return false
                if (neighborhood != other.neighborhood) return false
                if (city != other.city) return false
                if (complement != other.complement) return false
                if (uf != other.uf) return false
                if (availableCountry != other.availableCountry) return false
                if (user != other.user) return false
                if (isAccountAddress != other.isAccountAddress) return false
                if (isDeliveryAddress != other.isDeliveryAddress) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id.hashCode()
                result = 31 * result + postalCode.hashCode()
                result = 31 * result + number
                result = 31 * result + street.hashCode()
                result = 31 * result + neighborhood.hashCode()
                result = 31 * result + city.hashCode()
                result = 31 * result + (complement?.hashCode() ?: 0)
                result = 31 * result + uf.hashCode()
                result = 31 * result + availableCountry.hashCode()
                result = 31 * result + (user?.hashCode() ?: 0)
                result = 31 * result + isAccountAddress.hashCode()
                result = 31 * result + isDeliveryAddress.hashCode()
                return result
        }


}