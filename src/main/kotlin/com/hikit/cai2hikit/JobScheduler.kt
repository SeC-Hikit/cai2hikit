package com.hikit.cai2hikit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component


@Component
class JobScheduler @Autowired constructor(
    private val scheduler: TaskScheduler,
    private val trailFetchJob: TrailFetchJob,
    @Value("\${job.fetch.chron}") private val chronExpression: String
) {

    fun schedule() {
        scheduler.schedule({ trailFetchJob.getTrail() }, CronTrigger(chronExpression))
    }
}