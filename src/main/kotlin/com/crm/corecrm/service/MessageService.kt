package com.crm.corecrm.service

import com.crm.corecrm.config.TelegramNotificationBot
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.repository.ChatRepository
import com.crm.corecrm.domain.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val telegramNotificationBot: TelegramNotificationBot,
) {

    fun saveMessage(message: Message) {
        messageRepository.save(message)
    }

    fun getMessagesByChatIds(chatIds: List<Int>): Map<Int, List<Message>> {
        return messageRepository.getMessagesByChatIds(chatIds)
    }

    fun send(message: Message) {
        val chat = chatRepository.getById(message.chatId)
        messageRepository.save(message)
        chatRepository.setNewStatus(chat.id!!, ChatStatus.IN_WORK)
        telegramNotificationBot.sendMessage(chat.tgChatId, message.text)
    }
}