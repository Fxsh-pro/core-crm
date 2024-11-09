package com.crm.corecrm.api.dto

data class ChatDto(
    val id: Int?,
    val messages : List<MessageDto>,
    val creatorBy : String,
    val createdAt: Int,
    val status: ChatStatusDto
)
