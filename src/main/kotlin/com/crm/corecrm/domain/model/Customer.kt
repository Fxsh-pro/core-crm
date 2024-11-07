package com.crm.corecrm.domain.model

data class Customer(
    val id: Long?,
    val tgId : Long,
    val firstName : String,
    val lastName : String,
    val userName : String,
)
