package com.leonidov.listtasks.model

import com.leonidov.listtasks.model.enums.Status
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "tasks")
class Task (
    @Id
    val id: UUID?,
    var details: String,
    var status: Status,
    val created: String,
    var worked: String) {
    constructor(details: String, created: String):
            this(UUID.randomUUID(), details, Status.CREATED, created, "")
}