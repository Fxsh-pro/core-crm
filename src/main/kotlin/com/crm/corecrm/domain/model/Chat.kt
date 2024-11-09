package com.crm.corecrm.domain.model

data class Chat(
    val id: Int? = null,
    val tgChatId: Int,
    val creatorBy: Int,
    val createdAt: Int,
    val status: ChatStatus,
)