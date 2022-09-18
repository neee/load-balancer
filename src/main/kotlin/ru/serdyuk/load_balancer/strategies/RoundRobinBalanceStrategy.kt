package ru.serdyuk.load_balancer.strategies

class RoundRobinBalanceStrategy() : AbstractBalanceStrategy() {
    private var value = 0

    override fun getProviderNumber(): Int {
        if (value >= this.getProvidersNumber()) {
            value = 0
        }
        val providerNumber = value
        value++

        return providerNumber
    }
}
