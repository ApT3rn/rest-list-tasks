package com.leonidov.listtasks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class ListTasksApplication
	fun main(args: Array<String>) {
		runApplication<ListTasksApplication>(*args)
	}
