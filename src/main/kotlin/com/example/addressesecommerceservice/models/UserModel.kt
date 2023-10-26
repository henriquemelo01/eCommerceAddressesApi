package com.example.addressesecommerceservice.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
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
        var addresses: MutableList<UserAddressModel>
)
