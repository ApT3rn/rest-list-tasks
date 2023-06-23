package com.leonidov.listtasks.model

import com.leonidov.listtasks.model.enums.Status
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Document(collection = "tasks")
class Task(
    @Id
    val id: UUID?,
    var details: String,
    var status: String,
    val creator: String,
    val created_at: String,
    var completed_at: String,
    var worked: String) {
    constructor(details: String, creator: String):
            this(UUID.randomUUID(), details, Status.CREATED.name, creator, LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a")),"", "")
}