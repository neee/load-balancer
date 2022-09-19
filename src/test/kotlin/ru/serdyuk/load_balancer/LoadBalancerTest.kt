package ru.serdyuk.load_balancer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.serdyuk.load_balancer.strategies.RandomBalanceStrategy
import ru.serdyuk.providers.FirstProvider
import ru.serdyuk.providers.SecondProvider

internal class LoadBalancerTest {
    @Test
    fun getProviders() {
        val loadBalancer = LoadBalancer(RandomBalanceStrategy())
        loadBalancer.registerProvider(FirstProvider())
        loadBalancer.registerProvider(SecondProvider())

        val result = (1..100)
            .asSequence()
            .map { loadBalancer.get() }
            .toList()

        assertEquals(100, result.size)
    }
}
