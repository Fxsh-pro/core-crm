package com.crm.corecrm.domain.model

data class Customer(
    val id: Int? = null,
    val channelId : Long,
    val channelType: ChannelType,
    val firstName : String,
    val lastName : String,
    val userName : String,
)