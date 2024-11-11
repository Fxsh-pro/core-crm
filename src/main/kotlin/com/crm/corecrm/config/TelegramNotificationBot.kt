package com.crm.corecrm.config

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.domain.repository.ChatRepository
import com.crm.corecrm.domain.repository.MessageRepository
import com.crm.corecrm.service.CustomerService
import com.crm.corecrm.service.OperatorService
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Service
class TelegramNotificationBot(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.bot.name}") private val botName: String,
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val customerService: CustomerService,
    private val operatorService: OperatorService,

) : TelegramLongPollingBot(botToken) {

    @PostConstruct
    fun t() {
        println("Token : $botToken")
    }

    val LOG = LoggerFactory.getLogger(TelegramNotificationBot::class.java)

    private val log = LoggerFactory.getLogger(TelegramNotificationBot::class.java)

    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        LOG.info("Received message: ${update.message}")
        val tgChatId = update.message.chatId.toInt()
        val tgChat = update.message.chat
        val customer = Customer(
            tgId = tgChatId,
            firstName = tgChat.firstName ?: "NoFirstName",
            lastName = tgChat.lastName ?: "NoLastName" ,
            userName = tgChat.userName ?: "NoUserName" ,
        )

        val customerId = customerService.getIdOrCreate(customer)

        val chat = Chat(
            tgChatId = tgChatId,
            creatorBy = customerId,
            createdAt = update.message.date,
            status = ChatStatus.OPEN,
        )

        val chatId = chatRepository.getIdOrCreate(chat)

        val message = Message(
            chatId = chatId,
            createdAt = update.message.date,
            createdBy = customerId,
            text = update.message.text,
            type = MessageType.IN
        )

        messageRepository.save(message)
        operatorService.linkChatToOperator(chatId)

        // val sendMessage = SendMessage.builder()
        //     .chatId(tgChatId.toLong())
        //     .text("messageSaved")
        //     .build()
        //
        // execute(sendMessage)
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

    fun sendMessage(chatId: Int, message: String) {
        val sendMessage = SendMessage.builder()
            .chatId(chatId.toString())
            .text(message)
            .build()

        try {
            execute(sendMessage)
            println("YES")
        } catch (e: TelegramApiException) {
            log.error("Error occurred while sending message: {}", e.message)
        }
    }
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