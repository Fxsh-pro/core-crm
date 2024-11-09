package com.crm.corecrm.api.dto

import com.crm.corecrm.domain.model.ChatWithMessages

class ChatWithMessagesMapper {

    companion object {
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
}

