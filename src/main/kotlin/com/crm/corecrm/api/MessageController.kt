package com.crm.corecrm.api

import com.crm.corecrm.api.dto.MessageDto
import com.crm.corecrm.domain.model.Message
import com.crm.corecrm.domain.model.MessageType
import com.crm.corecrm.service.CurrentUserService
import com.crm.corecrm.service.MessageService
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
        path = ["/message"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun send(@RequestBody messageDto: MessageDto) {
        val message = Message(
            chatId = messageDto.chatId,
            createdAt = (System.currentTimeMillis() / 1000).toInt(),
            createdBy = currentUserService.getCurrentUserId(),
            text = messageDto.text,
            type = MessageType.OUT
        )
        messageService.send(message)
    }
}