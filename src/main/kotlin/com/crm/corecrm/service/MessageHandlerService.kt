package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.repository.MessageRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class MessageHandlerService(
    private val messageRepository: MessageRepository,
) {

    fun saveMessage(message: Message) {

        messageRepository.save(message)
    }
}