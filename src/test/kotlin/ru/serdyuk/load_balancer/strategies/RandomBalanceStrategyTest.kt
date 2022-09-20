package ru.serdyuk.load_balancer.strategies

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

internal class RandomBalanceStrategyTest {

    @Test
    fun `it checks that strategy generates not the same value`() {
        val strategy = RandomBalanceStrategy()
        strategy.setProvidersNumber(AtomicInteger(10))

        val values = (1..10)
            .map { strategy.get() }
            .distinct()

        assertTrue(values.size > 1)
    }
}
