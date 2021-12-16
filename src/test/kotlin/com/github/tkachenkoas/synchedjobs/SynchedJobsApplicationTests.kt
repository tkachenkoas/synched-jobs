package com.github.tkachenkoas.synchedjobs

import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperation
import com.github.tkachenkoas.synchedjobs.getall.ScheduledOperationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class SynchedJobsApplicationTests {

    @Test
    fun contextLoads() {

    }

    @Autowired
    private lateinit var scheduledOperationRepository: ScheduledOperationRepository

    @Test
    fun testScheduledOperationsRepository() {
        val now = Instant.now()
        val operation = scheduledOperationRepository.save(
            ScheduledOperation(UUID.randomUUID().toString(), false, now)
        )

        scheduledOperationRepository.save(
            ScheduledOperation(UUID.randomUUID().toString(), false, now.plusMillis(500))
        )

        assertThat(scheduledOperationRepository.findAllByNextExecutionLessThan(
            now.minusMillis(100)
        )).hasSize(0)

        val ready = scheduledOperationRepository.findAllByNextExecutionLessThan(now.plusMillis(100))

        assertThat(ready.map { it.operationInfo }).hasSize(1)
            .contains(operation.operationInfo)

        assertThat(scheduledOperationRepository.findAllByNextExecutionLessThan(
            now.plusMillis(1000)
        )).hasSize(2)
    }

}
