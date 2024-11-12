package com.crm.corecrm.service

import com.crm.corecrm.api.auth.AuthenticationResponse
import com.crm.corecrm.api.auth.LoginRequest
import com.crm.corecrm.api.auth.RegisterRequest
import com.crm.corecrm.domain.model.Operator
import com.crm.corecrm.domain.repository.OperatorRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class OperatorService(
    private val operatorRepository: OperatorRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
) {

    fun register(request: RegisterRequest): AuthenticationResponse {
        val operator = Operator(request.id, request.login, passwordEncoder.encode(request.password), request.name)
        operatorRepository.save(operator)
        val token = jwtService.generateToken(operator)
        return AuthenticationResponse(token)
    }

    fun authenticate(request: LoginRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.login,
                request.password
            )
        )
        val user = operatorRepository.findByLogin(request.login)
        val token: String = jwtService.generateToken(user)
        return AuthenticationResponse(token)
    }

    fun linkChatToOperator(chatId: Int) {
        operatorRepository.linkChatToOperator(chatId, 1)
    }

    fun getByIds(operatorIds: List<Int>): Map<Int, Operator> {
        return operatorRepository.getByIds(operatorIds)
    }
}