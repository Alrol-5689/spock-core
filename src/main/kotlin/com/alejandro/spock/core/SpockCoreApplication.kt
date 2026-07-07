package com.alejandro.spock.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpockCoreApplication

fun main(args: Array<String>) {
	runApplication<SpockCoreApplication>(*args)
}
