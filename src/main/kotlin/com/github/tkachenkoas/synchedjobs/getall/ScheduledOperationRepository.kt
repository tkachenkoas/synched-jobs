package com.github.tkachenkoas.synchedjobs.getall

import org.springframework.data.mongodb.repository.MongoRepository

interface ScheduledOperationRepository : MongoRepository<ScheduledOperation, String>