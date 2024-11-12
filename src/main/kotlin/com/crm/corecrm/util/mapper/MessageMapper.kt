package com.crm.corecrm.util.mapper

import com.crm.corecrm.api.dto.message.MessageDto
import com.crm.corecrm.api.dto.message.MessageTypeDto
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType

object MessageMapper {
    fun toDto(message: Message): MessageDto {
        return MessageDto(
            id = message.id,
            chatId = message.chatId,
            createdAt = (System.currentTimeMillis() / 1000).toInt(),
            createdBy = "",
            text = message.text,
            type = messageTypeToDto(message.type)
        )
    }

    fun messageTypeToDto(type: MessageType): MessageTypeDto {
        return when (type) {
            MessageType.IN -> MessageTypeDto.IN
            MessageType.OUT -> MessageTypeDto.OUT
        }
    }
}