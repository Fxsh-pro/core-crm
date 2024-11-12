package com.crm.corecrm.api

import com.crm.corecrm.api.dto.message.CreateMessageRequest
import com.crm.corecrm.api.dto.message.MessageDto
import com.crm.corecrm.api.dto.message.MessageTypeDto
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.service.CurrentUserService
import com.crm.corecrm.service.MessageService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/message"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@RestController
class MessageController(
    private val currentUserService: CurrentUserService,
    private val messageService: MessageService,
) {
    @PostMapping(
        path = ["/"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Send a message to the user. Pass only chatId and text for the request",
    )
    fun send(@RequestBody messageDto: CreateMessageRequest): MessageDto? {

        val message = Message(
            chatId = messageDto.chatId,
            createdAt = (System.currentTimeMillis() / 1000).toInt(),
            createdBy = currentUserService.getCurrentUserId(),
            text = messageDto.text,
            type = MessageType.OUT
        )
        val savedMessage = messageService.send(message)

        return MessageDto(
            id = savedMessage.id,
            chatId = savedMessage.chatId,
            createdAt = savedMessage.createdAt,
            createdBy = currentUserService.getCurrentOperator().username,
            text = savedMessage.text,
            type = MessageTypeDto.OUT
        )
    }
}