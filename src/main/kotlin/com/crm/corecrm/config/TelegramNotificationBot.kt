package com.crm.corecrm.config

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.service.ChatService
import com.crm.corecrm.service.CustomerService
import com.crm.corecrm.service.MessageHandlerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class TelegramNotificationBot(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.bot.name}") private val botName: String,
    private val messageHandlerService: MessageHandlerService,
    private val chatService: ChatService,
    private val customerService: CustomerService,

) : TelegramLongPollingBot(botToken) {

    private val log = LoggerFactory.getLogger(TelegramNotificationBot::class.java)

    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        val tgChatId = update.message.chatId.toLong()

        // val
        val chat = Chat(
            tgChatId = tgChatId,
            creatorBy = update.message.chatId.toLong(),
            createdAt = update.message.date.toLong(),
            status = ChatStatus.OPEN,
        )

        val chatId = chatService.getIdOrCreate(chat)

        val message = Message(
            chatId = chatId,
            createdAt = update.message.date.toLong(),
            createdBy = 1,
            text = update.message.text,
            type = MessageType.IN
        )

        messageHandlerService.saveMessage(message)

        val sendMessage = SendMessage.builder()
            .chatId(tgChatId)
            .text("messageSaved")
            .build()

        execute(sendMessage)
    }
    //     when (message) {
    //         "/reg" -> {
    //             val registrationMessage = SendMessage.builder()
    //                 .chatId(chatId.toString())
    //                 .text("Please provide your ID:")
    //                 .build()
    //
    //             try {
    //                 execute(registrationMessage)
    //             } catch (e: TelegramApiException) {
    //                 log.error("An error occurred while trying to send a message: {}", e.message)
    //             }
    //         }
    //
    //         else -> {
    //             message?.let {
    //                 processUserResponse(chatId, it)
    //             }
    //         }
    //     }
    // }
    //
    // private fun sendMessage(chatId: Long, message: String) {
    //     val sendMessage = SendMessage.builder()
    //         .chatId(chatId.toString())
    //         .text(message)
    //         .build()
    //
    //     try {
    //         execute(sendMessage)
    //     } catch (e: TelegramApiException) {
    //         log.error("Error occurred while sending message: {}", e.message)
    //     }
    // }
    //
    // private fun processUserResponse(chatId: Long, response: String) {
    //     try {
    //         val userId = response.toLong()
    //         // Assuming you have some other mechanism or client to send the Telegram ID to user-service.
    //         // The actual integration should replace this comment.
    //     } catch (e: NumberFormatException) {
    //         log.error("Error parsing user response as userId: {}", e.message)
    //     } catch (e: FeignException.FeignClientException) {
    //         log.error(e.message)
    //     }
    // }
    //
    // fun sendMessageToUser(user: UserDto, message: String) {
    //     val telegramContact = user.contacts
    //         .firstOrNull { it.type == ContactDto.ContactType.TELEGRAM }
    //
    //     telegramContact?.let { contact ->
    //         try {
    //             val chatId = contact.contact.toLong()
    //             sendMessage(chatId, message)
    //         } catch (e: TelegramApiException) {
    //             log.error("Error sending notification to user: {}", user, e)
    //         }
    //     }
    // }
    //
    // fun getPreferredContact(): PreferredContact {
    //     return PreferredContact.TELEGRAM
    // }
}

enum class PreferredContact{
    TELEGRAM,
}