package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Message.MESSAGE
import com.crm.corecrm.domain.model.Message
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class MessageRepository(dsl: DSLContext) : AbstractRepository(dsl) {

    fun save(message: Message) {
        val record = db.newRecord(MESSAGE).apply {
            chatId = message.chatId
            createdAt = message.createdAt
            createdBy = message.createdBy
            text = message.text
            type = message.type.toString()
        }
        record.store() // Automatically inserts the record and lets PostgreSQL handle the ID
    }
}