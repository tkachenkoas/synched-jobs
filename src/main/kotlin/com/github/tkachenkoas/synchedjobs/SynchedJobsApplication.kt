package com.github.tkachenkoas.synchedjobs

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperationRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.lang.Thread.sleep
import java.util.*

@EnableMongoRepositories
@Import(EmbeddedMongoAutoConfiguration::class)
@SpringBootApplication
@EnableScheduling
class SynchedJobsApplication

fun main(args: Array<String>) {
	val context = runApplication<SynchedJobsApplication>(*args)

	val operationRepository = context.getBean(ScheduledOperationRepository::class.java)

	repeat(10) {
		val operationsToExecute = List(50) {
			ScheduledOperation(UUID.randomUUID().toString())
		}
		operationRepository.saveAll(operationsToExecute)
		sleep(1000)
	}


}
