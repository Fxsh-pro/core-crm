package com.crm.corecrm.domain.model

data class Customer(
    val id: Int? = null,
    val tgId : Int,
    val firstName : String,
    val lastName : String,
    val userName : String,
)
