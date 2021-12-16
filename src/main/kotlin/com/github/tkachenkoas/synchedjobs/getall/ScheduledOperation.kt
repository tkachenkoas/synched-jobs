package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("ScheduledOperation")
data class ScheduledOperation(
    @Id
    var operationInfo: String,
    var heavy: Boolean,
    var nextExecution: Instant = Instant.now(),
    var executedTimes: Int = 0
)