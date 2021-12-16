package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.data.mongodb.core.mapping.Document

@Document("ScheduledOperation")
data class ScheduledOperation(
    var operationInfo: String,
    var heavy: Boolean
)