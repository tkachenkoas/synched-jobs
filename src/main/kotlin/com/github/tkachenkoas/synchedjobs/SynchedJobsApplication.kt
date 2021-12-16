package com.github.tkachenkoas.synchedjobs

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperationRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.lang.Thread.sleep
import java.time.Instant
import java.util.*
import kotlin.random.Random

@EnableMongoRepositories
@Import(EmbeddedMongoAutoConfiguration::class)
@SpringBootApplication
@EnableScheduling
class SynchedJobsApplication

fun main(args: Array<String>) {
    val context = runApplication<SynchedJobsApplication>(*args)

    val operationRepository = context.getBean(ScheduledOperationRepository::class.java)

    repeat(10) {
        val operationsToExecute = List(70) {
            ScheduledOperation(
                UUID.randomUUID().toString(),
                Random.nextLong(0, 100) < 10,
            )
        }
        operationRepository.saveAll(operationsToExecute)
        sleep(500)
    }

    repeat(10) {
        val operationsToExecute = List(30) {
            ScheduledOperation(
                UUID.randomUUID().toString(),
                Random.nextLong(0, 100) < 10,
            )
        }
        operationRepository.saveAll(operationsToExecute)
        sleep(500)
    }

    context.getBean(MeterRegistry::class.java)
        .gauge("total-scheduled", operationRepository) { it.count().toDouble() }

}
