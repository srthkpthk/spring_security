package com.srthk.quickstart.user

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "_dbuser")
data class User(
    @Id @GeneratedValue private val id: Int?,
    private val firstName: String,
    private val lastName: String,
    private val email: String,
    private val password: String,
    @Enumerated(EnumType.STRING)
    private val role: Role,
) : UserDetails {
    // Secondary constructor without the id parameter
    constructor(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: Role,
    ) : this(null, firstName, lastName, email, password, role)


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

enum class Role {
    USER, ADMIN
}
