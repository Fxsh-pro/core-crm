package com.crm.corecrm.api.auth

import com.crm.corecrm.service.OperatorService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/auth"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@RestController
class AuthenticationController(private val operatorService: OperatorService) {
    @PostMapping(
        path = ["/register"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun register(@RequestBody request: RegisterRequest): AuthenticationResponse {
        return operatorService.register(request)
    }

    @PostMapping(
        path = ["/login"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@RequestBody request: LoginRequest): AuthenticationResponse {
        print("REQUEST: $request")
        return operatorService.authenticate(request)
    }
}