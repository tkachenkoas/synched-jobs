package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface ScheduledOperationRepository : MongoRepository<ScheduledOperation, String> {

    fun findAllByNextExecutionLessThan(instant: Instant) : List<ScheduledOperation>
}