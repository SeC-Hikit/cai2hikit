package com.hikit.cai2hikit.config

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar


@Configuration
class Scheduler constructor(@Value("\${job.poolSize}") private val poolSize: Int) : SchedulingConfigurer {
    val logger: Logger = LogManager.getLogger(Scheduler::class.java)

    override fun configureTasks(scheduledTaskRegistrar: ScheduledTaskRegistrar) {
        logger.debug("Async Scheduler")
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler())
    }

    @Bean
    fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = poolSize // Better to read it from property file.
        scheduler.setThreadNamePrefix("ThreadScheduler-")
        scheduler.initialize()
        return scheduler
    }
}