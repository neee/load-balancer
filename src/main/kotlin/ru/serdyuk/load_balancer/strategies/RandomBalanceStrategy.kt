package ru.serdyuk.load_balancer.strategies

import kotlin.random.Random

class RandomBalanceStrategy() : AbstractBalanceStrategy() {
    override fun getProviderNumber(): Int = Random.nextInt(this.getProvidersNumber().get())
}
