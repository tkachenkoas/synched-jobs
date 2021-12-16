package com.github.tkachenkoas.synchedjobs.executor

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import kotlin.random.Random

@Component
class OperationExecutor(
    val meterRegistry: MeterRegistry
) {

    fun doTheJob(scheduledOperation: ScheduledOperation) {
        val duration = Random.nextLong(30, 100)
        sleep(
            if (scheduledOperation.heavy) {
                duration * 3
            } else {
                duration
            }
        )

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