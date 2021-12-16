package com.github.tkachenkoas.synchedjobs.getall

import com.github.tkachenkoas.synchedjobs.executor.OperationBatchExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GetAllJob(
    private val scheduledOperationRepository: ScheduledOperationRepository,
    private val operationBatchExecutor: OperationBatchExecutor
) {

    @Scheduled(fixedRate = 20_000)
    fun doTheDirtyJob() {
        val operations = scheduledOperationRepository.findAll()
        operationBatchExecutor.executeBatch(operations)
        println("Submitted batch of ${operations.size} entities")
    }

}