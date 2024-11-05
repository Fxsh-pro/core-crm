package com.crm.corecrm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoreCrmApplication

fun main(args: Array<String>) {
    runApplication<CoreCrmApplication>(*args)
}
