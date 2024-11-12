package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatFilter
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.ChatWithMessages
import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.domain.model.MessageWithCreatorLogin
import com.crm.corecrm.domain.model.Operator
import com.crm.corecrm.domain.repository.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val messageService: MessageService,
    private val customerService: CustomerService,
    private val currentUserService: CurrentUserService,
    private val operatorService: OperatorService,
) {

    fun getIdOrCreate(chat: Chat): Int {
        return chatRepository.getIdOrCreate(chat)
    }

    fun getById(chatId: Int): Chat {
        return chatRepository.getById(chatId)
    }

    fun getOrCreate(chatId: Int): Chat {
        return chatRepository.getById(chatId)
    }

    fun close(chatId: Int) {
        chatRepository.setNewStatus(chatId, ChatStatus.CLOSE)
    }

    fun getChatByFilter(filter: ChatFilter): List<ChatWithMessages> {
        val operator = currentUserService.getCurrentOperator()
        val operatorId = operator.getId()!!

        val chats = chatRepository.getChatsByOperatorAndChatIds(operatorId, filter.chatIds, filter.statuses)
        if (chats.isEmpty()) return emptyList()

        val chatIds = chats.mapNotNull { it.id }
        val messages = messageService.getMessagesByChatIds(chatIds)

        val customerIds = messages.values.flatten()
            .filter { it.type == MessageType.IN }
            .map { it.createdBy }
        val customerById = customerService.getCustomersByIds(customerIds)

        val operatorIds = messages.values.flatten()
            .filter { it.type == MessageType.OUT }
            .map { it.createdBy }
        val operatorById = operatorService.getByIds(operatorIds)
        return chats.mapNotNull {
            createChatWithMessages(it, messages, customerById, operatorById)
        }.sortedBy { it.createdAt }
    }

    private fun createChatWithMessages(
        chat: Chat,
        messagesById: Map<Int, List<Message>>,
        customerById: Map<Int, Customer>,
        operatorById: Map<Int, Operator>,
    ): ChatWithMessages? {
        val chatMessages = messagesById[chat.id] ?: return null

        val messagesWithCreatorLogin = chatMessages.map { message ->
            val creatorLogin = customerById[message.createdBy]?.let { customer ->
                "${customer.firstName} ${customer.lastName} ${customer.userName}"
            } ?: operatorById[message.createdBy]?.username ?: "N/A"

            MessageWithCreatorLogin(
                id = message.id,
                chatId = message.chatId,
                createdAt = message.createdAt,
                createdBy = creatorLogin,
                text = message.text,
                type = message.type
            )
        }.sortedByDescending { it.createdAt }

        val createdBy = customerById[chat.creatorBy]?.let { customer ->
            "${customer.firstName} ${customer.lastName} ${customer.userName}"
        } ?: "Unknown"

        return ChatWithMessages(
            id = chat.id,
            messages = messagesWithCreatorLogin,
            createdBy = createdBy,
            createdAt = chat.createdAt,
            status = chat.status
        )
    }
}