package com.crm.corecrm.domain.model

data class ChatWithMessages(
    val id: Int?,
    val messages : List<MessageWithCreatorLogin>,
    val createdBy : String,
    val createdAt: Int,
    val status: ChatStatus
)