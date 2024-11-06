package com.hikit.cai2hikit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class Cai2hikitApplication

fun main(args: Array<String>) {
	runApplication<Cai2hikitApplication>(*args)
}
