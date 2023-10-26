package com.example.addressesecommerceservice.repositories

import com.example.addressesecommerceservice.models.UserAddressModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserAddressRepository: JpaRepository<UserAddressModel, UUID>