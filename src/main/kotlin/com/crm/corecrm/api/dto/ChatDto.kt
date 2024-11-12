package com.crm.corecrm.api.dto

import com.crm.corecrm.api.dto.message.MessageDto

data class ChatDto(
    val id: Int?,
    val messages: List<MessageDto>,
    val creatorBy: String,
    val createdAt: Int,
    val status: ChatStatusDto
)
