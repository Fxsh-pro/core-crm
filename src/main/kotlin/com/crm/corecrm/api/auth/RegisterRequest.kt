package com.crm.corecrm.api.auth

data class RegisterRequest(
    val id: Int?,
    val login: String,
    val name: String,
    val password: String,
)