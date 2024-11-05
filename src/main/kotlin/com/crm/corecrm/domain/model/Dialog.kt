package com.crm.corecrm.domain.model

data class Dialog(
    val id: Int? = null,
    val creatorBy: Long,
    val createdAt: Long,
    val status: DialogStatus
)