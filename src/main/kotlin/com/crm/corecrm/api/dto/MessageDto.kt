package com.crm.corecrm.api.dto

data class MessageDto(
    val id: Long,
    val chatId: Long,
    val createdAt: Long,
    val createdBy: String,
    val text: String,
    val type: MessageTypeDto
)
