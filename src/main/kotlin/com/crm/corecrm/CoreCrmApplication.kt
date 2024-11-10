package com.crm.corecrm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class CoreCrmApplication

fun main(args: Array<String>) {
    runApplication<CoreCrmApplication>(*args)
}
