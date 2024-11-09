package com.crm.corecrm.api.dto

data class ChatRequestFilter(
    val chatIds: List<Int>?,
    val statuses : List<ChatStatusDto>?,
)
