package com.crm.corecrm.domain.model

data class Chat(
    val id: Int? = null,
    val channelId: Long,
    val channelType: ChannelType,
    val creatorBy: Int,
    val createdAt: Int,
    val status: ChatStatus,
)