package com.github.tkachenkoas.synchedjobs.reporter

import org.springframework.data.mongodb.core.mapping.Document

@Document("ExecutedOperation")
data class ExecutedOperation(
    var bucket: Long,
    var elapsed: Long
)