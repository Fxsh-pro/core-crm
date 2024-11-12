package com.crm.corecrm.service.telegram

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.domain.model.telegram.TelegramMessage
import com.crm.corecrm.domain.repository.ChatRepository
import com.crm.corecrm.domain.repository.MessageRepository
import com.crm.corecrm.service.CustomerService
import com.crm.corecrm.service.OperatorService
import org.springframework.stereotype.Service

@Service
class TelegramHandlerService(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val customerService: CustomerService,
    private val operatorService: OperatorService,
) {

    fun handleIncomingMessage(telegramMessage: TelegramMessage) {
        val user = telegramMessage.sender
        val customer = Customer(
            tgId = user.tgId,
            firstName = user.firstName,
            lastName = user.lastName,
            userName = user.userName,
        )

        val customerId = customerService.getOrCreate(customer).id!!

        val chat = Chat(
            tgChatId = telegramMessage.chatId,
            creatorBy = customerId,
            createdAt = telegramMessage.timestamp,
            status = ChatStatus.OPEN,
        )
        val chatId = chatRepository.getIdOrCreate(chat)

        val message = Message(
            chatId = chatId,
            createdAt = telegramMessage.timestamp,
            createdBy = customerId,
            text = telegramMessage.text,
            type = MessageType.IN
        )

        messageRepository.save(message)
        operatorService.linkChatToOperator(chatId)
    }
}