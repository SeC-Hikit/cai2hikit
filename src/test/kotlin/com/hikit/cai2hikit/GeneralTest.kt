package com.hikit.cai2hikit

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import kotlin.system.exitProcess
import kotlin.test.assertEquals


@SpringBootTest
class GeneralTest {

//    var context: ConfigurableApplicationContext = runApplication<Cai2hikitApplication>()

    @Test
    fun checkContextLoading() {
        runApplication<Cai2hikitApplication>()
    }

    @Test
    fun checkIfApiWorks(
        @Autowired trailController: TrailController,
        @Autowired context: ConfigurableApplicationContext
    ) {
        Thread.sleep(5000)
        assertEquals(trailController.getTrailByRef("109")?.get(0)?.properties?.from, "Monteacuto delle Alpi")
    }

//    @AfterEach
//    fun tearDown(@Autowired context: ConfigurableApplicationContext) {
//        println("after each!")
//    }
}