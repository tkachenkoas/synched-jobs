package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class OperationBatchExecutor(private val operationExecutor: OperationExecutor) {

    private val executor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()

    init {
        executor.corePoolSize = 20
        executor.maxPoolSize = 20
        executor.setQueueCapacity(1000)
        executor.initialize()
    }

    fun executeBatch(batch: List<ScheduledOperation>) {
        batch.forEach {
            executor.execute {
                operationExecutor.doTheJob(it)
            }
        }
    }

}