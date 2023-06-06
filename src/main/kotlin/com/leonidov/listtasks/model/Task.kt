package com.leonidov.listtasks.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("tasks")
class Task (
    @Id
    val id: UUID,
    var details: String,
    var decided: Boolean,
    val userCreated: UUID) {
    constructor(details: String, userCreated: UUID):
            this(UUID.randomUUID(), details, false, userCreated)
}