package com.crm.corecrm.api

import com.crm.corecrm.api.dto.AllChatsDto
import com.crm.corecrm.api.dto.ChatRequestFilter
import com.crm.corecrm.service.ChatService
import com.crm.corecrm.service.CurrentUserService
import com.crm.corecrm.util.mapper.ChatMapper
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
        path = ["/get"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Retrieve chats by filters: ids and statutes",
        description = "Returns chats based on the provided chat IDs or statuses. If no chat IDs and statuses are provided (pass empty json {}), it will return all chats for the authenticated operator."
    )
    fun getChats(@RequestBody chatFilter: ChatRequestFilter): AllChatsDto? {
        val filter = ChatMapper.fromFilterDto(chatFilter)
        val allChats = chatService.getChatByFilter(filter)
        return ChatMapper.mapToAllChatsDto(allChats)
    }

    @PostMapping("/close")
    @Operation(
        summary = "close chat by chatId",
    )
    fun closeChat(@RequestParam("chatId") chatId: Int) {
        chatService.close(chatId)
    }
}