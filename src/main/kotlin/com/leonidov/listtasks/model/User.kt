package com.leonidov.listtasks.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
class User (@Id
            val id: UUID?,
            var username: String,
            var password: String) {
    constructor(username: String, password: String):
            this(UUID.randomUUID(), username, password)
}