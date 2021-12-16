package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperationOffseter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import kotlin.random.Random

@Component
class OperationExecutor(
    private val meterRegistry: MeterRegistry,
    private val offseter: ScheduledOperationOffseter,
) {

    fun doTheJob(scheduledOperation: ScheduledOperation) {
        val duration = Random.nextLong(30, 100)
        val timer = meterRegistry.timer("operation-duration")
        timer.record {
            sleep(
                if (scheduledOperation.heavy) {
                    duration * 3
                } else {
                    duration
                }
            )
        }

        offseter.offsetOperation(scheduledOperation, 20_000)

        meterRegistry.counter(
            "operations-executed", "type",
            if (scheduledOperation.heavy) {
                "heavy"
            } else {
                "light"
            }
        ).increment()
    }

}