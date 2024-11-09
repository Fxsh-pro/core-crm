package com.crm.corecrm.api

import com.crm.corecrm.api.dto.AllChatsDto
import com.crm.corecrm.api.dto.ChatRequestFilter
import com.crm.corecrm.api.dto.ChatWithMessagesMapper
import com.crm.corecrm.domain.model.ChatFilter
import com.crm.corecrm.domain.model.ChatStatus
import com.crm.corecrm.service.ChatService
import com.crm.corecrm.service.CurrentUserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/chat"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@RestController
class ChatController(
    private val chatService: ChatService,
    private val currentUserService: CurrentUserService,
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Retrieve chats by IDs or all chats",
        description = "Returns chats based on the provided chat IDs. If no chat IDs are provided, it will return all chats for the authenticated operator."
    )
    fun getChats(@RequestBody chatFilter: ChatRequestFilter): AllChatsDto? {
        val filter = ChatFilter(
            chatIds = chatFilter.chatIds,
            statuses = chatFilter.statuses?.map { ChatStatus.valueOf(it.name) }
        )
        val allChats = chatService.getChatByFilter(filter)
        return ChatWithMessagesMapper.mapToAllChatsDto(allChats)
    }

    @PostMapping("/close")
    fun closeChat(@RequestParam("chatId") chatId: Int) {
        chatService.close(chatId)
    }
}