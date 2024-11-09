package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Message.MESSAGE
import com.crm.corecrm.domain.db.tables.records.MessageRecord
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
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
        record.store()
    }

    fun getMessagesByChatIds(chatIds: List<Int>): Map<Int, List<Message>> {
        val records = db
            .selectFrom(MESSAGE)
            .where(MESSAGE.CHAT_ID.`in`(chatIds))
            .fetch()
            .asSequence()
            .map { it.toMessage() }
            .toList()

        return records.groupBy { it.chatId }
    }


    fun MessageRecord.toMessage(): Message {
        return Message(
            id = this.id,
            chatId = this.chatId,
            createdAt = this.createdAt,
            createdBy = this.createdBy,
            text = this.text,
            type = MessageType.valueOf(this.type)
        )
    }
}