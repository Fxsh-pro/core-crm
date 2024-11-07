package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Chat
import com.crm.corecrm.domain.repository.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService (private val chatRepository: ChatRepository){

    fun getIdOrCreate(chat: Chat) : Long {
        return chatRepository.getIdOrCreate(chat)
    }

    fun getAllChats() : List<Chat>  {
        return chatRepository.getAllChats()


    }
}