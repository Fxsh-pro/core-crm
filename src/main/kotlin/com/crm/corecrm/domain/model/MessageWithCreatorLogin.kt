package com.crm.corecrm.domain.model

data class MessageWithCreatorLogin (
    val id: Int? = null,
    val chatId: Int,
    val createdAt: Int,
    val createdBy: String,
    val text: String,
    val type: MessageType
)