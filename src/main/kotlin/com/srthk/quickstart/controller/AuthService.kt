package com.srthk.quickstart.controller

import com.srthk.quickstart.config.JWTService
import com.srthk.quickstart.repository.UserRepository
import com.srthk.quickstart.user.Role
import com.srthk.quickstart.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

class UserAlreadyRegisteredException(message: String?) : Exception(message)

@Service
class AuthService(
    @Autowired val repository: UserRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired val jwtService: JWTService,
    @Autowired val authenticationManager: AuthenticationManager,
) {


    fun register(registerRequest: RegisterRequest): AuthenticationResponse? {
        if (repository.getUserByEmail(registerRequest.email).isPresent) {
            throw UserAlreadyRegisteredException("Already registered")
        }

        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            role = Role.USER
        )
        repository.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

    fun authenticate(registerRequest: AuthenticationRequest): AuthenticationResponse? {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                registerRequest.email, registerRequest.password
            )
        )
        val user = repository.getUserByEmail(registerRequest.email).orElseThrow {
            Exception("no User")
        }
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

}