package ru.serdyuk.load_balancer.strategies

import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RoundRobinBalanceStrategyTest {

    @Test
    fun `it checks that values generated proporcial`() {
        val strategy = RoundRobinBalanceStrategy()

        val values = (1..1000)
            .map { strategy.get(10) }
            .groupBy { it }
            .map { it.key to it.value.size }
            .toMap()

        val expected = (0..9).associateWith { 100 }

        assertTrue(expected == values)
    }

    @Test
    fun `it checks that values generated sequantly`() {
        val strategy = RoundRobinBalanceStrategy()

        val values = (1..10).map { strategy.get(10) }

        val expected = (0..9).map { it }

        assertIterableEquals(expected, values)
    }
}
