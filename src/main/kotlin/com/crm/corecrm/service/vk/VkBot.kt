package com.crm.corecrm.service.vk

import api.longpoll.bots.LongPollBot
import api.longpoll.bots.exceptions.VkApiException
import api.longpoll.bots.model.events.messages.MessageNew
import com.crm.corecrm.domain.model.vk.VkMessage
import com.crm.corecrm.domain.model.vk.VkUser
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class VkBot(
    @Value("\${vk.token:}") private val token: String,
    private val vkHandler: VkHandler
) : LongPollBot() {
    @PostConstruct
    fun init() {
        LOG.info("Started vk bot")
        Thread {
            this.startPolling() // start polling make while true so it blocks main thread
        }.start()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(VkBot::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onMessageNew(messageNew: MessageNew) {
        GlobalScope.launch {
            try {
                val message = messageNew.message
                val user = vk.users
                    .get()
                    .setUserIds(message.fromId.toString())
                    .execute().response
                    .map { VkUser(it.id, it.firstName, it.lastName) }
                    .first()

                val messageDto = VkMessage(
                    message.id,
                    message.peerId,
                    user,
                    message.text,
                    message.date
                )
                LOG.info("Received message: $messageDto")
                withContext(Dispatchers.IO) {
                    val response = vkHandler.handleIncomingMessage(messageDto)
                    if (response != null) {
                        sendMessage(messageDto.chatId.toLong() , response.text)
                    }
                }
            } catch (e: VkApiException) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(peerId: Long, message: String){
        vk.messages.send()
            .setPeerId(peerId.toInt())
            .setMessage(message)
            .execute()
    }
    override fun getAccessToken() = token
}