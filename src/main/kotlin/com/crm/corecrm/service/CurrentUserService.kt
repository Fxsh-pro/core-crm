package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Operator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentUserService {
    fun getCurrentUserId(): Int = getCurrentOperator().getId() ?: throw RuntimeException("Operator ID is missing")

    fun getCurrentOperator(): Operator {
        return SecurityContextHolder.getContext().authentication.principal as? Operator
            ?: throw RuntimeException("No authentication data found")
    }
}