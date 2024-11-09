package com.crm.corecrm.api.dto

data class MessageDto(
    val id: Int?,
    val chatId: Int,
    val createdAt: Int?,
    val createdBy: String?,
    val text: String,
    val type: MessageTypeDto?
)
