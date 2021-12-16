package com.github.tkachenkoas.synchedjobs.reporter

import org.springframework.data.mongodb.repository.MongoRepository

interface ExecutedOperationRepository : MongoRepository<ExecutedOperation, String> {

}