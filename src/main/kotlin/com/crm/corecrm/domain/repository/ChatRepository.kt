package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Chat.CHAT
import com.crm.corecrm.domain.db.tables.records.ChatRecord
import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.lang.RuntimeException

@Repository
class ChatRepository(dsl: DSLContext) : AbstractRepository(dsl) {

    fun getIdOrCreate(chat: Chat) : Long {
        val existingChatId: Long? = db.select(CHAT.ID)
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(chat.tgChatId))
            .fetchOneInto(Long::class.java)

        return if (existingChatId != null) {
            existingChatId
        } else {
            val result = db.insertInto(CHAT)
                .set(CHAT.TG_CHAT_ID, chat.tgChatId)
                .set(CHAT.CREATOR_BY, chat.creatorBy)
                .set(CHAT.CREATED_AT, chat.createdAt)
                .set(CHAT.STATUS, chat.status.name)
                .returning(CHAT.ID)
                .fetchOne()

            result?.getValue(CHAT.ID)?.toLong() ?: throw IllegalStateException("Failed to create new chat")
        }
    }

    fun getAllChats(): List<Chat> {
        val chats = db.selectFrom(CHAT).fetch().toList()
        return chats.map { fromChatRecord(it) }.toList()
    }

    private fun fromChatRecord(record: ChatRecord) : Chat {
        return Chat(
            record.id.toLong(),
            record.tgChatId,
            record.creatorBy,
            record.createdAt,
            ChatStatus.valueOf(record.status)
        )
    }
}