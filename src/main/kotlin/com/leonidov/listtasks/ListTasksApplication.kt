package com.leonidov.listtasks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories("com.leonidov.listtasks.persistence")
class ListTasksApplication
	fun main(args: Array<String>) {
		runApplication<ListTasksApplication>(*args)
	}
