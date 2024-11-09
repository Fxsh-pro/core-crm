package com.crm.corecrm.domain.model

data class ChatFilter (
    val chatIds: List<Int>?,
    val statuses : List<ChatStatus>?,
)