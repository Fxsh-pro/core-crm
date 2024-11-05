package com.crm.corecrm.domain.model

data class Message(
    val id: Int? = null,
    val dialogId: Long,
    val createdAt: Long,
    val createdBy: Long,
    val text: String,
    val type: MessageType
)