package com.crm.corecrm.api.dto

import com.crm.corecrm.domain.model.ChatStatus

data class ChatDto(
    val id: Long,
    val messages : List<MessageDto>,
    val creatorBy : String,
    val createdAt: Long,
    val status: ChatStatus
)
