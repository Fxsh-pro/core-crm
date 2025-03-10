package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Chat.CHAT
import com.crm.corecrm.domain.db.tables.OperatorDialog.OPERATOR_DIALOG
import com.crm.corecrm.domain.db.tables.records.ChatRecord
import com.crm.corecrm.domain.model.ChannelType
import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.model.ChatStatus
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class ChatRepository(dsl: DSLContext) : AbstractRepository(dsl) {

    fun getIdOrCreate(chat: Chat): Int {
        val existingChatId = db.select(CHAT.ID)
            .from(CHAT)
            .where(CHAT.CHAT_ID.eq(chat.channelId))
            .and(CHAT.CHANNEL_TYPE.eq(chat.channelType.name))
            .and(!CHAT.STATUS.eq(ChatStatus.CLOSE.name))
            .fetchOneInto(Int::class.java)

        return if (existingChatId != null) {
            existingChatId
        } else {
            val result = db.insertInto(CHAT)
                .set(CHAT.CHAT_ID, chat.channelId)
                .set(CHAT.CHANNEL_TYPE, chat.channelType.name)
                .set(CHAT.CREATOR_BY, chat.creatorBy)
                .set(CHAT.CREATED_AT, chat.createdAt)
                .set(CHAT.STATUS, chat.status.name)
                .returning(CHAT.ID)
                .fetchOne()

            result?.getValue(CHAT.ID)?.toInt() ?: throw IllegalStateException("Failed to create new chat")
        }
    }

    fun getById(id: Int): Chat {
        return fromChatRecord(
            db
                .selectFrom(CHAT)
                .where(CHAT.ID.eq(id))
                .fetchOne() ?: throw RuntimeException("Chat not found")
        )
    }

    fun setNewStatus(id: Int, status: ChatStatus) {
        db.update(CHAT)
            .set(DSL.field("status", String::class.java), status.name) // Escaped field
            .where(CHAT.ID.eq(id))
            .execute()
    }

    fun getChatsByOperatorAndChatIds(operatorId: Int, chatIds: List<Int>?, statuses: List<ChatStatus>?): List<Chat> {
        var condition = OPERATOR_DIALOG.OPERATOR_ID.eq(operatorId)
        if (!chatIds.isNullOrEmpty()) {
            condition = condition.and(OPERATOR_DIALOG.CHAT_ID.`in`(chatIds))
        }

        val chats = db
            .selectFrom(CHAT)
            .where(
                DSL.and(
                    CHAT.ID.`in`(
                        db.select(OPERATOR_DIALOG.CHAT_ID)
                            .from(OPERATOR_DIALOG)
                            .where(condition)
                    ),
                    if (statuses.isNullOrEmpty()) DSL.condition(true) else CHAT.STATUS.`in`(statuses)
                )
            )
            .fetch()
            .toList()
        return chats.map { fromChatRecord(it) }
    }

    private fun fromChatRecord(record: ChatRecord): Chat {
        return Chat(
            record.id,
            record.chatId,
            ChannelType.valueOf(record.channelType),
            record.creatorBy,
            record.createdAt,
            ChatStatus.valueOf(record.status)
        )
    }
}