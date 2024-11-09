package com.crm.corecrm.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val LOG = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<Any> {
        LOG.error("ex", ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: ${ex.message}")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<Any> {
        LOG.warn(ex::class.simpleName)
        LOG.error("Unknown exception caused: {}. \nStacktrace: {}", ex.message, ex.stackTraceToString())
        return ResponseEntity.internalServerError().body(ex.message)
    }
}
