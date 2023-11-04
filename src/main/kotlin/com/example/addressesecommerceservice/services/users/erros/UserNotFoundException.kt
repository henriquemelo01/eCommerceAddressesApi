package com.example.addressesecommerceservice.services.users.erros

import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserNotFoundException(userId: String) : UsernameNotFoundException("Não foi possível localizar o usuário $userId")
