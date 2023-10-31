package com.example.addressesecommerceservice.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "users")
class UserModel(
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name ="userId")
        var id: UUID = UUID.randomUUID(),

        @Column(unique = true)
        var email: String,

        @JsonIgnore // TODO - Será removido e utilizado no DTO posteriormente
        var encryptedPassword: String,

        @OneToMany(mappedBy = "user")
        var addresses: MutableList<UserAddressModel> = mutableListOf()
): UserDetails {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

        override fun getPassword() = encryptedPassword

        override fun getUsername() = email

        override fun isAccountNonExpired() = true

        override fun isAccountNonLocked() = true

        override fun isCredentialsNonExpired() = true

        override fun isEnabled() = true

        ///////////////////////////
        override fun toString(): String {
                return "UserModel(id=$id, email='$email', addresses=$addresses)"
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UserModel

                if (id != other.id) return false
                if (email != other.email) return false
                if (encryptedPassword != other.encryptedPassword) return false
                if (addresses != other.addresses) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id.hashCode()
                result = 31 * result + email.hashCode()
                result = 31 * result + encryptedPassword.hashCode()
                result = 31 * result + addresses.hashCode()
                return result
        }
}
