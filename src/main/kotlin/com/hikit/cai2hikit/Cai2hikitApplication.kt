package com.hikit.cai2hikit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ComponentScan(
    value =
    [
        "com.hikit.cai2hikit",
        "org.hikit.common.adapter",
        "org.hikit.common.processor",
        "org.hikit.common.datasource"
    ]
)
class Cai2hikitApplication

fun main(args: Array<String>) {
    runApplication<Cai2hikitApplication>(*args)
}

