package ru.serdyuk.load_balancer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import ru.serdyuk.load_balancer.exceptions.NoAnyAvailableProvider
import ru.serdyuk.load_balancer.exceptions.RegistrationProviderException
import ru.serdyuk.load_balancer.exceptions.UnregistrationProviderException
import ru.serdyuk.load_balancer.strategies.RandomBalanceStrategy
import ru.serdyuk.providers.FirstProvider
import ru.serdyuk.providers.SecondProvider

private const val UNKNOWN_PROVIDER_ID = "test"

internal class LoadBalancerTest {
    @Test
    fun `it checks amount are produced by providers values`() {
        val loadBalancer = LoadBalancer(RandomBalanceStrategy())
        loadBalancer.registerProvider(FirstProvider())
        loadBalancer.registerProvider(SecondProvider())

        val result = (1..100)
            .asSequence()
            .map { loadBalancer.get() }
            .toList()

        assertEquals(100, result.size)
    }

    @Test
    fun `it checks throwing an error if no any registered providers during a getting value`() {
        val loadBalancer = LoadBalancer(RandomBalanceStrategy())

        assertThrows(NoAnyAvailableProvider::class.java) { loadBalancer.get() }
    }

    @Test
    fun `it checks throwing an error if too many registered providers`() {
        val loadBalancer = LoadBalancer(RandomBalanceStrategy())

        assertThrows(RegistrationProviderException::class.java) { repeat(DEFAULT_MAX_PROVIDER_NUMBER + 1) { loadBalancer.registerProvider(FirstProvider()) } }
    }

    @Test
    fun `it checks throwing an error if provider wasn't found during unregistration`() {
        val loadBalancer = LoadBalancer(RandomBalanceStrategy())

        assertThrows(UnregistrationProviderException::class.java) { loadBalancer.unregisterProvider(UNKNOWN_PROVIDER_ID) }
    }

    //todo: 1) Test about randomizing values 2) Halthcheck test 3) other...
}
