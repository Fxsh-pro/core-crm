package com.crm.corecrm.api

import com.crm.corecrm.api.dto.AllChatsDto
import com.crm.corecrm.service.ChatService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/chat"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@RestController
class ChatController(val chatService: ChatService) {

    @GetMapping("/all")
    fun allChats() : AllChatsDto? {
        val allChats = chatService.getAllChats()
        // val all
        return null
    }

}