package com.crm.corecrm.service.vk

import com.crm.corecrm.domain.model.ChannelType
import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.domain.model.vk.VkMessage
import com.crm.corecrm.domain.repository.ChatRepository
import com.crm.corecrm.domain.repository.MessageRepository
import com.crm.corecrm.service.CustomerService
import com.crm.corecrm.service.OperatorService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VkHandler(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val customerService: CustomerService,
    private val operatorService: OperatorService,
) {
    @Transactional
    fun handleIncomingMessage(vkMessage: VkMessage): Message? {
        val user = vkMessage.sender
        val customer = Customer(
            channelId = user.userId.toLong(),
            firstName = user.name,
            lastName = user.lastName,
            userName = "N/A",
            channelType = ChannelType.VK
        )

        val customerId = customerService.getOrCreate(customer).id!!

        val chat = Chat(
            channelId = vkMessage.chatId.toLong(),
            creatorBy = customerId,
            createdAt = vkMessage.timestamp,
            status = ChatStatus.OPEN,
            channelType = ChannelType.VK
        )
        val chatId = chatRepository.getIdOrCreate(chat)

        val message = Message(
            chatId = chatId,
            createdAt = vkMessage.timestamp,
            createdBy = customerId,
            text = vkMessage.text,
            type = MessageType.IN
        )

        val response = messageRepository.saveWithAutoResponse(message)
        operatorService.linkChatToOperator(chatId)
        return response
    }
}