package com.crm.corecrm.domain.model

data class Chat(
    val id: Long? = null,
    val tgChatId: Long,
    val creatorBy: Long,
    val createdAt: Long,
    val status: ChatStatus,
)