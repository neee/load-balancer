package ru.serdyuk.load_balancer

import kotlin.concurrent.thread
import kotlin.time.Duration

class HealthChecker(
    private val interval: Duration,
    private val loadBalancer: LoadBalancer,
) {
    fun run() {
        thread {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(interval.inWholeMilliseconds)
                    healthcheck()
                } catch (_: InterruptedException) {}
            }
        }
    }

    fun healthcheck() {
        val iterator = loadBalancer.providers.iterator()
        while (iterator.hasNext()) {
            val provider = iterator.next()
            if (!provider.check()) {
                iterator.remove()
            }
        }
    }
}