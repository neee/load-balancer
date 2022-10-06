package ru.serdyuk.load_balancer.strategies

import kotlin.random.Random

class RandomBalanceStrategy : BalanceStrategy {
    override fun get(providersCount: Int): Int = Random.nextInt(providersCount)
}
