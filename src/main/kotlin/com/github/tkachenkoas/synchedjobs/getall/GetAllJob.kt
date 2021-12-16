package com.github.tkachenkoas.synchedjobs.getall

import com.github.tkachenkoas.synchedjobs.executor.OperationBatchExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class GetAllJob(
    private val scheduledOperationRepository: ScheduledOperationRepository,
    private val operationBatchExecutor: OperationBatchExecutor
) {

    @Scheduled(fixedDelay = 300)
    fun doTheDirtyJob() {
        val operations = scheduledOperationRepository.findAllByNextExecutionLessThan(Instant.now())
        operationBatchExecutor.executeBatch(operations)
        println("Submitted batch of ${operations.size} entities")
    }

}