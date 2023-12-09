package com.srthk.quickstart.repository

import com.srthk.quickstart.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Int> {
    fun getUserByEmail(email: String): Optional<User>
}