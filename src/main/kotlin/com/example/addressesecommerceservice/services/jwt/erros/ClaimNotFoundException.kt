package com.example.addressesecommerceservice.services.jwt.erros

class ClaimNotFoundException(claimKey: String) : Exception("$claimKey não enviada no JWT")