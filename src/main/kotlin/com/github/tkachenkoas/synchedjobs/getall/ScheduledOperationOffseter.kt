package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.random.Random

@Component
class ScheduledOperationOffseter(
    private val scheduledOperationRepository: ScheduledOperationRepository
) {

    fun offsetOperation(scheduledOperation: ScheduledOperation, offset: Long) {
        val desync = (offset / 100) * Random.nextLong(60, 140)
        scheduledOperation.nextExecution = Instant.now().plusMillis(desync)
        scheduledOperation.executedTimes++
        scheduledOperationRepository.save(scheduledOperation)
    }

}