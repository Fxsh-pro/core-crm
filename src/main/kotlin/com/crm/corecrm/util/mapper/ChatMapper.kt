package com.crm.corecrm.util.mapper

import com.crm.corecrm.api.dto.AllChatsDto
import com.crm.corecrm.api.dto.ChatDto
import com.crm.corecrm.api.dto.ChatRequestFilter
import com.crm.corecrm.api.dto.ChatStatusDto
import com.crm.corecrm.api.dto.message.MessageDto
import com.crm.corecrm.api.dto.message.MessageTypeDto
import com.crm.corecrm.domain.model.ChatFilter
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.domain.model.ChatWithMessages

object ChatMapper {
    fun fromFilterDto(filter: ChatRequestFilter): ChatFilter {
        return ChatFilter(
            chatIds = filter.chatIds,
            statuses = filter.statuses?.map { chatStatusFromDto(it) }
        )
    }

    fun chatStatusFromDto(status: ChatStatusDto): ChatStatus {
        return when (status) {
            ChatStatusDto.OPEN -> ChatStatus.OPEN
            ChatStatusDto.IN_WORK -> ChatStatus.IN_WORK
            ChatStatusDto.CLOSE -> ChatStatus.CLOSE
        }
    }

    fun mapToAllChatsDto(chatsWithMessages: List<ChatWithMessages>): AllChatsDto {
        val chatDtos = chatsWithMessages.map { chatWithMessages ->
            val messageDtos = chatWithMessages.messages.map { messageWithCreatorLogin ->
                MessageDto(
                    id = messageWithCreatorLogin.id,
                    chatId = messageWithCreatorLogin.chatId,
                    createdAt = messageWithCreatorLogin.createdAt,
                    createdBy = messageWithCreatorLogin.createdBy,
                    text = messageWithCreatorLogin.text,
                    type = MessageTypeDto.valueOf(messageWithCreatorLogin.type.name)
                )
            }

            ChatDto(
                id = chatWithMessages.id,
                messages = messageDtos,
                creatorBy = chatWithMessages.createdBy,
                createdAt = chatWithMessages.createdAt,
                status = ChatStatusDto.valueOf(chatWithMessages.status.name)
            )
        }

        return AllChatsDto(chats = chatDtos)
    }
}