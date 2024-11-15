package com.crm.corecrm.domain.model

data class Message(
    val id: Int? = null,
    val chatId: Int,
    val createdAt: Int,
    val createdBy: Int,
    val text: String,
    val type: MessageType
)