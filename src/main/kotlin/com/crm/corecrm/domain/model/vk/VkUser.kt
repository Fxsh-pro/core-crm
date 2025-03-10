package com.crm.corecrm.domain.model.vk

data class VkUser (
    val userId: Int,
    val name: String,
    val lastName: String,
)

data class VkMessage(
    val messageId: Int,
    val chatId: Int,
    val sender: VkUser,
    val text: String,
    val timestamp: Int
)