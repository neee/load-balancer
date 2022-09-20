package ru.serdyuk.load_balancer

import ru.serdyuk.load_balancer.exceptions.NoAnyAvailableProvider
import ru.serdyuk.load_balancer.exceptions.RegistrationProviderException
import ru.serdyuk.load_balancer.exceptions.UnregistrationProviderException
import ru.serdyuk.load_balancer.strategies.BalanceStrategy
import ru.serdyuk.providers.Provider
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger

const val DEFAULT_MAX_PROVIDER_NUMBER = 10
private const val MAX_PROVIDERS_NUMBER_REACHED_EXCEPTION_MESSAGE = "Max providers number reached (%d)"
private const val TOO_MANY_PROVIDERS_FOR_REGISTRATION_MESSAGE = "Too many providers for registration, available %d"
private const val NO_ANY_REGISTERED_PROVIDER_MESSAGE = "No any registered provider"
private const val PROVIDER_WITH_ID_NOT_FOUND_MESSAGE = "Provider with id = %s not found"

class LoadBalancer(
    private val strategy: BalanceStrategy,
    private val healthChecker: HealthChecker = HealthChecker(Duration.ofSeconds(10)),
    private val maxProvidersNumber: Int = DEFAULT_MAX_PROVIDER_NUMBER,
) {
    private var semaphore: Semaphore = Semaphore(0)
    val providers: MutableList<Provider> = CopyOnWriteArrayList()

    fun registerProvider(newProvider: Provider) {
        registerProviders(listOf(newProvider))
    }

    fun registerProviders(newProviders: Collection<Provider>) {
        if (providers.size >= maxProvidersNumber) {
            throw RegistrationProviderException(MAX_PROVIDERS_NUMBER_REACHED_EXCEPTION_MESSAGE.format(maxProvidersNumber))
        } else if (providers.size + newProviders.size > maxProvidersNumber) {
            throw RegistrationProviderException(TOO_MANY_PROVIDERS_FOR_REGISTRATION_MESSAGE.format(maxProvidersNumber - providers.size))
        } else {
            providers.addAll(newProviders)
            strategy.setProvidersNumber(AtomicInteger(providers.size))
            semaphore.release(providers.sumOf { it.concurrentLevel() })
            healthChecker.run(this)
        }
    }

    fun unregisterProvider(providerId: String) {
        if (providers.isEmpty()) {
            throw UnregistrationProviderException(NO_ANY_REGISTERED_PROVIDER_MESSAGE)
        }
        val provider = providers.firstOrNull { it.getId() == providerId }
            ?: throw UnregistrationProviderException(PROVIDER_WITH_ID_NOT_FOUND_MESSAGE.format(providerId))

        semaphore.release(provider.concurrentLevel())
        providers.remove(provider)
    }

    fun get(): String =
        if (semaphore.tryAcquire()) {
            val providerNumber = strategy.get()
            val provider = providers[providerNumber]
            val value = provider.get()
            semaphore.release()
            value
        } else {
            throw NoAnyAvailableProvider("Reached requests limit")
        }
}
