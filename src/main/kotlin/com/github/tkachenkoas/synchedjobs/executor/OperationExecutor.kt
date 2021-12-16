package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.reporter.ExecutedOperation
import com.github.tkachenkoas.synchedjobs.reporter.ExecutedOperationRepository
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.time.Duration
import java.time.Instant
import kotlin.random.Random

@Component
class OperationExecutor(val executedRepository: ExecutedOperationRepository) {

    private val tbb: Instant = Instant.now()

    fun doTheJob(scheduledOperation: ScheduledOperation) {
        sleep(Random.nextLong(30, 100))

        val elapsedMs = Duration.between(tbb, Instant.now()).toMillis()
        val bucket: Long = elapsedMs / 500
        executedRepository.save(ExecutedOperation(bucket, elapsedMs))
    }

}