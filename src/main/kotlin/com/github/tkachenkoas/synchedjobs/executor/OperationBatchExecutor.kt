package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperationOffseter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.core.task.TaskRejectedException
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class OperationBatchExecutor(
    private val operationExecutor: OperationExecutor,
    private val offseter: ScheduledOperationOffseter,
    private val meterRegistry: MeterRegistry,
) {

    private val taskExecutor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()

    init {
        taskExecutor.corePoolSize = 5
        taskExecutor.maxPoolSize = 10
        taskExecutor.keepAliveSeconds = 1
        taskExecutor.setQueueCapacity(1000)
        taskExecutor.initialize()

        val poolExecutor = taskExecutor.threadPoolExecutor
        meterRegistry.gauge("active-threads", poolExecutor) { it.activeCount.toDouble() }
        meterRegistry.gauge("queue-size", poolExecutor) { it.queue.size.toDouble() }
    }

    fun executeBatch(batch: List<ScheduledOperation>) {
        val executions = mutableListOf<CompletableFuture<Void>>()
        batch.forEach {
            try {
                val asyncTask = CompletableFuture.runAsync({
                    operationExecutor.doTheJob(it)
                }, taskExecutor)
                executions.add(asyncTask)
            } catch (rejected: TaskRejectedException) {
                meterRegistry.counter("rejected").increment()
                offseter.offsetOperation(it, 3_000)
            }
        }
        executions.forEach { it.get() }
    }

}