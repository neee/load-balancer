package ru.serdyuk.load_balancer.strategies

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RandomBalanceStrategyTest {

    @Test
    fun `it checks that strategy generates not the same value`() {
        val strategy = RandomBalanceStrategy()

        val values = (1..10)
            .map { strategy.get(10) }
            .distinct()

        assertTrue(values.size > 1)
    }
}
