package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class OperationBatchExecutor(
    private val operationExecutor: OperationExecutor,
    private val meterRegistry: MeterRegistry
) {

    private val taskExecutor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()

    init {
        taskExecutor.corePoolSize = 20
        taskExecutor.maxPoolSize = 20
        taskExecutor.setQueueCapacity(1000)
        taskExecutor.initialize()

        val poolExecutor = taskExecutor.threadPoolExecutor
        meterRegistry.gauge("active-threads", poolExecutor) { it.activeCount.toDouble() }
        meterRegistry.gauge("queue-size", poolExecutor) { it.queue.size.toDouble() }
    }

    fun executeBatch(batch: List<ScheduledOperation>) {
        batch.forEach {
            taskExecutor.execute {
                operationExecutor.doTheJob(it)
            }
        }
    }

}