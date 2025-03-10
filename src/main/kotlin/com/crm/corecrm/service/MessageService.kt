package com.crm.corecrm.service

import com.crm.corecrm.config.TelegramNotificationBot
import com.crm.corecrm.domain.model.ChannelType
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.repository.ChatRepository
import com.crm.corecrm.domain.repository.MessageRepository
import com.crm.corecrm.service.vk.VkBot
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val telegramNotificationBot: TelegramNotificationBot,
    private val vkBot: VkBot,
) {

    fun saveMessage(message: Message) {
        messageRepository.save(message)
    }

    fun getMessagesByChatIds(chatIds: List<Int>): Map<Int, List<Message>> {
        return messageRepository.getMessagesByChatIds(chatIds)
    }

    @Transactional
    fun send(message: Message) : Message{
        val chat = chatRepository.getById(message.chatId)
        val savedMessage = messageRepository.save(message)
        chatRepository.setNewStatus(chat.id!!, ChatStatus.IN_WORK)
        if (chat.channelType == ChannelType.TG) {
            telegramNotificationBot.sendMessage(chat.channelId, message.text)
        } else {
            vkBot.sendMessage(chat.channelId, message.text)
        }

        return savedMessage
    }
}