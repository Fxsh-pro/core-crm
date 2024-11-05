package com.crm.corecrm.api

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/operator"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@RestController
class OperatorController {

    @GetMapping(
        path = ["/qw"],
    )
    fun suggest(): String = "dsf"
}