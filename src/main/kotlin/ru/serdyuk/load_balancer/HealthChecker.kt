package ru.serdyuk.load_balancer

import ru.serdyuk.providers.Provider
import java.time.Duration
import kotlin.concurrent.thread

private const val MIN_AVAILABILITY_CHECK = 1

class HealthChecker(private val interval: Duration) {
    private val unavailableProviders: MutableMap<String, ProviderAvailability> = HashMap()
    private var started = false

    fun run(loadBalancer: LoadBalancer) {
        if (!started) started = true else return
        thread {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(interval.toMillis())
                    check(loadBalancer)
                } catch (_: InterruptedException) {
                }
            }
        }
    }

    private fun check(loadBalancer: LoadBalancer) {
        val providers = loadBalancer.providers + unavailableProviders.values.map { it.provider }
        val (available, unavailable) = providers.partition {
            try {
                it.check()
            } catch (e: Exception) {
                false
            }
        }

        // Disable inactive providers
        unavailable.forEach {
            loadBalancer.unregisterProvider(it.getId())
            unavailableProviders[it.getId()] = ProviderAvailability(it, 0)
        }

        // Re-activate providers if
        available.forEach {
            val providerAvailability = unavailableProviders[it.getId()]
            if (providerAvailability != null) {
                if (providerAvailability.availabilityCounter == MIN_AVAILABILITY_CHECK) {
                    loadBalancer.registerProvider(it)
                    unavailableProviders.remove(it.getId())
                } else {
                    providerAvailability.availabilityCounter++
                }
            }
        }
    }
}

data class ProviderAvailability(val provider: Provider, var availabilityCounter: Int)
