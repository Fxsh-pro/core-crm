package com.crm.corecrm.api.dto.message

data class CreateMessageRequest(
    val chatId: Int,
    val text: String,
)