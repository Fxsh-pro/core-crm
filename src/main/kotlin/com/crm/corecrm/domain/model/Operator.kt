package com.crm.corecrm.domain.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
// TODO Дата классы котлина криво работают при настледовании UserDetails. Надо попровить
data class Operator(
    private var id: Int?,
    private val login: String,
    private val password: String,
    private val name: String,
) : UserDetails {
    fun getId(): Int? = id
    fun getName(): String = name

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String = password
    override fun getUsername(): String = login
}
