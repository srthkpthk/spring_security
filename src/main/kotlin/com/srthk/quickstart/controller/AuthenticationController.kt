package com.srthk.quickstart.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/v1/auth")
class AuthenticationController(@Autowired val authService: AuthService) {
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authService.register(registerRequest))
        } catch (e: UserAlreadyRegisteredException) {
            ResponseEntity.ok(e.message)
        }
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody registerRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authService.authenticate(registerRequest))
    }
}