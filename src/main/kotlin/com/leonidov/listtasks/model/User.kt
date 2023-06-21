package com.leonidov.listtasks.model

import com.leonidov.listtasks.model.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

@Document(collection = "users")
class User(val _id: UUID, var _username: String, var _password: String, var _role: Role) : UserDetails {

    constructor(_username: String, _password: String, _role: Role):
            this(UUID.randomUUID(), _username, _password, _role)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(_role.name))
    override fun getPassword(): String = _password
    override fun getUsername(): String = _username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}