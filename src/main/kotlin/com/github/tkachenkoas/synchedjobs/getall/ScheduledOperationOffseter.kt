package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ScheduledOperationOffseter(
    private val scheduledOperationRepository: ScheduledOperationRepository
) {

    fun offsetOperation(scheduledOperation: ScheduledOperation, offset: Long) {
        scheduledOperation.nextExecution = Instant.now().plusMillis(offset)
        scheduledOperation.executedTimes++
        scheduledOperationRepository.save(scheduledOperation)
    }

}