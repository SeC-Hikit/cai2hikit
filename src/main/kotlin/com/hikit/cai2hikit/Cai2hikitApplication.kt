package com.hikit.cai2hikit

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class Cai2hikitApplication(private val jobScheduler: JobScheduler) : CommandLineRunner {

    override fun run(vararg args: String?) {
        jobScheduler.schedule()
    }
}

fun main(args: Array<String>) {
    runApplication<Cai2hikitApplication>(*args)
}

