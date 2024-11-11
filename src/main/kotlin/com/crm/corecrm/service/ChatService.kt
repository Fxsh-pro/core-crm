package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatFilter
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.ChatWithMessages
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.domain.model.MessageWithCreatorLogin
import com.crm.corecrm.domain.repository.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val messageService: MessageService,
    private val customerService: CustomerService,
    private val currentUserService: CurrentUserService,
) {

    fun getIdOrCreate(chat: Chat): Int {
        return chatRepository.getIdOrCreate(chat)
    }

    fun getById(chatId: Int): Chat {
        return chatRepository.getById(chatId)
    }

    fun close(chatId: Int) {
        chatRepository.setNewStatus(chatId, ChatStatus.CLOSE)
    }


    fun getChatByFilter(filter : ChatFilter): List<ChatWithMessages> {
        val operator = currentUserService.getCurrentOperator()
        val operatorId = operator.getId()!!

        val chats = chatRepository.getChatsByOperatorAndChatIds(operatorId, filter.chatIds, filter.statuses)
        if (chats.isEmpty()) return emptyList()

        val chatIds = chats.mapNotNull { it.id }
        val messages = messageService.getMessagesByChatIds(chatIds)

        val customerIds = messages.values.flatten()
            .filter { it.type == MessageType.IN }
            .map { it.createdBy }
        val customersById = customerService.getCustomersByIds(customerIds)

        return chats.mapNotNull { chat ->
            val chatMessages = messages[chat.id] ?: return@mapNotNull null

            val messagesWithCreatorLogin = chatMessages.map { message ->
                val customerInfo = customersById[message.createdBy]?.firstOrNull()?.let { customer ->
                    "${customer.firstName} ${customer.lastName} ${customer.userName}"
                } ?: operator.username

                MessageWithCreatorLogin(
                    id = message.id,
                    chatId = message.chatId,
                    createdAt = message.createdAt,
                    createdBy = customerInfo,
                    text = message.text,
                    type = message.type
                )
            }.sortedByDescending { it.createdAt }

            val createdBy = customersById[chat.creatorBy]?.firstOrNull()?.let { customer ->
                "${customer.firstName} ${customer.lastName} ${customer.userName}"
            } ?: "Unknown"

            ChatWithMessages(
                id = chat.id,
                messages = messagesWithCreatorLogin,
                createdBy = createdBy,
                createdAt = chat.createdAt,
                status = chat.status
            )
        }.sortedBy { it.createdAt }
    }
}